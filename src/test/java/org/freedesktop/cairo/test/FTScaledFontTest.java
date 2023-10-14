package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.jwharm.cairobindings.Platform;
import org.freedesktop.cairo.FTFontFace;
import org.freedesktop.cairo.FTScaledFont;
import org.freedesktop.cairo.FontOptions;
import org.freedesktop.cairo.Matrix;
import org.freedesktop.cairo.Status;
import org.freedesktop.freetype.Face;
import org.freedesktop.freetype.Library;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

class FTScaledFontTest {

    private static String TTF_FILE;

    @BeforeAll
    static void setup() {
        // These files are going to be in different locations depending on your system.
        switch (Platform.getRuntimePlatform()) {
            case "linux" -> {
                // Fedora
                TTF_FILE = "/usr/share/fonts/liberation-serif/LiberationSerif-Regular.ttf";
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
        Library ftLib = Library.initFreeType();
        Face ftFace = new Face(ftLib, TTF_FILE, 0);
        FTFontFace face = FTFontFace.create(ftFace, 0);
        FTScaledFont scaledFont = FTScaledFont.create(face, Matrix.createIdentity(), Matrix.createIdentity(), FontOptions.create());
        assertEquals(Status.SUCCESS, scaledFont.status());
    }

    @Test
    void testLockAndUnlockFace() {
        Library ftLib = Library.initFreeType();
        Face ftFace = new Face(ftLib, TTF_FILE, 0);
        FTFontFace face = FTFontFace.create(ftFace, 0);
        FTScaledFont scaledFont = FTScaledFont.create(face, Matrix.createIdentity(), Matrix.createIdentity(), FontOptions.create());
        scaledFont.lockFace();
        scaledFont.unlockFace();
        assertEquals(Status.SUCCESS, scaledFont.status());
    }
}
