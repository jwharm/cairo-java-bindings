package org.freedesktop.cairo;

/**
 * Used to identify the memory format of image data.
 * <p>
 * New entries may be added in future versions.
 * 
 * @see {@link Surface}
 */
public enum Format {

	/**
	 * no such format exists or is supported.
	 */
	INVALID,

	/**
	 * each pixel is a 32-bit quantity, with alpha in the upper 8 bits, then red,
	 * then green, then blue. The 32-bit quantities are stored native-endian.
	 * Pre-multiplied alpha is used. (That is, 50% transparent red is 0x80800000,
	 * not 0x80ff0000.)
	 * 
	 * @since 1.0
	 */
	ARGB32,

	/**
	 * each pixel is a 32-bit quantity, with the upper 8 bits unused. Red, Green,
	 * and Blue are stored in the remaining 24 bits in that order.
	 * 
	 * @since 1.0
	 */
	RGB24,

	/**
	 * each pixel is a 8-bit quantity holding an alpha value.
	 * 
	 * @since 1.0
	 */
	A8,

	/**
	 * each pixel is a 1-bit quantity holding an alpha value. Pixels are packed
	 * together into 32-bit quantities. The ordering of the bits matches the
	 * endianness of the platform. On a big-endian machine, the first pixel is in
	 * the uppermost bit, on a little-endian machine the first pixel is in the
	 * least-significant bit.
	 * 
	 * @since 1.0
	 */
	A1,

	/**
	 * each pixel is a 16-bit quantity with red in the upper 5 bits, then green in
	 * the middle 6 bits, and blue in the lower 5 bits.
	 * 
	 * @since 1.2
	 */
	RGB16_565,

	/**
	 * like RGB24 but with 10bpc.
	 * 
	 * @since 1.12
	 */
	RGB30;

	/**
	 * Returns the enum constant for the given ordinal (its position in the enum
	 * declaration).
	 * 
	 * @param ordinal the position in the enum declaration, starting from zero
	 * @return the enum constant for the given ordinal
	 */
	public static Format of(int ordinal) {
		return values()[ordinal];
	}
}
