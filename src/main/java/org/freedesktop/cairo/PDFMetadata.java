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
 * {@code PDFMetadata} is used by the cairo_pdf_surface_set_metadata() function
 * specify the metadata to set.
 * 
 * @since 1.16
 */
public enum PDFMetadata {

    /**
     * The document title
     * 
     * @since 1.16
     */
    TITLE,

    /**
     * The document author
     * 
     * @since 1.16
     */
    AUTHOR,

    /**
     * The document subject
     * 
     * @since 1.16
     */
    SUBJECT,

    /**
     * The document keywords
     * 
     * @since 1.16
     */
    KEYWORDS,

    /**
     * The document creator
     * 
     * @since 1.16
     */
    CREATOR,

    /**
     * The document creation date
     * 
     * @since 1.16
     */
    CREATE_DATE,

    /**
     * The document modification date
     * 
     * @since 1.16
     */
    MOD_DATE;

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
    public static PDFMetadata of(int ordinal) {
        return values()[ordinal];
    }
}
