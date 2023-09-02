package org.freedesktop.cairo;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.MemoryCleaner;
import org.freedesktop.freetype.Face;

/**
 * Provides a ScaledFont from the FreeType font backend.
 * <p>
 * The FreeType font backend is primarily used to render text on GNU/Linux
 * systems, but can be used on other platforms too.
 * 
 * @since 1.0
 */
public class FTScaledFont extends ScaledFont {

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Constructor used internally to instantiate a java FTScaledFont object for a
     * native {@code cairo_scaled_font_t} instance
     * 
     * @param address the memory address of the native {@code cairo_scaled_font_t}
     *                instance
     */
    public FTScaledFont(MemorySegment address) {
        super(address);
    }

    /**
     * Creates a FTScaledFont object from a FreeType font face and matrices that
     * describe the size of the font and the environment in which it will be used.
     * 
     * @param fontFace   a FTFontFace
     * @param fontMatrix font space to user space transformation matrix for the
     *                   font. In the simplest case of a N point font, this matrix
     *                   is just a scale by N, but it can also be used to shear the
     *                   font or stretch it unequally along the two axes. See
     *                   {@link Context#setFontMatrix(Matrix)}.
     * @param ctm        user to device transformation matrix with which the font
     *                   will be used.
     * @param options    options to use when getting metrics for the font and
     *                   rendering with it.
     * @return a newly created FTScaledFont
     * @since 1.0
     */
    public static FTScaledFont create(FTFontFace fontFace, Matrix fontMatrix, Matrix ctm, FontOptions options) {
        FTScaledFont font;
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment result = (MemorySegment) cairo_scaled_font_create.invoke(
                        fontFace == null ? MemorySegment.NULL : fontFace.handle(),
                        fontMatrix == null ? MemorySegment.NULL : fontMatrix.handle(),
                        ctm == null ? MemorySegment.NULL : ctm.handle(),
                        options == null ? MemorySegment.NULL : options.handle());
                font = new FTScaledFont(result);
                MemoryCleaner.takeOwnership(font.handle());
                font.fontFace = fontFace;
                font.fontMatrix = fontMatrix;
                font.ctm = ctm;
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (font.status() == Status.NO_MEMORY) {
            throw new RuntimeException(font.status().toString());
        }
        return font;
    }

    private static final MethodHandle cairo_scaled_font_create = Interop.downcallHandle(
            "cairo_scaled_font_create", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Gets the FT_Face object from a FreeType backend font and scales it
     * appropriately for the font and applies OpenType font variations if
     * applicable. You must release the face with {@link #unlockFace()} when you are
     * done using it. Since the {@link Face} object can be shared between multiple
     * FTScaledFont objects, you must not lock any other font objects until you
     * unlock this one. A count is kept of the number of times {@code lockFace()} is
     * called. {@code unlockFace()} must be called the same number of times.
     * <p>
     * You must be careful when using this function in a library or in a threaded
     * application, because freetype's design makes it unsafe to call freetype
     * functions simultaneously from multiple threads, (even if using distinct
     * FT_Face objects). Because of this, application code that acquires an FT_Face
     * object with this call must add its own locking to protect any use of that
     * object, (and which also must protect any other calls into cairo as almost any
     * cairo function might result in a call into the freetype library).
     * 
     * @return The FT_Face object for this font, scaled appropriately, or
     *         {@code null} if this font is in an error state (see
     *         {@link ScaledFont#status()}) or there is insufficient memory.
     * @since 1.0
     */
    public Face lockFace() {
        try {
            MemorySegment result = (MemorySegment) cairo_ft_scaled_font_lock_face.invoke(handle());
            return MemorySegment.NULL.equals(result) ? null : new Face(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_ft_scaled_font_lock_face = Interop.downcallHandle(
            "cairo_ft_scaled_font_lock_face", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Releases a face obtained with {@link #lockFace()}.
     * 
     * @since 1.0
     */
    public void unlockFace() {
        try {
            cairo_ft_scaled_font_unlock_face.invoke(handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_ft_scaled_font_unlock_face = Interop.downcallHandle(
            "cairo_ft_scaled_font_unlock_face", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
}
