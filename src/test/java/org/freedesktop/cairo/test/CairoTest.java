package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.*;

import org.freedesktop.cairo.Cairo;
import org.junit.jupiter.api.Test;

class CairoTest {

    @Test
    void testVersionEncode() {
        assertEquals(Cairo.versionEncode(1, 2, 3), 10203);
    }

    @Test
    void testVersionStringize() {
        assertEquals(Cairo.versionStringize(1, 2, 3), "1.2.3");
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
