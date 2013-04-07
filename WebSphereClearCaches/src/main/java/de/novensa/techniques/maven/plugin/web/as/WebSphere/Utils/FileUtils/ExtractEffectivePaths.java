package de.novensa.techniques.maven.plugin.web.as.WebSphere.utils.FileUtils;

import de.novensa.techniques.maven.plugin.web.as.WebSphere.ClearCachesMojo;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.runtime.RuntimeData;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.utils.Enums.WebSphereVersion;

import java.io.File;
import java.io.IOException;

/**
 * Delivers all methods to extract the effective path from the raw input.
 *
 * @author Daniel Schulz
 */
public abstract class ExtractEffectivePaths implements RuntimeData {


    private static final String WS_HOME_INSTALLATION_FOLDER_NAME = "AppServer";

    /**
     * These are the possible folders developers may provide accidentally. The plugin will quietly fix
     * this issue.
     */
    public static final String[] POSSIBLE_WS_HOME_ENDINGS = {
            WS_HOME_INSTALLATION_FOLDER_NAME + FILE_SEPARATOR + "bin" + FILE_SEPARATOR};


    /**
     * This is the default folder name for the WebSphere server.
     */
    public static final String EXPECTED_WS_HOME_ENDING = WS_HOME_INSTALLATION_FOLDER_NAME + FILE_SEPARATOR;


    /**
     * Any File format like V:\web\server1 will result in a unified format V:\web\server1\ with trailing file separator.
     * @param file The raw file from maven´s pom.xml
     * @return The trailing separator File in unified format
     */
    public static File preProcessRawFileToCommonFormat(final File file) throws IOException {

        final String canonicalPath = file.getCanonicalPath();
        return canonicalPath.endsWith(FILE_SEPARATOR) ? file : new File(canonicalPath + FILE_SEPARATOR);
    }


    /**
     * Replace all accidentally provided folders and file endings with the expected folder name.
     *
     * @param file The file to start the look up from
     * @param version The WebSphere version to get the file structure accurately
     * @return The file path to the WebSphere home directory
     * @throws IOException This will be thrown when there´s something odd with the provided
     * WebSphere home directory path
     */
    public static File getWsHome(File file, WebSphereVersion version) throws IOException {

        // process to the unified format
        file = ExtractEffectivePaths.preProcessRawFileToCommonFormat(file);
        String canonicalPath;

        for (String current : POSSIBLE_WS_HOME_ENDINGS) {

            canonicalPath = file.getCanonicalPath();
            if (canonicalPath.endsWith(current)) {
                file = new File(ClearCachesMojo.dropEndingString(canonicalPath, current) + EXPECTED_WS_HOME_ENDING);
            }
        }

        refineWebSphereVersion(file, version);

        return file;
    }

    @SuppressWarnings("UnusedParameters")
    protected static void refineWebSphereVersion(File file, WebSphereVersion version) {
        // does the file structure match the version?
        // TODO: this can be done in future versions
    }
}
