 package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.freedesktop.cairo.FTFontFace;
import org.freedesktop.cairo.FTSynthesize;
import org.freedesktop.cairo.Status;
import org.freedesktop.freetype.Face;
import org.freedesktop.freetype.Library;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.jwharm.cairobindings.Platform;

import java.io.File;
import java.util.Set;

 class FTFontFaceTest {

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
        Face ftFace = Face.newFace(ftLib, TTF_FILE, 0);
        FTFontFace face = FTFontFace.create(ftFace, 0);
        assertEquals(Status.SUCCESS, face.status());
        ftFace.doneFace();
        ftLib.doneFreeType();
    }

    @Test
    void testSynthesize() {
        Library ftLib = Library.initFreeType();
        Face ftFace = Face.newFace(ftLib, TTF_FILE, 0);
        FTFontFace face = FTFontFace.create(ftFace, 0);
        face.setSynthesize(Set.of(FTSynthesize.BOLD, FTSynthesize.OBLIQUE));
        face.unsetSynthesize(Set.of(FTSynthesize.BOLD));
        assertEquals(Set.of(FTSynthesize.OBLIQUE), face.getSynthesize());
        assertEquals(Status.SUCCESS, face.status());
        ftFace.doneFace();
        ftLib.doneFreeType();
    }
}
