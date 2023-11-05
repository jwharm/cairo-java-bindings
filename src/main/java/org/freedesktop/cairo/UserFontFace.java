/* cairo-java-bindings - Java language bindings for cairo
 * Copyright (C) 2023 Jan-Willem Harmannij
 *
 * SPDX-License-Identifier: LGPL-2.1-or-later
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://www.gnu.org/licenses/>.
 */

package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Interop;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.ref.Cleaner;

/**
 * Font support with font data provided by the user.
 * <p>
 * The user-font feature allows the cairo user to provide drawings for glyphs in a
 * font. This is most useful in implementing fonts in non-standard formats, like SVG
 * fonts and Flash fonts, but can also be used by games and other application to
 * draw "funky" fonts.
 *
 * @since 1.8
 */
public final class UserFontFace extends FontFace {

    static {
        Cairo.ensureInitialized();
    }

    // Cleaner used to close the arena
    private static final Cleaner CLEANER = Cleaner.create();

    // Arena used to allocate the upcall stubs for the callback functions
    private final Arena arena = Arena.ofShared();

    // Keep a reference to the callback functions that are passed to the
    // UserScaledFont during its lifetime.
    private UserScaledFontInitFunc initFunc = null;
    private UserScaledFontRenderGlyphFunc renderGlyphFunc = null;
    private UserScaledFontRenderGlyphFunc renderColorGlyphFunc = null;
    private UserScaledFontTextToGlyphsFunc textToGlyphsFunc = null;
    private UserScaledFontUnicodeToGlyphFunc unicodeToGlyphFunc = null;

    /**
     * Constructor used internally to instantiate a java UserFontFace object for a
     * native {@code cairo_font_face} instance
     *
     * @param address the memory address of the native {@code cairo_font_face}
     *                instance
     */
    public UserFontFace(MemorySegment address) {
        super(address);

        // Setup a Cleaner to close the Arena and release the allocated memory for
        // the callback functions
        CleanupAction cleanup = new CleanupAction(arena);
        CLEANER.register(this, cleanup);
    }

