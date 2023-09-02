package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.freedesktop.cairo.Mesh;
import org.freedesktop.cairo.Path;
import org.freedesktop.cairo.Point;
import org.freedesktop.cairo.Status;
import org.junit.jupiter.api.Test;

class MeshTest {

    @Test
    void testCreate() {
        Mesh mesh = Mesh.create();
        assertEquals(Status.SUCCESS, mesh.status());
    }

    @Test
    void testBeginPatch() {
        Mesh mesh = Mesh.create();
        mesh.beginPatch();
        assertEquals(Status.SUCCESS, mesh.status());
    }

    @Test
    void testEndPatch() {
        Mesh mesh = Mesh.create();
        mesh.beginPatch();
        mesh.moveTo(10, 10);
        mesh.endPatch();
        assertEquals(Status.SUCCESS, mesh.status());
    }

    @Test
    void testMoveTo() {
        Mesh mesh = Mesh.create();
        mesh.beginPatch();
        mesh.moveTo(10, 10);
        assertEquals(Status.SUCCESS, mesh.status());
    }

    @Test
    void testLineTo() {
        Mesh mesh = Mesh.create();
        mesh.beginPatch();
        mesh.lineTo(10, 10);
        assertEquals(Status.SUCCESS, mesh.status());
    }

    @Test
    void testCurveTo() {
        Mesh mesh = Mesh.create();
        mesh.beginPatch();
        mesh.curveTo(30, -30, 60, 30, 100, 0);
        assertEquals(Status.SUCCESS, mesh.status());
    }

    @Test
    void testSetControlPoint() {
        Mesh mesh = Mesh.create();
        mesh.beginPatch();
        mesh.setControlPoint(0, 10, 10);
        assertEquals(Status.SUCCESS, mesh.status());
    }

    @Test
    void testSetCornerColorRGB() {
        Mesh mesh = Mesh.create();
        mesh.beginPatch()
            .moveTo(10, 10)
            .lineTo(20, 20)
            .lineTo(30, 30)
            .setCornerColorRGB(0, 0.5, 0.5, 0.5);
        assertEquals(Status.SUCCESS, mesh.status());
    }

    @Test
    void testSetCornerColorRGBA() {
        Mesh mesh = Mesh.create();
        mesh.beginPatch()
            .moveTo(10, 10)
            .lineTo(20, 20)
            .lineTo(30, 30)
            .setCornerColorRGBA(0, 0.5, 0.5, 0.5, 1);
        assertEquals(Status.SUCCESS, mesh.status());
    }

    @Test
    void testGetPatchCount() {
        Mesh mesh = Mesh.create();
        assertEquals(0, mesh.getPatchCount());
        mesh.beginPatch();
        mesh.moveTo(10, 10);
        mesh.endPatch();
        assertEquals(1, mesh.getPatchCount());
        assertEquals(Status.SUCCESS, mesh.status());
    }

    @Test
    void testGetPath() {
        Mesh mesh = Mesh.create();
        mesh.beginPatch();
        mesh.moveTo(10, 10);
        mesh.endPatch();
        Path path = mesh.getPath(0);
        assertNotNull(path);
        assertEquals(Status.SUCCESS, mesh.status());
    }

    @Test
    void testGetControlPoint() {
        Mesh mesh = Mesh.create();
        mesh.beginPatch();
        mesh.setControlPoint(0, 10, 10);
        mesh.moveTo(20, 20);
        mesh.endPatch();
        Point point = mesh.getControlPoint(0, 0);
        assertNotNull(point);
        assertEquals(Status.SUCCESS, mesh.status());
    }

    @Test
    void testGetCornerColorRGBA() {
        Mesh mesh = Mesh.create();
        mesh.beginPatch()
            .moveTo(10, 10)
            .lineTo(20, 20)
            .lineTo(30, 30)
            .setCornerColorRGBA(0, 0.5, 0.6, 0.7, 0.8)
            .endPatch();
        double[] color = mesh.getCornerColorRGBA(0, 0);
        assertEquals(color.length, 4);
        assertEquals(0.5, color[0]);
        assertEquals(0.6, color[1]);
        assertEquals(0.7, color[2]);
        assertEquals(0.8, color[3]);
        assertEquals(Status.SUCCESS, mesh.status());
    }
}
