package org.freedesktop.cairo.test;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ScriptTest {

    @TempDir
    Path tempDir;

    @Test
    void testCreateString() {
        try (Script s = Script.create(tempDir.resolve("test.script").toString())) {
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
        try (Script s = Script.create(tempDir.resolve("test.script").toString())) {
            s.from(RecordingSurface.create(Content.COLOR_ALPHA, Rectangle.create(10, 10, 20, 20)));
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetMode() {
        try (Script s = Script.create(tempDir.resolve("test.script").toString())) {
            s.getMode();
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testSetMode() {
        try (Script s = Script.create(tempDir.resolve("test.script").toString())) {
            s.setMode(ScriptMode.ASCII);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testCreateScriptSurface() {
        try (Script s = Script.create(tempDir.resolve("test.script").toString())) {
            s.createScriptSurface(Content.COLOR, 30, 30);
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testCreateScriptSurfaceForTarget() {
        try (Script s = Script.create(tempDir.resolve("test.script").toString())) {
            s.createScriptSurfaceForTarget(ImageSurface.create(Format.ARGB32, 120, 120));
            assertEquals(s.status(), Status.SUCCESS);
        }
    }

    @Test
    void testWriteComment() {
        try (Script s = Script.create(tempDir.resolve("test.script").toString())) {
            s.writeComment("test");
            assertEquals(s.status(), Status.SUCCESS);
        }
    }
}