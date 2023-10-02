package org.freedesktop.cairo.test;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FontOptionsTest {

    @Test
    void testAntialias() {
        FontOptions fo = FontOptions.create();
        fo.setAntialias(Antialias.FAST);
        assertEquals(Antialias.FAST, fo.getAntialias());
        assertEquals(Status.SUCCESS, fo.status());
    }

    @Test
    void testSubpixelOrder() {
        FontOptions fo = FontOptions.create();
        fo.setSubpixelOrder(SubpixelOrder.BGR);
        assertEquals(SubpixelOrder.BGR, fo.getSubpixelOrder());
        assertEquals(Status.SUCCESS, fo.status());
    }

    @Test
    void testHintStyle() {
        FontOptions fo = FontOptions.create();
        fo.setHintStyle(HintStyle.SLIGHT);
        assertEquals(HintStyle.SLIGHT, fo.getHintStyle());
        assertEquals(Status.SUCCESS, fo.status());
    }

    @Test
    void testHintMetrics() {
        FontOptions fo = FontOptions.create();
        fo.setHintMetrics(HintMetrics.OFF);
        assertEquals(HintMetrics.OFF, fo.getHintMetrics());
        assertEquals(Status.SUCCESS, fo.status());
    }

    @Test
    void testVariations() {
        FontOptions fo = FontOptions.create();
        fo.setVariations("wght=200,wdth=140.5");
        assertEquals("wght=200,wdth=140.5", fo.getVariations());
        assertEquals(Status.SUCCESS, fo.status());
    }

    @Test
    void testColorMode() {
        FontOptions fo = FontOptions.create();
        fo.setColorMode(ColorMode.NO_COLOR);
        assertEquals(ColorMode.NO_COLOR, fo.getColorMode());
        assertEquals(Status.SUCCESS, fo.status());
    }

    @Test
    void testColorPalette() {
        FontOptions fo = FontOptions.create();
        fo.setColorPalette(FontOptions.COLOR_PALETTE_DEFAULT);
        assertEquals(FontOptions.COLOR_PALETTE_DEFAULT, fo.getColorPalette());
        assertEquals(Status.SUCCESS, fo.status());
    }

    @Test
    void testCustomColorPalette() {
        FontOptions fo = FontOptions.create();
        fo.setCustomPaletteColor(0, 10.0, 20.0, 30.0, 40.5);
        assertEquals(new RGBA(10.0, 20.0, 30.0, 40.5), fo.getCustomPaletteColor(0));
        assertEquals(Status.SUCCESS, fo.status());
    }
}
