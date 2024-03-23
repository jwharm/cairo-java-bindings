package org.freedesktop.cairo.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.foreign.Arena;

import io.github.jwharm.cairobindings.Interop;
import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SurfaceTest {

    @Test
    void testCreateSimilar() {
        try (Surface s1 = ImageSurface.create(Format.ARGB32, 120, 120)) {
            Surface s2 = Surface.createSimilar(s1, Content.COLOR_ALPHA, 120, 120);
            assertEquals(Status.SUCCESS, s2.status());
        }
    }

    @Test
    void testCreateSimilarImage() {
        try (Surface s1 = ImageSurface.create(Format.ARGB32, 120, 120)) {
            Surface s2 = Surface.createSimilarImage(s1, Format.ARGB32, 120, 120);
            assertEquals(Status.SUCCESS, s2.status());
        }
    }

    @Test
    void testCreateForRectangle() {
        try (Surface s1 = ImageSurface.create(Format.ARGB32, 120, 120)) {
            Surface s2 = Surface.createForRectangle(s1, 50, 50, 20, 20);
            assertEquals(Status.SUCCESS, s2.status());
        }
    }

    @Test
    void testStatus() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testFinish() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.finish();
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testFlush() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.flush();
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testGetDevice() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.getDevice();
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testGetFontOptions() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            FontOptions options = FontOptions.create();
            s.getFontOptions(options);
            assertEquals(Status.SUCCESS, s.status());
            assertEquals(Status.SUCCESS, options.status());
        }
    }

    @Test
    void testGetContent() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            assertEquals(Content.COLOR_ALPHA, s.getContent());
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testMarkDirty() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.markDirty();
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testMarkDirtyRectangle() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.markDirtyRectangle(0, 0, 10, 10);
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testDeviceOffset() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            Point p = s.setDeviceOffset(3, 5)
                    .getDeviceOffset();
            assertEquals(3, p.x());
            assertEquals(5, p.y());
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testDeviceScale() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            Point p = s.setDeviceScale(2.5, 3.5)
                    .getDeviceScale();
            assertEquals(2.5, p.x());
            assertEquals(3.5, p.y());
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testFallbackResolution() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            Point p = s.setFallbackResolution(2.5, 3.5)
                    .getFallbackResolution();
            assertEquals(2.5, p.x());
            assertEquals(3.5, p.y());
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testGetSurfaceType() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            assertEquals(SurfaceType.IMAGE, s.getSurfaceType());
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testCopyPage() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.copyPage();
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testShowPage() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.showPage();
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testHasShowTextGlyphs() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            assertFalse(s.hasShowTextGlyphs());
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testMimeData() throws IOException {
        try (ImageSurface s1 = ImageSurface.create(Format.ARGB32, 120, 120);
                ImageSurface s2 = ImageSurface.createSimilar(s1, Content.COLOR_ALPHA, 120, 120)) {
            Context cr = Context.create(s1);
            cr.rectangle(10, 10, 20, 20);
            cr.stroke();

            var outstream = new ByteArrayOutputStream();
            s1.writeToPNG(outstream);
            byte[] data = outstream.toByteArray();

            s2.setMimeData(MimeType.PNG, data);
            byte[] result = s2.getMimeData(MimeType.PNG);

            assertEquals(data.length, result.length);
            assertEquals(Status.SUCCESS, s2.status());
        }
    }

    @Test
    void testSupportsMimeType() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            assertFalse(s.supportsMimeType(MimeType.PNG));
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testMapToImage() {
        try (Arena arena = Arena.ofConfined();
             Surface s1 = ImageSurface.create(Format.ARGB32, 120, 120);
             ImageSurface s2 = s1.mapToImage(RectangleInt.create(arena, 0, 0, 120, 120))) {
            s1.unmapImage(s2);
            assertEquals(Status.SUCCESS, s1.status());
            assertEquals(Status.SUCCESS, s2.status());
        }
    }

    @Test
    void testUserData() {
        try (Arena arena = Arena.ofConfined();
             Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            String input = "test";
            var segmentIn = Interop.allocateNativeString(input, arena);

            UserDataKey key = UserDataKey.create(arena);
            s.setUserData(key, segmentIn);

            var segmentOut = s.getUserData(key).reinterpret(Integer.MAX_VALUE);
            String output = segmentOut.getString(0);

            assertEquals(input, output);
            assertEquals(Status.SUCCESS, s.status());
        }
    }
}
