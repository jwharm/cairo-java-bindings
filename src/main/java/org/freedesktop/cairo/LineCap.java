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
 * Specifies how to render the endpoints of the path when stroking.
 * <p>
 * The default line cap style is CAIRO_LINE_CAP_BUTT.
 */
public enum LineCap {

    /**
     * start(stop) the line exactly at the start(end) point
     * 
     * @since 1.0
     */
    BUTT,

    /**
     * use a round ending, the center of the circle is the end point
     * 
     * @since 1.0
     */
    ROUND,

    /**
     * use a squared ending, the center of the square is the end point
     * 
     * @since 1.0
     */
    SQUARE;

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
    public static LineCap of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoLineCap GType
     * @return the GType
     */
    public static org.gnome.gobject.Type getType() {
        try {
            long result = (long) cairo_gobject_line_cap_get_type.invoke();
            return new org.gnome.gobject.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_line_cap_get_type = Interop.downcallHandle(
            "cairo_gobject_line_cap_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}