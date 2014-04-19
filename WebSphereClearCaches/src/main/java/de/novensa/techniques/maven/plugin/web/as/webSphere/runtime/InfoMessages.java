package de.novensa.techniques.maven.plugin.web.as.webSphere.runtime;

/**
 * Here the plugin will find all kinds of information messages needed while processing the goal.
 *
 * @author Daniel Schulz
 */
public interface InfoMessages {

    static String MAVEN_SUMMARY_SCRIPT_RAN = "The WebSphere caches were cleaned successfully. There were " +
            "%s of %s files cleaned and the script was successfully executed.";
    // first %s: cleaned files c
    // second %s: amount of files to clean n, where n >= c

    static String MAVEN_SUMMARY_SCRIPT_FAILED = "The WebSphere caches were cleaned successfully. There were " +
            "%s of %s files cleaned but the script was not executed.";
    // first %s: cleaned files c
    // second %s: amount of files to clean n, where n >= c

    static final String SUMMARY_FILES_NOT_DELETED = "These files could not be deleted:";
}
