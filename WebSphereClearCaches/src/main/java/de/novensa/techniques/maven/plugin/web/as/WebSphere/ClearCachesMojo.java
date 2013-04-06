package de.novensa.techniques.maven.plugin.web.as.WebSphere;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * This class is responsible for clearing th temp caches in WebSphere Application Servers.
 *
 * @author Daniel Schulz
 */
@Mojo (name = "clearCaches", defaultPhase = LifecyclePhase.INSTALL)
public class ClearCachesMojo extends AbstractMojo implements RuntimeData, ErrorMessages {


    /**
     * The location the WebSphere is installed to.
     */
    @Parameter (defaultValue = "${project.build.sourceDirectory}", property = "wsHome", required = true)
    private File wsHome;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        if (doesWsHomeExists()) {

        } else {
            // wsHome cannot be null: iff so the 'doesWsHomeExists' reported an error and interrupted the
            // running execution
            reportError(String.format(WEBSPHERE_HOME_WAS_NOT_FOUND, wsHome));
        }
    }

    /**
     * Make sure the WebSphere´s home directory is being found and can be read and written to.
     *
     * @return True iff everything is in order; false otherwise.
     */
    private boolean doesWsHomeExists() throws MojoExecutionException, MojoFailureException {
        if (null == wsHome) {
            reportError(WEBSPHERE_HOME_IS_NOT_PROVIDED);
            return false;
        }


        // correctly passed all tests
        return true;
    }

    private static final void reportError(final String errorMessage)
            throws MojoExecutionException, MojoFailureException {


    }
}
