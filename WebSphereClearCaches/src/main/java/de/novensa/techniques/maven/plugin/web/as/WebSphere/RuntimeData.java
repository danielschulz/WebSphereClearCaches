package de.novensa.techniques.maven.plugin.web.as.WebSphere;

/**
 * This interface supplies the running plugin with JVM-specific, OS-specific, and other information.
 *
 * @author Daniel Schulz
 */
public interface RuntimeData {

    static final String LINE_BREAK = System.getProperty("line.separator");
    static final String FILE_SEPARATOR = System.getProperty("file.separator");

}
