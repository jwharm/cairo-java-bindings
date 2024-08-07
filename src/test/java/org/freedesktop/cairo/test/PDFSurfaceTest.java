package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.OutputStream;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;

class PDFSurfaceTest {

    @Test
    void testCreateStringIntInt() {
        try (PDFSurface s = PDFSurface.create((String) null, 120, 120)) {
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testCreateOutputStreamIntInt() {
        AtomicBoolean success = new AtomicBoolean();
        OutputStream stream = new OutputStream() {
            @Override
            public void write(int b) {
                success.set(true);
            }
        };
        try (PDFSurface s = PDFSurface.create(stream, 120, 120)) {
            s.showPage();
            assertEquals(Status.SUCCESS, s.status());
        }
        assertTrue(success.get());
    }

    @Test
    void testRestrictToVersion() {
        try (PDFSurface s = PDFSurface.create((String) null, 120, 120)) {
            s.restrictToVersion(PDFVersion.VERSION_1_4);
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testSetSize() {
        try (PDFSurface s = PDFSurface.create((String) null, 120, 120)) {
            s.setSize(100, 100);
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testAddOutline() {
        try (PDFSurface s = PDFSurface.create((String) null, 120, 120)) {
            // This verifies the method call runs, but the result will not be successful
            s.addOutline(PDFSurface.CAIRO_PDF_OUTLINE_ROOT, "test", "test", Set.of(PDFOutlineFlags.ITALIC));
        }
    }

    @Test
    void testSetMetadata() {
        try (PDFSurface s = PDFSurface.create((String) null, 120, 120)) {
            s.setMetadata(PDFMetadata.TITLE, "test document");
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testSetCustomMetadata() {
        try (PDFSurface s = PDFSurface.create((String) null, 120, 120)) {
            s.setCustomMetadata("ISBN", "978-0123456789");
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testSetPageLabel() {
        try (PDFSurface s = PDFSurface.create((String) null, 120, 120)) {
            s.setPageLabel("label");
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testSetThumbnailSize() {
        try (PDFSurface s = PDFSurface.create((String) null, 120, 120)) {
            s.setThumbnailSize(30, 30);
            assertEquals(Status.SUCCESS, s.status());
        }
    }
}
