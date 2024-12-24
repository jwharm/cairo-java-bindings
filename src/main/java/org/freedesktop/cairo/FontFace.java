/* cairo-java-bindings - Java language bindings for cairo
 * Copyright (C) 2024 Jan-Willem Harmannij
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

import io.github.jwharm.cairobindings.Proxy;
import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.MemoryCleaner;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * The base class for font faces.
 * <p>
 * FontFace represents a particular font at a particular weight, slant, and
 * other characteristic but no size, transformation, or size.
 * <p>
 * Font faces are created using font-backend-specific constructors, typically of
 * the form {@code FontFaceClass.create()}, or implicitly using the toy text API
 * by way of {@link Context#selectFontFace(String, FontSlant, FontWeight)}. The
 * resulting face can be accessed using {@link Context#getFontFace()}.
 * <p>
 * A FontFace specifies all aspects of a font other than the size or font matrix
 * (a font matrix is used to distort a font by shearing it or scaling it
 * unequally in the two directions) . A font face can be set on a Context by
 * using {@link Context#setFontFace(FontFace)} the size and font matrix are set
 * with {@link Context#setFontSize(double)} and
 * {@link Context#setFontMatrix(Matrix)}.
 * <p>
 * There are various types of font faces, depending on the font backend they
 * use. The type of a font face can be queried using {@link FontFace#getFontType()}.
 * 
 * @see ScaledFont
 * @since 1.0
 */
public sealed class FontFace extends Proxy permits ToyFontFace, FTFontFace, UserFontFace {

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Constructor used internally to instantiate a java FontFace object for a
     * native {@code cairo_font_face_t} instance
     * 
     * @param address the memory address of the native {@code cairo_font_face_t}
     *                instance
     */
    public FontFace(MemorySegment address) {
        super(address);
        MemoryCleaner.setFreeFunc(handle(), "cairo_font_face_destroy");
    }

    /**
     * Invokes the cleanup action that is normally invoked during garbage collection.
     * If the instance is "owned" by the user, the {@code destroy()} function is run
     * to dispose the native instance.
     */
    public void destroy() {
        MemoryCleaner.free(handle());
    }

    /**
     * Checks whether an error has previously occurred for this font face
     * 
     * @return {@link Status#SUCCESS} or another error such as
     *         {@link Status#NO_MEMORY}.
     * @since 1.0
     */
    public Status status() {
        try {
            int result = (int) cairo_font_face_status.invoke(handle());
            return Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_face_status = Interop.downcallHandle("cairo_font_face_status",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Returns the type of the backend used to create a font face. See
     * {@link FontType} for available types.
     * 
     * @return The type of the FontFace.
     * @since 1.2
     */
    public FontType getFontType() {
        try {
            int result = (int) cairo_font_face_get_type.invoke(handle());
            return FontType.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_face_get_type = Interop.downcallHandle("cairo_font_face_get_type",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Increases the reference count on the FontFace by one. This prevents the
     * FontFace from being destroyed until a matching call to
     * {@code cairo_font_face_destroy()} is made.
     * 
     * @since 1.0
     */
    void reference() {
        try {
            cairo_font_face_reference.invoke(handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_face_reference = Interop.downcallHandle("cairo_font_face_reference",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Attach user data to the font face. To remove user data from a font face, call
     * this function with the key that was used to set it and {@code null} for
     * {@code userData}.
     *
     * @param  key      the key to attach the user data to
     * @param  userData the user data to attach to the font face
     * @return the key
     * @throws NullPointerException if {@code key} is {@code null}
     * @since 1.4
     */
    public UserDataKey setUserData(UserDataKey key, MemorySegment userData) {
        Status status;
        try {
            int result = (int) cairo_font_face_set_user_data.invoke(handle(), key.handle(), userData, MemorySegment.NULL);
            status = Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
        return key;
    }

    private static final MethodHandle cairo_font_face_set_user_data = Interop
            .downcallHandle("cairo_font_face_set_user_data", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Return user data previously attached to the font face using the specified
     * key. If no user data has been attached with the given key this function
     * returns {@code null}.
     * <p>
     * The returned memory segment has zero length. It can be resized with
     * {@link MemorySegment#reinterpret(long)}.
     *
     * @param key the UserDataKey the user data was attached to
     * @return the user data previously attached or {@code null}
     * @since 1.4
     */
    public MemorySegment getUserData(UserDataKey key) {
        if (key == null) {
            return null;
        }
        try {
            MemorySegment result = (MemorySegment) cairo_font_face_get_user_data.invoke(handle(), key.handle());
            return MemorySegment.NULL.equals(result) ? null : result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_face_get_user_data = Interop.downcallHandle("cairo_get_user_data",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Get the CairoFontFace GType
     * @return the GType
     */
    public static org.gnome.gobject.Type getType() {
        try {
            long result = (long) cairo_gobject_font_face_get_type.invoke();
            return new org.gnome.gobject.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_font_face_get_type = Interop.downcallHandle(
            "cairo_gobject_font_face_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
