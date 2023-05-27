package org.freedesktop.cairo.surfaces;

/**
 * cairo_content_t is used to describe the content that a surface will contain,
 * whether color information, alpha information (translucence vs. opacity), or
 * both.
 * <p>
 * Note: The large values here are designed to keep cairo_content_t values
 * distinct from cairo_format_t values so that the implementation can detect the
 * error if users confuse the two types.
 * 
 * @since 1.0
 */
public enum Content {

	/**
	 * The surface will hold color content only.
	 * 
	 * @since 1.0
	 */
	COLOR,

	/**
	 * The surface will hold alpha content only.
	 * 
	 * @since 1.0
	 */
	ALPHA,

	/**
	 * The surface will hold color and alpha content.
	 * 
	 * @since 1.0
	 */
	COLOR_ALPHA;
}