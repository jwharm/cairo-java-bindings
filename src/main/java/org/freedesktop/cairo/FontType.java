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

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * FontType is used to describe the type of a given font face or scaled font.
 * The font types are also known as "font backends" within cairo.
 * <p>
 * The type of a font face is determined by the function used to create it,
 * which will generally be of the form {@code FontFaceClass.create()}. The font
 * face type can be queried with {@link FontFace#getType()}.
 * <p>
 * The various {@link FontFace} functions can be used with a font face of any
 * type.
 * <p>
 * The type of a scaled font is determined by the type of the font face passed
 * to {@link ScaledFont#create(FontFace, Matrix, Matrix, FontOptions)}. The
 * scaled font type can be queried with {@link ScaledFont#getType()}.
 * <p>
 * New entries may be added in future versions.
 * 
 * @since 1.2
 */
public enum FontType {

    /**
     * The font was created using cairo's toy font api
     * 
     * @since 1.2
     */
    TOY,

    /**
     * The font is of type FreeType
     * 
     * @since 1.2
     */
    FT,

    /**
     * The font is of type Win32
     * 
     * @since 1.2
     */
    WIN32,

    /**
     * The font is of type Quartz
     * 
     * @since 1.6, in 1.2 and 1.4 it was named ATSUI
     */
    QUARTZ,

    /**
     * The font was create using cairo's user font api
     * 
     * @since 1.8
     */
    USER,

    /**
     * The font is of type Win32 DWrite
     *
     * @since 1.18
     */
    DWRITE;

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Return the value of this enum
     * 
     * @return the value
     */
    public int getValue() {
        return ordinal();
    }

    /**
     * Returns the enum constant for the given ordinal (its position in the enum
     * declaration).
     * 
     * @param ordinal the position in the enum declaration, starting from zero
     * @return the enum constant for the given ordinal
     */
    public static FontType of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoFontType GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_font_type_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_font_type_get_type = Interop.downcallHandle(
            "cairo_gobject_font_type_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
