package me.themadjem;

import java.io.*;
import java.util.Properties;

/**
 * @author themadjem
 */
class PropertiesUtil extends Properties {

    private final String path;
    private final String comments;

    /**
     * Constructor
     *
     * @param path     path to properties file
     * @param comments comments on the file
     */
    PropertiesUtil(String path, String comments) {
        this.path = path;
        this.comments = comments;
    }

    /**
     * Loads properties from the given path
     * @throws Exception throws a fileIO exception
     */
    void loadParams() throws Exception {
        InputStream is;

        // First try loading from the current directory
        try {
            File f = new File(path);
            is = new FileInputStream(f);
        } catch (Exception e) {
            is = null;
        }

        try {
            if (is == null) {
                // Try loading from classpath
                is = getClass().getResourceAsStream(path);
            }

            // Try loading properties from the file (if found)
            this.load(is);
        } catch (Exception e) {
            throw new Exception(e.getCause());
        }
    }

    /**
     * Saves properties to the given path
     */
    void saveParamChanges() {
        try {
            OutputStream out = new FileOutputStream(new File(path));
            this.store(out, comments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
