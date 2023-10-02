package org.freedesktop.cairo;

/**
 * Specifies if color fonts are to be rendered using the color glyphs or
 * outline glyphs. Glyphs that do not have a color presentation, and
 * non-color fonts are not affected by this font option.
 *
 * @since 1.18
 */
public enum ColorMode {

    /**
     * Use the default color mode for font backend and target device
     *
     * @since 1.18
     */
    DEFAULT,

    /**
     * Disable rendering color glyphs. Glyphs are always rendered as outline
     * glyphs
     *
     * @since 1.18
     */
    NO_COLOR,

    /**
     * Enable rendering color glyphs. If the font contains a color presentation
     * for a glyph, and when supported by the font backend, the glyph will be
     * rendered in color
     *
     * @since 1.18
     */
    COLOR;

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
    public static ColorMode of(int ordinal) {
        return values()[ordinal];
    }
}
