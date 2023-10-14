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
 * MIME types defined as constants in Cairo.
 */
public enum MimeType {

    /**
     * Group 3 or Group 4 CCITT facsimile encoding (International Telecommunication
     * Union, Recommendations T.4 and T.6.)
     * 
     * @since 1.16
     */
    CCITT_FAX("image/g3fax"),

    /**
     * Decode parameters for Group 3 or Group 4 CCITT facsimile encoding. See
     * <a href=
     * "https://www.cairographics.org/manual/cairo-PDF-Surfaces.html#ccitt">CCITT
     * Fax Images</a>.
     * 
     * @since 1.16
     */
    CCITT_FAX_PARAMS("application/x-cairo.ccitt.params"),

    /**
     * Encapsulated PostScript file. <a href=
     * "http://wwwimages.adobe.com/content/dam/Adobe/endevnet/postscript/pdfs/5002.EPSF_Spec.pdf">Encapsulated
     * PostScript File Format Specification</a>
     * 
     * @since 1.16
     */
    TYPE_EPS("application/postscript"),

    /**
     * Embedding parameters Encapsulated PostScript data. See <a href=
     * "https://www.cairographics.org/manual/cairo-PostScript-Surfaces.html#eps">Embedding
     * EPS files</a>.
     * 
     * @since 1.16
     */
    EPS_PARAMS("application/x-cairo.eps.params"),

    /**
     * Joint Bi-level Image Experts Group image coding standard (ISO/IEC 11544).
     * 
     * @since 1.14
     */
    JBIG2("application/x-cairo.jbig2"),

    /**
     * Joint Bi-level Image Experts Group image coding standard (ISO/IEC 11544)
     * global segment.
     * 
     * @since 1.14
     */
    JBIG2_GLOBAL("application/x-cairo.jbig2-global"),

    /**
     * An unique identifier shared by a JBIG2 global segment and all JBIG2 images
     * that depend on the global segment.
     * 
     * @since 1.14
     */
    JBIG2_GLOBAL_ID("application/x-cairo.jbig2-global-id"),

    /**
     * The Joint Photographic Experts Group (JPEG) 2000 image coding standard
     * (ISO/IEC 15444-1).
     * 
     * @since 1.10
     */
    JP2("image/jp2"),

    /**
     * The Joint Photographic Experts Group (JPEG) image coding standard (ISO/IEC
     * 10918-1).
     * 
     * @since 1.10
     */
    JPEG("image/jpeg"),

    /**
     * The Portable Network Graphics image file format (ISO/IEC 15948).
     * 
     * @since 1.10
     */
    PNG("image/png"),

    /**
     * URI for an image file (unofficial MIME type).
     * 
     * @since 1.10
     */
    URI("text/x-uri"),

    /**
     * Unique identifier for a surface (cairo specific MIME type). All surfaces with
     * the same unique identifier will only be embedded once.
     * 
     * @since 1.12
     */
    UNIQUE_ID("application/x-cairo.uuid");

    private final String name;

    MimeType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Returns the enum constant for the given ordinal (its position in the enum
     * declaration).
     * 
     * @param ordinal the position in the enum declaration, starting from zero
     * @return the enum constant for the given ordinal
     */
    public static MimeType of(int ordinal) {
        return values()[ordinal];
    }
}
