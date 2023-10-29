package org.freedesktop.cairo.test;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class UserFontFaceTest {

    private Context createContext() {
        try {
            return Context.create(ImageSurface.create(Format.ARGB32, 120, 120));
        } catch (IOException ioe) {
            fail(ioe);
            throw new RuntimeException(ioe);
        }
    }

    @Test
    void testCreate() {
        UserFontFace uf = UserFontFace.create();
        assertEquals(Status.SUCCESS, uf.status());
    }

    @Test
    void testInit() {
        UserFontFace uf = UserFontFace.create();
        AtomicBoolean flag = new AtomicBoolean(false);
        UserScaledFontInitFunc func = (font, cr, extents) -> flag.set(true);
        uf.setInitFunc(func);
        Context cr = createContext();
        cr.setFontFace(uf);
        cr.showText("test");
        assertTrue(flag.get());
        assertEquals(func, uf.getInitFunc());
        assertEquals(Status.SUCCESS, uf.status());
    }

    @Test
    void testRenderGlyph() {
        UserFontFace uf = UserFontFace.create();
        AtomicBoolean flag = new AtomicBoolean(false);
        UserScaledFontRenderGlyphFunc func = (font, glyph, cr, extents) -> flag.set(true);
        uf.setRenderGlyphFunc(func);
        Context cr = createContext();
        cr.setFontFace(uf);
        cr.showText("test");
        assertTrue(flag.get());
        assertEquals(func, uf.getRenderGlyphFunc());
        assertEquals(Status.SUCCESS, uf.status());
    }

    @Test
    void testRenderColorGlyph() {
        UserFontFace uf = UserFontFace.create();
        AtomicBoolean flag = new AtomicBoolean(false);
        UserScaledFontRenderGlyphFunc func = (font, glyph, cr, extents) -> flag.set(true);
        uf.setRenderColorGlyphFunc(func);
        Context cr = createContext();
        cr.setFontFace(uf);
        cr.showText("test");
        assertTrue(flag.get());
        assertEquals(func, uf.getRenderColorGlyphFunc());
        assertEquals(Status.SUCCESS, uf.status());
    }

    @Test
    void testRenderColorGlyph_Fallback() {
        UserFontFace uf = UserFontFace.create();
        AtomicBoolean flag = new AtomicBoolean(false);
        UserScaledFontRenderGlyphFunc renderColorFunc = (font, glyph, cr, extents) -> {
            throw new UnsupportedOperationException(); // should redirect to renderFunc
        };
        UserScaledFontRenderGlyphFunc renderFunc = (font, glyph, cr, extents) -> flag.set(true);
        uf.setRenderColorGlyphFunc(renderColorFunc);
        uf.setRenderGlyphFunc(renderFunc);
        Context cr = createContext();
        cr.setFontFace(uf);
        cr.showText("test");
        assertTrue(flag.get());
        assertEquals(Status.SUCCESS, uf.status());
    }

    @Test
    void testUnicodeToGlyph() {
        UserFontFace uf = UserFontFace.create();
        AtomicBoolean flag = new AtomicBoolean(false);
        UserScaledFontUnicodeToGlyphFunc func = (font, unicode) -> {
            flag.set(true);
            return 0;
        };
        uf.setUnicodeToGlyphFunc(func);
        Context cr = createContext();
        cr.setFontFace(uf);
        cr.showText("test");
        assertTrue(flag.get());
        assertEquals(func, uf.getUnicodeToGlyphFunc());
        assertEquals(Status.SUCCESS, uf.status());
    }

    @Test
    void testTextToGlyphs() {
        UserFontFace uf = UserFontFace.create();
        AtomicBoolean flag = new AtomicBoolean(false);
        UserScaledFontTextToGlyphsFunc func = (font, string, glyphs) -> {
            flag.set(true);
        };
        uf.setTextToGlyphsFunc(func);
        Context cr = createContext();
        cr.setFontFace(uf);
        cr.showText("test");
        assertTrue(flag.get());
        assertEquals(func, uf.getTextToGlyphsFunc());
        assertEquals(Status.SUCCESS, uf.status());
    }
}
