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
 * A set of synthesis options to control how FreeType renders the glyphs for a particular font face.
 * <p>
 * Individual synthesis features of a {@link FTFontFace} can be set using
 * {@link FTFontFace#setSynthesize(FTSynthesize)}, or disabled using {@link FTFontFace#unsetSynthesize(FTSynthesize)}.
 * The currently enabled set of synthesis options can be queried with {@link FTFontFace#getSynthesize()}.
 * <p>
 * Note that when synthesizing glyphs, the font metrics returned will only be estimates.
 *
 * @param value {@link #BOLD}, {@link #OBLIQUE} or a combination of both
 * @since 1.12
 */
public record FTSynthesize(int value) {

    /**
     * Embolden the glyphs (redraw with a pixel offset)
     */
    public static final FTSynthesize BOLD = new FTSynthesize(1 /* actually 1 << 0 */);

    /**
     * Slant the glyph outline by 12 degrees to the right.
     */
    public static final FTSynthesize OBLIQUE = new FTSynthesize(1 << 1);

    /**
     * Combine (bitwise OR) operation
     *
     * @param masks one or more values to combine with
     * @return the combined value by calculating {@code this | mask}
     */
    public FTSynthesize or(FTSynthesize... masks) {
        int value = this.value();
        for (var arg : masks) {
            value |= arg.value();
        }
        return new FTSynthesize(value);
    }

    /**
     * Combine (bitwise OR) operation
     *
     * @param mask  the first value to combine
     * @param masks the other values to combine
     * @return the combined value by calculating {@code mask | masks[0] | masks[1] | ...}
     */
    public static FTSynthesize combined(FTSynthesize mask, FTSynthesize... masks) {
        int value = mask.value();
        for (var arg : masks) {
            value |= arg.value();
        }
        return new FTSynthesize(value);
    }

    /**
     * Compares the value of this bitfield with the provided int value
     *
     * @param bitfield an int value to compare with
     * @return returns true when {@code this.value == bitfield}
     */
    public boolean equals(int bitfield) {
        return this.value == bitfield;
    }
}
