package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.freedesktop.cairo.FontSlant;
import org.freedesktop.cairo.FontWeight;
import org.freedesktop.cairo.Status;
import org.freedesktop.cairo.ToyFontFace;
import org.junit.jupiter.api.Test;

class ToyFontFaceTest {

    @Test
    void testCreate() {
        ToyFontFace f = ToyFontFace.create();
        assertEquals(Status.SUCCESS, f.status());
    }

    @Test
    void testCreateStringFontSlantFontWeight() {
        ToyFontFace f = ToyFontFace.create("Arial", FontSlant.NORMAL, FontWeight.NORMAL);
        assertEquals(Status.SUCCESS, f.status());
    }

    @Test
    void testGetFamily() {
        ToyFontFace f = ToyFontFace.create("Arial", FontSlant.NORMAL, FontWeight.NORMAL);
        assertEquals("Arial", f.getFamily());
        assertEquals(Status.SUCCESS, f.status());
    }

    @Test
    void testGetSlant() {
        ToyFontFace f = ToyFontFace.create("Arial", FontSlant.ITALIC, FontWeight.BOLD);
        assertEquals(FontSlant.ITALIC, f.getSlant());
        assertEquals(Status.SUCCESS, f.status());
    }

    @Test
    void testGetWeight() {
        ToyFontFace f = ToyFontFace.create("Arial", FontSlant.ITALIC, FontWeight.BOLD);
        assertEquals(FontWeight.BOLD, f.getWeight());
        assertEquals(Status.SUCCESS, f.status());
    }
}
