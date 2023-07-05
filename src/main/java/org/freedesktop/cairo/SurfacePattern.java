package org.freedesktop.cairo;

import io.github.jwharm.javagi.interop.Interop;
import io.github.jwharm.javagi.interop.MemoryCleaner;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * A pattern based on a surface (an image).
 */
public class SurfacePattern extends Pattern {

    static {
        Cairo.ensureInitialized();
    }

    // Keep a reference to the target surface during the lifetime of this pattern
    Surface surface;

    /**
     * Constructor used internally to instantiate a java SurfacePattern object for a
     * native {@code cairo_pattern_t} instance
     * 
     * @param address the memory address of the native {@code cairo_pattern_t}
     *                instance
     */
    public SurfacePattern(MemorySegment address) {
        super(address);
    }

    /**
     * Create a new {@link Pattern} for the given surface.
     * 
     * @param surface the surface
     * @return the newly created {@link SurfacePattern}
     * @since 1.0
     */
    public static SurfacePattern create(Surface surface) {
        try {
            MemorySegment result = (MemorySegment) cairo_pattern_create_for_surface
                    .invoke(surface == null ? MemorySegment.NULL : surface.handle());
            SurfacePattern pattern = new SurfacePattern(result);
            MemoryCleaner.takeOwnership(pattern.handle());
            pattern.surface = surface;
            return pattern;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_create_for_surface = Interop.downcallHandle(
            "cairo_pattern_create_for_surface", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Gets the surface of a surface pattern.
     * 
     * @return surface of pattern
     * @since 1.4
     */
    public Surface getSurface() {
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment surfacePtr = arena.allocate(ValueLayout.ADDRESS);
                cairo_pattern_get_surface.invoke(handle(), surfacePtr);
                return new Surface(surfacePtr.get(ValueLayout.ADDRESS, 0));
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_get_surface = Interop.downcallHandle("cairo_pattern_get_surface",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

}
