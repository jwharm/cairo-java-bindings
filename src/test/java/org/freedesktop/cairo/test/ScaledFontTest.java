package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;

class ScaledFontTest {

    private ScaledFont create() {
        return ScaledFont.create(ToyFontFace.create("Arial", FontSlant.NORMAL, FontWeight.NORMAL),
                Matrix.createIdentity(), Matrix.createIdentity(), FontOptions.create());
    }
    
    @Test
    void testCreate() {
        ScaledFont sf = create();
        assertEquals(Status.SUCCESS, sf.status());
    }

    @Test
    void testExtents() {
        ScaledFont sf = create();
        FontExtents e = sf.extents();
        assertNotNull(e);
        assertEquals(Status.SUCCESS, sf.status());
    }

    @Test
    void testTextExtents() {
        ScaledFont sf = create();
        TextExtents extents = sf.textExtents("test");
        // I'm not sure if I can hard-code the expected height and width, so
        // we will only test if the extents are wider than they are high.
        assertTrue(extents.width() > extents.height());
        assertEquals(Status.SUCCESS, sf.status());
    }

    @Test
    void testGlyphExtents() {
        ScaledFont sf = create();
        try (Glyphs glyphs = sf.textToGlyphs(0, 0, "test")) {
            TextExtents extents = sf.glyphExtents(glyphs);
            // I'm not sure if I can hard-code the expected height and width, so
            // we will only test if the extents are wider than they are high.
            assertTrue(extents.width() > extents.height());
            assertEquals(Status.SUCCESS, sf.status());
        }
    }

    @Test
    void testTextToGlyphs() {
        ScaledFont sf = create();
        try (Glyphs glyphs = sf.textToGlyphs(0, 0, "test")) {
            assertEquals(4, glyphs.getNumGlyphs());
            assertEquals(Status.SUCCESS, sf.status());
        }
    }

    @Test
    void testGetFontFace() {
        ScaledFont sf = create();
        FontFace f = sf.getFontFace();
        assertEquals(FontType.TOY, f.getFontType());
        assertEquals(Status.SUCCESS, f.status());
        assertEquals(Status.SUCCESS, sf.status());
    }

    @Test
    void testGetFontOptions() {
        ScaledFont sf = create();
        FontOptions options = FontOptions.create();
        sf.getFontOptions(options);
        assertEquals(Status.SUCCESS, options.status());
        assertEquals(Status.SUCCESS, sf.status());
    }

    @Test
    void testGetFontMatrix() {
        ScaledFont sf = create();
        Matrix m = sf.getFontMatrix();
        assertNotNull(m);
        assertEquals(Status.SUCCESS, sf.status());
    }

    @Test
    void testGetCTM() {
        ScaledFont sf = create();
        Matrix ctm = sf.getCTM();
        assertNotNull(ctm);
        assertEquals(Status.SUCCESS, sf.status());
    }

    @Test
    void testGetScaleMatrix() {
        ScaledFont sf = create();
        Matrix m = sf.getScaleMatrix();
        assertNotNull(m);
        assertEquals(Status.SUCCESS, sf.status());
    }

    @Test
    void testGetFontType() {
        ScaledFont sf = create();
        FontType t = sf.getFontType();
        assertEquals(Status.SUCCESS, sf.status());
    }
}
