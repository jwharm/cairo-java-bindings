package org.freedesktop.cairo;

/**
 * A set of script output variants.
 * 
 * @since 1.12
 */
public enum ScriptMode {

	/**
	 * the output will be in readable text (default).
	 * 
	 * @since 1.12
	 */
	ASCII,
	
	/**
	 * the output will use byte codes.
	 * 
	 * @since 1.12
	 */
	BINARY;

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
	public static ScriptMode of(int ordinal) {
		return values()[ordinal];
	}
}
