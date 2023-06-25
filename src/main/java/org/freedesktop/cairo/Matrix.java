package org.freedesktop.cairo;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

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
 * device-space coordinates. See {@link Context#getMatrix()} and
 * {@link Context#setMatrix(Matrix)}.
 * 
 * @since 1.0
 */
public class Matrix extends Proxy {

    static {
        Interop.ensureInitialized();
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

    /**
     * Allocate a new {@code caio_matrix_t}
     */
    static Matrix create() {
        return new Matrix(SegmentAllocator.nativeAllocator(SegmentScope.auto()).allocate(getMemoryLayout()));
    }

    /**
     * Convenience method to use instead of {@code Matrix.create().init(...);}
     * <p>
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
     * @see Matrix#init(double, double, double, double, double, double)
     * @since 1.0
     */
    public static Matrix create(double xx, double yx, double xy, double yy, double x0, double y0) {
        try {
            Matrix matrix = create();
            cairo_matrix_init.invoke(matrix.handle(), xx, yx, xy, yy, x0, y0);
            return matrix;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
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
     * @return the resulting Matrix
     * @since 1.0
     */
    public static Matrix createIdentity() {
        try {
            Matrix matrix = create();
            cairo_matrix_init_identity.invoke(matrix.handle());
            return matrix;
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
     * @return the resulting Matrix
     * @since 1.0
     */
    public static Matrix createTranslate(double tx, double ty) {
        try {
            Matrix matrix = create();
            cairo_matrix_init_translate.invoke(matrix.handle(), tx, ty);
            return matrix;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_matrix_init_translate = Interop.downcallHandle(
            "cairo_matrix_init_translate",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Initializes the matrix to a transformation that scales by {@code sx} and
     * {@code sy} in the X and Y dimensions, respectively.
     * 
     * @param sx scale factor in the X direction
     * @param sy scale factor in the Y direction
     * @return the resulting Matrix
     * @since 1.0
     */
    public static Matrix createScale(double sx, double sy) {
        try {
            Matrix matrix = create();
            cairo_matrix_init_scale.invoke(matrix.handle(), sx, sy);
            return matrix;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_matrix_init_scale = Interop.downcallHandle("cairo_matrix_init_scale",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Initializes the matrix to a transformation that rotates by {@code radians}.
     * 
     * @param radians angle of rotation, in radians. The direction of rotation is
     *                defined such that positive angles rotate in the direction from
     *                the positive X axis toward the positive Y axis. With the
     *                default axis orientation of cairo, positive angles rotate in a
     *                clockwise direction.
     * @return the resulting Matrix
     * @since 1.0
     */
    public static Matrix createRotate(double radians) {
        try {
            Matrix matrix = create();
            cairo_matrix_init_rotate.invoke(matrix.handle(), radians);
            return matrix;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_matrix_init_rotate = Interop.downcallHandle("cairo_matrix_init_rotate",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE));

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
     * resulting transformation is to first apply the transformation in this matrix
     * to the coordinates and then apply the transformation in the provided matrix
     * to the coordinates.
     * <p>
     * It is allowable for the resulting matrix to be identical to either this
     * matrix or the provided matrix.
     * 
     * @param other a matrix
     * @return the resulting matrix
     * @since 1.0
     */
    public Matrix multiply(Matrix other) {
        try {
            Matrix result = create();
            cairo_matrix_multiply.invoke(result.handle(), this.handle(), other == null ? MemorySegment.NULL : other.handle());
            return result;
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
     * Affine transformations are position invariant, so the same vector always
     * transforms to the same vector. If {@code (x1, y1)} transforms to
     * {@code (x2, y2)} then {@code (x1 + dx1, y1 + dy1)} will transform to
     * {@code (x1 + dx2, y1 + dy2)} for all values of {@code x1} and {@code x2}.
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
            try (Arena arena = Arena.openConfined()) {
                MemorySegment dxPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
                MemorySegment dyPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
                dxPtr.set(ValueLayout.JAVA_DOUBLE, 0, distanceVector.x());
                dyPtr.set(ValueLayout.JAVA_DOUBLE, 0, distanceVector.y());
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
            try (Arena arena = Arena.openConfined()) {
                MemorySegment dxPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
                MemorySegment dyPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
                dxPtr.set(ValueLayout.JAVA_DOUBLE, 0, point.x());
                dyPtr.set(ValueLayout.JAVA_DOUBLE, 0, point.y());
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
}
