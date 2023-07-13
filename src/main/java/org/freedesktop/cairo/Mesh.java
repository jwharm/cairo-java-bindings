package org.freedesktop.cairo;

import io.github.jwharm.javagi.interop.Interop;
import io.github.jwharm.javagi.interop.MemoryCleaner;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * A mesh pattern.
 */
public class Mesh extends Pattern {

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Constructor used internally to instantiate a java MeshPattern object for a
     * native {@code cairo_pattern_t} instance
     * 
     * @param address the memory address of the native {@code cairo_pattern_t}
     *                instance
     */
    public Mesh(MemorySegment address) {
        super(address);
    }

    /**
     * Create a new mesh pattern.
     * <p>
     * Mesh patterns are tensor-product patch meshes (type 7 shadings in PDF). Mesh
     * patterns may also be used to create other types of shadings that are special
     * cases of tensor-product patch meshes such as Coons patch meshes (type 6
     * shading in PDF) and Gouraud-shaded triangle meshes (type 4 and 5 shadings in
     * PDF).
     * <p>
     * Mesh patterns consist of one or more tensor-product patches, which should be
     * defined before using the mesh pattern. Using a mesh pattern with a partially
     * defined patch as source or mask will put the context in an error status with
     * a status of {@link Status#INVALID_MESH_CONSTRUCTION}.
     * <p>
     * A tensor-product patch is defined by 4 Bézier curves (side 0, 1, 2, 3) and by
     * 4 additional control points (P0, P1, P2, P3) that provide further control
     * over the patch and complete the definition of the tensor-product patch. The
     * corner C0 is the first point of the patch.
     * <p>
     * Degenerate sides are permitted so straight lines may be used. A zero length
     * line on one side may be used to create 3 sided patches.
     * 
     * <pre>
     *       C1     Side 1       C2
     *        +---------------+
     *        |               |
     *        |  P1       P2  |
     *        |               |
     * Side 0 |               | Side 2
     *        |               |
     *        |               |
     *        |  P0       P3  |
     *        |               |
     *        +---------------+
     *      C0     Side 3        C3
     * </pre>
     * 
     * Each patch is constructed by first calling {@link #beginPatch()}, then
     * {@link #moveTo(double, double)} to specify the first point in the patch (C0).
     * Then the sides are specified with calls to
     * {@link #curveTo(double, double, double, double, double, double)} and
     * {@link #lineTo(double, double)}
     * <p>
     * The four additional control points (P0, P1, P2, P3) in a patch can be
     * specified with {@link #setControlPoint(int, double, double)}.
     * <p>
     * At each corner of the patch (C0, C1, C2, C3) a color may be specified with
     * {@link #setCornerColorRGB(int, double, double, double)} or
     * {@link #setCornerColorRGBA(int, double, double, double, double)}. Any corner
     * whose color is not explicitly specified defaults to transparent black.
     * <p>
     * A Coons patch is a special case of the tensor-product patch where the control
     * points are implicitly defined by the sides of the patch. The default value
     * for any control point not specified is the implicit value for a Coons patch,
     * i.e. if no control points are specified the patch is a Coons patch.
     * <p>
     * A triangle is a special case of the tensor-product patch where the control
     * points are implicitly defined by the sides of the patch, all the sides are
     * lines and one of them has length 0, i.e. if the patch is specified using just
     * 3 lines, it is a triangle. If the corners connected by the 0-length side have
     * the same color, the patch is a Gouraud-shaded triangle.
     * <p>
     * Patches may be oriented differently to the above diagram. For example the
     * first point could be at the top left. The diagram only shows the relationship
     * between the sides, corners and control points. Regardless of where the first
     * point is located, when specifying colors, corner 0 will always be the first
     * point, corner 1 the point between side 0 and side 1 etc.
     * <p>
     * Calling {@link #endPatch()} completes the current patch. If less than 4 sides
     * have been defined, the first missing side is defined as a line from the
     * current point to the first point of the patch (C0) and the other sides are
     * degenerate lines from C0 to C0. The corners between the added sides will all
     * be coincident with C0 of the patch and their color will be set to be the same
     * as the color of C0.
     * <p>
     * Additional patches may be added with additional calls to
     * {@link #beginPatch()}/{@link #endPatch()}.
     *
     * <pre>
     * Mesh pattern = Mesh.createMesh();
     * 
     * // Add a Coons patch
     * pattern.beginPatch().moveTo(0, 0).curveTo(30, -30, 60, 30, 100, 0).curveTo(60, 30, 130, 60, 100, 100)
     *         .curveTo(60, 70, 30, 130, 0, 100).curveTo(30, 70, -30, 30, 0, 0).setCornerColorRGB(0, 1, 0, 0)
     *         .setCornerColorRGB(1, 0, 1, 0).setCornerColorRGB(2, 0, 0, 1).setCornerColorRGB(3, 1, 1, 0).endPatch();
     * 
     * // Add a Gouraud-shaded triangle
     * pattern.beginPatch().moveTo(100, 100).lineTo(130, 130).lineTo(130, 70).setCornerColorRGB(0, 1, 0, 0)
     *         .setCornerColorRGB(1, 0, 1, 0).setCornerColorRGB(2, 0, 0, 1).endPatch();
     * </pre>
     * 
     * When two patches overlap, the last one that has been added is drawn over the
     * first one.
     * <p>
     * When a patch folds over itself, points are sorted depending on their
     * parameter coordinates inside the patch. The v coordinate ranges from 0 to 1
     * when moving from side 3 to side 1; the u coordinate ranges from 0 to 1 when
     * going from side 0 to side 1. Points with higher v coordinate hide points with
     * lower v coordinate. When two points have the same v coordinate, the one with
     * higher u coordinate is above. This means that points nearer to side 1 are
     * above points nearer to side 3; when this is not sufficient to decide which
     * point is above (for example when both points belong to side 1 or side 3)
     * points nearer to side 2 are above points nearer to side 0.
     * <p>
     * For a complete definition of tensor-product patches, see the PDF
     * specification (ISO32000), which describes the parametrization in detail.
     * <p>
     * Note: The coordinates are always in pattern space. For a new pattern, pattern
     * space is identical to user space, but the relationship between the spaces can
     * be changed with {@link #setMatrix(Matrix)}.
     * 
     * @return the newly created {@link Mesh}
     * @since 1.12
     */
    public static Mesh create() {
        try {
            MemorySegment result = (MemorySegment) cairo_pattern_create_mesh.invoke();
            Mesh pattern = new Mesh(result);
            MemoryCleaner.takeOwnership(pattern.handle());
            return pattern;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_create_mesh = Interop.downcallHandle("cairo_pattern_create_mesh",
            FunctionDescriptor.of(ValueLayout.ADDRESS));

    /**
     * Begin a patch in a mesh pattern.
     * <p>
     * After calling this function, the patch shape should be defined with
     * {@link #moveTo(double, double)}, {@link #lineTo(double, double)} and
     * {@link #curveTo(double, double, double, double, double, double)}.
     * <p>
     * After defining the patch, {@link #endPatch()} must be called before using
     * pattern as a source or mask.
     * 
     * @return the mesh
     * @throws IllegalStateException If the pattern already has a current patch. See
     *                               {@link Status#INVALID_MESH_CONSTRUCTION}
     * @since 1.12
     */
    public Mesh beginPatch() throws IllegalStateException {
        try {
            cairo_mesh_pattern_begin_patch.invoke(handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status() == Status.INVALID_MESH_CONSTRUCTION) {
            throw new IllegalStateException(Status.INVALID_MESH_CONSTRUCTION.toString());
        }
        return this;
    }

    private static final MethodHandle cairo_mesh_pattern_begin_patch = Interop
            .downcallHandle("cairo_mesh_pattern_begin_patch", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

    /**
     * Indicates the end of the current patch in a mesh pattern.
     * <p>
     * If the current patch has less than 4 sides, it is closed with a straight line
     * from the current point to the first point of the patch as if
     * {@link #lineTo(double, double)} was used.
     * 
     * @return the mesh
     * @throws IllegalStateException If the pattern already has no current patch or
     *                               the current patch has no current point. See
     *                               {@link Status#INVALID_MESH_CONSTRUCTION}
     * @since 1.12
     */
    public Mesh endPatch() throws IllegalStateException {
        try {
            cairo_mesh_pattern_end_patch.invoke(handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status() == Status.INVALID_MESH_CONSTRUCTION) {
            throw new IllegalStateException(Status.INVALID_MESH_CONSTRUCTION.toString());
        }
        return this;
    }

    private static final MethodHandle cairo_mesh_pattern_end_patch = Interop
            .downcallHandle("cairo_mesh_pattern_end_patch", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

    /**
     * Define the first point of the current patch in a mesh pattern.
     * <p>
     * After this call the current point will be (x, y).
     * 
     * @param x the X coordinate of the new position
     * @param y the Y coordinate of the new position
     * @return the mesh
     * @throws IllegalStateException If the pattern has no current patch or the
     *                               current patch already has at least one side.
     *                               See {@link Status#INVALID_MESH_CONSTRUCTION}
     * @since 1.12
     */
    public Mesh moveTo(double x, double y) throws IllegalStateException {
        try {
            cairo_mesh_pattern_move_to.invoke(handle(), x, y);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status() == Status.INVALID_MESH_CONSTRUCTION) {
            throw new IllegalStateException(Status.INVALID_MESH_CONSTRUCTION.toString());
        }
        return this;
    }

    private static final MethodHandle cairo_mesh_pattern_move_to = Interop.downcallHandle("cairo_mesh_pattern_move_to",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Adds a line to the current patch from the current point to position (x, y) in
     * pattern-space coordinates.
     * <p>
     * If there is no current point before the call to {@code lineTo()}
     * this function will behave as {@code moveTo(x, y)}.
     * <p>
     * After this call the current point will be (x, y).
     * 
     * @param x the X coordinate of the end of the new line
     * @param y the X coordinate of the end of the new line
     * @return the mesh
     * @throws IllegalStateException If the pattern has no current patch or the
     *                               current patch already has 4 sides. See
     *                               {@link Status#INVALID_MESH_CONSTRUCTION}
     * @since 1.12
     */
    public Mesh lineTo(double x, double y) throws IllegalStateException {
        try {
            cairo_mesh_pattern_line_to.invoke(handle(), x, y);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status() == Status.INVALID_MESH_CONSTRUCTION) {
            throw new IllegalStateException(Status.INVALID_MESH_CONSTRUCTION.toString());
        }
        return this;
    }

    private static final MethodHandle cairo_mesh_pattern_line_to = Interop.downcallHandle("cairo_mesh_pattern_line_to",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Adds a cubic Bézier spline to the current patch from the current point to
     * position (x3, y3) in pattern-space coordinates, using (x1, y1) and (x2, y2)
     * as the control points.
     * <p>
     * If the current patch has no current point before the call to
     * {@code curveTo()}, this function will behave as if preceded by a call to
     * {@code moveTo(x, y)}.
     * <p>
     * After this call the current point will be (x3, y3).
     * 
     * @param x1 the X coordinate of the first control point
     * @param y1 the Y coordinate of the first control point
     * @param x2 the X coordinate of the second control point
     * @param y2 the Y coordinate of the second control point
     * @param x3 the X coordinate of the end of the curve
     * @param y3 the Y coordinate of the end of the curve
     * @return the mesh
     * @throws IllegalStateException If the pattern has no current patch or the
     *                               current patch already has 4 sides. See
     *                               {@link Status#INVALID_MESH_CONSTRUCTION}
     * @since 1.2
     */
    public Mesh curveTo(double x1, double y1, double x2, double y2, double x3, double y3) throws IllegalStateException {
        try {
            cairo_mesh_pattern_curve_to.invoke(handle(), x1, y1, x2, y2, x3, y3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status() == Status.INVALID_MESH_CONSTRUCTION) {
            throw new IllegalStateException(Status.INVALID_MESH_CONSTRUCTION.toString());
        }
        return this;
    }

    private static final MethodHandle cairo_mesh_pattern_curve_to = Interop.downcallHandle(
            "cairo_mesh_pattern_curve_to",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
                    ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Set an internal control point of the current patch.
     * <p>
     * Valid values for point_num are from 0 to 3 and identify the control points as
     * explained in {@link #create()}.
     * 
     * @param pointNum the control point to set the position for
     * @param x        the X coordinate of the control point
     * @param y        the Y coordinate of the control point
     * @return the mesh
     * @throws IllegalStateException If the pattern has no current patch. See
     *                               {@link Status#INVALID_MESH_CONSTRUCTION}
     * @since 1.12
     */
    public Mesh setControlPoint(int pointNum, double x, double y) throws IllegalStateException {
        try {
            cairo_mesh_pattern_set_control_point.invoke(handle(), pointNum, x, y);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status() == Status.INVALID_MESH_CONSTRUCTION) {
            throw new IllegalStateException(Status.INVALID_MESH_CONSTRUCTION.toString());
        }
        return this;
    }

    private static final MethodHandle cairo_mesh_pattern_set_control_point = Interop
            .downcallHandle("cairo_mesh_pattern_set_control_point", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Sets the color of a corner of the current patch in a mesh pattern.
     * <p>
     * The color is specified in the same way as in
     * {@link Context#setSourceRGB(double, double, double)}.
     * <p>
     * Valid values for corner_num are from 0 to 3 and identify the corners as
     * explained in {@link #create()}.
     * 
     * @param cornerNum the corner to set the color for
     * @param red       red component of color
     * @param green     green component of color
     * @param blue      blue component of color
     * @return the mesh
     * @throws IllegalStateException If the pattern has no current patch. See
     *                               {@link Status#INVALID_MESH_CONSTRUCTION}
     * @since 1.12
     */
    public Mesh setCornerColorRGB(int cornerNum, double red, double green, double blue) throws IllegalStateException {
        try {
            cairo_mesh_pattern_set_corner_color_rgb.invoke(handle(), cornerNum, red, green, blue);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status() == Status.INVALID_MESH_CONSTRUCTION) {
            throw new IllegalStateException(Status.INVALID_MESH_CONSTRUCTION.toString());
        }
        return this;
    }

    private static final MethodHandle cairo_mesh_pattern_set_corner_color_rgb = Interop.downcallHandle(
            "cairo_mesh_pattern_set_corner_color_rgb", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Sets the color of a corner of the current patch in a mesh pattern.
     * <p>
     * The color is specified in the same way as in
     * {@link Context#setSourceRGBA(double, double, double, double)}.
     * <p>
     * Valid values for {@code cornerNum} are from 0 to 3 and identify the corners
     * as explained in {@link #create()}.
     * 
     * @param cornerNum the corner to set the color for
     * @param red       red component of color
     * @param green     green component of color
     * @param blue      blue component of color
     * @param alpha     alpha component of color
     * @return the mesh
     * @throws IllegalStateException If the pattern has no current patch. See
     *                               {@link Status#INVALID_MESH_CONSTRUCTION}
     * @since 1.12
     */
    public Mesh setCornerColorRGBA(int cornerNum, double red, double green, double blue, double alpha)
            throws IllegalStateException {
        try {
            cairo_mesh_pattern_set_corner_color_rgba.invoke(handle(), cornerNum, red, green, blue, alpha);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status() == Status.INVALID_MESH_CONSTRUCTION) {
            throw new IllegalStateException(Status.INVALID_MESH_CONSTRUCTION.toString());
        }
        return this;
    }

    private static final MethodHandle cairo_mesh_pattern_set_corner_color_rgba = Interop.downcallHandle(
            "cairo_mesh_pattern_set_corner_color_rgba",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE,
                    ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Gets the number of patches specified in the given mesh pattern.
     * <p>
     * The number only includes patches which have been finished by calling
     * {@link #endPatch()}. For example it will be 0 during the definition of the
     * first patch.
     * 
     * @return the number patches
     * @since 1.12
     */
    public int getPatchCount() {
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment count = arena.allocate(ValueLayout.JAVA_INT);
                cairo_mesh_pattern_get_patch_count.invoke(handle(), count);
                return count.get(ValueLayout.JAVA_INT, 0);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_mesh_pattern_get_patch_count = Interop.downcallHandle(
            "cairo_mesh_pattern_get_patch_count",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Gets path defining the patch {@code patchNum} for a mesh pattern.
     * <p>
     * {@code patchNum} can range from 0 to n-1 where n is the number returned by
     * {@link #getPatchCount()}.
     * 
     * @param patchNum the patch number to return data for
     * @return the path defining the patch, or a path with status
     *         CAIRO_STATUS_INVALID_INDEX if patchNum or pointNum is not valid for
     *         the pattern
     * @throws IndexOutOfBoundsException if patchNum is not valid for the pattern.
     *                                   {@link Status#INVALID_INDEX}.
     * @since 1.12
     */
    public Path getPath(int patchNum) throws IndexOutOfBoundsException {
        try {
            MemorySegment result = (MemorySegment) cairo_mesh_pattern_get_path.invoke(handle(), patchNum);
            Path path = new Path(result);
            MemoryCleaner.takeOwnership(path.handle());
            if (path.status() == Status.INVALID_INDEX) {
                throw new IllegalStateException(Status.INVALID_INDEX.toString());
            }
            return path;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_mesh_pattern_get_path = Interop.downcallHandle(
            "cairo_mesh_pattern_get_path",
            FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded(), ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Gets the control point {@code pointNum} of patch {@code patchNum} for a mesh
     * pattern.
     * <p>
     * {@code patchNum} can range from 0 to n-1 where n is the number returned by
     * {@link #getPatchCount()}.
     * <p>
     * Valid values for {@code pointNum} are from 0 to 3 and identify the control
     * points as explained in {@link #create()}.
     * 
     * @param patchNum the patch number to return data for
     * @param pointNum the control point number to return data for
     * @return a {@link Point} object with the x and y coordinates of the control
     *         point
     * @throws IndexOutOfBoundsException if {@code patchNum} or {@code pointNum} is
     *                                   not valid for the pattern
     * @since 1.12
     */
    public Point getControlPoint(int patchNum, int pointNum) throws IndexOutOfBoundsException {
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment xPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
                MemorySegment yPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
                int result = (int) cairo_mesh_pattern_get_control_point.invoke(handle(), patchNum, pointNum, xPtr,
                        yPtr);
                Status status = Status.of(result);
                if (status == Status.INVALID_INDEX) {
                    throw new IndexOutOfBoundsException(Status.INVALID_INDEX.toString());
                }
                return new Point(xPtr.get(ValueLayout.JAVA_DOUBLE, 0), yPtr.get(ValueLayout.JAVA_DOUBLE, 0));
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_mesh_pattern_get_control_point = Interop.downcallHandle(
            "cairo_mesh_pattern_get_control_point", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Gets the color information in corner {@code cornerNum} of patch
     * {@code patchNum} for a mesh pattern.
     * <p>
     * {@code patchNum} can range from 0 to n-1 where n is the number returned by
     * {@link #getPatchCount()}.
     * <p>
     * Valid values for {@code cornerNum} are from 0 to 3 and identify the corners
     * as explained in {@link #create()}.
     * 
     * @param patchNum  the patch number to return data for
     * @param cornerNum the corner number to return data for
     * @return a 4-element array with red, green, blue and alpha color components
     * @throws IndexOutOfBoundsException if {@code patchNum} or {@code pointNum} is
     *                                   not valid for the pattern
     * @since 1.12
     */
    public double[] getCornerColorRGBA(int patchNum, int cornerNum) throws IndexOutOfBoundsException {
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment redPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
                MemorySegment greenPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
                MemorySegment bluePtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
                MemorySegment alphaPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
                int result = (int) cairo_mesh_pattern_get_corner_color_rgba.invoke(handle(), patchNum, cornerNum,
                        redPtr, greenPtr, bluePtr, alphaPtr);
                Status status = Status.of(result);
                if (status == Status.INVALID_INDEX) {
                    throw new IndexOutOfBoundsException(Status.INVALID_INDEX.toString());
                }
                return new double[] { redPtr.get(ValueLayout.JAVA_DOUBLE, 0), greenPtr.get(ValueLayout.JAVA_DOUBLE, 0),
                        bluePtr.get(ValueLayout.JAVA_DOUBLE, 0), alphaPtr.get(ValueLayout.JAVA_DOUBLE, 0) };
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_mesh_pattern_get_corner_color_rgba = Interop
            .downcallHandle("cairo_mesh_pattern_get_corner_color_rgba",
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                            ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                            ValueLayout.ADDRESS));
}
