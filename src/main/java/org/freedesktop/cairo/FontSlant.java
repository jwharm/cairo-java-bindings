package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Interop;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Specifies variants of a font face based on their slant.
 * 
 * @since 1.0
 */
public enum FontSlant {

    /**
     * Upright font style
     * 
     * @since 1.0
     */
    NORMAL,

    /**
     * Italic font style
     * 
     * @since 1.0
     */
    ITALIC,

    /**
     * Oblique font style
     * 
     * @since 1.0
     */
    OBLIQUE;

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
    public static FontSlant of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoFontSlant GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_font_slant_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_font_slant_get_type = Interop.downcallHandle(
            "cairo_gobject_font_slant_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
