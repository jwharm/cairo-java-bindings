package org.freedesktop.cairo.test;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserScaledFontTest {

    @Test
    void testForegroundMarker() throws IOException {
        UserFontFace uf = UserFontFace.create();
        AtomicBoolean flag = new AtomicBoolean(false);
        uf.setRenderColorGlyphFunc((font, glyph, cr, extents) -> {
            Pattern p = font.getForegroundMarker();
            flag.set(Status.SUCCESS.equals(p.status()));
        });
        try (SVGSurface s = SVGSurface.create((String) null, 120, 120)) {
            Context cr = Context.create(s);
            cr.setFontFace(uf);
            cr.showText("test");
            assertTrue(flag.get());
            assertEquals(Status.SUCCESS, uf.status());
        }
    }

    @Test
    void testForegroundSource() throws IOException {
        UserFontFace uf = UserFontFace.create();
        AtomicBoolean flag = new AtomicBoolean(false);
        uf.setRenderColorGlyphFunc((font, glyph, cr, extents) -> {
            Pattern p = font.getForegroundSource();
            flag.set(Status.SUCCESS.equals(p.status()));
        });
        try (SVGSurface s = SVGSurface.create((String) null, 120, 120)) {
            Context cr = Context.create(s);
            cr.setFontFace(uf);
            cr.showText("test");
            assertTrue(flag.get());
            assertEquals(Status.SUCCESS, uf.status());
        }
    }
}
