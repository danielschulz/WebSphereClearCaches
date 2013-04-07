package de.novensa.techniques.maven.plugin.web.as.WebSphere;

import de.novensa.techniques.maven.plugin.web.as.WebSphere.Enums.LogLvl;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.Enums.WebSphereVersion;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.FileUtils.ExtractEffectivePaths;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.FileUtils.WebSphereVersionDependingPathsWithinWsHome;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.WebSphereVersionUtils.WebSphereVersionUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

import static de.novensa.techniques.maven.plugin.web.as.WebSphere.Enums.LogLvl.ERROR;
import static de.novensa.techniques.maven.plugin.web.as.WebSphere.Enums.LogLvl.WARN;

/**
 * This class is responsible for clearing th temp caches in WebSphere Application Servers. The procedure is based on the IBM document
 *  http://www-01.ibm.com/support/docview.wss?uid=swg21460859 ,
 *  http://www-01.ibm.com/support/docview.wss?uid=swg21607887 , and
 * consulting the mentioned script inside my WebSphere instance. This plugin helps in automating these tasks in your maven build chain.
 *
 * @author Daniel Schulz
 */
@SuppressWarnings("UnusedDeclaration")
@Mojo (name = "clearCaches", defaultPhase = LifecyclePhase.INSTALL)
public class ClearCachesMojo extends AbstractMojo implements RuntimeData, ErrorMessages {


    /**
     * The location the WebSphere is installed to.
     */
    @Parameter (defaultValue = "${project.webSphere.homeDirectory}", property = "wsHome", required = true)
    private File wsHome;

    /**
     * The version string for the WebSphere application server. In case it was not supplied the most likely setting
     * will be used.
     */
    @Parameter (defaultValue = "${project.webSphere.version}", property = "wsVersion", required = false)
    private String rawWsVersion;


    /**
     * This defines the name of your WebSphere´s AppServer profile. This is a technical value derived from IBM
     * application server techniques.
     */
    @Parameter (defaultValue = "${project.webSphere.appServerProfile}", property = "appServerProfile", required = true)
    private String appServerProfile;

    /**
     * This defines the name of your WebSphere´s AppServer itself. Whereas the AppServer profile has another field to
     * be declared.  This is a technical value derived from IBM application server techniques.
     */
    @Parameter (defaultValue = "${project.webSphere.appServer}", property = "appServer", required = true)
    private String appServer;

    /**
     * This defines the cell´s name of your AppServer. This is a technical value derived from IBM application
     * server techniques.
     */
    @Parameter (defaultValue = "${project.webSphere.cell}", property = "cell", required = true)
    private String cell;

    /**
     * This defines the cell´s node name of your AppServer. This is a technical value derived from IBM application
     * server techniques.
     */
    @Parameter (defaultValue = "${project.webSphere.node}", property = "node", required = true)
    private String node;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        // prior detecting the WebSphere version -- this detecting may be defined when looking at the
        // home directory structure in the upcoming phase to get the effective path
        WebSphereVersion wsVersion = WebSphereVersionUtils.getWebSphereVersion(rawWsVersion);
        if (null == wsVersion) {
            log(WARN, String.format(ErrorMessages.WEB_SPHERE_VERSION_WAS_NOT_FOUND, rawWsVersion));
        }


        // figure out the WebSphere home directory
        // if necessary the wsVersion will be corrected here -- but shall be rather seldom the case and there will
        // occur an info log iff done so
        try {
            wsHome = ExtractEffectivePaths.getWsHome(wsHome, wsVersion);
        } catch (IOException e) {
            log(e.fillInStackTrace());
        }

        if (isWsHomeDurable()) {
            WebSphereVersionDependingPathsWithinWsHome pathsWithinWsHome =
                    new WebSphereVersionDependingPathsWithinWsHome(wsVersion, appServerProfile, appServer, cell, node);
            final String[] listOfCleaningItems = pathsWithinWsHome.getPathsWithinWsHome();
        }
    }


    /**
     * Make sure the WebSphere´s home directory is being found and can be read and written to.
     *
     * @return True iff everything is in order; false otherwise.
     */
    private boolean isWsHomeDurable() throws MojoExecutionException, MojoFailureException {
        if (null == wsHome) {
            log(ERROR, WEB_SPHERE_HOME_IS_NOT_PROVIDED);
            return false;
        }

        // looks for an existing WebSphere home directory
        if (!wsHome.exists()) {
            log(ERROR, String.format(WEB_SPHERE_HOME_WAS_NOT_FOUND, wsHome));
        }

        if (!wsHome.canRead()) {
            log(ERROR, String.format(DIRECTORY_CANNOT_BE_READ, wsHome));
        }

        // correctly passed all tests
        return true;
    }

    public final void log(final Throwable throwable) {
        getLog().error(throwable);
    }

    public final void log(final LogLvl lvl, final String errorMessage)
            throws MojoExecutionException, MojoFailureException {

        switch (lvl) {
            case INFO:
                getLog().info(errorMessage);
                break;

            case WARN:
                getLog().warn(errorMessage);
                break;

            case DEBUG:
                getLog().debug(errorMessage);
                break;

            case ERROR:
                getLog().error(errorMessage);
                break;

            default:
                getLog().info(errorMessage);
        }
    }
}
