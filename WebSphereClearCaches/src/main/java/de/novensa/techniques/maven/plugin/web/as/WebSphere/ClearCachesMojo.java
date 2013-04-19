package de.novensa.techniques.maven.plugin.web.as.WebSphere;

import com.google.common.base.Joiner;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.runtime.*;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.utils.Enums.LogLvl;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.utils.Enums.WebSphereVersion;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.utils.FileUtils.ExtractEffectivePaths;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.utils.FileUtils.WebSphereVersionDependingPathsWithinWsHome;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.utils.WebSphereVersionUtils.WebSphereVersionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.novensa.techniques.maven.plugin.web.as.WebSphere.runtime.Constants.WIN_OS;
import static de.novensa.techniques.maven.plugin.web.as.WebSphere.utils.Enums.LogLvl.*;

/**
 * This class is responsible for clearing th temp caches in WebSphere Application Servers. The procedure is based on the IBM document
 * http://www-01.ibm.com/support/docview.wss?uid=swg21460859 ,
 * http://www-01.ibm.com/support/docview.wss?uid=swg21607887 , and
 * consulting the mentioned script inside my WebSphere instance. This plugin helps in automating these tasks in your maven build chain.
 *
 * @author Daniel Schulz
 */
@SuppressWarnings({"UnusedDeclaration", "JavaDoc"})
@Mojo(name = "clearCaches", defaultPhase = LifecyclePhase.INSTALL)
public class ClearCachesMojo extends MavenLogger implements RuntimeData, ErrorMessages {

    /**
     * The location the WebSphere is installed to.
     *
     * @parameter property="wsHome"
     * @required
     */
    @Parameter (property = "wsHome", defaultValue = "wsHome", required = true)
    private File wsHome;

    /**
     * The version string for the WebSphere application server. In case it was not supplied the most likely setting
     * will be used.
     *
     * @parameter property="wsVersion"
     */
    @Parameter (property = "wsVersion", defaultValue = "wsVersion")
    private String wsVersion;


    /**
     * This defines the name of your WebSphere´s AppServer profile. This is a technical value derived from IBM
     * application server techniques.
     *
     * @parameter property="appServerProfile"
     * @required
     */
    @Parameter (property = "appServerProfile", defaultValue = "appServerProfile", required = true)
    private String appServerProfile;

    /**
     * This defines the name of your WebSphere´s AppServer itself. Whereas the AppServer profile has another field to
     * be declared.  This is a technical value derived from IBM application server techniques.
     *
     * @parameter property="appServer"
     * @required
     */
    @Parameter (property = "appServer", defaultValue = "appServer", required = true)
    private String appServer;

    /**
     * This defines the cell´s name of your AppServer. This is a technical value derived from IBM application
     * server techniques.
     *
     * @parameter property="cell"
     * @required
     */
    @Parameter (property = "cell", defaultValue = "cell", required = true)
    private String cell;

    /**
     * This defines the cell´s node name of your AppServer. This is a technical value derived from IBM application
     * server techniques.
     *
     * @parameter property="node"
     * @required
     */
    @Parameter (property = "node", defaultValue = "node", required = true)
    private String node;


    // technical members
    private List<File> persistedFiles = new ArrayList<File>(Constants.SMALL_LIST_INIT_SIZE);
    private int cleanedCount = 0;
    private int filesToCleanCount = 0;
    private boolean ranTheScript = false;
    private boolean scriptShallRun = true;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        // just warn and stop on real problem -- some missing values may not cause the task to be impossible and may
        // be learned to solve on runtime in future versions
        if (null == wsHome || null == wsVersion ||
                null == appServerProfile || null == appServer || null == cell || null == node) {
            log(WARN, PLUGIN_REQUIRED_FIELDS_NOT_PROVIDED);
        }


        // real problem that will case the plugin to terminate
        if (null == wsHome) {
            log(ERROR, WEB_SPHERE_HOME_IS_NOT_PROVIDED);
        }

