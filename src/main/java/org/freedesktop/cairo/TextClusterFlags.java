package org.freedesktop.cairo;

/**
 * Specifies properties of a text cluster mapping.
 * 
 * @since 1.8
 */
public enum TextClusterFlags {

    /**
     * The clusters in the cluster array map to glyphs in the glyph array from end
     * to start.
     * 
     * @since 1.8
     */
    BACKWARD(0x00000001);

    private final int value;

    TextClusterFlags(int value) {
        this.value = value;
    }

    /**
     * Return the value of this enum
     * 
     * @return the value
     */
    public int value() {
        return value;
    }

    /**
     * Returns the enum member for the given value.
     *
     * @param value the value of the enum member
     * @return the enum member for the given value
     */
    public static TextClusterFlags of(int value) {
        if (value == 0x00000001) {
            return BACKWARD;
        } else {
            throw new IllegalArgumentException("No TextClusterFlags enum with value " + value);
        }
    }
}
