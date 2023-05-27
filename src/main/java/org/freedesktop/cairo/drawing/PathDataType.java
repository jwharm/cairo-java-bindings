package org.freedesktop.cairo.drawing;

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

}
