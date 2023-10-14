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
 * Dither is an intentionally applied form of noise used to randomize
 * quantization error, preventing large-scale patterns such as color banding in
 * images (e.g. for gradients). Ordered dithering applies a precomputed
 * threshold matrix to spread the errors smoothly.
 * <p>
 * {@link Dither} is modeled on pixman dithering algorithm choice. As of
 * Pixman 0.40, FAST corresponds to a 8x8 ordered bayer noise and GOOD and BEST
 * use an ordered 64x64 precomputed blue noise.
 *
 * @since 1.18
 */
public enum Dither {

    /**
     * No dithering.
     *
     * @since 1.18
     */
    NONE,

    /**
     * Default choice at cairo compile time. Currently NONE.
     *
     * @since 1.18
     */
    DEFAULT,

    /**
     * Fastest dithering algorithm supported by the backend
     *
     * @since 1.18
     */
    FAST,

    /**
     * An algorithm with smoother dithering than FAST
     *
     * @since 1.18
     */
    GOOD,

    /**
     * Best algorithm available in the backend
     *
     * @since 1.18
     */
    BEST;

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
    public static Dither of(int ordinal) {
        return values()[ordinal];
    }
}
