package org.freedesktop.cairo;

/**
 * Specifies variants of a font face based on their slant.
 * 
 * @since 1.0
 */
public enum FontSlant {

    /**
     * Upright font style
     * 
     * @since 1.0
     */
    NORMAL,

    /**
     * Italic font style
     * 
     * @since 1.0
     */
    ITALIC,

    /**
     * Oblique font style
     * 
     * @since 1.0
     */
    OBLIQUE;

    /**
     * Return the value of this enum
     * 
     * @return the value
     */
    public int getValue() {
        return ordinal();
    }

    /**
     * Returns the enum constant for the given ordinal (its position in the enum
     * declaration).
     * 
     * @param ordinal the position in the enum declaration, starting from zero
     * @return the enum constant for the given ordinal
     */
    public static FontSlant of(int ordinal) {
        return values()[ordinal];
    }
}
