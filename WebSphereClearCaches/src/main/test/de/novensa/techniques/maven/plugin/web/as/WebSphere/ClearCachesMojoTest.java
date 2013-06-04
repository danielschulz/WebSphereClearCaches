package de.novensa.techniques.maven.plugin.web.as.WebSphere;

import de.novensa.techniques.maven.plugin.web.as.WebSphere.utils.enums.LogLvl;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * This tests the main class of the plugin.
 *
 * @author Daniel Schulz
 */
@SuppressWarnings({"UnusedDeclaration", "JavaDoc"})
@Mojo(name = "clearCaches", defaultPhase = LifecyclePhase.INSTALL)
public class ClearCachesMojoTest extends MasterTestCase {

    /**
     * Tests the existence of the WebSphere home.
     *
     * @throws Exception Default JUnit assertion exception
     */
    public void testWasHome() throws Exception {
        assertTrue(mojo.isWsHomeDurable());
    }


    /**
     * Tests the logger.
     *
     * @throws Exception Default JUnit assertion exception
     */
    public void testLogger() throws Exception {
        mojo.log(LogLvl.DEBUG, "debug level: log and forget");
        mojo.log(LogLvl.INFO, "debug level: log and forget");
        mojo.log(LogLvl.WARN, "debug level: log and forget");
        // on log level error we have to end up in the catch root on executing the first line
        try {
            mojo.log(LogLvl.ERROR, "error level: log and forget");
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}
