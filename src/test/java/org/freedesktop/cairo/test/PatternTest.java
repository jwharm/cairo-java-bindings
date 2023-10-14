package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.*;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class PatternTest {

    @Test
    void testStatus() {
        Gradient pattern = LinearGradient.create(0, 0, 10, 10);
        assertEquals(Status.SUCCESS, pattern.status());
    }

    @Test
    void testExtend() {
        Gradient pattern = LinearGradient.create(0, 0, 10, 10);
        pattern.setExtend(Extend.PAD);
        Extend e = pattern.getExtend();
        assertEquals(Extend.PAD, e);
        assertEquals(Status.SUCCESS, pattern.status());
    }

    @Test
    void testFilter() {
        Gradient pattern = LinearGradient.create(0, 0, 10, 10);
        pattern.setFilter(Filter.NEAREST);
        Filter f = pattern.getFilter();
        assertEquals(Filter.NEAREST, f);
        assertEquals(Status.SUCCESS, pattern.status());
    }

    @Test
    void testMatrix() {
        Gradient pattern = LinearGradient.create(0, 0, 10, 10);
        pattern.setMatrix(Matrix.createScale(2, 2));
        Matrix m = Matrix.create(0, 0, 0, 0, 0, 0);
        pattern.getMatrix(m);
        assertEquals(Status.SUCCESS, pattern.status());
    }

    @Test
    void testGetPatternType() {
        Gradient pattern = LinearGradient.create(0, 0, 10, 10);
        assertEquals(PatternType.LINEAR, pattern.getPatternType());
        assertEquals(Status.SUCCESS, pattern.status());
    }

    @Disabled("Does not work with cairo 1.17.8 (Fedora 38, Gnome 45 Flatpak SDK)")
    @Test
    void testDither() {
        Pattern pattern = LinearGradient.create(0, 0, 10, 10);
        pattern.setDither(Dither.FAST);
        assertEquals(Dither.FAST, pattern.getDither());
        assertEquals(Status.SUCCESS, pattern.status());
    }
}
