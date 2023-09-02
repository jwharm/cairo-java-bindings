package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Proxy;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

/**
 * The TextExtents structure stores the extents of a single glyph or a string of
 * glyphs in user-space coordinates. Because text extents are in user-space
 * coordinates, they are mostly, but not entirely, independent of the current
 * transformation matrix. If you call {@code Context.scale(2.0, 2.0)}, text will
 * be drawn twice as big, but the reported text extents will not be doubled.
 * They will change slightly due to hinting (so you can't assume that metrics
 * are independent of the transformation matrix), but otherwise will remain
 * unchanged.
 * 
 * @since 1.0
 */
public class TextExtents extends Proxy {

    /**
     * The memory layout of the native C struct
     * 
     * @return the memory layout of the native C struct
     */
    static MemoryLayout getMemoryLayout() {
        return MemoryLayout.structLayout(
                ValueLayout.JAVA_DOUBLE.withName("x_bearing"),
                ValueLayout.JAVA_DOUBLE.withName("y_bearing"),
                ValueLayout.JAVA_DOUBLE.withName("width"),
                ValueLayout.JAVA_DOUBLE.withName("height"),
                ValueLayout.JAVA_DOUBLE.withName("x_advance"),
                ValueLayout.JAVA_DOUBLE.withName("y_advance"))
            .withName("cairo_text_extents_t");
    }

    private static final VarHandle X_BEARING = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("x_bearing"));
    private static final VarHandle Y_BEARING = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("y_bearing"));
    private static final VarHandle WIDTH = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("width"));
    private static final VarHandle HEIGHT = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("height"));
    private static final VarHandle X_ADVANCE = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("x_advance"));
    private static final VarHandle Y_ADVANCE = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("y_advance"));

    /**
     * Allocate a new {@code cairo_text_extents_t}
     */
    static TextExtents create() {
        return new TextExtents(SegmentAllocator.nativeAllocator(SegmentScope.auto()).allocate(getMemoryLayout()));
    }

    /**
     * The horizontal distance from the origin to the leftmost part of the glyphs as
     * drawn. Positive if the glyphs lie entirely to the right of the origin.
     * 
     * @return the horizontal distance from the origin to the leftmost part of the glyphs as drawn
     */
    public double xBearing() {
        return (double) X_BEARING.get(handle());
    }

    /**
     * The vertical distance from the origin to the topmost part of the glyphs as
     * drawn. Positive only if the glyphs lie completely below the origin; will
     * usually be negative.
     * 
     * @return the vertical distance from the origin to the topmost part of the glyphs as drawn
     */
    public double yBearing() {
        return (double) Y_BEARING.get(handle());
    }

    /**
     * Width of the glyphs as drawn
     * 
     * @return width of the glyphs as drawn
     */
    public double width() {
        return (double) WIDTH.get(handle());
    }

    /**
     * Height of the glyphs as drawn
     * 
     * @return height of the glyphs as drawn
     */
    public double height() {
        return (double) HEIGHT.get(handle());
    }

    /**
     * Distance to advance in the X direction after drawing these glyphs
     * @return distance to advance in the X direction after drawing these glyphs
     */
    public double xAdvance() {
        return (double) X_ADVANCE.get(handle());
    }

    /**
     * Distance to advance in the Y direction after drawing these glyphs. Will
     * typically be zero except for vertical text layout as found in East-Asian
     * languages.
     * 
     * @return distance to advance in the Y direction after drawing these glyphs
     */
    public double yAdvance() {
        return (double) Y_ADVANCE.get(handle());
    }

    /**
     * Constructor used internally to instantiate a java TextExtents object for a
     * native {@code cairo_text_extents_t} instance
     * 
     * @param address the memory address of the native {@code cairo_text_extents_t}
     *                instance
     */
    public TextExtents(MemorySegment address) {
        super(address);
    }
}
