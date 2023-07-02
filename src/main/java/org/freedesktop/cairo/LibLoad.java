package org.freedesktop.cairo;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * The LibLoad class is used internally to load native libraries by name
 */
public class LibLoad {

    /**
     * Prevent instantiation
     */
    private LibLoad() {
    }

    /**
     * Load the native library with the provided name. First the library is loaded
     * with {@link System#loadLibrary(String)}, but if that is unsuccessful, a file
     * with the provided name is searched in the "java.library.path" and loaded with
     * {@link System#load(String)}.
     * 
     * @param name the name of the library
     */
    public static void loadLibrary(String name) {
        RuntimeException fail = new RuntimeException("Could not load library");
        try {
            System.loadLibrary(name);
            return;
        } catch (Throwable t) {
            fail.addSuppressed(t);
        }
        for (String s : System.getProperty("java.library.path").split(File.pathSeparator)) {
            if (s.isBlank()) continue;
            Path pk = Path.of(s).toAbsolutePath().normalize();
            if (!Files.isDirectory(pk)) continue;
            Path[] paths;
            try (Stream<Path> p = Files.list(pk)) {
                paths = p.toArray(Path[]::new);
            } catch (Throwable t) {
                fail.addSuppressed(t);
                continue;
            }
            for (Path path : paths) {
                try {
                    String fn = path.getFileName().toString();
                    if (fn.equals(name)) {
                        System.load(path.toString());
                        return;
                    }
                } catch (Throwable t) {
                    fail.addSuppressed(t);
                }
            }
        }
        throw fail;
    }
}