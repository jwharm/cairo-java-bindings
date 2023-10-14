package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Interop;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Specifies how to render the junction of two lines when stroking.
 * <p>
 * The default line join style is CAIRO_LINE_JOIN_MITER.
 */
public enum LineJoin {

    /**
     * use a sharp (angled) corner, see cairo_set_miter_limit()
     * 
     * @since 1.0
     */
    MITER,

    /**
     * use a rounded join, the center of the circle is the joint point
     * 
     * @since 1.0
     */
    ROUND,

    /**
     * use a cut-off join, the join is cut off at half the line width from the joint
     * point
     * 
     * @since 1.0
     */
    BEVEL;

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
    public static LineJoin of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoLineJoin GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_line_join_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_line_join_get_type = Interop.downcallHandle(
            "cairo_gobject_line_join_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}