package org.freedesktop.cairo;

import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.ProxyInstance;

/**
 * The cairo drawing context.
 * <p>
 * Context is the main object used when drawing with cairo. To draw with cairo,
 * you create a Context, set the target surface, and drawing options for the
 * Context, create shapes with functions like {@link #moveTo(double, double)}
 * and {@link #lineTo(double, double)}, and then draw shapes with
 * {@link #stroke()} or {@link #fill()}.
 * <p>
 * Context's can be pushed to a stack via {@link #save()}. They may then safely
 * be changed, without losing the current state. Use {@link #restore()} to
 * restore to the saved state.
 * <p>
 * The functions with <i>text</i> in their name form cairo's <i>toy</i> text
 * API. The toy API takes UTF-8 encoded text and is limited in its functionality
 * to rendering simple left-to-right text with no advanced features. That means
 * for example that most complex scripts like Hebrew, Arabic, and Indic scripts
 * are out of question. No kerning or correct positioning of diacritical marks
 * either. The font selection is pretty limited too and doesn't handle the case
 * that the selected font does not cover the characters in the text. This set of
 * functions are really that, a toy text API, for testing and demonstration
 * purposes. Any serious application should avoid them.
 * <p>
 * The functions with <i>glyphs</i> in their name form cairo's <i>low-level</i>
 * text API. The low-level API relies on the user to convert text to a set of
 * glyph indexes and positions. This is a very hard problem and is best handled
 * by external libraries, like the pangocairo that is part of the
 * <a href="http://www.pango.org/">Pango</a> text layout and rendering library.
 * <p>
 * The tag functions ({@link #tagBegin(String, String)} and
 * {@link #tagEnd(String)}) provide the ability to specify hyperlinks and
 * document logical structure on supported backends. The following tags are
 * supported:
 * 
 * <ul>
 * <li><a href=
 * "https://www.cairographics.org/manual/cairo-Tags-and-Links.html#link">Link</a>
 * - Create a hyperlink
 * <li><a href=
 * "https://www.cairographics.org/manual/cairo-Tags-and-Links.html#dest">Destinations</a>
 * - Create a hyperlink destination
 * <li><a href=
 * "https://www.cairographics.org/manual/cairo-Tags-and-Links.html#doc-struct">Document
 * Structure Tags</a> - Create PDF Document Structure
 * </ul>
 * 
 * @since 1.0
 */
public final class Context extends ProxyInstance {

	static {
		Interop.ensureInitialized();
	}

	/**
	 * Create a destination for a hyperlink.
	 * 
	 * @see #tagBegin(String, String)
	 * @since 1.16
	 */
	public static final String TAG_DEST = "cairo.dest";

	/**
	 * Create hyperlink.
	 * 
	 * @see #tagBegin(String, String)
	 * @since 1.16
	 */
	public static final String TAG_LINK = "Link";

	// Keep a reference to natively allocated resources that are passed to the
	// Context during its lifetime.

	@SuppressWarnings("unused")
	private ProxyInstance source;

	@SuppressWarnings("unused")
	private ProxyInstance target;

	@SuppressWarnings("unused")
	private ProxyInstance mask;

	@SuppressWarnings("unused")
	private Matrix matrix;

	@SuppressWarnings("unused")
	private Matrix fontMatrix;

	@SuppressWarnings("unused")
	private ScaledFont scaledFont;

	/**
	 * Constructor used internally to instantiate a java Context object for a native
	 * {@code cairo_t} instance
	 * 
	 * @param address the memory address of the native {@code cairo_t} instance
	 */
	public Context(MemorySegment address) {
		super(address);
		setDestroyFunc("cairo_destroy");
	}

