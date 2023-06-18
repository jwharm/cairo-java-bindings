package org.freedesktop.cairo;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.ProxyInstance;

/**
 * Font face at particular size and options.
 * <p>
 * A ScaledFont is a font scaled to a particular size and device resolution. A
 * ScaledFont is most useful for low-level font usage where a library or
 * application wants to cache a reference to a scaled font to speed up the
 * computation of metrics.
 * 
 * There are various types of scaled fonts, depending on the font backend they
 * use. The type of a scaled font can be queried using
 * {@link ScaledFont#getType()}.
 * 
 * @see FontFace
 * @see Matrix
 * @see FontOptions
 * @since 1.0
 */
public class ScaledFont extends ProxyInstance {

	{
		Interop.ensureInitialized();
	}

	// Keep a reference to natively allocated resources that are passed to the
	// ScaledFont during its lifetime.

	@SuppressWarnings("unused")
	private FontFace fontFace;

	@SuppressWarnings("unused")
	private Matrix fontMatrix;

	@SuppressWarnings("unused")
	private Matrix ctm;

	/**
	 * Constructor used internally to instantiate a java Context object for a native
	 * {@code cairo_scaled_font_t} instance
	 * 
	 * @param address the memory address of the native {@code cairo_scaled_font_t}
	 *                instance
	 */
	public ScaledFont(MemorySegment address) {
		super(address);
		setDestroyFunc("cairo_scaled_font_destroy");
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
		Status status = null;
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment result = (MemorySegment) cairo_scaled_font_create.invoke(
						fontFace == null ? MemorySegment.NULL : fontFace,
						fontMatrix == null ? MemorySegment.NULL : fontMatrix,
						ctm == null ? MemorySegment.NULL : ctm,
						options == null ? MemorySegment.NULL : options);
				ScaledFont font = new ScaledFont(result);
				font.takeOwnership();
				font.fontFace = fontFace;
				font.fontMatrix = fontMatrix;
				font.ctm = ctm;
				status = font.status();
				return font;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			if (status == Status.NO_MEMORY) {
				throw new RuntimeException(status.toString());
			}
		}
	}

	private static final MethodHandle cairo_scaled_font_create = Interop.downcallHandle("cairo_scaled_font_create",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
					ValueLayout.ADDRESS),
			false);

	/**
	 * Increases the reference count on the ScaledFont by one. This prevents the
	 * ScaledFont from being destroyed until a matching call to
	 * cairo_scaled_font_destroy() is made.
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
			"cairo_scaled_font_reference", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

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

	private static final MethodHandle cairo_scaled_font_status = Interop.downcallHandle("cairo_scaled_font_status",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

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

	private static final MethodHandle cairo_scaled_font_extents = Interop.downcallHandle("cairo_scaled_font_extents",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

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
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

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

	private static final MethodHandle cairo_scaled_font_glyph_extents = Interop
			.downcallHandle("cairo_scaled_font_glyph_extents", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
					ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Converts UTF-8 text to an array of glyphs, optionally with cluster mapping,
	 * that can be used to render later using the ScaledFont.
	 * <p>
	 * The output values can be readily passed to
	 * {@link Context#showTextGlyphs(String, Glyph[], TextCluster[], TextClusterFlags)},
	 * {@link Context#showGlyphs(Glyph[])}, or related functions, assuming that the
	 * exact same ScaledFont is used for the operation.
	 * <p>
	 * TODO: Return Clusters and ClusterFlags
	 * 
	 * @param x      X position to place first glyph
	 * @param y      Y position to place first glyph
	 * @param string a string of text
	 * @return array of glyphs
	 */
	public Glyph[] textToGlyphs(double x, double y, String string) {
		if (string == null) {
			return null;
		}
		Status status = null;
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment utf8 = Interop.allocateNativeString(string, arena);
				MemorySegment glyphsPtr = arena.allocate(ValueLayout.ADDRESS);
				MemorySegment numGlyphsPtr = arena.allocate(ValueLayout.ADDRESS);
				int result = (int) cairo_scaled_font_text_to_glyphs.invoke(handle(), x, y, utf8,
						string == null ? 0 : string.length(), glyphsPtr, numGlyphsPtr, MemorySegment.NULL,
						MemorySegment.NULL, MemorySegment.NULL);
				status = Status.of(result);
				if (status != Status.SUCCESS) {
					cairo_glyph_free.invoke(glyphsPtr);
					return null; // finally block will throw exception
				}
				int numGlyphs = numGlyphsPtr.get(ValueLayout.JAVA_INT, 0);
				Glyph[] glyphs = new Glyph[numGlyphs];
				for (int i = 0; i < numGlyphs; i++) {
					glyphs[i] = new Glyph(glyphsPtr.asSlice(i * ValueLayout.JAVA_BYTE.byteSize()));
				}
				cairo_glyph_free.invoke(glyphsPtr);
				return glyphs;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			if (status != Status.SUCCESS) {
				throw new IllegalStateException(
						status == null ? "cairo_scaled_font_text_to_glyphs returned NULL" : status.toString());
			}
		}
	}

	private static final MethodHandle cairo_scaled_font_text_to_glyphs = Interop.downcallHandle(
			"cairo_scaled_font_text_to_glyphs",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
					ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
					ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS),
			false);

	private static final MethodHandle cairo_glyph_free = Interop.downcallHandle("cairo_glyph_free",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

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
			fontFace.takeOwnership();
			return fontFace;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_scaled_font_get_font_face = Interop.downcallHandle(
			"cairo_scaled_font_get_font_face", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Stores the font options with which the ScaledFont was created into
	 * {@code options}.
	 * 
	 * @param options return value for the font options
	 * @since 1.2
	 */
	public void getFontOptions(FontOptions options) {
		try {
			cairo_scaled_font_get_font_options.invoke(handle(),
					options == null ? MemorySegment.NULL : options.handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_scaled_font_get_font_options = Interop.downcallHandle(
			"cairo_scaled_font_get_font_options", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS),
			false);

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
			"cairo_scaled_font_get_font_matrix", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS),
			false);

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

	private static final MethodHandle cairo_scaled_font_get_ctm = Interop.downcallHandle("cairo_scaled_font_get_ctm",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

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
			"cairo_scaled_font_get_scale_matrix", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS),
			false);

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

	private static final MethodHandle cairo_scaled_font_get_type = Interop.downcallHandle("cairo_scaled_font_get_type",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);
}
