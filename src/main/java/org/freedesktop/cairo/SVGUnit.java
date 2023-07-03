package org.freedesktop.cairo;

/**
 * SVGUnit is used to describe the units valid for coordinates and lengths in
 * the SVG specification.
 * 
 * See also:
 * <ul>
 * <li><a href=
 * "https://www.w3.org/TR/SVG/coords.html#Units">https://www.w3.org/TR/SVG/coords.html#Units</a>
 * <li><a href=
 * "https://www.w3.org/TR/SVG/types.html#InterfaceSVGLength">https://www.w3.org/TR/SVG/types.html#InterfaceSVGLength</a>
 * <li><a href=
 * "https://www.w3.org/TR/css-values-3/#lengths">https://www.w3.org/TR/css-values-3/#lengths</a>
 * </ul>
 * 
 * @see Surface
 * @since 1.16
 */
public enum SVGUnit {

    /**
     * User unit, a value in the current coordinate system. If used in the root
     * element for the initial coordinate systems it corresponds to pixels.
     * 
     * @since 1.16
     */
    USER,

    /**
     * The size of the element's font.
     * 
     * @since 1.16
     */
    EM,

    /**
     * The x-height of the element’s font.
     * 
     * @since 1.16
     */
    EX,

    /**
     * Pixels (1px = 1/96th of 1in).
     * 
     * @since 1.16
     */
    PX,

    /**
     * Inches (1in = 2.54cm = 96px).
     * 
     * @since 1.16
     */
    IN,

    /**
     * Centimeters (1cm = 96px/2.54).
     * 
     * @since 1.16
     */
    CM,

    /**
     * Millimeters (1mm = 1/10th of 1cm).
     * 
     * @since 1.16
     */
    MM,

    /**
     * Points (1pt = 1/72th of 1in).
     * 
     * @since 1.16
     */
    PT,

    /**
     * Picas (1pc = 1/6th of 1in).
     * 
     * @since 1.16
     */
    PC,

    /**
     * Percent, a value that is some fraction of another reference value.
     * 
     * @since 1.16
     */
    PERCENT;

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
    public static SVGUnit of(int ordinal) {
        return values()[ordinal];
    }
}
