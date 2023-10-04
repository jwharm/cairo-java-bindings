package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Interop;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Used as the return value for cairo_region_contains_rectangle().
 * 
 * @since 1.10
 */
public enum RegionOverlap {

    /**
     * The contents are entirely inside the region.
     * 
     * @since 1.10
     */
    IN,

    /**
     * The contents are entirely outside the region.
     * 
     * @since 1.10
     */
    OUT,

    /**
     * The contents are partially inside and partially outside the region.
     * 
     * @since 1.10
     */
    PART;

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
    public static RegionOverlap of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoRegionOverlap GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_region_overlap_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_region_overlap_get_type = Interop.downcallHandle(
            "cairo_gobject_region_overlap_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
