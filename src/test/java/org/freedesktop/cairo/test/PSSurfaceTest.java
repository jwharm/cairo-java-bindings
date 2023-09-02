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
        try (PSSurface s = PSSurface.create((String) null, 120, 120)) {
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
        try (PSSurface s = PSSurface.create(stream, 120, 120)) {
            s.showPage();
            assertEquals(Status.SUCCESS, s.status());
        }
        assertTrue(success.get());
    }

    @Test
    void testRestrictToLevel() {
        try (PSSurface s = PSSurface.create((String) null, 120, 120)) {
            s.restrictToLevel(PSLevel.LEVEL_3);
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testSetEPS() {
        try (PSSurface s = PSSurface.create((String) null, 120, 120)) {
            s.setEPS(true);
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testGetEPS() {
        try (PSSurface s = PSSurface.create((String) null, 120, 120)) {
            s.getEPS();
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testSetSize() {
        try (PSSurface s = PSSurface.create((String) null, 120, 120)) {
            s.setSize(140, 140);
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testDscBeginSetup() {
        try (PSSurface s = PSSurface.create((String) null, 120, 120)) {
            s.dscBeginSetup();
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testDscBeginPageSetup() {
        try (PSSurface s = PSSurface.create((String) null, 120, 120)) {
            s.dscBeginPageSetup();
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testDscComment() {
        try (PSSurface s = PSSurface.create((String) null, 120, 120)) {
            s.dscComment("%%Title: Test");
            assertEquals(Status.SUCCESS, s.status());
        }
    }
}