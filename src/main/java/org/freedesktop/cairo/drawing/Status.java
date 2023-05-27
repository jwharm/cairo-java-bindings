package org.freedesktop.cairo.drawing;

/**
 * cairo_status_t is used to indicate errors that can occur when using Cairo.
 * In some cases it is returned directly by functions. but when using cairo_t,
 * the last error, if any, is stored in the context and can be retrieved with
 * cairo_status().
 * <p>
 * New entries may be added in future versions. Use cairo_status_to_string()
 * to get a human-readable representation of an error message.
 */
public enum Status {

    /**
     * no error has occurred
     * @since 1.0
     */
    CAIRO_STATUS_SUCCESS,
    /**
     * out of memory
     * @since 1.0
     */
    CAIRO_STATUS_NO_MEMORY,
    /**
     * cairo_restore() called without matching cairo_save()
     * @since 1.0
     */
    CAIRO_STATUS_INVALID_RESTORE,
    /**
     * no saved group to pop, i.e. cairo_pop_group() without matching cairo_push_group()
     * @since 1.0
     */
    CAIRO_STATUS_INVALID_POP_GROUP,
    /**
     * no current point defined
     * @since 1.0
     */
    CAIRO_STATUS_NO_CURRENT_POINT,
    /**
     * invalid matrix (not invertible)
     * @since 1.0
     */
    CAIRO_STATUS_INVALID_MATRIX,
    /**
     * invalid value for an input cairo_status_t
     * @since 1.0
     */
    CAIRO_STATUS_INVALID_STATUS,
    /**
     * NULL pointer
     * @since 1.0
     */
    CAIRO_STATUS_NULL_POINTER,
    /**
     * input string not valid UTF-8
     * @since 1.0
     */
    CAIRO_STATUS_INVALID_STRING,
    /**
     * input path data not valid
     * @since 1.0
     */
    CAIRO_STATUS_INVALID_PATH_DATA,
    /**
     * error while reading from input stream
     * @since 1.0
     */
    CAIRO_STATUS_READ_ERROR,
    /**
     * error while writing to output stream
     * @since 1.0
     */
    CAIRO_STATUS_WRITE_ERROR,
    /**
     * target surface has been finished
     * @since 1.0
     */
    CAIRO_STATUS_SURFACE_FINISHED,
    /**
     * the surface type is not appropriate for the operation
     * @since 1.0
     */
    CAIRO_STATUS_SURFACE_TYPE_MISMATCH,
    /**
     * the pattern type is not appropriate for the operation
     * @since 1.0
     */
    CAIRO_STATUS_PATTERN_TYPE_MISMATCH,
    /**
     * invalid value for an input cairo_content_t
     * @since 1.0
     */
    CAIRO_STATUS_INVALID_CONTENT,
    /**
     * invalid value for an input cairo_format_t
     * @since 1.0
     */
    CAIRO_STATUS_INVALID_FORMAT,
    /**
     * invalid value for an input Visual*
     * @since 1.0
     */
    CAIRO_STATUS_INVALID_VISUAL,
    /**
     * file not found
     * @since 1.0
     */
    CAIRO_STATUS_FILE_NOT_FOUND,
    /**
     * invalid value for a dash setting
     * @since 1.0
     */
    CAIRO_STATUS_INVALID_DASH,
    /**
     * invalid value for a DSC comment
     * @since 1.2
     */
    CAIRO_STATUS_INVALID_DSC_COMMENT,
    /**
     * invalid index passed to getter
     * @since 1.4
     */
    CAIRO_STATUS_INVALID_INDEX,
    /**
     * clip region not representable in desired format
     * @since 1.4
     */
    CAIRO_STATUS_CLIP_NOT_REPRESENTABLE,
    /**
     * error creating or writing to a temporary file
     * @since 1.6
     */
    CAIRO_STATUS_TEMP_FILE_ERROR,
    /**
     * invalid value for stride
     * @since 1.6
     */
    CAIRO_STATUS_INVALID_STRIDE,
    /**
     * the font type is not appropriate for the operation
     * @since 1.8
     */
    CAIRO_STATUS_FONT_TYPE_MISMATCH,
    /**
     * the user-font is immutable
     * @since 1.8
     */
    CAIRO_STATUS_USER_FONT_IMMUTABLE,
    /**
     * error occurred in a user-font callback function
     * @since 1.8
     */
    CAIRO_STATUS_USER_FONT_ERROR,
    /**
     * negative number used where it is not allowed
     * @since 1.8
     */
    CAIRO_STATUS_NEGATIVE_COUNT,
    /**
     * input clusters do not represent the accompanying text and glyph array
     * @since 1.8
     */
    CAIRO_STATUS_INVALID_CLUSTERS,
    /**
     * invalid value for an input cairo_font_slant_t
     * @since 1.8
     */
    CAIRO_STATUS_INVALID_SLANT,
    /**
     * invalid value for an input cairo_font_weight_t
     * @since 1.8
     */
    CAIRO_STATUS_INVALID_WEIGHT,
    /**
     * invalid value (typically too big) for the size of the input (surface, pattern, etc.)
     * @since 1.10
     */
    CAIRO_STATUS_INVALID_SIZE,
    /**
     * user-font method not implemented
     * @since 1.10
     */
    CAIRO_STATUS_USER_FONT_NOT_IMPLEMENTED,
    /**
     * the device type is not appropriate for the operation
     * @since 1.10
     */
    CAIRO_STATUS_DEVICE_TYPE_MISMATCH,
    /**
     * an operation to the device caused an unspecified error
     * @since 1.10
     */
    CAIRO_STATUS_DEVICE_ERROR,
    /**
     * a mesh pattern construction operation was used outside of a
     * cairo_mesh_pattern_begin_patch()/cairo_mesh_pattern_end_patch() pair
     * @since 1.12
     */
    CAIRO_STATUS_INVALID_MESH_CONSTRUCTION,
    /**
     * target device has been finished
     * @since 1.12
     */
    CAIRO_STATUS_DEVICE_FINISHED,
    /**
     * CAIRO_MIME_TYPE_JBIG2_GLOBAL_ID has been used on at least one image but no image
     * provided CAIRO_MIME_TYPE_JBIG2_GLOBAL
     * @since 1.14
     */
    CAIRO_STATUS_JBIG2_GLOBAL_MISSING,
    /**
     * error occurred in libpng while reading from or writing to a PNG file
     * @since 1.16
     */
    CAIRO_STATUS_PNG_ERROR,
    /**
     * error occurred in libfreetype
     * @since 1.16
     */
    CAIRO_STATUS_FREETYPE_ERROR,
    /**
     * error occurred in the Windows Graphics Device Interface
     * @since 1.16
     */
    CAIRO_STATUS_WIN32_GDI_ERROR,
    /**
     * invalid tag name, attributes, or nesting
     * @since 1.16
     */
    CAIRO_STATUS_TAG_ERROR,
    /**
     * this is a special value indicating the number of status values defined in this enumeration.
     * When using this value, note that the version of cairo at run-time may have additional
     * status values defined than the value of this symbol at compile-time.
     * @since 1.10
     */
    CAIRO_STATUS_LAST_STATUS;
}
