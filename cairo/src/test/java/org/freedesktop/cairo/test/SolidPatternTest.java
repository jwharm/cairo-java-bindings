package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.*;

import org.freedesktop.cairo.SolidPattern;
import org.freedesktop.cairo.Status;
import org.junit.jupiter.api.Test;

class SolidPatternTest {

    @Test
    void testCreateRGB() {
        SolidPattern pattern = SolidPattern.createRGB(0.1, 0.2, 0.3);
        assertEquals(Status.SUCCESS, pattern.status());
    }

    @Test
    void testCreateRGBA() {
        SolidPattern pattern = SolidPattern.createRGBA(0.1, 0.2, 0.3, 0.4);
        assertEquals(Status.SUCCESS, pattern.status());
    }

    @Test
    void testGetRGBA() {
        SolidPattern pattern = SolidPattern.createRGBA(0.1, 0.2, 0.3, 0.4);
        double[] rgba = pattern.getRGBA();
        assertEquals(4, rgba.length);
        assertEquals(0.1, rgba[0]);
        assertEquals(0.2, rgba[1]);
        assertEquals(0.3, rgba[2]);
        assertEquals(0.4, rgba[3]);
        assertEquals(Status.SUCCESS, pattern.status());
    }
}
