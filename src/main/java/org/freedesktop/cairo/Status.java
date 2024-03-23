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
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Status is used to indicate errors that can occur when using Cairo. In some
 * cases it is returned directly by functions. but when using {@link Context},
 * the last error, if any, is stored in the context and can be retrieved with
 * {@link Context#status()}.
 * <p>
 * Cairo uses a single status type to represent all kinds of errors. A status
 * value of {@link Status#SUCCESS} represents no error and has an integer value
 * of zero. All other status values represent an error.
 * <p>
 * Cairo's error handling is designed to be easy to use and safe. All major
 * cairo objects retain an error status internally which can be queried anytime
 * by the users using {@code status()} calls. In the mean time, it is safe to
 * call all cairo functions normally even if the underlying object is in an
 * error status. This means that no error handling code is required before or
 * after each individual cairo function call.
 * <p>
 * New entries may be added in future versions. Use {@link #toString()} to get a
 * human-readable representation of an error message.
 * 
 * @see Context#status()
 * @see Surface#status()
 * @see Pattern#status()
 * @see FontFace#status()
 * @see ScaledFont#status()
 * @see Region#status()
 * @since 1.0
 */
public enum Status {

    /**
     * no error has occurred
     * 
     * @since 1.0
     */
    SUCCESS,
    /**
     * out of memory
     * 
     * @since 1.0
     */
    NO_MEMORY,
    /**
     * cairo_restore() called without matching cairo_save()
     * 
     * @since 1.0
     */
    INVALID_RESTORE,
    /**
     * no saved group to pop, i.e. cairo_pop_group() without matching
     * cairo_push_group()
     * 
     * @since 1.0
     */
    INVALID_POP_GROUP,
    /**
     * no current point defined
     * 
     * @since 1.0
     */
    NO_CURRENT_POINT,
    /**
     * invalid matrix (not invertible)
     * 
     * @since 1.0
     */
    INVALID_MATRIX,
    /**
     * invalid value for an input t
     * 
     * @since 1.0
     */
    INVALID_STATUS,
    /**
     * NULL pointer
     * 
     * @since 1.0
     */
    NULL_POINTER,
    /**
     * input string not valid UTF-8
     * 
     * @since 1.0
     */
    INVALID_STRING,
    /**
     * input path data not valid
     * 
     * @since 1.0
     */
    INVALID_PATH_DATA,
    /**
     * error while reading from input stream
     * 
     * @since 1.0
     */
    READ_ERROR,
    /**
     * error while writing to output stream
     * 
     * @since 1.0
     */
    WRITE_ERROR,
    /**
     * target surface has been finished
     * 
     * @since 1.0
     */
    SURFACE_FINISHED,
    /**
     * the surface type is not appropriate for the operation
     * 
     * @since 1.0
     */
    SURFACE_TYPE_MISMATCH,
    /**
     * the pattern type is not appropriate for the operation
     * 
     * @since 1.0
     */
    PATTERN_TYPE_MISMATCH,
    /**
     * invalid value for an input cairo_content_t
     * 
     * @since 1.0
     */
    INVALID_CONTENT,
    /**
     * invalid value for an input cairo_format_t
     * 
     * @since 1.0
     */
    INVALID_FORMAT,
    /**
     * invalid value for an input Visual*
     * 
     * @since 1.0
     */
    INVALID_VISUAL,
    /**
     * file not found
     * 
     * @since 1.0
     */
    FILE_NOT_FOUND,
    /**
     * invalid value for a dash setting
     * 
     * @since 1.0
     */
    INVALID_DASH,
    /**
     * invalid value for a DSC comment
     * 
     * @since 1.2
     */
    INVALID_DSC_COMMENT,
    /**
     * invalid index passed to getter
     * 
     * @since 1.4
     */
    INVALID_INDEX,
    /**
     * clip region not representable in desired format
     * 
     * @since 1.4
     */
    CLIP_NOT_REPRESENTABLE,
    /**
     * error creating or writing to a temporary file
     * 
     * @since 1.6
     */
    TEMP_FILE_ERROR,
    /**
     * invalid value for stride
     * 
     * @since 1.6
     */
    INVALID_STRIDE,
    /**
     * the font type is not appropriate for the operation
     * 
     * @since 1.8
     */
    FONT_TYPE_MISMATCH,
    /**
     * the user-font is immutable
     * 
     * @since 1.8
     */
    USER_FONT_IMMUTABLE,
    /**
     * error occurred in a user-font callback function
     * 
     * @since 1.8
     */
    USER_FONT_ERROR,
    /**
     * negative number used where it is not allowed
     * 
     * @since 1.8
     */
    NEGATIVE_COUNT,
    /**
     * input clusters do not represent the accompanying text and glyph array
     * 
     * @since 1.8
     */
    INVALID_CLUSTERS,
    /**
     * invalid value for an input cairo_font_slant_t
     * 
     * @since 1.8
     */
    INVALID_SLANT,
    /**
     * invalid value for an input cairo_font_weight_t
     * 
     * @since 1.8
     */
    INVALID_WEIGHT,
    /**
     * invalid value (typically too big) for the size of the input (surface,
     * pattern, etc.)
     * 
     * @since 1.10
     */
    INVALID_SIZE,
    /**
     * user-font method not implemented
     * 
     * @since 1.10
     */
    USER_FONT_NOT_IMPLEMENTED,
    /**
     * the device type is not appropriate for the operation
     * 
     * @since 1.10
     */
    DEVICE_TYPE_MISMATCH,
    /**
     * an operation to the device caused an unspecified error
     * 
     * @since 1.10
     */
    DEVICE_ERROR,
    /**
     * a mesh pattern construction operation was used outside of a
     * cairo_mesh_pattern_begin_patch()/cairo_mesh_pattern_end_patch() pair
     * 
     * @since 1.12
     */
    INVALID_MESH_CONSTRUCTION,
    /**
     * target device has been finished
     * 
     * @since 1.12
     */
    DEVICE_FINISHED,
    /**
     * CAIRO_MIME_TYPE_JBIG2_GLOBAL_ID has been used on at least one image but no
     * image provided CAIRO_MIME_TYPE_JBIG2_GLOBAL
     * 
     * @since 1.14
     */
    JBIG2_GLOBAL_MISSING,
    /**
     * error occurred in libpng while reading from or writing to a PNG file
     * 
     * @since 1.16
     */
    PNG_ERROR,
    /**
     * error occurred in libfreetype
     * 
     * @since 1.16
     */
    FREETYPE_ERROR,
    /**
     * error occurred in the Windows Graphics Device Interface
     * 
     * @since 1.16
     */
    WIN32_GDI_ERROR,
    /**
     * invalid tag name, attributes, or nesting
     * 
     * @since 1.16
     */
    TAG_ERROR,
    /**
     * error occurred in the Windows Direct Write API
     *
     * @since 1.18
     */
    DWRITE_ERROR,
    /**
     * error occurred in OpenType-SVG font rendering
     *
     * @since 1.18
     */
    SVG_FONT_ERROR,
    /**
     * this is a special value indicating the number of status values defined in
     * this enumeration. When using this value, note that the version of cairo at
     * run-time may have additional status values defined than the value of this
     * symbol at compile-time.
     * 
     * @since 1.10
     */
    LAST_STATUS;

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
     * Provides a human-readable description of a {@link Status}.
     * 
     * @return a string representation of the status
     * @since 1.0
     */
    @Override
    public String toString() {
        try {
            MemorySegment result = (MemorySegment) cairo_status_to_string.invoke(getValue());
            if (MemorySegment.NULL.equals(result)) {
                return null;
            }
            return result.reinterpret(Integer.MAX_VALUE).getString(0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_status_to_string = Interop.downcallHandle("cairo_status_to_string",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Returns the enum constant for the given ordinal (its position in the enum
     * declaration).
     * 
     * @param ordinal the position in the enum declaration, starting from zero
     * @return the enum constant for the given ordinal
     */
    public static Status of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoStatus GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_status_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_status_get_type = Interop.downcallHandle(
            "cairo_gobject_status_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
