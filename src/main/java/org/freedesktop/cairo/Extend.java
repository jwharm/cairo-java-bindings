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
 * Extend is used to describe how pattern color/alpha will be determined for
 * areas "outside" the pattern's natural area, (for example, outside the surface
 * bounds or outside the gradient geometry).
 * <p>
 * Mesh patterns are not affected by the extend mode.
 * <p>
 * The default extend mode is {@link #NONE} for surface patterns and
 * {@link #PAD} for gradient patterns.
 * <p>
 * New entries may be added in future versions.
 * 
 * @since 1.0
 */
public enum Extend {

    /**
     * pixels outside of the source pattern are fully transparent
     * 
     * @since 1.0
     */
    NONE,

    /**
     * the pattern is tiled by repeating
     * 
     * @since 1.0
     */
    REPEAT,

    /**
     * the pattern is tiled by reflecting at the edges
     * 
     * @since 1.0; but only implemented for surface patterns since 1.6
     */
    REFLECT,

    /**
     * pixels outside of the pattern copy the closest pixel from the source
     * 
     * @since 1.2; but only implemented for surface patterns since 1.6
     */
    PAD;

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
    public static Extend of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoExtend GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_extend_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_extend_get_type = Interop.downcallHandle(
            "cairo_gobject_extend_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
