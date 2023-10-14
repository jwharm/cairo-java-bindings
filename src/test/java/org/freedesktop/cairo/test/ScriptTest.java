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
            assertEquals(Status.SUCCESS, s.status());
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
            assertEquals(Status.SUCCESS, s.status());
        }
        assertTrue(success.get());
    }

    @Test
    void testFrom() {
        try (Script s = Script.create(tempDir.resolve("test.script").toString())) {
            s.from(RecordingSurface.create(Content.COLOR_ALPHA, null));
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testMode() {
        try (Script s = Script.create(tempDir.resolve("test.script").toString())) {
            s.setMode(ScriptMode.ASCII);
            assertEquals(ScriptMode.ASCII, s.getMode());
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testCreateScriptSurface() {
        try (Script s = Script.create(tempDir.resolve("test.script").toString())) {
            s.createScriptSurface(Content.COLOR, 30, 30);
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testCreateScriptSurfaceForTarget() {
        try (Script s = Script.create(tempDir.resolve("test.script").toString())) {
            s.createScriptSurfaceForTarget(ImageSurface.create(Format.ARGB32, 120, 120));
            assertEquals(Status.SUCCESS, s.status());
        }
    }

    @Test
    void testWriteComment() {
        try (Script s = Script.create(tempDir.resolve("test.script").toString())) {
            s.writeComment("test");
            assertEquals(Status.SUCCESS, s.status());
        }
    }
}