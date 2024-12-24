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
 * Specifies how to render the junction of two lines when stroking.
 * <p>
 * The default line join style is CAIRO_LINE_JOIN_MITER.
 */
public enum LineJoin {

    /**
     * use a sharp (angled) corner, see cairo_set_miter_limit()
     * 
     * @since 1.0
     */
    MITER,

    /**
     * use a rounded join, the center of the circle is the joint point
     * 
     * @since 1.0
     */
    ROUND,

    /**
     * use a cut-off join, the join is cut off at half the line width from the joint
     * point
     * 
     * @since 1.0
     */
    BEVEL;

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
    public static LineJoin of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoLineJoin GType
     * @return the GType
     */
    public static org.gnome.gobject.Type getType() {
        try {
            long result = (long) cairo_gobject_line_join_get_type.invoke();
            return new org.gnome.gobject.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_line_join_get_type = Interop.downcallHandle(
            "cairo_gobject_line_join_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}