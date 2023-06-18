package org.freedesktop.cairo;

/**
 * The subpixel order specifies the order of color elements within each pixel on
 * the display device when rendering with an antialiasing mode of
 * {@link Antialias#SUBPIXEL}.
 * 
 * @since 1.0
 */
public enum SubpixelOrder {

	/**
	 * Use the default subpixel order for for the target device
	 * 
	 * @since 1.0
	 */
	DEFAULT,

	/**
	 * Subpixel elements are arranged horizontally with red at the left
	 * 
	 * @since 1.0
	 */
	RGB,

	/**
	 * Subpixel elements are arranged horizontally with blue at the left
	 * 
	 * @since 1.0
	 */
	BGR,

	/**
	 * Subpixel elements are arranged vertically with red at the top
	 * 
	 * @since 1.0
	 */
	VRGB,

	/**
	 * Subpixel elements are arranged vertically with blue at the top
	 * 
	 * @since 1.0
	 */
	VBGR;

	/**
	 * Return the value of this enum
	 * @return the value
	 */
	public int value() {
		return ordinal();
	}

	/**
	 * Returns the enum constant for the given ordinal (its position in the enum
	 * declaration).
	 * 
	 * @param ordinal the position in the enum declaration, starting from zero
	 * @return the enum constant for the given ordinal
	 */
	public static SubpixelOrder of(int ordinal) {
		return values()[ordinal];
	}
}