        if (null == wsVersion) {
            log(ERROR, WEB_SPHERE_VERSION_IS_EMPTY);
        }


        // prior detecting the WebSphere version -- this detecting may be defined when looking at the
        // home directory structure in the upcoming phase to get the effective path
        WebSphereVersion wsParsedVersion = WebSphereVersionUtils.getWebSphereVersion(wsVersion);
        if (null == wsParsedVersion) {
            log(ERROR, String.format(WEB_SPHERE_VERSION_WAS_NOT_FOUND, wsVersion));
        }


        String wsHomeCanonical = null;
        // figure out the WebSphere home directory
        // if necessary the wsParsedVersion will be corrected here -- but shall be rather seldom the case and there will
        // occur an info log iff done so
        try {
            wsHome = ExtractEffectivePaths.getWsHome(wsHome, wsParsedVersion);
            wsHomeCanonical = wsHome.getCanonicalPath();
        } catch (IOException e) {
            log(e.fillInStackTrace());
        }

        if (null != wsHomeCanonical && isWsHomeDurable()) {
            final String[] listOfCleaningItems =
                    (new WebSphereVersionDependingPathsWithinWsHome(wsParsedVersion, appServer, cell, node))
                            .getPathsWithinWsHome();

            final String locationsRelSteam =
                    FILE_SEPARATOR + "profiles" + FILE_SEPARATOR + appServerProfile + FILE_SEPARATOR;
            final String wsHomeProfileStem =
                    wsHomeCanonical +
                    locationsRelSteam;// location´s relative stem -- what does not change for any cleaning item

            final File wsHomeProfileStemFile = new File(wsHomeProfileStem);
            if (!wsHomeProfileStemFile.exists()) {
                throw new IllegalStateException(String.format(ErrorMessages.PROFILES_DIRECTORY_DOES_NOT_EXIST,
                        appServerProfile, locationsRelSteam));
            }

            for (String cleaningItem : listOfCleaningItems) {
                processFile(wsHomeProfileStem + cleaningItem);
            }


            // run the clear classes script if wished
            if (scriptShallRun) {
                runTheScript(wsHomeProfileStem);
            }
        }


