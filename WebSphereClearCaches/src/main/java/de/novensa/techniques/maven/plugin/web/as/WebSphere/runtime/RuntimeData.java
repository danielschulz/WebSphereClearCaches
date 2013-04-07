package de.novensa.techniques.maven.plugin.web.as.WebSphere.runtime;

/**
 * This interface supplies the running plugin with JVM-specific, OS-specific, and other information.
 *
 * @author Daniel Schulz
 */
@SuppressWarnings("UnusedDeclaration")
public interface RuntimeData {

    static final String LINE_BREAK = System.getProperty("line.separator");
    static final String OS_NAME = System.getProperty("os.name");
    static final String TABULATOR = "\t";

    static final String FILE_SEPARATOR = System.getProperty("file.separator");
    static final String ANY_FILES_WITHIN = FILE_SEPARATOR + ":*:";              // tells the plugin just to clear the
    // contained files but not the folder itself
}
