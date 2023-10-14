package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Interop;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Specifies whether to hint font metrics; hinting font metrics means quantizing
 * them so that they are integer values in device space. Doing this improves the
 * consistency of letter and line spacing, however it also means that text will
 * be laid out differently at different zoom factors.
 * 
 * @since 1.0
 */
public enum HintMetrics {

    /**
     * Hint metrics in the default manner for the font backend and target device
     * 
     * @since 1.0
     */
    DEFAULT,

    /**
     * Do not hint font metrics
     * 
     * @since 1.0
     */
    OFF,

    /**
     * Hint font metrics
     * 
     * @since 1.0
     */
    ON;

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
    public static HintMetrics of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoHintMetrics GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_hint_metrics_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_hint_metrics_get_type = Interop.downcallHandle(
            "cairo_gobject_hint_metrics_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
