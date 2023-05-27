package org.freedesktop.cairo.drawing;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import org.freedesktop.cairo.surfaces.Content;
import org.freedesktop.cairo.surfaces.Surface;

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.ProxyInstance;

/**
 * The cairo drawing context.
 * <p>
 * Context is the main object used when drawing with cairo. To draw with cairo,
 * you create a Context, set the target surface, and drawing options for the
 * Context, create shapes with functions like cairo_move_to() and
 * cairo_line_to(), and then draw shapes with {@link #stroke()} or
 * {@link #fill()}.
 * <p>
 * Context's can be pushed to a stack via {@link #save()}. They may then safely
 * be changed, without losing the current state. Use {@link #restore()} to
 * restore to the saved state.
 * 
 * @since 1.0
 */
public final class Context extends ProxyInstance {

	{
		Interop.ensureInitialized();
	}

	/**
	 * Constructor used internally to instantiate a java Context object for a native
	 * {@code cairo_t} instance
	 * 
	 * @param address the memory address of the native {@code cairo_t} instance
	 */
	public Context(MemorySegment address) {
		super(address);
		setDereferenceFunc("cairo_context_destroy");
	}

	/**
	 * Creates a new cairo_t with all graphics state parameters set to default
	 * values and with target as a target surface. The target surface should be
	 * constructed with a backend-specific function such as
	 * cairo_image_surface_create() (or any other cairo_backend_surface_create()
	 * variant).
	 * <p>
	 * This function references target , so you can immediately call
	 * cairo_surface_destroy() on it if you don't need to maintain a separate
	 * reference to it.
	 * 
	 * @param target target surface for the context
	 * @return a newly allocated cairo_t with a reference count of 1. The initial
	 *         reference count should be released with cairo_destroy() when you are
	 *         done using the cairo_t. This function never returns NULL. If memory
	 *         cannot be allocated, a special cairo_t object will be returned on
	 *         which cairo_status() returns CAIRO_STATUS_NO_MEMORY. If you attempt
	 *         to target a surface which does not support writing (such as
	 *         cairo_mime_surface_t) then a CAIRO_STATUS_WRITE_ERROR will be raised.
	 *         You can use this object normally, but no drawing will be done.
	 * @since 1.0
	 */
	public static Context create(Surface target) {
		try {
			MemorySegment result = (MemorySegment) cairo_create.invoke(target.handle());
			Context context = new Context(result);
			context.takeOwnership();
			return context;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_create = Interop.downcallHandle("cairo_create",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Checks whether an error has previously occurred for this context.
	 * 
	 * @return the current status of this context, see cairo_status_t
	 * @since 1.0
	 */
	public Status status() {
		try {
			int result = (int) cairo_status.invoke(handle());
			return Status.values()[result];
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_status = Interop.downcallHandle("cairo_status",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Makes a copy of the current state of cr and saves it on an internal stack of
	 * saved states for cr . When cairo_restore() is called, cr will be restored to
	 * the saved state. Multiple calls to cairo_save() and cairo_restore() can be
	 * nested; each call to cairo_restore() restores the state from the matching
	 * paired cairo_save().
	 * <p>
	 * It isn't necessary to clear all saved states before a cairo_t is freed. If
	 * the reference count of a cairo_t drops to zero in response to a call to
	 * cairo_destroy(), any saved states will be freed along with the cairo_t.
	 * 
	 * @since 1.0
	 */
	public void save() {
		try {
			cairo_save.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_save = Interop.downcallHandle("cairo_save",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Restores cr to the state saved by a preceding call to cairo_save() and
	 * removes that state from the stack of saved states.
	 * 
	 * @since 1.0
	 */
	public void restore() {
		try {
			cairo_restore.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_restore = Interop.downcallHandle("cairo_restore",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Gets the target surface for the cairo context as passed to cairo_create().
	 * <p>
	 * This function will always return a valid pointer, but the result can be a
	 * "nil" surface if cr is already in an error state, (ie. cairo_status() !=
	 * CAIRO_STATUS_SUCCESS). A nil surface is indicated by cairo_surface_status()
	 * != CAIRO_STATUS_SUCCESS.
	 * 
	 * @return the target surface. This object is owned by cairo. To keep a
	 *         reference to it, you must call cairo_surface_reference().
	 * @since 1.0
	 */
	public Surface getTarget() {
		try {
			MemorySegment result = (MemorySegment) cairo_get_target.invoke(handle());
			return new Surface(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_target = Interop.downcallHandle("cairo_get_target",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Temporarily redirects drawing to an intermediate surface known as a group.
	 * The redirection lasts until the group is completed by a call to
	 * cairo_pop_group() or cairo_pop_group_to_source(). These calls provide the
	 * result of any drawing to the group as a pattern, (either as an explicit
	 * object, or set as the source pattern).
	 * <p>
	 * This group functionality can be convenient for performing intermediate
	 * compositing. One common use of a group is to render objects as opaque within
	 * the group, (so that they occlude each other), and then blend the result with
	 * translucence onto the destination.
	 * <p>
	 * Groups can be nested arbitrarily deep by making balanced calls to
	 * cairo_push_group()/cairo_pop_group(). Each call pushes/pops the new target
	 * group onto/from a stack.
	 * <p>
	 * The cairo_push_group() function calls cairo_save() so that any changes to the
	 * graphics state will not be visible outside the group, (the pop_group
	 * functions call cairo_restore()).
	 * <p>
	 * By default the intermediate group will have a content type of
	 * CAIRO_CONTENT_COLOR_ALPHA. Other content types can be chosen for the group by
	 * using cairo_push_group_with_content() instead.
	 * <p>
	 * As an example, here is how one might fill and stroke a path with
	 * translucence, but without any portion of the fill being visible under the
	 * stroke:
	 * {@snippet :
	 * cairo_push_group(cr);
	 * cairo_set_source(cr, fill_pattern);
	 * cairo_fill_preserve(cr);
	 * cairo_set_source(cr, stroke_pattern);
	 * cairo_stroke(cr);
	 * cairo_pop_group_to_source(cr);
	 * cairo_paint_with_alpha(cr, alpha);
	 * }
	 * 
	 * @since 1.2
	 */
	public void pushGroup() {
		try {
			cairo_push_group.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_push_group = Interop.downcallHandle("cairo_push_group",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Temporarily redirects drawing to an intermediate surface known as a group.
	 * The redirection lasts until the group is completed by a call to
	 * cairo_pop_group() or cairo_pop_group_to_source(). These calls provide the
	 * result of any drawing to the group as a pattern, (either as an explicit
	 * object, or set as the source pattern).
	 * <p>
	 * The group will have a content type of content . The ability to control this
	 * content type is the only distinction between this function and
	 * cairo_push_group() which you should see for a more detailed description of
	 * group rendering.
	 * 
	 * @param content a cairo_content_t indicating the type of group that will be
	 *                created
	 * @since 1.2
	 */
	public void pushGroupWithContent(Content content) {
		try {
			cairo_push_group_with_content.invoke(handle(), content.ordinal());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_push_group_with_content = Interop.downcallHandle(
			"cairo_push_group_with_content", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT),
			false);

	/**
	 * Terminates the redirection begun by a call to cairo_push_group() or
	 * cairo_push_group_with_content() and returns a new pattern containing the
	 * results of all drawing operations performed to the group.
	 * <p>
	 * The cairo_pop_group() function calls cairo_restore(), (balancing a call to
	 * cairo_save() by the push_group function), so that any changes to the graphics
	 * state will not be visible outside the group.
	 * 
	 * @return a newly created (surface) pattern containing the results of all
	 *         drawing operations performed to the group. The caller owns the
	 *         returned object and should call cairo_pattern_destroy() when finished
	 *         with it.
	 * @since 1.2
	 */
	public Pattern popGroup() {
		try {
			MemorySegment result = (MemorySegment) cairo_pop_group.invoke(handle());
			Pattern pattern = new Pattern(result);
			pattern.takeOwnership();
			return pattern;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_pop_group = Interop.downcallHandle("cairo_pop_group",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Terminates the redirection begun by a call to cairo_push_group() or
	 * cairo_push_group_with_content() and installs the resulting pattern as the
	 * source pattern in the given cairo context.
	 * <p>
	 * The behavior of this function is equivalent to the sequence of operations:
	 * {@snippet :
	 * cairo_pattern_t *group = cairo_pop_group (cr);
	 * cairo_set_source (cr, group);
	 * cairo_pattern_destroy (group);
	 * }
	 * but is more convenient as their is no need for a variable to store the
	 * short-lived pointer to the pattern.
	 * <p>
	 * The cairo_pop_group() function calls cairo_restore(), (balancing a call to
	 * cairo_save() by the push_group function), so that any changes to the graphics
	 * state will not be visible outside the group.
	 * 
	 * @since 1.2
	 */
	public void popGroupToSource() {
		try {
			cairo_pop_group_to_source.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_pop_group_to_source = Interop.downcallHandle("cairo_pop_group_to_source",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Gets the current destination surface for the context. This is either the
	 * original target surface as passed to cairo_create() or the target surface for
	 * the current group as started by the most recent call to cairo_push_group() or
	 * cairo_push_group_with_content().
	 * <p>
	 * This function will always return a valid pointer, but the result can be a
	 * "nil" surface if cr is already in an error state, (ie. cairo_status() !=
	 * CAIRO_STATUS_SUCCESS). A nil surface is indicated by cairo_surface_status()
	 * != CAIRO_STATUS_SUCCESS.
	 * 
	 * @return the target surface. This object is owned by cairo. To keep a
	 *         reference to it, you must call cairo_surface_reference().
	 * @since 1.2
	 */
	public Surface getGroupTarget() {
		try {
			MemorySegment result = (MemorySegment) cairo_get_group_target.invoke(handle());
			return new Surface(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_group_target = Interop.downcallHandle("cairo_get_group_target",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Sets the source pattern within cr to an opaque color. This opaque color will
	 * then be used for any subsequent drawing operation until a new source pattern
	 * is set.
	 * <p>
	 * The color components are floating point numbers in the range 0 to 1. If the
	 * values passed in are outside that range, they will be clamped.
	 * <p>
	 * The default source pattern is opaque black, (that is, it is equivalent to
	 * cairo_set_source_rgb(cr, 0.0, 0.0, 0.0)).
	 * 
	 * @param red   red component of color
	 * @param green green component of color
	 * @param blue  blue component of color
	 * @since 1.0
	 */
	public void setSourceRGB(double red, double green, double blue) {
		try {
			cairo_set_source_rgb.invoke(handle(), red, green, blue);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_source_rgb = Interop.downcallHandle("cairo_set_source_rgb",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Sets the source pattern within cr to a translucent color. This color will
	 * then be used for any subsequent drawing operation until a new source pattern
	 * is set.
	 * <p>
	 * The color and alpha components are floating point numbers in the range 0 to
	 * 1. If the values passed in are outside that range, they will be clamped.
	 * <p>
	 * The default source pattern is opaque black, (that is, it is equivalent to
	 * cairo_set_source_rgba(cr, 0.0, 0.0, 0.0, 1.0)).
	 * 
	 * @param red   red component of color
	 * @param green green component of color
	 * @param blue  blue component of color
	 * @param alpha alpha component of color
	 * @since 1.0
	 */
	public void setSourceRGBA(double red, double green, double blue, double alpha) {
		try {
			cairo_set_source_rgba.invoke(handle(), red, green, blue, alpha);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_source_rgba = Interop.downcallHandle("cairo_set_source_rgba",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Sets the source pattern within cr to source. This pattern will then be used
	 * for any subsequent drawing operation until a new source pattern is set.
	 * <p>
	 * Note: The pattern's transformation matrix will be locked to the user space in
	 * effect at the time of cairo_set_source(). This means that further
	 * modifications of the current transformation matrix will not affect the source
	 * pattern. See cairo_pattern_set_matrix().
	 * <p>
	 * The default source pattern is a solid pattern that is opaque black, (that is,
	 * it is equivalent to cairo_set_source_rgb(cr, 0.0, 0.0, 0.0)).
	 * 
	 * @param source a cairo_pattern_t to be used as the source for subsequent
	 *               drawing operations.
	 * @since 1.0
	 */
	public void setSource(Pattern source) {
		try {
			cairo_set_source.invoke(handle(), source.handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_source = Interop.downcallHandle("cairo_set_source",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * This is a convenience function for creating a pattern from surface and
	 * setting it as the source in cr with cairo_set_source().
	 * <p>
	 * The x and y parameters give the user-space coordinate at which the surface
	 * origin should appear. (The surface origin is its upper-left corner before any
	 * transformation has been applied.) The x and y parameters are negated and then
	 * set as translation values in the pattern matrix.
	 * <p>
	 * Other than the initial translation pattern matrix, as described above, all
	 * other pattern attributes, (such as its extend mode), are set to the default
	 * values as in cairo_pattern_create_for_surface(). The resulting pattern can be
	 * queried with cairo_get_source() so that these attributes can be modified if
	 * desired, (eg. to create a repeating pattern with cairo_pattern_set_extend()).
	 * 
	 * @param surface a surface to be used to set the source pattern
	 * @param x       User-space X coordinate for surface origin
	 * @param y       User-space Y coordinate for surface origin
	 * @since 1.0
	 */
	public void setSourceSurface(Surface surface, double x, double y) {
		try {
			cairo_set_source_surface.invoke(handle(), surface.handle(), x, y);
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
	 * @return the current source pattern. This object is owned by cairo. To keep a
	 *         reference to it, you must call cairo_pattern_reference().
	 * @since 1.0
	 */
	public Pattern getSource() {
		try {
			MemorySegment result = (MemorySegment) cairo_get_source.invoke(handle());
			return new Pattern(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_source = Interop.downcallHandle("cairo_get_source",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Set the antialiasing mode of the rasterizer used for drawing shapes. This
	 * value is a hint, and a particular backend may or may not support a particular
	 * value. At the current time, no backend supports CAIRO_ANTIALIAS_SUBPIXEL when
	 * drawing shapes.
	 * <p>
	 * Note that this option does not affect text rendering, instead see
	 * cairo_font_options_set_antialias().
	 * 
	 * @param antialias the new antialiasing mode
	 * @since 1.0
	 */
	public void setAntialias(Antialias antialias) {
		try {
			cairo_set_antialias.invoke(handle(), antialias.ordinal());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_antialias = Interop.downcallHandle("cairo_set_antialias",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT), false);

	/**
	 * Gets the current shape antialiasing mode, as set by cairo_set_antialias().
	 * 
	 * @return the current shape antialiasing mode.
	 * @since 1.0
	 */
	public Antialias getAntialias() {
		try {
			int result = (int) cairo_get_antialias.invoke(handle());
			return Antialias.values()[result];
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_antialias = Interop.downcallHandle("cairo_get_antialias",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Sets the dash pattern to be used by cairo_stroke(). A dash pattern is
	 * specified by dashes, an array of positive values. Each value provides the
	 * length of alternate "on" and "off" portions of the stroke. The offset
	 * specifies an offset into the pattern at which the stroke begins.
	 * <p>
	 * Each "on" segment will have caps applied as if the segment were a separate
	 * sub-path. In particular, it is valid to use an "on" length of 0.0 with
	 * CAIRO_LINE_CAP_ROUND or CAIRO_LINE_CAP_SQUARE in order to distributed dots or
	 * squares along a path.
	 * <p>
	 * Note: The length values are in user-space units as evaluated at the time of
	 * stroking. This is not necessarily the same as the user space at the time of
	 * cairo_set_dash().
	 * <p>
	 * If {@code dashes.length} is 0 dashing is disabled.
	 * <p>
	 * If {@code dashes.length} is 1 a symmetric pattern is assumed with alternating
	 * on and off portions of the size specified by the single value in dashes.
	 * <p>
	 * If any value in dashes is negative, or if all values are 0, then the Context
	 * will be put into an error state with a status of CAIRO_STATUS_INVALID_DASH.
	 * 
	 * @param dash the dash pattern, with two fields: an array specifying alternate
	 *             lengths of on and off stroke portions, and an offset into the
	 *             dash pattern at which the stroke should start
	 * @since 1.0
	 */
	public void setDash(Dash dash) {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment dashesPtr = arena.allocateArray(ValueLayout.JAVA_DOUBLE, dash.dashes());
				cairo_set_dash.invoke(handle(), dashesPtr, dash.dashes().length, dash.offset());
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_dash = Interop.downcallHandle("cairo_set_dash", FunctionDescriptor
			.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * This function returns the length of the dash array in cr (0 if dashing is not
	 * currently in effect).
	 * <p>
	 * See also cairo_set_dash() and cairo_get_dash().
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
	 * 
	 * @return the dash pattern, with the return value for the dash array and the
	 *         current dash offset
	 * @since 1.4
	 */
	public Dash getDash() {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment dashesPtr = arena.allocate(ValueLayout.JAVA_DOUBLE.byteSize() * getDashCount());
				MemorySegment offsetPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				cairo_get_dash.invoke(handle(), dashesPtr, offsetPtr);
				double[] dashes = dashesPtr.toArray(ValueLayout.JAVA_DOUBLE);
				double offset = offsetPtr.get(ValueLayout.JAVA_DOUBLE, 0);
				return new Dash(dashes, offset);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_dash = Interop.downcallHandle("cairo_get_dash",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Set the current fill rule within the cairo context. The fill rule is used to
	 * determine which regions are inside or outside a complex (potentially
	 * self-intersecting) path. The current fill rule affects both cairo_fill() and
	 * cairo_clip(). See cairo_fill_rule_t for details on the semantics of each
	 * available fill rule.
	 * <p>
	 * The default fill rule is CAIRO_FILL_RULE_WINDING.
	 * 
	 * @param fillRule a fill rule, specified as a cairo_fill_rule_t
	 * @since 1.0
	 */
	public void setFillRule(FillRule fillRule) {
		try {
			cairo_set_fill_rule.invoke(handle(), fillRule.ordinal());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_fill_rule = Interop.downcallHandle("cairo_set_fill_rule",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT), false);

	/**
	 * Gets the current fill rule, as set by cairo_set_fill_rule().
	 * 
	 * @return the current fill rule.
	 * @since 1.0
	 */
	public FillRule getFillRule() {
		try {
			int result = (int) cairo_get_fill_rule.invoke(handle());
			return FillRule.values()[result];
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_fill_rule = Interop.downcallHandle("cairo_get_fill_rule",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Sets the current line cap style within the cairo context. See
	 * cairo_line_cap_t for details about how the available line cap styles are
	 * drawn.
	 * <p>
	 * As with the other stroke parameters, the current line cap style is examined
	 * by cairo_stroke(), cairo_stroke_extents(), and cairo_stroke_to_path(), but
	 * does not have any effect during path construction.
	 * <p>
	 * The default line cap style is CAIRO_LINE_CAP_BUTT.
	 * 
	 * @param lineCap a line cap style
	 * @since 1.0
	 */
	public void setLineCap(LineCap lineCap) {
		try {
			cairo_set_line_cap.invoke(handle(), lineCap.ordinal());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_line_cap = Interop.downcallHandle("cairo_set_line_cap",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT), false);

	/**
	 * Gets the current line cap style, as set by cairo_set_line_cap().
	 * 
	 * @return the current line cap style.
	 * @since 1.0
	 */
	public LineCap getLineCap() {
		try {
			int result = (int) cairo_get_line_cap.invoke(handle());
			return LineCap.values()[result];
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_get_line_cap = Interop.downcallHandle("cairo_get_line_cap",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Sets the current line join style within the cairo context. See
	 * cairo_line_join_t for details about how the available line join styles are
	 * drawn.
	 * <p>
	 * As with the other stroke parameters, the current line join style is examined
	 * by cairo_stroke(), cairo_stroke_extents(), and cairo_stroke_to_path(), but
	 * does not have any effect during path construction.
	 * <p>
	 * The default line join style is CAIRO_LINE_JOIN_MITER.
	 * 
	 * @param lineJoin a line join style
	 * @since 1.0
	 */
	public void setLineJoin(LineJoin lineJoin) {
		try {
			cairo_set_line_join.invoke(handle(), lineJoin.ordinal());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_line_join = Interop.downcallHandle("cairo_set_line_join",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT), false);

	/**
	 * Gets the current line join style, as set by cairo_set_line_join().
	 * 
	 * @return the current line join style.
	 * @since 1.0
	 */
	public LineJoin getLineJoin() {
		try {
			int result = (int) cairo_get_line_join.invoke(handle());
			return LineJoin.values()[result];
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
	 * cairo_set_line_width(). The simplest usage makes both of these spaces
	 * identical. That is, if there is no change to the CTM between a call to
	 * cairo_set_line_width() and the stroking operation, then one can just pass
	 * user-space values to cairo_set_line_width() and ignore this note.
	 * <p>
	 * As with the other stroke parameters, the current line width is examined by
	 * cairo_stroke(), cairo_stroke_extents(), and cairo_stroke_to_path(), but does
	 * not have any effect during path construction.
	 * <p>
	 * The default line width value is 2.0.
	 * 
	 * @param width a line width
	 * @since 1.0
	 */
	public void setLineWidth(double width) {
		try {
			cairo_set_line_width.invoke(handle(), width);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_line_width = Interop.downcallHandle("cairo_set_line_width",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * This function returns the current line width value exactly as set by
	 * cairo_set_line_width(). Note that the value is unchanged even if the CTM has
	 * changed between the calls to cairo_set_line_width() and
	 * cairo_get_line_width().
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
	 * If the current line join style is set to CAIRO_LINE_JOIN_MITER (see
	 * cairo_set_line_join()), the miter limit is used to determine whether the
	 * lines should be joined with a bevel instead of a miter. Cairo divides the
	 * length of the miter by the line width. If the result is greater than the
	 * miter limit, the style is converted to a bevel.
	 * <p>
	 * As with the other stroke parameters, the current line miter limit is examined
	 * by cairo_stroke(), cairo_stroke_extents(), and cairo_stroke_to_path(), but
	 * does not have any effect during path construction.
	 * <p>
	 * The default miter limit value is 10.0, which will convert joins with interior
	 * angles less than 11 degrees to bevels instead of miters. For reference, a
	 * miter limit of 2.0 makes the miter cutoff at 60 degrees, and a miter limit of
	 * 1.414 makes the cutoff at 90 degrees.
	 * <p>
	 * A miter limit for a desired angle can be computed as: miter limit =
	 * 1/sin(angle/2)
	 * 
	 * @param limit miter limit to set
	 * @since 1.0
	 */
	public void setMiterLimit(double limit) {
		try {
			cairo_set_miter_limit.invoke(handle(), limit);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_miter_limit = Interop.downcallHandle("cairo_set_miter_limit",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Gets the current miter limit, as set by cairo_set_miter_limit().
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
	 * cairo_operator_t for details on the semantics of each available compositing
	 * operator.
	 * <p>
	 * The default operator is CAIRO_OPERATOR_OVER.
	 * 
	 * @param op a compositing operator, specified as a cairo_operator_t
	 * @since 1.0
	 */
	public void setOperator(Operator op) {
		try {
			cairo_set_operator.invoke(handle(), op.ordinal());
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
			return Operator.values()[result];
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
	 * @since 1.0
	 */
	public void setTolerance(double tolerance) {
		try {
			cairo_set_tolerance.invoke(handle(), tolerance);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_set_tolerance = Interop.downcallHandle("cairo_set_tolerance",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Gets the current tolerance value, as set by cairo_set_tolerance().
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
	 * the current path as it would be filled by cairo_fill() and according to the
	 * current fill rule (see cairo_set_fill_rule()).
	 * <p>
	 * After cairo_clip(), the current path will be cleared from the cairo context.
	 * <p>
	 * The current clip region affects all drawing operations by effectively masking
	 * out any changes to the surface that are outside the current clip region.
	 * <p>
	 * Calling cairo_clip() can only make the clip region smaller, never larger. But
	 * the current clip is part of the graphics state, so a temporary restriction of
	 * the clip region can be achieved by calling cairo_clip() within a
	 * cairo_save()/cairo_restore() pair. The only other means of increasing the
	 * size of the clip region is cairo_reset_clip().
	 * 
	 * @since 1.0
	 */
	public void clip() {
		try {
			cairo_clip.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_clip = Interop.downcallHandle("cairo_clip",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Establishes a new clip region by intersecting the current clip region with
	 * the current path as it would be filled by cairo_fill() and according to the
	 * current fill rule (see cairo_set_fill_rule()).
	 * <p>
	 * Unlike cairo_clip(), cairo_clip_preserve() preserves the path within the
	 * cairo context.
	 * <p>
	 * The current clip region affects all drawing operations by effectively masking
	 * out any changes to the surface that are outside the current clip region.
	 * <p>
	 * Calling cairo_clip_preserve() can only make the clip region smaller, never
	 * larger. But the current clip is part of the graphics state, so a temporary
	 * restriction of the clip region can be achieved by calling
	 * cairo_clip_preserve() within a cairo_save()/cairo_restore() pair. The only
	 * other means of increasing the size of the clip region is cairo_reset_clip().
	 * 
	 * @since 1.0
	 */
	public void clipPreserve() {
		try {
			cairo_clip_preserve.invoke(handle());
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
				double x1 = x1Ptr.get(ValueLayout.JAVA_DOUBLE, 0);
				double y1 = y1Ptr.get(ValueLayout.JAVA_DOUBLE, 0);
				double x2 = x2Ptr.get(ValueLayout.JAVA_DOUBLE, 0);
				double y2 = y2Ptr.get(ValueLayout.JAVA_DOUBLE, 0);
				return new Rectangle(x1, y1, x2 - x1, y2 - y1);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_clip_extents = Interop.downcallHandle("cairo_clip_extents",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Tests whether the given point is inside the area that would be visible
	 * through the current clip, i.e. the area that would be filled by a
	 * cairo_paint() operation.
	 * 
	 * @see {@link #clip()}, and {@link #clipPreserve()}
	 * @param x X coordinate of the point to test
	 * @param y Y coordinate of the point to test
	 * @return True if the point is inside, false if outside.
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
	 * Note that code meant to be reusable should not call cairo_reset_clip() as it
	 * will cause results unexpected by higher-level code which calls cairo_clip().
	 * Consider using cairo_save() and cairo_restore() around cairo_clip() as a more
	 * robust means of temporarily restricting the clip region.
	 * 
	 * @since 1.0
	 */
	public void resetClip() {
		try {
			cairo_reset_clip.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_reset_clip = Interop.downcallHandle("cairo_reset_clip",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Gets the current clip region as a list of rectangles in user coordinates.
	 * Never returns NULL.
	 * <p>
	 * The status in the list may be CAIRO_STATUS_CLIP_NOT_REPRESENTABLE to indicate
	 * that the clip region cannot be represented as a list of user-space
	 * rectangles. The status may have other values to indicate other errors.
	 * 
	 * @return the current clip region as a list of rectangles in user coordinates
	 * @since 1.4
	 */
	public RectangleList copyClipRectangleList() {
		try {
			MemorySegment result = (MemorySegment) cairo_copy_clip_rectangle_list.invoke(handle());
			return new RectangleList(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_copy_clip_rectangle_list = Interop.downcallHandle(
			"cairo_copy_clip_rectangle_list", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * A drawing operator that fills the current path according to the current fill
	 * rule, (each sub-path is implicitly closed before being filled). After
	 * cairo_fill(), the current path will be cleared from the cairo context. See
	 * cairo_set_fill_rule() and cairo_fill_preserve().
	 * 
	 * @since 1.0
	 */
	public void fill() {
		try {
			cairo_fill.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_fill = Interop.downcallHandle("cairo_fill",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * A drawing operator that fills the current path according to the current fill
	 * rule, (each sub-path is implicitly closed before being filled). Unlike
	 * cairo_fill(), cairo_fill_preserve() preserves the path within the cairo
	 * context.
	 * 
	 * @see {@link #setFillRule(FillRule)} and {@link #fill()}.
	 * @since 1.0
	 */
	public void fillPreserve() {
		try {
			cairo_fill_preserve.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_fill_preserve = Interop.downcallHandle("cairo_fill_preserve",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Computes a bounding box in user coordinates covering the area that would be
	 * affected, (the "inked" area), by a cairo_fill() operation given the current
	 * path and fill parameters. If the current path is empty, returns an empty
	 * rectangle ((0,0), (0,0)). Surface dimensions and clipping are not taken into
	 * account.
	 * <p>
	 * Contrast with cairo_path_extents(), which is similar, but returns non-zero
	 * extents for some paths with no inked area, (such as a simple line segment).
	 * <p>
	 * Note that cairo_fill_extents() must necessarily do more work to compute the
	 * precise inked areas in light of the fill rule, so cairo_path_extents() may be
	 * more desirable for sake of performance if the non-inked path extents are
	 * desired.
	 * 
	 * @see {@link #fill()}, {@link #setFillRule(FillRule)} and
	 *      {@link #fillPreserve()}.
	 * @return The resulting extents
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
				double x1 = x1Ptr.get(ValueLayout.JAVA_DOUBLE, 0);
				double y1 = y1Ptr.get(ValueLayout.JAVA_DOUBLE, 0);
				double x2 = x2Ptr.get(ValueLayout.JAVA_DOUBLE, 0);
				double y2 = y2Ptr.get(ValueLayout.JAVA_DOUBLE, 0);
				return new Rectangle(x1, y1, x2 - x1, y2 - y1);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_fill_extents = Interop.downcallHandle("cairo_fill_extents",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Tests whether the given point is inside the area that would be affected by a
	 * cairo_fill() operation given the current path and filling parameters. Surface
	 * dimensions and clipping are not taken into account.
	 * 
	 * @see {@link #fill()}, {@link #setFillRule(FillRule)} and
	 *      {@link #fillPreserve()}.
	 * @param x X coordinate of the point to test
	 * @param y Y coordinate of the point to test
	 * @return true if the point is inside, or false if outside.
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
	 * @param pattern a cairo_pattern_t
	 * @since 1.0
	 */
	public void mask(Pattern pattern) {
		try {
			cairo_mask.invoke(handle(), pattern.handle());
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
	 * @param surface  a cairo_surface_t
	 * @param surfaceX X coordinate at which to place the origin of surface
	 * @param surfaceY Y coordinate at which to place the origin of surface
	 * @since 1.0
	 */
	public void maskSurface(Surface surface, double surfaceX, double surfaceY) {
		try {
			cairo_mask_surface.invoke(handle(), surface.handle(), surfaceX, surfaceY);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_mask_surface = Interop.downcallHandle("cairo_mask_surface",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * A drawing operator that paints the current source everywhere within the
	 * current clip region.
	 * 
	 * @since 1.0
	 */
	public void paint() {
		try {
			cairo_paint.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_paint = Interop.downcallHandle("cairo_paint",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * A drawing operator that paints the current source everywhere within the
	 * current clip region using a mask of constant alpha value alpha . The effect
	 * is similar to cairo_paint(), but the drawing is faded out using the alpha
	 * value.
	 * 
	 * @param alpha alpha value, between 0 (transparent) and 1 (opaque)
	 * @since 1.0
	 */
	public void paintWithAlpha(double alpha) {
		try {
			cairo_paint_with_alpha.invoke(handle(), alpha);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_paint_with_alpha = Interop.downcallHandle("cairo_paint_with_alpha",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * A drawing operator that strokes the current path according to the current
	 * line width, line join, line cap, and dash settings. After cairo_stroke(), the
	 * current path will be cleared from the cairo context.
	 * <p>
	 * Note: Degenerate segments and sub-paths are treated specially and provide a
	 * useful result. These can result in two different situations:
	 * <ol>
	 * <li>Zero-length "on" segments set in cairo_set_dash(). If the cap style is
	 * CAIRO_LINE_CAP_ROUND or CAIRO_LINE_CAP_SQUARE then these segments will be
	 * drawn as circular dots or squares respectively. In the case of
	 * CAIRO_LINE_CAP_SQUARE, the orientation of the squares is determined by the
	 * direction of the underlying path.
	 * <li>A sub-path created by cairo_move_to() followed by either a
	 * cairo_close_path() or one or more calls to cairo_line_to() to the same
	 * coordinate as the cairo_move_to(). If the cap style is CAIRO_LINE_CAP_ROUND
	 * then these sub-paths will be drawn as circular dots. Note that in the case of
	 * CAIRO_LINE_CAP_SQUARE a degenerate sub-path will not be drawn at all, (since
	 * the correct orientation is indeterminate).
	 * </ol>
	 * In no case will a cap style of CAIRO_LINE_CAP_BUTT cause anything to be drawn
	 * in the case of either degenerate segments or sub-paths.
	 * 
	 * @see {@link #setLineWidth(double)}, {@link #setLineJoin(LineJoin)},
	 *      {@link #setLineCap(LineCap)}, {@link #setDash(Dash)}, and
	 *      {@link #strokePreserve()}.
	 * @since 1.0
	 */
	public void stroke() {
		try {
			cairo_stroke.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_stroke = Interop.downcallHandle("cairo_stroke",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * A drawing operator that strokes the current path according to the current
	 * line width, line join, line cap, and dash settings. Unlike cairo_stroke(),
	 * cairo_stroke_preserve() preserves the path within the cairo context.
	 * 
	 * @see {@link #setLineWidth(double)}, {@link #setLineJoin(LineJoin)},
	 *      {@link #setLineCap(LineCap)}, {@link #setDash(Dash)}, and
	 *      {@link #stroke()}.
	 * @since 1.0
	 */
	public void strokePreserve() {
		try {
			cairo_stroke_preserve.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_stroke_preserve = Interop.downcallHandle("cairo_stroke_preserve",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Computes a bounding box in user coordinates covering the area that would be
	 * affected, (the "inked" area), by a cairo_stroke() operation given the current
	 * path and stroke parameters. If the current path is empty, returns an empty
	 * rectangle ((0,0), (0,0)). Surface dimensions and clipping are not taken into
	 * account.
	 * <p>
	 * Note that if the line width is set to exactly zero, then
	 * cairo_stroke_extents() will return an empty rectangle. Contrast with
	 * cairo_path_extents() which can be used to compute the non-empty bounds as the
	 * line width approaches zero.
	 * <p>
	 * Note that cairo_stroke_extents() must necessarily do more work to compute the
	 * precise inked areas in light of the stroke parameters, so
	 * cairo_path_extents() may be more desirable for sake of performance if
	 * non-inked path extents are desired.
	 * 
	 * @see {@link #stroke()}, {@link #setLineWidth(double)},
	 *      {@link #setLineJoin(LineJoin)}, {@link #setLineCap(LineCap)},
	 *      {@link #setDash(Dash)}, and {@link #strokePreserve()}.
	 * @return The resulting extents
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
				double x1 = x1Ptr.get(ValueLayout.JAVA_DOUBLE, 0);
				double y1 = y1Ptr.get(ValueLayout.JAVA_DOUBLE, 0);
				double x2 = x2Ptr.get(ValueLayout.JAVA_DOUBLE, 0);
				double y2 = y2Ptr.get(ValueLayout.JAVA_DOUBLE, 0);
				return new Rectangle(x1, y1, x2 - x1, y2 - y1);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_stroke_extents = Interop.downcallHandle("cairo_stroke_extents",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Tests whether the given point is inside the area that would be affected by a
	 * cairo_stroke() operation given the current path and stroking parameters.
	 * Surface dimensions and clipping are not taken into account.
	 * 
	 * @see {@link #stroke()}, {@link #setLineWidth(double)},
	 *      {@link #setLineJoin(LineJoin)}, {@link #setLineCap(LineCap)},
	 *      {@link #setDash(Dash)}, and {@link #strokePreserve()}.
	 * @param x X coordinate of the point to test
	 * @param y Y coordinate of the point to test
	 * @return true if the point is inside, or false if outside.
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
	 * page too. Use cairo_show_page() if you want to get an empty page after the
	 * emission.
	 * <p>
	 * This is a convenience function that simply calls cairo_surface_copy_page() on
	 * this Context's target.
	 * 
	 * @since 1.0
	 */
	public void copyPage() {
		try {
			cairo_copy_page.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_copy_page = Interop.downcallHandle("cairo_copy_page",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Emits and clears the current page for backends that support multiple pages.
	 * Use cairo_copy_page() if you don't want to clear the page.
	 * <p>
	 * This is a convenience function that simply calls cairo_surface_show_page() on
	 * cr 's target.
	 * 
	 * @since 1.0
	 */
	public void showPage() {
		try {
			cairo_show_page.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_show_page = Interop.downcallHandle("cairo_show_page",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Creates a copy of the current path and returns it to the user as a
	 * cairo_path_t. See cairo_path_data_t for hints on how to iterate over the
	 * returned data structure.
	 * <p>
	 * This function will always return a valid pointer, but the result will have no
	 * data (data==NULL and num_data==0), if either of the following conditions
	 * hold:
	 * <ol>
	 * <li>If there is insufficient memory to copy the path. In this case
	 * path->status will be set to CAIRO_STATUS_NO_MEMORY.
	 * <li>If cr is already in an error state. In this case path->status will
	 * contain the same status that would be returned by cairo_status().
	 * </ol>
	 * 
	 * @return the copy of the current path.
	 * @since 1.0
	 */
	public Path copyPath() {
		try {
			MemorySegment result = (MemorySegment) cairo_copy_path.invoke(handle());
			Path path = new Path(result);
			path.takeOwnership();
			return path;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_copy_path = Interop.downcallHandle("cairo_copy_path",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);
	
	/**
	 * Gets a flattened copy of the current path and returns it to the user as a
	 * cairo_path_t. See cairo_path_data_t for hints on how to iterate over the
	 * returned data structure.
	 * <p>
	 * This function is like cairo_copy_path() except that any curves in the path
	 * will be approximated with piecewise-linear approximations, (accurate to
	 * within the current tolerance value). That is, the result is guaranteed to not
	 * have any elements of type CAIRO_PATH_CURVE_TO which will instead be replaced
	 * by a series of CAIRO_PATH_LINE_TO elements.
	 * <p>
	 * This function will always return a valid pointer, but the result will have no
	 * data (data==NULL and num_data==0), if either of the following conditions
	 * hold:
	 * <ol>
	 * <li>If there is insufficient memory to copy the path. In this case
	 * path->status will be set to CAIRO_STATUS_NO_MEMORY.
	 * <li>If cr is already in an error state. In this case path->status will
	 * contain the same status that would be returned by cairo_status().
	 * </ol>
	 * 
	 * @return the copy of the current path.
	 * @since 1.0
	 */
	public Path copyPathFlat() {
		try {
			MemorySegment result = (MemorySegment) cairo_copy_path_flat.invoke(handle());
			Path path = new Path(result);
			path.takeOwnership();
			return path;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_copy_path_flat = Interop.downcallHandle("cairo_copy_path_flat",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);
	
	/**
	 * Append the path onto the current path. The path may be either the return
	 * value from one of cairo_copy_path() or cairo_copy_path_flat() or it may be
	 * constructed manually. See cairo_path_t for details on how the path data
	 * structure should be initialized, and note that path->status must be
	 * initialized to CAIRO_STATUS_SUCCESS.
	 * 
	 * @param path path to be appended
	 * @since 1.0
	 */
	public void appendPath(Path path) {
		try {
			cairo_append_path.invoke(handle(), path.handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_append_path = Interop.downcallHandle("cairo_append_path",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);
	
	/**
	 * Returns whether a current point is defined on the current path. See
	 * cairo_get_current_point() for details on the current point.
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
	 * is no defined current point or if cr is in an error status, x and y will both
	 * be set to 0.0. It is possible to check this in advance with
	 * cairo_has_current_point().
	 * <p>
	 * Most path construction functions alter the current point. See the following
	 * for details on how they affect the current point: cairo_new_path(),
	 * cairo_new_sub_path(), cairo_append_path(), cairo_close_path(),
	 * cairo_move_to(), cairo_line_to(), cairo_curve_to(), cairo_rel_move_to(),
	 * cairo_rel_line_to(), cairo_rel_curve_to(), cairo_arc(), cairo_arc_negative(),
	 * cairo_rectangle(), cairo_text_path(), cairo_glyph_path(),
	 * cairo_stroke_to_path().
	 * <p>
	 * Some functions use and alter the current point but do not otherwise change
	 * current path: cairo_show_text().
	 * <p>
	 * Some functions unset the current path and as a result, current point:
	 * cairo_fill(), cairo_stroke().
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
	 * @since 1.0
	 */
	public void newPath() {
		try {
			cairo_new_path.invoke(handle());
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
	 * started with cairo_move_to().
	 * <p>
	 * A call to cairo_new_sub_path() is particularly useful when beginning a new
	 * sub-path with one of the cairo_arc() calls. This makes things easier as it is
	 * no longer necessary to manually compute the arc's initial coordinates for a
	 * call to cairo_move_to().
	 * 
	 * @since 1.2
	 */
	public void newSubPath() {
		try {
			cairo_new_sub_path.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_new_sub_path = Interop.downcallHandle("cairo_new_sub_path",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Adds a line segment to the path from the current point to the beginning of
	 * the current sub-path, (the most recent point passed to cairo_move_to()), and
	 * closes this sub-path. After this call the current point will be at the joined
	 * endpoint of the sub-path.
	 * <p>
	 * The behavior of cairo_close_path() is distinct from simply calling
	 * cairo_line_to() with the equivalent coordinate in the case of stroking. When
	 * a closed sub-path is stroked, there are no caps on the ends of the sub-path.
	 * Instead, there is a line join connecting the final and initial segments of
	 * the sub-path.
	 * <p>
	 * If there is no current point before the call to cairo_close_path(), this
	 * function will have no effect.
	 * <p>
	 * Note: As of cairo version 1.2.4 any call to cairo_close_path() will place an
	 * explicit MOVE_TO element into the path immediately after the CLOSE_PATH
	 * element, (which can be seen in cairo_copy_path() for example). This can
	 * simplify path processing in some cases as it may not be necessary to save the
	 * "last move_to point" during processing as the MOVE_TO immediately after the
	 * CLOSE_PATH will provide that point.
	 * 
	 * @since 1.0
	 */
	public void closePath() {
		try {
			cairo_close_path.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_close_path = Interop.downcallHandle("cairo_close_path",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);
	
	/**
	 * Adds a circular arc of the given radius to the current path. The arc is
	 * centered at (xc , yc ), begins at angle1 and proceeds in the direction of
	 * increasing angles to end at angle2 . If angle2 is less than angle1 it will be
	 * progressively increased by 2*M_PI until it is greater than angle1 .
	 * <p>
	 * If there is a current point, an initial line segment will be added to the
	 * path to connect the current point to the beginning of the arc. If this
	 * initial line is undesired, it can be avoided by calling cairo_new_sub_path()
	 * before calling cairo_arc().
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
	 * cairo_arc_negative() to get the arc in the direction of decreasing angles.
	 * <p>
	 * The arc is circular in user space. To achieve an elliptical arc, you can
	 * scale the current transformation matrix by different amounts in the X and Y
	 * directions. For example, to draw an ellipse in the box given by x , y , width
	 * , height :
	 * 
	 * {@snippet :
	 * cairo_save(cr);
	 * cairo_translate(cr, x + width / 2., y + height / 2.);
	 * cairo_scale(cr, width / 2., height / 2.);
	 * cairo_arc(cr, 0., 0., 1., 0., 2 * M_PI);
	 * cairo_restore(cr);
	 * }
	 * 
	 * @param xc     X position of the center of the arc
	 * @param yc     Y position of the center of the arc
	 * @param radius the radius of the arc
	 * @param angle1 the start angle, in radians
	 * @param angle2 the end angle, in radians
	 * @since 1.0
	 */
	public void arc(double xc, double yc, double radius, double angle1, double angle2) {
		try {
			cairo_arc.invoke(handle(), xc, yc, radius, angle1, angle2);
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
	 * centered at (xc , yc ), begins at angle1 and proceeds in the direction of
	 * decreasing angles to end at angle2 . If angle2 is greater than angle1 it will
	 * be progressively decreased by 2*M_PI until it is less than angle1 .
	 * <p>
	 * See cairo_arc() for more details. This function differs only in the direction
	 * of the arc between the two angles.
	 * 
	 * @param xc     X position of the center of the arc
	 * @param yc     Y position of the center of the arc
	 * @param radius the radius of the arc
	 * @param angle1 the start angle, in radians
	 * @param angle2 the end angle, in radians
	 * @since 1.0
	 */
	public void arcNegative(double xc, double yc, double radius, double angle1, double angle2) {
		try {
			cairo_arc_negative.invoke(handle(), xc, yc, radius, angle1, angle2);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_arc_negative = Interop.downcallHandle(
			"cairo_arc_negative", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Adds a cubic Bézier spline to the path from the current point to position (x3
	 * , y3 ) in user-space coordinates, using (x1 , y1 ) and (x2 , y2 ) as the
	 * control points. After this call the current point will be (x3 , y3 ).
	 * <p>
	 * If there is no current point before the call to cairo_curve_to() this
	 * function will behave as if preceded by a call to cairo_move_to(cr , x1 , y1
	 * ).
	 * 
	 * @param x1 the X coordinate of the first control point
	 * @param y1 the Y coordinate of the first control point
	 * @param x2 the X coordinate of the second control point
	 * @param y2 the Y coordinate of the second control point
	 * @param x3 the X coordinate of the end of the curve
	 * @param y3 the Y coordinate of the end of the curve
	 * @since 1.0
	 */
	public void curveTo(double x1, double y1, double x2, double y2, double x3, double y3) {
		try {
			cairo_curve_to.invoke(handle(), x1, y1, x2, y2, x3, y3);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_curve_to = Interop.downcallHandle("cairo_curve_to",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE),
			false);
	
	/**
	 * Adds a line to the path from the current point to position (x , y ) in
	 * user-space coordinates. After this call the current point will be (x , y ).
	 * <p>
	 * If there is no current point before the call to cairo_line_to() this function
	 * will behave as cairo_move_to(cr , x , y ).
	 * 
	 * @param x the X coordinate of the end of the new line
	 * @param y the Y coordinate of the end of the new line
	 * @since 1.0
	 */
	public void lineTo(double x, double y) {
		try {
			cairo_line_to.invoke(handle(), x, y);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_line_to = Interop.downcallHandle("cairo_line_to",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Begin a new sub-path. After this call the current point will be (x , y ).
	 * 
	 * @param x the X coordinate of the new position
	 * @param y the Y coordinate of the new position
	 * @since 1.0
	 */
	public void moveTo(double x, double y) {
		try {
			cairo_move_to.invoke(handle(), x, y);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_move_to = Interop.downcallHandle("cairo_move_to",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE), false);	
	
	/**
	 * Adds a closed sub-path rectangle of the given size to the current path at
	 * position (x , y ) in user-space coordinates.
	 * 
	 * This function is logically equivalent to:
	 * {@snippet :
	 * cairo_move_to(cr, x, y);
	 * cairo_rel_line_to(cr, width, 0);
	 * cairo_rel_line_to(cr, 0, height);
	 * cairo_rel_line_to(cr, -width, 0);
	 * cairo_close_path(cr);
	 * }
	 * 
	 * @param x      the X coordinate of the top left corner of the rectangle
	 * @param y      the Y coordinate of the top left corner of the rectangle
	 * @param width  the width of the rectangle
	 * @param height the height of the rectangle
	 * @since 1.0
	 */
	public void rectangle(double x, double y, double width, double height) {
		try {
			cairo_rectangle.invoke(handle(), x, y, width, height);
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
	 * filled, achieves an effect similar to that of cairo_show_glyphs().
	 * 
	 * @param glyphs array of glyphs to show
	 * @since 1.0
	 */
	public void glyphPath(Glyph[] glyphs) {
		if (glyphs == null || glyphs.length == 0) {
			return;
		}
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment glyphsPtr = arena.allocateArray(ValueLayout.ADDRESS, glyphs.length);
				for (int i = 0; i < glyphs.length; i++) {
					glyphsPtr.setAtIndex(ValueLayout.ADDRESS, i, glyphs[i].handle());
				}
				cairo_glyph_path.invoke(handle(), glyphsPtr, glyphs.length);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_glyph_path = Interop.downcallHandle("cairo_glyph_path",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT), false);

	/**
	 * Adds closed paths for text to the current path. The generated path if filled,
	 * achieves an effect similar to that of cairo_show_text().
	 * <p>
	 * Text conversion and positioning is done similar to cairo_show_text().
	 * <p>
	 * Like cairo_show_text(), After this call the current point is moved to the
	 * origin of where the next glyph would be placed in this same progression. That
	 * is, the current point will be at the origin of the final glyph offset by its
	 * advance values. This allows for chaining multiple calls to to
	 * cairo_text_path() without having to set current point in between.
	 * <p>
	 * Note: The cairo_text_path() function call is part of what the cairo designers
	 * call the "toy" text API. It is convenient for short demos and simple
	 * programs, but it is not expected to be adequate for serious text-using
	 * applications. See cairo_glyph_path() for the "real" text path API in cairo.
	 * 
	 * @param string a string of text
	 * @since 1.0
	 */
	public void textPath(String string) {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment utf8 = Interop.allocateNativeString(string, arena);
				cairo_text_path.invoke(handle(), utf8);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_text_path = Interop.downcallHandle("cairo_text_path",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Relative-coordinate version of cairo_curve_to(). All offsets are relative to
	 * the current point. Adds a cubic Bézier spline to the path from the current
	 * point to a point offset from the current point by (dx3 , dy3 ), using points
	 * offset by (dx1 , dy1 ) and (dx2 , dy2 ) as the control points. After this
	 * call the current point will be offset by (dx3 , dy3 ).
	 * <p>
	 * Given a current point of (x, y), cairo_rel_curve_to(cr , dx1 , dy1 , dx2 ,
	 * dy2 , dx3 , dy3 ) is logically equivalent to cairo_curve_to(cr , x+dx1 ,
	 * y+dy1 , x+dx2 , y+dy2 , x+dx3 , y+dy3 ).
	 * <p>
	 * It is an error to call this function with no current point. Doing so will
	 * cause cr to shutdown with a status of CAIRO_STATUS_NO_CURRENT_POINT.
	 * 
	 * @param dx1 the X offset to the first control point
	 * @param dy1 the Y offset to the first control point
	 * @param dx2 the X offset to the second control point
	 * @param dy2 the Y offset to the second control point
	 * @param dx3 the X offset to the end of the curve
	 * @param dy3 the Y offset to the end of the curve
	 * @since 1.0
	 */
	public void relCurveTo(double dx1, double dy1, double dx2, double dy2, double dx3, double dy3) {
		try {
			cairo_rel_curve_to.invoke(handle(), dx1, dy1, dx2, dy2, dx3, dy3);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_rel_curve_to = Interop.downcallHandle("cairo_rel_curve_to",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Relative-coordinate version of cairo_line_to(). Adds a line to the path from
	 * the current point to a point that is offset from the current point by (dx ,
	 * dy ) in user space. After this call the current point will be offset by (dx ,
	 * dy ).
	 * <p>
	 * Given a current point of (x, y), cairo_rel_line_to(cr , dx , dy ) is
	 * logically equivalent to cairo_line_to(cr , x + dx , y + dy ).
	 * <p>
	 * It is an error to call this function with no current point. Doing so will
	 * cause cr to shutdown with a status of CAIRO_STATUS_NO_CURRENT_POINT.
	 * 
	 * @param dx the X offset to the end of the new line
	 * @param dy the Y offset to the end of the new line
	 * @since 1.0
	 */
	public void relLineTo(double dx, double dy) {
		try {
			cairo_rel_line_to.invoke(handle(), dx, dy);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_rel_line_to = Interop.downcallHandle("cairo_rel_line_to",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Begin a new sub-path. After this call the current point will offset by (x , y
	 * ).
	 * <p>
	 * Given a current point of (x, y), cairo_rel_move_to(cr , dx , dy ) is
	 * logically equivalent to cairo_move_to(cr , x + dx , y + dy ).
	 * <p>
	 * It is an error to call this function with no current point. Doing so will
	 * cause cr to shutdown with a status of CAIRO_STATUS_NO_CURRENT_POINT.
	 * 
	 * @param dx the X offset
	 * @param dy the Y offset
	 * @since 1.0
	 */
	public void relMoveTo(double dx, double dy) {
		try {
			cairo_rel_move_to.invoke(handle(), dx, dy);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_rel_move_to = Interop.downcallHandle("cairo_rel_move_to",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Computes a bounding box in user-space coordinates covering the points on the
	 * current path. If the current path is empty, returns an empty rectangle
	 * ((0,0), (0,0)). Stroke parameters, fill rule, surface dimensions and clipping
	 * are not taken into account.
	 * <p>
	 * Contrast with cairo_fill_extents() and cairo_stroke_extents() which return
	 * the extents of only the area that would be "inked" by the corresponding
	 * drawing operations.
	 * <p>
	 * The result of cairo_path_extents() is defined as equivalent to the limit of
	 * cairo_stroke_extents() with CAIRO_LINE_CAP_ROUND as the line width approaches
	 * 0.0, (but never reaching the empty-rectangle returned by
	 * cairo_stroke_extents() for a line width of 0.0).
	 * <p>
	 * Specifically, this means that zero-area sub-paths such as
	 * cairo_move_to();cairo_line_to() segments, (even degenerate cases where the
	 * coordinates to both calls are identical), will be considered as contributing
	 * to the extents. However, a lone cairo_move_to() will not contribute to the
	 * results of cairo_path_extents().
	 * 
	 * @param x1 left of the resulting extents
	 * @param y1 top of the resulting extents
	 * @param x2 right of the resulting extents
	 * @param y2 bottom of the resulting extents
	 * @since 1.6
	 */
	public void pathExtents(double x1, double y1, double x2, double y2) {
		try {
			cairo_path_extents.invoke(handle(), x1, y1, x2, y2);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_path_extents = Interop.downcallHandle("cairo_path_extents",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE,
					ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE),
			false);
}
