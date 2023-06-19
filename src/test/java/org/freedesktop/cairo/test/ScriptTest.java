package org.freedesktop.cairo.test;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ScriptTest {

    @Test
    void testCreateString() {
        try (Script s = Script.create("")) {
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testCreateOutputStream() {
        AtomicBoolean success = new AtomicBoolean();
        OutputStream stream = new OutputStream() {
            @Override
            public void write(int b) {
                success.set(true);
            }
        };
        try (Script s = Script.create(stream)) {
            assertEquals(s.status(), Status.SUCCESS);
        }
        assertTrue(success.get());
    }

    @Test
    void testFrom() {
        try (Script s = Script.create("")) {
            s.from(RecordingSurface.create(Content.COLOR_ALPHA, Rectangle.create(10, 10, 20, 20)));
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetMode() {
        try (Script s = Script.create("")) {
            s.getMode();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testSetMode() {
        try (Script s = Script.create("")) {
            s.setMode(ScriptMode.ASCII);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testCreateScriptSurface() {
        try (Script s = Script.create("")) {
            s.createScriptSurface(Content.COLOR, 30, 30);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testCreateScriptSurfaceForTarget() {
        try (Script s = Script.create("")) {
            s.createScriptSurfaceForTarget(ImageSurface.create(Format.ARGB32, 120, 120));
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testWriteComment() {
        try (Script s = Script.create("")) {
            s.writeComment("test");
            assertEquals(s.status(), Status.SUCCESS);
        }
    }
}