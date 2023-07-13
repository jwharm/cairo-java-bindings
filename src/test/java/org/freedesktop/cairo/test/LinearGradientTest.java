package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.freedesktop.cairo.LinearGradient;
import org.freedesktop.cairo.Status;
import org.junit.jupiter.api.Test;

class LinearGradientTest {

    @Test
    void testCreate() {
        LinearGradient gradient = LinearGradient.create(0, 0, 10, 10);
        assertEquals(Status.SUCCESS, gradient.status());
    }

    @Test
    void testGetLinearPoints() {
        LinearGradient gradient = LinearGradient.create(0, 1, 10, 11);
        var points = gradient.getLinearPoints();
        assertEquals(2, points.length);
        assertEquals(0, points[0].x());
        assertEquals(1, points[0].y());
        assertEquals(10, points[1].x());
        assertEquals(11, points[1].y());
        assertEquals(Status.SUCCESS, gradient.status());
    }
}
