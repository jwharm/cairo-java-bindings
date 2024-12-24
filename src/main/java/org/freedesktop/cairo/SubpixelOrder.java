/* cairo-java-bindings - Java language bindings for cairo
 * Copyright (C) 2023-2024 Jan-Willem Harmannij
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
 * The subpixel order specifies the order of color elements within each pixel on
 * the display device when rendering with an antialiasing mode of
 * {@link Antialias#SUBPIXEL}.
 * 
 * @since 1.0
 */
public enum SubpixelOrder {

    /**
     * Use the default subpixel order for for the target device
     * 
     * @since 1.0
     */
    DEFAULT,

    /**
     * Subpixel elements are arranged horizontally with red at the left
     * 
     * @since 1.0
     */
    RGB,

    /**
     * Subpixel elements are arranged horizontally with blue at the left
     * 
     * @since 1.0
     */
    BGR,

    /**
     * Subpixel elements are arranged vertically with red at the top
     * 
     * @since 1.0
     */
    VRGB,

    /**
     * Subpixel elements are arranged vertically with blue at the top
     * 
     * @since 1.0
     */
    VBGR;

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Return the value of this enum
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
    public static SubpixelOrder of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoSubpixelOrder GType
     * @return the GType
     */
    public static org.gnome.gobject.Type getType() {
        try {
            long result = (long) cairo_gobject_subpixel_order_get_type.invoke();
            return new org.gnome.gobject.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_subpixel_order_get_type = Interop.downcallHandle(
            "cairo_gobject_subpixel_order_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
