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

/**
 * The PathElement interface is a sealed type that models Path elements. It permits four records:
 * 
 * <ul>
 * <li>{@link PathElement.MoveTo} that corresponds with {@link Context#moveTo(double, double)}
 * <li>{@link PathElement.LineTo} that corresponds with {@link Context#lineTo(double, double)}
 * <li>{@link PathElement.CurveTo} that corresponds with {@link Context#curveTo(double, double, double, double, double, double)}
 * <li>{@link PathElement.ClosePath} that corresponds with {@link Context#closePath()}
 * </ul>
 * 
 * See {@link Path} for more information about working with paths.
 */
public sealed interface PathElement
        permits PathElement.MoveTo, PathElement.LineTo, PathElement.CurveTo, PathElement.ClosePath {

    /**
     * A {@link PathDataType#MOVE_TO} path element
     *
     * @param x the X coordinate of the new position
     * @param y the Y coordinate of the new position
     */
    record MoveTo(double x, double y) implements PathElement {
    }

    /**
     * A {@link PathDataType#LINE_TO} path element
     *
     * @param x the X coordinate of the end of the new line
     * @param y the Y coordinate of the end of the new line
     */
    record LineTo(double x, double y) implements PathElement {
    }

    /**
     * A {@link PathDataType#CURVE_TO} path element
     *
     * @param x1 the X coordinate of the first control point
     * @param y1 the Y coordinate of the first control point
     * @param x2 the X coordinate of the second control point
     * @param y2 the Y coordinate of the second control point
     * @param x3 the X coordinate of the end of the curve
     * @param y3 the Y coordinate of the end of the curve
     */
    record CurveTo(double x1, double y1, double x2, double y2, double x3, double y3) implements PathElement {
    }

    /**
     * A {@link PathDataType#CLOSE_PATH} path element
     */
    record ClosePath() implements PathElement {
    }
}
