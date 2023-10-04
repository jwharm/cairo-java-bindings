package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.MemoryCleaner;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * ToyFontFace is used in cairo's toy text API. The toy API takes UTF-8 encoded
 * text and is limited in its functionality to rendering simple left-to-right
 * text with no advanced features. That means for example that most complex
 * scripts like Hebrew, Arabic, and Indic scripts are out of question. No
 * kerning or correct positioning of diacritical marks either. The font
 * selection is pretty limited too and doesn't handle the case that the selected
 * font does not cover the characters in the text. This set of functions are
 * really that, a toy text API, for testing and demonstration purposes. Any
 * serious application should avoid ToyFontFace.
 * 
 * @see Context
 * @see FontFace
 * @since 1.8
 */
public class ToyFontFace extends FontFace {

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Constructor used internally to instantiate a java ToyFontFace object for a
     * native {@code cairo_font_face_t} instance
     * 
     * @param address the memory address of the native {@code cairo_font_face_t}
     *                instance
     */
    public ToyFontFace(MemorySegment address) {
        super(address);
    }

    /**
     * Convenience constructor that invokes
     * {@link #create(String, FontSlant, FontWeight)} with a {@code null}
     * {@code family} to specify the platform-specific default font family,
     * {@link FontSlant#NORMAL} and {@link FontWeight#NORMAL}.
     * 
     * @return a newly created ToyFontFace
     */
    public static ToyFontFace create() {
        return create("", FontSlant.NORMAL, FontWeight.NORMAL);
    }

    /**
     * Creates a toy font face from a triplet of family, slant, and weight. These
     * font faces are used in implementation of the Cairo "toy" font API.
     * <p>
     * If {@code family} is empty, the platform-specific default family is
     * assumed. The default family then can be queried using {@link #getFamily()}.
     * <p>
     * The {@link Context#selectFontFace(String, FontSlant, FontWeight)} function
     * uses this to create font faces. See that function for limitations and other
     * details of toy font faces.
     * 
     * @param family a font family name
     * @param slant  the slant for the font
     * @param weight the weight for the font
     * @return a newly created ToyFontFace
     * @since 1.8
     */
    public static ToyFontFace create(String family, FontSlant slant, FontWeight weight) {
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment familyPtr = Interop.allocateNativeString(family, arena);
                MemorySegment result = (MemorySegment) cairo_toy_font_face_create.invoke(familyPtr, slant.getValue(),
                        weight.getValue());
                ToyFontFace fontFace = new ToyFontFace(result);
                MemoryCleaner.takeOwnership(fontFace.handle());
                return fontFace;
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_toy_font_face_create = Interop.downcallHandle("cairo_toy_font_face_create",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

    /**
     * Gets the family name of a toy font.
     * 
     * @return the family name
     * @since 1.8
     */
    public String getFamily() {
        try {
            MemorySegment result = (MemorySegment) cairo_toy_font_face_get_family.invoke(handle());
            return result.getUtf8String(0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_toy_font_face_get_family = Interop.downcallHandle(
            "cairo_toy_font_face_get_family",
            FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded(), ValueLayout.ADDRESS));

    /**
     * Gets the slant of a toy font.
     * 
     * @return the slant value
     * @since 1.8
     */
    public FontSlant getSlant() {
        try {
            int result = (int) cairo_toy_font_face_get_slant.invoke(handle());
            return FontSlant.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_toy_font_face_get_slant = Interop.downcallHandle(
            "cairo_toy_font_face_get_slant", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Gets the weight a toy font.
     * 
     * @return the weight value
     * @since 1.8
     */
    public FontWeight getWeight() {
        try {
            int result = (int) cairo_toy_font_face_get_weight.invoke(handle());
            return FontWeight.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_toy_font_face_get_weight = Interop.downcallHandle(
            "cairo_toy_font_face_get_weight", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
}
