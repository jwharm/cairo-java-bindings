package org.freedesktop.cairo.test;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class SVGSurfaceTest {

    @Test
    void testCreateStringIntInt() {
        try (SVGSurface s = SVGSurface.create((String) null, 120, 120)) {
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testCreateOutputStreamIntInt() throws IOException {
        AtomicBoolean success = new AtomicBoolean();
        OutputStream stream = new OutputStream() {
            @Override
            public void write(int b) {
                success.set(true);
            }
        };
        try (SVGSurface s = SVGSurface.create(stream, 120, 120)) {
            Context cr = Context.create(s);
            cr.rectangle(10, 10, 20, 20);
            cr.fill();
            assertEquals(Status.SUCCESS, s.status());
        }
        assertTrue(success.get());
    }

    @Test
    void testGetDocumentUnit() {
        try (SVGSurface s = SVGSurface.create((String) null, 120, 120)) {
            s.getDocumentUnit();
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testSetDocumentUnit() {
        try (SVGSurface s = SVGSurface.create((String) null, 120, 120)) {
            s.setDocumentUnit(SVGUnit.CM);
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testRestrictToVersion() {
        try (SVGSurface s = SVGSurface.create((String) null, 120, 120)) {
            s.restrictToVersion(SVGVersion.VERSION_1_2);
            assertEquals(Status.SUCCESS, s.status());
        }
    }
}
