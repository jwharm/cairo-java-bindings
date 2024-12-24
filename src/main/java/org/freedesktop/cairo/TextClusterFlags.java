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
 * Specifies properties of a text cluster mapping.
 * 
 * @since 1.8
 */
public enum TextClusterFlags {

    /**
     * The clusters in the cluster array map to glyphs in the glyph array from end
     * to start.
     * 
     * @since 1.8
     */
    BACKWARD(0x00000001);

    static {
        Cairo.ensureInitialized();
    }

    private final int value;

    TextClusterFlags(int value) {
        this.value = value;
    }

    /**
     * Return the value of this enum
     * 
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the enum member for the given value.
     *
     * @param value the value of the enum member
     * @return the enum member for the given value
     */
    public static TextClusterFlags of(int value) {
        if (value == 0x00000001) {
            return BACKWARD;
        } else {
            throw new IllegalArgumentException("No TextClusterFlags enum with value " + value);
        }
    }

    /**
     * Get the CairoTextClusterFlags GType
     * @return the GType
     */
    public static org.gnome.gobject.Type getType() {
        try {
            long result = (long) cairo_gobject_text_cluster_flags_get_type.invoke();
            return new org.gnome.gobject.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_text_cluster_flags_get_type = Interop.downcallHandle(
            "cairo_gobject_text_cluster_flags_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
