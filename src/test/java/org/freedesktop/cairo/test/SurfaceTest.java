package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.freedesktop.cairo.Content;
import org.freedesktop.cairo.Context;
import org.freedesktop.cairo.FontOptions;
import org.freedesktop.cairo.Format;
import org.freedesktop.cairo.ImageSurface;
import org.freedesktop.cairo.MimeType;
import org.freedesktop.cairo.RectangleInt;
import org.freedesktop.cairo.Status;
import org.freedesktop.cairo.Surface;
import org.freedesktop.cairo.UserDataKey;
import org.junit.jupiter.api.Test;

class SurfaceTest {

    @Test
    void testCreateSimilar() {
        try (Surface s1 = ImageSurface.create(Format.ARGB32, 120, 120)) {
            Surface s2 = Surface.createSimilar(s1, Content.COLOR_ALPHA, 120, 120);
            assertEquals(s2.status(), Status.SUCCESS);
        }
    }

    @Test
    void testCreateSimilarImage() {
        try (Surface s1 = ImageSurface.create(Format.ARGB32, 120, 120)) {
            Surface s2 = Surface.createSimilarImage(s1, Format.ARGB32, 120, 120);
            assertEquals(s2.status(), Status.SUCCESS);
        }
    }

    @Test
    void testCreateForRectangle() {
        try (Surface s1 = ImageSurface.create(Format.ARGB32, 120, 120)) {
            Surface s2 = Surface.createForRectangle(s1, 50, 50, 20, 20);
            assertEquals(s2.status(), Status.SUCCESS);
        }
    }

    @Test
    void testStatus() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testFinish() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.finish();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testFlush() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.flush();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetDevice() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.getDevice();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetFontOptions() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            FontOptions options = FontOptions.create();
            s.getFontOptions(options);
            assertEquals(s.status(), Status.SUCCESS);
            assertEquals(options.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetContent() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.getContent();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testMarkDirty() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.markDirty();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testMarkDirtyRectangle() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.markDirtyRectangle(0, 0, 10, 10);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testSetDeviceOffset() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.setDeviceOffset(0, 0);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetDeviceOffset() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.getDeviceOffset();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetDeviceScale() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.getDeviceScale();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testSetDeviceScale() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.setDeviceScale(1, 1);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testSetFallbackResolution() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.setFallbackResolution(1, 1);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetFallbackResolution() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.getFallbackResolution();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetType() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.getType();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testCopyPage() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.copyPage();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testShowPage() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.showPage();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testHasShowTextGlyphs() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.hasShowTextGlyphs();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testSetMimeData() throws IOException {
        try (ImageSurface s1 = ImageSurface.create(Format.ARGB32, 120, 120);
                ImageSurface s2 = ImageSurface.createSimilar(s1, Content.COLOR_ALPHA, 120, 120)) {
            Context cr = Context.create(s1);
            cr.rectangle(10, 10, 20, 20);
            cr.stroke();

            var outstream = new ByteArrayOutputStream();
            s1.writeToPNG(outstream);
            byte[] data = outstream.toByteArray();

            s2.setMimeData(MimeType.PNG, data);
            assertEquals(s2.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetMimeData() throws IOException {
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
            assertEquals(s2.status(), Status.SUCCESS);
        }
    }

    @Test
    void testSupportsMimeType() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.supportsMimeType(MimeType.PNG);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testMapToImage() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.mapToImage(RectangleInt.create(0, 0, 120, 120));
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testUnmapImage() {
        try (Surface s1 = ImageSurface.create(Format.ARGB32, 120, 120)) {
            ImageSurface s2 = s1.mapToImage(RectangleInt.create(0, 0, 120, 120));
            s1.unmapImage(s2);
            assertEquals(s1.status(), Status.SUCCESS);
        }
    }

    @Test
    void testSetUserData() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.setUserData("test");
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetUserData() {
        try (Surface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            String input = "test";
            UserDataKey key = s.setUserData(input);
            String output = (String) s.getUserData(key);
            assertEquals(input, output);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }
}
