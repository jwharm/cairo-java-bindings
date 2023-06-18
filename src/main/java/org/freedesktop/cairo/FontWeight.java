package org.freedesktop.cairo;

/**
 * Specifies variants of a font face based on their weight.
 * 
 * @since 1.0
 */
public enum FontWeight {

	/**
	 * Normal font weight
	 * 
	 * @since 1.0
	 */
	NORMAL,

	/**
	 * Bold font weight
	 * 
	 * @since 1.0
	 */
	BOLD;

	/**
	 * Return the value of this enum
	 * 
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
	public static FontWeight of(int ordinal) {
		return values()[ordinal];
	}
}
