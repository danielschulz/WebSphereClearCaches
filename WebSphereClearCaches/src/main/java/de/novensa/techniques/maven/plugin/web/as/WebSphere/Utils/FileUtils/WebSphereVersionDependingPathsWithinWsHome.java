package de.novensa.techniques.maven.plugin.web.as.WebSphere.utils.FileUtils;

import de.novensa.techniques.maven.plugin.web.as.WebSphere.runtime.Constants;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.utils.Enums.WebSphereVersion;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.runtime.RuntimeData;

import java.util.HashMap;
import java.util.Map;

import static de.novensa.techniques.maven.plugin.web.as.WebSphere.utils.Enums.WebSphereVersion.WAS_6v1;

/**
 * This interface supplies a effective paths within WS Home depending on your WebSphere version.
 *
 * @author Daniel Schulz
 */
@SuppressWarnings("SpellCheckingInspection")
public class WebSphereVersionDependingPathsWithinWsHome implements RuntimeData {

    // field members just to fill the map from
    private final WebSphereVersion version;
    private final String appServerProfile;
    private final String appServer;
    private final String cell;
    private final String node;


    // technical aggregator for effective paths; no other fields will be needed from inside here
    private Map<WebSphereVersion, String[]> pathsWithinWsHome =
            new HashMap<WebSphereVersion, String[]>(Constants.SMALL_LIST_INIT_SIZE);


    // get the desired map
    public String[] getPathsWithinWsHome() {
        return this.pathsWithinWsHome.get(this.version);
    }


    // constructor
    public WebSphereVersionDependingPathsWithinWsHome(final WebSphereVersion version,
                                                      final String appServerProfile,
                                                      final String appServer,
                                                      final String cell,
                                                      final String node) {

        this.version = getCastingForWebSphereVersionToWebSphereFolderVersionStyle(version);
        this.appServerProfile = appServerProfile;
        this.appServer = appServer;
        this.cell = cell;
        this.node = node;
        
        populateMapFromInstances();
    }

    /**
     * We only need the one style now. May be the folder structure changes in the future (or did in the past). To
     * arrange these changes please change the return style. If this style just passed the input by to stay exact
     * inside here the mapping had to be extended to any possible input/output pair.
     * This way the outside version is correct and just within here we take the incorrect but meaningful WebSphere
     * ´pseudo version´. As this field just will stay inside without the possibility to come out this is accurate.
     *
     * @param webSphereVersion The exact WebSphere version
     * @return The casted WebSphere version style
     */
    @SuppressWarnings("UnusedParameters")
    private static WebSphereVersion getCastingForWebSphereVersionToWebSphereFolderVersionStyle(
            final WebSphereVersion webSphereVersion) {
        return WAS_6v1;
    }

    // inner logic
    protected void populateMapFromInstances() {
        pathsWithinWsHome.put(WAS_6v1, new String[]{

                FILE_SEPARATOR + "profiles" + FILE_SEPARATOR + appServerProfile + FILE_SEPARATOR +
                        "temp" + FILE_SEPARATOR + node + FILE_SEPARATOR + appServer,

                FILE_SEPARATOR + "profiles" + FILE_SEPARATOR + appServerProfile + FILE_SEPARATOR +
                        "wstemp" + ANY_FILES_WITHIN,

                FILE_SEPARATOR + "profiles" + FILE_SEPARATOR + appServerProfile + FILE_SEPARATOR +
                        "tranlog" + FILE_SEPARATOR + cell + FILE_SEPARATOR + node + FILE_SEPARATOR +
                        appServer + FILE_SEPARATOR
        });
    }
}
