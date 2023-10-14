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
 * Specifies the type of hinting to do on font outlines. Hinting is the process
 * of fitting outlines to the pixel grid in order to improve the appearance of
 * the result. Since hinting outlines involves distorting them, it also reduces
 * the faithfulness to the original outline shapes. Not all of the outline
 * hinting styles are supported by all font backends.
 * <p>
 * New entries may be added in future versions.
 * 
 * @since 1.0
 */
public enum HintStyle {

    /**
     * Use the default hint style for font backend and target device
     * 
     * @since 1.0
     */
    DEFAULT,

    /**
     * Do not hint outlines
     * 
     * @since 1.0
     */
    NONE,

    /**
     * Hint outlines slightly to improve contrast while retaining good fidelity to
     * the original shapes
     * 
     * @since 1.0
     */
    SLIGHT,

    /**
     * Hint outlines with medium strength giving a compromise between fidelity to
     * the original shapes and contrast
     * 
     * @since 1.0
     */
    MEDIUM,

    /**
     * Hint outlines to maximize contrast
     * 
     * @since 1.0
     */
    FULL;

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
    public static HintStyle of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoHintStyle GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_hint_style_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_hint_style_get_type = Interop.downcallHandle(
            "cairo_gobject_hint_style_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
