/* cairo-java-bindings - Java language bindings for cairo
 * Copyright (C) 2023 Jan-Willem Harmannij
 *
 * SPDX-License-Identifier: LGPL-2.1-or-later
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://www.gnu.org/licenses/>.
 */

package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Proxy;

import java.lang.foreign.*;
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
     * Allocate a new, uninitialized {@code cairo_text_extents_t}
     *
     * @param arena the arena in which the TextExtents will be allocated
     * @return a newly allocated, uninitialized TextExtents
     */
    public static TextExtents create(Arena arena) {
        return new TextExtents(arena.allocate(getMemoryLayout()));
    }

    /**
     * The horizontal distance from the origin to the leftmost part of the glyphs as
     * drawn. Positive if the glyphs lie entirely to the right of the origin.
     * 
     * @return the horizontal distance from the origin to the leftmost part of the glyphs as drawn
     */
    public double xBearing() {
        return (double) X_BEARING.get(handle(), 0);
    }

    /**
     * The vertical distance from the origin to the topmost part of the glyphs as
     * drawn. Positive only if the glyphs lie completely below the origin; will
     * usually be negative.
     * 
     * @return the vertical distance from the origin to the topmost part of the glyphs as drawn
     */
    public double yBearing() {
        return (double) Y_BEARING.get(handle(), 0);
    }

    /**
     * Width of the glyphs as drawn
     * 
     * @return width of the glyphs as drawn
     */
    public double width() {
        return (double) WIDTH.get(handle(), 0);
    }

    /**
     * Height of the glyphs as drawn
     * 
     * @return height of the glyphs as drawn
     */
    public double height() {
        return (double) HEIGHT.get(handle(), 0);
    }

    /**
     * Distance to advance in the X direction after drawing these glyphs
     * @return distance to advance in the X direction after drawing these glyphs
     */
    public double xAdvance() {
        return (double) X_ADVANCE.get(handle(), 0);
    }

    /**
     * Distance to advance in the Y direction after drawing these glyphs. Will
     * typically be zero except for vertical text layout as found in East-Asian
     * languages.
     * 
     * @return distance to advance in the Y direction after drawing these glyphs
     */
    public double yAdvance() {
        return (double) Y_ADVANCE.get(handle(), 0);
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
