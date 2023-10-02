package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.MemoryCleaner;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * A pattern with a solid (uniform) color. It may be opaque or translucent.
 */
public class SolidPattern extends Pattern {

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Constructor used internally to instantiate a java SolidPattern object for a
     * native {@code cairo_pattern_t} instance
     * 
     * @param address the memory address of the native {@code cairo_pattern_t}
     *                instance
     */
    public SolidPattern(MemorySegment address) {
        super(address);
    }

    /**
     * Creates a new {@link Pattern} corresponding to an opaque color. The color
     * components are floating point numbers in the range 0 to 1. If the values
     * passed in are outside that range, they will be clamped.
     * 
     * @param red   red component of the color
     * @param green green component of the color
     * @param blue  blue component of the color
     * @return the newly created {@link SolidPattern}
     * @since 1.0
     */
    public static SolidPattern createRGB(double red, double green, double blue) {
        try {
            MemorySegment result = (MemorySegment) cairo_pattern_create_rgb.invoke(red, green, blue);
            SolidPattern pattern = new SolidPattern(result);
            MemoryCleaner.takeOwnership(pattern.handle());
            return pattern;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_create_rgb = Interop.downcallHandle("cairo_pattern_create_rgb",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
                    ValueLayout.JAVA_DOUBLE));

    /**
     * Creates a new {@link Pattern} corresponding to a translucent color. The color
     * components are floating point numbers in the range 0 to 1. If the values
     * passed in are outside that range, they will be clamped.
     * <p>
     * The color is specified in the same way as in {@link Context#setSourceRGB(double, double, double)}.
     * 
     * @param red   red component of the color
     * @param green green component of the color
     * @param blue  blue component of the color
     * @param alpha alpha component of the color
     * @return the newly created {@link SolidPattern}
     * @since 1.0
     */
    public static SolidPattern createRGBA(double red, double green, double blue, double alpha) {
        try {
            MemorySegment result = (MemorySegment) cairo_pattern_create_rgba.invoke(red, green, blue, alpha);
            SolidPattern pattern = new SolidPattern(result);
            MemoryCleaner.takeOwnership(pattern.handle());
            return pattern;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_create_rgba = Interop.downcallHandle("cairo_pattern_create_rgba",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
                    ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Gets the solid color for a solid color pattern.
     * <p>
     * Note that the color and alpha values are not premultiplied.
     * 
     * @return a 4-element array with red, green, blue and alpha color components
     * @since 1.4
     */
    public double[] getRGBA() {
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment ptrs = arena.allocateArray(ValueLayout.JAVA_DOUBLE, 4);
                long size = ValueLayout.JAVA_DOUBLE.byteSize();
                cairo_pattern_get_rgba.invoke(handle(), ptrs, ptrs.asSlice(size), ptrs.asSlice(2 * size),
                        ptrs.asSlice(3 * size));
                double[] values = ptrs.toArray(ValueLayout.JAVA_DOUBLE);
                return new double[] { values[0], values[1], values[2], values[3] };
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_get_rgba = Interop.downcallHandle("cairo_pattern_get_rgba",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS));

}
