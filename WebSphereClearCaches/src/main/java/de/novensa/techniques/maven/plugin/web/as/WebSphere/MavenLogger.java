package de.novensa.techniques.maven.plugin.web.as.WebSphere;

import de.novensa.techniques.maven.plugin.web.as.WebSphere.Enums.LogLvl;
import org.apache.maven.plugin.AbstractMojo;

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
     */
    public final void log(final Throwable throwable) {
        getLog().error(throwable);
    }


    /**
     * Writes the <code>mavenMessage</code> according to the <code>LogLvl</code> out to the maven log.
     *
     * @param lvl The type of message: info, warn, debug, error
     * @param mavenMessage The message details to write
     */
    public final void log(final LogLvl lvl, final String mavenMessage) {

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
                break;

            default:
                getLog().info(mavenMessage);
        }
    }
}
