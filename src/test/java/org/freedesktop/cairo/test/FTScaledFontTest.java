package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.jwharm.cairobindings.Platform;
import org.freedesktop.cairo.FTFontFace;
import org.freedesktop.cairo.FTScaledFont;
import org.freedesktop.cairo.FontOptions;
import org.freedesktop.cairo.Matrix;
import org.freedesktop.cairo.Status;
import org.freedesktop.freetype.Face;
import org.freedesktop.freetype.Library;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.foreign.Arena;

class FTScaledFontTest {

    private static String TTF_FILE;

    @BeforeAll
    static void setup() {
        // These files are going to be in different locations depending on your system.
        switch (Platform.getRuntimePlatform()) {
            case "linux" -> {
                // Fedora
                TTF_FILE = "/usr/share/fonts/liberation-serif-fonts/LiberationSerif-Regular.ttf";
                if (! new File(TTF_FILE).exists()) {
                    // Ubuntu
                    TTF_FILE = "/usr/share/fonts/truetype/liberation/LiberationSerif-Regular.ttf";
                }
            }
            case "windows" -> TTF_FILE = "C:\\Windows\\Fonts\\arial.ttf";
            case "macos" -> TTF_FILE = "/Library/Fonts/Arial Unicode.ttf";
        }
    }

    @Test
    void testCreate() {
        try (Arena arena = Arena.ofConfined()) {
            Library ftLib = Library.initFreeType();
            Face ftFace = Face.newFace(ftLib, TTF_FILE, 0);
            FTFontFace face = FTFontFace.create(ftFace, 0);
            FTScaledFont scaledFont = FTScaledFont.create(face, Matrix.create(arena).initIdentity(), Matrix.create(arena).initIdentity(), FontOptions.create());
            assertEquals(Status.SUCCESS, scaledFont.status());
            ftFace.doneFace();
            ftLib.doneFreeType();
        }
    }

    @Test
    void testLockAndUnlockFace() {
        try (Arena arena = Arena.ofConfined()) {
            Library ftLib = Library.initFreeType();
            Face ftFace = Face.newFace(ftLib, TTF_FILE, 0);
            FTFontFace face = FTFontFace.create(ftFace, 0);
            FTScaledFont scaledFont = FTScaledFont.create(face, Matrix.create(arena).initIdentity(), Matrix.create(arena).initIdentity(), FontOptions.create());
            scaledFont.lockFace();
            scaledFont.unlockFace();
            assertEquals(Status.SUCCESS, scaledFont.status());
            ftFace.doneFace();
            ftLib.doneFreeType();
        }
    }
}
