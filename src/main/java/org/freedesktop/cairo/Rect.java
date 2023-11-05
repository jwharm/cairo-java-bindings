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
 * A rectangle.
 * <p>
 * This is one of several helper classes in Java (see also {@link RGBA} and
 * {@link Point}), that do not exist in the native cairo API. The difference between
 * {@code Rect} and {@link Rectangle} is that the latter class is part of the native
 * cairo API and stores its values in native memory, while {@code Rect} instances
 * only exist in the JVM.
 *
 * @param x      X coordinate of the left side of the rectangle
 * @param y      Y coordinate of the top side of the rectangle
 * @param width  width of the rectangle
 * @param height height of the rectangle
 * @since 1.18.1
 */
public record Rect(double x, double y, double width, double height) {
}
