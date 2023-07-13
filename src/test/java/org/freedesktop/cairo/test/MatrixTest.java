package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.freedesktop.cairo.Matrix;
import org.freedesktop.cairo.Point;
import org.junit.jupiter.api.Test;

class MatrixTest {

    @Test
    void testCreateDoubleDoubleDoubleDoubleDoubleDouble() {
        Matrix matrix = Matrix.create(1, 0, 0, 1, 0, 0);
        assertNotNull(matrix);
    }

    @Test
    void testInit() {
        Matrix matrix = Matrix.create(1, 0, 0, 1, 0, 0);
        matrix.init(2, 0, 0, 2, 0, 0);
    }

    @Test
    void testCreateIdentity() {
        Matrix matrix = Matrix.createIdentity();
        assertNotNull(matrix);
    }

    @Test
    void testCreateTranslate() {
        Matrix matrix = Matrix.createTranslate(20, 20);
        assertNotNull(matrix);
    }

    @Test
    void testCreateScale() {
        Matrix matrix = Matrix.createScale(2, 2);
        assertNotNull(matrix);
    }

    @Test
    void testCreateRotate() {
        Matrix matrix = Matrix.createRotate(90);
        assertNotNull(matrix);
    }

    @Test
    void testTranslate() {
        Matrix matrix = Matrix.createIdentity();
        matrix.translate(10, 10);
    }

    @Test
    void testScale() {
        Matrix matrix = Matrix.createTranslate(20, 20);
        matrix.scale(0.5, 0.5);
    }

    @Test
    void testRotate() {
        Matrix matrix = Matrix.createTranslate(20, 20);
        matrix.rotate(180);
    }

    @Test
    void testInvert() {
        Matrix matrix = Matrix.createTranslate(20, 20);
        matrix.invert();
    }

    @Test
    void testMultiply() {
        Matrix matrix1 = Matrix.createRotate(90);
        Matrix matrix2 = Matrix.createTranslate(20, 20);
        matrix1.multiply(matrix2);
    }

    @Test
    void testTransformDistance() {
        Matrix matrix = Matrix.createScale(3, 2);
        Point point = matrix.transformDistance(new Point(10, 10));
        assertEquals(30, point.x());
        assertEquals(20, point.y());
    }

    @Test
    void testTransformPoint() {
        Matrix matrix = Matrix.createTranslate(20, 30);
        Point point = matrix.transformPoint(new Point(30, 40));
        assertEquals(50, point.x());
        assertEquals(70, point.y());
    }
}
