package org.freedesktop.cairo.surfaces;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import org.freedesktop.cairo.drawing.Point;
import org.freedesktop.cairo.drawing.Status;
import org.freedesktop.cairo.fonts.FontOptions;
import org.freedesktop.cairo.utilities.RectangleInt;

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.ProxyInstance;

/**
 * Base class for surfaces.
 * <p>
 * Surface is the abstract type representing all different drawing targets that
 * cairo can render to. The actual drawings are performed using a cairo context.
 * <p>
 * A cairo surface is created by using backend-specific constructors, typically
 * of the form cairo_backend_surface_create().
 * <p>
 * Most surface types allow accessing the surface without using Cairo functions.
 * If you do this, keep in mind that it is mandatory that you call
 * cairo_surface_flush() before reading from or writing to the surface and that
 * you must use cairo_surface_mark_dirty() after modifying it.
 * <p>
 * Note that for surface types other than ImageSurface it might be necessary to
 * acquire the surface's device first. See cairo_device_acquire() for a
 * discussion of devices.
 */
public class Surface extends ProxyInstance {

	{
		Interop.ensureInitialized();
	}

	/**
	 * Constructor used internally to instantiate a java Surface object for a native
	 * {@code cairo_surface_t} instance
	 * 
	 * @param address the memory address of the native {@code cairo_surface_t}
	 *                instance
	 */
	public Surface(MemorySegment address) {
		super(address);
		setDereferenceFunc("cairo_surface_destroy");
	}

