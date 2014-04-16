package de.novensa.techniques.maven.plugin.web.as.WebSphere.Runtime;

/**
 * Delivers constants for the Runtime and initialization of the plugin.
 *
 * @author Daniel Schulz
 */
public class Constants implements RuntimeData {

    // plugin-internal, technical constants
    public static final int SMALL_LIST_INIT_SIZE = 32;

    // OS vendors
    public static final String WIN_OS = "Windows";

    // script location
    public static final String SCRIPT_LOCATION_WITHIN_WS_HOME = "bin" + FILE_SEPARATOR + "clearClassCache.%s";
    // the generic file extension may be replaced with fitting the UNIX_AND_WINDOWS_SCRIPT_EXTENSIONS entry according
    public static final String[] UNIX_AND_WINDOWS_SCRIPT_EXTENSIONS = {"sh", "bat"};
}
