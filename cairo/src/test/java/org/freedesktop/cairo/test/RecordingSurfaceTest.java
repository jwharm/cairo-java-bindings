package org.freedesktop.cairo.test;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RecordingSurfaceTest {

    @Test
    void testCreate() throws IOException {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
             RecordingSurface r = RecordingSurface.create(Content.COLOR_ALPHA, Rectangle.create(20, 20, 50, 50))) {
            assertEquals(s.status(), Status.SUCCESS);
            assertEquals(r.status(), Status.SUCCESS);
        }
    }

    @Test
    void testInkExtents() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
             RecordingSurface r = RecordingSurface.create(Content.COLOR_ALPHA, Rectangle.create(20, 20, 50, 50))) {
            r.inkExtents();
            assertEquals(s.status(), Status.SUCCESS);
            assertEquals(r.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetExtents() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
             RecordingSurface r = RecordingSurface.create(Content.COLOR_ALPHA, Rectangle.create(20, 20, 50, 50))) {
            r.getExtents();
            assertEquals(s.status(), Status.SUCCESS);
            assertEquals(r.status(), Status.SUCCESS);
        }
    }
}
