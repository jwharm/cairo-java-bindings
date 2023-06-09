package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.nio.file.Files;
import java.nio.file.Path;

import org.freedesktop.cairo.Context;
import org.freedesktop.cairo.Format;
import org.freedesktop.cairo.ImageSurface;
import org.freedesktop.cairo.Status;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ImageSurfaceTest {

    @TempDir
    static Path tempDir;

    /* 
     * Workaround for issue where the test fails because JUnit cannot delete 
     * the temp directory: Explicitly create and delete the directory
     */
    
    @BeforeAll
    public static void before() throws IOException {
        tempDir = Files.createTempDirectory(null);
    }

    @AfterAll
    public static void after() {
        tempDir.toFile().delete();
    }
    
    @Test
    void testFormatStrideForWidth() {
        int stride = ImageSurface.formatStrideForWidth(Format.ARGB32, 120);
        assertNotEquals(stride, -1);
    }

    @Test
    void testCreateFormatIntInt() {
        try (ImageSurface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testCreateMemorySegmentFormatIntIntInt() {
        int stride = ImageSurface.formatStrideForWidth(Format.ARGB32, 120);
        MemorySegment data = SegmentAllocator.nativeAllocator(SegmentScope.auto()).allocate(120L * 120 * stride);
        try (ImageSurface s = ImageSurface.create(data, Format.ARGB32, 120, 120, stride)) {
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testCreateFromPNGString() throws IOException {
        try (ImageSurface s1 = ImageSurface.create(Format.ARGB32, 120, 120)) {
            Context cr = Context.create(s1);
            cr.rectangle(10, 10, 20, 20);
            cr.stroke();
            String filename = tempDir.resolve("test.png").toString();
            s1.writeToPNG(filename);
            ImageSurface s2 = ImageSurface.createFromPNG(filename);
            assertEquals(s2.status(), Status.SUCCESS);
        }
    }

    @Test
    void testCreateFromPNGInputStream() throws IOException {
        try (ImageSurface s1 = ImageSurface.create(Format.ARGB32, 120, 120)) {
            Context cr = Context.create(s1);
            cr.rectangle(10, 10, 20, 20);
            cr.stroke();
            String filename = tempDir.resolve("test.png").toString();
            s1.writeToPNG(filename);
            ImageSurface s2 = ImageSurface.createFromPNG(new FileInputStream(filename));
            assertEquals(s2.status(), Status.SUCCESS);
        }
    }

    @Test
    void testWriteToPNGString() throws IOException {
        try (ImageSurface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            Context cr = Context.create(s);
            cr.rectangle(10, 10, 20, 20);
            cr.stroke();
            String filename = tempDir.resolve("test.png").toString();
            s.writeToPNG(filename);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testWriteToPNGOutputStream() throws IOException {
        try (ImageSurface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            Context cr = Context.create(s);
            cr.rectangle(10, 10, 20, 20);
            cr.stroke();
            String filename = tempDir.resolve("test.png").toString();
            s.writeToPNG(new FileOutputStream(filename));
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetData() {
        try (ImageSurface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.getData();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetFormat() {
        try (ImageSurface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.getFormat();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetWidth() {
        try (ImageSurface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.getWidth();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetHeight() {
        try (ImageSurface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.getHeight();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetStride() {
        try (ImageSurface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            s.getStride();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }
}
