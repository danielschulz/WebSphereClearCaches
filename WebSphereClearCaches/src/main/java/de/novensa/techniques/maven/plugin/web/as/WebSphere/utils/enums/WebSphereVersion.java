package de.novensa.techniques.maven.plugin.web.as.WebSphere.Utils.Enums;

/**
 * Identifies the WebSphere versions.
 *
 * @author Daniel Schulz
 */
public enum WebSphereVersion {
    // based on:
    // http://en.wikipedia.org/wiki/IBM_WebSphere_Application_Server
    // older versions are treated like v6.0
    WAS_6v0, WAS_6v1, WAS_6v5, WAS_7v0, WAS_8v0, WAS_8v5
}
