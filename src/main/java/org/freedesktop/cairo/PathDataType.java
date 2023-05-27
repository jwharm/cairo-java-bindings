package org.freedesktop.cairo;

/**
 * Describes the type of one portion of a path when represented as a
 * {@link Path}. See {@link PathData} for details.
 * 
 * @since 1.0
 */
public enum PathDataType {
	
	/**
	 * A move-to operation
	 * @since 1.0
	 */
	MOVE_TO,
	
	/**
	 * A line-to operation
	 * @since 1.0
	 */
	LINE_TO,
	
	/**
	 * A curve-to operation
	 * @since 1.0
	 */
	CURVE_TO,
	
	/**
	 * A close-path operation
	 * @since 1.0
	 */
	CLOSE_PATH;

	/**
	 * Returns the enum constant for the given ordinal (its position in the enum
	 * declaration).
	 * 
	 * @param ordinal the position in the enum declaration, starting from zero
	 * @return the enum constant for the given ordinal
	 */
	public static PathDataType of(int ordinal) {
		return values()[ordinal];
	}
}
