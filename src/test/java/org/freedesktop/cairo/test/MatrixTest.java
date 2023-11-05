package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.freedesktop.cairo.Matrix;
import org.freedesktop.cairo.Point;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;

class MatrixTest {

    @Test
    void testInit() {
        try (Arena arena = Arena.ofConfined()) {
            Matrix matrix = Matrix.create(arena).init(1, 0, 0, 1, 0, 0);
            matrix.init(2, 0, 0, 2, 0, 0);
        }
    }

    @Test
    void testCreateIdentity() {
        try (Arena arena = Arena.ofConfined()) {
            Matrix matrix = Matrix.create(arena).initIdentity();
            assertNotNull(matrix);
        }
    }

    @Test
    void testCreateTranslate() {
        try (Arena arena = Arena.ofConfined()) {
            Matrix matrix = Matrix.create(arena).initTranslate(20, 20);
            assertNotNull(matrix);
        }
    }

    @Test
    void testCreateScale() {
        try (Arena arena = Arena.ofConfined()) {
            Matrix matrix = Matrix.create(arena).initIdentity();
            matrix.scale(2, 2);
            assertNotNull(matrix);
        }
    }

    @Test
    void testCreateRotate() {
        try (Arena arena = Arena.ofConfined()) {
            Matrix matrix = Matrix.create(arena).initIdentity();
            matrix.rotate(90);
            assertNotNull(matrix);
        }
    }

    @Test
    void testTranslate() {
        try (Arena arena = Arena.ofConfined()) {
            Matrix matrix = Matrix.create(arena).initIdentity();
            matrix.translate(10, 10);
        }
    }

    @Test
    void testScale() {
        try (Arena arena = Arena.ofConfined()) {
            Matrix matrix = Matrix.create(arena).initTranslate(20, 20);
            matrix.scale(0.5, 0.5);
        }
    }

    @Test
    void testRotate() {
        try (Arena arena = Arena.ofConfined()) {
            Matrix matrix = Matrix.create(arena).initTranslate(20, 20);
            matrix.rotate(180);
        }
    }

    @Test
    void testInvert() {
        try (Arena arena = Arena.ofConfined()) {
            Matrix matrix = Matrix.create(arena).initTranslate(20, 20);
            matrix.invert();
        }
    }

    @Test
    void testMultiply() {
        try (Arena arena = Arena.ofConfined()) {
            Matrix matrix1 = Matrix.create(arena).initIdentity();
            matrix1.rotate(90);
            Matrix matrix2 = Matrix.create(arena).initTranslate(20, 20);
            Matrix matrix3 = Matrix.create(arena).initIdentity();
            matrix3.multiply(matrix1, matrix2);
        }
    }

    @Test
    void testTransformDistance() {
        try (Arena arena = Arena.ofConfined()) {
            Matrix matrix = Matrix.create(arena).initIdentity();
            matrix.scale(3, 2);
            Point point = matrix.transformDistance(new Point(10, 10));
            assertEquals(30, point.x());
            assertEquals(20, point.y());
        }
    }

    @Test
    void testTransformPoint() {
        try (Arena arena = Arena.ofConfined()) {
            Matrix matrix = Matrix.create(arena).initTranslate(20, 30);
            Point point = matrix.transformPoint(new Point(30, 40));
            assertEquals(50, point.x());
            assertEquals(70, point.y());
        }
    }
}
