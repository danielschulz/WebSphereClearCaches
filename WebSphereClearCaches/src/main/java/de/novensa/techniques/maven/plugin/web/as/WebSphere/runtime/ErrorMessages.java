package de.novensa.techniques.maven.plugin.web.as.WebSphere.runtime;

/**
 * Here the plugin will find all kinds of error messages needed while processing the goal.
 *
 * @author Daniel Schulz
 */
public interface ErrorMessages {

    static final String WEB_SPHERE_HOME_WAS_NOT_FOUND = "The WebSphere´s home directory was declared " +
            "being '%s'. Unfortunately this path cannot be found in the location provided. Please make sure this is " +
            "a valid path in your OS and can be recognized by your JVM currently running maven. The example files " +
            "may be helpful in solving this issue.";

    static final String PLUGIN_REQUIRED_FIELDS_NOT_PROVIDED = "This plugin needs some parameter in order to work " +
            "correctly. Please make sure you provide WebSphere´s home directory (wsHome), the version of your " +
            "application server (wsVersion), the application server profile (appServerProfile), the identifier " +
            "(appServer), the cell (cell), and it´s node (node). Either one of them missing will hardly work and " +
            "therefore will cause this plugin to terminate.";

    static final String WEB_SPHERE_HOME_IS_NOT_PROVIDED = "The field wsHome´s value was detected being null. " +
            "This indicates it has not been provided or a typo occured in it´s declaration. Please make sure the " +
            "field <wsHome>directory</wsHome> is pointing to the WebSphere´s installation directory and can " +
            "be recognized being a valid Java path in your OS or vendor´s JVM. You find help in the example " +
            "configuration provided with the plugin itself.";

    static final String WEB_SPHERE_VERSION_WAS_NOT_FOUND = "Unfortunately there was no matching WebSphere version " +
            "found for the string '%s'. If this may be due to a too new version you can ignore this: the plugin will " +
            "take the information to the most close version to find. If the plugin fails this may be the cause. " +
            "The detected WebSphere version may be refined upon parsing the WebSphere´s home directory if needed.";

    static final String WEB_SPHERE_VERSION_IS_EMPTY = "The version of the WebSphere was null. This may be due to a " +
            "lacking configuration of the maven plugin. A consultation of the provided example files shall help " +
            "solving this issue.";

    static final String DIRECTORY_DOES_NOT_EXIST = "The directory '%s' cannot be found.";

    static final String FILE_CANNOT_BE_RETRIEVED_ITS_CANONICAL_PATH_FROM = "The file '%s' cannot be retrieved it´s " +
            "canonical path from.";

    static final String DIRECTORY_CANNOT_BE_READ = "The directory '%s' cannot be read. Please provide read " +
            "privileges to at least this directory. Most likely both read and write privileges for the WebSphere " +
            "home directory will be helpful to succeed.";

    static final String DIRECTORY_CANNOT_BE_WRITTEN = "The directory '%s' cannot be written to. Please provide write " +
            "privileges to at least this directory. Most likely both read and write privileges for the WebSphere " +
            "home directory will be helpful to succeed.";

    static final String RESOURCE_CANNOT_BE_CLEANED_BUT_PRIVILEGES_GRANTED =
            "The resource at '%s' cannot be cleaned. It may be in use because the privileges needed seem to be " +
                    "granted.";

    static final String RESOURCE_IS_STILL_OR_JUST_CLEAN = "the resource '%s' is still or just clean. " +
            "Therefore nothing was done.";

    static final String SCRIPT_HAS_NOT_BEEN_EXECUTED = "The script to clear the class caches has not been run. You " +
            "may run this script on your shell by running '%s'.";
}
