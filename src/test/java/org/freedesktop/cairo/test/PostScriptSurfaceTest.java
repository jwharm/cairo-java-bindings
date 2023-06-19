package org.freedesktop.cairo.test;

import org.freedesktop.cairo.PostScriptLevel;
import org.freedesktop.cairo.PostScriptSurface;
import org.freedesktop.cairo.Status;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class PostScriptSurfaceTest {

    @Test
    void testCreateStringIntInt() {
        try (PostScriptSurface s = PostScriptSurface.create("", 120, 120)) {
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
        try (PostScriptSurface s = PostScriptSurface.create(stream, 120, 120)) {
            s.showPage();
            assertEquals(s.status(), Status.SUCCESS);
        }
        assertTrue(success.get());
    }

    @Test
    void testRestrictToLevel() {
        try (PostScriptSurface s = PostScriptSurface.create("", 120, 120)) {
            s.restrictToLevel(PostScriptLevel.LEVEL_3);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testSetEPS() {
        try (PostScriptSurface s = PostScriptSurface.create("", 120, 120)) {
            s.setEPS(true);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetEPS() {
        try (PostScriptSurface s = PostScriptSurface.create("", 120, 120)) {
            s.getEPS();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testSetSize() {
        try (PostScriptSurface s = PostScriptSurface.create("", 120, 120)) {
            s.setSize(140, 140);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testDscBeginSetup() {
        try (PostScriptSurface s = PostScriptSurface.create("", 120, 120)) {
            s.dscBeginSetup();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testDscBeginPageSetup() {
        try (PostScriptSurface s = PostScriptSurface.create("", 120, 120)) {
            s.dscBeginPageSetup();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testDscComment() {
        try (PostScriptSurface s = PostScriptSurface.create("", 120, 120)) {
            s.dscComment("%%Title: Test");
            assertEquals(s.status(), Status.SUCCESS);
        }
    }
}