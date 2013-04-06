package de.novensa.techniques.maven.plugin.web.as.WebSphere;

/**
 * Here the plugin will find all kinds of error messages needed while processing the goal.
 *
 * @author Daniel Schulz
 */
public interface ErrorMessages {

    static final String WEBSPHERE_HOME_WAS_NOT_FOUND = "The WebSphere´s home directory was declared " +
            "being '%s'. Unfortunately this path cannot be found in the location provided. Please make sure this is " +
            "a valid path in your OS and can be recognized by your JVM currently running maven. The example files " +
            "may be helpful in solving this issue.";

    static final String WEBSPHERE_HOME_IS_NOT_PROVIDED = "The field wsHome´s value was detected being null. " +
            "This indicates it has not been provided or a typo occured in it´s declaration. Please make sure the " +
            "field <wsHome>directory</wsHome> is pointing to the WebSphere´s installation directory and can " +
            "be recognized being a valid Java path in your OS or vendor´s JVM. You find help in the example " +
            "configuration provided with the plugin itself.";
}
