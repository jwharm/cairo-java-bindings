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
 * UserScaledFontUnicodeToGlyphFunc is the type of function which is called to
 * convert an input Unicode character to a single glyph. This is used by the
 * {@link Context#showText(String)} operation.
 * <p>
 * This callback is used to provide the same functionality as the text_to_glyphs
 * callback does (see {@link UserScaledFontTextToGlyphsFunc}) but has much less
 * control on the output, in exchange for increased ease of use. The inherent
 * assumption to using this callback is that each character maps to one glyph, and
 * that the mapping is context independent. It also assumes that glyphs are
 * positioned according to their advance width. These mean no ligatures, kerning, or
 * complex scripts can be implemented using this callback.
 * <p>
 * The callback is optional, and only used if text_to_glyphs callback is not set or
 * fails to return glyphs. If this callback is not set, an identity mapping from
 * Unicode code-points to glyph indices is assumed.
 * <p>
 * Note: While cairo does not impose any limitation on glyph indices, some
 * applications may assume that a glyph index fits in a 16-bit unsigned integer. As
 * such, it is advised that user-fonts keep their glyphs in the 0 to 65535 range.
 * Furthermore, some applications may assume that glyph 0 is a special
 * glyph-not-found glyph. User-fonts are advised to use glyph 0 for such purposes
 * and do not use that glyph value for other purposes.
 *
 * @since 1.8
 */
@FunctionalInterface
public interface UserScaledFontUnicodeToGlyphFunc {

    /**
     * Called to convert an input Unicode character to a single glyph.
     *
     * @param scaledFont the scaled-font being created
     * @param unicode    input unicode character code-point
     * @return output glyph index
     * @throws UnsupportedOperationException when fallback options should be tried
     * @throws Exception                     when an error occurs. Throwing an
     *                                       exception will trigger a
     *                                       {@link Status#USER_FONT_ERROR} return
     *                                       value to native code.
     * @since 1.8
     */
    long unicodeToGlyph(UserScaledFont scaledFont, long unicode)
            throws UnsupportedOperationException, Exception;

    /**
     * The callback that is executed by native code. This method marshals the
     * parameters and calls {@link #unicodeToGlyph}.
     *
     * @param scaledFont the scaled-font being created
     * @param unicode input unicode character code-point
     * @param glyphIndex output glyph index
     * @return {@link Status#SUCCESS} upon success,
     *         {@link Status#USER_FONT_NOT_IMPLEMENTED} if fallback options should
     *         be tried, or {@link Status#USER_FONT_ERROR} or any other error status
     *         on error.
     * @since 1.8
     */
    default int upcall(MemorySegment scaledFont, long unicode, MemorySegment glyphIndex) {
        try {
            long result = unicodeToGlyph(new UserScaledFont(scaledFont), unicode);
            glyphIndex.reinterpret(ValueLayout.JAVA_LONG.byteSize()).set(ValueLayout.JAVA_LONG, 0, result);
            return Status.SUCCESS.getValue();
        } catch (UnsupportedOperationException uoe) {
            return Status.USER_FONT_NOT_IMPLEMENTED.getValue();
        } catch (Exception e) {
            return Status.USER_FONT_ERROR.getValue();
        }
    }

    /**
     * Generates an upcall stub, a C function pointer that will call
     * {@link #upcall}.
     *
     * @param arena the arena in which the upcall stub will be allocated
     * @return the function pointer of the upcall stub
     * @since 1.8
     */
    default MemorySegment toCallback(Arena arena) {
        try {
            FunctionDescriptor fdesc = FunctionDescriptor.of(ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS);
            MethodHandle handle = MethodHandles.lookup().findVirtual(
                    UserScaledFontUnicodeToGlyphFunc.class, "upcall", fdesc.toMethodType());
            return Linker.nativeLinker().upcallStub(handle.bindTo(this), fdesc, arena);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
