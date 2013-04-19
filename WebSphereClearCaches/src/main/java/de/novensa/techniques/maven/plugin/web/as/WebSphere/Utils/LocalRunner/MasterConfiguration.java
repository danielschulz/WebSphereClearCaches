package de.novensa.techniques.maven.plugin.web.as.WebSphere.utils.LocalRunner;

import de.novensa.techniques.maven.plugin.web.as.WebSphere.ClearCachesMojo;

/**
 * This is the local configuration to run this plugin in local mode and to test run it.
 *
 * @author Daniel Schulz
 */
public class MasterConfiguration implements PrivateMasterConfigurationsConstants {


    /**
     * the master configuration for your local WebSphere installation
     */
    static final ClearCachesMojo mojo = new ClearCachesMojo();
    static {
        mojo.setWsHome(WS_HOME);
        mojo.setWsVersion(WS_VERSION);
        mojo.setAppServerProfile(WS_APP_SERVER_PROFILE);
        mojo.setAppServer(WS_APP_SERVER);
        mojo.setNode(WS_APP_SERVER_NODE);
        mojo.setCell(WS_APP_SERVER_CELL);
    }


    /**
     * Returns the master configuration in a <code>ClearCachesMojo</code> instance.
     *
     * @return The mojo with the master configuration´s settings
     */
    public static ClearCachesMojo getMasterConfiguration() {
        return mojo;
    }
}
