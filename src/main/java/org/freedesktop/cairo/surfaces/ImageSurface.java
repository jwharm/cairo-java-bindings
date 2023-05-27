package org.freedesktop.cairo.surfaces;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import io.github.jwharm.cairobindings.Interop;
import org.freedesktop.cairo.drawing.Status;

/**
 * Image Surfaces — Rendering to memory buffers.
 * <p>
 * Image surfaces provide the ability to render to memory buffers either
 * allocated by cairo or by the calling code. The supported image formats are
 * those defined in cairo_format_t.
 */
public class ImageSurface extends Surface {

	{
		Interop.ensureInitialized();
	}

	/**
	 * Constructor used internally to instantiate a java ImageSurface object for a
	 * native {@code cairo_image_surface_t} instance
	 * 
	 * @param address the memory address of the native {@code cairo_image_surface_t}
	 *                instance
	 */
	public ImageSurface(MemorySegment address) {
		super(address);
	}

	/**
	 * This function provides a stride value that will respect all alignment
	 * requirements of the accelerated image-rendering code within cairo. Typical
	 * usage will be of the form:
	 * {@snippet :
	 * int stride;
	 * unsigned char *data;
	 * cairo_surface_t *surface;
	 * 
	 * stride = cairo_format_stride_for_width (format, width);
	 * data = malloc (stride * height);
	 * surface = cairo_image_surface_create_for_data (data, format,
	 * 											  	  width, height,
	 * 					  							  stride);
	 * }
	 * 
	 * @param format A {@link Format} value
	 * @param width  The desired width of an image surface to be created.
	 * @return the appropriate stride to use given the desired format and width, or
	 *         -1 if either the format is invalid or the width too large.
	 * @since 1.6
	 */
	public int formatStrideForWidth(Format format, int width) {
		FunctionDescriptor fdesc = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
				ValueLayout.JAVA_INT);
		try {
			return (int) Interop.downcallHandle("cairo_format_stride_for_width", fdesc, false).invoke(format.ordinal(),
					width);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Creates an image surface of the specified format and dimensions. Initially
	 * the surface contents are set to 0. (Specifically, within each pixel, each
	 * color or alpha channel belonging to format will be 0. The contents of bits
	 * within a pixel, but not belonging to the given format are undefined).
	 * 
	 * @param format format of pixels in the surface to create
	 * @param width  width of the surface, in pixels
	 * @param height height of the surface, in pixels
	 * @return the newly created surface. This function always returns a valid
	 *         pointer, but it will return a pointer to a "nil" surface if an error
	 *         such as out of memory occurs. You can use cairo_surface_status() to
	 *         check for this.
	 * @since 1.0
	 */
	public static ImageSurface create(Format format, int width, int height) {
		try {
			MemorySegment result = (MemorySegment) cairo_image_surface_create.invoke(format.ordinal(), width, height);
			ImageSurface surface = new ImageSurface(result);
			surface.takeOwnership();
			return surface;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_image_surface_create = Interop.downcallHandle("cairo_image_surface_create",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT), false);

	/**
	 * Creates an image surface for the provided pixel data. The output buffer must
	 * be kept around until the {@link Surface} is destroyed or
	 * cairo_surface_finish() is called on the surface. The initial contents of data
	 * will be used as the initial image contents; you must explicitly clear the
	 * buffer, using, for example, cairo_rectangle() and cairo_fill() if you want it
	 * cleared.
	 * <p>
	 * Note that the stride may be larger than width*bytes_per_pixel to provide
	 * proper alignment for each pixel and row. This alignment is required to allow
	 * high-performance rendering within cairo. The correct way to obtain a legal
	 * stride value is to call {@link #formatStrideForWidth(Format, int)} with the
	 * desired format and maximum image width value, and then use the resulting stride
	 * value to allocate the data and to create the image surface. See
	 * {@link #formatStrideForWidth(Format, int)} for example code.
	 * 
	 * @param data   a pointer to a buffer supplied by the application in which to
	 *               write contents. This pointer must be suitably aligned for any
	 *               kind of variable, (for example, a pointer returned by malloc).
	 * @param format the format of pixels in the buffer
	 * @param width  the width of the image to be stored in the buffer
	 * @param height the height of the image to be stored in the buffer
	 * @param stride the number of bytes between the start of rows in the buffer as
	 *               allocated. This value should always be computed by
	 *               {@link #formatStrideForWidth(Format, int)} before allocating the
	 *               data buffer.
	 * @return a pointer to the newly created surface. The caller owns the surface
	 *         and should call cairo_surface_destroy() when done with it.
	 *         <p>
	 *         This function always returns a valid pointer, but it will return a
	 *         pointer to a "nil" surface in the case of an error such as out of
	 *         memory or an invalid stride value. In case of invalid stride value
	 *         the error status of the returned surface will be
	 *         CAIRO_STATUS_INVALID_STRIDE. You can use cairo_surface_status() to
	 *         check for this.
	 * @since 1.0
	 */
	public static ImageSurface createForData(MemorySegment data, Format format, int width, int height, int stride) {
		try {
			MemorySegment result = (MemorySegment) cairo_image_surface_create_for_data.invoke(data, format.ordinal(),
					width, height, stride);
			ImageSurface surface = new ImageSurface(result);
			surface.takeOwnership();
			return surface;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_image_surface_create_for_data = Interop
			.downcallHandle(
					"cairo_image_surface_create_for_data", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS,
							ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT),
					false);

	/**
	 * Creates a new image surface and initializes the contents to the given PNG file.
	 * @param filename name of PNG file to load. On Windows this filename is encoded in UTF-8.
	 * @return a new cairo_surface_t initialized with the contents of the PNG file, or a "nil"
	 * surface if any error occurred. A nil surface can be checked for with
	 * cairo_surface_status(surface) which may return one of the following values:
	 * <p>
	 * CAIRO_STATUS_NO_MEMORY CAIRO_STATUS_FILE_NOT_FOUND CAIRO_STATUS_READ_ERROR
	 * CAIRO_STATUS_PNG_ERROR
	 * <p>
	 * Alternatively, you can allow errors to propagate through the drawing operations and
	 * check the status on the context upon completion using cairo_status().
	 * @since 1.0
	 */
	public static ImageSurface createFromPNG(String filename) {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment filenamePtr = Interop.allocateNativeString(filename, arena);
				MemorySegment result = (MemorySegment) cairo_image_surface_create_from_png.invoke(filenamePtr);
				ImageSurface surface = new ImageSurface(result);
				surface.takeOwnership();
				return surface;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_image_surface_create_from_png = Interop
			.downcallHandle(
					"cairo_image_surface_create_from_png", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS),
					false);

	/**
	 * Writes the contents of surface to a new file filename as a PNG image.
	 * @param filename the name of a file to write to; on Windows this filename is encoded in UTF-8.
	 * @return CAIRO_STATUS_SUCCESS if the PNG file was written successfully. Otherwise, CAIRO_STATUS_NO_MEMORY
	 * if memory could not be allocated for the operation or CAIRO_STATUS_SURFACE_TYPE_MISMATCH if the surface
	 * does not have pixel contents, or CAIRO_STATUS_WRITE_ERROR if an I/O error occurs while attempting to write
	 * the file, or CAIRO_STATUS_PNG_ERROR if libpng returned an error.
	 * @since 1.0
	 */
	public Status writeToPNG(String filename) {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment filenamePtr = Interop.allocateNativeString(filename, arena);
				int result = (int) cairo_surface_write_to_png.invoke(handle(), filenamePtr);
				return Status.values()[result];
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_surface_write_to_png = Interop
			.downcallHandle(
					"cairo_surface_write_to_png", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS),
					false);

	/**
	 * Get a pointer to the data of the image surface, for direct inspection or
	 * modification. A call to cairo_surface_flush() is required before accessing
	 * the pixel data to ensure that all pending drawing operations are finished. A
	 * call to cairo_surface_mark_dirty() is required after the data is modified.
	 * 
	 * @return a pointer to the image data of this surface or NULL if surface is not
	 *         an image surface, or if cairo_surface_finish() has been called.
	 * @since 1.2
	 */
	public MemorySegment getData() {
		try {
			return (MemorySegment) cairo_image_surface_get_data.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_image_surface_get_data = Interop.downcallHandle(
			"cairo_image_surface_get_data", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Get the format of the surface.
	 * 
	 * @return the format of the surface
	 * @since 1.2
	 */
	public Format getFormat() {
		try {
			int result = (int) cairo_image_surface_get_format.invoke(handle());
			return Format.values()[result];
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_image_surface_get_format = Interop.downcallHandle(
			"cairo_image_surface_get_format", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Get the width of the image surface in pixels.
	 * 
	 * @return the width of the surface in pixels.
	 * @since 1.0
	 */
	public int getWidth() {
		try {
			return (int) cairo_image_surface_get_width.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_image_surface_get_width = Interop.downcallHandle(
			"cairo_image_surface_get_width", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Get the height of the image surface in pixels.
	 * 
	 * @return the height of the surface in pixels.
	 * @since 1.0
	 */
	public int getHeight() {
		try {
			return (int) cairo_image_surface_get_height.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_image_surface_get_height = Interop.downcallHandle(
			"cairo_image_surface_get_height", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Get the stride of the image surface in bytes.
	 * 
	 * @return the stride of the image surface in bytes (or 0 if surface is not an
	 *         image surface). The stride is the distance in bytes from the
	 *         beginning of one row of the image data to the beginning of the next
	 *         row.
	 * @since 1.2
	 */
	public int getStride() {
		try {
			return (int) cairo_image_surface_get_stride.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_image_surface_get_stride = Interop.downcallHandle(
			"cairo_image_surface_get_stride", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);
}