	/**
	 * Creates a new Context with all graphics state parameters set to default
	 * values and with target as a target surface. The target surface should be
	 * constructed with a backend-specific function such as
	 * {@link ImageSurface#create(Format, int, int)} (or any other backend surface
	 * {@code create()} variant).
	 * 
	 * @param target target surface for the context
	 * @return a newly allocated {@link Context}
	 * @throws IOException attempt to target a surface that does not support writing
	 * @since 1.0
	 */
	public static Context create(Surface target) throws IOException {
		Context context;
		try {
			MemorySegment result = (MemorySegment) cairo_create
					.invoke(target == null ? MemorySegment.NULL : target.handle());
			context = new Context(result);
			context.takeOwnership();
			context.target = target;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (context.status() == Status.WRITE_ERROR) {
			throw new IOException(context.status().toString());
		}
		if (context.status() == Status.NO_MEMORY) {
			throw new RuntimeException(context.status().toString());
		}
		return context;
	}

	private static final MethodHandle cairo_create = Interop.downcallHandle("cairo_create",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Checks whether an error has previously occurred for this context.
	 * 
	 * @return the current status of this context, see {@link Status}
	 * @since 1.0
	 */
	public Status status() {
		try {
			int result = (int) cairo_status.invoke(handle());
			return Status.of(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_status = Interop.downcallHandle("cairo_status",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Makes a copy of the current state of the context and saves it on an internal
	 * stack of saved states for the context. When {@link #restore()} is called, the
	 * context will be restored to the saved state. Multiple calls to
	 * {@code save()} and {@code restore()} can be nested; each call to
	 * {@code restore()} restores the state from the matching paired
	 * {@code save()}.
	 * <p>
	 * It isn't necessary to clear all saved states before a context is freed. If
	 * the reference count of a context drops to zero in response to a call to
	 * {@link #destroy()}, any saved states will be freed along with the context.
	 * 
	 * @return the context
	 * @since 1.0
	 */
	public Context save() {
		try {
			cairo_save.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_save = Interop.downcallHandle("cairo_save",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Restores the context to the state saved by a preceding call to
	 * {@link #save()} and removes that state from the stack of saved states.
	 * 
	 * @return the context
	 * @since 1.0
	 */
	public Context restore() {
		try {
			cairo_restore.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_restore = Interop.downcallHandle("cairo_restore",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Gets the target surface for the cairo context as passed to
	 * {@link #create(Surface)}.
	 * 
	 * @return the target surface.
	 * @since 1.0
	 */
	public Surface getTarget() {
		try {
			MemorySegment result = (MemorySegment) cairo_get_target.invoke(handle());
			Surface surface = new Surface(result);
			return surface.status() == Status.SUCCESS ? surface : null;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_target = Interop.downcallHandle("cairo_get_target",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Temporarily redirects drawing to an intermediate surface known as a group.
	 * The redirection lasts until the group is completed by a call to
	 * {@link #popGroup()} or {@link #popGroupToSource()}. These calls provide the
	 * result of any drawing to the group as a pattern, (either as an explicit
	 * object, or set as the source pattern).
	 * <p>
	 * This group functionality can be convenient for performing intermediate
	 * compositing. One common use of a group is to render objects as opaque within
	 * the group, (so that they occlude each other), and then blend the result with
	 * translucence onto the destination.
	 * <p>
	 * Groups can be nested arbitrarily deep by making balanced calls to
	 * {@code pushGroup()} /{@code popGroup()}. Each call pushes/pops the new
	 * target group onto/from a stack.
	 * <p>
	 * The {@code pushGroup()} function calls {@link #save()} so that any changes
	 * to the graphics state will not be visible outside the group, (the pop_group
	 * functions call {@link #restore()}).
	 * <p>
	 * By default the intermediate group will have a content type of
	 * {@link Content#COLOR_ALPHA}. Other content types can be chosen for the group
	 * by using {@link #pushGroupWithContent(Content)} instead.
	 * <p>
	 * As an example, here is how one might fill and stroke a path with
	 * translucence, but without any portion of the fill being visible under the
	 * stroke:
	 * 
	 * <pre>
	 * cr.pushGroup()
	 *   .setSource(fillPattern)
	 *   .fillPreserve()
	 *   .setSource(strokePattern)
	 *   .stroke()
	 *   .popGroupToSource()
	 *   .paintWithAlpha(alpha);
	 * </pre>
	 * 
	 * @return the context
	 * @since 1.2
	 */
	public Context pushGroup() {
		try {
			cairo_push_group.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_push_group = Interop.downcallHandle("cairo_push_group",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Temporarily redirects drawing to an intermediate surface known as a group.
	 * The redirection lasts until the group is completed by a call to
	 * {@link #popGroup()} or {@link #popGroupToSource()}. These calls provide the
	 * result of any drawing to the group as a pattern, (either as an explicit
	 * object, or set as the source pattern).
	 * <p>
	 * The group will have a content type of content . The ability to control this
	 * content type is the only distinction between this function and
	 * {@link #pushGroup()} which you should see for a more detailed description of
	 * group rendering.
	 * 
	 * @param content a {@link Content} indicating the type of group that will be
	 *                created
	 * @return the context
	 * @since 1.2
	 */
	public Context pushGroupWithContent(Content content) {
		try {
			cairo_push_group_with_content.invoke(handle(), content.value());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_push_group_with_content = Interop.downcallHandle(
			"cairo_push_group_with_content", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT),
			false);

	/**
	 * Terminates the redirection begun by a call to {@link #pushGroup()} or
	 * {@link #pushGroupWithContent(Content)} and returns a new pattern containing
	 * the results of all drawing operations performed to the group.
	 * <p>
	 * The {@code popGroup()} function calls {@link #restore()}, (balancing a call
	 * to {@link #save()} by the pushGroup function), so that any changes to the
	 * graphics state will not be visible outside the group.
	 * 
	 * @return a newly created (surface) pattern containing the results of all
	 *         drawing operations performed to the group.
	 * @since 1.2
	 */
	public SurfacePattern popGroup() {
		try {
			MemorySegment result = (MemorySegment) cairo_pop_group.invoke(handle());
			SurfacePattern pattern = new SurfacePattern(result);
			pattern.takeOwnership();
			return pattern;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_pop_group = Interop.downcallHandle("cairo_pop_group",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Terminates the redirection begun by a call to {@link #pushGroup()} or
	 * {@link #pushGroupWithContent(Content)} and installs the resulting pattern as
	 * the source pattern in the given cairo context.
	 * <p>
	 * The behavior of this function is equivalent to the sequence of operations:
	 * 
	 * <pre>
	 * SurfacePattern group = cr.popGroup();
	 * cr.setSource(group);
	 * </pre>
	 * 
	 * but is more convenient as there is no need for a variable to store the
	 * short-lived pointer to the pattern.
	 * <p>
	 * The {@link #popGroup()} function calls {@link #restore()}, (balancing a call
	 * to {@link #save()} by the pushGroup function), so that any changes to the
	 * graphics state will not be visible outside the group.
	 * 
	 * @return the context
	 * @since 1.2
	 */
	public Context popGroupToSource() {
		try {
			cairo_pop_group_to_source.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_pop_group_to_source = Interop.downcallHandle("cairo_pop_group_to_source",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Gets the current destination surface for the context. This is either the
	 * original target surface as passed to {@link #create(Surface)} or the target
	 * surface for the current group as started by the most recent call to
	 * {@link #pushGroup()} or {@link #pushGroupWithContent(Content)}.
	 * 
	 * @return the target surface
	 * @since 1.2
	 */
	public Surface getGroupTarget() {
		try {
			MemorySegment result = (MemorySegment) cairo_get_group_target.invoke(handle());
			Surface surface = new Surface(result);
			return surface.status() == Status.SUCCESS ? surface : null;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_group_target = Interop.downcallHandle("cairo_get_group_target",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Sets the source pattern within the context to an opaque color. This opaque
	 * color will then be used for any subsequent drawing operation until a new
	 * source pattern is set.
	 * <p>
	 * The color components are floating point numbers in the range 0 to 1. If the
	 * values passed in are outside that range, they will be clamped.
	 * <p>
	 * The default source pattern is opaque black, (that is, it is equivalent to
	 * {@code setSourceRGB(0.0, 0.0, 0.0)}).
	 * 
	 * @param red   red component of color
	 * @param green green component of color
	 * @param blue  blue component of color
	 * @return the context
	 * @since 1.0
	 */
	public Context setSourceRGB(double red, double green, double blue) {
		try {
			cairo_set_source_rgb.invoke(handle(), red, green, blue);
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_source_rgb = Interop.downcallHandle("cairo_set_source_rgb",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Sets the source pattern within the context to a translucent color. This color
	 * will then be used for any subsequent drawing operation until a new source
	 * pattern is set.
	 * <p>
	 * The color and alpha components are floating point numbers in the range 0 to
	 * 1. If the values passed in are outside that range, they will be clamped.
	 * <p>
	 * The default source pattern is opaque black, (that is, it is equivalent to
	 * {@code setSourceRGBA(0.0, 0.0, 0.0, 1.0)}).
	 * 
	 * @param red   red component of color
	 * @param green green component of color
	 * @param blue  blue component of color
	 * @param alpha alpha component of color
	 * @return the context
	 * @since 1.0
	 */
	public Context setSourceRGBA(double red, double green, double blue, double alpha) {
		try {
			cairo_set_source_rgba.invoke(handle(), red, green, blue, alpha);
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_source_rgba = Interop.downcallHandle("cairo_set_source_rgba",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Sets the source pattern within the context to source. This pattern will then
	 * be used for any subsequent drawing operation until a new source pattern is
	 * set.
	 * <p>
	 * Note: The pattern's transformation matrix will be locked to the user space in
	 * effect at the time of {@code setSource()}. This means that further
	 * modifications of the current transformation matrix will not affect the source
	 * pattern. See {@link Pattern#setMatrix(Matrix)}.
	 * <p>
	 * The default source pattern is a solid pattern that is opaque black, (that is,
	 * it is equivalent to {@code setSourceRGB(0.0, 0.0, 0.0)}).
	 * 
	 * @param source a Pattern to be used as the source for subsequent drawing
	 *               operations.
	 * @return the context
	 * @since 1.0
	 */
	public Context setSource(Pattern source) {
		try {
			cairo_set_source.invoke(handle(), source == null ? MemorySegment.NULL : source.handle());
			this.source = source;
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_source = Interop.downcallHandle("cairo_set_source",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * This is a convenience function for creating a pattern from surface and
	 * setting it as the source with {@link Context#setSource(Pattern)}.
	 * <p>
	 * The x and y parameters give the user-space coordinate at which the surface
	 * origin should appear. (The surface origin is its upper-left corner before any
	 * transformation has been applied.) The x and y parameters are negated and then
	 * set as translation values in the pattern matrix.
	 * <p>
	 * Other than the initial translation pattern matrix, as described above, all
	 * other pattern attributes, (such as its extend mode), are set to the default
	 * values as in {@link SurfacePattern#create(Surface)}. The resulting
	 * pattern can be queried with {@link #getSource()} so that these attributes can
	 * be modified if desired, (eg. to create a repeating pattern with
	 * {@link Pattern#setExtend(Extend)}).
	 * 
	 * @param surface a surface to be used to set the source pattern
	 * @param x       User-space X coordinate for surface origin
	 * @param y       User-space Y coordinate for surface origin
	 * @return the context
	 * @since 1.0
	 */
	public Context setSource(Surface surface, double x, double y) {
		try {
			cairo_set_source_surface.invoke(handle(), surface == null ? MemorySegment.NULL : surface.handle(), x, y);
			this.source = surface;
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_source_surface = Interop.downcallHandle("cairo_set_source_surface",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Gets the current source pattern.
	 * 
	 * @return the current source pattern.
	 * @since 1.0
	 */
	public SurfacePattern getSource() {
		try {
			MemorySegment result = (MemorySegment) cairo_get_source.invoke(handle());
			return new SurfacePattern(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_source = Interop.downcallHandle("cairo_get_source",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Set the antialiasing mode of the rasterizer used for drawing shapes. This
	 * value is a hint, and a particular backend may or may not support a particular
	 * value. At the current time, no backend supports {@link Antialias#SUBPIXEL}
	 * when drawing shapes.
	 * <p>
	 * Note that this option does not affect text rendering, instead see
	 * cairo_font_options_set_antialias().
	 * 
	 * @param antialias the new antialiasing mode
	 * @return the context
	 * @since 1.0
	 */
	public Context setAntialias(Antialias antialias) {
		try {
			cairo_set_antialias.invoke(handle(), antialias.value());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_antialias = Interop.downcallHandle("cairo_set_antialias",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT), false);

	/**
	 * Gets the current shape antialiasing mode, as set by
	 * {@link #setAntialias(Antialias)}.
	 * 
	 * @return the current shape antialiasing mode.
	 * @since 1.0
	 */
	public Antialias getAntialias() {
		try {
			int result = (int) cairo_get_antialias.invoke(handle());
			return Antialias.of(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_antialias = Interop.downcallHandle("cairo_get_antialias",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Sets the dash pattern to be used by {@link #stroke()}. A dash pattern is
	 * specified by dashes, an array of positive values. Each value provides the
	 * length of alternate "on" and "off" portions of the stroke. The offset
	 * specifies an offset into the pattern at which the stroke begins.
	 * <p>
	 * Each "on" segment will have caps applied as if the segment were a separate
	 * sub-path. In particular, it is valid to use an "on" length of 0.0 with
	 * {@link LineCap#ROUND} or {@link LineCap#SQUARE} in order to distributed dots
	 * or squares along a path.
	 * <p>
	 * Note: The length values are in user-space units as evaluated at the time of
	 * stroking. This is not necessarily the same as the user space at the time of
	 * {@code setDash()}.
	 * <p>
	 * If {@code dashes.length} is 0 dashing is disabled.
	 * <p>
	 * If {@code dashes.length} is 1 a symmetric pattern is assumed with alternating
	 * on and off portions of the size specified by the single value in dashes.
	 * 
	 * @param dash   an array specifying alternate lengths of on and off stroke
	 *               portions
	 * @param offset an offset into the dash pattern at which the stroke should
	 *               start
	 * @return the context
	 * @throws IllegalArgumentException if any value in dashes is negative, or if
	 *                                  all values are 0
	 * @since 1.0
	 */
	public Context setDash(double[] dash, double offset) throws IllegalArgumentException {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment dashesPtr = (dash == null || dash.length == 0) ? MemorySegment.NULL
						: arena.allocateArray(ValueLayout.JAVA_DOUBLE, dash);
				cairo_set_dash.invoke(handle(), dashesPtr, dash == null ? 0 : dash.length, offset);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (status() == Status.INVALID_DASH) {
			throw new IllegalArgumentException(Status.INVALID_DASH.toString());
		}
		return this;
	}

	private static final MethodHandle cairo_set_dash = Interop.downcallHandle("cairo_set_dash", FunctionDescriptor
			.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * This function returns the length of the dash array in the context (0 if
	 * dashing is not currently in effect).
	 * <p>
	 * See also {@link #setDash(double[], double)} and {@link #getDash()}.
	 * 
	 * @return the length of the dash array, or 0 if no dash array set.
	 * @since 1.4
	 */
	public int getDashCount() {
		try {
			return (int) cairo_get_dash_count.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_dash_count = Interop.downcallHandle("cairo_get_dash_count",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Gets the current dash array.
	 * <p>
	 * See also {@link #getDashOffset()}.
	 * 
	 * @return the dash array
	 * @since 1.4
	 */
	public double[] getDash() {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment dashesPtr = arena.allocate(ValueLayout.JAVA_DOUBLE.byteSize() * getDashCount());
				MemorySegment offsetPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				cairo_get_dash.invoke(handle(), dashesPtr, offsetPtr);
				return dashesPtr.toArray(ValueLayout.JAVA_DOUBLE);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the current offset into the dash array.
	 * <p>
	 * See also {@link #getDash()}
	 * 
	 * @return the offset
	 * @since 1.4
	 */
	public double getDashOffset() {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment dashesPtr = arena.allocate(ValueLayout.JAVA_DOUBLE.byteSize() * getDashCount());
				MemorySegment offsetPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				cairo_get_dash.invoke(handle(), dashesPtr, offsetPtr);
				return offsetPtr.get(ValueLayout.JAVA_DOUBLE, 0);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_dash = Interop.downcallHandle("cairo_get_dash",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Set the current fill rule within the cairo context. The fill rule is used to
	 * determine which regions are inside or outside a complex (potentially
	 * self-intersecting) path. The current fill rule affects both {@link #fill()}
	 * and {@link #clip()}. See {@link FillRule} for details on the semantics of
	 * each available fill rule.
	 * <p>
	 * The default fill rule is {@link FillRule#WINDING}.
	 * 
	 * @param fillRule a fill rule, specified as a {@link FillRule}
	 * @return the context
	 * @since 1.0
	 */
	public Context setFillRule(FillRule fillRule) {
		try {
			cairo_set_fill_rule.invoke(handle(), fillRule.value());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_fill_rule = Interop.downcallHandle("cairo_set_fill_rule",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT), false);

	/**
	 * Gets the current fill rule, as set by {@link #setFillRule(FillRule)}.
	 * 
	 * @return the current fill rule.
	 * @since 1.0
	 */
	public FillRule getFillRule() {
		try {
			int result = (int) cairo_get_fill_rule.invoke(handle());
			return FillRule.of(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_fill_rule = Interop.downcallHandle("cairo_get_fill_rule",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Sets the current line cap style within the cairo context. See {@link LineCap}
	 * for details about how the available line cap styles are drawn.
	 * <p>
	 * As with the other stroke parameters, the current line cap style is examined
	 * by {@link #stroke()} and {@link #strokeExtents()}, but does not have any
	 * effect during path construction.
	 * <p>
	 * The default line cap style is {@link LineCap#BUTT}.
	 * 
	 * @param lineCap a line cap style
	 * @return the context
	 * @since 1.0
	 */
	public Context setLineCap(LineCap lineCap) {
		try {
			cairo_set_line_cap.invoke(handle(), lineCap.value());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_line_cap = Interop.downcallHandle("cairo_set_line_cap",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT), false);

	/**
	 * Gets the current line cap style, as set by {@link #setLineCap(LineCap)}.
	 * 
	 * @return the current line cap style.
	 * @since 1.0
	 */
	public LineCap getLineCap() {
		try {
			int result = (int) cairo_get_line_cap.invoke(handle());
			return LineCap.of(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_line_cap = Interop.downcallHandle("cairo_get_line_cap",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Sets the current line join style within the cairo context. See
	 * {@link LineJoin} for details about how the available line join styles are
	 * drawn.
	 * <p>
	 * As with the other stroke parameters, the current line join style is examined
	 * by {@link #stroke()} and {@link #strokeExtents()}, but does not have any
	 * effect during path construction.
	 * <p>
	 * The default line join style is {@link LineJoin#MITER}.
	 * 
	 * @param lineJoin a line join style
	 * @return the context
	 * @since 1.0
	 */
	public Context setLineJoin(LineJoin lineJoin) {
		try {
			cairo_set_line_join.invoke(handle(), lineJoin.value());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_line_join = Interop.downcallHandle("cairo_set_line_join",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT), false);

	/**
	 * Gets the current line join style, as set by {@link #setLineJoin(LineJoin)}.
	 * 
	 * @return the current line join style.
	 * @since 1.0
	 */
	public LineJoin getLineJoin() {
		try {
			int result = (int) cairo_get_line_join.invoke(handle());
			return LineJoin.of(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_line_join = Interop.downcallHandle("cairo_get_line_join",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Sets the current line width within the cairo context. The line width value
	 * specifies the diameter of a pen that is circular in user space, (though
	 * device-space pen may be an ellipse in general due to scaling/shear/rotation
	 * of the CTM).
	 * <p>
	 * Note: When the description above refers to user space and CTM it refers to
	 * the user space and CTM in effect at the time of the stroking operation, not
	 * the user space and CTM in effect at the time of the call to
	 * {@code setLineWidth()}. The simplest usage makes both of these spaces
	 * identical. That is, if there is no change to the CTM between a call to
	 * {@code setLineWidth()} and the stroking operation, then one can just pass
	 * user-space values to {@code setLineWidth()} and ignore this note.
	 * <p>
	 * As with the other stroke parameters, the current line width is examined by
	 * {{@link #stroke()} and {@link #strokeExtents()}} but does not have any effect
	 * during path construction.
	 * <p>
	 * The default line width value is 2.0.
	 * 
	 * @param width a line width
	 * @return the context
	 * @since 1.0
	 */
	public Context setLineWidth(double width) {
		try {
			cairo_set_line_width.invoke(handle(), width);
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_line_width = Interop.downcallHandle("cairo_set_line_width",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * This function returns the current line width value exactly as set by
	 * {@link #setLineWidth(double)}. Note that the value is unchanged even if the
	 * CTM has changed between the calls to {@link #setLineWidth(double)} and
	 * {@code getLineWidth()}.
	 * 
	 * @return the current line width.
	 * @since 1.0
	 */
	public double getLineWidth() {
		try {
			return (double) cairo_get_line_width.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_line_width = Interop.downcallHandle("cairo_get_line_width",
			FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS), false);

	/**
	 * Sets the current miter limit within the cairo context.
	 * <p>
	 * If the current line join style is set to {@link LineJoin#MITER} (see
	 * {@link #setLineJoin(LineJoin)}), the miter limit is used to determine whether
	 * the lines should be joined with a bevel instead of a miter. Cairo divides the
	 * length of the miter by the line width. If the result is greater than the
	 * miter limit, the style is converted to a bevel.
	 * <p>
	 * As with the other stroke parameters, the current line miter limit is examined
	 * by {@link #stroke()} and {@link #strokeExtents()}, but does not have any
	 * effect during path construction.
	 * <p>
	 * The default miter limit value is 10.0, which will convert joins with interior
	 * angles less than 11 degrees to bevels instead of miters. For reference, a
	 * miter limit of 2.0 makes the miter cutoff at 60 degrees, and a miter limit of
	 * 1.414 makes the cutoff at 90 degrees.
	 * <p>
	 * A miter limit for a desired angle can be computed as: {@code miter limit =
	 * 1/sin(angle/2)}
	 * 
	 * @param limit miter limit to set
	 * @return the context
	 * @since 1.0
	 */
	public Context setMiterLimit(double limit) {
		try {
			cairo_set_miter_limit.invoke(handle(), limit);
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_miter_limit = Interop.downcallHandle("cairo_set_miter_limit",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Gets the current miter limit, as set by {@link #setMiterLimit(double)}.
	 * 
	 * @return the current miter limit.
	 * @since 1.0
	 */
	public double getMiterLimit() {
		try {
			return (double) cairo_get_miter_limit.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_miter_limit = Interop.downcallHandle("cairo_get_miter_limit",
			FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS), false);

	/**
	 * Sets the compositing operator to be used for all drawing operations. See
	 * {@link Operator} for details on the semantics of each available compositing
	 * operator.
	 * <p>
	 * The default operator is {@link Operator#OVER}.
	 * 
	 * @param op a compositing operator, specified as an {@link Operator}
	 * @return the context
	 * @since 1.0
	 */
	public Context setOperator(Operator op) {
		try {
			cairo_set_operator.invoke(handle(), op.value());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_operator = Interop.downcallHandle("cairo_set_operator",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT), false);

	/**
	 * Gets the current compositing operator for a cairo context.
	 * 
	 * @return the current compositing operator.
	 * @since 1.0
	 */
	public Operator getOperator() {
		try {
			int result = (int) cairo_get_operator.invoke(handle());
			return Operator.of(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_operator = Interop.downcallHandle("cairo_get_operator",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Sets the tolerance used when converting paths into trapezoids. Curved
	 * segments of the path will be subdivided until the maximum deviation between
	 * the original path and the polygonal approximation is less than tolerance .
	 * The default value is 0.1. A larger value will give better performance, a
	 * smaller value, better appearance. (Reducing the value from the default value
	 * of 0.1 is unlikely to improve appearance significantly.) The accuracy of
	 * paths within Cairo is limited by the precision of its internal arithmetic,
	 * and the prescribed tolerance is restricted to the smallest representable
	 * internal value.
	 * 
	 * @param tolerance the tolerance, in device units (typically pixels)
	 * @return the context
	 * @since 1.0
	 */
	public Context setTolerance(double tolerance) {
		try {
			cairo_set_tolerance.invoke(handle(), tolerance);
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_tolerance = Interop.downcallHandle("cairo_set_tolerance",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Gets the current tolerance value, as set by {@link #setTolerance(double)}.
	 * 
	 * @return the current tolerance value.
	 * @since 1.0
	 */
	public double getTolerance() {
		try {
			return (double) cairo_get_tolerance.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_tolerance = Interop.downcallHandle("cairo_get_tolerance",
			FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS), false);

	/**
	 * Establishes a new clip region by intersecting the current clip region with
	 * the current path as it would be filled by {@link #fill()} and according to
	 * the current fill rule (see {@link #setFillRule(FillRule)}).
	 * <p>
	 * After {@code clip()}, the current path will be cleared from the cairo
	 * context.
	 * <p>
	 * The current clip region affects all drawing operations by effectively masking
	 * out any changes to the surface that are outside the current clip region.
	 * <p>
	 * Calling {@code clip()} can only make the clip region smaller, never larger.
	 * But the current clip is part of the graphics state, so a temporary
	 * restriction of the clip region can be achieved by calling {@code clip()}
	 * within a {@link #save()}/{@link #restore()} pair. The only other means of
	 * increasing the size of the clip region is {@link #resetClip()}.
	 * 
	 * @return the context
	 * @since 1.0
	 */
	public Context clip() {
		try {
			cairo_clip.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_clip = Interop.downcallHandle("cairo_clip",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Establishes a new clip region by intersecting the current clip region with
	 * the current path as it would be filled by {@link #fill()} and according to
	 * the current fill rule (see {@link #setFillRule(FillRule)}).
	 * <p>
	 * Unlike {@link #clip()}, {@code clipPreserve()} preserves the path within the
	 * cairo context.
	 * <p>
	 * The current clip region affects all drawing operations by effectively masking
	 * out any changes to the surface that are outside the current clip region.
	 * <p>
	 * Calling {@code clipPreserve()} can only make the clip region smaller, never
	 * larger. But the current clip is part of the graphics state, so a temporary
	 * restriction of the clip region can be achieved by calling
	 * {@code clipPreserve()} within a {@link #save()}/{@link #restore()} pair. The
	 * only other means of increasing the size of the clip region is
	 * {@link #resetClip()}.
	 * 
	 * @return the context
	 * @since 1.0
	 */
	public Context clipPreserve() {
		try {
			cairo_clip_preserve.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_clip_preserve = Interop.downcallHandle("cairo_clip_preserve",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Computes a bounding box in user coordinates covering the area inside the
	 * current clip.
	 * <p>
	 * 
	 * @return the resulting extents
	 * @since 1.4
	 */
	public Rectangle clipExtents() {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment x1Ptr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment y1Ptr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment x2Ptr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment y2Ptr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				cairo_clip_extents.invoke(handle(), x1Ptr, y1Ptr, x2Ptr, y2Ptr);
				return Rectangle.create(x1Ptr.get(ValueLayout.JAVA_DOUBLE, 0), y1Ptr.get(ValueLayout.JAVA_DOUBLE, 0),
						x2Ptr.get(ValueLayout.JAVA_DOUBLE, 0), y2Ptr.get(ValueLayout.JAVA_DOUBLE, 0));
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_clip_extents = Interop.downcallHandle("cairo_clip_extents",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
					ValueLayout.ADDRESS, ValueLayout.ADDRESS),
			false);

	/**
	 * Tests whether the given point is inside the area that would be visible
	 * through the current clip, i.e. the area that would be filled by a
	 * {@link #paint()} operation.
	 * 
	 * @param x X coordinate of the point to test
	 * @param y Y coordinate of the point to test
	 * @return True if the point is inside, false if outside.
	 * @see #clip()
	 * @see #clipPreserve()
	 * @since 1.10
	 */
	public boolean inClip(double x, double y) {
		try {
			int result = (int) cairo_in_clip.invoke(handle(), x, y);
			return result != 0;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_in_clip = Interop.downcallHandle("cairo_in_clip", FunctionDescriptor
			.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Reset the current clip region to its original, unrestricted state. That is,
	 * set the clip region to an infinitely large shape containing the target
	 * surface. Equivalently, if infinity is too hard to grasp, one can imagine the
	 * clip region being reset to the exact bounds of the target surface.
	 * <p>
	 * Note that code meant to be reusable should not call {@code resetClip()} as
	 * it will cause results unexpected by higher-level code which {@link #clip()}.
	 * Consider using {@link #save()} and {@link #restore()} around {@link #clip()}
	 * as a more robust means of temporarily restricting the clip region.
	 * 
	 * @return the context
	 * @since 1.0
	 */
	public Context resetClip() {
		try {
			cairo_reset_clip.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_reset_clip = Interop.downcallHandle("cairo_reset_clip",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Gets the current clip region as a list of rectangles in user coordinates.
	 * Never returns {@code null}.
	 * 
	 * @return the current clip region as a list of rectangles in user coordinates
	 * @throws IllegalStateException if the clip region cannot be represented as a
	 *                               list of user-space rectangles. The exception
	 *                               may also indicate other errors.
	 * @since 1.4
	 */
	public RectangleList copyClipRectangleList() throws IllegalStateException {
		RectangleList list;
		try {
			MemorySegment result = (MemorySegment) cairo_copy_clip_rectangle_list.invoke(handle());
			list = new RectangleList(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (list.status() != Status.SUCCESS) {
			throw new IllegalStateException(list.status().toString());
		}
		return list;
	}

	private static final MethodHandle cairo_copy_clip_rectangle_list = Interop.downcallHandle(
			"cairo_copy_clip_rectangle_list", FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded(), ValueLayout.ADDRESS), false);

	/**
	 * A drawing operator that fills the current path according to the current fill
	 * rule, (each sub-path is implicitly closed before being filled). After
	 * {@code fill()}, the current path will be cleared from the cairo context. See
	 * {@link #setFillRule(FillRule)} and {@link #fillPreserve()}.
	 * 
	 * @return the context
	 * @since 1.0
	 */
	public Context fill() {
		try {
			cairo_fill.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_fill = Interop.downcallHandle("cairo_fill",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * A drawing operator that fills the current path according to the current fill
	 * rule, (each sub-path is implicitly closed before being filled). Unlike
	 * {@link #fill()}, {@code fillPreserve()} preserves the path within the cairo
	 * context.
	 * 
	 * @return the context
	 * @see #setFillRule(FillRule)
	 * @see #fill()
	 * @since 1.0
	 */
	public Context fillPreserve() {
		try {
			cairo_fill_preserve.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_fill_preserve = Interop.downcallHandle("cairo_fill_preserve",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Computes a bounding box in user coordinates covering the area that would be
	 * affected, (the "inked" area), by a {@link #fill()} operation given the
	 * current path and fill parameters. If the current path is empty, returns an
	 * empty rectangle ({@code (0,0), (0,0)}). Surface dimensions and clipping are
	 * not taken into account.
	 * <p>
	 * Contrast with {@link #pathExtents(double, double, double, double)}, which is
	 * similar, but returns non-zero extents for some paths with no inked area,
	 * (such as a simple line segment).
	 * <p>
	 * Note that {@code fillExtents()} must necessarily do more work to compute the
	 * precise inked areas in light of the fill rule, so {@code pathExtents()} may
	 * be more desirable for sake of performance if the non-inked path extents are
	 * desired.
	 * 
	 * @return The resulting extents
	 * @see #fill()
	 * @see #setFillRule(FillRule)
	 * @see #fillPreserve()
	 * @since 1.0
	 */
	public Rectangle fillExtents() {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment x1Ptr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment y1Ptr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment x2Ptr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment y2Ptr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				cairo_fill_extents.invoke(handle(), x1Ptr, y1Ptr, x2Ptr, y2Ptr);
				return Rectangle.create(x1Ptr.get(ValueLayout.JAVA_DOUBLE, 0), y1Ptr.get(ValueLayout.JAVA_DOUBLE, 0),
						x2Ptr.get(ValueLayout.JAVA_DOUBLE, 0), y2Ptr.get(ValueLayout.JAVA_DOUBLE, 0));
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_fill_extents = Interop.downcallHandle("cairo_fill_extents",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
					ValueLayout.ADDRESS, ValueLayout.ADDRESS),
			false);

	/**
	 * Tests whether the given point is inside the area that would be affected by a
	 * {@link #fill()} operation given the current path and filling parameters.
	 * Surface dimensions and clipping are not taken into account.
	 * 
\	 * @param x X coordinate of the point to test
	 * @param y Y coordinate of the point to test
	 * @return true if the point is inside, or false if outside.
	 * @see #fill()
	 * @see #setFillRule(FillRule)
	 * @see #fillPreserve()
	 * @since 1.0
	 */
	public boolean inFill(double x, double y) {
		try {
			int result = (int) cairo_in_fill.invoke(handle(), x, y);
			return result != 0;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_in_fill = Interop.downcallHandle("cairo_in_fill", FunctionDescriptor
			.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * A drawing operator that paints the current source using the alpha channel of
	 * pattern as a mask. (Opaque areas of pattern are painted with the source,
	 * transparent areas are not painted.)
	 * 
	 * @param pattern a {@link Pattern}
	 * @return the context
	 * @since 1.0
	 */
	public Context mask(Pattern pattern) {
		try {
			cairo_mask.invoke(handle(), pattern == null ? MemorySegment.NULL : pattern.handle());
			this.mask = pattern;
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_mask = Interop.downcallHandle("cairo_mask",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * A drawing operator that paints the current source using the alpha channel of
	 * surface as a mask. (Opaque areas of surface are painted with the source,
	 * transparent areas are not painted.)
	 * 
	 * @param surface  a {@link Surface}
	 * @param surfaceX X coordinate at which to place the origin of surface
	 * @param surfaceY Y coordinate at which to place the origin of surface
	 * @return the context
	 * @since 1.0
	 */
	public Context mask(Surface surface, double surfaceX, double surfaceY) {
		try {
			cairo_mask_surface.invoke(handle(), surface == null ? MemorySegment.NULL : surface.handle(), surfaceX,
					surfaceY);
			this.mask = surface;
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_mask_surface = Interop.downcallHandle("cairo_mask_surface",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * A drawing operator that paints the current source everywhere within the
	 * current clip region.
	 * 
	 * @return the context
	 * @since 1.0
	 */
	public Context paint() {
		try {
			cairo_paint.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_paint = Interop.downcallHandle("cairo_paint",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * A drawing operator that paints the current source everywhere within the
	 * current clip region using a mask of constant alpha value {@code alpha}. The
	 * effect is similar to {@link #paint()}, but the drawing is faded out using the
	 * alpha value.
	 * 
	 * @param alpha alpha value, between 0 (transparent) and 1 (opaque)
	 * @return the context
	 * @since 1.0
	 */
	public Context paintWithAlpha(double alpha) {
		try {
			cairo_paint_with_alpha.invoke(handle(), alpha);
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_paint_with_alpha = Interop.downcallHandle("cairo_paint_with_alpha",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * A drawing operator that strokes the current path according to the current
	 * line width, line join, line cap, and dash settings. After {@code stroke()},
	 * the current path will be cleared from the cairo context.
	 * <p>
	 * Note: Degenerate segments and sub-paths are treated specially and provide a
	 * useful result. These can result in two different situations:
	 * <ol>
	 * <li>Zero-length "on" segments set in {@link #setDash(double[], double)}. If
	 * the cap style is {@link LineCap#ROUND} or {@link LineCap#SQUARE} then these
	 * segments will be drawn as circular dots or squares respectively. In the case
	 * of {@link LineCap#SQUARE}, the orientation of the squares is determined by
	 * the direction of the underlying path.
	 * <li>A sub-path created by {@link #moveTo(double, double)} followed by either
	 * a {@link #closePath()} or one or more calls to
	 * {@link #lineTo(double, double)} to the same coordinate as the
	 * {@link #moveTo(double, double)}. If the cap style is {@link LineCap#ROUND}
	 * then these sub-paths will be drawn as circular dots. Note that in the case of
	 * {@link LineCap#SQUARE} a degenerate sub-path will not be drawn at all, (since
	 * the correct orientation is indeterminate).
	 * </ol>
	 * In no case will a cap style of {@link LineCap#BUTT} cause anything to be
	 * drawn in the case of either degenerate segments or sub-paths.
	 * 
	 * @return the context
	 * @see #setLineWidth(double)
	 * @see #setLineJoin(LineJoin)
	 * @see #setLineCap(LineCap)
	 * @see #setDash(double[], double)
	 * @see #strokePreserve()
	 * @since 1.0
	 */
	public Context stroke() {
		try {
			cairo_stroke.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_stroke = Interop.downcallHandle("cairo_stroke",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * A drawing operator that strokes the current path according to the current
	 * line width, line join, line cap, and dash settings. Unlike {@link #stroke()},
	 * {@code strokePreserve()} preserves the path within the cairo context.
	 * 
	 * @return the context
	 * @see #setLineWidth(double)
	 * @see #setLineJoin(LineJoin)
	 * @see #setLineCap(LineCap)
	 * @see #setDash(double[], double)
	 * @see #stroke()
	 * @since 1.0
	 */
	public Context strokePreserve() {
		try {
			cairo_stroke_preserve.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_stroke_preserve = Interop.downcallHandle("cairo_stroke_preserve",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Computes a bounding box in user coordinates covering the area that would be
	 * affected, (the "inked" area), by a {@link #stroke()} operation given the
	 * current path and stroke parameters. If the current path is empty, returns an
	 * empty rectangle ({@code (0,0), (0,0)}). Surface dimensions and clipping are
	 * not taken into account.
	 * <p>
	 * Note that if the line width is set to exactly zero, then
	 * {@code strokeExtents()} will return an empty rectangle. Contrast with
	 * {@link #pathExtents(double, double, double, double)} which can be used to
	 * compute the non-empty bounds as the line width approaches zero.
	 * <p>
	 * Note that {@code strokeExtents()} must necessarily do more work to compute
	 * the precise inked areas in light of the stroke parameters, so
	 * {@code pathExtents()} may be more desirable for sake of performance if
	 * non-inked path extents are desired.
	 *
	 * @return The resulting extents
	 * @see #stroke()
	 * @see #setLineWidth(double)
	 * @see #setLineJoin(LineJoin)
	 * @see #setLineCap(LineCap)
	 * @see #setDash(double[], double)
	 * @since 1.0
	 */
	public Rectangle strokeExtents() {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment x1Ptr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment y1Ptr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment x2Ptr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment y2Ptr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				cairo_stroke_extents.invoke(handle(), x1Ptr, y1Ptr, x2Ptr, y2Ptr);
				return Rectangle.create(x1Ptr.get(ValueLayout.JAVA_DOUBLE, 0), y1Ptr.get(ValueLayout.JAVA_DOUBLE, 0),
						x2Ptr.get(ValueLayout.JAVA_DOUBLE, 0), y2Ptr.get(ValueLayout.JAVA_DOUBLE, 0));
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_stroke_extents = Interop.downcallHandle("cairo_stroke_extents",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
					ValueLayout.ADDRESS, ValueLayout.ADDRESS),
			false);

	/**
	 * Tests whether the given point is inside the area that would be affected by a
	 * {@link #stroke()} operation given the current path and stroking parameters.
	 * Surface dimensions and clipping are not taken into account.
	 * 
	 * @param x X coordinate of the point to test
	 * @param y Y coordinate of the point to test
	 * @return true if the point is inside, or false if outside.
	 * @see #stroke()
	 * @see #setLineWidth(double)
	 * @see #setLineJoin(LineJoin)
	 * @see #setLineCap(LineCap)
	 * @see #setDash(double[], double)
	 * @see #strokePreserve()
	 * @since 1.0
	 */
	public boolean inStroke(double x, double y) {
		try {
			int result = (int) cairo_in_stroke.invoke(handle(), x, y);
			return result != 0;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_in_stroke = Interop.downcallHandle("cairo_in_stroke", FunctionDescriptor
			.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Emits the current page for backends that support multiple pages, but doesn't
	 * clear it, so, the contents of the current page will be retained for the next
	 * page too. Use {@link #showPage()} if you want to get an empty page after the
	 * emission.
	 * <p>
	 * This is a convenience function that simply calls {@link Surface#copyPage()}
	 * on this Context's target.
	 * 
	 * @return the context
	 * @since 1.0
	 */
	public Context copyPage() {
		try {
			cairo_copy_page.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_copy_page = Interop.downcallHandle("cairo_copy_page",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Emits and clears the current page for backends that support multiple pages.
	 * Use {@link #copyPage()} if you don't want to clear the page.
	 * <p>
	 * This is a convenience function that simply calls {@link Surface#showPage()}
	 * on this Context's target.
	 * 
	 * @return the context
	 * @since 1.0
	 */
	public Context showPage() {
		try {
			cairo_show_page.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_show_page = Interop.downcallHandle("cairo_show_page",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Creates a copy of the current path and returns it to the user as a
	 * {@link Path}. See {@link PathData} for hints on how to iterate over the
	 * returned data structure.
	 * 
	 * @return the copy of the current path, or {@code null} when the Context is in
	 *         an error state, or there is insufficient memory to copy the path.
	 * @since 1.0
	 */
	public Path copyPath() {
		try {
			MemorySegment result = (MemorySegment) cairo_copy_path.invoke(handle());
			Path path = new Path(result);
			path.takeOwnership();
			if (path.status() != Status.SUCCESS) {
				return null;
			}
			return path;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_copy_path = Interop.downcallHandle("cairo_copy_path",
			FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded(), ValueLayout.ADDRESS), false);

	/**
	 * Gets a flattened copy of the current path and returns it to the user as a
	 * {@link Path}. See {@link PathData} for hints on how to iterate over the
	 * returned data structure.
	 * <p>
	 * This function is like {@link #copyPath()} except that any curves in the path
	 * will be approximated with piecewise-linear approximations, (accurate to
	 * within the current tolerance value). That is, the result is guaranteed to not
	 * have any elements of type {@link PathDataType#CURVE_TO} which will instead be
	 * replaced by a series of {@link PathDataType#LINE_TO} elements.
	 * 
	 * @return the copy of the current path, or {@code null} when the Context is in
	 *         an error state, or there is insufficient memory to copy the path.
	 * @since 1.0
	 */
	public Path copyPathFlat() {
		try {
			MemorySegment result = (MemorySegment) cairo_copy_path_flat.invoke(handle());
			Path path = new Path(result);
			path.takeOwnership();
			if (path.status() != Status.SUCCESS) {
				return null;
			}
			return path;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_copy_path_flat = Interop.downcallHandle("cairo_copy_path_flat",
			FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded(), ValueLayout.ADDRESS), false);

	/**
	 * Append the path onto the current path. The path may be either the return
	 * value from one of {@link #copyPath} or {@link #copyPathFlat()} or it may be
	 * constructed manually. See {@link Path} for details on how the path data
	 * structure should be initialized, and note that {@code path.status} must be
	 * initialized to {@link Status#SUCCESS}.
	 * 
	 * @param path path to be appended
	 * @return the context
	 * @since 1.0
	 */
	public Context appendPath(Path path) {
		try {
			cairo_append_path.invoke(handle(), path == null ? MemorySegment.NULL : path.handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_append_path = Interop.downcallHandle("cairo_append_path",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Returns whether a current point is defined on the current path. See
	 * {@link #getCurrentPoint()} for details on the current point.
	 * 
	 * @return whether a current point is defined.
	 * @since 1.6
	 */
	public boolean hasCurrentPoint() {
		try {
			int result = (int) cairo_has_current_point.invoke(handle());
			return result != 0;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_has_current_point = Interop.downcallHandle("cairo_has_current_point",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Gets the current point of the current path, which is conceptually the final
	 * point reached by the path so far.
	 * <p>
	 * The current point is returned in the user-space coordinate system. If there
	 * is no defined current point or if the Context is in an error status, x and y
	 * will both be set to 0.0. It is possible to check this in advance with
	 * {@link #hasCurrentPoint()}.
	 * <p>
	 * Most path construction functions alter the current point. See the following
	 * for details on how they affect the current point: {@link #newPath()},
	 * {@link #newSubPath()}, {@link #appendPath(Path)}, {@link #closePath()},
	 * {@link #moveTo(double, double)}, {@link #lineTo(double, double)},
	 * {@link #curveTo(double, double, double, double, double, double)},
	 * {@link #relMoveTo(double, double)}, {@link #relLineTo(double, double)},
	 * {@link #relCurveTo(double, double, double, double, double, double)},
	 * {@link #arc(double, double, double, double, double)},
	 * {@link #arcNegative(double, double, double, double, double)},
	 * {@link #rectangle(double, double, double, double)},
	 * {@link #textPath(String)}, {@link #glyphPath(Glyph[])},
	 * cairo_stroke_to_path().
	 * <p>
	 * Some functions use and alter the current point but do not otherwise change
	 * current path: cairo_show_text().
	 * <p>
	 * Some functions unset the current path and as a result, current point:
	 * {@link #fill()}, {@link #stroke()}.
	 * 
	 * @return X and Y coordinates of the current point
	 * @since 1.0
	 */
	public Point getCurrentPoint() {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment xPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment yPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				cairo_get_current_point.invoke(handle(), xPtr, yPtr);
				double x = xPtr.get(ValueLayout.JAVA_DOUBLE, 0);
				double y = yPtr.get(ValueLayout.JAVA_DOUBLE, 0);
				return new Point(x, y);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_current_point = Interop.downcallHandle("cairo_get_current_point",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Clears the current path. After this call there will be no path and no current
	 * point.
	 * 
	 * @return the context
	 * @since 1.0
	 */
	public Context newPath() {
		try {
			cairo_new_path.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_new_path = Interop.downcallHandle("cairo_new_path",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Begin a new sub-path. Note that the existing path is not affected. After this
	 * call there will be no current point.
	 * <p>
	 * In many cases, this call is not needed since new sub-paths are frequently
	 * started with {@link #moveTo(double, double)}.
	 * <p>
	 * A call to {@code newSubPath()} is particularly useful when beginning a new
	 * sub-path with one of the {@code arc()} calls. This makes things easier as it
	 * is no longer necessary to manually compute the arc's initial coordinates for
	 * a call to {@link #moveTo(double, double)}.
	 * 
	 * @return the context
	 * @since 1.2
	 */
	public Context newSubPath() {
		try {
			cairo_new_sub_path.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_new_sub_path = Interop.downcallHandle("cairo_new_sub_path",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Adds a line segment to the path from the current point to the beginning of
	 * the current sub-path, (the most recent point passed to
	 * {@link #moveTo(double, double)}), and closes this sub-path. After this call
	 * the current point will be at the joined endpoint of the sub-path.
	 * <p>
	 * The behavior of {@code closePath()} is distinct from simply calling
	 * {@link #lineTo(double, double)} with the equivalent coordinate in the case of
	 * stroking. When a closed sub-path is stroked, there are no caps on the ends of
	 * the sub-path. Instead, there is a line join connecting the final and initial
	 * segments of the sub-path.
	 * <p>
	 * If there is no current point before the call to {@code closePath()}, this
	 * function will have no effect.
	 * <p>
	 * Note: As of cairo version 1.2.4 any call to {@code closePath()} will place
	 * an explicit {@code MOVE_TO} element into the path immediately after the
	 * {@code CLOSE_PATH} element, (which can be seen in {@link #copyPath()} for
	 * example). This can simplify path processing in some cases as it may not be
	 * necessary to save the "last move_to point" during processing as the MOVE_TO
	 * immediately after the CLOSE_PATH will provide that point.
	 * 
	 * @return the context
	 * @since 1.0
	 */
	public Context closePath() {
		try {
			cairo_close_path.invoke(handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_close_path = Interop.downcallHandle("cairo_close_path",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Adds a circular arc of the given radius to the current path. The arc is
	 * centered at ({@code xc}, {@code yc}), begins at {@code angle1} and proceeds
	 * in the direction of increasing angles to end at {@code angle2}. If
	 * {@code angle2} is less than {@code angle1} it will be progressively increased
	 * by 2*M_PI until it is greater than {@code angle2}.
	 * <p>
	 * If there is a current point, an initial line segment will be added to the
	 * path to connect the current point to the beginning of the arc. If this
	 * initial line is undesired, it can be avoided by calling {@link #newSubPath()}
	 * before calling {@code arc()}.
	 * <p>
	 * Angles are measured in radians. An angle of 0.0 is in the direction of the
	 * positive X axis (in user space). An angle of M_PI/2.0 radians (90 degrees) is
	 * in the direction of the positive Y axis (in user space). Angles increase in
	 * the direction from the positive X axis toward the positive Y axis. So with
	 * the default transformation matrix, angles increase in a clockwise direction.
	 * <p>
	 * (To convert from degrees to radians, use degrees * (M_PI / 180.).)
	 * <p>
	 * This function gives the arc in the direction of increasing angles; see
	 * {@link #arcNegative(double, double, double, double, double)} to get the arc
	 * in the direction of decreasing angles.
	 * <p>
	 * The arc is circular in user space. To achieve an elliptical arc, you can
	 * scale the current transformation matrix by different amounts in the X and Y
	 * directions. For example, to draw an ellipse in the box given by
	 * {@code x, y, width, height}:
	 * 
	 * <pre>
	 * cr.save()
	 *   .translate(x + width / 2., y + height / 2.)
	 *   .scale(width / 2., height / 2.)
	 * 	 .arc(0., 0., 1., 0., 2 * Math.PI)
	 *   .restore();
	 * </pre>
	 * 
	 * @param xc     X position of the center of the arc
	 * @param yc     Y position of the center of the arc
	 * @param radius the radius of the arc
	 * @param angle1 the start angle, in radians
	 * @param angle2 the end angle, in radians
	 * @return the context
	 * @since 1.0
	 */
	public Context arc(double xc, double yc, double radius, double angle1, double angle2) {
		try {
			cairo_arc.invoke(handle(), xc, yc, radius, angle1, angle2);
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_arc = Interop.downcallHandle(
			"cairo_arc", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Adds a circular arc of the given radius to the current path. The arc is
	 * centered at ({@code xc}, {@code yc}), begins at {@code angle1} and proceeds
	 * in the direction of decreasing angles to end at {@code angle2}. If
	 * {@code angle2} is greater than {@code angle1} it will be progressively
	 * decreased by 2*M_PI until it is less than {@code angle1}.
	 * <p>
	 * See {@link #arc(double, double, double, double, double)} for more details.
	 * This function differs only in the direction of the arc between the two
	 * angles.
	 * 
	 * @param xc     X position of the center of the arc
	 * @param yc     Y position of the center of the arc
	 * @param radius the radius of the arc
	 * @param angle1 the start angle, in radians
	 * @param angle2 the end angle, in radians
	 * @return the context
	 * @since 1.0
	 */
	public Context arcNegative(double xc, double yc, double radius, double angle1, double angle2) {
		try {
			cairo_arc_negative.invoke(handle(), xc, yc, radius, angle1, angle2);
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_arc_negative = Interop.downcallHandle(
			"cairo_arc_negative", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Adds a cubic Bézier spline to the path from the current point to position
	 * {@code (x3, y3)} in user-space coordinates, using {@code (x1 , y1)} and
	 * {@code (x2, y2)} as the control points. After this call the current point
	 * will be {@code (x3, y3)}.
	 * <p>
	 * If there is no current point before the call to {@code curveTo()} this
	 * function will behave as if preceded by a call to {@code moveTo(x1, y1)}.
	 * 
	 * @param x1 the X coordinate of the first control point
	 * @param y1 the Y coordinate of the first control point
	 * @param x2 the X coordinate of the second control point
	 * @param y2 the Y coordinate of the second control point
	 * @param x3 the X coordinate of the end of the curve
	 * @param y3 the Y coordinate of the end of the curve
	 * @return the context
	 * @since 1.0
	 */
	public Context curveTo(double x1, double y1, double x2, double y2, double x3, double y3) {
		try {
			cairo_curve_to.invoke(handle(), x1, y1, x2, y2, x3, y3);
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_curve_to = Interop.downcallHandle("cairo_curve_to",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Adds a line to the path from the current point to position {@code (x, y)} in
	 * user-space coordinates. After this call the current point will be
	 * {@code (x, y)}.
	 * <p>
	 * If there is no current point before the call to {@code lineTo()} this
	 * function will behave as {@code moveTo(x, y)}.
	 * 
	 * @param x the X coordinate of the end of the new line
	 * @param y the Y coordinate of the end of the new line
	 * @return the context
	 * @since 1.0
	 */
	public Context lineTo(double x, double y) {
		try {
			cairo_line_to.invoke(handle(), x, y);
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_line_to = Interop.downcallHandle("cairo_line_to",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Begin a new sub-path. After this call the current point will be ({@code x},
	 * {@code y}).
	 * 
	 * @param x the X coordinate of the new position
	 * @param y the Y coordinate of the new position
	 * @return the context
	 * @since 1.0
	 */
	public Context moveTo(double x, double y) {
		try {
			cairo_move_to.invoke(handle(), x, y);
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_move_to = Interop.downcallHandle("cairo_move_to",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Adds a closed sub-path rectangle of the given size to the current path at
	 * position ({@code x}, {@code y}) in user-space coordinates.
	 * <p>
	 * This function is logically equivalent to:
	 * 
	 * <pre>
	 * cr.moveTo(x, y)
	 *   .relLineTo(width, 0)
	 *   .relLineTo(0, height)
	 *   .relLineTo(-width, 0)
	 *   .closePath();
	 * </pre>
	 * 
	 * @param x      the X coordinate of the top left corner of the rectangle
	 * @param y      the Y coordinate of the top left corner of the rectangle
	 * @param width  the width of the rectangle
	 * @param height the height of the rectangle
	 * @return the context
	 * @since 1.0
	 */
	public Context rectangle(double x, double y, double width, double height) {
		try {
			cairo_rectangle.invoke(handle(), x, y, width, height);
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_rectangle = Interop.downcallHandle("cairo_rectangle",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Adds closed paths for the glyphs to the current path. The generated path if
	 * filled, achieves an effect similar to that of {@link #showGlyphs(Glyph[])}.
	 * 
	 * @param glyphs array of glyphs to show
	 * @return the context
	 * @since 1.0
	 */
	public Context glyphPath(Glyph[] glyphs) {
		if (glyphs == null || glyphs.length == 0) {
			return this;
		}
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment glyphsPtr = arena.allocateArray(ValueLayout.ADDRESS, glyphs.length);
				for (int i = 0; i < glyphs.length; i++) {
					glyphsPtr.setAtIndex(ValueLayout.ADDRESS, i, glyphs[i].handle());
				}
				cairo_glyph_path.invoke(handle(), glyphsPtr, glyphs.length);
				return this;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_glyph_path = Interop.downcallHandle("cairo_glyph_path",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT), false);

	/**
	 * Adds closed paths for text to the current path. The generated path if filled,
	 * achieves an effect similar to that of {@link #showText(String)}.
	 * <p>
	 * Text conversion and positioning is done similar to {@link #showText(String)}.
	 * <p>
	 * Like {@link #showText(String)}, After this call the current point is moved to
	 * the origin of where the next glyph would be placed in this same progression.
	 * That is, the current point will be at the origin of the final glyph offset by
	 * its advance values. This allows for chaining multiple calls to to
	 * {@code textPath()} without having to set current point in between.
	 * <p>
	 * <strong>Note:</strong> The {@code textPath()} function call is part of
	 * what the cairo designers call the "toy" text API. It is convenient for short
	 * demos and simple programs, but it is not expected to be adequate for serious
	 * text-using applications. See {@link #glyphPath(Glyph[])} for the "real" text
	 * path API in cairo.
	 * 
	 * @param string a string of text
	 * @return the context
	 * @since 1.0
	 */
	public Context textPath(String string) {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment utf8 = Interop.allocateNativeString(string, arena);
				cairo_text_path.invoke(handle(), utf8);
				return this;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_text_path = Interop.downcallHandle("cairo_text_path",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Relative-coordinate version of
	 * {@link #curveTo(double, double, double, double, double, double)}. All offsets
	 * are relative to the current point. Adds a cubic Bézier spline to the path
	 * from the current point to a point offset from the current point by
	 * {@code (dx3, dy3)}, using points offset by {@code (dx1, dy1)} and
	 * {@code (dx2, dy2)} as the control points. After this call the current point
	 * will be offset by {@code (dx3, dy3)}.
	 * <p>
	 * Given a current point of ({@code x}, {@code y}),
	 * {@code relCurveTo(dx1, dy1, dx2, dy2, dx3, dy3)} is logically equivalent to
	 * {@code curveTo(x+dx1, y+dy1, x+dx2, y+dy2, x+dx3, y+dy3)}.
	 * 
	 * @param dx1 the X offset to the first control point
	 * @param dy1 the Y offset to the first control point
	 * @param dx2 the X offset to the second control point
	 * @param dy2 the Y offset to the second control point
	 * @param dx3 the X offset to the end of the curve
	 * @param dy3 the Y offset to the end of the curve
	 * @return the context
	 * @throws IllegalStateException when called with no current point
	 * @since 1.0
	 */
	public Context relCurveTo(double dx1, double dy1, double dx2, double dy2, double dx3, double dy3)
			throws IllegalStateException {
		try {
			cairo_rel_curve_to.invoke(handle(), dx1, dy1, dx2, dy2, dx3, dy3);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (status() == Status.NO_CURRENT_POINT) {
			throw new IllegalStateException(Status.NO_CURRENT_POINT.toString());
		}
		return this;
	}

	private static final MethodHandle cairo_rel_curve_to = Interop.downcallHandle("cairo_rel_curve_to",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Relative-coordinate version of {@link #lineTo(double, double)}. Adds a line
	 * to the path from the current point to a point that is offset from the current
	 * point by {@code (dx, dy)} in user space. After this call the current point
	 * will be offset by {@code (dx, dy)}.
	 * <p>
	 * Given a current point of ({@code x}, {@code y}), {@code relLineTo(dx, dy)} is
	 * logically equivalent to {@code lineTo(x + dx, y + dy)}.
	 * 
	 * @param dx the X offset to the end of the new line
	 * @param dy the Y offset to the end of the new line
	 * @return the context
	 * @throws IllegalStateException when called with no current point
	 * @since 1.0
	 */
	public Context relLineTo(double dx, double dy) throws IllegalStateException {
		try {
			cairo_rel_line_to.invoke(handle(), dx, dy);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (status() == Status.NO_CURRENT_POINT) {
			throw new IllegalStateException(Status.NO_CURRENT_POINT.toString());
		}
		return this;
	}

	private static final MethodHandle cairo_rel_line_to = Interop.downcallHandle("cairo_rel_line_to",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Begin a new sub-path. After this call the current point will offset by
	 * ({@code x}, {@code y}).
	 * <p>
	 * Given a current point of ({@code x}, {@code y}), {@code relMoveTo(dx, dy)} is
	 * logically equivalent to {@code moveTo(x + dx, y + dy)}.
	 * 
	 * @param dx the X offset
	 * @param dy the Y offset
	 * @return the context
	 * @throws IllegalStateException when called with no current point
	 * @since 1.0
	 */
	public Context relMoveTo(double dx, double dy) throws IllegalStateException {
		try {
			cairo_rel_move_to.invoke(handle(), dx, dy);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (status() == Status.NO_CURRENT_POINT) {
			throw new IllegalStateException(Status.NO_CURRENT_POINT.toString());
		}
		return this;
	}

	private static final MethodHandle cairo_rel_move_to = Interop.downcallHandle("cairo_rel_move_to",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Computes a bounding box in user-space coordinates covering the points on the
	 * current path. If the current path is empty, returns an empty rectangle
	 * ((0,0), (0,0)). Stroke parameters, fill rule, surface dimensions and clipping
	 * are not taken into account.
	 * <p>
	 * Contrast with {@link #fillExtents()} and {@link #strokeExtents()} which
	 * return the extents of only the area that would be "inked" by the
	 * corresponding drawing operations.
	 * <p>
	 * The result of {@code pathExtents()} is defined as equivalent to the limit of
	 * {@code strokeExtents()} with {@link LineCap#ROUND} as the line width
	 * approaches 0.0, (but never reaching the empty-rectangle returned by
	 * {@link #strokeExtents()} for a line width of 0.0).
	 * <p>
	 * Specifically, this means that zero-area sub-paths such as
	 * {@code cr.moveTo().lineTo()} segments, (even degenerate cases where the
	 * coordinates to both calls are identical), will be considered as contributing
	 * to the extents. However, a lone {@code moveTo()} will not contribute to the
	 * results of {@code pathExtents()}.
	 * 
	 * @param x1 left of the resulting extents
	 * @param y1 top of the resulting extents
	 * @param x2 right of the resulting extents
	 * @param y2 bottom of the resulting extents
	 * @since 1.6
	 */
	public Rectangle pathExtents(double x1, double y1, double x2, double y2) {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment x1Ptr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment y1Ptr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment x2Ptr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment y2Ptr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				cairo_path_extents.invoke(handle(), x1Ptr, y1Ptr, x2Ptr, y2Ptr);
				return Rectangle.create(x1Ptr.get(ValueLayout.JAVA_DOUBLE, 0), y1Ptr.get(ValueLayout.JAVA_DOUBLE, 0),
						x2Ptr.get(ValueLayout.JAVA_DOUBLE, 0), y2Ptr.get(ValueLayout.JAVA_DOUBLE, 0));
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_path_extents = Interop.downcallHandle("cairo_path_extents",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
					ValueLayout.ADDRESS, ValueLayout.ADDRESS),
			false);

	/**
	 * Modifies the current transformation matrix (CTM) by translating the
	 * user-space origin by {@code (tx, ty)}. This offset is interpreted as a
	 * user-space coordinate according to the CTM in place before the new call to
	 * {@code translate()}. In other words, the translation of the user-space origin
	 * takes place after any existing transformation.
	 * 
	 * @param tx amount to translate in the X direction
	 * @param ty amount to translate in the Y direction
	 * @return the context
	 * @since 1.0
	 */
	public Context translate(double tx, double ty) {
		try {
			cairo_translate.invoke(handle(), tx, ty);
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_translate = Interop.downcallHandle("cairo_translate",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Modifies the current transformation matrix (CTM) by scaling the X and Y
	 * user-space axes by {@code sx} and {@code sy} respectively. The scaling of the
	 * axes takes place after any existing transformation of user space.
	 * 
	 * @param sx scale factor for the X dimension
	 * @param sy scale factor for the Y dimension
	 * @return the context
	 * @since 1.0
	 */
	public Context scale(double sx, double sy) {
		try {
			cairo_scale.invoke(handle(), sx, sy);
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_scale = Interop.downcallHandle("cairo_scale",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Modifies the current transformation matrix (CTM) by rotating the user-space
	 * axes by {@code angle} radians. The rotation of the axes takes places after
	 * any existing transformation of user space. The rotation direction for
	 * positive angles is from the positive X axis toward the positive Y axis.
	 * 
	 * @param angle angle (in radians) by which the user-space axes will be rotated
	 * @return the context
	 * @since 1.0
	 */
	public Context rotate(double angle) {
		try {
			cairo_rotate.invoke(handle(), angle);
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_rotate = Interop.downcallHandle("cairo_rotate",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Modifies the current transformation matrix (CTM) by applying {@code matrix}
	 * as an additional transformation. The new transformation of user space takes
	 * place after any existing transformation.
	 * 
	 * @param matrix a transformation to be applied to the user-space axes
	 * @return the context
	 * @since 1.0
	 */
	public Context transform(Matrix matrix) {
		try {
			cairo_transform.invoke(handle(), matrix == null ? MemorySegment.NULL : matrix.handle());
			this.matrix = matrix;
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_transform = Interop.downcallHandle("cairo_transform",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Modifies the current transformation matrix (CTM) by setting it equal to
	 * {@code matrix}.
	 * <p>
	 * The CTM is a two-dimensional affine transformation that maps all coordinates
	 * and other drawing instruments from the user space into the surface's
	 * canonical coordinate system, also known as the device space.
	 * 
	 * @param matrix a transformation matrix from user space to device space
	 * @return the context
	 * @since 1.0
	 */
	public Context setMatrix(Matrix matrix) {
		try {
			cairo_set_matrix.invoke(handle(), matrix == null ? MemorySegment.NULL : matrix.handle());
			this.matrix = matrix;
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_matrix = Interop.downcallHandle("cairo_set_matrix",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Stores the current transformation matrix (CTM) into the returned matrix.
	 * <p>
	 * The CTM is a two-dimensional affine transformation that maps all coordinates
	 * and other drawing instruments from the user space into the surface's
	 * canonical coordinate system, also known as the device space.
	 * 
	 * @return a matrix with the current transformation matrix
	 * @since 1.0
	 */
	public Matrix getMatrix() {
		try {
			Matrix matrix = Matrix.create();
			cairo_get_matrix.invoke(handle(), matrix.handle());
			return matrix;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_matrix = Interop.downcallHandle("cairo_get_matrix",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Resets the current transformation matrix (CTM) by setting it equal to the
	 * identity matrix. That is, the user-space and device-space axes will be
	 * aligned and one user-space unit will transform to one device-space unit.
	 * 
	 * @return the context
	 * @since 1.0
	 */
	public Context identityMatrix() {
		try {
			cairo_identity_matrix.invoke(handle());
			this.matrix = null;
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_identity_matrix = Interop.downcallHandle("cairo_identity_matrix",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Transform a coordinate from user space to device space by multiplying the
	 * given point by the current transformation matrix (CTM).
	 * 
	 * @param point X and Y values of coordinates
	 * @return a Point with the transformed X and Y coordinates
	 * @since 1.0
	 */
	public Point userToDevice(Point point) {
		if (point == null) {
			return null;
		}
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment xPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment yPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				xPtr.set(ValueLayout.JAVA_DOUBLE, 0, point.x());
				yPtr.set(ValueLayout.JAVA_DOUBLE, 0, point.y());
				cairo_user_to_device.invoke(handle(), xPtr, yPtr);
				return new Point(xPtr.get(ValueLayout.JAVA_DOUBLE, 0), yPtr.get(ValueLayout.JAVA_DOUBLE, 0));
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_user_to_device = Interop.downcallHandle("cairo_user_to_device",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Transform a distance vector from user space to device space. This function is
	 * similar to {@link #userToDevice(Point)} except that the translation
	 * components of the CTM will be ignored when transforming {@code (dx, dy)}.
	 * 
	 * @param point X and Y components of a distance vector
	 * @return a Point with the transformed X and Y compontents
	 * @since 1.0
	 */
	public Point userToDeviceDistance(Point point) {
		if (point == null) {
			return null;
		}
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment xPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment yPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				xPtr.set(ValueLayout.JAVA_DOUBLE, 0, point.x());
				yPtr.set(ValueLayout.JAVA_DOUBLE, 0, point.y());
				cairo_user_to_device_distance.invoke(handle(), xPtr, yPtr);
				return new Point(xPtr.get(ValueLayout.JAVA_DOUBLE, 0), yPtr.get(ValueLayout.JAVA_DOUBLE, 0));
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_user_to_device_distance = Interop.downcallHandle(
			"cairo_user_to_device_distance",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Transform a coordinate from device space to user space by multiplying the
	 * given point by the inverse of the current transformation matrix (CTM).
	 * 
	 * @param point X and Y values of coordinate
	 * @return a Point with the transformed X and Y coordinates
	 * @since 1.0
	 */
	public Point deviceToUser(Point point) {
		if (point == null) {
			return null;
		}
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment xPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment yPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				xPtr.set(ValueLayout.JAVA_DOUBLE, 0, point.x());
				yPtr.set(ValueLayout.JAVA_DOUBLE, 0, point.y());
				cairo_device_to_user.invoke(handle(), xPtr, yPtr);
				return new Point(xPtr.get(ValueLayout.JAVA_DOUBLE, 0), yPtr.get(ValueLayout.JAVA_DOUBLE, 0));
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_device_to_user = Interop.downcallHandle("cairo_device_to_user",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Transform a distance vector from device space to user space. This function is
	 * similar to {@link #deviceToUser(Point)} except that the translation
	 * components of the inverse CTM will be ignored when transforming
	 * {@code (dx, dy)}.
	 * 
	 * @param point X and Y components of a distance vector
	 * @return a Point with the transformed X and Y compontents
	 * @since 1.0
	 */
	public Point deviceToUserDistance(Point point) {
		if (point == null) {
			return null;
		}
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment xPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment yPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				xPtr.set(ValueLayout.JAVA_DOUBLE, 0, point.x());
				yPtr.set(ValueLayout.JAVA_DOUBLE, 0, point.y());
				cairo_device_to_user_distance.invoke(handle(), xPtr, yPtr);
				return new Point(xPtr.get(ValueLayout.JAVA_DOUBLE, 0), yPtr.get(ValueLayout.JAVA_DOUBLE, 0));
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_device_to_user_distance = Interop.downcallHandle(
			"cairo_device_to_user_distance",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * <strong>Note:</strong> The {@code selectFontFace()} method is part of what
	 * the cairo designers call the "toy" text API. It is convenient for short demos
	 * and simple programs, but it is not expected to be adequate for serious
	 * text-using applications.
	 * <p>
	 * Selects a family and style of font from a simplified description as a family
	 * name, slant and weight. Cairo provides no operation to list available family
	 * names on the system (this is a "toy", remember), but the standard CSS2
	 * generic family names, ("serif", "sans-serif", "cursive", "fantasy",
	 * "monospace"), are likely to work as expected.
	 * <p>
	 * If {@code family} starts with the string "{@code cairo :}", or if no native
	 * font backends are compiled in, cairo will use an internal font family. The
	 * internal font family recognizes many modifiers in the {@code family} string,
	 * most notably, it recognizes the string "monospace". That is, the family name
	 * "{@code cairo :monospace}" will use the monospace version of the internal
	 * font family.
	 * <p>
	 * For "real" font selection, see the font-backend-specific font_face_create
	 * functions for the font backend you are using. (For example, if you are using
	 * the freetype-based cairo-ft font backend, see
	 * cairo_ft_font_face_create_for_ft_face() or
	 * cairo_ft_font_face_create_for_pattern().) The resulting font face could then
	 * be used with {@link ScaledFont#create(FontFace, Matrix, Matrix, FontOptions)}
	 * and {@link #setScaledFont(ScaledFont)}.
	 * <p>
	 * Similarly, when using the "real" font support, you can call directly into the
	 * underlying font system, (such as fontconfig or freetype), for operations such
	 * as listing available fonts, etc.
	 * <p>
	 * It is expected that most applications will need to use a more comprehensive
	 * font handling and text layout library, (for example, pango), in conjunction
	 * with cairo.
	 * <p>
	 * If text is drawn without a call to {@code selectFontFace()}, (nor
	 * {@link #setFontFace(FontFace)} nor {@link #setScaledFont(ScaledFont)}), the
	 * default family is platform-specific, but is essentially "sans-serif". Default
	 * slant is {@link FontSlant#NORMAL} , and default weight is
	 * {@link FontWeight#NORMAL}.
	 * <p>
	 * This function is equivalent to a call to {@link ToyFontFace#create()}
	 * followed by {@link #setFontFace(FontFace)}.
	 * 
	 * @param family a font family name
	 * @param slant  the slant for the font
	 * @param weight the weight for the font
	 * @return the context
	 * @since 1.0
	 */
	public Context selectFontFace(String family, FontSlant slant, FontWeight weight) {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment utf8 = Interop.allocateNativeString(family, arena);
				cairo_select_font_face.invoke(handle(), utf8, slant.value(), weight.value());
				return this;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_select_font_face = Interop.downcallHandle("cairo_select_font_face",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
					ValueLayout.JAVA_INT),
			false);

	/**
	 * Sets the current font matrix to a scale by a factor of {@code size},
	 * replacing any font matrix previously set with {@code setFontSize()} or
	 * {@link #setFontMatrix(Matrix)}. This results in a font size of {@code size}
	 * user space units. (More precisely, this matrix will result in the font's
	 * em-square being a {@code size} by {@code size} square in user space.)
	 * <p>
	 * If text is drawn without a call to {@code setFontSize()}, (nor
	 * {@link #setFontMatrix(Matrix)} nor {@link #setScaledFont(ScaledFont)}), the
	 * default font size is 10.0.
	 * 
	 * @param size the new font size, in user space units
	 * @return the context
	 * @since 1.0
	 */
	public Context setFontSize(double size) {
		try {
			cairo_set_font_size.invoke(handle(), size);
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_font_size = Interop.downcallHandle("cairo_set_font_size",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Sets the current font matrix to {@code matrix}. The font matrix gives a
	 * transformation from the design space of the font (in this space, the
	 * em-square is 1 unit by 1 unit) to user space. Normally, a simple scale is
	 * used (see {@link #setFontSize(double)}), but a more complex font matrix can
	 * be used to shear the font or stretch it unequally along the two axes
	 * 
	 * @param matrix a Matrix describing a transform to be applied to the current
	 *               font.
	 * @return the context
	 * @since 1.0
	 */
	public Context setFontMatrix(Matrix matrix) {
		try {
			cairo_set_font_matrix.invoke(handle(), matrix == null ? MemorySegment.NULL : matrix.handle());
			this.fontMatrix = matrix;
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_font_matrix = Interop.downcallHandle("cairo_set_font_matrix",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Returns the current font matrix. See {@link #setFontMatrix(Matrix)}
	 * 
	 * @return the matrix
	 * @since 1.0
	 */
	public Matrix getFontMatrix() {
		try {
			Matrix fontMatrix = Matrix.create();
			cairo_get_font_matrix.invoke(handle(), fontMatrix.handle());
			return fontMatrix;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_font_matrix = Interop.downcallHandle("cairo_get_font_matrix",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Sets a set of custom font rendering options for the Context. Rendering
	 * options are derived by merging these options with the options derived from
	 * underlying surface; if the value in {@code options} has a default value (like
	 * {@link Antialias#DEFAULT}), then the value from the surface is used.
	 * 
	 * @param options font options to use
	 * @return this context
	 * @since 1.0
	 */
	public Context setFontOptions(FontOptions options) {
		try {
			cairo_set_font_options.invoke(handle(), options == null ? MemorySegment.NULL : options.handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_font_options = Interop.downcallHandle("cairo_set_font_options",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Retrieves font rendering options set via
	 * {@link #setFontOptions(FontOptions)}. Note that the returned options do not
	 * include any options derived from the underlying surface; they are literally
	 * the options passed to {@link #setFontOptions(FontOptions)}.
	 * 
	 * @return the retrieved options
	 * @since 1.0
	 */
	public FontOptions getFontOptions() {
		try {
			FontOptions options = FontOptions.create();
			cairo_get_font_options.invoke(handle(), options.handle());
			return options;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_font_options = Interop.downcallHandle("cairo_get_font_options",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Replaces the current FontFace object in the Context with {@code fontFace}.
	 * The replaced font face in the Context will be destroyed if there are no other
	 * references to it.
	 * 
	 * @param fontFace a FontFace, or {@code null} to restore to the default font
	 * @return the context
	 * @since 1.0
	 */
	public Context setFontFace(FontFace fontFace) {
		try {
			cairo_set_font_face.invoke(handle(), fontFace == null ? MemorySegment.NULL : fontFace.handle());
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_font_face = Interop.downcallHandle("cairo_set_font_face",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Gets the current font face for a {@link Context}.
	 * 
	 * @return the current font face
	 * @since 1.0
	 */
	public FontFace getFontFace() {
		FontFace fontFace;
		try {
			MemorySegment result = (MemorySegment) cairo_get_font_face.invoke(handle());
			fontFace = new FontFace(result);
			// Take a reference on the returned fontface
			fontFace.reference();
			fontFace.takeOwnership();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (fontFace.status() == Status.NO_MEMORY) {
			throw new RuntimeException(fontFace.status().toString());
		}
		return fontFace;
	}

	private static final MethodHandle cairo_get_font_face = Interop.downcallHandle("cairo_get_font_face",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Replaces the current font face, font matrix, and font options in the
	 * {@link Context} with those of the {@link ScaledFont}. Except for some
	 * translation, the current CTM of the Context should be the same as that of the
	 * ScaledFont, which can be accessed using {@link ScaledFont#getCTM()}.
	 * 
	 * @param scaledFont a ScaledFont
	 * @return the context
	 * @since 1.2
	 */
	public Context setScaledFont(ScaledFont scaledFont) {
		try {
			cairo_set_scaled_font.invoke(handle(), scaledFont == null ? MemorySegment.NULL : scaledFont.handle());
			this.scaledFont = scaledFont;
			return this;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_scaled_font = Interop.downcallHandle("cairo_set_scaled_font",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Gets the current scaled font
	 * 
	 * @return the current scaled font
	 * @since 1.4
	 */
	public ScaledFont getScaledFont() {
		ScaledFont scaledFont;
		try {
			MemorySegment result = (MemorySegment) cairo_get_scaled_font.invoke(handle());
			scaledFont = new ScaledFont(result);
			// Take a reference on the returned fontface
			scaledFont.reference();
			scaledFont.takeOwnership();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (scaledFont.status() == Status.NO_MEMORY) {
			throw new RuntimeException(scaledFont.status().toString());
		}
		return scaledFont;
	}

	private static final MethodHandle cairo_get_scaled_font = Interop.downcallHandle("cairo_get_scaled_font",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * A drawing operator that generates the shape from a string of characters,
	 * rendered according to the current fontFace, fontSize (fontMatrix), and
	 * fontOptions.
	 * <p>
	 * This function first computes a set of glyphs for the string of text. The
	 * first glyph is placed so that its origin is at the current point. The origin
	 * of each subsequent glyph is offset from that of the previous glyph by the
	 * advance values of the previous glyph.
	 * <p>
	 * After this call the current point is moved to the origin of where the next
	 * glyph would be placed in this same progression. That is, the current point
	 * will be at the origin of the final glyph offset by its advance values. This
	 * allows for easy display of a single logical string with multiple calls to
	 * {@code showText()}.
	 * <p>
	 * <strong>Note:</strong> The {@code showText()} function call is part of what
	 * the cairo designers call the "toy" text API. It is convenient for short demos
	 * and simple programs, but it is not expected to be adequate for serious
	 * text-using applications. See {@link #showGlyphs(Glyph[])} for the "real" text
	 * display API in cairo.
	 * 
	 * @param string a string of text, or {@code null}
	 * @return the context
	 * @since 1.0
	 */
	public Context showText(String string) {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment utf8 = Interop.allocateNativeString(string, arena);
				cairo_show_text.invoke(handle(), utf8);
				return this;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_show_text = Interop.downcallHandle("cairo_show_text",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * A drawing operator that generates the shape from an array of glyphs, rendered
	 * according to the current fontFace, fontSize (fontMatrix), and fontOptions.
	 * 
	 * @param glyphs array of glyphs to show
	 * @return the context
	 * @since 1.0
	 */
	public Context showGlyphs(Glyph[] glyphs) {
		if (glyphs == null || glyphs.length == 0) {
			return this;
		}
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment glyphsPtr = arena.allocateArray(ValueLayout.ADDRESS, glyphs.length);
				for (int i = 0; i < glyphs.length; i++) {
					glyphsPtr.setAtIndex(ValueLayout.ADDRESS, i, glyphs[i].handle());
				}
				cairo_show_glyphs.invoke(handle(), glyphsPtr, glyphs.length);
				return this;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_show_glyphs = Interop.downcallHandle("cairo_show_glyphs",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT), false);

	/**
	 * This operation has rendering effects similar to {@link #showGlyphs(Glyph[])}
	 * but, if the target surface supports it, uses the provided text and cluster
	 * mapping to embed the text for the glyphs shown in the output. If the target
	 * does not support the extended attributes, this function acts like the basic
	 * {@link #showGlyphs(Glyph[])} as if it had been passed {@code glyphs}.
	 * <p>
	 * The mapping between the string and glyphs array is provided by an array of
	 * <i>clusters</i>. Each cluster covers a number of text bytes and glyphs, and
	 * neighboring clusters cover neighboring areas of the text string and the
	 * glyphs array. The clusters should collectively cover {@code string} and
	 * {@code glyphs} in entirety.
	 * <p>
	 * The first cluster always covers bytes from the beginning of {@code string}.
	 * If {@code clusterFlags} do not have the {@link TextClusterFlags#BACKWARD}
	 * set, the first cluster also covers the beginning of {@code glyphs}, otherwise
	 * it covers the end of the glyphs array and following clusters move backward.
	 * <p>
	 * See {@link TextCluster} for constraints on valid clusters.
	 * 
	 * @param string       a string of text
	 * @param glyphs       array of glyphs to show
	 * @param clusters     array of cluster mapping information
	 * @param clusterFlags cluster mapping flags
	 * @return the context
	 * @since 1.8
	 */
	public Context showTextGlyphs(String string, Glyph[] glyphs, TextCluster[] clusters,
			TextClusterFlags clusterFlags) {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment utf8 = Interop.allocateNativeString(string, arena);
				MemorySegment glyphsPtr;
				MemorySegment clustersPtr;
				if (glyphs == null || glyphs.length == 0) {
					glyphsPtr = MemorySegment.NULL;
				} else {
					glyphsPtr = arena.allocateArray(ValueLayout.ADDRESS, glyphs.length);
					for (int i = 0; i < glyphs.length; i++) {
						glyphsPtr.setAtIndex(ValueLayout.ADDRESS, i, glyphs[i].handle());
					}
				}
				if (clusters == null || clusters.length == 0) {
					clustersPtr = MemorySegment.NULL;
				} else {
					clustersPtr = arena.allocateArray(ValueLayout.ADDRESS, clusters.length);
					for (int i = 0; i < clusters.length; i++) {
						clustersPtr.setAtIndex(ValueLayout.ADDRESS, i, clusters[i].handle());
					}
				}
				cairo_show_text_glyphs.invoke(handle(), utf8, string == null ? 0 : string.length(), glyphsPtr,
						glyphs == null ? 0 : glyphs.length, clustersPtr, clusters == null ? 0 : clusters.length,
						clusterFlags.value());
				return this;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_show_text_glyphs = Interop.downcallHandle("cairo_show_text_glyphs",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
					ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
					ValueLayout.JAVA_INT),
			false);

	/**
	 * Gets the font extents for the currently selected font.
	 * 
	 * @return the font extents
	 * @since 1.0
	 */
	public FontExtents fontExtents() {
		try {
			FontExtents extents = FontExtents.create();
			cairo_font_extents.invoke(handle(), extents.handle());
			return extents;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_font_extents = Interop.downcallHandle("cairo_font_extents",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Gets the extents for a string of text. The extents describe a user-space
	 * rectangle that encloses the "inked" portion of the text, (as it would be
	 * drawn by {@link #showText(String)}). Additionally, the {@code xAdvance} and
	 * {@code yAdvance} values indicate the amount by which the current point would
	 * be advanced by {@link #showText(String)}.
	 * <p>
	 * Note that whitespace characters do not directly contribute to the size of the
	 * rectangle ({@code extents.width} and {@code extents.height}). They do
	 * contribute indirectly by changing the position of non-whitespace characters.
	 * In particular, trailing whitespace characters are likely to not affect the
	 * size of the rectangle, though they will affect the {@code xAdvance} and
	 * {@code yAdvance} values.
	 * 
	 * @param string a string of text, or {@code null}
	 * @return the text extents
	 * @since 1.0
	 */
	public TextExtents textExtents(String string) {
		try {
			try (Arena arena = Arena.openConfined()) {
				TextExtents extents = TextExtents.create();
				MemorySegment utf8 = Interop.allocateNativeString(string, arena);
				cairo_text_extents.invoke(handle(), utf8, extents.handle());
				return extents;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_text_extents = Interop.downcallHandle("cairo_text_extents",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Gets the extents for an array of glyphs. The extents describe a user-space
	 * rectangle that encloses the "inked" portion of the glyphs, (as they would be
	 * drawn by {@link #showGlyphs(Glyph[])}). Additionally, the {@code xAdvance}
	 * and {@code yAdvance} values indicate the amount by which the current point
	 * would be advanced by {@link #showGlyphs(Glyph[])}.
	 * <p>
	 * Note that whitespace glyphs do not contribute to the size of the rectangle
	 * ({@code extents.width} and {@code extents.height}).
	 * 
	 * @param glyphs an array of Glyph objects
	 * @return the glyph extents
	 * @since 1.0
	 */
	public TextExtents glyphExtents(Glyph[] glyphs) {
		if (glyphs == null || glyphs.length == 0) {
			return null;
		}
		try {
			try (Arena arena = Arena.openConfined()) {
				TextExtents extents = TextExtents.create();
				MemorySegment glyphsPtr = arena.allocateArray(ValueLayout.ADDRESS, glyphs.length);
				for (int i = 0; i < glyphs.length; i++) {
					glyphsPtr.setAtIndex(ValueLayout.ADDRESS, i, glyphs[i].handle());
				}
				cairo_glyph_extents.invoke(handle(), glyphsPtr, glyphs.length, extents.handle());
				return extents;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_glyph_extents = Interop.downcallHandle("cairo_glyph_extents",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Marks the beginning of the tag_name structure. Call {@link #tagEnd(String)}
	 * with the same {@code tagName} to mark the end of the structure.
	 * <p>
	 * The attributes string is of the form "key1=value2 key2=value2 ...". Values
	 * may be boolean (true/false or 1/0), integer, float, string, or an array.
	 * <p>
	 * String values are enclosed in single quotes ('). Single quotes and
	 * backslashes inside the string should be escaped with a backslash.
	 * <p>
	 * Boolean values may be set to true by only specifying the key. eg the
	 * attribute string "key" is the equivalent to "key=true".
	 * <p>
	 * Arrays are enclosed in '[]'. eg "rect=[1.2 4.3 2.0 3.0]".
	 * <p>
	 * If no attributes are required, {@code attributes} can be an empty string or
	 * {@code null}.
	 * <p>
	 * See <a href=
	 * "https://www.cairographics.org/manual/cairo-Tags-and-Links.html#cairo-Tags-and-Links.description">Tags
	 * and Links Description</a> for the list of tags and attributes.
	 * 
	 * @param tagName    tag name
	 * @param attributes tag attributes
	 * @return the context
	 * @throws IllegalArgumentException invalid nesting of tags or invalid
	 *                                  attributes, see {@link Status#TAG_ERROR}
	 * @see #tagEnd(String)
	 * @since 1.16
	 */
	public Context tagBegin(String tagName, String attributes) throws IllegalArgumentException {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment tagNamePtr = Interop.allocateNativeString(tagName, arena);
				MemorySegment attributesPtr = Interop.allocateNativeString(attributes, arena);
				cairo_tag_begin.invoke(handle(), tagNamePtr, attributesPtr);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (status() != Status.SUCCESS) {
			throw new IllegalArgumentException(status().toString());
		}
		return this;
	}

	private static final MethodHandle cairo_tag_begin = Interop.downcallHandle("cairo_tag_begin",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Marks the end of the tag_name structure.
	 * 
	 * @param tagName tag name
	 * @return the context
	 * @throws IllegalArgumentException Invalid nesting of tags, see
	 *                                  {@link Status#TAG_ERROR}
	 * @see #tagBegin(String, String)
	 * @since 1.16
	 */
	public Context tagEnd(String tagName) throws IllegalArgumentException {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment tagNamePtr = Interop.allocateNativeString(tagName, arena);
				cairo_tag_end.invoke(handle(), tagNamePtr);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (status() != Status.SUCCESS) {
			throw new IllegalArgumentException(status().toString());
		}
		return this;
	}

	private static final MethodHandle cairo_tag_end = Interop.downcallHandle("cairo_tag_end",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);
}
