package org.freedesktop.cairo.test;

import java.io.*;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
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

import static org.junit.jupiter.api.Assertions.*;

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
        assertNotEquals(-1, stride);
    }

    @Test
    void testCreateFormatIntInt() {
        try (ImageSurface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testCreateMemorySegmentFormatIntIntInt() {
        try (Arena arena = Arena.ofConfined()) {
            int stride = ImageSurface.formatStrideForWidth(Format.ARGB32, 120);
            MemorySegment data = arena.allocate(120L * 120 * stride);
            try (ImageSurface s = ImageSurface.create(data, Format.ARGB32, 120, 120, stride)) {
                assertEquals(Status.SUCCESS, s.status());
            }
        }
    }

    @Test
    void testWriteAndCreatePNG_String() throws IOException {
        try (ImageSurface s1 = ImageSurface.create(Format.ARGB32, 120, 120)) {
            Context cr = Context.create(s1);
            cr.rectangle(10, 10, 20, 20);
            cr.stroke();
            String filename = tempDir.resolve("test.png").toString();
            s1.writeToPNG(filename);
            try (ImageSurface s2 = ImageSurface.createFromPNG(filename)) {
                assertEquals(Status.SUCCESS, s2.status());
            }
        }
    }

    @Test
    void testWriteAndCreatePNG_Stream() throws IOException {
        try (ImageSurface s1 = ImageSurface.create(Format.ARGB32, 120, 120)) {
            Context cr = Context.create(s1);
            cr.rectangle(10, 10, 20, 20);
            cr.stroke();
            String filename = tempDir.resolve("test.png").toString();
            try (OutputStream outStream = new FileOutputStream(filename)) {
                s1.writeToPNG(outStream);
            }
            try (InputStream inStream = new FileInputStream(filename);
                 ImageSurface s2 = ImageSurface.createFromPNG(inStream)) {
                assertEquals(Status.SUCCESS, s2.status());
            }
        }
    }

    @Test
    void testGetData() {
        try (ImageSurface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            MemorySegment data = s.getData();
            assertNotEquals(MemorySegment.NULL, data);
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testGetFormat() {
        try (ImageSurface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            assertEquals(Format.ARGB32, s.getFormat());
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testGetWidth() {
        try (ImageSurface s = ImageSurface.create(Format.ARGB32, 120, 100)) {
            assertEquals(120, s.getWidth());
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testGetHeight() {
        try (ImageSurface s = ImageSurface.create(Format.ARGB32, 100, 120)) {
            assertEquals(120, s.getHeight());
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testGetStride() {
        try (ImageSurface s = ImageSurface.create(Format.ARGB32, 120, 120)) {
            assertEquals(480, s.getStride());
            assertEquals(Status.SUCCESS, s.status());
        }
    }
}
