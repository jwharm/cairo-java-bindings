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
import io.github.jwharm.cairobindings.MemoryCleaner;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * A linear gradient pattern.
 */
public class LinearGradient extends Gradient {

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Constructor used internally to instantiate a java LinearPattern object for a
     * native {@code cairo_pattern_t} instance
     * 
     * @param address the memory address of the native {@code cairo_pattern_t}
     *                instance
     */
    public LinearGradient(MemorySegment address) {
        super(address);
    }

    /**
     * Create a new linear gradient {@link Pattern} along the line defined by (x0,
     * y0) and (x1, y1). Before using the gradient pattern, a number of color stops
     * should be defined using
     * {@link #addColorStopRGB(double, double, double, double)} or
     * {@link #addColorStopRGBA(double, double, double, double, double)}.
     * <p>
     * Note: The coordinates here are in pattern space. For a new pattern, pattern
     * space is identical to user space, but the relationship between the spaces can
     * be changed with cairo_pattern_set_matrix().
     * 
     * @param x0 x coordinate of the start point
     * @param y0 y coordinate of the start point
     * @param x1 x coordinate of the end point
     * @param y1 y coordinate of the end point
     * @return the newly created {@link Pattern}
     * @since 1.0
     */
    public static LinearGradient create(double x0, double y0, double x1, double y1) {
        try {
            MemorySegment result = (MemorySegment) cairo_pattern_create_linear.invoke(x0, y0, x1, y1);
            LinearGradient pattern = new LinearGradient(result);
            MemoryCleaner.takeOwnership(pattern.handle());
            return pattern;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_create_linear = Interop.downcallHandle(
            "cairo_pattern_create_linear", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE,
                    ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Gets the gradient endpoints for a linear gradient.
     * 
     * @return a 2-element {@link Point} array with the x and y coordinates of the
     *         first and second point
     * @since 1.4
     */
    public Point[] getLinearPoints() {
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment ptrs = arena.allocateArray(ValueLayout.JAVA_DOUBLE, 4);
                long size = ValueLayout.JAVA_DOUBLE.byteSize();
                cairo_pattern_get_linear_points.invoke(handle(), ptrs, ptrs.asSlice(size), ptrs.asSlice(2 * size),
                        ptrs.asSlice(3 * size));
                double[] values = ptrs.toArray(ValueLayout.JAVA_DOUBLE);
                return new Point[] { new Point(values[0], values[1]), new Point(values[2], values[3]) };
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_get_linear_points = Interop.downcallHandle(
            "cairo_pattern_get_linear_points", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

}
