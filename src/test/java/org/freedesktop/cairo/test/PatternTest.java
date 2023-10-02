package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.*;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;

class PatternTest {

    @Test
    void testStatus() {
        Gradient pattern = LinearGradient.create(0, 0, 10, 10);
        assertEquals(Status.SUCCESS, pattern.status());
    }

    @Test
    void testSetExtend() {
        Gradient pattern = LinearGradient.create(0, 0, 10, 10);
        pattern.setExtend(Extend.PAD);
        assertEquals(Status.SUCCESS, pattern.status());
    }

    @Test
    void testGetExtend() {
        Gradient pattern = LinearGradient.create(0, 0, 10, 10);
        pattern.setExtend(Extend.PAD);
        Extend e = pattern.getExtend();
        assertEquals(Extend.PAD, e);
        assertEquals(Status.SUCCESS, pattern.status());
    }

    @Test
    void testSetFilter() {
        Gradient pattern = LinearGradient.create(0, 0, 10, 10);
        pattern.setFilter(Filter.NEAREST);
        assertEquals(Status.SUCCESS, pattern.status());
    }

    @Test
    void testGetFilter() {
        Gradient pattern = LinearGradient.create(0, 0, 10, 10);
        pattern.setFilter(Filter.NEAREST);
        Filter f = pattern.getFilter();
        assertEquals(Filter.NEAREST, f);
        assertEquals(Status.SUCCESS, pattern.status());
    }

    @Test
    void testSetMatrix() {
        Gradient pattern = LinearGradient.create(0, 0, 10, 10);
        pattern.setMatrix(Matrix.createScale(2, 2));
        assertEquals(Status.SUCCESS, pattern.status());
    }

    @Test
    void testGetMatrix() {
        Gradient pattern = LinearGradient.create(0, 0, 10, 10);
        pattern.setMatrix(Matrix.createScale(2, 2));
        Matrix m = Matrix.create(0, 0, 0, 0, 0, 0);
        pattern.getMatrix(m);
        assertEquals(Status.SUCCESS, pattern.status());
    }

    @Test
    void testGetType() {
        Gradient pattern = LinearGradient.create(0, 0, 10, 10);
        assertEquals(PatternType.LINEAR, pattern.getType());
        assertEquals(Status.SUCCESS, pattern.status());
    }

    @Test
    void testSetDither() {
        Pattern pattern = LinearGradient.create(0, 0, 10, 10);
        pattern.setDither(Dither.FAST);
        assertEquals(Status.SUCCESS, pattern.status());
    }

    @Test
    void testGetDither() {
        Pattern pattern = LinearGradient.create(0, 0, 10, 10);
        pattern.setDither(Dither.FAST);
        assertEquals(Dither.FAST, pattern.getDither());
        assertEquals(Status.SUCCESS, pattern.status());
    }
}
