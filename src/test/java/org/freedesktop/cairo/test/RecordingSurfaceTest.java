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
            assertEquals(Status.SUCCESS, s.status());
            assertEquals(Status.SUCCESS, r.status());
        }
    }

    @Test
    void testInkExtents() throws IOException {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
             RecordingSurface r = RecordingSurface.create(Content.COLOR_ALPHA, null)) {
            Context.create(r)
                    .rectangle(12, 14, 16, 18)
                    .fill();
            Rectangle rect = r.inkExtents();
            assertEquals(12, rect.x());
            assertEquals(14, rect.y());
            assertEquals(16, rect.width());
            assertEquals(18, rect.height());
            assertEquals(Status.SUCCESS, s.status());
            assertEquals(Status.SUCCESS, r.status());
        }
    }

    @Test
    void testGetExtents() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
             RecordingSurface r = RecordingSurface.create(Content.COLOR_ALPHA, Rectangle.create(20, 30, 50, 60))) {
            Rectangle rect = r.getExtents();
            assertEquals(20, rect.x());
            assertEquals(30, rect.y());
            assertEquals(50, rect.width());
            assertEquals(60, rect.height());
            assertEquals(Status.SUCCESS, s.status());
            assertEquals(Status.SUCCESS, r.status());
        }
    }
}
