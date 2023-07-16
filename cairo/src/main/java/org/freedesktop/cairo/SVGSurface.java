package org.freedesktop.cairo;

import io.github.jwharm.javagi.interop.Interop;
import io.github.jwharm.javagi.interop.MemoryCleaner;

import java.io.OutputStream;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * The SVG surface is used to render cairo graphics to SVG files and is a
 * multi-page vector surface backend.
 * 
 * @see Surface
 * @since 1.2
 */
public final class SVGSurface extends Surface {

    static {
        Cairo.ensureInitialized();
    }

    /*
     * Initialized by {@link #create(OutputStream, int, int)} to keep a reference to
     * the memory segment for the upcall stub alive during the lifetime of the
     * SVGSurface instance.
     */
    @SuppressWarnings("unused")
    private MemorySegment callbackAllocation;

    /**
     * Constructor used internally to instantiate a java SVGSurface object for a
     * native {@code cairo_surface_t} instance
     * 
     * @param address the memory address of the native {@code cairo_surface_t}
     *                instance
     */
    public SVGSurface(MemorySegment address) {
        super(address);
    }

    /**
     * Creates a SVG surface of the specified size in points to be written to
     * {@code filename}.
     * <p>
     * The SVG surface backend recognizes the following MIME types for the data
     * attached to a surface (see {@link Surface#setMimeData(MimeType, byte[])})
     * when it is used as a source pattern for drawing on this surface:
     * {@link MimeType#JPEG}, {@link MimeType#PNG}, {@link MimeType#URI}. If any
     * of them is specified, the SVG backend emits a href with the content of MIME
     * data instead of a surface snapshot (PNG, Base64-encoded) in the
     * corresponding image tag.
     * <p>
     * The unofficial MIME type {@link MimeType#URI} is examined first. If present,
     * the URI is emitted as is: assuring the correctness of URI is left to the
     * client code.
     * <p>
     * If {@link MimeType#URI} is not present, but {@link MimeType#JPEG} or
     * {@link MimeType#PNG} is specified, the corresponding data is Base64-encoded
     * and emitted.
     * <p>
     * If {@link MimeType#UNIQUE_ID} is present, all surfaces with the same unique
     * identifier will only be embedded once.
     * 
     * @param filename       a filename for the SVG output (must be writable), NULL
     *                       may be used to specify no output. This will generate a
     *                       SVG surface that may be queried and used as a source,
     *                       without generating a temporary file.
     * @param widthInPoints  width of the surface, in points (1 point == 1/72.0
     *                       inch)
     * @param heightInPoints height of the surface, in points (1 point == 1/72.0
     *                       inch)
     * @return the newly created surface
     * @since 1.2
     */
    public static SVGSurface create(String filename, int widthInPoints, int heightInPoints) {
        SVGSurface surface;
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment filenamePtr = Interop.allocateNativeString(filename, arena);
                MemorySegment result = (MemorySegment) cairo_svg_surface_create.invoke(filenamePtr, widthInPoints,
                        heightInPoints);
                surface = new SVGSurface(result);
                MemoryCleaner.takeOwnership(surface.handle());
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (surface.status() == Status.NO_MEMORY) {
            throw new RuntimeException(surface.status().toString());
        }
        return surface;
    }

    private static final MethodHandle cairo_svg_surface_create = Interop.downcallHandle("cairo_svg_surface_create",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

    /**
     * Creates a SVG surface of the specified size in points to be written
     * incrementally to the {@code stream}.
     * 
     * @param stream         an {@link OutputStream} to accept the output data, may
     *                       be {@code null} to indicate a no-op stream. With a
     *                       no-op stream, the surface may be queried or used as a
     *                       source without generating any temporary files.
     * @param widthInPoints  width of the surface, in points (1 point == 1/72.0
     *                       inch)
     * @param heightInPoints height of the surface, in points (1 point == 1/72.0
     *                       inch)
     * @return the newly created surface
     * @since 1.2
     */
    public static SVGSurface create(OutputStream stream, int widthInPoints, int heightInPoints) {
        SVGSurface surface;
        try {
            MemorySegment writeFuncPtr;
            if (stream != null) {
                WriteFunc writeFunc = stream::write;
                writeFuncPtr = writeFunc.toCallback(SegmentScope.auto());
            } else {
                writeFuncPtr = MemorySegment.NULL;
            }
            MemorySegment result = (MemorySegment) cairo_svg_surface_create_for_stream.invoke(writeFuncPtr,
                    MemorySegment.NULL, widthInPoints, heightInPoints);
            surface = new SVGSurface(result);
            MemoryCleaner.takeOwnership(surface.handle());
            if (stream != null) {
                surface.callbackAllocation = writeFuncPtr; // Keep the memory segment of the upcall stub alive
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (surface.status() == Status.NO_MEMORY) {
            throw new RuntimeException(surface.status().toString());
        }
        return surface;
    }

    private static final MethodHandle cairo_svg_surface_create_for_stream = Interop
            .downcallHandle(
                    "cairo_svg_surface_create_for_stream", FunctionDescriptor.of(ValueLayout.ADDRESS,
                            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

    /**
     * Get the unit of the SVG surface.
     * 
     * @return the SVG unit of the SVG surface.
     * @since 1.16
     */
    public SVGUnit getDocumentUnit() {
        try {
            int result = (int) cairo_svg_surface_get_document_unit.invoke(handle());
            return SVGUnit.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_svg_surface_get_document_unit = Interop.downcallHandle(
            "cairo_svg_surface_get_document_unit", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Use the specified unit for the width and height of the generated SVG file.
     * See {@link SVGUnit} for a list of available unit values that can be used
     * here.
     * <p>
     * This function can be called at any time before generating the SVG file.
     * <p>
     * However to minimize the risk of ambiguities it's recommended to call it
     * before any drawing operations have been performed on the given surface, to
     * make it clearer what the unit used in the drawing operations is.
     * <p>
     * The simplest way to do this is to call this function immediately after
     * creating the SVG surface.
     * <p>
     * Note if this function is never called, the default unit for SVG documents
     * generated by cairo will be "pt". This is for historical reasons.
     * 
     * @param unit SVG unit
     * @return the SVG surface
     * @since 1.16
     */
    public SVGSurface setDocumentUnit(SVGUnit unit) {
        try {
            cairo_svg_surface_set_document_unit.invoke(handle(), unit.getValue());
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_svg_surface_set_document_unit = Interop.downcallHandle(
            "cairo_svg_surface_set_document_unit", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Restricts the generated SVG file to version . See
     * {@link SVGVersion#getVersions()} for a list of available version values that
     * can be used here.
     * <p>
     * This function should only be called before any drawing operations have been
     * performed on the given surface. The simplest way to do this is to call this
     * function immediately after creating the surface.
     * 
     * @param version SVG version
     * @return the SVG surface
     * @since 1.2
     */
    public SVGSurface restrictToVersion(SVGVersion version) {
        try {
            cairo_svg_surface_restrict_to_version.invoke(handle(), version.getValue());
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_svg_surface_restrict_to_version = Interop.downcallHandle(
            "cairo_svg_surface_restrict_to_version",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
}
