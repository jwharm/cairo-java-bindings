package org.freedesktop.cairo;

import io.github.jwharm.javagi.base.ProxyInstance;
import io.github.jwharm.javagi.interop.Interop;
import io.github.jwharm.javagi.interop.MemoryCleaner;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.ref.Cleaner;

/**
 * Base class for surfaces.
 * <p>
 * Surface is the abstract type representing all different drawing targets that
 * cairo can render to. The actual drawings are performed using a cairo context.
 * <p>
 * A cairo surface is created by using backend-specific constructors, typically
 * of the form of a static {@code create()} method.
 * <p>
 * Most surface types allow accessing the surface without using Cairo functions.
 * If you do this, keep in mind that it is mandatory that you call
 * {@link Surface#flush()} before reading from or writing to the surface and
 * that you must use {@link Surface#markDirty()} after modifying it.
 * <p>
 * Note that for surface types other than ImageSurface it might be necessary to
 * acquire the surface's device first. See {@link Device#acquire()} for a
 * discussion of devices.
 */
public sealed class Surface extends ProxyInstance implements AutoCloseable
        permits ImageSurface, PDFSurface, PSSurface, RecordingSurface, SVGSurface, ScriptSurface {

    static {
        Cairo.ensureInitialized();
    }

    // Keeps user data keys and values
    private final UserDataStore userDataStore;
    
    // Keep a reference to the target surface during the lifetime of this surface
    @SuppressWarnings("unused")
    private Surface target;

    /**
     * Constructor used internally to instantiate a java Surface object for a native
     * {@code cairo_surface_t} instance
     * 
     * @param address the memory address of the native {@code cairo_surface_t}
     *                instance
     */
    public Surface(MemorySegment address) {
        super(address);
        MemoryCleaner.setFreeFunc(handle(), "cairo_surface_destroy");
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
     * Create a new surface that is as compatible as possible with an existing
     * surface. For example the new surface will have the same device scale,
     * fallback resolution and font options as other . Generally, the new surface
     * will also use the same backend as other , unless that is not possible for
     * some reason. The type of the returned surface may be examined with
     * {@link Surface#getType()}.
     * <p>
     * Initially the surface contents are all 0 (transparent if contents have
     * transparency, black otherwise.)
     * <p>
     * Use {@link #createSimilarImage(Surface, Format, int, int)} if you need an
     * image surface which can be painted quickly to the target surface.
     * 
     * @param other   an existing surface used to select the backend of the new
     *                surface
     * @param content the content for the new surface
     * @param width   width of the new surface, (in device-space units)
     * @param height  height of the new surface (in device-space units)
     * @param <T>     the type of the created Surface
     * @return the newly allocated surface.
     * @since 1.0
     */
    @SuppressWarnings("unchecked")
    public static <T extends Surface> T createSimilar(T other, Content content, int width, int height) {
        if (other == null) {
            return null;
        }
        try {
            MemorySegment result = (MemorySegment) cairo_surface_create_similar.invoke(other.handle(), content.getValue(),
                    width, height);
            Surface surface = new Surface(result);
            // Try to instantiate the correct class, based on the SurfaceType
            SurfaceType type = other.getType();
            if (type == SurfaceType.IMAGE) {
                surface = new ImageSurface(result);
            } else if (type == SurfaceType.PDF) {
                surface = new PDFSurface(result);
            } else if (type == SurfaceType.PS) {
                surface = new PSSurface(result);
            } else if (type == SurfaceType.RECORDING) {
                surface = new RecordingSurface(result);
            } else if (type == SurfaceType.SVG) {
                surface = new SVGSurface(result);
            } else if (type == SurfaceType.SCRIPT) {
                surface = new ScriptSurface(result);
            }
            MemoryCleaner.takeOwnership(surface.handle());
            // Cast to T is safe, because all possible Surface types are instantiated above
            return (T) surface;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_create_similar = Interop.downcallHandle(
            "cairo_surface_create_similar", FunctionDescriptor.of(ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

    /**
     * Create a new image surface that is as compatible as possible for uploading to
     * and the use in conjunction with an existing surface. However, this surface
     * can still be used like any normal image surface. Unlike
     * {@link #createSimilar(Surface, Content, int, int)} the new image surface
     * won't inherit the device scale from {@code other}.
     * <p>
     * Initially the surface contents are all 0 (transparent if contents have
     * transparency, black otherwise.)
     * <p>
     * Use {@link #createSimilar(Surface, Content, int, int)} if you don't need an
     * image surface.
     * 
     * @param other  an existing surface used to select the preference of the new
     *               surface
     * @param format the format for the new surface
     * @param width  width of the new surface, (in pixels)
     * @param height height of the new surface, (in pixels)
     * @return the newly allocated surface
     * @since 1.12
     */
    public static ImageSurface createSimilarImage(Surface other, Format format, int width, int height) {
        try {
            MemorySegment result = (MemorySegment) cairo_surface_create_similar_image
                    .invoke(other == null ? MemorySegment.NULL : other.handle(), format.getValue(), width, height);
            ImageSurface surface = new ImageSurface(result);
            MemoryCleaner.takeOwnership(surface.handle());
            return surface;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_create_similar_image = Interop
            .downcallHandle(
                    "cairo_surface_create_similar_image", FunctionDescriptor.of(ValueLayout.ADDRESS,
                            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

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
     * @return the newly allocated surface.
     * @since 1.10
     */
    public static Surface createForRectangle(Surface target, double x, double y, double width, double height) {
        try {
            MemorySegment result = (MemorySegment) cairo_surface_create_for_rectangle
                    .invoke(target == null ? MemorySegment.NULL : target.handle(), x, y, width, height);
            Surface surface = new Surface(result);
            MemoryCleaner.takeOwnership(surface.handle());
            /*
             * The subsurface will keep a reference to the target, to keep it alive during
             * its lifetime
             */
            surface.target = target;
            return surface;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_create_for_rectangle = Interop.downcallHandle(
            "cairo_surface_create_for_rectangle", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                    ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Checks whether an error has previously occurred for this surface.
     * 
     * @return {@link Status#SUCCESS}, {@link Status#NULL_POINTER},
     *         {@link Status#NO_MEMORY}, {@link Status#READ_ERROR},
     *         {@link Status#INVALID_CONTENT}, {@link Status#INVALID_FORMAT}, or
     *         {@link Status#INVALID_VISUAL}.
     * @since 1.0
     */
    public Status status() {
        try {
            int result = (int) cairo_surface_status.invoke(handle());
            return Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_status = Interop.downcallHandle("cairo_surface_status",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * This function finishes the surface and drops all references to external
     * resources. For example, for the Xlib backend it means that cairo will no
     * longer access the drawable, which can be freed. After calling
     * {@code finish()} the only valid operations on a surface are getting and
     * setting user, referencing and destroying, and flushing and finishing it.
     * Further drawing to the surface will not affect the surface but will instead
     * trigger a {@link Status#SURFACE_FINISHED} error.
     * <p>
     * When the Surface instance is destroyed, cairo will call {@code finish()} if
     * it hasn't been called already, before freeing the resources associated with
     * the surface.
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
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

    /**
     * Do any pending drawing for the surface and also restore any temporary
     * modifications cairo has made to the surface's state. This function must be
     * called before switching from drawing on the surface with cairo to drawing on
     * it directly with native APIs, or accessing its memory outside of Cairo. If
     * the surface doesn't support direct access, then this function does nothing.
     * 
     * @return the surface
     * @since 1.0
     */
    public Surface flush() {
        try {
            cairo_surface_flush.invoke(handle());
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_flush = Interop.downcallHandle("cairo_surface_flush",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

    /**
     * This function returns the device for a surface. See {@link Device}.
     * 
     * @return The device for surface or {@code null} if the surface does not have
     *         an associated device.
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
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Retrieves the default font rendering options for the surface. This allows
     * display surfaces to report the correct subpixel order for rendering on them,
     * print surfaces to disable hinting of metrics and so forth. The result can
     * then be used with cairo_scaled_font_create().
     * 
     * @param options a FontOptions object into which to store the retrieved
     *                options. All existing values are overwritten
     * @since 1.0
     */
    public void getFontOptions(FontOptions options) {
        try {
            cairo_surface_get_font_options.invoke(handle(), options == null ? MemorySegment.NULL : options.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_get_font_options = Interop.downcallHandle(
            "cairo_surface_get_font_options", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * This function returns the content type of surface which indicates whether the
     * surface contains color and/or alpha information. See {@link Content}.
     * 
     * @return The content type of ths surface.
     * @since 1.2
     */
    public Content getContent() {
        try {
            int result = (int) cairo_surface_get_content.invoke(handle());
            return Content.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_get_content = Interop.downcallHandle("cairo_surface_get_content",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Tells cairo that drawing has been done to surface using means other than
     * cairo, and that cairo should reread any cached areas. Note that you must call
     * {@link #flush()} before doing such drawing.
     * 
     * @return the surface
     * @since 1.0
     */
    public Surface markDirty() {
        try {
            cairo_surface_mark_dirty.invoke(handle());
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_mark_dirty = Interop.downcallHandle("cairo_surface_mark_dirty",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

    /**
     * Like {@link #markDirty()}, but drawing has been done only to the specified
     * rectangle, so that cairo can retain cached contents for other parts of the
     * surface.
     * <p>
     * Any cached clip set on the surface will be reset by this function, to make
     * sure that future cairo calls have the clip set that they expect.
     * 
     * @param x      X coordinate of dirty rectangle
     * @param y      Y coordinate of dirty rectangle
     * @param width  width of dirty rectangle
     * @param height height of dirty rectangle
     * @return the surface
     * @since 1.0
     */
    public Surface markDirtyRectangle(int x, int y, int width, int height) {
        try {
            cairo_surface_mark_dirty_rectangle.invoke(handle(), x, y, width, height);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_mark_dirty_rectangle = Interop
            .downcallHandle(
                    "cairo_surface_mark_dirty_rectangle", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
                            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

    /**
     * Sets an offset that is added to the device coordinates determined by the CTM
     * when drawing to surface . One use case for this function is when we want to
     * create a cairo_surface_t that redirects drawing for a portion of an onscreen
     * surface to an offscreen surface in a way that is completely invisible to the
     * user of the cairo API. Setting a transformation via
     * {@link Context#translate(double, double)} isn't sufficient to do this, since
     * functions like {@link Context#deviceToUser(Point)} will expose the hidden
     * offset.
     * <p>
     * Note that the offset affects drawing to the surface as well as using the
     * surface in a source pattern.
     * 
     * @param xOffset the offset in the X direction, in device units
     * @param yOffset the offset in the Y direction, in device units
     * @return the surface
     * @since 1.0
     */
    public Surface setDeviceOffset(double xOffset, double yOffset) {
        try {
            cairo_surface_set_device_offset.invoke(handle(), xOffset, yOffset);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_set_device_offset = Interop.downcallHandle(
            "cairo_surface_set_device_offset",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * This function returns the previous device offset set by
     * {@link #setDeviceOffset(double, double)}.
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
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * This function returns the previous device offset set by
     * {@link #setDeviceScale(double, double)}.
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
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Sets a scale that is multiplied to the device coordinates determined by the
     * CTM when drawing to surface . One common use for this is to render to very
     * high resolution display devices at a scale factor, so that code that assumes
     * 1 pixel will be a certain size will still work. Setting a transformation via
     * {@link Context#translate(double, double)} isn't sufficient to do this, since
     * functions like {@link Context#deviceToUser(Point)} will expose the hidden
     * scale.
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
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

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
     * page (with {@link Context#showPage()} or {@link Context#copyPage()}) so there
     * is currently no way to have more than one fallback resolution in effect on a
     * single page.
     * <p>
     * The default fallback resoultion is 300 pixels per inch in both dimensions.
     * 
     * @param xPixelsPerInch horizontal setting for pixels per inch
     * @param yPixelsPerInch vertical setting for pixels per inch
     * @return the surface
     * @since 1.2
     */
    public Surface setFallbackResolution(double xPixelsPerInch, double yPixelsPerInch) {
        try {
            cairo_surface_set_fallback_resolution.invoke(handle(), xPixelsPerInch, yPixelsPerInch);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_set_fallback_resolution = Interop.downcallHandle(
            "cairo_surface_set_fallback_resolution",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * This function returns the previous fallback resolution set by
     * {@link #setFallbackResolution(double, double)}, or default fallback
     * resolution if never set.
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
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * This function returns the type of the backend used to create a surface. See
     * {@link SurfaceType} for available types.
     * 
     * @return The type of {@link Surface}.
     * @since 1.2
     */
    public SurfaceType getType() {
        try {
            int result = (int) cairo_surface_get_type.invoke(handle());
            return SurfaceType.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_get_type = Interop.downcallHandle("cairo_surface_get_type",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Emits the current page for backends that support multiple pages, but doesn't
     * clear it, so that the contents of the current page will be retained for the
     * next page. Use {@link #showPage()} if you want to get an empty page after the
     * emission.
     * <p>
     * There is a convenience function for this in {@link Context}, namely
     * {@link Context#copyPage()}.
     * 
     * @return the surface
     * @since 1.6
     */
    public Surface copyPage() {
        try {
            cairo_surface_copy_page.invoke(handle());
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_copy_page = Interop.downcallHandle("cairo_surface_copy_page",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

    /**
     * Emits and clears the current page for backends that support multiple pages.
     * Use {@link #copyPage()} if you don't want to clear the page.
     * <p>
     * There is a convenience function for this that takes a {@link Context} namely
     * {@link Context#showPage()}.
     *
     * @return the surface
     * @since 1.6
     */
    public Surface showPage() {
        try {
            cairo_surface_show_page.invoke(handle());
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_show_page = Interop.downcallHandle("cairo_surface_show_page",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

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
            "cairo_surface_has_show_text_glyphs", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Attach an image in the format mimeType to the surface. To remove the data
     * from a surface, call this function with same mime type and {@code null} for
     * data.
     * <p>
     * The attached image (or filename) data can later be used by backends which
     * support it (currently: PDF, PS, SVG and Win32 Printing surfaces) to emit this
     * data instead of making a snapshot of the surface. This approach tends to be
     * faster and requires less memory and disk space.
     * <p>
     * The recognized MIME types are the following: {@link MimeType#JPEG},
     * {@link MimeType#PNG}, {@link MimeType#JP2}, {@link MimeType#URI},
     * {@link MimeType#UNIQUE_ID}, {@link MimeType#JBIG2},
     * {@link MimeType#JBIG2_GLOBAL}, {@link MimeType#JBIG2_GLOBAL_ID},
     * {@link MimeType#CCITT_FAX}, {@link MimeType#CCITT_FAX_PARAMS}.
     * <p>
     * See corresponding backend surface docs for details about which MIME types it
     * can handle. Caution: the associated MIME data will be discarded if you draw
     * on the surface afterwards. Use this function with care.
     * <p>
     * Even if a backend supports a MIME type, that does not mean cairo will always
     * be able to use the attached MIME data. For example, if the backend does not
     * natively support the compositing operation used to apply the MIME data to the
     * backend. In that case, the MIME data will be ignored. Therefore, to apply an
     * image in all cases, it is best to create an image surface which contains the
     * decoded image data and then attach the MIME data to that. This ensures the
     * image will always be used while still allowing the MIME data to be used
     * whenever possible.
     * <p>
     * <i>Language binding note:</i> The data is passed to cairo as a memory segment
     * backed by the on-heap region of memory that holds the given byte array. See
     * {@link MemorySegment#ofArray(byte[])} for more details.
     * 
     * @param mimeType the MIME type of the image data
     * @param data     the image data to attach to the surface
     * @return the surface
     * @since 1.10
     */
    public Surface setMimeData(MimeType mimeType, byte[] data) {
        Status status;
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment mimeTypePtr = Interop.allocateNativeString(mimeType.toString(), arena);
                MemorySegment dataPtr = data == null ? MemorySegment.NULL :
                        arena.allocateArray(ValueLayout.JAVA_BYTE, data);
                int result = (int) cairo_surface_set_mime_data.invoke(handle(), mimeTypePtr, dataPtr,
                        data == null ? 0 : data.length, MemorySegment.NULL, MemorySegment.NULL);
                status = Status.of(result);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
        return this;
    }

    private static final MethodHandle cairo_surface_set_mime_data = Interop.downcallHandle(
            "cairo_surface_set_mime_data",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                    ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Return mime data previously attached to the surface using the specified mime
     * type. If no data has been attached with the given mime type, {@code null} is
     * returned.
     * <p>
     * <i>Language binding note:</i> The returned mime data is a copy of the actual
     * data attached to the surface.
     * 
     * @param mimeType the mime type of the image data
     * @return the image data to attached to the surface
     * @since 1.10
     */
    public byte[] getMimeData(MimeType mimeType) {
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment mimeTypePtr = Interop.allocateNativeString(mimeType.toString(), arena);
                MemorySegment dataPtr = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment lengthPtr = arena.allocate(ValueLayout.ADDRESS);
                cairo_surface_get_mime_data.invoke(handle(), mimeTypePtr, dataPtr, lengthPtr);
                long length = lengthPtr.get(ValueLayout.JAVA_LONG, 0);
                if (length <= 0) {
                    return null;
                }
                MemorySegment data = dataPtr.get(ValueLayout.ADDRESS, 0);
                if (MemorySegment.NULL.equals(data)) {
                    return null;
                }
                MemorySegment array = MemorySegment.ofAddress(data.address(), length);
                return array.toArray(ValueLayout.JAVA_BYTE);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_get_mime_data = Interop
            .downcallHandle("cairo_surface_get_mime_data", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS.asUnbounded(), ValueLayout.ADDRESS.asUnbounded()));

    /**
     * Return whether this surface supports {@code mime_type}.
     * 
     * @param mimeType the mime type
     * @return mimeType {@code true} if this surface supports {@code mime_type},
     *         {@code false} otherwise
     * @since 1.12
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
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Returns an image surface that is the most efficient mechanism for modifying
     * the backing store of the target surface. The region retrieved may be limited
     * to the extents or {@code null} for the whole surface.
     * <p>
     * Note, the use of the original surface as a target or source whilst it is
     * mapped is undefined. The result of mapping the surface multiple times is
     * undefined. Calling {@link #close()} {@link #destroy()} or {@link #finish()}
     * on the resulting image surface results in undefined behavior. Letting the
     * image surface get garbage-collected from Java results in undefined behavior.
     * Changing the device transform of the image surface or of the source surface
     * before the image surface is unmapped results in undefined behavior.
     * 
     * @param extents limit the extraction to an rectangular region
     * @return the newly allocated surface. This function always returns a valid
     *         pointer, but it will return a pointer to a "nil" surface if the
     *         original is already in an error state or any other error occurs.
     * @since 1.12
     */
    public ImageSurface mapToImage(RectangleInt extents) {
        try {
            MemorySegment result = (MemorySegment) cairo_surface_map_to_image.invoke(handle(),
                    extents == null ? MemorySegment.NULL : extents.handle());
            return new ImageSurface(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_map_to_image = Interop.downcallHandle("cairo_surface_map_to_image",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Unmaps the image surface as returned from {@link #mapToImage(RectangleInt)}.
     * <p>
     * The content of the image will be uploaded to the target surface. Afterwards,
     * the image is destroyed.
     * <p>
     * Using an image surface which wasn't returned by
     * {@link #mapToImage(RectangleInt)} results in undefined behavior.
     * 
     * @param image the currently mapped image
     * @since 1.12
     */
    public void unmapImage(ImageSurface image) {
        try {
            cairo_surface_unmap_image.invoke(handle(), image == null ? MemorySegment.NULL : image.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_unmap_image = Interop.downcallHandle("cairo_surface_unmap_image",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Closing a surface will invoke {@link #finish()}, which will flush the
     * surface and drop all references to external resources. A closed surface
     * cannot be used to perform drawing operations and cannot be reopened.
     * <p>
     * Although the Java bindings make an effort to properly dispose native
     * resources using a {@link Cleaner}, this is not guaranteed to work in
     * all situations. Users must therefore always call {@code close()} or
     * {@code finish()} on surfaces (either manually or with a
     * try-with-resources statement) or risk issues like resource exhaustion
     * and data loss.
     */
    @Override
    public void close() {
        finish();
    }

    /**
     * Attach user data to the surface. This method will generate and return a
     * {@link UserDataKey}. To update the user data for the same key, call
     * {@link #setUserData(UserDataKey, Object)}. To remove user data from a
     * surface, call this function with {@code null} for {@code userData}.
     * 
     * @param userData the user data to attach to the surface. {@code userData} can
     *                 be any Java object, but if it is a primitive type, a
     *                 {@link MemorySegment} or a {@link ProxyInstance} instance, it will be
     *                 stored as cairo user data in native memory as well.
     * @return the key that the user data is attached to
     * @since 1.0
     */
    public UserDataKey setUserData(Object userData) {
        UserDataKey key = UserDataKey.create(this);
        return setUserData(key, userData);
    }

    /**
     * Attach user data to the surface. To remove user data from a surface, call
     * this function with the key that was used to set it and {@code null} for
     * {@code userData}.
     * 
     * @param key      the key to attach the user data to
     * @param userData the user data to attach to the surface. {@code userData} can
     *                 be any Java object, but if it is a primitive type, a
     *                 {@link MemorySegment} or a {@link ProxyInstance} instance, it will be
     *                 stored as cairo user data in native memory as well.
     * @return the key
     * @throws NullPointerException if {@code key} is {@code null}
     * @since 1.0
     */
    public UserDataKey setUserData(UserDataKey key, Object userData) {
        Status status;
        userDataStore.set(key, userData);
        try {
            int result = (int) cairo_surface_set_user_data.invoke(handle(), key.handle(), userDataStore.dataSegment(userData),
                    MemorySegment.NULL);
            status = Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
        return key;
    }

    private static final MethodHandle cairo_surface_set_user_data = Interop.downcallHandle("cairo_surface_set_user_data",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS));

    /**
     * Return user data previously attached to the surface using the specified key.
     * If no user data has been attached with the given key this function returns
     * {@code null}.
     * 
     * @param key the UserDataKey the user data was attached to
     * @return the user data previously attached or {@code null}
     * @since 1.0
     */
    public Object getUserData(UserDataKey key) {
        return key == null ? null : userDataStore.get(key);
    }
}
