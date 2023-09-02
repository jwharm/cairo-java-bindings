package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Proxy;
import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.MemoryCleaner;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.util.List;

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
 * {@link ScaledFont#getType()}.
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

    // Keeps user data keys and values
    private final UserDataStore userDataStore;

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
        userDataStore = new UserDataStore(address.scope());
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
            try (Arena arena = Arena.openConfined()) {
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
     * @return the retrieved extents
     * @since 1.0
     */
    public FontExtents extents() {
        try {
            FontExtents extents = new FontExtents(
                    SegmentAllocator.nativeAllocator(SegmentScope.auto()).allocate(FontExtents.getMemoryLayout()));
            cairo_scaled_font_extents.invoke(handle(), extents.handle());
            return extents;
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
     * @return the retrieved extents
     * @since 1.2
     */
    public TextExtents textExtents(String string) {
        if (string == null) {
            return null;
        }
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment utf8 = Interop.allocateNativeString(string, arena);
                TextExtents extents = new TextExtents(
                        SegmentAllocator.nativeAllocator(SegmentScope.auto()).allocate(TextExtents.getMemoryLayout()));
                cairo_scaled_font_text_extents.invoke(handle(), utf8, extents.handle());
                return extents;
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
     * {@link Context#showGlyphs(Glyph[])}.
     * <p>
     * Note that whitespace glyphs do not contribute to the size of the rectangle
     * ({@code extents.width()} and {@code extents.height()}).
     * 
     * @param glyphs an array of glyph IDs with X and Y offsets.
     * @return the retrieved extents
     * @since 1.0
     */
    public TextExtents glyphExtents(Glyph[] glyphs) {
        if (glyphs == null || glyphs.length == 0) {
            return null;
        }
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment glyphsPtr = arena.allocateArray(ValueLayout.ADDRESS, glyphs.length);
                for (int i = 0; i < glyphs.length; i++) {
                    glyphsPtr.setAtIndex(ValueLayout.ADDRESS, i, glyphs[i].handle());
                }
                TextExtents extents = new TextExtents(
                        SegmentAllocator.nativeAllocator(SegmentScope.auto()).allocate(TextExtents.getMemoryLayout()));
                cairo_scaled_font_glyph_extents.invoke(handle(), glyphsPtr, glyphs.length, extents.handle());
                return extents;
            }
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
     * {@link Context#showTextGlyphs(String, Glyph[], TextCluster[], TextClusterFlags)}.
     * <p>
     * The output values can be converted to arrays and then readily passed to
     * {@link Context#showTextGlyphs(String, Glyph[], TextCluster[], TextClusterFlags)},
     * {@link Context#showGlyphs(Glyph[])}, or related functions, assuming that the
     * exact same ScaledFont is used for the operation.
     * 
     * @param x        X position to place first glyph
     * @param y        Y position to place first glyph
     * @param string   a string of text
     * @param glyphs   a modifiable list of glyphs to fill
     * @param clusters a modifiable list of cluster mapping information to fill, or
     *                 {@code null}
     * @return cluster flags corresponding to the output {@code clusters}, or
     *         {@code null} when {@code clusters} was {@code null}
     * @throws IllegalStateException if the input values are wrong or if conversion
     *                               failed
     * @since 1.8
     */
    public TextClusterFlags textToGlyphs(double x, double y, String string, List<Glyph> glyphs,
            List<TextCluster> clusters) throws IllegalStateException {
        if (string == null || glyphs == null) {
            return null;
        }
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment utf8 = Interop.allocateNativeString(string, arena);
                MemorySegment glyphsPtr = arena.allocate(ValueLayout.ADDRESS.asUnbounded());
                MemorySegment numGlyphsPtr = arena.allocate(ValueLayout.ADDRESS.asUnbounded());
                MemorySegment clustersPtr = clusters == null ? MemorySegment.NULL : arena.allocate(ValueLayout.ADDRESS.asUnbounded());
                MemorySegment numClustersPtr = clusters == null ? MemorySegment.NULL : arena.allocate(ValueLayout.ADDRESS.asUnbounded());
                MemorySegment clusterFlagsPtr = clusters == null ? MemorySegment.NULL : arena.allocate(ValueLayout.ADDRESS.asUnbounded());

                int result = (int) cairo_scaled_font_text_to_glyphs.invoke(handle(), x, y, utf8, string.length(),
                        glyphsPtr, numGlyphsPtr, clustersPtr, numClustersPtr, clusterFlagsPtr);

                // Check returned status, throw exception
                Status status = Status.of(result);
                if (status != Status.SUCCESS) {
                    cairo_glyph_free.invoke(glyphsPtr);
                    if (clusters != null) {
                        cairo_text_cluster_free.invoke(clustersPtr);
                    }
                    throw new IllegalStateException(status.toString());
                }

                // Read the glyphs array
                int numGlyphs = numGlyphsPtr.get(ValueLayout.JAVA_INT, 0);
                MemorySegment newPtr = glyphsPtr.get(ValueLayout.ADDRESS, 0);
                MemorySegment glyphsArray = MemorySegment.ofAddress(newPtr.address(), numGlyphs * Glyph.getMemoryLayout().byteSize());
                for (int i = 0; i < numGlyphs; i++) {
                    MemorySegment gl = glyphsArray.asSlice(Glyph.getMemoryLayout().byteSize() * i);
                    glyphs.add(new Glyph(gl));
                }
                cairo_glyph_free.invoke(newPtr);

                // Clusters are optional
                if (clusters == null) {
                    return null;
                }

                // Read the clusters array
                int numClusters = numClustersPtr.get(ValueLayout.JAVA_INT, 0);
                newPtr = clustersPtr.get(ValueLayout.ADDRESS, 0);
                MemorySegment clustersArray = MemorySegment.ofAddress(newPtr.address(), numClusters * TextCluster.getMemoryLayout().byteSize());
                for (int i = 0; i < numClusters; i++) {
                    MemorySegment tc = clustersArray.asSlice(TextCluster.getMemoryLayout().byteSize() * i);
                    clusters.add(new TextCluster(tc));
                }
                cairo_text_cluster_free.invoke(newPtr);

                // Read the cluster flags
                int flags = clusterFlagsPtr.get(ValueLayout.JAVA_INT, 0);
                return TextClusterFlags.of(flags);
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
                    ValueLayout.ADDRESS.asUnbounded(), ValueLayout.ADDRESS.asUnbounded(),
                    ValueLayout.ADDRESS.asUnbounded(), ValueLayout.ADDRESS.asUnbounded(),
                    ValueLayout.ADDRESS.asUnbounded()));

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
            FontFace fontFace = new FontFace(result);
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
     * Returns the font matrix with which the ScaledFont was created.
     * 
     * @return the matrix
     * @since 1.2
     */
    public Matrix getFontMatrix() {
        try {
            Matrix fontMatrix = Matrix.create();
            cairo_scaled_font_get_font_matrix.invoke(handle(), fontMatrix.handle());
            return fontMatrix;
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
     * @return the CTM
     * @since 1.2
     */
    public Matrix getCTM() {
        try {
            Matrix ctm = Matrix.create();
            cairo_scaled_font_get_ctm.invoke(handle(), ctm.handle());
            return ctm;
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
     * @return the matrix
     * @since 1.8
     */
    public Matrix getScaleMatrix() {
        try {
            Matrix scaleMatrix = Matrix.create();
            cairo_scaled_font_get_scale_matrix.invoke(handle(), scaleMatrix.handle());
            return scaleMatrix;
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
    public FontType getType() {
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
     * Attach user data to the scaled font. This method will generate and return a
     * {@link UserDataKey}. To update the user data for the same key, call
     * {@link #setUserData(UserDataKey, Object)}. To remove user data from a scaled
     * font, call this function with {@code null} for {@code userData}.
     * 
     * @param userData the user data to attach to the scaled font. {@code userData}
     *                 can be any Java object, but if it is a primitive type, a
     *                 {@link MemorySegment} or a {@link Proxy} instance, it will be
     *                 stored as cairo user data in native memory as well.
     * @return the key that the user data is attached to
     * @since 1.4
     */
    public UserDataKey setUserData(Object userData) {
        UserDataKey key = UserDataKey.create(this);
        return setUserData(key, userData);
    }

    /**
     * Attach user data to the scaled font. To remove user data from a scaled font,
     * call this function with the key that was used to set it and {@code null} for
     * {@code userData}.
     * 
     * @param key      the key to attach the user data to
     * @param userData the user data to attach to the scaled font. {@code userData}
     *                 can be any Java object, but if it is a primitive type, a
     *                 {@link MemorySegment} or a {@link Proxy} instance, it will be
     *                 stored as cairo user data in native memory as well.
     * @return the key
     * @throws NullPointerException if {@code key} is {@code null}
     * @since 1.4
     */
    public UserDataKey setUserData(UserDataKey key, Object userData) {
        Status status;
        userDataStore.set(key, userData);
        try {
            int result = (int) cairo_scaled_font_set_user_data.invoke(
                    handle(), key.handle(), userDataStore.dataSegment(userData), MemorySegment.NULL);
            status = Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
        return key;
    }

    private static final MethodHandle cairo_scaled_font_set_user_data = Interop.downcallHandle(
            "cairo_scaled_font_set_user_data", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Return user data previously attached to the scaled font using the specified
     * key. If no user data has been attached with the given key this function
     * returns {@code null}.
     * 
     * @param key the UserDataKey the user data was attached to
     * @return the user data previously attached or {@code null}
     * @since 1.4
     */
    public Object getUserData(UserDataKey key) {
        return key == null ? null : userDataStore.get(key);
    }
}
