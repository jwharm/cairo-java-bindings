package org.freedesktop.freetype;

import io.github.jwharm.javagi.interop.LibLoad;
import io.github.jwharm.javagi.interop.Platform;

/**
 * This class contains global declarations that do not belong in a specific
 * FreeType class definition.
 */
public class FreeType2 {

    static {
        switch (Platform.getRuntimePlatform()) {
            case "linux" -> io.github.jwharm.javagi.interop.LibLoad.loadLibrary("libfreetype.so.6");
            case "windows" -> io.github.jwharm.javagi.interop.LibLoad.loadLibrary("freetype-6.dll");
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