    /**
     * Creates a new user font-face.
     * <p>
     * Use the setter functions to associate callbacks with the returned user font.
     * The only mandatory callback is render_glyph.
     * <p>
     * After the font-face is created, the user can attach arbitrary data (the
     * actual font data) to it using {@link FontFace#setUserData} and access it
     * from the user-font callbacks by using {@link ScaledFont#getFontFace()} 
     * followed by {@link FontFace#getUserData}.
     *
     * @return a newly created UserFontFace
     * @since 1.8
     */
    public static UserFontFace create() {
        try {
            MemorySegment result = (MemorySegment) cairo_user_font_face_create.invoke();
            return new UserFontFace(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_user_font_face_create = Interop.downcallHandle(
            "cairo_user_font_face_create", FunctionDescriptor.of(ValueLayout.ADDRESS));

    /**
     * Sets the scaled-font initialization function of a user-font. See
     * {@link UserScaledFontInitFunc} for details of how the callback works.
     * <p>
     * The font-face should not be immutable or an {@link IllegalStateException}
     * will be thrown. A user font-face is immutable as soon as a scaled-font is
     * created from it.
     *
     * @param initFunc The init callback, or {@code null}
     * @since 1.8
     */
    public void setInitFunc(UserScaledFontInitFunc initFunc) throws IllegalStateException {
        try {
            cairo_user_font_face_set_init_func.invoke(handle(),
                    initFunc == null ? MemorySegment.NULL : initFunc.toCallback(arena));
            this.initFunc = initFunc;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status() == Status.USER_FONT_IMMUTABLE) {
            throw new IllegalStateException(status().toString());
        }
    }

    private static final MethodHandle cairo_user_font_face_set_init_func = Interop.downcallHandle(
            "cairo_user_font_face_set_init_func", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS));

    /**
     * Gets the scaled-font initialization function of a user-font.
     *
     * @return The init callback of this font-face or {@code null} if none set or an
     *         error has occurred.
     * @since 1.8
     */
    public UserScaledFontInitFunc getInitFunc() {
        return this.initFunc;
    }

    /**
     * Sets the glyph rendering function of a user-font. See
     * {@link UserScaledFontRenderGlyphFunc} for details of how the callback works.
     * <p>
     * The font-face should not be immutable or an {@link IllegalStateException}
     * will be thrown. A user font-face is immutable as soon as a scaled-font is
     * created from it.
     * <p>
     * The render_glyph callback is the only mandatory callback of a user-font. At
     * least one of {@link #setRenderColorGlyphFunc} or {@code setRenderGlyphFunc}
     * must be called to set a render callback. If both callbacks are set, the color
     * glyph render callback is invoked first. If the color glyph render callback
     * throws {@link UnsupportedOperationException}, the non-color version of the
     * callback is invoked.
     * <p>
     * If the callback is {@code null} and a glyph is tried to be rendered using
     * this font-face, an {@link IllegalStateException} will be thrown.
     *
     * @param renderGlyphFunc The render_glyph callback, or {@code null}
     * @throws IllegalStateException when the font-face is immutable, or the
     *                               callback is null and a glyph is tried to be
     *                               rendered using this font-face
     * @since 1.8
     */
    public void setRenderGlyphFunc(UserScaledFontRenderGlyphFunc renderGlyphFunc) throws IllegalStateException {
        try {
            cairo_user_font_face_set_render_glyph_func.invoke(handle(),
                    renderGlyphFunc == null ? MemorySegment.NULL : renderGlyphFunc.toCallback(arena));
            this.renderGlyphFunc = renderGlyphFunc;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status() == Status.USER_FONT_IMMUTABLE || status() == Status.USER_FONT_ERROR) {
            throw new IllegalStateException(status().toString());
        }
    }

    private static final MethodHandle cairo_user_font_face_set_render_glyph_func = Interop.downcallHandle(
            "cairo_user_font_face_set_render_glyph_func", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS));

    /**
     * Gets the glyph rendering function of a user-font.
     *
     * @return The render_glyph callback of this font-face or {@code null} if none
     *         set or an error has occurred.
     * @since 1.8
     */
    public UserScaledFontRenderGlyphFunc getRenderGlyphFunc() {
        return this.renderGlyphFunc;
    }

    /**
     * Sets the color glyph rendering function of a user-font. See
     * {@link UserScaledFontRenderGlyphFunc} for details of how the callback works.
     * <p>
     * The font-face should not be immutable or an {@link IllegalStateException}
     * will be thrown. A user font-face is immutable as soon as a scaled-font is
     * created from it.
     * <p>
     * The render_glyph callback is the only mandatory callback of a user-font. At
     * least one of {@code setRenderColorGlyphFunc} or {@link #setRenderGlyphFunc}
     * must be called to set a render callback. If both callbacks are set, the color
     * glyph render callback is invoked first. If the color glyph render callback
     * throws {@link UnsupportedOperationException}, the non-color version of the
     * callback is invoked.
     * <p>
     * If the callback is {@code null} and a glyph is tried to be rendered using
     * this font-face, an {@link IllegalStateException} will be thrown.
     *
     * @param renderGlyphFunc The render_glyph callback, or {@code null}
     * @throws IllegalStateException when the font-face is immutable, or the
     *                               callback is null and a glyph is tried to be
     *                               rendered using this font-face
     * @since 1.8
     */
    public void setRenderColorGlyphFunc(UserScaledFontRenderGlyphFunc renderGlyphFunc) throws IllegalStateException {
        try {
            cairo_user_font_face_set_render_color_glyph_func.invoke(handle(),
                    renderGlyphFunc == null ? MemorySegment.NULL : renderGlyphFunc.toCallback(arena));
            this.renderColorGlyphFunc = renderGlyphFunc;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status() == Status.USER_FONT_IMMUTABLE || status() == Status.USER_FONT_ERROR) {
            throw new IllegalStateException(status().toString());
        }
    }

    private static final MethodHandle cairo_user_font_face_set_render_color_glyph_func = Interop.downcallHandle(
            "cairo_user_font_face_set_render_color_glyph_func", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS));

