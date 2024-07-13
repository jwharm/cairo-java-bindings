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
 * {@code PDFOutlineFlags} is used by the cairo_pdf_surface_add_outline()
 * function specify the attributes of an outline item. These flags may be
 * bitwise-or'd to produce any combination of flags.
 * 
 * @since 1.16
 */
public enum PDFOutlineFlags implements Flag {

    /**
     * The outline item defaults to open in the PDF viewer
     * 
     * @since 1.16
     */
    OPEN,

    /**
     * The outline item is displayed by the viewer in bold text
     * 
     * @since 1.16
     */
    BOLD,

    /**
     * The outline item is displayed by the viewer in italic text
     * 
     * @since 1.16
     */
    ITALIC;

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
    public static PDFOutlineFlags of(int ordinal) {
        return values()[ordinal];
    }
}
