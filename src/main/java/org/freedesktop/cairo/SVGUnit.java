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

/**
 * SVGUnit is used to describe the units valid for coordinates and lengths in
 * the SVG specification.
 * <p>
 * See also:
 * <ul>
 * <li><a href=
 * "https://www.w3.org/TR/SVG/coords.html#Units">https://www.w3.org/TR/SVG/coords.html#Units</a>
 * <li><a href=
 * "https://www.w3.org/TR/SVG/types.html#InterfaceSVGLength">https://www.w3.org/TR/SVG/types.html#InterfaceSVGLength</a>
 * <li><a href=
 * "https://www.w3.org/TR/css-values-3/#lengths">https://www.w3.org/TR/css-values-3/#lengths</a>
 * </ul>
 * 
 * @see Surface
 * @since 1.16
 */
public enum SVGUnit {

    /**
     * User unit, a value in the current coordinate system. If used in the root
     * element for the initial coordinate systems it corresponds to pixels.
     * 
     * @since 1.16
     */
    USER,

    /**
     * The size of the element's font.
     * 
     * @since 1.16
     */
    EM,

    /**
     * The x-height of the element’s font.
     * 
     * @since 1.16
     */
    EX,

    /**
     * Pixels (1px = 1/96th of 1in).
     * 
     * @since 1.16
     */
    PX,

    /**
     * Inches (1in = 2.54cm = 96px).
     * 
     * @since 1.16
     */
    IN,

    /**
     * Centimeters (1cm = 96px/2.54).
     * 
     * @since 1.16
     */
    CM,

    /**
     * Millimeters (1mm = 1/10th of 1cm).
     * 
     * @since 1.16
     */
    MM,

    /**
     * Points (1pt = 1/72th of 1in).
     * 
     * @since 1.16
     */
    PT,

    /**
     * Picas (1pc = 1/6th of 1in).
     * 
     * @since 1.16
     */
    PC,

    /**
     * Percent, a value that is some fraction of another reference value.
     * 
     * @since 1.16
     */
    PERCENT;

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
    public static SVGUnit of(int ordinal) {
        return values()[ordinal];
    }
}
