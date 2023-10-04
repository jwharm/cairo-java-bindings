package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.freedesktop.cairo.FontExtents;
import org.freedesktop.cairo.FontFace;
import org.freedesktop.cairo.FontOptions;
import org.freedesktop.cairo.FontSlant;
import org.freedesktop.cairo.FontType;
import org.freedesktop.cairo.FontWeight;
import org.freedesktop.cairo.Glyph;
import org.freedesktop.cairo.Matrix;
import org.freedesktop.cairo.ScaledFont;
import org.freedesktop.cairo.Status;
import org.freedesktop.cairo.TextExtents;
import org.freedesktop.cairo.ToyFontFace;
import org.junit.jupiter.api.Disabled;
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

    @Disabled
    @Test
    void testTextExtents() {
        ScaledFont sf = create();
        TextExtents te = sf.textExtents("test");
        assertTrue(te.height() > 0);
        assertTrue(te.height() < 1);
        assertEquals(Status.SUCCESS, sf.status());
    }

    @Disabled
    @Test
    void testGlyphExtents() {
        ScaledFont sf = create();
        List<Glyph> glyphs = new ArrayList<>();
        sf.textToGlyphs(0, 0, "test", glyphs, null);
        TextExtents extents = sf.glyphExtents(glyphs.toArray(new Glyph[] {}));
        assertTrue(extents.height() > 0);
        assertTrue(extents.height() < 1);
        assertEquals(Status.SUCCESS, sf.status());
    }

    @Test
    void testTextToGlyphs() {
        ScaledFont sf = create();
        List<Glyph> glyphs = new ArrayList<>();
        sf.textToGlyphs(0, 0, "test", glyphs, null);
        assertEquals(4, glyphs.size());
        assertEquals(Status.SUCCESS, sf.status());
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
