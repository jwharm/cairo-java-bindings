package org.freedesktop.cairo;

import io.github.jwharm.javagi.base.Bitfield;

/**
 * A set of synthesis options to control how FreeType renders the glyphs for a
 * particular font face.
 * <p>
 * Individual synthesis features of a {@link FTFontFace} can be set using
 * {@link FTFontFace#setSynthesize(FTSynthesize)}, or disabled using
 * {@link FTFontFace#unsetSynthesize(FTSynthesize)}. The currently enabled set of
 * synthesis options can be queried with {@link FTFontFace#getSynthesize()}.
 * <p>
 * Note that when synthesizing glyphs, the font metrics returned will only be
 * estimates.
 * 
 * @since 1.12
 */
public class FTSynthesize extends Bitfield {

    public FTSynthesize(int value) {
        super(value);
    }

    /**
     * Embolden the glyphs (redraw with a pixel offset)
     */
    public static final FTSynthesize BOLD = new FTSynthesize(1 << 0);

    /**
     * Slant the glyph outline by 12 degrees to the right.
     */
    public static final FTSynthesize OBLIQUE = new FTSynthesize(1 << 1);
    
    /**
     * Combine (bitwise OR) operation
     * @param masks one or more values to combine with
     * @return the combined value by calculating {@code this | mask} 
     */
    public FTSynthesize or(FTSynthesize... masks) {
        int value = this.getValue();
        for (FTSynthesize arg : masks) {
            value |= arg.getValue();
        }
        return new FTSynthesize(value);
    }
    
    /**
     * Combine (bitwise OR) operation
     * @param mask the first value to combine
     * @param masks the other values to combine
     * @return the combined value by calculating {@code mask | masks[0] | masks[1] | ...} 
     */
    public static FTSynthesize combined(FTSynthesize mask, FTSynthesize... masks) {
        int value = mask.getValue();
        for (FTSynthesize arg : masks) {
            value |= arg.getValue();
        }
        return new FTSynthesize(value);
    }
}
