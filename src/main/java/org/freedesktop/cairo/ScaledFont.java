/* cairo-java-bindings - Java language bindings for cairo
 * Copyright (C) 2024 Jan-Willem Harmannij
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

import io.github.jwharm.cairobindings.Proxy;
import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.MemoryCleaner;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Font face at particular size and options.
 * <p>
 * A ScaledFont is a font scaled to a particular size and device resolution. A
 * ScaledFont is most useful for low-level font usage where a library or
 * application wants to cache a reference to a scaled font to speed up the
 * computation of metrics.
 * <p>
 * There are various types of scaled fonts, depending on the font backend they
 * use. The type of a scaled font can be queried using
 * {@link ScaledFont#getFontType()}.
 * 
 * @see FontFace
 * @see Matrix
 * @see FontOptions
 * @since 1.0
 */
public class ScaledFont extends Proxy {

    static {
        Cairo.ensureInitialized();
    }

    // Keep a reference to natively allocated resources that are passed to the
    // ScaledFont during its lifetime.

    FontFace fontFace;
    Matrix fontMatrix;
    Matrix ctm;

    /**
     * Constructor used internally to instantiate a java ScaledFont object for a
     * native {@code cairo_scaled_font_t} instance
     * 
     * @param address the memory address of the native {@code cairo_scaled_font_t}
     *                instance
     */
    public ScaledFont(MemorySegment address) {
        super(address);
        MemoryCleaner.setFreeFunc(handle(), "cairo_scaled_font_destroy");
    }

    /**
     * Invokes the cleanup action that is normally invoked during garbage collection.
     * If the instance is "owned" by the user, the {@code destroy()} function is run
     * to dispose the native instance.
     */
    public void destroy() {
        MemoryCleaner.free(handle());
    }

