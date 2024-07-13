/* cairo-java-bindings - Java language bindings for cairo
 * Copyright (C) 2023 Jan-Willem Harmannij
 *
 * SPDX-License-Identifier: LGPL-2.1-or-later
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://www.gnu.org/licenses/>.
 */

package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.ArenaCloseAction;
import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.MemoryCleaner;

import java.io.OutputStream;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.ref.Cleaner;
import java.util.Set;

import static io.github.jwharm.cairobindings.Interop.enumSetToInt;

/**
 * The PDF surface is used to render cairo graphics to Adobe PDF files and is a
 * multi-page vector surface backend.
 * <p>
 * The following mime types are supported on source patterns:
 * {@link MimeType#JPEG}, {@link MimeType#JP2}, {@link MimeType#UNIQUE_ID},
 * {@link MimeType#JBIG2}, {@link MimeType#JBIG2_GLOBAL},
 * {@link MimeType#JBIG2_GLOBAL_ID}, {@link MimeType#CCITT_FAX},
 * {@link MimeType#CCITT_FAX_PARAMS}.
 * <p>
 * <strong>JBIG2 Images</strong>
 * <p>
 * JBIG2 data in PDF must be in the embedded format as described in ISO/IEC
 * 11544. Image specific JBIG2 data must be in {@link MimeType#JBIG2}. Any
 * global segments in the JBIG2 data (segments with page association field set
 * to 0) must be in {@link MimeType#JBIG2_GLOBAL}. The global data may be shared
 * by multiple images. All images sharing the same global data must set
 * {@link MimeType#JBIG2_GLOBAL_ID} to a unique identifier. At least one of the
 * images must provide the global data using {@link MimeType#JBIG2_GLOBAL}. The
 * global data will only be embedded once and shared by all JBIG2 images with
 * the same {@link MimeType#JBIG2_GLOBAL_ID}.
 * <p>
 * <strong>CCITT Fax Images</strong>
 * <p>
 * The {@link MimeType#CCITT_FAX} mime data requires a number of decoding
 * parameters These parameters are specified using
 * {@link MimeType#CCITT_FAX_PARAMS}.
 * <p>
 * {@link MimeType#CCITT_FAX_PARAMS} mime data must contain a string of the form
 * "param1=value1 param2=value2 ...".
 * <p>
 * {@code Columns}: [required] An integer specifying the width of the image in
 * pixels.
 * <p>
 * {@code Rows}: [required] An integer specifying the height of the image in
 * scan lines.
 * <p>
 * {@code K}: [optional] An integer identifying the encoding scheme used. &lt; 0
 * is 2 dimensional Group 4, = 0 is Group3 1 dimensional, &gt; 0 is mixed 1 and
 * 2 dimensional encoding. Default is 0.
 * <p>
 * {@code EndOfLine}: [optional] If true end-of-line bit patterns are present.
 * Default is false.
 * <p>
 * {@code EncodedByteAlign}: [optional] If true the end of line is padded with 0
 * bits so the next line begins on a byte boundary. Default is false.
 * <p>
 * {@code EndOfBlock}: [optional] If true the data contains an end-of-block
 * pattern. Default is true.
 * <p>
 * {@code BlackIs1}: [optional] If true 1 bits are black pixels. Default is
 * false.
 * <p>
 * {@code DamagedRowsBeforeError}: [optional] An integer specifying the number
 * of damages rows tolerated before an error occurs. Default is 0.
 * <p>
 * Boolean values may be "true" or "false", or 1 or 0.
 * <p>
 * These parameters are the same as the CCITTFaxDecode parameters in the
 * <a href="https://www.adobe.com/products/postscript/pdfs/PLRM.pdf">PostScript
 * Language Reference</a> and <a href=
 * "https://www.adobe.com/content/dam/Adobe/en/devnet/pdf/pdfs/PDF32000_2008.pdf">Portable
 * Document Format (PDF)</a>. Refer to these documents for further details.
 * <p>
 * An example {@link MimeType#CCITT_FAX_PARAMS} string is:
 * 
 * <pre>
 * "Columns=10230 Rows=40000 K=1 EndOfLine=true EncodedByteAlign=1 BlackIs1=false"
 * </pre>
 * 
 * @see Surface
 * @since 1.2
 */
public final class PDFSurface extends Surface {

    static {
        Cairo.ensureInitialized();
    }

