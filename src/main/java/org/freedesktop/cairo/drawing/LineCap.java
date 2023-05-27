package org.freedesktop.cairo.drawing;

/**
 * Specifies how to render the endpoints of the path when stroking.
 * <p>
 * The default line cap style is CAIRO_LINE_CAP_BUTT.
 */
public enum LineCap {

	/**
	 * start(stop) the line exactly at the start(end) point
	 * 
	 * @since 1.0
	 */
	BUTT,

	/**
	 * use a round ending, the center of the circle is the end point
	 * 
	 * @since 1.0
	 */
	ROUND,

	/**
	 * use a squared ending, the center of the square is the end point
	 * 
	 * @since 1.0
	 */
	SQUARE;
}