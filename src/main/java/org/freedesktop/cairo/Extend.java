package org.freedesktop.cairo;

/**
 * Extend is used to describe how pattern color/alpha will be determined for
 * areas "outside" the pattern's natural area, (for example, outside the surface
 * bounds or outside the gradient geometry).
 * <p>
 * Mesh patterns are not affected by the extend mode.
 * <p>
 * The default extend mode is {@link #NONE} for surface patterns and
 * {@link #PAD} for gradient patterns.
 * <p>
 * New entries may be added in future versions.
 * 
 * @since 1.0
 */
public enum Extend {

	/**
	 * pixels outside of the source pattern are fully transparent
	 * 
	 * @since 1.0
	 */
	NONE,

	/**
	 * the pattern is tiled by repeating
	 * 
	 * @since 1.0
	 */
	REPEAT,

	/**
	 * the pattern is tiled by reflecting at the edges
	 * 
	 * @since 1.0; but only implemented for surface patterns since 1.6
	 */
	REFLECT,

	/**
	 * pixels outside of the pattern copy the closest pixel from the source
	 * 
	 * @since 1.2; but only implemented for surface patterns since 1.6
	 */
	PAD;

	/**
	 * Returns the enum constant for the given ordinal (its position in the enum
	 * declaration).
	 * 
	 * @param ordinal the position in the enum declaration, starting from zero
	 * @return the enum constant for the given ordinal
	 */
	public static Extend of(int ordinal) {
		return values()[ordinal];
	}
}