	/**
	 * Create a new surface that is as compatible as possible with an existing
	 * surface. For example the new surface will have the same device scale,
	 * fallback resolution and font options as other . Generally, the new surface
	 * will also use the same backend as other , unless that is not possible for
	 * some reason. The type of the returned surface may be examined with
	 * cairo_surface_get_type().
	 * <p>
	 * Initially the surface contents are all 0 (transparent if contents have
	 * transparency, black otherwise.)
	 * <p>
	 * Use cairo_surface_create_similar_image() if you need an image surface which
	 * can be painted quickly to the target surface.
	 * 
	 * @param other   an existing surface used to select the backend of the new
	 *                surface
	 * @param content the content for the new surface
	 * @param width   width of the new surface, (in device-space units)
	 * @param height  height of the new surface (in device-space units)
	 * @return the newly allocated surface. This function always returns a valid
	 *         pointer, but it will return a pointer to a "nil" surface if other is
	 *         already in an error state or any other error occurs.
	 * @since 1.0
	 */
	public static Surface createSimilar(Surface other, Content content, int width, int height) {
		try {
			MemorySegment result = (MemorySegment) cairo_surface_create_similar.invoke(other.handle(),
					content.ordinal(), width, height);
			Surface surface = new Surface(result);
			surface.takeOwnership();
			return surface;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_create_similar = Interop
			.downcallHandle("cairo_surface_create_similar", FunctionDescriptor.of(ValueLayout.ADDRESS,
					ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT), false);

	/**
	 * Create a new image surface that is as compatible as possible for uploading to
	 * and the use in conjunction with an existing surface. However, this surface
	 * can still be used like any normal image surface. Unlike
	 * cairo_surface_create_similar() the new image surface won't inherit the device
	 * scale from {@code other}.
	 * <p>
	 * Initially the surface contents are all 0 (transparent if contents have
	 * transparency, black otherwise.)
	 * <p>
	 * Use cairo_surface_create_similar() if you don't need an image surface.
	 * 
	 * @param other  an existing surface used to select the preference of the new
	 *               surface
	 * @param format the format for the new surface
	 * @param width  width of the new surface, (in pixels)
	 * @param height height of the new surface, (in pixels)
	 * @return the newly allocated surface. This function always returns a valid
	 *         pointer, but it will return a pointer to a "nil" surface if other is
	 *         already in an error state or any other error occurs.
	 * @since 1.12
	 */
	public static Surface createSimilarImage(Surface other, Format format, int width, int height) {
		try {
			MemorySegment result = (MemorySegment) cairo_surface_create_similar_image.invoke(other.handle(),
					format.ordinal(), width, height);
			Surface surface = new Surface(result);
			surface.takeOwnership();
			return surface;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_create_similar_image = Interop
			.downcallHandle(
					"cairo_surface_create_similar_image", FunctionDescriptor.of(ValueLayout.ADDRESS,
							ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT),
					false);

	/**
	 * Create a new surface that is a rectangle within the target surface. All
	 * operations drawn to this surface are then clipped and translated onto the
	 * target surface. Nothing drawn via this sub-surface outside of its bounds is
	 * drawn onto the target surface, making this a useful method for passing
	 * constrained child surfaces to library routines that draw directly onto the
	 * parent surface, i.e. with no further backend allocations, double buffering or
	 * copies.
	 * <p>
	 * <strong> The semantics of subsurfaces have not been finalized yet unless the
	 * rectangle is in full device units, is contained within the extents of the
	 * target surface, and the target or subsurface's device transforms are not
	 * changed. </strong>
	 * 
	 * @param target an existing surface for which the sub-surface will point to
	 * @param x      the x-origin of the sub-surface from the top-left of the target
	 *               surface (in device-space units)
	 * @param y      the y-origin of the sub-surface from the top-left of the target
	 *               surface (in device-space units)
	 * @param width  width of the sub-surface (in device-space units)
	 * @param height height of the sub-surface (in device-space units)
	 * @return the newly allocated surface. This function always returns a valid
	 *         pointer, but it will return a pointer to a "nil" surface if other is
	 *         already in an error state or any other error occurs.
	 * @since 1.10
	 */
	public static Surface createForRectangle(Surface target, double x, double y, double width, double height) {
		try {
			MemorySegment result = (MemorySegment) cairo_surface_create_for_rectangle.invoke(target.handle(), x, y,
					width, height);
			Surface surface = new Surface(result);
			surface.takeOwnership();
			return surface;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_create_for_rectangle = Interop.downcallHandle(
			"cairo_surface_create_for_rectangle", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS,
					ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Checks whether an error has previously occurred for this surface.
	 * 
	 * @return CAIRO_STATUS_SUCCESS, CAIRO_STATUS_NULL_POINTER,
	 *         CAIRO_STATUS_NO_MEMORY, CAIRO_STATUS_READ_ERROR,
	 *         CAIRO_STATUS_INVALID_CONTENT, CAIRO_STATUS_INVALID_FORMAT, or
	 *         CAIRO_STATUS_INVALID_VISUAL.
	 * @since 1.0
	 */
	public Status status() {
		try {
			int result = (int) cairo_surface_status.invoke(handle());
			return Status.values()[result];
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_status = Interop.downcallHandle("cairo_surface_status",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * This function finishes the surface and drops all references to external
	 * resources. For example, for the Xlib backend it means that cairo will no
	 * longer access the drawable, which can be freed. After calling
	 * cairo_surface_finish() the only valid operations on a surface are getting and
	 * setting user, referencing and destroying, and flushing and finishing it.
	 * Further drawing to the surface will not affect the surface but will instead
	 * trigger a CAIRO_STATUS_SURFACE_FINISHED error.
	 * <p>
	 * When the last call to cairo_surface_destroy() decreases the reference count
	 * to zero, cairo will call cairo_surface_finish() if it hasn't been called
	 * already, before freeing the resources associated with the surface.
	 * 
	 * @since 1.0
	 */
	public void finish() {
		try {
			cairo_surface_finish.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_finish = Interop.downcallHandle("cairo_surface_finish",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Do any pending drawing for the surface and also restore any temporary
	 * modifications cairo has made to the surface's state. This function must be
	 * called before switching from drawing on the surface with cairo to drawing on
	 * it directly with native APIs, or accessing its memory outside of Cairo. If
	 * the surface doesn't support direct access, then this function does nothing.
	 * 
	 * @since 1.0
	 */
	public void flush() {
		try {
			cairo_surface_flush.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_flush = Interop.downcallHandle("cairo_surface_flush",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * This function returns the device for a surface. See cairo_device_t.
	 * 
	 * @return The device for surface or NULL if the surface does not have an
	 *         associated device.
	 * @since 1.10
	 */
	public Device getDevice() {
		try {
			MemorySegment result = (MemorySegment) cairo_surface_get_device.invoke(handle());
			return new Device(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_get_device = Interop.downcallHandle("cairo_surface_get_device",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Retrieves the default font rendering options for the surface. This allows
	 * display surfaces to report the correct subpixel order for rendering on them,
	 * print surfaces to disable hinting of metrics and so forth. The result can
	 * then be used with cairo_scaled_font_create().
	 * 
	 * @param options a cairo_font_options_t object into which to store the
	 *                retrieved options. All existing values are overwritten
	 * @since 1.0
	 */
	public void getFontOptions(FontOptions options) {
		try {
			cairo_surface_get_font_options.invoke(handle(), options.handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_get_font_options = Interop.downcallHandle(
			"cairo_surface_get_font_options", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS),
			false);

	/**
	 * This function returns the content type of surface which indicates whether the
	 * surface contains color and/or alpha information. See cairo_content_t.
	 * 
	 * @return The content type of surface .
	 * @since 1.2
	 */
	public Content getContent() {
		try {
			int result = (int) cairo_surface_get_content.invoke(handle());
			return Content.values()[result];
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_get_content = Interop.downcallHandle("cairo_surface_get_content",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Tells cairo that drawing has been done to surface using means other than
	 * cairo, and that cairo should reread any cached areas. Note that you must call
	 * cairo_surface_flush() before doing such drawing.
	 * 
	 * @since 1.0
	 */
	public void markDirty() {
		try {
			cairo_surface_mark_dirty.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_mark_dirty = Interop.downcallHandle("cairo_surface_mark_dirty",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Like cairo_surface_mark_dirty(), but drawing has been done only to the
	 * specified rectangle, so that cairo can retain cached contents for other parts
	 * of the surface.
	 * <p>
	 * Any cached clip set on the surface will be reset by this function, to make
	 * sure that future cairo calls have the clip set that they expect.
	 * 
	 * @param x      X coordinate of dirty rectangle
	 * @param y      Y coordinate of dirty rectangle
	 * @param width  width of dirty rectangle
	 * @param height height of dirty rectangle
	 * @since 1.0
	 */
	public void markDirtyRectangle(int x, int y, int width, int height) {
		try {
			cairo_surface_mark_dirty_rectangle.invoke(handle(), x, y, width, height);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_mark_dirty_rectangle = Interop
			.downcallHandle(
					"cairo_surface_mark_dirty_rectangle", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
							ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT),
					false);

	/**
	 * Sets an offset that is added to the device coordinates determined by the CTM
	 * when drawing to surface . One use case for this function is when we want to
	 * create a cairo_surface_t that redirects drawing for a portion of an onscreen
	 * surface to an offscreen surface in a way that is completely invisible to the
	 * user of the cairo API. Setting a transformation via cairo_translate() isn't
	 * sufficient to do this, since functions like cairo_device_to_user() will
	 * expose the hidden offset.
	 * <p>
	 * Note that the offset affects drawing to the surface as well as using the
	 * surface in a source pattern.
	 * 
	 * @param xOffset the offset in the X direction, in device units
	 * @param yOffset the offset in the Y direction, in device units
	 * @since 1.0
	 */
	public void setDeviceOffset(double xOffset, double yOffset) {
		try {
			cairo_surface_set_device_offset.invoke(handle(), xOffset, yOffset);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_set_device_offset = Interop.downcallHandle(
			"cairo_surface_set_device_offset",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * This function returns the previous device offset set by
	 * cairo_surface_set_device_offset().
	 * 
	 * @return the x and y values of the returned Point object contain the offset in
	 *         the X and Y direction, in device units
	 * @since 1.2
	 */
	public Point getDeviceOffset() {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment xPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment yPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				cairo_surface_get_device_offset.invoke(handle(), xPtr, yPtr);
				double x = xPtr.get(ValueLayout.JAVA_DOUBLE, 0);
				double y = yPtr.get(ValueLayout.JAVA_DOUBLE, 0);
				return new Point(x, y);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_get_device_offset = Interop.downcallHandle(
			"cairo_surface_get_device_offset",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * This function returns the previous device offset set by
	 * cairo_surface_set_device_scale().
	 * 
	 * @return the x and y values of the returned Point object contain the scale in
	 *         the X and Y direction, in device units
	 * @since 1.14
	 */
	public Point getDeviceScale() {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment xPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment yPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				cairo_surface_get_device_scale.invoke(handle(), xPtr, yPtr);
				double x = xPtr.get(ValueLayout.JAVA_DOUBLE, 0);
				double y = yPtr.get(ValueLayout.JAVA_DOUBLE, 0);
				return new Point(x, y);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_get_device_scale = Interop.downcallHandle(
			"cairo_surface_get_device_scale",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Sets a scale that is multiplied to the device coordinates determined by the
	 * CTM when drawing to surface . One common use for this is to render to very
	 * high resolution display devices at a scale factor, so that code that assumes
	 * 1 pixel will be a certain size will still work. Setting a transformation via
	 * cairo_translate() isn't sufficient to do this, since functions like
	 * cairo_device_to_user() will expose the hidden scale.
	 * <p>
	 * Note that the scale affects drawing to the surface as well as using the
	 * surface in a source pattern.
	 * 
	 * @param xScale a scale factor in the X direction
	 * @param yScale a scale factor in the Y direction
	 * @since 1.14
	 */
	public void setDeviceScale(double xScale, double yScale) {
		try {
			cairo_surface_set_device_scale.invoke(handle(), xScale, yScale);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_set_device_scale = Interop.downcallHandle(
			"cairo_surface_set_device_scale",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * Set the horizontal and vertical resolution for image fallbacks.
	 * <p>
	 * When certain operations aren't supported natively by a backend, cairo will
	 * fallback by rendering operations to an image and then overlaying that image
	 * onto the output. For backends that are natively vector-oriented, this
	 * function can be used to set the resolution used for these image fallbacks,
	 * (larger values will result in more detailed images, but also larger file
	 * sizes).
	 * <p>
	 * Some examples of natively vector-oriented backends are the ps, pdf, and svg
	 * backends.
	 * <p>
	 * For backends that are natively raster-oriented, image fallbacks are still
	 * possible, but they are always performed at the native device resolution. So
	 * this function has no effect on those backends.
	 * <p>
	 * Note: The fallback resolution only takes effect at the time of completing a
	 * page (with cairo_show_page() or cairo_copy_page()) so there is currently no
	 * way to have more than one fallback resolution in effect on a single page.
	 * <p>
	 * The default fallback resoultion is 300 pixels per inch in both dimensions.
	 * 
	 * @param xPixelsPerInch horizontal setting for pixels per inch
	 * @param yPixelsPerInch vertical setting for pixels per inch
	 * @since 1.2
	 */
	public void setFallbackResolution(double xPixelsPerInch, double yPixelsPerInch) {
		try {
			cairo_surface_set_fallback_resolution.invoke(handle(), xPixelsPerInch, yPixelsPerInch);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_set_fallback_resolution = Interop.downcallHandle(
			"cairo_surface_set_fallback_resolution",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE), false);

	/**
	 * This function returns the previous fallback resolution set by
	 * cairo_surface_set_fallback_resolution(), or default fallback resolution if
	 * never set.
	 * 
	 * @return the x and y values of the returned Point object contain the
	 *         horizontal and vertical pixels per inch
	 * @since 1.8
	 */
	public Point getFallbackResolution() {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment xPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				MemorySegment yPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
				cairo_surface_get_fallback_resolution.invoke(handle(), xPtr, yPtr);
				double x = xPtr.get(ValueLayout.JAVA_DOUBLE, 0);
				double y = yPtr.get(ValueLayout.JAVA_DOUBLE, 0);
				return new Point(x, y);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_get_fallback_resolution = Interop.downcallHandle(
			"cairo_surface_get_fallback_resolution",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * This function returns the type of the backend used to create a surface. See
	 * cairo_surface_type_t for available types.
	 * 
	 * @return The type of {@link Surface}.
	 * @since 1.2
	 */
	public SurfaceType getType() {
		try {
			int result = (int) cairo_surface_get_type.invoke(handle());
			return SurfaceType.values()[result];
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_get_type = Interop.downcallHandle("cairo_surface_get_type",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Emits the current page for backends that support multiple pages, but doesn't
	 * clear it, so that the contents of the current page will be retained for the
	 * next page. Use cairo_surface_show_page() if you want to get an empty page
	 * after the emission.
	 * <p>
	 * There is a convenience function for this that takes a cairo_t, namely
	 * cairo_copy_page().
	 * 
	 * @since 1.6
	 */
	public void copyPage() {
		try {
			cairo_surface_copy_page.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_copy_page = Interop.downcallHandle("cairo_surface_copy_page",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Emits and clears the current page for backends that support multiple pages.
	 * Use cairo_surface_copy_page() if you don't want to clear the page.
	 * <p>
	 * There is a convenience function for this that takes a cairo_t, namely
	 * cairo_show_page().
	 * 
	 * @since 1.6
	 */
	public void showPage() {
		try {
			cairo_surface_show_page.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_show_page = Interop.downcallHandle("cairo_surface_show_page",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Returns whether the surface supports sophisticated cairo_show_text_glyphs()
	 * operations. That is, whether it actually uses the provided text and cluster
	 * data to a cairo_show_text_glyphs() call.
	 * <p>
	 * Note: Even if this function returns {@code false}, a cairo_show_text_glyphs()
	 * operation targeted at surface will still succeed. It just will act like a
	 * cairo_show_glyphs() operation. Users can use this function to avoid computing
	 * UTF-8 text and cluster mapping if the target surface does not use it.
	 * 
	 * @return {@code true} if surface supports cairo_show_text_glyphs(),
	 *         {@code false} otherwise
	 * @since 1.8
	 */
	public boolean hasShowTextGlyphs() {
		try {
			int result = (int) cairo_surface_has_show_text_glyphs.invoke(handle());
			return result != 0;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_has_show_text_glyphs = Interop.downcallHandle(
			"cairo_surface_has_show_text_glyphs", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS),
			false);

	// Todo: implement
	public Status setMimeData() {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	private static final MethodHandle cairo_surface_set_mime_data = Interop
			.downcallHandle("cairo_surface_set_mime_data",
					FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
							ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.ADDRESS),
					false);
	
	// Todo: implement
	public void getMimeData() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	private static final MethodHandle cairo_surface_get_mime_data = Interop
			.downcallHandle("cairo_surface_get_mime_data", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
					ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Return whether this surface supports {@code mime_type}.
	 * 
	 * @param the mime type
	 * @return mimeType {@code true{ if this surface supports {@code mime_type},
	 *         {@code false} otherwise @since 1.12
	 */
	public boolean supportsMimeType(MimeType mimeType) {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment mimeTypePtr = Interop.allocateNativeString(mimeType.toString(), arena);
				int result = (int) cairo_surface_supports_mime_type.invoke(handle(), mimeTypePtr);
				return result != 0;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_supports_mime_type = Interop.downcallHandle(
			"cairo_surface_supports_mime_type",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Returns an image surface that is the most efficient mechanism for modifying
	 * the backing store of the target surface. The region retrieved may be limited
	 * to the extents or NULL for the whole surface.
	 * <p>
	 * Note, the use of the original surface as a target or source whilst it is
	 * mapped is undefined. The result of mapping the surface multiple times is
	 * undefined. Calling cairo_surface_destroy() or cairo_surface_finish() on the
	 * resulting image surface results in undefined behavior. Changing the device
	 * transform of the image surface or of the source surface before the image
	 * surface is unmapped results in undefined behavior.
	 * 
	 * @param extents limit the extraction to an rectangular region
	 * @return the newly allocated surface. This function always returns a valid
	 *         pointer, but it will return a pointer to a "nil" surface if other is
	 *         already in an error state or any other error occurs.
	 * @since 1.12
	 */
	public Surface mapToImage(RectangleInt extents) {
		try {
			MemorySegment result = (MemorySegment) cairo_surface_map_to_image.invoke(handle(), extents.handle());
			return new Surface(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_map_to_image = Interop.downcallHandle("cairo_surface_map_to_image",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);
	
	/**
	 * Unmaps the image surface as returned from cairo_surface_map_to_image().
	 * <p>
	 * The content of the image will be uploaded to the target surface. Afterwards,
	 * the image is destroyed.
	 * <p>
	 * Using an image surface which wasn't returned by cairo_surface_map_to_image()
	 * results in undefined behavior.
	 * 
	 * @param image the currently mapped image
	 * @since 1.12
	 */
	public void unmapImage(Surface image) {
		try {
			cairo_surface_unmap_image.invoke(handle(), image.handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_unmap_image = Interop.downcallHandle("cairo_surface_unmap_image",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);
}
