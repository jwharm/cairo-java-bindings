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
 * cairo_fill_rule_t is used to select how paths are filled. For both fill
 * rules, whether or not a point is included in the fill is determined by taking
 * a ray from that point to infinity and looking at intersections with the path.
 * The ray can be in any direction, as long as it doesn't pass through the end
 * point of a segment or have a tricky intersection such as intersecting tangent
 * to the path. (Note that filling is not actually implemented in this way. This
 * is just a description of the rule that is applied.)
 * <p>
 * The default fill rule is CAIRO_FILL_RULE_WINDING.
 * <p>
 * New entries may be added in future versions.
 * 
 * @since 1.0
 */
public enum FillRule {

    /**
     * If the path crosses the ray from left-to-right, counts +1. If the path
     * crosses the ray from right to left, counts -1. (Left and right are determined
     * from the perspective of looking along the ray from the starting point.) If
     * the total count is non-zero, the point will be filled.
     * 
     * @since 1.0
     */
    WINDING,

    /**
     * Counts the total number of intersections, without regard to the orientation
     * of the contour. If the total number of intersections is odd, the point will
     * be filled.
     * 
     * @since 1.0
     */
    EVEN_ODD;

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
    public static FillRule of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoFillRule GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_fill_rule_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_fill_rule_get_type = Interop.downcallHandle(
            "cairo_gobject_fill_rule_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}