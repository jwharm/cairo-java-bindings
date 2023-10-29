/* cairo-java-bindings - Java language bindings for cairo
 * Copyright (C) 2023 Jan-Willem Harmannij
 *
 * SPDX-License-Identifier: LGPL-2.1-or-later
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://www.gnu.org/licenses/>.
 */

package org.freedesktop.cairo;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * UserScaledFontRenderGlyphFunc is the type of function which is called when a user
 * scaled-font needs to render a glyph.
 * <p>
 * The callback is mandatory, and expected to draw the glyph with code {@code glyph}
 * to the cairo context {@code cr}. {@code cr} is prepared such that the glyph
 * drawing is done in font space. That is, the matrix set on {@code cr} is the scale
 * matrix of {@code scaledFont}. The {@code extents} argument is where the user font
 * sets the font extents for {@code scaledFont}. However, if user prefers to draw in
 * user space, they can achieve that by changing the matrix on {@code cr}.
 * <p>
 * All cairo rendering operations to {@code cr} are permitted. However, when this
 * callback is set with {@link UserFontFace#setRenderGlyphFunc}, the result is
 * undefined if any source other than the default source on {@code cr} is used. That
 * means, glyph bitmaps should be rendered using {@link Context#mask(Pattern)}
 * instead of {@link Context#paint()}.
 * <p>
 * When this callback is set with {@link UserFontFace#setRenderColorGlyphFunc}, the
 * default source is black. Setting the source is a valid operation.
 * {@link UserScaledFont#getForegroundMarker()} or
 * {@link UserScaledFont#getForegroundSource()} may be called to obtain the current
 * source at the time the glyph is rendered.
 * <p>
 * Other non-default settings on {@code cr} include a font size of 1.0 (given that
 * it is set up to be in font space), and font options corresponding to
 * {@code scaledFont}.
 * <p>
 * The {@code extents} argument is preset to have {@code xBearing}, {@code width},
 * and {@code yAdvance} of zero, {@code yBearing} set to {@code -extents.ascent},
 * {@code height} to {@code extents.ascent+extents.descent}, and {@code xAdvance} to
 * {@code extents.maxXAdvance}. The only field a user needs to set in the majority
 * of cases is {@code xAdvance}. If the {@code width} field is zero upon the
 * callback returning (which is its preset value), the glyph extents are
 * automatically computed based on the drawings done to {@code cr}. This is in most
 * cases exactly what the desired behavior is. However, if for any reason the
 * callback sets the extents, it must be ink extents, and include the extents of all
 * drawing done to {@code cr} in the callback.
 * <p>
 * Where both color and non-color callbacks has been set using
 * {@link UserFontFace#setRenderGlyphFunc}, and
 * {@link UserFontFace#setRenderColorGlyphFunc}, the color glyph callback will be
 * called first. If the color glyph callback throws
 * {@link UnsupportedOperationException}, any drawing operations are discarded and
 * the non-color callback will be called. This fallback sequence allows a user font
 * face to contain a combination of both color and non-color glyphs.
 *
 * @since 1.8
 */
@FunctionalInterface
public interface UserScaledFontRenderGlyphFunc {

    /**
     * called when a user scaled-font needs to render a glyph.
     *
     * @param scaledFont user scaled-font
     * @param glyph      glyph code to render
     * @param cr         cairo context to draw to, in font space
     * @param extents    glyph extents to fill in, in font space
     * @throws UnsupportedOperationException when fallback options should be tried
     * @throws Exception                     when an error occurs. Throwing an
     *                                       exception will trigger a
     *                                       {@link Status#USER_FONT_ERROR} return
     *                                       value to native code.
     * @since 1.8
     */
    void renderGlyph(UserScaledFont scaledFont, long glyph, Context cr, FontExtents extents)
            throws UnsupportedOperationException, Exception;

    /**
     * The callback that is executed by native code. This method marshals the
     * parameters and calls {@link #renderGlyph}.
     *
     * @param scaledFont user scaled-font
     * @param glyph      glyph code to render
     * @param cr         cairo context to draw to, in font space
     * @param extents    glyph extents to fill in, in font space
     * @return {@link Status#SUCCESS} upon success,
     *         {@link Status#USER_FONT_NOT_IMPLEMENTED} if fallback options should
     *         be tried, or {@link Status#USER_FONT_ERROR} if an exception was
     *         thrown.
     * @since 1.8
     */
    default int upcall(MemorySegment scaledFont, long glyph, MemorySegment cr, MemorySegment extents) {
        try {
            renderGlyph(new UserScaledFont(scaledFont), glyph, new Context(cr), new FontExtents(extents));
            return Status.SUCCESS.getValue();
        } catch (UnsupportedOperationException uoe) {
            return Status.USER_FONT_NOT_IMPLEMENTED.getValue();
        } catch (Exception e) {
            return Status.USER_FONT_ERROR.getValue();
        }
    }

    /**
     * Generates an upcall stub, a C function pointer that will call
     * {@link #upcall(MemorySegment, long, MemorySegment, MemorySegment)}.
     *
     * @param scope the scope in which the upcall stub will be allocated
     * @return the function pointer of the upcall stub
     * @since 1.8
     */
    default MemorySegment toCallback(SegmentScope scope) {
        try {
            FunctionDescriptor fdesc = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                    ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.ADDRESS);
            MethodHandle handle = MethodHandles.lookup().findVirtual(
                    UserScaledFontRenderGlyphFunc.class, "upcall", fdesc.toMethodType());
            return Linker.nativeLinker().upcallStub(handle.bindTo(this), fdesc, scope);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
