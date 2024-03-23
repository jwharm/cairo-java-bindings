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
import io.github.jwharm.cairobindings.Interop;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;

/**
 * Generic matrix operations.
 * <p>
 * Matrix is used throughout cairo to convert between different coordinate
 * spaces. A Matrix holds an affine transformation, such as a scale, rotation,
 * shear, or a combination of these. The transformation of a point (x,y) is
 * given by:
 * 
 * <pre>
 * xNew = xx * x + xy * y + x0;
 * yNew = yx * x + yy * y + y0;
 * </pre>
 * 
 * The current transformation matrix of a {@link Context}, represented as a
 * Matrix, defines the transformation from user-space coordinates to
 * device-space coordinates. See {@link Context#getMatrix} and
 * {@link Context#setMatrix(Matrix)}.
 * 
 * @since 1.0
 */
public class Matrix extends Proxy {

    static {
        Cairo.ensureInitialized();
    }

    static MemoryLayout getMemoryLayout() {
        return MemoryLayout.structLayout(
                ValueLayout.JAVA_DOUBLE.withName("xx"),
                ValueLayout.JAVA_DOUBLE.withName("yx"),
                ValueLayout.JAVA_DOUBLE.withName("xy"),
                ValueLayout.JAVA_DOUBLE.withName("yy"),
                ValueLayout.JAVA_DOUBLE.withName("x0"),
                ValueLayout.JAVA_DOUBLE.withName("y0"))
            .withName("cairo_matrix_t");
    }

