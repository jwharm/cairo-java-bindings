package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Interop;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * cairo_content_t is used to describe the content that a surface will contain,
 * whether color information, alpha information (translucence vs. opacity), or
 * both.
 * <p>
 * Note: The large values here are designed to keep cairo_content_t values
 * distinct from cairo_format_t values so that the implementation can detect the
 * error if users confuse the two types.
 * 
 * @since 1.0
 */
public enum Content {

    /**
     * The surface will hold color content only.
     * 
     * @since 1.0
     */
    COLOR(0x1000),

    /**
     * The surface will hold alpha content only.
     * 
     * @since 1.0
     */
    ALPHA(0x2000),

    /**
     * The surface will hold color and alpha content.
     * 
     * @since 1.0
     */
    COLOR_ALPHA(0x3000);

    static {
        Cairo.ensureInitialized();
    }

    private final int value;

    Content(int value) {
        this.value = value;
    }

    /**
     * Return the value of this enum
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the enum member for the given value.
     * 
     * @param value the value of the enum member
     * @return the enum member for the given value
     */
    public static Content of(int value) {
        if (value == 0x1000) {
            return COLOR;
        } else if (value == 0x2000) {
            return ALPHA;
        } else if (value == 0x3000) {
            return COLOR_ALPHA;
        } else {
            throw new IllegalArgumentException("No Content enum with value " + value);
        }
    }

    /**
     * Get the CairoContent GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_content_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_content_get_type = Interop.downcallHandle(
            "cairo_gobject_content_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}