    /**
     * Gets the color glyph rendering function of a user-font.
     *
     * @return The render_glyph callback of this font-face or {@code null} if none
     * set or an error has occurred.
     * @since 1.8
     */
    public UserScaledFontRenderGlyphFunc getRenderColorGlyphFunc() {
        return this.renderColorGlyphFunc;
    }

    /**
     * Sets the unicode-to-glyph conversion function of a user-font. See
     * {@link UserScaledFontUnicodeToGlyphFunc} for details of how the
     * callback works.
     * <p>
     * The font-face should not be immutable or an {@link IllegalStateException}
     * will be thrown. A user font-face is immutable as soon as a scaled-font is
     * created from it.
     *
     * @param unicodeToGlyphFunc The unicode_to_glyph callback, or {@code null}
     * @throws IllegalStateException when the font-face is immutable
     * @since 1.8
     */
    public void setUnicodeToGlyphFunc(UserScaledFontUnicodeToGlyphFunc unicodeToGlyphFunc) throws IllegalStateException {
        try {
            cairo_user_font_face_set_unicode_to_glyph_func.invoke(handle(),
                    unicodeToGlyphFunc == null ? MemorySegment.NULL : unicodeToGlyphFunc.toCallback(arena));
            this.unicodeToGlyphFunc = unicodeToGlyphFunc;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status() == Status.USER_FONT_IMMUTABLE) {
            throw new IllegalStateException(status().toString());
        }
    }

    private static final MethodHandle cairo_user_font_face_set_unicode_to_glyph_func = Interop.downcallHandle(
            "cairo_user_font_face_set_unicode_to_glyph_func", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS));

    /**
     * Gets the unicode-to-glyph conversion function of a user-font.
     *
     * @return The unicode_to_glyph callback of this font-face or {@code null} if
     * none set or an error occurred.
     * @since 1.8
     */
    public UserScaledFontUnicodeToGlyphFunc getUnicodeToGlyphFunc() {
        return this.unicodeToGlyphFunc;
    }

    /**
     * Sets th text-to-glyphs conversion function of a user-font. See
     * cairo_user_scaled_font_text_to_glyphs_func_t for details of how the callback
     * works.
     * <p>
     * The font-face should not be immutable or an {@link IllegalStateException}
     * will be thrown. A user font-face is immutable as soon as a scaled-font is
     * created from it.
     *
     * @param textToGlyphsFunc The text_to_glyphs callback, or {@code null}
     * @throws IllegalStateException when the font-face is immutable
     * @since 1.8
     */
    public void setTextToGlyphsFunc(UserScaledFontTextToGlyphsFunc textToGlyphsFunc) throws IllegalStateException {
        try {
            cairo_user_font_face_set_text_to_glyphs_func.invoke(handle(),
                    textToGlyphsFunc == null ? MemorySegment.NULL : textToGlyphsFunc.toCallback(arena));
            this.textToGlyphsFunc = textToGlyphsFunc;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status() == Status.USER_FONT_IMMUTABLE) {
            throw new IllegalStateException(status().toString());
        }
    }

    private static final MethodHandle cairo_user_font_face_set_text_to_glyphs_func = Interop.downcallHandle(
            "cairo_user_font_face_set_text_to_glyphs_func", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS));

    /**
     * Gets the text-to-glyphs conversion function of a user-font.
     *
     * @return The text_to_glyphs callback of this font-face or {@code null} if none
     * set or an error occurred.
     * @since 1.8
     */
    public UserScaledFontTextToGlyphsFunc getTextToGlyphsFunc() {
        return this.textToGlyphsFunc;
    }

    // Static class to separate the cleanup logic from the object being cleaned
    private record CleanupAction(Arena arena) implements Runnable {
        @Override public void run() {
            arena.close();
        }
    }
}
