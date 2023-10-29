package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Interop;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Provides a ScaledFont from the User font backend.
 * <p>
 * The user-font feature allows the cairo user to provide drawings for glyphs in a
 * font. This is most useful in implementing fonts in non-standard formats, like SVG
 * fonts and Flash fonts, but can also be used by games and other application to
 * draw "funky" fonts.
 *
 * @since 1.8
 */
public class UserScaledFont extends ScaledFont {

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Constructor used internally to instantiate a java UserScaledFont object for a
     * native {@code cairo_scaled_font_t} instance
     *
     * @param address the memory address of the native {@code cairo_scaled_font_t}
     *                instance
     */
    public UserScaledFont(MemorySegment address) {
        super(address);
    }

    /**
     * Gets the foreground pattern of the glyph currently being rendered. A
     * {@link UserScaledFontRenderGlyphFunc} function that has been set with
     * {@link UserFontFace#setRenderColorGlyphFunc} may call this function to
     * retrieve the current foreground pattern for the glyph being rendered. The
     * function should not be called outside of a
     * {@link UserFontFace#setRenderColorGlyphFunc(UserScaledFontRenderGlyphFunc)}
     * callback.
     * <p>
     * The foreground marker pattern contains an internal marker to indicate that it
     * is to be substituted with the current source when rendered to a surface.
     * Querying the foreground marker will reveal a solid black color, however this
     * is not representative of the color that will actually be used. Similarly,
     * setting a solid black color will render black, not the foreground pattern
     * when the glyph is painted to a surface. Using the foreground marker as the
     * source instead of {@link #getForegroundSource()} in a color render callback
     * has the following benefits:
     * <ol>
     * <li>Cairo only needs to call the render callback once as it can cache the
     * recording. Cairo will substitute the actual foreground color when rendering
     * the recording.
     * <li>On backends that have the concept of a foreground color in fonts such as PDF,
     * PostScript, and SVG, cairo can generate more optimal output. The glyph can be
     * included in an embedded font.
     * </ol>
     * The one drawback of the using foreground marker is the render callback can
     * not access the color components of the pattern as the actual foreground
     * pattern is not available at the time the render callback is invoked. If the
     * render callback needs to query the foreground pattern, use
     * {@link #getForegroundSource()}.
     * <p>
     * If the render callback simply wants to call {@link Context#getSource()} with the
     * foreground pattern, {@code getForegroundMarker} is the
     * preferred function to use as it results in better performance than
     * {@link #getForegroundSource()}.
     *
     * @return the current foreground source marker pattern. This object must not be
     *         modified or used outside of a color render callback.
     * @since 1.18
     */
    public Pattern getForegroundMarker() {
        try {
            MemorySegment result = (MemorySegment) cairo_user_scaled_font_get_foreground_marker.invoke(handle());
            return new Pattern.PatternImpl(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_user_scaled_font_get_foreground_marker = Interop.downcallHandle(
            "cairo_user_scaled_font_get_foreground_marker", FunctionDescriptor.of(ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS));

    /**
     * Gets the foreground pattern of the glyph currently being rendered. A
     * {@link UserScaledFontRenderGlyphFunc} function that has been set with
     * {@link UserFontFace#setRenderColorGlyphFunc} may call this function to
     * retrieve the current foreground pattern for the glyph being rendered. The
     * function should not be called outside of a
     * {@link UserFontFace#setRenderColorGlyphFunc(UserScaledFontRenderGlyphFunc)}
     * callback.
     * <p>
     * This function returns the current source at the time the glyph is rendered.
     * Compared with {@link #getForegroundMarker()}, this function returns the
     * actual source pattern that will be used to render the glyph. The render
     * callback is free to query the pattern and extract color components or other
     * pattern data. For example if the render callback wants to create a gradient
     * stop based on colors in the foreground source pattern, it will need to use
     * this function in order to be able to query the colors in the foreground
     * pattern.
     * <p>
     * While this function does not have the restrictions on using the pattern that
     * {@link #getForegroundMarker()} has, it does incur a performance penalty. If a
     * render callback calls this function:
     * <ol>
     * <li>Cairo will call the render callback whenever the current pattern of the
     * context in which the glyph is rendered changes.
     * <li>On backends that support font embedding (PDF, PostScript, and SVG), cairo can
     * not embed this glyph in a font. Instead the glyph will be emitted as an image
     * or sequence of drawing operations each time it is used.
     * </ol>
     *
     * @return the current foreground source pattern.
     * @since 1.18
     */
    public Pattern getForegroundSource() {
        try {
            MemorySegment result = (MemorySegment) cairo_user_scaled_font_get_foreground_source.invoke(handle());
            return new Pattern.PatternImpl(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_user_scaled_font_get_foreground_source = Interop.downcallHandle(
            "cairo_user_scaled_font_get_foreground_source", FunctionDescriptor.of(ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS));
}
