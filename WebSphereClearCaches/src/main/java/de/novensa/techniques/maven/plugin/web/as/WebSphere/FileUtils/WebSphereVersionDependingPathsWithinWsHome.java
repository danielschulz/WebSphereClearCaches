package de.novensa.techniques.maven.plugin.web.as.WebSphere.FileUtils;

import de.novensa.techniques.maven.plugin.web.as.WebSphere.Enums.WebSphereVersion;
import de.novensa.techniques.maven.plugin.web.as.WebSphere.RuntimeData;

import java.util.HashMap;
import java.util.Map;

/**
 * This interface supplies a effective paths within WS Home depending on your WebSphere version.
 *
 * @author Daniel Schulz
 */
public abstract class WebSphereVersionDependingPathsWithinWsHome implements RuntimeData {

    private final Map<WebSphereVersion, String[]> pathsWithinWsHome =
            new HashMap<WebSphereVersion, String[]>(32);

}
