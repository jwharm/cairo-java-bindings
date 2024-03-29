package org.freedesktop.cairo.test;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.lang.foreign.Arena;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DeviceTest {

    @TempDir
    Path tempDir;

    @Test
    void status() {
        try (Device d = Script.create(tempDir.resolve("test.script").toString())) {
            assertEquals(Status.SUCCESS, d.status());
        }
    }

    @Test
    void finish() {
        try (Device d = Script.create(tempDir.resolve("test.script").toString())) {
            d.finish();
            assertEquals(Status.SUCCESS, d.status());
        }
    }

    @Test
    void flush() {
        try (Device d = Script.create(tempDir.resolve("test.script").toString())) {
            d.flush();
            assertEquals(Status.SUCCESS, d.status());
        }
    }

    @Test
    void getDeviceType() {
        try (Device d = Script.create(tempDir.resolve("test.script").toString())) {
            DeviceType type = d.getDeviceType();
            assertEquals(DeviceType.SCRIPT, type);
            assertEquals(Status.SUCCESS, d.status());
        }
    }

    @Test
    void acquireAndRelease() {
        try (Device d = Script.create(tempDir.resolve("test.script").toString())) {
            d.acquire();
            d.release();
            assertEquals(d.status(), Status.SUCCESS);
        }
    }

    @Test
    void testUserData() {
        try (Arena arena = Arena.ofConfined();
             Device device = Script.create(tempDir.resolve("test.script").toString())) {
            Rectangle input = Rectangle.create(arena, 0, 0, 10, 10);
            UserDataKey key = UserDataKey.create(arena);
            device.setUserData(key, input.handle());
            Rectangle output = new Rectangle(device.getUserData(key));
            assertEquals(input, output);
            assertEquals(device.status(), Status.SUCCESS);
        }
    }
}
