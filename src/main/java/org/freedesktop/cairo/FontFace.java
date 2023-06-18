package org.freedesktop.cairo;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.ProxyInstance;

/**
 * The base class for font faces.
 * <p>
 * FontFace represents a particular font at a particular weight, slant, and
 * other characteristic but no size, transformation, or size.
 * <p>
 * Font faces are created using font-backend-specific constructors, typically of
 * the form {@code FontFaceClass.create()}, or implicitly using the toy text API
 * by way of {@link Context#selectFontFace(String, FontSlant, FontWeight)}. The
 * resulting face can be accessed using {@link Context#getFontFace()}.
 * <p>
 * A FontFace specifies all aspects of a font other than the size or font matrix
 * (a font matrix is used to distort a font by shearing it or scaling it
 * unequally in the two directions) . A font face can be set on a Context by
 * using {@link Context#setFontFace(FontFace)} the size and font matrix are set
 * with {@link Context#setFontSize(double)} and
 * {@link Context#setFontMatrix(Matrix)}.
 * <p>
 * There are various types of font faces, depending on the font backend they
 * use. The type of a font face can be queried using {@link FontFace#getType()}.
 * 
 * @see ScaledFont
 * @since 1.0
 */
public class FontFace extends ProxyInstance {

	static {
		Interop.ensureInitialized();
	}

	/**
	 * Constructor used internally to instantiate a java FontFace object for a
	 * native {@code cairo_font_face_t} instance
	 * 
	 * @param address the memory address of the native {@code cairo_font_face_t}
	 *                instance
	 */
	public FontFace(MemorySegment address) {
		super(address);
		setDestroyFunc("cairo_font_face_destroy");
	}

	/**
	 * Checks whether an error has previously occurred for this font face
	 * 
	 * @return {@link Status#SUCCESS} or another error such as
	 *         {@link Status#NO_MEMORY}.
	 * @since 1.0
	 */
	public Status status() {
		try {
			int result = (int) cairo_font_face_status.invoke(handle());
			return Status.of(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_font_face_status = Interop.downcallHandle("cairo_font_face_status",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Returns the type of the backend used to create a font face. See
	 * {@link FontType} for available types.
	 * 
	 * @return The type of the FontFace.
	 * @since 1.2
	 */
	public FontType getType() {
		try {
			int result = (int) cairo_font_face_get_type.invoke(handle());
			return FontType.of(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_font_face_get_type = Interop.downcallHandle("cairo_font_face_get_type",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Increases the reference count on the FontFace by one. This prevents the
	 * FontFace from being destroyed until a matching call to
	 * {@code cairo_font_face_destroy()} is made.
	 * 
	 * @since 1.0
	 */
	void reference() {
		try {
			cairo_font_face_reference.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_font_face_reference = Interop.downcallHandle("cairo_font_face_reference",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);
}
