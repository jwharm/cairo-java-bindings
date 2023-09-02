package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.freedesktop.cairo.Gradient;
import org.freedesktop.cairo.LinearGradient;
import org.freedesktop.cairo.Status;
import org.junit.jupiter.api.Test;

class GradientTest {

    @Test
    void testAddColorStopRGB() {
        Gradient gradient = LinearGradient.create(0, 0, 10, 10);
        gradient.addColorStopRGB(0, 1, 1, 1);
        assertEquals(Status.SUCCESS, gradient.status());
    }

    @Test
    void testAddColorStopRGBA() {
        Gradient gradient = LinearGradient.create(0, 0, 10, 10);
        gradient.addColorStopRGBA(0, 1, 1, 1, 1);
        assertEquals(Status.SUCCESS, gradient.status());
    }

    @Test
    void testGetColorStopCount() {
        Gradient gradient = LinearGradient.create(0, 0, 10, 10);
        gradient.addColorStopRGB(0, 1, 1, 1);
        assertEquals(1, gradient.getColorStopCount());
        assertEquals(Status.SUCCESS, gradient.status());
    }

    @Test
    void testGetColorStopRGBA() {
        Gradient gradient = LinearGradient.create(0, 0, 10, 10);
        gradient.addColorStopRGBA(0, 0.5, 0.6, 0.7, 0.8);
        var color = gradient.getColorStopRGBA(0);
        assertEquals(0, color[0]);
        assertEquals(0.5, color[1]);
        assertEquals(0.6, color[2]);
        assertEquals(0.7, color[3]);
        assertEquals(0.8, color[4]);
        assertEquals(Status.SUCCESS, gradient.status());
    }
}
