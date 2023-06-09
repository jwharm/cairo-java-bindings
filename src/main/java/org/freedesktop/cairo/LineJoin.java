package org.freedesktop.cairo;

/**
 * Specifies how to render the junction of two lines when stroking.
 * <p>
 * The default line join style is CAIRO_LINE_JOIN_MITER.
 */
public enum LineJoin {

    /**
     * use a sharp (angled) corner, see cairo_set_miter_limit()
     * 
     * @since 1.0
     */
    MITER,

    /**
     * use a rounded join, the center of the circle is the joint point
     * 
     * @since 1.0
     */
    ROUND,

    /**
     * use a cut-off join, the join is cut off at half the line width from the joint
     * point
     * 
     * @since 1.0
     */
    BEVEL;

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
    public static LineJoin of(int ordinal) {
        return values()[ordinal];
    }
}