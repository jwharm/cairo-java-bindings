package org.freedesktop.cairo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Image Surfaces — Rendering to memory buffers.
 * <p>
 * Image surfaces provide the ability to render to memory buffers either
 * allocated by cairo or by the calling code. The supported image formats are
 * those defined in {@link Format}.
 * <p>
 * The PNG functions allow reading PNG images into image surfaces, and writing
 * any surface to a PNG file. It is a toy API. It only offers very simple
 * support for reading and writing PNG files, which is sufficient for testing
 * and demonstration purposes. Applications which need more control over the
 * generated PNG file should access the pixel data directly, using
 * {@link #getData()} or a backend-specific access function, and process it
 * with another library, e.g. gdk-pixbuf or libpng.
 *
 * @see Surface
 * @since 1.0
 */
public final class ImageSurface extends Surface {

    static {
        Interop.ensureInitialized();
    }

    // Keep a reference to the data during the lifetime of the
    // Context.
    @SuppressWarnings("unused")
    private MemorySegment data;

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
     * 
     * <pre>
     * int stride = ImageSurface.formatStrideForWidth(format, width);
     * ImageSurface surface = ImageSurface.createForData(data, format, width, height, stride);
     * </pre>
     * 
     * @param format A {@link Format} value
     * @param width  The desired width of an image surface to be created.
     * @return the appropriate stride to use given the desired format and width, or
     *         -1 if either the format is invalid or the width too large.
     * @since 1.6
     */
    public static int formatStrideForWidth(Format format, int width) {
        try {
            return (int) cairo_format_stride_for_width.invoke(format.value(), width);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_format_stride_for_width = Interop.downcallHandle(
            "cairo_format_stride_for_width",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

    /**
     * Creates an image surface of the specified format and dimensions. Initially
     * the surface contents are set to 0. (Specifically, within each pixel, each
     * color or alpha channel belonging to format will be 0. The contents of bits
     * within a pixel, but not belonging to the given format are undefined).
     * 
     * @param format format of pixels in the surface to create
     * @param width  width of the surface, in pixels
     * @param height height of the surface, in pixels
     * @return the newly created surface
     * @since 1.0
     */
    public static ImageSurface create(Format format, int width, int height) {
        ImageSurface surface;
        try {
            MemorySegment result = (MemorySegment) cairo_image_surface_create.invoke(format.value(), width, height);
            surface = new ImageSurface(result);
            surface.takeOwnership();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (surface.status() == Status.NO_MEMORY) {
            throw new RuntimeException(surface.status().toString());
        }
        return surface;
    }

    private static final MethodHandle cairo_image_surface_create = Interop.downcallHandle("cairo_image_surface_create",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_INT));

    /**
     * Creates an image surface for the provided pixel data. The output buffer must
     * be kept around until the {@link Surface} is destroyed or
     * {@link Surface#finish()} is called on the surface. The initial contents of
     * data will be used as the initial image contents; you must explicitly clear
     * the buffer, using, for example,
     * {@link Context#rectangle(double, double, double, double)} and
     * {@link Context#fill()} if you want it cleared.
     * <p>
     * Note that the stride may be larger than width*bytes_per_pixel to provide
     * proper alignment for each pixel and row. This alignment is required to allow
     * high-performance rendering within cairo. The correct way to obtain a legal
     * stride value is to call {@link #formatStrideForWidth(Format, int)} with the
     * desired format and maximum image width value, and then use the resulting
     * stride value to allocate the data and to create the image surface. See
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
     *               {@link #formatStrideForWidth(Format, int)} before allocating
     *               the data buffer.
     * @return the newly created surface
     * @throws IllegalArgumentException in case of invalid stride value
     * @since 1.0
     */
    public static ImageSurface create(MemorySegment data, Format format, int width, int height, int stride)
            throws IllegalArgumentException {
        ImageSurface surface;
        try {
            MemorySegment result = (MemorySegment) cairo_image_surface_create_for_data
                    .invoke(data == null ? MemorySegment.NULL : data, format.value(), width, height, stride);
            surface = new ImageSurface(result);
            surface.takeOwnership();
            /*
             * Keep a reference to the MemorySegment, in case it is allocated as
             * SegmentScope#auto() and the user doesn't keep a reference alive
             */
            surface.data = data;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (surface.status() == Status.INVALID_STRIDE) {
            throw new IllegalArgumentException(surface.status().toString());
        }
        if (surface.status() == Status.NO_MEMORY) {
            throw new RuntimeException(surface.status().toString());
        }
        return surface;
    }

    private static final MethodHandle cairo_image_surface_create_for_data = Interop.downcallHandle(
            "cairo_image_surface_create_for_data", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

    /**
     * Creates a new image surface and initializes the contents to the given PNG
     * file.
     * 
     * @param filename name of PNG file to load. On Windows this filename is encoded
     *                 in UTF-8.
     * @return a new Surface initialized with the contents of the PNG file
     * @throws IOException when the status of the new Surface is
     *                     {@link Status#FILE_NOT_FOUND}, {@link Status#READ_ERROR}
     *                     or {@link Status#PNG_ERROR}.
     * @since 1.0
     */
    public static ImageSurface createFromPNG(String filename) throws IOException {
        ImageSurface surface;
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment filenamePtr = Interop.allocateString(filename, arena);
                MemorySegment result = (MemorySegment) cairo_image_surface_create_from_png.invoke(filenamePtr);
                surface = new ImageSurface(result);
                surface.takeOwnership();
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        Status status = surface.status();
        if (status == Status.FILE_NOT_FOUND || status == Status.READ_ERROR || status == Status.PNG_ERROR) {
            throw new IOException(status.toString());
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
        return surface;
    }

    private static final MethodHandle cairo_image_surface_create_from_png = Interop.downcallHandle(
            "cairo_image_surface_create_from_png", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Creates a new image surface from PNG data read incrementally from the input
     * stream.
     * 
     * @param stream input stream from which to read the data of the file
     * @return a new Surface initialized with the contents of the PNG file
     * @throws IOException if an exception occured while reading from the input
     *                     stream ({@link Status#READ_ERROR}), or libpng returned an
     *                     error ({@link Status#PNG_ERROR})
     * @since 1.0
     */
    public static ImageSurface createFromPNG(InputStream stream) throws IOException {
        if (stream == null) {
            return null;
        }
        ImageSurface surface;
        try {
            try (Arena arena = Arena.openConfined()) {
                ReadFunc readFunc = stream::readNBytes;
                MemorySegment result = (MemorySegment) cairo_image_surface_create_from_png_stream
                        .invoke(readFunc.toCallback(arena.scope()), MemorySegment.NULL);
                surface = new ImageSurface(result);
                surface.takeOwnership();
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        Status status = surface.status();
        if (status == Status.READ_ERROR || status == Status.PNG_ERROR) {
            throw new IOException(status.toString());
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
        return surface;
    }

    private static final MethodHandle cairo_image_surface_create_from_png_stream = Interop.downcallHandle(
            "cairo_image_surface_create_from_png_stream",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Writes the contents of surface to a new file filename as a PNG image.
     * 
     * @param filename the name of a file to write to; on Windows this filename is
     *                 encoded in UTF-8.
     * @throws IOException if the surface does not have pixel contents (see
     *                     {@link Status#SURFACE_TYPE_MISMATCH}), or an I/O error
     *                     occurs while attempting to write the file
     *                     ({@link Status#WRITE_ERROR}), or if libpng returned an
     *                     error ({@link Status#PNG_ERROR}).
     * @since 1.0
     */
    public void writeToPNG(String filename) throws IOException {
        Status status = null;
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment filenamePtr = Interop.allocateString(filename, arena);
                int result = (int) cairo_surface_write_to_png.invoke(handle(), filenamePtr);
                status = Status.of(result);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.SURFACE_TYPE_MISMATCH || status == Status.WRITE_ERROR || status == Status.PNG_ERROR) {
            throw new IOException(status.toString());
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
    }

    private static final MethodHandle cairo_surface_write_to_png = Interop.downcallHandle("cairo_surface_write_to_png",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Writes the image surface to the output stream.
     * 
     * @param stream output stream to write the pixel contents of the surface to
     * @throws IOException if the surface does not have pixel contents (see
     *                     {@link Status#SURFACE_TYPE_MISMATCH}), or an I/O error
     *                     occurs while writing to the output stream
     *                     ({@link Status#WRITE_ERROR}), or if libpng returned an
     *                     error ({@link Status#PNG_ERROR})
     * @since 1.0
     */
    public void writeToPNG(OutputStream stream) throws IOException {
        if (stream == null) {
            return;
        }
        Status status;
        try {
            try (Arena arena = Arena.openConfined()) {
                WriteFunc writeFunc = stream::write;
                int result = (int) cairo_surface_write_to_png_stream.invoke(handle(),
                        writeFunc.toCallback(arena.scope()), MemorySegment.NULL);
                status = Status.of(result);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.SURFACE_TYPE_MISMATCH || status == Status.WRITE_ERROR || status == Status.PNG_ERROR) {
            throw new IOException(status.toString());
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
    }

    private static final MethodHandle cairo_surface_write_to_png_stream = Interop.downcallHandle(
            "cairo_surface_write_to_png_stream",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Get a pointer to the data of the image surface, for direct inspection or
     * modification. A call to {@link Surface#flush()} is required before accessing
     * the pixel data to ensure that all pending drawing operations are finished. A
     * call to {@link Surface#markDirty()} is required after the data is modified.
     * 
     * @return a pointer to the image data of this surface or NULL if surface is not
     *         an image surface, or if {@link Surface#finish()} has been called.
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
            "cairo_image_surface_get_data", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Get the format of the surface.
     * 
     * @return the format of the surface
     * @since 1.2
     */
    public Format getFormat() {
        try {
            int result = (int) cairo_image_surface_get_format.invoke(handle());
            return Format.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_image_surface_get_format = Interop.downcallHandle(
            "cairo_image_surface_get_format", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

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
            "cairo_image_surface_get_width", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

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
            "cairo_image_surface_get_height", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

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
            "cairo_image_surface_get_stride", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
}
