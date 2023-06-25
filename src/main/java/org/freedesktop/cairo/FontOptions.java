package org.freedesktop.cairo;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * How a font should be rendered.
 * <p>
 * The font options specify how fonts should be rendered. Most of the time the
 * font options implied by a surface are just right and do not need any changes,
 * but for pixel-based targets tweaking font options may result in superior
 * output on a particular display.
 * <p>
 * Individual features of a cairo_font_options_t can be set or accessed using
 * functions named {@code FontOptions.setFeatureName()} and
 * {@code FontOptions.getFeatureName()}, like {@link #setAntialias(Antialias)}
 * and {@link #getAntialias()}.
 * <p>
 * New features may be added to FontOptions in the future. For this reason,
 * {@link #copy()}, {@link #equals(Object)}, {@link #merge(FontOptions)} and
 * {@link #hashCode()} should be used to copy, check for equality, merge, or
 * compute a hash value of FontOptions objects.
 * 
 * @see ScaledFont
 * @since 1.0
 */
public class FontOptions extends Proxy {

    static {
        Interop.ensureInitialized();
    }

    /**
     * Constructor used internally to instantiate a java FontOptions object for a
     * native {@code cairo_font_options_t} instance
     * 
     * @param address the memory address of the native {@code cairo_font_options_t}
     *                instance
     */
    public FontOptions(MemorySegment address) {
        super(address);
        setDestroyFunc("cairo_font_options_destroy");
    }

    /**
     * Allocates a new font options object with all options initialized to default
     * values.
     * 
     * @return newly allocated FontOptions
     * @since 1.0
     */
    public static FontOptions create() {
        try {
            MemorySegment result = (MemorySegment) cairo_font_options_create.invoke();
            FontOptions options = new FontOptions(result);
            options.takeOwnership();
            return options;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_options_create = Interop.downcallHandle("cairo_font_options_create",
            FunctionDescriptor.of(ValueLayout.ADDRESS));

    /**
     * Allocates a new font options object copying the option values from the
     * original.
     * 
     * @return newly allocated FontOptions
     * @since 1.0
     */
    public FontOptions copy() {
        FontOptions copy;
        try {
            MemorySegment result = (MemorySegment) cairo_font_options_copy.invoke(handle());
            copy = new FontOptions(result);
            copy.takeOwnership();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (copy.status() == Status.NO_MEMORY) {
            throw new RuntimeException(copy.status().toString());
        }
        return copy;
    }

    private static final MethodHandle cairo_font_options_copy = Interop.downcallHandle("cairo_font_options_copy",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Checks whether an error has previously occurred for this font options object
     * 
     * @return {@link Status#SUCCESS} or {@link Status#NO_MEMORY}
     * @since 1.0
     */
    public Status status() {
        try {
            int result = (int) cairo_font_options_status.invoke(handle());
            return Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_options_status = Interop.downcallHandle("cairo_font_options_status",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Merges non-default options from other into this FontOptions, replacing
     * existing values. This operation can be thought of as somewhat similar to
     * compositing other onto options with the operation of {@link Operator#OVER}.
     * 
     * @param other another FontOptions
     * @since 1.0
     */
    public void merge(FontOptions other) {
        try {
            cairo_font_options_merge.invoke(handle(), other == null ? MemorySegment.NULL : other.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_options_merge = Interop.downcallHandle("cairo_font_options_merge",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Compute a hash for the font options object; this value will be useful when
     * storing an object containing FontOptions in a hash table.
     * 
     * @return the hash value for the font options object. The return value is cast
     *         to a 32-bit type.
     * @since 1.0
     */
    @Override
    public int hashCode() {
        try {
            // The return value in C is long, but must be marshaled to (JAVA_INT) on
            // Windows, because Windows long values are 32-bits.
            boolean OS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");
            if (OS_WINDOWS) {
                return (int) cairo_font_options_hash_win.invoke(handle());
            } else {
                // The (long) cast defines the return value of the
                // method invocation.
                return (int) (long) cairo_font_options_hash.invoke(handle());
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_options_hash = Interop.downcallHandle("cairo_font_options_hash",
            FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));

    private static final MethodHandle cairo_font_options_hash_win = Interop.downcallHandle("cairo_font_options_hash",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Compares two font options objects for equality.
     * 
     * @param other another FontOptions
     * @return {@code true} if all fields of the two font options objects match.
     *         Note that this function will return {@code false} if either object is
     *         in error.
     * @since 1.0
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof FontOptions options) {
            try {
                int result = (int) cairo_font_options_equal.invoke(handle(), options.handle());
                return result != 0;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            return false;
        }
    }

    private static final MethodHandle cairo_font_options_equal = Interop.downcallHandle("cairo_font_options_equal",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Sets the antialiasing mode for the font options object. This specifies the
     * type of antialiasing to do when rendering text.
     * 
     * @param antialias the new antialiasing mode
     * @since 1.0
     */
    public void setAntialias(Antialias antialias) {
        try {
            cairo_font_options_set_antialias.invoke(handle(), antialias.value());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_options_set_antialias = Interop.downcallHandle(
            "cairo_font_options_set_antialias", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Gets the antialiasing mode for the font options object.
     * 
     * @return the antialiasing mode
     * @since 1.0
     */
    public Antialias getAntialias() {
        try {
            int result = (int) cairo_font_options_get_antialias.invoke(handle());
            return Antialias.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_options_get_antialias = Interop.downcallHandle(
            "cairo_font_options_get_antialias", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Sets the subpixel order for the font options object. The subpixel order
     * specifies the order of color elements within each pixel on the display device
     * when rendering with an antialiasing mode of {@link Antialias#SUBPIXEL}. See
     * the documentation for {@link SubpixelOrder} for full details.
     * 
     * @param subpixelOrder the new subpixel order
     * @since 1.0
     */
    public void setSubpixelOrder(SubpixelOrder subpixelOrder) {
        try {
            cairo_font_options_set_subpixel_order.invoke(handle(), subpixelOrder.value());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_options_set_subpixel_order = Interop.downcallHandle(
            "cairo_font_options_set_subpixel_order",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Gets the subpixel order for the font options object. See the documentation
     * for {@link SubpixelOrder} for full details.
     * 
     * @return the subpixel order for the font options object
     * @since 1.0
     */
    public SubpixelOrder getSubpixelOrder() {
        try {
            int result = (int) cairo_font_options_get_subpixel_order.invoke(handle());
            return SubpixelOrder.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_options_get_subpixel_order = Interop.downcallHandle(
            "cairo_font_options_get_subpixel_order", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Sets the hint style for font outlines for the font options object. This
     * controls whether to fit font outlines to the pixel grid, and if so, whether
     * to optimize for fidelity or contrast. See the documentation for
     * {@link HintStyle} for full details.
     * 
     * @param hintStyle the new hint style
     * @since 1.0
     */
    public void setHintStyle(HintStyle hintStyle) {
        try {
            cairo_font_options_set_hint_style.invoke(handle(), hintStyle.value());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_options_set_hint_style = Interop.downcallHandle(
            "cairo_font_options_set_hint_style", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Gets the hint style for font outlines for the font options object. See the
     * documentation for {@link HintStyle} for full details.
     * 
     * @return the hint style for the font options object
     * @since 1.0
     */
    public HintStyle getHintStyle() {
        try {
            int result = (int) cairo_font_options_get_hint_style.invoke(handle());
            return HintStyle.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_options_get_hint_style = Interop.downcallHandle(
            "cairo_font_options_get_hint_style", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Sets the metrics hinting mode for the font options object. This controls
     * whether metrics are quantized to integer values in device units. See the
     * documentation for {@link HintMetrics} for full details.
     * 
     * @param hintMetrics the new metrics hinting mode
     * @since 1.0
     */
    public void setHintMetrics(HintMetrics hintMetrics) {
        try {
            cairo_font_options_set_hint_metrics.invoke(handle(), hintMetrics.value());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_options_set_hint_metrics = Interop.downcallHandle(
            "cairo_font_options_set_hint_metrics", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Gets the metrics hinting mode for the font options object. See the
     * documentation for {@link HintMetrics} for full details.
     * 
     * @return the metrics hinting mode for the font options object
     * @since 1.0
     */
    public HintMetrics getHintMetrics() {
        try {
            int result = (int) cairo_font_options_get_hint_metrics.invoke(handle());
            return HintMetrics.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_options_get_hint_metrics = Interop.downcallHandle(
            "cairo_font_options_get_hint_metrics", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Gets the OpenType font variations for the font options object. See
     * {@link #setVariations(String)} for details about the string format.
     * 
     * @return the font variations for the font options object
     * @since 1.16
     */
    public String getVariations() {
        try {
            MemorySegment result = (MemorySegment) cairo_font_options_get_variations.invoke(handle());
            if (MemorySegment.NULL.equals(result)) {
                return null;
            }
            return result.getUtf8String(0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_options_get_variations = Interop.downcallHandle(
            "cairo_font_options_get_variations",
            FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded(), ValueLayout.ADDRESS));

    /**
     * Sets the OpenType font variations for the font options object. Font
     * variations are specified as a string with a format that is similar to the CSS
     * font-variation-settings. The string contains a comma-separated list of axis
     * assignments, which each assignment consists of a 4-character axis name and a
     * value, separated by whitespace and optional equals sign.
     * <p>
     * Examples:
     * 
     * <pre>
     * wght=200,wdth=140.5
     * wght 200 , wdth 140.5
     * </pre>
     * 
     * @param variations the new font variations, or {@code null}
     * @since 1.16
     */
    public void setVariations(String variations) {
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment utf8 = Interop.allocateString(variations, arena);
                cairo_font_options_set_variations.invoke(handle(), utf8);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_font_options_set_variations = Interop.downcallHandle(
            "cairo_font_options_set_variations", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));
}
