package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.freedesktop.cairo.Circle;
import org.freedesktop.cairo.RadialGradient;
import org.freedesktop.cairo.Status;
import org.junit.jupiter.api.Test;

class RadialGradientTest {

    @Test
    void testCreate() {
        RadialGradient gradient = RadialGradient.create(100, 100, 50, 200, 100, 60);
        assertEquals(Status.SUCCESS, gradient.status());
    }

    @Test
    void testGetRadialCircles() {
        RadialGradient gradient = RadialGradient.create(100, 100, 50, 200, 100, 60);
        Circle[] circles = gradient.getRadialCircles();
        assertEquals(circles.length, 2);
        assertEquals(circles[0].x(), 100);
        assertEquals(circles[0].y(), 100);
        assertEquals(circles[0].radius(), 50);
        assertEquals(circles[1].x(), 200);
        assertEquals(circles[1].y(), 100);
        assertEquals(circles[1].radius(), 60);
        assertEquals(Status.SUCCESS, gradient.status());
    }
}
