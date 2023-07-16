package org.freedesktop.cairo;

/**
 * Used to identify the memory format of image data.
 * <p>
 * New entries may be added in future versions.
 * 
 * @see Surface
 */
public enum Format {

    /**
     * no such format exists or is supported.
     */
    INVALID(-1),

    /**
     * each pixel is a 32-bit quantity, with alpha in the upper 8 bits, then red,
     * then green, then blue. The 32-bit quantities are stored native-endian.
     * Pre-multiplied alpha is used. (That is, 50% transparent red is 0x80800000,
     * not 0x80ff0000.)
     * 
     * @since 1.0
     */
    ARGB32(0),

    /**
     * each pixel is a 32-bit quantity, with the upper 8 bits unused. Red, Green,
     * and Blue are stored in the remaining 24 bits in that order.
     * 
     * @since 1.0
     */
    RGB24(1),

    /**
     * each pixel is a 8-bit quantity holding an alpha value.
     * 
     * @since 1.0
     */
    A8(2),

    /**
     * each pixel is a 1-bit quantity holding an alpha value. Pixels are packed
     * together into 32-bit quantities. The ordering of the bits matches the
     * endianness of the platform. On a big-endian machine, the first pixel is in
     * the uppermost bit, on a little-endian machine the first pixel is in the
     * least-significant bit.
     * 
     * @since 1.0
     */
    A1(3),

    /**
     * each pixel is a 16-bit quantity with red in the upper 5 bits, then green in
     * the middle 6 bits, and blue in the lower 5 bits.
     * 
     * @since 1.2
     */
    RGB16_565(4),

    /**
     * like RGB24 but with 10bpc.
     * 
     * @since 1.12
     */
    RGB30(5);

    private final int value;

    Format(int value) {
        this.value = value;
    }

    /**
     * Return the value of this enum
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the enum member for the given value.
     *
     * @param value the value of the enum member
     * @return the enum member for the given value
     */
    public static Format of(int value) {
        if (value == -1) {
            return INVALID;
        } else if (value == 0) {
            return ARGB32;
        } else if (value == 1) {
            return RGB24;
        } else if (value == 2) {
            return A8;
        } else if (value == 3) {
            return A1;
        } else if (value == 4) {
            return RGB16_565;
        } else if (value == 5) {
            return RGB30;
        } else {
            throw new IllegalArgumentException("No Format enum with value " + value);
        }
    }
}
