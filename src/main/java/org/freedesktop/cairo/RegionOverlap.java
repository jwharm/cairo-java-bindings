package org.freedesktop.cairo;

/**
 * Used as the return value for cairo_region_contains_rectangle().
 * 
 * @since 1.10
 */
public enum RegionOverlap {

    /**
     * The contents are entirely inside the region.
     * 
     * @since 1.10
     */
    IN,

    /**
     * The contents are entirely outside the region.
     * 
     * @since 1.10
     */
    OUT,

    /**
     * The contents are partially inside and partially outside the region.
     * 
     * @since 1.10
     */
    PART;

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
    public static RegionOverlap of(int ordinal) {
        return values()[ordinal];
    }
}
