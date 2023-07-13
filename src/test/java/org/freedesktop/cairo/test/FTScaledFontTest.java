package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.freedesktop.cairo.FTFontFace;
import org.freedesktop.cairo.FTScaledFont;
import org.freedesktop.cairo.FontOptions;
import org.freedesktop.cairo.Matrix;
import org.freedesktop.cairo.Status;
import org.freedesktop.freetype.Face;
import org.freedesktop.freetype.Library;
import org.junit.jupiter.api.Test;

class FTScaledFontTest {

    private static final String TTF_FILE = "C:\\Windows\\Fonts\\arial.ttf";
    
    @Test
    void testCreate() {
        Library ftLib = Library.initFreeType();
        Face ftFace = new Face(ftLib, TTF_FILE, 0);
        FTFontFace face = FTFontFace.create(ftFace, 0);
        FTScaledFont scaledFont = FTScaledFont.create(face, Matrix.createIdentity(), Matrix.createIdentity(), FontOptions.create());
        assertEquals(Status.SUCCESS, scaledFont.status());
    }

    @Test
    void testLockFace() {
        Library ftLib = Library.initFreeType();
        Face ftFace = new Face(ftLib, TTF_FILE, 0);
        FTFontFace face = FTFontFace.create(ftFace, 0);
        FTScaledFont scaledFont = FTScaledFont.create(face, Matrix.createIdentity(), Matrix.createIdentity(), FontOptions.create());
        Face f = scaledFont.lockFace();
        assertNotNull(f);
        assertEquals(Status.SUCCESS, scaledFont.status());
    }

    @Test
    void testUnlockFace() {
        Library ftLib = Library.initFreeType();
        Face ftFace = new Face(ftLib, TTF_FILE, 0);
        FTFontFace face = FTFontFace.create(ftFace, 0);
        FTScaledFont scaledFont = FTScaledFont.create(face, Matrix.createIdentity(), Matrix.createIdentity(), FontOptions.create());
        scaledFont.lockFace();
        scaledFont.unlockFace();
        assertEquals(Status.SUCCESS, scaledFont.status());
    }

}
