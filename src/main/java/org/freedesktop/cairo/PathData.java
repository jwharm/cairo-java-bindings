package org.freedesktop.cairo;

import io.github.jwharm.javagi.base.ProxyInstance;

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
class PathData extends ProxyInstance {

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

    private static final VarHandle TYPE = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("type"));
    private static final VarHandle LENGTH = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("length"));
    private static final VarHandle X = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("x"));
    private static final VarHandle Y = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("y"));

    /**
     * Read the type field of the header
     * 
     * @return the type
     */
    public PathDataType type() {
        int result = (int) TYPE.get(handle());
        return PathDataType.of(result);
    }

    /**
     * Read the length field of the header
     * 
     * @return the length
     */
    public int length() {
        return (int) LENGTH.get(handle());
    }

    /**
     * Read the x field of the point
     * 
     * @return the x value
     */
    public double x() {
        return (double) X.get(handle());
    }

    /**
     * Read the y field of the point
     * 
     * @return the y value
     */
    public double y() {
        return (double) Y.get(handle());
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
