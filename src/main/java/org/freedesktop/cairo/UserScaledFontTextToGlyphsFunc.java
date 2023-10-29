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

import io.github.jwharm.cairobindings.Interop;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;

/**
 * UserScaledFontTextToGlyphsFunc is the type of function which is called to convert
 * input text to an array of glyphs. This is used by the {@link Context#showText}
 * operation.
 * <p>
 * Using this callback the user-font has full control on glyphs and their positions.
 * That means, it allows for features like ligatures and kerning, as well as
 * complex
 * <i>shaping</i> required for scripts like Arabic and Indic.
 * <p>
 * In the Glyphs object, the {@code numGlyphs} field is preset to the number of
 * glyph entries available in the {@code glyphsPointer} buffer. If the glyphs buffer
 * is {@code null}, the value of {@code numGlyphs} will be zero. If the provided
 * glyph array is too short for the conversion (or for convenience), a new glyph
 * array may be allocated using {@link Glyphs#allocateGlyphs(int)} and placed in
 * {@code glyphsPointer}. Upon return, {@code numGlyphs} should contain the number
 * of generated glyphs. If the value {@code glyphsPointer} points at has changed
 * after the call, the caller will free the allocated glyph array using
 * {@code cairo_glyph_free()}. (If the caller is using these bindings, this will
 * happen automatically.) The caller will also free the original value of
 * {@code glyphsPointer}, so the callback shouldn't do so. The callback should
 * populate the glyph indices and positions (in font space) assuming that the text
 * is to be shown at the origin.
 * <p>
 * If {@code clustersPointer} is not {@link MemorySegment#NULL}, {@code numClusters}
 * and {@code clusterFlags} are also non-{@code NULL}, and cluster mapping should be
 * computed. The semantics of how cluster array allocation works is similar to the
 * glyph array. That is, if {@code clustersPointer} initially points to a
 * non-{@code NULL} value, that array may be used as a cluster buffer, and
 * {@code numClusters} points to the number of cluster entries available there. If
 * the provided cluster array is too short for the conversion (or for convenience),
 * a new cluster array may be allocated using {@link Glyphs#allocateClusters(int)}
 * and placed in {@code clustersPointer}. In this case, the original value of
 * {@code clustersPointer} will still be freed by the caller. Upon return,
 * {@code numClusters} should contain the number of generated clusters. If the value
 * clusters points at has changed after the call, the caller will free the allocated
 * cluster array using {@code cairo_text_cluster_free()}. (If the caller is using
 * these bindings, this will happen automatically.)
 * <p>
 * The callback is optional. If the {@code numGlyphs} field in the Glyphs object is
 * negative upon the callback returning or the callback throws
 * {@link UnsupportedOperationException}, the unicode_to_glyph callback is tried.
 * See {@link UserScaledFontUnicodeToGlyphFunc}.
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
public interface UserScaledFontTextToGlyphsFunc {

    /**
     * Called to convert input text to an array of glyphs.
     *
     * @param scaledFont the scaled-font being created
     * @param string     a string of text encoded in UTF-8
     * @param glyphs     a Glyphs object with a pointer to the array of glyphs to
     *                   fill and its initial length, a pointer to the array of text
     *                   clusters to fill and its initial length, and the initial
     *                   TextClusterFlags. These fields are expected to be replaced
     *                   with the results.
     * @throws UnsupportedOperationException when fallback options should be tried
     * @throws Exception                     when an error occurs. Throwing an
     *                                       exception will trigger a
     *                                       {@link Status#USER_FONT_ERROR} return
     *                                       value to native code.
     * @since 1.8
     */
    void textToGlyphs(UserScaledFont scaledFont, String string, Glyphs glyphs)
            throws UnsupportedOperationException, Exception;

    /**
     * The callback that is executed by native code. This method marshals the
     * parameters and calls {@link #textToGlyphs}.
     *
     * @param scaledFont      the scaled-font being created
     * @param utf8            a string of text encoded in UTF-8
     * @param utf8Len         length of utf8 in bytes
     * @param glyphsPtr       pointer to array of glyphs to fill, in font space
     * @param numGlyphsPtr    pointer to number of glyphs
     * @param clustersPtr     pointer to array of cluster mapping information to
     *                        fill, or {@link MemorySegment#NULL}
     * @param numClustersPtr  pointer to number of clusters
     * @param clusterFlagsPtr pointer to location to store cluster flags
     *                        corresponding to the output {@code clustersPtr}
     * @return {@link Status#SUCCESS} upon success,
     *         {@link Status#USER_FONT_NOT_IMPLEMENTED} if fallback options should
     *         be tried, or {@link Status#USER_FONT_ERROR} or any other error status
     *         on error.
     * @since 1.8
     */
    default int upcall(MemorySegment scaledFont, MemorySegment utf8, int utf8Len,
                       MemorySegment glyphsPtr, MemorySegment numGlyphsPtr, MemorySegment clustersPtr,
                       MemorySegment numClustersPtr, MemorySegment clusterFlagsPtr) {
        // Read the string
        byte[] utf8Bytes = new byte[utf8Len];
        Interop.reinterpret(utf8, utf8Len).asByteBuffer().get(utf8Bytes);

        // Read the memory segments
        int numGlyphs = numGlyphsPtr.equals(MemorySegment.NULL) ? 0
                : Interop.reinterpret(numGlyphsPtr, ValueLayout.JAVA_INT).get(ValueLayout.JAVA_INT, 0);
        int numClusters = numClustersPtr.equals(MemorySegment.NULL) ? 0
                : Interop.reinterpret(numClustersPtr, ValueLayout.JAVA_INT).get(ValueLayout.JAVA_INT, 0);
        TextClusterFlags clusterFlags = clusterFlagsPtr.equals(MemorySegment.NULL) ? null
                : TextClusterFlags.of(Interop.reinterpret(clusterFlagsPtr, ValueLayout.JAVA_INT).get(ValueLayout.JAVA_INT, 0));

        // Run the callback
        try {
            Glyphs glyphs = new Glyphs(glyphsPtr, numGlyphs, clustersPtr, numClusters, clusterFlags);
            textToGlyphs(new UserScaledFont(scaledFont), new String(utf8Bytes, StandardCharsets.UTF_8), glyphs);

            // Write the results back into the memory segments
            if (! numGlyphsPtr.equals(MemorySegment.NULL))
                Interop.reinterpret(numGlyphsPtr, ValueLayout.JAVA_INT).set(ValueLayout.JAVA_INT, 0, glyphs.getNumGlyphs());
            if (! numClustersPtr.equals(MemorySegment.NULL))
                Interop.reinterpret(numClustersPtr, ValueLayout.JAVA_INT).set(ValueLayout.JAVA_INT, 0, glyphs.getNumClusters());
            if (! clusterFlagsPtr.equals(MemorySegment.NULL))
                Interop.reinterpret(clusterFlagsPtr, ValueLayout.JAVA_INT).set(ValueLayout.JAVA_INT, 0, glyphs.getClusterFlags().getValue());

            return Status.SUCCESS.getValue();
        } catch (UnsupportedOperationException uoe) {
            return Status.USER_FONT_NOT_IMPLEMENTED.getValue();
        } catch (Exception e) {
            return Status.USER_FONT_ERROR.getValue();
        }
    }

    /**
     * Generates an upcall stub, a C function pointer that will call
     * {@link #upcall(MemorySegment, MemorySegment, int, MemorySegment,
     * MemorySegment, MemorySegment, MemorySegment, MemorySegment)}.
     *
     * @param scope the scope in which the upcall stub will be allocated
     * @return the function pointer of the upcall stub
     * @since 1.8
     */
    default MemorySegment toCallback(SegmentScope scope) {
        try {
            FunctionDescriptor fdesc = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS);
            MethodHandle handle = MethodHandles.lookup().findVirtual(
                    UserScaledFontTextToGlyphsFunc.class, "upcall", fdesc.toMethodType());
            return Linker.nativeLinker().upcallStub(handle.bindTo(this), fdesc, scope);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