        mavenSummary();
    }

    private void runTheScript(final String scriptLocationProfileStem)
            throws MojoFailureException, MojoExecutionException {

        final File scriptLocation;
        // if is Linux system (check not to be Windows): true; otherwise iff is "Windows" something then false
        final boolean unixFileStyle = null != OS_NAME && !OS_NAME.contains(WIN_OS);

        scriptLocation = new File(scriptLocationProfileStem +
                WebSphereVersionDependingPathsWithinWsHome.getScriptLocation(unixFileStyle));

        runNativeFile(scriptLocation);
    }


    /**
     * Run the script given. In case of an failure abort the processing and print out the salient exception.
     *
     * @param file The reference to the script to run
     * @throws MojoExecutionException This exception will be thrown when there is an exception regarding the runtime
     *                                of the plugin
     * @throws MojoFailureException This exception will be thrown when there is an exception regarding the runtime
     *                              of the plugin
     */
    private void runNativeFile(final File file) throws MojoFailureException, MojoExecutionException {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(file.getCanonicalPath());
            ranTheScript = true;
        } catch (IOException e) {
            ranTheScript = false;
            log(WARN, SCRIPT_HAS_NOT_BEEN_EXECUTED);
            log(e);
        }
    }


    /**
     * This method summarizes the runtime in the maven log. There will be information about cleaned files and whether
     * the clearClasses script ran or not.
     *
     * @throws MojoExecutionException This exception will be thrown when there is an exception regarding the runtime
     *                                of the plugin
     * @throws MojoFailureException This exception will be thrown when there is an exception regarding the runtime
     *                              of the plugin
     */
    private void mavenSummary() throws MojoFailureException, MojoExecutionException {

        // make the heading summary
        final StringBuilder result = new StringBuilder();
        if (ranTheScript) {
            result.append(String.format(InfoMessages.MAVEN_SUMMARY_SCRIPT_RAN, cleanedCount, filesToCleanCount));
        } else {
            result.append(String.format(InfoMessages.MAVEN_SUMMARY_SCRIPT_FAILED, cleanedCount, filesToCleanCount));
        }

        if (null != persistedFiles) {
            // possibly add details about persisted files iff there are some
            if (persistedFiles.size() > 0) {

                final String separator = LINE_BREAK + TABULATOR + TABULATOR + TABULATOR;
                Joiner joiner = Joiner.on(separator);
                result.append(separator).append(joiner.join(persistedFiles));
            }
        }

        // ranTheScript iff scriptShallRun
        if (ranTheScript || !scriptShallRun) {
            log(LogLvl.INFO, result.toString());
        } else {
            log(LogLvl.WARN, result.toString());
        }
    }


    /**
     * Takes care of the individual files. Makes sure the are existing, can be read, and written. The path may
     * contain an indicator to delete all files within. Therefore this ending from
     * <code>RuntimeData.ANY_FILES_WITHIN</code> must be dropped with the method <code>dropEndingString</code>.
     *
     * @param path The current complete file string to the folder (make sure to drop the ending to take any files
     *             within if it is present)
     */
    private void processFile(final String path) throws MojoFailureException, MojoExecutionException {

        final File effectivePath = new File(dropEndingString(path, ANY_FILES_WITHIN));

        if (!effectivePath.exists()) {
            // look for the parent to exist
            possiblyReportNotExistingParent(effectivePath.getParentFile());

            // only iff parent was there -- else this is an exception and we are not here any more
            log(INFO, String.format(DIRECTORY_DOES_NOT_EXIST, effectivePath));
            cleanedCount++;
            filesToCleanCount++;
            return;
        }

        if (path.endsWith(ANY_FILES_WITHIN)) {
            // clear the folder items only
            if (effectivePath.exists()) {
                final File[] files = effectivePath.listFiles();
                if (null != files) {
                    // check for existing parent
                    possiblyReportNotExistingParent(effectivePath);

                    filesToCleanCount += files.length;

                    for (File file : files) {
                        cleanFile(file);
                    }
                }
            } else {
                log(ERROR, String.format(DIRECTORY_DOES_NOT_EXIST, effectivePath));
            }
        } else {
            // check for existing parent
            possiblyReportNotExistingParent(effectivePath.getParentFile());

            // clear the whole folder
            filesToCleanCount++;
            cleanFile(effectivePath);
        }
    }

    /**
     * Checks the file to be an existing directory. If not an <code>IllegalStateException</code> exception will
     * be thrown.
     *
     * @param effectivePath The reference to the directory to look for
     */
    private void possiblyReportNotExistingParent(final File effectivePath) {
        if (!effectivePath.exists() || !effectivePath.isDirectory()) {
            throw new IllegalStateException(String.format(ErrorMessages.DIRECTORY_DOES_NOT_EXIST, effectivePath));
        }
    }


    /**
     * Deletes the file referred and keeps track on the success rate counter <code>cleanedCount</code>. Cleans files
     * and directories as well. Checks the privileges and logs or aborts runtime if necessary.
     *
     * @param file The file or directory to delete
     * @throws MojoExecutionException This exception will be thrown when there is an exception regarding the runtime
     *                                of the plugin
     * @throws MojoFailureException This exception will be thrown when there is an exception regarding the runtime
     *                              of the plugin
     */
    private void cleanFile(final File file) throws MojoFailureException, MojoExecutionException {
        // check permissions
        if (!file.canWrite()) {
            log(ERROR, String.format(DIRECTORY_CANNOT_BE_WRITTEN, file));
        }

        // is still or just clean
        if (!file.exists()) {
            log(INFO, String.format(RESOURCE_IS_STILL_OR_JUST_CLEAN, file));

            // if the file does not exist anymore it shall not appear in the not-able-to-clean list summarizing the
            // execution
            cleanedCount++;
            return;
        }


        // delete when everything is fine
        if (forceDeleteResource(file)) {
            // record successful cleaned file
            cleanedCount++;
        } else {
            // when the file was not deleted
            log(WARN, String.format(RESOURCE_CANNOT_BE_CLEANED_BUT_PRIVILEGES_GRANTED, file));
            persistedFiles.add(file);
        }
    }

    /**
     * This forces the deletion of the file. Safe-delete with check returned.
     *
     * @param file The reference to the file to delete
     * @return true iff the file was successfully deleted and does not exist anymore
     * @throws MojoExecutionException This exception will be thrown when there is an exception regarding the runtime
     *                                of the plugin
     * @throws MojoFailureException This exception will be thrown when there is an exception regarding the runtime
     *                              of the plugin
     */
    private boolean forceDeleteResource(final File file) throws MojoFailureException, MojoExecutionException {
        if (file.isDirectory()) {
            // directories
            try {
                FileUtils.deleteDirectory(file);
            } catch (IOException e) {
                return false;
            }
            // safe-check
            return !file.exists();
        } else {
            // files
            // safe-check
            return file.delete();
        }
    }


    /**
     * Cuts the <code>endingString</code> form the <code>baseString</code>. If either one is null or both of the two
     * are null the base string will be returned even iff null. The same will happen if the <code>endingString</code>
     * is not the last part of the <code>baseString</code>.
     *
     * @param baseString   The base string ending with the endingString
     * @param endingString The suffix of the baseString
     * @return The baseString without the endingString; e.g.
     *         dropEndingString("Albert Einstein", "Einstein") -> "Albert ")
     */
    public static String dropEndingString(final String baseString, final String endingString) {
        if (null != baseString && null != endingString && baseString.endsWith(endingString)) {
            return baseString.substring(0, baseString.length() - endingString.length());
        } else {
            return baseString;
        }
    }


    /**
     * Make sure the WebSphere´s home directory is being found and can be read and written to.
     *
     * @return True iff everything is in order; false otherwise.
     */
    protected boolean isWsHomeDurable() throws MojoExecutionException, MojoFailureException {
        if (null == wsHome) {
            log(ERROR, WEB_SPHERE_HOME_IS_NOT_PROVIDED);
            return false;
        }

        // looks for an existing WebSphere home directory
        if (!wsHome.exists()) {
            log(ERROR, String.format(WEB_SPHERE_HOME_WAS_NOT_FOUND, wsHome));
            return false;
        }

        if (!wsHome.canRead()) {
            log(ERROR, String.format(DIRECTORY_CANNOT_BE_READ, wsHome));
            return false;
        }

        // correctly passed all tests
        return true;
    }


    // constructors
    public ClearCachesMojo() {
    }


    // getters / setters
    public File getWsHome() {
        return wsHome;
    }

    public void setWsHome(final File wsHome) {
        this.wsHome = wsHome;
    }

    public void setWsHome(final String wsHome) {
        this.wsHome = new File(wsHome);
    }


    public String getWsVersion() {
        return wsVersion;
    }

    public void setWsVersion(final String wsVersion) {
        this.wsVersion = wsVersion;
    }

    public String getAppServerProfile() {
        return appServerProfile;
    }

    public void setAppServerProfile(final String appServerProfile) {
        this.appServerProfile = appServerProfile;
    }

    public String getAppServer() {
        return appServer;
    }

    public void setAppServer(final String appServer) {
        this.appServer = appServer;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(final String cell) {
        this.cell = cell;
    }

    public String getNode() {
        return node;
    }

    public void setNode(final String node) {
        this.node = node;
    }
}
