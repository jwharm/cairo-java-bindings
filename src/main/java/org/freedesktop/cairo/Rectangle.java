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
 * A data structure for holding a rectangle.
 * 
 * @since 1.4
 */
public class Rectangle extends Proxy {

    /**
     * The memory layout of the native C struct
     * 
     * @return the memory layout of the native C struct
     */
    static MemoryLayout getMemoryLayout() {
        return MemoryLayout.structLayout(
                ValueLayout.JAVA_DOUBLE.withName("x"), 
                ValueLayout.JAVA_DOUBLE.withName("y"),
                ValueLayout.JAVA_DOUBLE.withName("width"),
                ValueLayout.JAVA_DOUBLE.withName("height"))
                .withName("cairo_rectangle_t");
    }

    private static final VarHandle X = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("x"));
    private static final VarHandle Y = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("y"));
    private static final VarHandle WIDTH = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("width"));
    private static final VarHandle HEIGHT = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("height"));

    /**
     * Get the x value of the Rectangle
     * 
     * @return the x value
     */
    public double x() {
        return (double) X.get(handle(), 0);
    }

    /**
     * Get the y value of the Rectangle
     * 
     * @return the y value
     */
    public double y() {
        return (double) Y.get(handle(), 0);
    }

    /**
     * Get the width value of the Rectangle
     * 
     * @return the width value
     */
    public double width() {
        return (double) WIDTH.get(handle(), 0);
    }

    /**
     * Get the height value of the Rectangle
     * 
     * @return the height value
     */
    public double height() {
        return (double) HEIGHT.get(handle(), 0);
    }

    /**
     * Constructor used internally to instantiate a java Rectangle object for a
     * native {@code cairo_rectangle_t} instance
     * 
     * @param address the memory address of the native {@code cairo_rectangle_t}
     *                instance
     */
    public Rectangle(MemorySegment address) {
        super(address.reinterpret(getMemoryLayout().byteSize()));
    }

    /**
     * A data structure for holding a rectangle.
     *
     * @param arena  the arena in which memory for the Rectangle is allocated
     * @param x      X coordinate of the left side of the rectangle
     * @param y      Y coordinate of the top side of the rectangle
     * @param width  width of the rectangle
     * @param height height of the rectangle
     * @return the newly created rectangle
     */
    public static Rectangle create(Arena arena, double x, double y, double width, double height) {
        Rectangle rect = new Rectangle(arena.allocate(getMemoryLayout()));
        X.set(rect.handle(), 0, x);
        Y.set(rect.handle(), 0, y);
        WIDTH.set(rect.handle(), 0, width);
        HEIGHT.set(rect.handle(), 0, height);
        return rect;
    }
    
    /**
     * String representation of this Rectangle
     * 
     * @return a String representation of this Rectangle
     */
    @Override
    public String toString() {
        return String.format("Rectangle address=%d x=%f y=%f width=%f height=%f",
                handle().address(), x(), y(), width(), height());
    }

    /**
     * Get the CairoRectangle GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_rectangle_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_rectangle_get_type = Interop.downcallHandle(
            "cairo_gobject_rectangle_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}