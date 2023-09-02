package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Proxy;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

/**
 * The FontExtents structure stores metric information for a font. Values are
 * given in the current user-space coordinate system.
 * <p>
 * Because font metrics are in user-space coordinates, they are mostly, but not
 * entirely, independent of the current transformation matrix. If you call
 * {@code Context.scale(2.0, 2.0)}, text will be drawn twice as big, but the
 * reported text extents will not be doubled. They will change slightly due to
 * hinting (so you can't assume that metrics are independent of the
 * transformation matrix), but otherwise will remain unchanged.
 * 
 * @since 1.0
 */
public class FontExtents extends Proxy {

    /**
     * The memory layout of the native C struct
     * 
     * @return the memory layout of the native C struct
     */
    static MemoryLayout getMemoryLayout() {
        return MemoryLayout.structLayout(
                ValueLayout.JAVA_DOUBLE.withName("ascent"),
                ValueLayout.JAVA_DOUBLE.withName("descent"),
                ValueLayout.JAVA_DOUBLE.withName("height"),
                ValueLayout.JAVA_DOUBLE.withName("max_x_advance"),
                ValueLayout.JAVA_DOUBLE.withName("max_y_advance"))
            .withName("cairo_font_extents_t");
    }

    private static final VarHandle ASCENT = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("ascent"));
    private static final VarHandle DESCENT = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("descent"));
    private static final VarHandle HEIGHT = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("height"));
    private static final VarHandle MAX_X_ADVANCE = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("max_x_advance"));
    private static final VarHandle MAX_Y_ADVANCE = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("max_y_advance"));

    /**
     * Allocate a new {@code cairo_font_extents_t}
     */
    static FontExtents create() {
        return new FontExtents(SegmentAllocator.nativeAllocator(SegmentScope.auto()).allocate(getMemoryLayout()));
    }

    /**
     * The distance that the font extends above the baseline. Note that this is not
     * always exactly equal to the maximum of the extents of all the glyphs in the
     * font, but rather is picked to express the font designer's intent as to how
     * the font should align with elements above it.
     * 
     * @return the distance that the font extends above the baseline
     */
    public double ascent() {
        return (double) ASCENT.get(handle());
    }

    /**
     * The distance that the font extends below the baseline. This value is positive
     * for typical fonts that include portions below the baseline. Note that this is
     * not always exactly equal to the maximum of the extents of all the glyphs in
     * the font, but rather is picked to express the font designer's intent as to
     * how the font should align with elements below it.
     * 
     * @return the distance that the font extends below the baseline
     */
    public double descent() {
        return (double) DESCENT.get(handle());
    }

    /**
     * The recommended vertical distance between baselines when setting consecutive
     * lines of text with the font. This is greater than {@code ascent} +
     * {@code descent} by a quantity known as the <i>line spacing</i> or <i>external
     * leading</i>. When space is at a premium, most fonts can be set with only a
     * distance of {@code ascent} + {@code descent} between lines.
     * 
     * @return the recommended vertical distance between baselines when setting
     *         consecutive lines of text with the font
     */
    public double height() {
        return (double) HEIGHT.get(handle());
    }

    /**
     * The maximum distance in the X direction that the origin is advanced for any
     * glyph in the font.
     * 
     * @return the maximum distance in the X direction that the origin is advanced
     *         for any glyph in the font
     */
    public double maxXAdvance() {
        return (double) MAX_X_ADVANCE.get(handle());
    }

    /**
     * The maximum distance in the Y direction that the origin is advanced for any
     * glyph in the font. This will be zero for normal fonts used for horizontal
     * writing. (The scripts of East Asia are sometimes written vertically.)
     * 
     * @return the maximum distance in the Y direction that the origin is advanced
     *         for any glyph in the font
     */
    public double maxYAdvance() {
        return (double) MAX_Y_ADVANCE.get(handle());
    }

    /**
     * Constructor used internally to instantiate a java FontExtents object for a
     * native {@code cairo_font_extents_t} instance
     * 
     * @param address the memory address of the native {@code cairo_font_extents_t}
     *                instance
     */
    public FontExtents(MemorySegment address) {
        super(address);
    }
}