    private static final VarHandle XX = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("xx"));
    private static final VarHandle YX = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("yx"));
    private static final VarHandle XY = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("xy"));
    private static final VarHandle YY = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("yy"));
    private static final VarHandle X0 = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("x0"));
    private static final VarHandle Y0 = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("y0"));

    /**
     * Get the xx value of the Matrix
     *
     * @return the xx value
     */
    public double xx() {
        return (double) XX.get(handle());
    }

    /**
     * Get the yx value of the Matrix
     *
     * @return the yx value
     */
    public double yx() {
        return (double) YX.get(handle());
    }

    /**
     * Get the xy value of the Matrix
     *
     * @return the xy value
     */
    public double xy() {
        return (double) XY.get(handle());
    }

    /**
     * Get the yy value of the Matrix
     *
     * @return the yy value
     */
    public double yy() {
        return (double) YY.get(handle());
    }

    /**
     * Get the x0 value of the Matrix
     *
     * @return the x0 value
     */
    public double x0() {
        return (double) X0.get(handle());
    }

    /**
     * Get the y0 value of the Matrix
     *
     * @return the y0 value
     */
    public double y0() {
        return (double) Y0.get(handle());
    }

    /**
     * Allocate a new, uninitialized {@code caio_matrix_t}
     *
     * @param arena the arena in which the Matrix will be allocated
     * @return the newly allocated, uninitialized Matrix object
     */
    public static Matrix create(Arena arena) {
        return new Matrix(arena.allocate(getMemoryLayout()));
    }

    /**
     * Sets the matrix to be the affine transformation given by {@code xx},
     * {@code yx}, {@code xy}, {@code yy}, {@code x0}, {@code y0}. The
     * transformation is given by:
     * 
     * <pre>
     * xNew = xx * x + xy * y + x0;
     * yNew = yx * x + yy * y + y0;
     * </pre>
     * 
     * @param xx xx component of the affine transformation
     * @param yx yx component of the affine transformation
     * @param xy xy component of the affine transformation
     * @param yy yy component of the affine transformation
     * @param x0 X translation component of the affine transformation
     * @param y0 Y translation component of the affine transformation
     * @return the resulting Matrix
     * @since 1.0
     */
    public Matrix init(double xx, double yx, double xy, double yy, double x0, double y0) {
        try {
            cairo_matrix_init.invoke(handle(), xx, yx, xy, yy, x0, y0);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_matrix_init = Interop.downcallHandle("cairo_matrix_init",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
                    ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Modifies the matrix to be an identity transformation.
     *
     * @return the matrix
     * @since 1.0
     */
    public Matrix initIdentity() {
        try {
            cairo_matrix_init_identity.invoke(handle());
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_matrix_init_identity = Interop.downcallHandle("cairo_matrix_init_identity",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

    /**
     * Initializes the matrix to a transformation that translates by {@code tx} and
     * {@code ty} in the X and Y dimensions, respectively.
     * 
     * @param tx amount to translate in the X direction
     * @param ty amount to translate in the Y direction
     * @return the matrix
     * @since 1.0
     */
    public Matrix initTranslate(double tx, double ty) {
        try {
            cairo_matrix_init_translate.invoke(handle(), tx, ty);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_matrix_init_translate = Interop.downcallHandle(
            "cairo_matrix_init_translate",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Applies a translation by {@code tx, ty} to the transformation in the matrix.
     * The effect of the new transformation is to first translate the coordinates by
     * {@code tx} and {@code ty}, then apply the original transformation to the
     * coordinates.
     * 
     * @param tx amount to translate in the X direction
     * @param ty amount to translate in the Y direction
     * @since 1.0
     */
    public void translate(double tx, double ty) {
        try {
            cairo_matrix_translate.invoke(handle(), tx, ty);
            /*
             * No `return this;` (to allow method chaining), because that would hide the
             * fact that this is a destructive operation
             */
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_matrix_translate = Interop.downcallHandle("cairo_matrix_translate",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Applies scaling by {@code sx, sy} to the transformation in the matrix. The
     * effect of the new transformation is to first scale the coordinates by
     * {@code sx} and {@code sy}, then apply the original transformation to the
     * coordinates.
     * 
     * @param sx scale factor in the X direction
     * @param sy scale factor in the X direction
     * @since 1.0
     */
    public void scale(double sx, double sy) {
        try {
            cairo_matrix_scale.invoke(handle(), sx, sy);
            /*
             * No `return this;` (to allow method chaining), because that would hide the
             * fact that this is a destructive operation
             */
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_matrix_scale = Interop.downcallHandle("cairo_matrix_scale",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Applies rotation by {@code radians} to the transformation in the matrix. The
     * effect of the new transformation is to first rotate the coordinates by
     * {@code radians}, then apply the original transformation to the coordinates.
     * 
     * @param radians angle of rotation, in radians. The direction of rotation is
     *                defined such that positive angles rotate in the direction from
     *                the positive X axis toward the positive Y axis. With the
     *                default axis orientation of cairo, positive angles rotate in a
     *                clockwise direction.
     * @since 1.0
     */
    public void rotate(double radians) {
        try {
            cairo_matrix_rotate.invoke(handle(), radians);
            /*
             * No `return this;` (to allow method chaining), because that would hide the
             * fact that this is a destructive operation
             */
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_matrix_rotate = Interop.downcallHandle("cairo_matrix_rotate",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE));

    /**
     * Changes the matrix to be the inverse of its original value. Not all
     * transformation matrices have inverses; if the matrix collapses points
     * together (it is <i>degenerate</i>), then it has no inverse and this function
     * will fail.
     * 
     * @throws IllegalArgumentException if the matrix has no inverse. See
     *                                  {@link Status#INVALID_MATRIX}.
     */
    public void invert() throws IllegalArgumentException {
        try {
            int result = (int) cairo_matrix_invert.invoke(handle());
            Status status = Status.of(result);
            if (status == Status.INVALID_MATRIX) {
                throw new IllegalArgumentException(status.toString());
            }
            /*
             * No `return this;` (to allow method chaining), because that would hide the
             * fact that this is a destructive operation
             */
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_matrix_invert = Interop.downcallHandle("cairo_matrix_invert",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Multiplies the affine transformations in this matrix and the provided matrix
     * together and stores the result in the resulting matrix. The effect of the
     * resulting transformation is to first apply the transformation in {@code a}
     * to the coordinates and then apply the transformation in {@code b} to the
     * coordinates.
     * <p>
     * It is allowable for the resulting matrix to be identical to either {@code a}
     * or {@code b}.
     * 
     * @param  a a matrix
     * @param  b a matrix
     * @since 1.0
     */
    public void multiply(Matrix a, Matrix b) {
        try {
            cairo_matrix_multiply.invoke(this.handle(),
                    a == null ? MemorySegment.NULL : a.handle(),
                    b == null ? MemorySegment.NULL : b.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_matrix_multiply = Interop.downcallHandle("cairo_matrix_multiply",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Transforms the distance vector {@code (dx, dy)} by the matrix. This is
     * similar to {@link #transformPoint(Point)} except that the translation
     * components of the transformation are ignored. The calculation of the returned
     * vector is as follows:
     * 
     * <pre>
     * dx2 = dx1 * a + dy1 * c;
     * dy2 = dx1 * b + dy1 * d;
     * </pre>
     *
     * @param distanceVector X and Y component of a distance vector
     * @return the transformed X and Y component
     * @since 1.0
     */
    public Point transformDistance(Point distanceVector) {
        if (distanceVector == null) {
            return null;
        }
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment dxPtr = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, distanceVector.x());
                MemorySegment dyPtr = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, distanceVector.y());
                cairo_matrix_transform_distance.invoke(handle(), dxPtr, dyPtr);
                return new Point(dxPtr.get(ValueLayout.JAVA_DOUBLE, 0), dyPtr.get(ValueLayout.JAVA_DOUBLE, 0));
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_matrix_transform_distance = Interop.downcallHandle(
            "cairo_matrix_transform_distance",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Transforms the point {@code (x, y)} by the matrix.
     * 
     * @param point X and Y position
     * @return the transformed X and Y position
     * @since 1.0
     */
    public Point transformPoint(Point point) {
        if (point == null) {
            return null;
        }
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment dxPtr = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, point.x());
                MemorySegment dyPtr = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, point.y());
                cairo_matrix_transform_point.invoke(handle(), dxPtr, dyPtr);
                return new Point(dxPtr.get(ValueLayout.JAVA_DOUBLE, 0), dyPtr.get(ValueLayout.JAVA_DOUBLE, 0));
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_matrix_transform_point = Interop.downcallHandle(
            "cairo_matrix_transform_point",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Constructor used internally to instantiate a java Matrix object for a native
     * {@code cairo_matrix_t} instance
     * 
     * @param address the memory address of the native {@code cairo_matrix_t}
     *                instance
     */
    public Matrix(MemorySegment address) {
        super(address);
    }

    /**
     * String representation of this Matrix
     *
     * @return a String representation of this Matrix
     */
    @Override
    public String toString() {
        return String.format("Matrix address=%d xx=%f yx=%f xy=%f yy=%f x0=%f y0=%f",
                handle().address(), xx(), yx(), xy(), yy(), x0(), y0());
    }
}
