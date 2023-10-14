package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Interop;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Filter is used to indicate what filtering should be applied when reading
 * pixel values from patterns. See {@link Pattern#setFilter} for indicating the
 * desired filter to be used with a particular pattern.
 *
 * @since 1.0
 */
public enum Filter {

    /**
     * A high-performance filter, with quality similar to {@link #NEAREST}
     * 
     * @since 1.0
     */
    FAST,

    /**
     * A reasonable-performance filter, with quality similar to {@link #BILINEAR}
     * 
     * @since 1.0
     */
    GOOD,

    /**
     * The highest-quality available, performance may not be suitable for
     * interactive use.
     * 
     * @since 1.0
     */
    BEST,

    /**
     * Nearest-neighbor filtering
     * 
     * @since 1.0
     */
    NEAREST,

    /**
     * Linear interpolation in two dimensions
     * 
     * @since 1.0
     */
    BILINEAR,

    /**
     * This filter value is currently unimplemented, and should not be used in
     * current code.
     * 
     * @since 1.0
     */
    GAUSSIAN;

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Return the value of this enum
     * @return the value
     */
    public int getValue() {
        return ordinal();
    }

    /**
     * Returns the enum constant for the given ordinal (its position in the enum
     * declaration).
     * 
     * @param ordinal the position in the enum declaration, starting from zero
     * @return the enum constant for the given ordinal
     */
    public static Filter of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoFilter GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_filter_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_filter_get_type = Interop.downcallHandle(
            "cairo_gobject_filter_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
