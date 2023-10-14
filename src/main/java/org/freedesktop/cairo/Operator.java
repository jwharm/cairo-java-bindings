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
 * cairo_operator_t is used to set the compositing operator for all cairo
 * drawing operations.
 * <p>
 * The default operator is CAIRO_OPERATOR_OVER.
 * <p>
 * The operators marked as unbounded modify their destination even outside of
 * the mask layer (that is, their effect is not bound by the mask layer).
 * However, their effect can still be limited by way of clipping.
 * <p>
 * To keep things simple, the operator descriptions here document the behavior
 * for when both source and destination are either fully transparent or fully
 * opaque. The actual implementation works for translucent layers too. For a
 * more detailed explanation of the effects of each operator, including the
 * mathematical definitions, see <a href="https://cairographics.org/operators/">
 * https://cairographics.org/operators/ </a>.
 */
public enum Operator {

    /**
     * clear destination layer (bounded)
     * 
     * @since 1.0
     */
    CLEAR,

    /**
     * replace destination layer (bounded)
     * 
     * @since 1.0
     */
    SOURCE,

    /**
     * draw source layer on top of destination layer (bounded)
     * 
     * @since 1.0
     */
    OVER,

    /**
     * draw source where there was destination content (unbounded)
     * 
     * @since 1.0
     */
    IN,

    /**
     * draw source where there was no destination content (unbounded)
     * 
     * @since 1.0
     */
    OUT,

    /**
     * draw source on top of destination content and only there
     * 
     * @since 1.0
     */
    ATOP,

    /**
     * ignore the source
     * 
     * @since 1.0
     */
    DEST,

    /**
     * draw destination on top of source
     * 
     * @since 1.0
     */
    DEST_OVER,

    /**
     * leave destination only where there was source content (unbounded)
     * 
     * @since 1.0
     */
    DEST_IN,

    /**
     * leave destination only where there was no source content
     * 
     * @since 1.0
     */
    DEST_OUT,

    /**
     * leave destination on top of source content and only there (unbounded)
     * 
     * @since 1.0
     */
    DEST_ATOP,

    /**
     * source and destination are shown where there is only one of them
     * 
     * @since 1.0
     */
    XOR,

    /**
     * source and destination layers are accumulated
     * 
     * @since 1.0
     */
    ADD,

    /**
     * like over, but assuming source and dest are disjoint geometries
     * 
     * @since 1.0
     */
    SATURATE,

    /**
     * source and destination layers are multiplied. This causes the result to be at
     * least as dark as the darker inputs.
     * 
     * @since 1.10
     */
    MULTIPLY,

    /**
     * source and destination are complemented and multiplied. This causes the
     * result to be at least as light as the lighter inputs.
     * 
     * @since 1.10
     */
    SCREEN,

    /**
     * multiplies or screens, depending on the lightness of the destination color.
     * 
     * @since 1.10
     */
    OVERLAY,

    /**
     * replaces the destination with the source if it is darker, otherwise keeps the
     * source.
     * 
     * @since 1.10
     */
    DARKEN,

    /**
     * replaces the destination with the source if it is lighter, otherwise keeps
     * the source.
     * 
     * @since 1.10
     */
    LIGHTEN,

    /**
     * brightens the destination color to reflect the source color.
     * 
     * @since 1.10
     */
    COLOR_DODGE,

    /**
     * darkens the destination color to reflect the source color.
     * 
     * @since 1.10
     */
    COLOR_BURN,

    /**
     * Multiplies or screens, dependent on source color.
     * 
     * @since 1.10
     */
    HARD_LIGHT,

    /**
     * Darkens or lightens, dependent on source color.
     * 
     * @since 1.10
     */
    SOFT_LIGHT,

    /**
     * Takes the difference of the source and destination color.
     * 
     * @since 1.10
     */
    DIFFERENCE,

    /**
     * Produces an effect similar to difference, but with lower contrast.
     * 
     * @since 1.10
     */
    EXCLUSION,

    /**
     * Creates a color with the hue of the source and the saturation and luminosity
     * of the target.
     * 
     * @since 1.0
     */
    HSL_HUE,

    /**
     * Creates a color with the saturation of the source and the hue and luminosity
     * of the target. Painting with this mode onto a gray area produces no change.
     * 
     * @since 1.10
     */
    HSL_SATURATION,

    /**
     * Creates a color with the hue and saturation of the source and the luminosity
     * of the target. This preserves the gray levels of the target and is useful for
     * coloring monochrome images or tinting color images.
     * 
     * @since 1.10
     */
    HSL_COLOR,

    /**
     * Creates a color with the luminosity of the source and the hue and saturation
     * of the target. This produces an inverse effect to HSL_COLOR.
     * 
     * @since 1.10
     */
    HSL_LUMINOSITY;

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
    public static Operator of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoOperator GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_operator_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_operator_get_type = Interop.downcallHandle(
            "cairo_gobject_operator_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}