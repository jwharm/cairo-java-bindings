package org.freedesktop.cairo;

/**
 * Filter is used to indicate what filtering should be applied when reading
 * pixel values from patterns. See cairo_pattern_set_filter() for indicating the
 * desired filter to be used with a particular pattern.
 * 
 * @since 1.0
 */
public enum Filter {

    /**
     * A high-performance filter, with quality similar to {@link #NEAREST}
     * 
     * @since 1.0
     */
    FAST,

    /**
     * A reasonable-performance filter, with quality similar to {@link #BILINEAR}
     * 
     * @since 1.0
     */
    GOOD,

    /**
     * The highest-quality available, performance may not be suitable for
     * interactive use.
     * 
     * @since 1.0
     */
    BEST,

    /**
     * Nearest-neighbor filtering
     * 
     * @since 1.0
     */
    NEAREST,

    /**
     * Linear interpolation in two dimensions
     * 
     * @since 1.0
     */
    BILINEAR,

    /**
     * This filter value is currently unimplemented, and should not be used in
     * current code.
     * 
     * @since 1.0
     */
    GAUSSIAN;

    /**
     * Return the value of this enum
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
    public static Filter of(int ordinal) {
        return values()[ordinal];
    }
}
