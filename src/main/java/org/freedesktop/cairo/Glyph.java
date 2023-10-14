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

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.Proxy;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

/**
 * The Glyph structure holds information about a single glyph when drawing or
 * measuring text. A font is (in simple terms) a collection of shapes used to
 * draw text. A glyph is one of these shapes. There can be multiple glyphs for a
 * single character (alternates to be used in different contexts, for example),
 * or a glyph can be a <i>ligature</i> of multiple characters. Cairo doesn't
 * expose any way of converting input text into glyphs, so in order to use the
 * Cairo interfaces that take arrays of glyphs, you must directly access the
 * appropriate underlying font system.
 * <p>
 * Note that the offsets given by {@code x} and {@code y} are not cumulative.
 * When drawing or measuring text, each glyph is individually positioned with
 * respect to the overall origin.
 * 
 * @since 1.0
 */
public class Glyph extends Proxy {

    /**
     * The memory layout of the native C struct
     * 
     * @return the memory layout of the native C struct
     */
    static MemoryLayout getMemoryLayout() {
        return MemoryLayout.structLayout(
                ValueLayout.JAVA_LONG.withName("index"),
                ValueLayout.JAVA_DOUBLE.withName("x"),
                ValueLayout.JAVA_DOUBLE.withName("y"))
            .withName("cairo_glyph_t");
    }

    private static final VarHandle INDEX = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("index"));
    private static final VarHandle X = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("x"));
    private static final VarHandle Y = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("y"));

    /**
     * Glyph index in the font. The exact interpretation of the glyph index depends
     * on the font technology being used.
     * 
     * @return glyph index in the font
     */
    public long index() {
        return (long) INDEX.get(handle());
    }

    /**
     * The offset in the X direction between the origin used for drawing or
     * measuring the string and the origin of this glyph.
     * 
     * @return the offset in the X direction
     */
    public double x() {
        return (double) X.get(handle());
    }

    /**
     * The offset in the Y direction between the origin used for drawing or
     * measuring the string and the origin of this glyph.
     * 
     * @return the offset in the Y direction
     */
    public double y() {
        return (double) Y.get(handle());
    }

    /**
     * Constructor used internally to instantiate a java Glyph object for a native
     * {@code cairo_glyph_t} instance
     * 
     * @param address the memory address of the native {@code cairo_glyph_t}
     *                instance
     */
    public Glyph(MemorySegment address) {
        super(Interop.reinterpret(address, getMemoryLayout()));
    }
}
