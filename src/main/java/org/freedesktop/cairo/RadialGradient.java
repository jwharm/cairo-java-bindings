package org.freedesktop.cairo;

import io.github.jwharm.javagi.interop.Interop;
import io.github.jwharm.javagi.interop.MemoryCleaner;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * A radial gradient pattern.
 */
public class RadialGradient extends Gradient {

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Constructor used internally to instantiate a java RadialPattern object for a
     * native {@code cairo_pattern_t} instance
     * 
     * @param address the memory address of the native {@code cairo_pattern_t}
     *                instance
     */
    public RadialGradient(MemorySegment address) {
        super(address);
    }

    /**
     * Creates a new radial gradient cairo_pattern_t between the two circles defined
     * by (cx0, cy0, radius0) and (cx1, cy1, radius1). Before using the gradient
     * pattern, a number of color stops should be defined using
     * cairo_pattern_add_color_stop_rgb() or cairo_pattern_add_color_stop_rgba().
     * <p>
     * Note: The coordinates here are in pattern space. For a new pattern, pattern
     * space is identical to user space, but the relationship between the spaces can
     * be changed with cairo_pattern_set_matrix().
     * 
     * @param cx0     x coordinate for the center of the start circle
     * @param cy0     y coordinate for the center of the start circle
     * @param radius0 radius of the start circle
     * @param cx1     x coordinate for the center of the end circle
     * @param cy1     y coordinate for the center of the end circle
     * @param radius1 radius of the end circle
     * @return the newly created {@link RadialGradient}
     * @since 1.0
     */
    public static RadialGradient create(double cx0, double cy0, double radius0, double cx1, double cy1,
            double radius1) {
        try {
            MemorySegment result = (MemorySegment) cairo_pattern_create_radial.invoke(cx0, cy0, radius0, cx1, cy1,
                    radius1);
            RadialGradient pattern = new RadialGradient(result);
            MemoryCleaner.takeOwnership(pattern.handle());
            return pattern;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_create_radial = Interop.downcallHandle(
            "cairo_pattern_create_radial",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
                    ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Gets the gradient endpoint circles for a radial gradient, each specified as a
     * center coordinate and a radius.
     * 
     * @return a 2-element {@link Circle} array with the x and y coordinates and
     *         radius of the first and second circle
     * @since 1.4
     */
    public Circle[] getRadialCircles() {
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment ptrs = arena.allocateArray(ValueLayout.JAVA_DOUBLE, 6);
                long size = ValueLayout.JAVA_DOUBLE.byteSize();
                cairo_pattern_get_radial_circles.invoke(handle(), ptrs, ptrs.asSlice(size), ptrs.asSlice(2 * size),
                        ptrs.asSlice(3 * size), ptrs.asSlice(4 * size), ptrs.asSlice(5 * size));
                double[] values = ptrs.toArray(ValueLayout.JAVA_DOUBLE);
                return new Circle[] { new Circle(values[0], values[1], values[2]),
                        new Circle(values[3], values[4], values[5]) };
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_get_radial_circles = Interop.downcallHandle(
            "cairo_pattern_get_radial_circles",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

}
