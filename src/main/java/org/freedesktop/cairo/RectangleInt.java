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

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;

/**
 * A data structure for holding a rectangle with integer coordinates.
 * 
 * @since 1.10
 */
public class RectangleInt extends Proxy {

    /**
     * The memory layout of the native C struct
     * 
     * @return the memory layout of the native C struct
     */
    static MemoryLayout getMemoryLayout() {
        return MemoryLayout.structLayout(
                ValueLayout.JAVA_INT.withName("x"), 
                ValueLayout.JAVA_INT.withName("y"), 
                ValueLayout.JAVA_INT.withName("width"), 
                ValueLayout.JAVA_INT.withName("height"))
                .withName("cairo_rectangle_int_t");
    }

    private static final VarHandle X = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("x"));
    private static final VarHandle Y = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("y"));
    private static final VarHandle WIDTH = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("width"));
    private static final VarHandle HEIGHT = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("height"));

    /**
     * Get the x value of the RectangleInt
     * 
     * @return the x value
     */
    public int x() {
        return (int) X.get(handle());
    }

    /**
     * Get the y value of the RectangleInt
     * 
     * @return the y value
     */
    public int y() {
        return (int) Y.get(handle());
    }

    /**
     * Get the width value of the RectangleInt
     * 
     * @return the width value
     */
    public int width() {
        return (int) WIDTH.get(handle());
    }

    /**
     * Get the height value of the RectangleInt
     * 
     * @return the height value
     */
    public int height() {
        return (int) HEIGHT.get(handle());
    }

    /**
     * Constructor used internally to instantiate a java RectangleInt object for a
     * native {@code cairo_rectangle_int_t} instance
     * 
     * @param address the memory address of the native {@code cairo_rectangle_int_t}
     *                instance
     */
    public RectangleInt(MemorySegment address) {
        super(address);
    }

    /**
     * Create a rectangle with integer coordinates.
     *
     * @param arena  the arena in which memory for the RectangleInt is allocated
     * @param x      X coordinate of the left side of the rectangle
     * @param y      Y coordinate of the the top side of the rectangle
     * @param width  width of the rectangle
     * @param height height of the rectangle
     * @return the newly created rectangle
     */
    public static RectangleInt create(Arena arena, int x, int y, int width, int height) {
        RectangleInt rect = new RectangleInt(arena.allocate(getMemoryLayout()));
        X.set(rect.handle(), x);
        Y.set(rect.handle(), y);
        WIDTH.set(rect.handle(), width);
        HEIGHT.set(rect.handle(), height);
        return rect;
    }
    
    /**
     * String representation of this RectangleInt
     * 
     * @return a String representation of this RectangleInt
     */
    @Override
    public String toString() {
        return String.format("RectangleInt address=%d x=%d y=%d width=%d height=%d",
                handle().address(), x(), y(), width(), height());
    }

    /**
     * Get the CairoRectangleInt GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_rectangle_int_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_rectangle_int_get_type = Interop.downcallHandle(
            "cairo_gobject_rectangle_int_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}