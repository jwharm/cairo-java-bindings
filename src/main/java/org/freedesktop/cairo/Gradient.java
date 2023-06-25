package org.freedesktop.cairo;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Base class (linear or radial) gradient patterns.
 */
public abstract class Gradient extends Pattern {

    static {
        Interop.ensureInitialized();
    }

    /**
     * Constructor used internally to instantiate a java GradientPattern object for
     * a native {@code cairo_pattern_t} instance
     * 
     * @param address the memory address of the native {@code cairo_pattern_t}
     *                instance
     */
    public Gradient(MemorySegment address) {
        super(address);
    }

    /**
     * Adds an opaque color stop to a gradient pattern. The offset specifies the
     * location along the gradient's control vector. For example, a linear
     * gradient's control vector is from (x0,y0) to (x1,y1) while a radial
     * gradient's control vector is from any point on the start circle to the
     * corresponding point on the end circle.
     * <p>
     * The color is specified in the same way as in
     * {@link Context#setSourceRGB(double, double, double)}.
     * <p>
     * If two (or more) stops are specified with identical offset values, they will
     * be sorted according to the order in which the stops are added, (stops added
     * earlier will compare less than stops added later). This can be useful for
     * reliably making sharp color transitions instead of the typical blend.
     * 
     * @param offset an offset in the range [0.0 .. 1.0]
     * @param red    red component of color
     * @param green  green component of color
     * @param blue   blue component of color
     * @since 1.0
     */
    public void addColorStopRGB(double offset, double red, double green, double blue) {
        try {
            cairo_pattern_add_color_stop_rgb.invoke(handle(), offset, red, green, blue);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_add_color_stop_rgb = Interop.downcallHandle(
            "cairo_pattern_add_color_stop_rgb", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE,
                    ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Adds a translucent color stop to a gradient pattern. The offset specifies the
     * location along the gradient's control vector. For example, a linear
     * gradient's control vector is from (x0,y0) to (x1,y1) while a radial
     * gradient's control vector is from any point on the start circle to the
     * corresponding point on the end circle.
     * <p>
     * The color is specified in the same way as in
     * {@link Context#setSourceRGBA(double, double, double, double)}.
     * <p>
     * If two (or more) stops are specified with identical offset values, they will
     * be sorted according to the order in which the stops are added, (stops added
     * earlier will compare less than stops added later). This can be useful for
     * reliably making sharp color transitions instead of the typical blend.
     * 
     * @param offset an offset in the range [0.0 .. 1.0]
     * @param red    red component of color
     * @param green  green component of color
     * @param blue   blue component of color
     * @param alpha  alpha component of color
     * @since 1.0
     */
    public void addColorStopRGBA(double offset, double red, double green, double blue, double alpha) {
        try {
            cairo_pattern_add_color_stop_rgba.invoke(handle(), offset, red, green, blue, alpha);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_add_color_stop_rgba = Interop.downcallHandle(
            "cairo_pattern_add_color_stop_rgba", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE,
                    ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Gets the number of color stops specified in the given gradient pattern.
     * 
     * @return the number of color stops
     * @since 1.4
     */
    public int getColorStopCount() {
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment countPtr = arena.allocate(ValueLayout.JAVA_INT);
                cairo_pattern_get_color_stop_count.invoke(handle(), countPtr);
                return countPtr.get(ValueLayout.JAVA_INT, 0);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_get_color_stop_count = Interop.downcallHandle(
            "cairo_pattern_get_color_stop_count",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Gets the color and offset information at the given {@code index} for a
     * gradient pattern. Values of {@code index} range from 0 to n-1 where n is the
     * number returned by {@link #getColorStopCount()}.
     * 
     * @param index index of the stop to return data for
     * @return a 5-element array with the offset and red, green, blue and alpha
     *         color components
     * @throws IndexOutOfBoundsException if index is not valid for the given pattern
     *                                   (see {@link Status#INVALID_INDEX}).
     * @since 1.4
     */
    public double[] getColorStopRGBA(int index) throws IndexOutOfBoundsException {
        double[] values;
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment ptrs = arena.allocateArray(ValueLayout.JAVA_DOUBLE, 5);
                long size = ValueLayout.JAVA_DOUBLE.byteSize();
                int result = (int) cairo_pattern_get_color_stop_rgba.invoke(handle(), index, ptrs, ptrs.asSlice(size),
                        ptrs.asSlice(2 * size), ptrs.asSlice(3 * size), ptrs.asSlice(4 * size));
                if (result == Status.INVALID_INDEX.value()) {
                    throw new IndexOutOfBoundsException(Status.INVALID_INDEX.toString());
                }
                values = ptrs.toArray(ValueLayout.JAVA_DOUBLE);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status() == Status.INVALID_INDEX) {
            throw new IllegalStateException(Status.INVALID_INDEX.toString());
        }
        return values;
    }

    private static final MethodHandle cairo_pattern_get_color_stop_rgba = Interop
            .downcallHandle("cairo_pattern_get_color_stop_rgba",
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                            ValueLayout.ADDRESS));

}
