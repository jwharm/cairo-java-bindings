package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.*;

import org.freedesktop.cairo.Cairo;
import org.junit.jupiter.api.Test;

class CairoTest {

    @Test
    void testVersionEncode() {
        assertEquals(10203, Cairo.versionEncode(1, 2, 3));
    }

    @Test
    void testVersionStringize() {
        assertEquals("1.2.3", Cairo.versionStringize(1, 2, 3));
    }

    @Test
    void testVersion() {
        assertTrue(Cairo.version() > 10000);
    }

    @Test
    void testVersionString() {
        assertNotNull(Cairo.versionString());
    }
}