    /**
     * The root outline item in cairo_pdf_surface_add_outline().
     * 
     * @since 1.16
     */
    public static final int CAIRO_PDF_OUTLINE_ROOT = 0;

    /**
     * Constructor used internally to instantiate a java PDFSurface object for a
     * native {@code cairo_surface_t} instance
     * 
     * @param address the memory address of the native {@code cairo_surface_t}
     *                instance
     */
    public PDFSurface(MemorySegment address) {
        super(address);
    }

    /**
     * Creates a PDF surface of the specified size in points to be written to
     * {@code filename}.
     * 
     * @param filename       a filename for the PDF output (must be writable),
     *                       and empty String may be used to specify no output. This
     *                       will generate a PDF surface that may be queried and
     *                       used as a source, without generating a temporary file.
     * @param widthInPoints  width of the surface, in points (1 point == 1/72.0
     *                       inch)
     * @param heightInPoints height of the surface, in points (1 point == 1/72.0
     *                       inch)
     * @return the newly created surface
     * @since 1.2
     */
    public static PDFSurface create(String filename, int widthInPoints, int heightInPoints) {
        PDFSurface surface;
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment filenamePtr = Interop.allocateNativeString(filename, arena);
                MemorySegment result = (MemorySegment) cairo_pdf_surface_create.invoke(filenamePtr, widthInPoints,
                        heightInPoints);
                surface = new PDFSurface(result);
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

    private static final MethodHandle cairo_pdf_surface_create = Interop.downcallHandle("cairo_pdf_surface_create",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

    /**
     * Creates a PDF surface of the specified size in points to be written
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
    public static PDFSurface create(OutputStream stream, int widthInPoints, int heightInPoints) {
        PDFSurface surface;
        Arena arena = Arena.ofConfined();
        try {
            MemorySegment writeFuncPtr;
            if (stream != null) {
                WriteFunc writeFunc = stream::write;
                writeFuncPtr = writeFunc.toCallback(arena);
            } else {
                writeFuncPtr = MemorySegment.NULL;
            }
            MemorySegment result = (MemorySegment) cairo_pdf_surface_create_for_stream.invoke(writeFuncPtr,
                    MemorySegment.NULL, widthInPoints, heightInPoints);
            surface = new PDFSurface(result);
            MemoryCleaner.takeOwnership(surface.handle());
            if (stream != null) {
                ArenaCloseAction.CLEANER.register(surface, new ArenaCloseAction(arena));
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (surface.status() == Status.NO_MEMORY) {
            throw new RuntimeException(surface.status().toString());
        }
        return surface;
    }

    private static final MethodHandle cairo_pdf_surface_create_for_stream = Interop
            .downcallHandle(
                    "cairo_pdf_surface_create_for_stream", FunctionDescriptor.of(ValueLayout.ADDRESS,
                            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

    /**
     * Restricts the generated PDF file to version . See
     * {@link PDFVersion#getVersions()} for a list of available version values that
     * can be used here.
     * <p>
     * This function should only be called before any drawing operations have been
     * performed on the given surface. The simplest way to do this is to call this
     * function immediately after creating the surface.
     * 
     * @param version PDF version
     * @return the PDF surface
     * @since 1.10
     */
    public PDFSurface restrictToVersion(PDFVersion version) {
        try {
            cairo_pdf_surface_restrict_to_version.invoke(handle(), version.getValue());
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pdf_surface_restrict_to_version = Interop.downcallHandle(
            "cairo_pdf_surface_restrict_to_version", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Changes the size of a PDF surface for the current (and subsequent) pages.
     * <p>
     * This function should only be called before any drawing operations have been
     * performed on the current page. The simplest way to do this is to call this
     * function immediately after creating the surface or immediately after
     * completing a page with either {@link Context#showPage()} or
     * {@link Context#copyPage()}.
     * 
     * @param widthInPoints  new surface width, in points (1 point == 1/72.0 inch)
     * @param heightInPoints new surface height, in points (1 point == 1/72.0 inch)
     * @return the PDF surface
     * @since 1.2
     */
    public PDFSurface setSize(double widthInPoints, double heightInPoints) {
        try {
            cairo_pdf_surface_set_size.invoke(handle(), widthInPoints, heightInPoints);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pdf_surface_set_size = Interop.downcallHandle("cairo_pdf_surface_set_size",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Add an item to the document outline hierarchy with the name {@code utf8} that
     * links to the location specified by {@code linkAttribs}. Link attributes have
     * the same keys and values as the <a href=
     * "https://www.cairographics.org/manual/cairo-Tags-and-Links.html#link">Link
     * Tag</a>, excluding the "rect" attribute. The item will be a child of the item
     * with id {@code parentId}. Use {@link #CAIRO_PDF_OUTLINE_ROOT} as the parent
     * id of top level items.
     * 
     * @param parentId    the id of the parent item or
     *                    {@link #CAIRO_PDF_OUTLINE_ROOT} if this is a top level
     *                    item.
     * @param string      the name of the outline
     * @param linkAttribs the link attributes specifying where this outline links to
     * @param flags       outline item flags
     * @return the id for the added item.
     * @since 1.16
     */
    public int addOutline(int parentId, String string, String linkAttribs, Set<PDFOutlineFlags> flags) {
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment utf8 = Interop.allocateNativeString(string, arena);
                MemorySegment linkAttribsPtr = linkAttribs == null ? MemorySegment.NULL
                        : arena.allocateFrom(linkAttribs);
                return (int) cairo_pdf_surface_add_outline.invoke(handle(), parentId, utf8, linkAttribsPtr,
                        enumSetToInt(flags));
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pdf_surface_add_outline = Interop
            .downcallHandle(
                    "cairo_pdf_surface_add_outline", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                            ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Set document metadata. The {@link PDFMetadata#CREATE_DATE} and
     * {@link PDFMetadata#MOD_DATE} values must be in ISO-8601 format:
     * YYYY-MM-DDThh:mm:ss. An optional timezone of the form "[+/-]hh:mm" or "Z" for
     * UTC time can be appended. All other metadata values can be any UTF-8 string.
     * <p>
     * For example:
     * 
     * <pre>
     * surface.setMetadata(PDFMetadata.TITLE, "My Document");
     * surface.setMetadata(PDFMetadata.CREATE_DATE, "2015-12-31T23:59+02:00");
     * </pre>
     * 
     * @param metadata The metadata item to set.
     * @param string     metadata value
     * @return the PDF surface
     * @since 1.16
     */
    public PDFSurface setMetadata(PDFMetadata metadata, String string) {
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment utf8 = Interop.allocateNativeString(string, arena);
                cairo_pdf_surface_set_metadata.invoke(handle(), metadata.getValue(), utf8);
                return this;
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pdf_surface_set_metadata = Interop.downcallHandle(
            "cairo_pdf_surface_set_metadata",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Set custom document metadata. name may be any string except for the
     * following names reserved by PDF: "Title", "Author", "Subject", "Keywords",
     * "Creator", "Producer", "CreationDate", "ModDate", "Trapped".
     * <p>
     * If {@code value} is null or an empty string, the {@code name} metadata will not be set.
     * <p>
     * For example:
     * <p>
     * {@code pdfSurface.setCustomMetadata("ISBN", "978-0123456789");}
     * @param name  The name of the custom metadata item to set (utf8).
     * @param value The value of the metadata (utf8).
     * @return the PDF surface
     * @since 1.18
     */
    public PDFSurface setCustomMetadata(String name, String value) {
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment namePtr = Interop.allocateNativeString(name, arena);
                MemorySegment valuePtr = Interop.allocateNativeString(value, arena);
                cairo_pdf_surface_set_custom_metadata.invoke(handle(), namePtr, valuePtr);
                return this;
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pdf_surface_set_custom_metadata = Interop.downcallHandle(
            "cairo_pdf_surface_set_custom_metadata",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Set page label for the current page.
     * 
     * @param string The page label.
     * @return the PDF surface
     * @since 1.16
     */
    public PDFSurface setPageLabel(String string) {
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment utf8 = Interop.allocateNativeString(string, arena);
                cairo_pdf_surface_set_page_label.invoke(handle(), utf8);
                return this;
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pdf_surface_set_page_label = Interop.downcallHandle(
            "cairo_pdf_surface_set_page_label", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Set the thumbnail image size for the current and all subsequent pages.
     * Setting a width or height of 0 disables thumbnails for the current and
     * subsequent pages.
     * 
     * @param width  Thumbnail width.
     * @param height Thumbnail height.
     * @return the PDF surface
     * @since 1.16
     */
    public PDFSurface setThumbnailSize(int width, int height) {
        try {
            cairo_pdf_surface_set_thumbnail_size.invoke(handle(), width, height);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pdf_surface_set_thumbnail_size = Interop.downcallHandle(
            "cairo_pdf_surface_set_thumbnail_size",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
}
