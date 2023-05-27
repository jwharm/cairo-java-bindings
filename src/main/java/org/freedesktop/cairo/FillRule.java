package org.freedesktop.cairo;

/**
 * cairo_fill_rule_t is used to select how paths are filled. For both fill
 * rules, whether or not a point is included in the fill is determined by taking
 * a ray from that point to infinity and looking at intersections with the path.
 * The ray can be in any direction, as long as it doesn't pass through the end
 * point of a segment or have a tricky intersection such as intersecting tangent
 * to the path. (Note that filling is not actually implemented in this way. This
 * is just a description of the rule that is applied.)
 * <p>
 * The default fill rule is CAIRO_FILL_RULE_WINDING.
 * <p>
 * New entries may be added in future versions.
 * 
 * @since 1.0
 */
public enum FillRule {

	/**
	 * If the path crosses the ray from left-to-right, counts +1. If the path
	 * crosses the ray from right to left, counts -1. (Left and right are determined
	 * from the perspective of looking along the ray from the starting point.) If
	 * the total count is non-zero, the point will be filled.
	 * 
	 * @since 1.0
	 */
	WINDING,

	/**
	 * Counts the total number of intersections, without regard to the orientation
	 * of the contour. If the total number of intersections is odd, the point will
	 * be filled.
	 * 
	 * @since 1.0
	 */
	EVEN_ODD;

	/**
	 * Returns the enum constant for the given ordinal (its position in the enum
	 * declaration).
	 * 
	 * @param ordinal the position in the enum declaration, starting from zero
	 * @return the enum constant for the given ordinal
	 */
	public static FillRule of(int ordinal) {
		return values()[ordinal];
	}
}