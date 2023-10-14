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
 * Specifies if color fonts are to be rendered using the color glyphs or
 * outline glyphs. Glyphs that do not have a color presentation, and
 * non-color fonts are not affected by this font option.
 *
 * @since 1.18
 */
public enum ColorMode {

    /**
     * Use the default color mode for font backend and target device
     *
     * @since 1.18
     */
    DEFAULT,

    /**
     * Disable rendering color glyphs. Glyphs are always rendered as outline
     * glyphs
     *
     * @since 1.18
     */
    NO_COLOR,

    /**
     * Enable rendering color glyphs. If the font contains a color presentation
     * for a glyph, and when supported by the font backend, the glyph will be
     * rendered in color
     *
     * @since 1.18
     */
    COLOR;

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
    public static ColorMode of(int ordinal) {
        return values()[ordinal];
    }
}
