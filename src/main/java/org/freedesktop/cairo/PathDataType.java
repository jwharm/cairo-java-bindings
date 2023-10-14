package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Interop;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Describes the type of one portion of a path when represented as a
 * {@link Path}. See {@link PathData} for details.
 * 
 * @since 1.0
 */
public enum PathDataType {

    /**
     * A move-to operation
     * @since 1.0
     */
    MOVE_TO,

    /**
     * A line-to operation
     * @since 1.0
     */
    LINE_TO,

    /**
     * A curve-to operation
     * @since 1.0
     */
    CURVE_TO,

    /**
     * A close-path operation
     * @since 1.0
     */
    CLOSE_PATH;

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
    public static PathDataType of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoPathDataType GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_path_data_type_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_path_data_type_get_type = Interop.downcallHandle(
            "cairo_gobject_path_data_type_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
