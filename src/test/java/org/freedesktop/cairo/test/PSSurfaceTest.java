package org.freedesktop.cairo.test;

import org.freedesktop.cairo.PSLevel;
import org.freedesktop.cairo.PSSurface;
import org.freedesktop.cairo.Status;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class PSSurfaceTest {

    @Test
    void testCreateStringIntInt() {
        try (PSSurface s = PSSurface.create("", 120, 120)) {
            assertEquals(s.status(), Status.SUCCESS);
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
        try (PSSurface s = PSSurface.create(stream, 120, 120)) {
            s.showPage();
            assertEquals(s.status(), Status.SUCCESS);
        }
        assertTrue(success.get());
    }

    @Test
    void testRestrictToLevel() {
        try (PSSurface s = PSSurface.create("", 120, 120)) {
            s.restrictToLevel(PSLevel.LEVEL_3);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testSetEPS() {
        try (PSSurface s = PSSurface.create("", 120, 120)) {
            s.setEPS(true);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetEPS() {
        try (PSSurface s = PSSurface.create("", 120, 120)) {
            s.getEPS();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testSetSize() {
        try (PSSurface s = PSSurface.create("", 120, 120)) {
            s.setSize(140, 140);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testDscBeginSetup() {
        try (PSSurface s = PSSurface.create("", 120, 120)) {
            s.dscBeginSetup();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testDscBeginPageSetup() {
        try (PSSurface s = PSSurface.create("", 120, 120)) {
            s.dscBeginPageSetup();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testDscComment() {
        try (PSSurface s = PSSurface.create("", 120, 120)) {
            s.dscComment("%%Title: Test");
            assertEquals(s.status(), Status.SUCCESS);
        }
    }
}