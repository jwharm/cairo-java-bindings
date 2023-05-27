package org.freedesktop.cairo;

/**
 * PatternType is used to describe the type of a given pattern.
 * <p>
 * The type of a pattern is determined by the function used to create it. The
 * cairo_pattern_create_rgb() and cairo_pattern_create_rgba() functions create
 * SOLID patterns. The remaining cairo_pattern_create functions map to pattern
 * types in obvious ways.
 * <p>
 * The pattern type can be queried with cairo_pattern_get_type()
 * <p>
 * Most Pattern functions can be called with a pattern of any type, (though
 * trying to change the extend or filter for a solid pattern will have no
 * effect). A notable exception is cairo_pattern_add_color_stop_rgb() and
 * cairo_pattern_add_color_stop_rgba() which must only be called with gradient
 * patterns (either LINEAR or RADIAL). Otherwise the pattern will be shutdown
 * and put into an error state.
 * <p>
 * New entries may be added in future versions.
 * 
 * @since 1.2
 */
public enum PatternType {

	/**
	 * The pattern is a solid (uniform) color. It may be opaque or translucent
	 * @since 1.2
	 */
	SOLID,
	
	/**
	 * The pattern is a based on a surface (an image)
	 * @since 1.2
	 */
	SURFACE,
	
	/**
	 * The pattern is a linear gradient
	 * @since 1.2
	 */
	LINEAR,
	
	/**
	 * The pattern is a radial gradient
	 * @since 1.2
	 */
	RADIAL,
	
	/**
	 * The pattern is a mesh
	 * @since 1.2
	 */
	MESH,
	
	/**
	 * The pattern is a user pattern providing raster data
	 * @since 1.2
	 */
	RASTER_SOURCE;

	/**
	 * Returns the enum constant for the given ordinal (its position in the enum
	 * declaration).
	 * 
	 * @param ordinal the position in the enum declaration, starting from zero
	 * @return the enum constant for the given ordinal
	 */
	public static PatternType of(int ordinal) {
		return values()[ordinal];
	}
}
