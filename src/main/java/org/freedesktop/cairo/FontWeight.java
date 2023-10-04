package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Interop;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Specifies variants of a font face based on their weight.
 * 
 * @since 1.0
 */
public enum FontWeight {

    /**
     * Normal font weight
     * 
     * @since 1.0
     */
    NORMAL,

    /**
     * Bold font weight
     * 
     * @since 1.0
     */
    BOLD;

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Return the value of this enum
     * 
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
    public static FontWeight of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoFontWeight GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_font_weight_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_font_weight_get_type = Interop.downcallHandle(
            "cairo_gobject_font_weight_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
