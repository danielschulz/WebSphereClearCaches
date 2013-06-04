package de.novensa.techniques.maven.plugin.web.as.WebSphere;

import de.novensa.techniques.maven.plugin.web.as.WebSphere.utils.localRunner.MasterConfiguration;
import junit.framework.TestCase;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * This tests the main class of the plugin.
 *
 * @author Daniel Schulz
 */
@SuppressWarnings({"UnusedDeclaration", "JavaDoc", "SpellCheckingInspection"})
@Mojo(name = "clearCaches", defaultPhase = LifecyclePhase.INSTALL)
public abstract class MasterTestCase extends TestCase {


    static final ClearCachesMojo mojo = MasterConfiguration.getMasterConfiguration();


    /**
     * JUnit wants even abstract test classes to have at least one test method.
     *
     * @throws Exception Default JUnit assertion exception
     */
    public void testNothing() throws Exception {
    }
}
