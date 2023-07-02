package org.freedesktop.cairo;

/**
 * A set of synthesis options to control how FreeType renders the glyphs for a
 * particular font face.
 * <p>
 * Individual synthesis features of a {@link FTFontFace} can be set using
 * {@link FTFontFace#setSynthesize(int)}, or disabled using
 * {@link FTFontFace#unsetSynthesize(int)}. The currently enabled set of
 * synthesis options can be queried with {@link FTFontFace#getSynthesize()}.
 * <p>
 * Note that when synthesizing glyphs, the font metrics returned will only be
 * estimates.
 * 
 * @since 1.12
 */
public enum FTSynthesize {

    /**
     * Embolden the glyphs (redraw with a pixel offset)
     */
    BOLD(1 << 0),

    /**
     * Slant the glyph outline by 12 degrees to the right.
     */
    OBLIQUE(1 << 1);

    private final int value;

    FTSynthesize(int value) {
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
    public static FTSynthesize of(int value) {
        if (value == 1 << 0) {
            return BOLD;
        } else if (value == 1 << 1) {
            return OBLIQUE;
        } else {
            throw new IllegalArgumentException("No FTSynthesize enum with value " + value);
        }
    }
}
