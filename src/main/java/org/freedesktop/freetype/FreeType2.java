package org.freedesktop.freetype;

import io.github.jwharm.cairobindings.LibLoad;
import io.github.jwharm.cairobindings.Platform;

/**
 * This class contains global declarations that do not belong in a specific
 * FreeType class definition.
 */
public class FreeType2 {

    static {
        switch (Platform.getRuntimePlatform()) {
            case "linux" -> LibLoad.loadLibrary("libfreetype.so.6");
            case "windows" -> LibLoad.loadLibrary("freetype-6.dll");
            case "macos" -> LibLoad.loadLibrary("libfreetype.6.dylib");
        }
    }

    /**
     * Ensures the class initializer has loaded the freetype library.
     */
    public static void ensureInitialized() {
    }

    // Prohibit instantiation
    private FreeType2() {
    }
}
