package de.novensa.techniques.maven.plugin.web.as.WebSphere;

import de.novensa.techniques.maven.plugin.web.as.WebSphere.Enums.WebSphereVersion;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.FileUtils.ExtractEffectivePaths;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.WebSphereVersionUtils.WebSphereVersionUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

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
    @Parameter (defaultValue = "${project.webSphere.homeDirectory}", property = "wsHome", required = true)
    private File wsHome;

    /**
     * The version string for the WebSphere application server. In case it was not supplied the most likely setting
     * will be used.
     */
    @Parameter (defaultValue = "${project.webSphere.version}", property = "wsVersion", required = false)
    private String rawWsVersion;

    private WebSphereVersion wsVersion = null;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        // prior detecting the WebSphere version -- this detecting may be defined when looking at the
        // home directory structure in the upcoming phase to get the effective path
        wsVersion = WebSphereVersionUtils.getWebSphereVersion(rawWsVersion);
        if (null == wsVersion) {
            getLog().warn(String.format(ErrorMessages.WEB_SPHERE_VERSION_WAS_NOT_FOUND, rawWsVersion));
        }


        try {
            wsHome = ExtractEffectivePaths.getWsHome(wsHome, wsVersion);
        } catch (IOException e) {
            getLog().error(e.fillInStackTrace());
        }

        if (doesWsHomeExists()) {

        } else {
            // wsHome cannot be null: iff so the 'doesWsHomeExists' reported an error and interrupted the
            // running execution
            reportError(String.format(WEB_SPHERE_HOME_WAS_NOT_FOUND, wsHome));
        }
    }


    /**
     * Make sure the WebSphere´s home directory is being found and can be read and written to.
     *
     * @return True iff everything is in order; false otherwise.
     */
    private boolean doesWsHomeExists() throws MojoExecutionException, MojoFailureException {
        if (null == wsHome) {
            reportError(WEB_SPHERE_HOME_IS_NOT_PROVIDED);
            return false;
        }

        if (wsHome.exists()) {

        }

        // correctly passed all tests
        return true;
    }

    private final void reportError(final String errorMessage)
            throws MojoExecutionException, MojoFailureException {

        getLog().error(errorMessage);
    }
}
