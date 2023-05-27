package org.freedesktop.cairo;

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

	/**
	 * Returns the enum constant for the given ordinal (its position in the enum
	 * declaration).
	 * 
	 * @param ordinal the position in the enum declaration, starting from zero
	 * @return the enum constant for the given ordinal
	 */
	public static LineCap of(int ordinal) {
		return values()[ordinal];
	}
}