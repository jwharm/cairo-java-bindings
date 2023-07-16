package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.freedesktop.cairo.FontSlant;
import org.freedesktop.cairo.FontType;
import org.freedesktop.cairo.FontWeight;
import org.freedesktop.cairo.Status;
import org.freedesktop.cairo.ToyFontFace;
import org.junit.jupiter.api.Test;

class FontFaceTest {

    @Test
    void testStatus() {
        assertEquals(Status.SUCCESS, ToyFontFace.create("Arial", FontSlant.NORMAL, FontWeight.NORMAL).status());
    }

    @Test
    void testGetType() {
        assertEquals(FontType.TOY, ToyFontFace.create("Arial", FontSlant.NORMAL, FontWeight.NORMAL).getType());
    }
}
