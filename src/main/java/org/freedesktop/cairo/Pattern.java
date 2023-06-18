package org.freedesktop.cairo;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.ProxyInstance;

/**
 * Sources for drawing.
 * <p>
 * A {@link Pattern} represents a source when drawing onto a surface. There are
 * different subtypes of Pattern, for different types of sources; for example,
 * cairo_pattern_create_rgb() creates a pattern for a solid opaque color.
 * <p>
 * Other than various cairo_pattern_create_type() functions, some of the pattern
 * types can be implicitly created using various cairo_set_source_type()
 * functions; for example cairo_set_source_rgb().
 * <p>
 * The type of a pattern can be queried with cairo_pattern_get_type().
 * <p>
 * Memory management of cairo_pattern_t is done with cairo_pattern_reference()
 * and cairo_pattern_destroy().
 * 
 * @since 1.0
 */
public abstract class Pattern extends ProxyInstance {

	static {
		Interop.ensureInitialized();
	}

	// Keep a reference to the matrix during the lifetime of the Pattern.
	Matrix matrix;

	/**
	 * Constructor used internally to instantiate a java Pattern object for a native
	 * {@code cairo_pattern_t} instance
	 * 
	 * @param address the memory address of the native {@code cairo_pattern_t}
	 *                instance
	 */
	public Pattern(MemorySegment address) {
		super(address);
		setDestroyFunc("cairo_pattern_destroy");
	}

	/**
	 * Checks whether an error has previously occurred for this pattern.
	 * 
	 * @return {@link Status#SUCCESS}, {@link Status#NO_MEMORY},
	 *         {@link Status#INVALID_MATRIX}, {@link Status#PATTERN_TYPE_MISMATCH},
	 *         or {@link Status#INVALID_MESH_CONSTRUCTION}.
	 * @since 1.0
	 */
	public Status status() {
		try {
			int result = (int) cairo_pattern_status.invoke(handle());
			return Status.of(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_pattern_status = Interop.downcallHandle("cairo_pattern_status",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Sets the mode to be used for drawing outside the area of a pattern. See
	 * {@link Extend} for details on the semantics of each extend strategy.
	 * <p>
	 * The default extend mode is {@link Extend#NONE} for surface patterns and
	 * {@link Extend#PAD} for gradient patterns.
	 * 
	 * @param extend a {@link Extend} describing how the area outside of the pattern
	 *               will be drawn
	 * @since 1.0
	 */
	public void setExtend(Extend extend) {
		try {
			cairo_pattern_set_extend.invoke(handle(), extend.value());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_pattern_set_extend = Interop.downcallHandle("cairo_pattern_set_extend",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT), false);

	/**
	 * Gets the current extend mode for a pattern. See {@link Extend} for details on
	 * the semantics of each extend strategy.
	 * 
	 * @return the current extend strategy used for drawing the pattern.
	 * @since 1.0
	 */
	public Extend getExtend() {
		try {
			int result = (int) cairo_pattern_get_extend.invoke(handle());
			return Extend.of(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_pattern_get_extend = Interop.downcallHandle("cairo_pattern_get_extend",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Sets the filter to be used for resizing when using this pattern. See
	 * {@link Filter} for details on each filter.
	 * <p>
	 * Note that you might want to control filtering even when you do not have an
	 * explicit {@link Pattern} object, (for example when using
	 * {@link Context#setSource(Surface, double, double)}). In these cases, it is
	 * convenient to use {@link Context#getSource()} to get access to the pattern
	 * that cairo creates implicitly. For example:
	 * 
	 * <pre>
	 * cr.setSourceSurface(image, x, y);
	 * pattern.setFilter(cr.getSource(), Filter.NEAREST);
	 * </pre>
	 * 
	 * @param filter a {@link Filter} describing the filter to use for resizing the
	 *               pattern
	 * @since 1.0
	 */
	public void setFilter(Filter filter) {
		try {
			cairo_pattern_set_filter.invoke(handle(), filter.value());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_pattern_set_filter = Interop.downcallHandle("cairo_pattern_set_filter",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT), false);

	/**
	 * Gets the current filter for a pattern. See {@link Filter} for details on each
	 * filter.
	 * 
	 * @return the current filter used for resizing the pattern.
	 * @since 1.0
	 */
	public Filter getFilter() {
		try {
			int result = (int) cairo_pattern_get_filter.invoke(handle());
			return Filter.of(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_pattern_get_filter = Interop.downcallHandle("cairo_pattern_get_filter",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Sets the pattern's transformation matrix to {@code matrix}. This matrix is a
	 * transformation from user space to pattern space.
	 * <p>
	 * When a pattern is first created it always has the identity matrix for its
	 * transformation matrix, which means that pattern space is initially identical
	 * to user space.
	 * <p>
	 * Important: Please note that the direction of this transformation matrix is
	 * from user space to pattern space. This means that if you imagine the flow
	 * from a pattern to user space (and on to device space), then coordinates in
	 * that flow will be transformed by the inverse of the pattern matrix.
	 * <p>
	 * For example, if you want to make a pattern appear twice as large as it does
	 * by default the correct code to use is:
	 * 
	 * <pre>
	 * Matrix matrix = Matrix.createScale(0.5, 0.5);
	 * pattern.setMatrix(matrix);
	 * </pre>
	 * 
	 * Meanwhile, using values of 2.0 rather than 0.5 in the code above would cause
	 * the pattern to appear at half of its default size.
	 * <p>
	 * Also, please note the discussion of the user-space locking semantics of
	 * {@link Context#setSource(Pattern)}.
	 * 
	 * @param matrix a {@link Matrix}
	 * @since 1.0
	 */
	public void setMatrix(Matrix matrix) {
		try {
			cairo_pattern_set_matrix.invoke(handle(), matrix == null ? MemorySegment.NULL : matrix.handle());
			this.matrix = matrix;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_pattern_set_matrix = Interop.downcallHandle("cairo_pattern_set_matrix",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Stores the pattern's transformation matrix into {@code matrix}.
	 * 
	 * @param matrix return value for the matrix
	 * @since 1.0
	 */
	public void getMatrix(Matrix matrix) {
		try {
			cairo_pattern_get_matrix.invoke(handle(), matrix == null ? MemorySegment.NULL : matrix.handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_pattern_get_matrix = Interop.downcallHandle("cairo_pattern_get_matrix",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Get the pattern's type. See {@link PatternType} for available types.
	 * 
	 * @return The type of pattern.
	 * @since 1.2
	 */
	public PatternType getType() {
		try {
			int result = (int) cairo_pattern_get_type.invoke(handle());
			return PatternType.of(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_pattern_get_type = Interop.downcallHandle("cairo_pattern_get_type",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);
}
