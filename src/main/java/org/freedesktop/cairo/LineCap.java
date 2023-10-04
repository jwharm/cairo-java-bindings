package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Interop;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Specifies how to render the endpoints of the path when stroking.
 * <p>
 * The default line cap style is CAIRO_LINE_CAP_BUTT.
 */
public enum LineCap {

    /**
     * start(stop) the line exactly at the start(end) point
     * 
     * @since 1.0
     */
    BUTT,

    /**
     * use a round ending, the center of the circle is the end point
     * 
     * @since 1.0
     */
    ROUND,

    /**
     * use a squared ending, the center of the square is the end point
     * 
     * @since 1.0
     */
    SQUARE;

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
    public static LineCap of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoLineCap GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_line_cap_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_line_cap_get_type = Interop.downcallHandle(
            "cairo_gobject_line_cap_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}