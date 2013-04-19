package de.novensa.techniques.maven.plugin.web.as.WebSphere;

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
     * Tests the existance of the WebSphere home.
     *
     * @throws Exception Default JUnit assertion exception
     */
    public void testWasHome() throws Exception {
        assertTrue(mojo.isWsHomeDurable());
    }
}