    /**
     * Creates a ScaledFont object from a font face and matrices that describe the
     * size of the font and the environment in which it will be used.
     * 
     * @param fontFace   a FontFace
     * @param fontMatrix font space to user space transformation matrix for the
     *                   font. In the simplest case of a N point font, this matrix
     *                   is just a scale by N, but it can also be used to shear the
     *                   font or stretch it unequally along the two axes. See
     *                   {@link Context#setFontMatrix(Matrix)}.
     * @param ctm        user to device transformation matrix with which the font
     *                   will be used.
     * @param options    options to use when getting metrics for the font and
     *                   rendering with it.
     * @return a newly created ScaledFont
     * @since 1.0
     */
    public static ScaledFont create(FontFace fontFace, Matrix fontMatrix, Matrix ctm, FontOptions options) {
        ScaledFont font;
        try {
            MemorySegment result = (MemorySegment) cairo_scaled_font_create.invoke(
                    fontFace == null ? MemorySegment.NULL : fontFace.handle(),
                    fontMatrix == null ? MemorySegment.NULL : fontMatrix.handle(),
                    ctm == null ? MemorySegment.NULL : ctm.handle(),
                    options == null ? MemorySegment.NULL : options.handle());
            font = new ScaledFont(result);
            MemoryCleaner.takeOwnership(font.handle());
            font.fontFace = fontFace;
            font.fontMatrix = fontMatrix;
            font.ctm = ctm;
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
     * Increases the reference count on the ScaledFont by one. This prevents the
     * ScaledFont from being destroyed until a matching call to
     * {@link #destroy()} is made.
     * 
     * @since 1.0
     */
    void reference() {
        try {
            cairo_scaled_font_reference.invoke(handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_scaled_font_reference = Interop.downcallHandle(
            "cairo_scaled_font_reference", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Checks whether an error has previously occurred for this scaled_font.
     * 
     * @return {@link Status#SUCCESS} or another error such as
     *         {@link Status#NO_MEMORY}.
     * @since 1.0
     */
    public Status status() {
        try {
            int result = (int) cairo_scaled_font_status.invoke(handle());
            return Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_scaled_font_status = Interop.downcallHandle(
            "cairo_scaled_font_status", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Gets the metrics for a ScaledFont.
     * 
     * @param extents a FontExtents in which to store the retrieved extents
     * @since 1.0
     */
    public void extents(FontExtents extents) {
        try {
            cairo_scaled_font_extents.invoke(handle(), extents.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_scaled_font_extents = Interop.downcallHandle(
            "cairo_scaled_font_extents", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Gets the extents for a string of text. The extents describe a user-space
     * rectangle that encloses the "inked" portion of the text drawn at the origin
     * (0,0) (as it would be drawn by {@link Context#showText(String)} if the cairo
     * graphics state were set to the same FontFace, font Matrix, CTM, and
     * FontOptions as this ScaledFont). Additionally, the {@code xAdvance()} and
     * {@code yAdvance()} values indicate the amount by which the current point
     * would be advanced by {@link Context#showText(String)}.
     * <p>
     * Note that whitespace characters do not directly contribute to the size of the
     * rectangle ({@code extents.width()} and {@code extents.height()}). They do
     * contribute indirectly by changing the position of non-whitespace characters.
     * In particular, trailing whitespace characters are likely to not affect the
     * size of the rectangle, though they will affect the {@code xAdvance()} and
     * {@code yAdvance()} values.
     * 
     * @param string a string of text
     * @param extents a TextExtents in which to store the retrieved extents.
     * @since 1.2
     */
    public void textExtents(String string, TextExtents extents) {
        if (string == null) {
            return;
        }
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment utf8 = Interop.allocateNativeString(string, arena);
                cairo_scaled_font_text_extents.invoke(handle(), utf8, extents.handle());
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_scaled_font_text_extents = Interop.downcallHandle(
            "cairo_scaled_font_text_extents",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Gets the extents for an array of glyphs. The extents describe a user-space
     * rectangle that encloses the "inked" portion of the glyphs, (as they would be
     * drawn by cairo_show_glyphs() if the cairo graphics state were set to the same
     * FontFace, font Matrix, CTM, and FontOptions as this ScaledFont).
     * Additionally, the {@code xAdvance()} and {@code yAdvance()} values indicate
     * the amount by which the current point would be advanced by
     * {@link Context#showGlyphs}.
     * <p>
     * Note that whitespace glyphs do not contribute to the size of the rectangle
     * ({@code extents.width()} and {@code extents.height()}).
     * 
     * @param glyphs an array of glyph IDs with X and Y offsets.
     * @param extents a TextExtents in which to store the retrieved extents.
     * @since 1.0
     */
    public void glyphExtents(Glyphs glyphs, TextExtents extents) {
        if (glyphs == null) {
            return;
        }
        try {
            cairo_scaled_font_glyph_extents.invoke(handle(), glyphs.getGlyphsPointer(), glyphs.getNumGlyphs(), extents.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_scaled_font_glyph_extents = Interop.downcallHandle(
            "cairo_scaled_font_glyph_extents", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, 
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Converts UTF-8 text to an array of glyphs, optionally with cluster mapping,
     * that can be used to render later using the ScaledFont.
     * <p>
     * The {@code glyphs} and {@code clusters} parameters must be modifiable lists.
     * Upon completion of the function call, the glyphs and cluster mappings will
     * have been added to these lists. The {@code glyphs} list cannot be
     * {@code null}, but {@code clusters} is optional and can be {@code null} when
     * cluster mapping does not need to be computed.
     * <p>
     * In the simplest case, clusters can point to NULL because no cluster mapping
     * is needed. In code:
     * 
     * <pre>{@code
     * List<Glyph> glyphs = new ArrayList<>();
     * scaledFont.textToGlyphs(x, y, string, glyphs, null);
     * }</pre>
     * 
     * If cluster mapping is to be computed:
     * 
     * <pre>{@code
     * List<Glyph> glyphs = new ArrayList<>();
     * List<TextCluster> clusters = new ArrayList<>();
     * var clusterFlags = scaledFont.textToGlyphs(x, y, string, glyphs, clusters);
     * }</pre>
     * 
     * For details of how TextClusters and TextClusterFlags map input UTF-8 text to
     * the output glyphs see
     * {@link Context#showTextGlyphs(String, Glyphs)}.
     * <p>
     * The output values can be converted to arrays and then readily passed to
     * {@link Context#showTextGlyphs(String, Glyphs)},
     * {@link Context#showGlyphs}, or related functions, assuming that the
     * exact same ScaledFont is used for the operation.
     * 
     * @param  x       X position to place first glyph
     * @param  y       Y position to place first glyph
     * @param  string  a string of text
     * @return the glyphs array
     * @throws IllegalStateException if the input values are wrong or if conversion
     *                               failed
     * @since 1.8
     */
    public Glyphs textToGlyphs(double x, double y, String string) throws IllegalStateException {
        if (string == null) {
            return null;
        }

        // Define shorthands for void* and int* memory layouts
        final ValueLayout VOID_POINTER = ValueLayout.ADDRESS.withTargetLayout(ValueLayout.ADDRESS);
        final ValueLayout INT_POINTER = ValueLayout.ADDRESS.withTargetLayout(ValueLayout.JAVA_INT);

        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment stringPtr = Interop.allocateNativeString(string, arena);
                MemorySegment glyphsPtr = arena.allocate(VOID_POINTER);
                MemorySegment numGlyphsPtr = arena.allocate(INT_POINTER);
                MemorySegment clustersPtr = arena.allocate(VOID_POINTER);
                MemorySegment numClustersPtr = arena.allocate(INT_POINTER);
                MemorySegment clusterFlagsPtr = arena.allocate(INT_POINTER);

                int result = (int) cairo_scaled_font_text_to_glyphs.invoke(handle(), x, y, stringPtr, string.length(),
                        glyphsPtr, numGlyphsPtr, clustersPtr, numClustersPtr, clusterFlagsPtr);

                // Check returned status, throw exception
                Status status = Status.of(result);
                if (status != Status.SUCCESS) {
                    cairo_glyph_free.invoke(glyphsPtr);
                    cairo_text_cluster_free.invoke(clustersPtr);
                    throw new IllegalStateException(status.toString());
                }

                var numGlyphs = numGlyphsPtr.get(ValueLayout.JAVA_INT, 0);
                var glyphsArray = glyphsPtr.get(ValueLayout.ADDRESS, 0);
                var numClusters = numClustersPtr.get(ValueLayout.JAVA_INT, 0);
                var clustersArray = clustersPtr.get(ValueLayout.ADDRESS, 0);
                var flags = clusterFlagsPtr.get(ValueLayout.JAVA_INT, 0);

                return new Glyphs(glyphsArray, numGlyphs, clustersArray, numClusters,
                        flags == 0 ? null : TextClusterFlags.of(flags));
            }
        } catch (Throwable e) {
            if (e instanceof IllegalStateException ise) {
                throw ise;
            }
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_scaled_font_text_to_glyphs = Interop.downcallHandle(
            "cairo_scaled_font_text_to_glyphs",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE,
                    ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    private static final MethodHandle cairo_glyph_free = Interop.downcallHandle(
            "cairo_glyph_free", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

    private static final MethodHandle cairo_text_cluster_free = Interop.downcallHandle(
            "cairo_text_cluster_free", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

    /**
     * Gets the font face that this scaled font uses. This might be the font face
     * passed to {@link #create(FontFace, Matrix, Matrix, FontOptions)}, but this
     * does not hold true for all possible cases.
     * 
     * @return The FontFace with which the ScaledFont was created.
     */
    public FontFace getFontFace() {
        try {
            MemorySegment result = (MemorySegment) cairo_scaled_font_get_font_face.invoke(handle());
            FontFace temp = new FontFace(result);
            // Instantiate the correct FontFace subclass for the FontType
            FontFace fontFace = switch(temp.getFontType()) {
                case TOY -> new ToyFontFace(result);
                case FT -> new FTFontFace(result);
                case USER -> new UserFontFace(result);
                default -> temp;
            };
            // Take a reference on the returned fontface
            fontFace.reference();
            MemoryCleaner.takeOwnership(fontFace.handle());
            return fontFace;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_scaled_font_get_font_face = Interop.downcallHandle(
            "cairo_scaled_font_get_font_face", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Stores the font options with which the ScaledFont was created into
     * {@code options}.
     * 
     * @param options return value for the font options
     * @since 1.2
     */
    public void getFontOptions(FontOptions options) {
        try {
            cairo_scaled_font_get_font_options.invoke(handle(), options == null ? MemorySegment.NULL : options.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_scaled_font_get_font_options = Interop.downcallHandle(
            "cairo_scaled_font_get_font_options", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Stores the font matrix with which the ScaledFont was created into
     * {@code fontMatrix}.
     *
     * @param fontMatrix return value for the matrix
     * @since 1.2
     */
    public void getFontMatrix(Matrix fontMatrix) {
        try {
            cairo_scaled_font_get_font_matrix.invoke(handle(), fontMatrix.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_scaled_font_get_font_matrix = Interop.downcallHandle(
            "cairo_scaled_font_get_font_matrix", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Returns the CTM with which the ScaledFont was created. Note that the
     * translation offsets (x0, y0) of the CTM are ignored by
     * {@link #create(FontFace, Matrix, Matrix, FontOptions)}. So, the matrix this
     * function returns always has 0,0 as x0,y0.
     * 
     * @param ctm return value for the CTM
     * @since 1.2
     */
    public void getCTM(Matrix ctm) {
        try {
            cairo_scaled_font_get_ctm.invoke(handle(), ctm.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_scaled_font_get_ctm = Interop.downcallHandle(
            "cairo_scaled_font_get_ctm", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Returns the scale matrix of the ScaledFont. The scale matrix is product of
     * the font matrix and the ctm associated with the scaled font, and hence is the
     * matrix mapping from font space to device space.
     * 
     * @param scaleMatrix return value for the matrix
     * @since 1.8
     */
    public void getScaleMatrix(Matrix scaleMatrix) {
        try {
            cairo_scaled_font_get_scale_matrix.invoke(handle(), scaleMatrix.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_scaled_font_get_scale_matrix = Interop.downcallHandle(
            "cairo_scaled_font_get_scale_matrix", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * This function returns the type of the backend used to create a scaled font.
     * See {@link FontType} for available types. However, this function never
     * returns {@link FontType#TOY}.
     * 
     * @return the type of the ScaledFont.
     * @since 1.2
     */
    public FontType getFontType() {
        try {
            int result = (int) cairo_scaled_font_get_type.invoke(handle());
            return FontType.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_scaled_font_get_type = Interop.downcallHandle(
            "cairo_scaled_font_get_type", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Attach user data to the scaled font. To remove user data from a scaled font,
     * call this function with the key that was used to set it and {@code null} for
     * {@code userData}.
     *
     * @param key      the key to attach the user data to
     * @param userData the user data to attach to the scaled font
     * @return the key
     * @throws NullPointerException if {@code key} is {@code null}
     * @since 1.4
     */
    public UserDataKey setUserData(UserDataKey key, MemorySegment userData) {
        Status status;
        try {
            int result = (int) cairo_scaled_font_set_user_data.invoke(handle(), key.handle(), userData, MemorySegment.NULL);
            status = Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
        return key;
    }

    private static final MethodHandle cairo_scaled_font_set_user_data = Interop.downcallHandle("cairo_scaled_font_set_user_data",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS));

    /**
     * Return user data previously attached to the scaled font using the specified
     * key. If no user data has been attached with the given key this function
     * returns {@code null}.
     * <p>
     * The returned memory segment has zero length. It can be resized with
     * {@link MemorySegment#reinterpret(long)}.
     *
     * @param key the UserDataKey the user data was attached to
     * @return the user data previously attached or {@code null}
     * @since 1.4
     */
    public MemorySegment getUserData(UserDataKey key) {
        if (key == null) {
            return null;
        }
        try {
            MemorySegment result = (MemorySegment) cairo_scaled_font_get_user_data.invoke(handle(), key.handle());
            return MemorySegment.NULL.equals(result) ? null : result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_scaled_font_get_user_data = Interop.downcallHandle("cairo_scaled_font_get_user_data",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Get the CairoScaledFont GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_scaled_font_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_scaled_font_get_type = Interop.downcallHandle(
            "cairo_gobject_scaled_font_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
