package org.freedesktop.cairo.test;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeviceTest {

    @Test
    void status() {
        try (Device d = Script.create("")) {
            assertEquals(d.status(), Status.SUCCESS);
        }
    }

    @Test
    void finish() {
        try (Device d = Script.create("")) {
            d.finish();
            assertEquals(d.status(), Status.SUCCESS);
        }
    }

    @Test
    void flush() {
        try (Device d = Script.create("")) {
            d.flush();
            assertEquals(d.status(), Status.SUCCESS);
        }
    }

    @Test
    void getType() {
        try (Device d = Script.create("")) {
            d.getType();
            assertEquals(d.status(), Status.SUCCESS);
        }
    }

    @Test
    void acquire() {
        try (Device d = Script.create("")) {
            d.acquire();
            d.release();
            assertEquals(d.status(), Status.SUCCESS);
        }
    }

    @Test
    void release() {
        try (Device d = Script.create("")) {
            d.acquire();
            d.release();
            assertEquals(d.status(), Status.SUCCESS);
        }
    }

    @Test
    void testSetUserData() {
        try (Device d = Script.create("")) {
            d.setUserData(Rectangle.create(0, 0, 10, 10));
            assertEquals(d.status(), Status.SUCCESS);
        }
    }

    @Test
    void testGetUserData() {
        try (Device d = Script.create("")) {
            Rectangle input = Rectangle.create(0, 0, 10, 10);
            UserDataKey key = d.setUserData(input);
            Rectangle output = (Rectangle) d.getUserData(key);
            assertEquals(input, output);
            assertEquals(d.status(), Status.SUCCESS);
        }
    }
}