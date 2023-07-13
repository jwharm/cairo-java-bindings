package org.freedesktop.cairo;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.ref.Cleaner;

import io.github.jwharm.javagi.interop.Interop;
import org.freedesktop.freetype.Face;

/**
 * Provides a FontFace for FreeType.
 * <p>
 * The FreeType font backend is primarily used to render text on GNU/Linux
 * systems, but can be used on other platforms too.
 * 
 * @since 1.0
 */
public class FTFontFace extends FontFace {

    static {
        Cairo.ensureInitialized();
    }

    // Keep a reference to the FreeType FT_Face during the lifetime of the
    // FTFontFace.

    @SuppressWarnings("unused")
    private Face ftFace;

    /**
     * Constructor used internally to instantiate a java FTFontFace object for a
     * native {@code cairo_font_face} instance
     * 
     * @param address the memory address of the native {@code cairo_font_face}
     *                instance
     */
    public FTFontFace(MemorySegment address) {
        super(address);
    }

    /**
     * Creates a new font face for the FreeType font backend from a pre-opened
     * FreeType face. This font can then be used with
     * {@link Context#setFontFace(FontFace)} or
     * {@link FTScaledFont#create(FTFontFace, Matrix, Matrix, FontOptions)}. The
     * {@link FTScaledFont} returned from {@code FTScaledFont.create()} is also for
     * the FreeType backend and can be used with functions such as
     * {@link FTScaledFont#lockFace()}. Note that Cairo may keep a reference to the
     * FT_Face alive in a font-cache and the exact lifetime of the reference depends
     * highly upon the exact usage pattern and is subject to external factors. The
     * Java bindings will call {@code FT_Done_Face()} when the last reference to the
     * {@code FTFontFace} has been dropped (using a {@link Cleaner}).
     * 
     * @param ftFace    A FreeType face object, already opened.
     * @param loadFlags flags to pass to {@code FT_Load_Glyph} when loading glyphs
     *                  from the font. These flags are OR'ed together with the flags
     *                  derived from the {@link FontOptions} passed to
     *                  {@link ScaledFont#create(FontFace, Matrix, Matrix, FontOptions)},
     *                  so only a few values such as
     *                  {@code FT_LOAD_VERTICAL_LAYOUT}, and
     *                  {@code FT_LOAD_FORCE_AUTOHINT} are useful. You should not
     *                  pass any of the flags affecting the load target, such as
     *                  {@code FT_LOAD_TARGET_LIGHT}.
     * @return a newly created FTFontFace.
     * @since 1.0
     */
    public static FTFontFace create(Face ftFace, int loadFlags) {
        try {
            MemorySegment result = (MemorySegment) cairo_ft_font_face_create_for_ft_face.invoke(ftFace.handle(), loadFlags);
            FTFontFace fontFace = new FTFontFace(result);
            fontFace.ftFace = ftFace;
            return fontFace;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_ft_font_face_create_for_ft_face = Interop.downcallHandle(
            "cairo_ft_font_face_create_for_ft_face",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * See {@link FTSynthesize}.
     * 
     * @return the current set of synthesis options.
     * @since 1.12
     */
    public FTSynthesize getSynthesize() {
        try {
            int result = (int) cairo_ft_font_face_get_synthesize.invoke(handle());
            return new FTSynthesize(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_ft_font_face_get_synthesize = Interop.downcallHandle(
            "cairo_ft_font_face_get_synthesize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * FreeType provides the ability to synthesize different glyphs from a base
     * font, which is useful if you lack those glyphs from a true bold or oblique
     * font. See also {@link FTSynthesize}.
     * 
     * @param synthFlags the set of synthesis options to enable
     * @since 1.12
     */
    public void setSynthesize(FTSynthesize synthFlags) {
        try {
            cairo_ft_font_face_set_synthesize.invoke(handle(), synthFlags.getValue());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_ft_font_face_set_synthesize = Interop.downcallHandle(
            "cairo_ft_font_face_set_synthesize", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * See {@link FTSynthesize}.
     * 
     * @param synthFlags the set of synthesis options to disable
     * @since 1.12
     */
    public void unsetSynthesize(FTSynthesize synthFlags) {
        try {
            cairo_ft_font_face_unset_synthesize.invoke(handle(), synthFlags.getValue());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_ft_font_face_unset_synthesize = Interop.downcallHandle(
            "cairo_ft_font_face_unset_synthesize",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
}
