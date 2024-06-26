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

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

/**
 * PathData is used to represent the path data inside a {@link Path}.
 * <p>
 * The data structure is designed to try to balance the demands of efficiency
 * and ease-of-use. A path is represented as an array of {@link PathData}, which
 * is a union of headers and points.
 * <p>
 * Each portion of the path is represented by one or more elements in the array,
 * (one header followed by 0 or more points). The length value of the header is
 * the number of array elements for the current portion including the header,
 * (ie. length == 1 + # of points), and where the number of points for each
 * element type is as follows:
 * 
 * <ul>
 * <li>CAIRO_PATH_MOVE_TO: 1 point
 * <li>CAIRO_PATH_LINE_TO: 1 point
 * <li>CAIRO_PATH_CURVE_TO: 3 points
 * <li>CAIRO_PATH_CLOSE_PATH: 0 points
 * </ul>
 * 
 * The semantics and ordering of the coordinate values are consistent with
 * {@link Context#moveTo(double, double)},
 * {@link Context#lineTo(double, double)},
 * {@link Context#curveTo(double, double, double, double, double, double)}, and
 * {@link Context#closePath()}.
 * <p>
 * <strong>Note:</strong> PathData is not exposed as a public class. The user is
 * expected to use the {@link PathElement} instances produced by
 * {@link Path#iterator()}.
 * 
 * @see Path See the Path class documentation for sample code for iterating
 *      through a Path.
 * @since 1.0
 */
class PathData extends Proxy {

    /**
     * The memory layout of the native C struct
     * 
     * @return the memory layout of the native C struct
     */
    static MemoryLayout getMemoryLayout() {
        return MemoryLayout.unionLayout(
                    MemoryLayout.structLayout(
                        ValueLayout.JAVA_INT.withName("type"),
                        ValueLayout.JAVA_INT.withName("length")
                    ).withName("header"),
                    MemoryLayout.structLayout(
                        ValueLayout.JAVA_DOUBLE.withName("x"),
                        ValueLayout.JAVA_DOUBLE.withName("y")
                    ).withName("point")
                ).withName("cairo_path_data_t");
    }
    
    private static MemoryLayout headerLayout = MemoryLayout.structLayout(
            ValueLayout.JAVA_INT.withName("type"),
            ValueLayout.JAVA_INT.withName("length")
        ).withName("header");
    
    private static MemoryLayout pointLayout = MemoryLayout.structLayout(
            ValueLayout.JAVA_DOUBLE.withName("x"),
            ValueLayout.JAVA_DOUBLE.withName("y")
        ).withName("point");

    private static final VarHandle TYPE = headerLayout.varHandle(MemoryLayout.PathElement.groupElement("type"));
    private static final VarHandle LENGTH = headerLayout.varHandle(MemoryLayout.PathElement.groupElement("length"));
    private static final VarHandle X = pointLayout.varHandle(MemoryLayout.PathElement.groupElement("x"));
    private static final VarHandle Y = pointLayout.varHandle(MemoryLayout.PathElement.groupElement("y"));

    /**
     * Read the type field of the header
     * 
     * @return the type
     */
    public PathDataType type() {
        int result = (int) TYPE.get(handle(), 0);
        return PathDataType.of(result);
    }

    /**
     * Read the length field of the header
     * 
     * @return the length
     */
    public int length() {
        return (int) LENGTH.get(handle(), 0);
    }

    /**
     * Read the x field of the point
     * 
     * @return the x value
     */
    public double x() {
        return (double) X.get(handle(), 0);
    }

    /**
     * Read the y field of the point
     * 
     * @return the y value
     */
    public double y() {
        return (double) Y.get(handle(), 0);
    }

    /**
     * Constructor used internally to instantiate a java PathData object for a
     * native {@code cairo_path_data_t} instance
     * 
     * @param address the memory address of the native {@code cairo_path_data_t}
     *                instance
     */
    public PathData(MemorySegment address) {
        super(address);
    }
}
