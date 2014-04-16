package de.novensa.techniques.maven.plugin.web.as.WebSphere.runtime;

import de.novensa.techniques.maven.plugin.web.as.WebSphere.utils.enums.LogLvl;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * This class helps write messages according to type out to the maven log.
 *
 * @author Daniel Schulz
 */
public abstract class MavenLogger extends AbstractMojo {


    /**
     * Writes the exception details as error message to the log.
     *
     * @param throwable The exception caught
     * @throws MojoExecutionException This exception will be thrown when there is an exception regarding the runtime
     *                                of the plugin
     * @throws MojoFailureException This exception will be thrown when there is an exception regarding the runtime
     *                              of the plugin
     */
    public final void log(final Throwable throwable) throws MojoExecutionException, MojoFailureException {
        getLog().error(throwable);
        throw new MojoExecutionException(throwable.getMessage());
    }


    /**
     * Writes the <code>mavenMessage</code> according to the <code>LogLvl</code> out to the maven log.
     *
     * @param lvl The type of message: info, warn, debug, error
     * @param mavenMessage The message details to write
     * @throws MojoExecutionException This exception will be thrown when there is an exception regarding the runtime
     *                                of the plugin
     * @throws MojoFailureException This exception will be thrown when there is an exception regarding the runtime
     *                              of the plugin
     */
    public final void log(final LogLvl lvl, final String mavenMessage)
            throws MojoExecutionException, MojoFailureException {

        switch (lvl) {
            case INFO:
                getLog().info(mavenMessage);
                break;

            case WARN:
                getLog().warn(mavenMessage);
                break;

            case DEBUG:
                getLog().debug(mavenMessage);
                break;

            case ERROR:
                getLog().error(mavenMessage);
                throw new MojoFailureException(mavenMessage);

            default:
                getLog().info(mavenMessage);
        }
    }
}
