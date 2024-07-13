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

import io.github.jwharm.cairobindings.Flag;

/**
 * A set of synthesis options to control how FreeType renders the glyphs for a particular font face.
 * <p>
 * Individual synthesis features of a {@link FTFontFace} can be set using
 * {@link FTFontFace#setSynthesize}, or disabled using {@link FTFontFace#unsetSynthesize}.
 * The currently enabled set of synthesis options can be queried with {@link FTFontFace#getSynthesize}.
 * <p>
 * Note that when synthesizing glyphs, the font metrics returned will only be estimates.
 *
 * @since 1.12
 */
public enum FTSynthesize implements Flag {
    /**
     * Embolden the glyphs (redraw with a pixel offset)
     */
    BOLD(1),

    /**
     * Slant the glyph outline by 12 degrees to the right.
     */
    OBLIQUE(1 << 1);

    private final int value;

    /**
     * Create a new FTSynthesize enum value
     *
     * @param value {@link #BOLD}, {@link #OBLIQUE} or a combination of both
     */
    FTSynthesize(int value) {
        this.value = value;
    }

    /**
     * Get the value of this FTSynthesize enum
     *
     * @return {@link #BOLD}, {@link #OBLIQUE} or a combination of both
     */
    public int getValue() {
        return value;
    }

    /**
     * Create an FTSynthesize enum for this value
     *
     * @param  value the value of the enum
     * @return a new FTSynthesize enum member
     */
    public static FTSynthesize of(int value) {
        return value == 1 ? BOLD : OBLIQUE;
    }
}
