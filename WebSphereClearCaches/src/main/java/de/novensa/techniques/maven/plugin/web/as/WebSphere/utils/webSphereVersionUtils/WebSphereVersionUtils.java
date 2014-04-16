package de.novensa.techniques.maven.plugin.web.as.WebSphere.Utils.WebSphereVersionUtils;

import de.novensa.techniques.maven.plugin.web.as.WebSphere.Utils.Enums.WebSphereVersion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.novensa.techniques.maven.plugin.web.as.WebSphere.Utils.Enums.WebSphereVersion.*;

/**
 * THis class delivers utilities to parse the WebSphere version from the pom.xml without any prior
 * knowledge of other kind. Another technique to distinguish the versions will be performed in the extraction of the
 * effective file paths in the file utils.
 *
 * @author Daniel Schulz
 */
public class WebSphereVersionUtils {

    private static final Pattern PATTERN = Pattern.compile("(v|version)?(\\d+)\\.(\\d+)((-|\\.)\\d+)*");

    public static WebSphereVersion getWebSphereVersion(final String rawVersionString) {
        final Matcher matcher = PATTERN.matcher(rawVersionString);
        WebSphereVersion result = null;

        while (matcher.find()) {
            result = getWebSphereVersion(matcher.group(2), matcher.group(3));
        }

        return result;
    }

    private static WebSphereVersion getWebSphereVersion(final String major, final String minor) {

        final int maj = Integer.parseInt(major);
        final int min = Integer.parseInt(minor);

        // double variable-switch by major version, minor version to get the a good guess
        // major 6
        if (6 <= maj) {
            if (0 == min) {
                return WAS_6v0;
            } else if (1 <= min) {
                return WAS_6v1;
            } else if (5 <= min) {
                return WAS_6v5;
            }
        }
        // major 7
        else if (7 == maj) {
            return WAS_7v0;
        }
        // major 8
        else if (8 == maj) {
            if (0 == min) {
                return WAS_8v0;
            } else if (5 <= min) {
                return WAS_8v5;
            }
        } else {
            // in case this plugin does not know about the latest version -- apply the wisdom of the
            // most recent version
            return WAS_8v5;
        }

        // no fuzzy or exact match
        return null;
    }
}
