package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;

class ScaledFontTest {

    private ScaledFont create(Arena arena) {
        return ScaledFont.create(ToyFontFace.create("Arial", FontSlant.NORMAL, FontWeight.NORMAL),
                Matrix.create(arena).initIdentity(), Matrix.create(arena).initIdentity(), FontOptions.create());
    }
    
    @Test
    void testCreate() {
        try (Arena arena = Arena.ofConfined()) {
            ScaledFont sf = create(arena);
            assertEquals(Status.SUCCESS, sf.status());
        }
    }

    @Test
    void testExtents() {
        try (Arena arena = Arena.ofConfined()) {
            ScaledFont sf = create(arena);
            FontExtents e = FontExtents.create(arena);
            sf.extents(e);
            assertNotNull(e);
            assertEquals(Status.SUCCESS, sf.status());
        }
    }

    @Test
    void testTextExtents() {
        try (Arena arena = Arena.ofConfined()) {
            ScaledFont sf = create(arena);
            TextExtents extents = TextExtents.create(arena);
            sf.textExtents("test", extents);
            // I'm not sure if I can hard-code the expected height and width, so
            // we will only test if the extents are wider than they are high.
            assertTrue(extents.width() > extents.height());
            assertEquals(Status.SUCCESS, sf.status());
        }
    }

    @Test
    void testGlyphExtents() {
        try (Arena arena = Arena.ofConfined()) {
            ScaledFont sf = create(arena);
            try (Glyphs glyphs = sf.textToGlyphs(0, 0, "test")) {
                TextExtents extents = TextExtents.create(arena);
                sf.glyphExtents(glyphs, extents);
                // I'm not sure if I can hard-code the expected height and width, so
                // we will only test if the extents are wider than they are high.
                assertTrue(extents.width() > extents.height());
                assertEquals(Status.SUCCESS, sf.status());
            }
        }
    }

    @Test
    void testTextToGlyphs() {
        try (Arena arena = Arena.ofConfined()) {
            ScaledFont sf = create(arena);
            try (Glyphs glyphs = sf.textToGlyphs(0, 0, "test")) {
                assertEquals(4, glyphs.getNumGlyphs());
                assertEquals(Status.SUCCESS, sf.status());
            }
        }
    }

    @Test
    void testGetFontFace() {
        try (Arena arena = Arena.ofConfined()) {
            ScaledFont sf = create(arena);
            FontFace f = sf.getFontFace();
            assertEquals(FontType.TOY, f.getFontType());
            assertEquals(Status.SUCCESS, f.status());
            assertEquals(Status.SUCCESS, sf.status());
        }
    }

    @Test
    void testGetFontOptions() {
        try (Arena arena = Arena.ofConfined()) {
            ScaledFont sf = create(arena);
            FontOptions options = FontOptions.create();
            sf.getFontOptions(options);
            assertEquals(Status.SUCCESS, options.status());
            assertEquals(Status.SUCCESS, sf.status());
        }
    }

    @Test
    void testGetFontMatrix() {
        try (Arena arena = Arena.ofConfined()) {
            ScaledFont sf = create(arena);
            Matrix m = Matrix.create(arena);
            sf.getFontMatrix(m);
            assertNotNull(m);
            assertEquals(Status.SUCCESS, sf.status());
        }
    }

    @Test
    void testGetCTM() {
        try (Arena arena = Arena.ofConfined()) {
            ScaledFont sf = create(arena);
            Matrix ctm = Matrix.create(arena);
            sf.getCTM(ctm);
            assertNotNull(ctm);
            assertEquals(Status.SUCCESS, sf.status());
        }
    }

    @Test
    void testGetScaleMatrix() {
        try (Arena arena = Arena.ofConfined()) {
            ScaledFont sf = create(arena);
            Matrix m = Matrix.create(arena);
            sf.getScaleMatrix(m);
            assertNotNull(m);
            assertEquals(Status.SUCCESS, sf.status());
        }
    }

    @Test
    void testGetFontType() {
        try (Arena arena = Arena.ofConfined()) {
            ScaledFont sf = create(arena);
            FontType t = sf.getFontType();
            assertNotNull(t);
            assertEquals(Status.SUCCESS, sf.status());
        }
    }
}
