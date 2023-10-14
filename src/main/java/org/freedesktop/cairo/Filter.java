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
 * Filter is used to indicate what filtering should be applied when reading
 * pixel values from patterns. See {@link Pattern#setFilter} for indicating the
 * desired filter to be used with a particular pattern.
 *
 * @since 1.0
 */
public enum Filter {

    /**
     * A high-performance filter, with quality similar to {@link #NEAREST}
     * 
     * @since 1.0
     */
    FAST,

    /**
     * A reasonable-performance filter, with quality similar to {@link #BILINEAR}
     * 
     * @since 1.0
     */
    GOOD,

    /**
     * The highest-quality available, performance may not be suitable for
     * interactive use.
     * 
     * @since 1.0
     */
    BEST,

    /**
     * Nearest-neighbor filtering
     * 
     * @since 1.0
     */
    NEAREST,

    /**
     * Linear interpolation in two dimensions
     * 
     * @since 1.0
     */
    BILINEAR,

    /**
     * This filter value is currently unimplemented, and should not be used in
     * current code.
     * 
     * @since 1.0
     */
    GAUSSIAN;

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
    public static Filter of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoFilter GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_filter_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_filter_get_type = Interop.downcallHandle(
            "cairo_gobject_filter_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
