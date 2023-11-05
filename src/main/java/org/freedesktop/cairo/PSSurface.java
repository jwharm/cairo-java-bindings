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

/**
 * The PostScript surface is used to render cairo graphics to Adobe PostScript
 * files and is a multi-page vector surface backend.
 * <p>
 * The following mime types are supported on source patterns:
 * {@link MimeType#JPEG}, {@link MimeType#UNIQUE_ID},
 * {@link MimeType#CCITT_FAX}, {@link MimeType#CCITT_FAX_PARAMS},
 * {@link MimeType#TYPE_EPS}, {@link MimeType#EPS_PARAMS}.
 * <p>
 * Source surfaces used by the PostScript surface that have a
 * {@link MimeType#UNIQUE_ID} mime type will be stored in PostScript printer
 * memory for the duration of the print job. {@link MimeType#UNIQUE_ID} should
 * only be used for small frequently used sources.
 * <p>
 * The {@link MimeType#CCITT_FAX} and {@link MimeType#CCITT_FAX_PARAMS} mime
 * types are documented in CCITT Fax Images.
 * <p>
 * <strong>Embedding EPS files</strong>
 * <p>
 * Encapsulated PostScript files can be embedded in the PS output by setting the
 * {@link MimeType#TYPE_EPS} mime data on a surface to the EPS data and painting
 * the surface. The EPS will be scaled and translated to the extents of the
 * surface the EPS data is attached to.
 * <p>
 * The {@link MimeType#TYPE_EPS} mime type requires the
 * {@link MimeType#EPS_PARAMS} mime data to also be provided in order to specify
 * the embedding parameters. {@link MimeType#EPS_PARAMS} mime data must contain
 * a string of the form {@code "bbox=[llx lly urx ury]"} that specifies the
 * bounding box (in PS coordinates) of the EPS graphics. The parameters are:
 * lower left x, lower left y, upper right x, upper right y. Normally the bbox
 * data is identical to the {@code BoundingBox} data in the EPS file.
 * 
 * @see Surface
 * @since 1.2
 */
public final class PSSurface extends Surface {

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Constructor used internally to instantiate a java PSSurface object for a
     * native {@code cairo_surface_t} instance
     * 
     * @param address the memory address of the native {@code cairo_surface_t}
     *                instance
     */
    public PSSurface(MemorySegment address) {
        super(address);
    }

    /**
     * Creates a PostScript surface of the specified size in points to be written to
     * {@code filename}. See {@link #create(OutputStream, int, int)} for a more
     * flexible mechanism for handling the PostScript output than simply writing it
     * to a named file.
     * <p>
     * Note that the size of individual pages of the PostScript output can vary. See
     * {@link #setSize(double, double)}.
     * 
     * @param filename       a filename for the PS output (must be writable),
     *                       {@code null} may be used to specify no output. This
     *                       will generate a PS surface that may be queried and used
     *                       as a source, without generating a temporary file.
     * @param widthInPoints  width of the surface, in points (1 point == 1/72.0
     *                       inch)
     * @param heightInPoints height of the surface, in points (1 point == 1/72.0
     *                       inch)
     * @return the newly created surface.
     * @since 1.2
     */
    public static PSSurface create(String filename, int widthInPoints, int heightInPoints) {
        PSSurface surface;
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment filenamePtr = Interop.allocateNativeString(filename, arena);
                MemorySegment result = (MemorySegment) cairo_ps_surface_create.invoke(filenamePtr, widthInPoints,
                        heightInPoints);
                surface = new PSSurface(result);
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

    private static final MethodHandle cairo_ps_surface_create = Interop.downcallHandle("cairo_ps_surface_create",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

    /**
     * Creates a PostScript surface of the specified size in points to be written
     * incrementally to the OutputStream {@code stream}. See
     * {@link #create(String, int, int)} for a more convenient way to simply direct
     * the PostScript output to a named file.
     * <p>
     * Note that the size of individual pages of the PostScript output can vary. See
     * {@link #setSize(double, double)}.
     * 
     * @param stream         an {@link OutputStream} to accept the output data, may
     *                       be {@code null} to indicate a no-op stream. With a
     *                       no-op stream, the surface may be queried or used as a
     *                       source without generating any temporary files.
     * @param widthInPoints  width of the surface, in points (1 point == 1/72.0
     *                       inch)
     * @param heightInPoints height of the surface, in points (1 point == 1/72.0
     *                       inch)
     * @return the newly created surface.
     * @since 1.2
     */
    public static PSSurface create(OutputStream stream, int widthInPoints, int heightInPoints) {
        PSSurface surface;
        Arena arena = Arena.ofConfined();
        try {
            MemorySegment writeFuncPtr;
            if (stream != null) {
                WriteFunc writeFunc = stream::write;
                writeFuncPtr = writeFunc.toCallback(arena);
            } else {
                writeFuncPtr = MemorySegment.NULL;
            }
            MemorySegment result = (MemorySegment) cairo_ps_surface_create_for_stream.invoke(writeFuncPtr,
                    MemorySegment.NULL, widthInPoints, heightInPoints);
            surface = new PSSurface(result);
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

    private static final MethodHandle cairo_ps_surface_create_for_stream = Interop
            .downcallHandle(
                    "cairo_ps_surface_create_for_stream", FunctionDescriptor.of(ValueLayout.ADDRESS,
                            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

    /**
     * Restricts the generated PostSript file to {@code level}. See
     * {@link PSLevel#getLevels()} for a list of available level values that
     * can be used here.
     * <p>
     * This function should only be called before any drawing operations have been
     * performed on the given surface. The simplest way to do this is to call this
     * function immediately after creating the surface.
     * 
     * @param level PostScript level
     * @return the PostScript surface
     * @since 1.6
     */
    public PSSurface restrictToLevel(PSLevel level) {
        try {
            cairo_ps_surface_restrict_to_level.invoke(handle(), level.getValue());
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_ps_surface_restrict_to_level = Interop.downcallHandle(
            "cairo_ps_surface_restrict_to_level", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * If eps is {@code true}, the PostScript surface will output Encapsulated
     * PostScript.
     * <p>
     * This function should only be called before any drawing operations have been
     * performed on the current page. The simplest way to do this is to call this
     * function immediately after creating the surface. An Encapsulated PostScript
     * file should never contain more than one page.
     * 
     * @param eps {@code true} to output EPS format PostScript
     * @return the PostScript surface
     * @since 1.6
     */
    public PSSurface setEPS(boolean eps) {
        try {
            cairo_ps_surface_set_eps.invoke(handle(), eps ? 1 : 0);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_ps_surface_set_eps = Interop.downcallHandle("cairo_ps_surface_set_eps",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Check whether the PostScript surface will output Encapsulated PostScript.
     * 
     * @return {@code true} if the surface will output Encapsulated PostScript.
     * @since 1.6
     */
    public boolean getEPS() {
        try {
            int result = (int) cairo_ps_surface_get_eps.invoke(handle());
            return result != 0;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_ps_surface_get_eps = Interop.downcallHandle("cairo_ps_surface_get_eps",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Changes the size of a PostScript surface for the current (and subsequent)
     * pages.
     * <p>
     * This function should only be called before any drawing operations have been
     * performed on the current page. The simplest way to do this is to call this
     * function immediately after creating the surface or immediately after
     * completing a page with either {@link Context#showPage()} or
     * {@link Context#copyPage()}.
     * 
     * @param widthInPoints  new surface width, in points (1 point == 1/72.0 inch)
     * @param heightInPoints new surface height, in points (1 point == 1/72.0 inch)
     * @return the PostScript surface
     * @since 1.2
     */
    public PSSurface setSize(double widthInPoints, double heightInPoints) {
        try {
            cairo_ps_surface_set_size.invoke(handle(), widthInPoints, heightInPoints);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_ps_surface_set_size = Interop.downcallHandle("cairo_ps_surface_set_size",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * This function indicates that subsequent calls to {@link #dscComment(String)}
     * should direct comments to the Setup section of the PostScript output.
     * <p>
     * This function should be called at most once per surface, and must be called
     * before any call to {@link #dscBeginPageSetup()} and before any drawing is
     * performed to the surface.
     * <p>
     * See {@link #dscComment(String)} for more details.
     * 
     * @return the PostScript surface
     * @since 1.2
     */
    public PSSurface dscBeginSetup() {
        try {
            cairo_ps_surface_dsc_begin_setup.invoke(handle());
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_ps_surface_dsc_begin_setup = Interop
            .downcallHandle("cairo_ps_surface_dsc_begin_setup", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

    /**
     * This function indicates that subsequent calls to {@link #dscComment(String)}
     * should direct comments to the PageSetup section of the PostScript output.
     * <p>
     * This function call is only needed for the first page of a surface. It should
     * be called after any call to {@link #dscBeginSetup()} and before any drawing
     * is performed to the surface.
     * <p>
     * See {@link #dscComment(String)} for more details.
     * 
     * @return the PostScript surface
     * @since 1.2
     */
    public PSSurface dscBeginPageSetup() {
        try {
            cairo_ps_surface_dsc_begin_page_setup.invoke(handle());
            return this;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_ps_surface_dsc_begin_page_setup = Interop.downcallHandle(
            "cairo_ps_surface_dsc_begin_page_setup", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

    /**
     * Emit a comment into the PostScript output for the given surface.
     * <p>
     * The comment is expected to conform to the PostScript Language Document
     * Structuring Conventions (DSC). Please see that manual for details on the
     * available comments and their meanings. In particular, the %%IncludeFeature
     * comment allows a device-independent means of controlling printer device
     * features. So the PostScript Printer Description Files Specification will also
     * be a useful reference.
     * <p>
     * The comment string must begin with a percent character (%) and the total
     * length of the string (including any initial percent characters) must not
     * exceed 255 characters. Violating either of these conditions will throw
     * {@link IllegalArgumentException} and place the surface into an error state.
     * But beyond these two conditions, this function will not enforce conformance
     * of the comment with any particular specification.
     * <p>
     * The comment string must not contain any newline characters.
     * <p>
     * The DSC specifies different sections in which particular comments can appear.
     * This function provides for comments to be emitted within three sections: the
     * header, the Setup section, and the PageSetup section. Comments appearing in
     * the first two sections apply to the entire document while comments in the
     * BeginPageSetup section apply only to a single page.
     * <p>
     * For comments to appear in the header section, this function should be called
     * after the surface is created, but before a call to {@link #dscBeginSetup()}.
     * <p>
     * For comments to appear in the Setup section, this function should be called
     * after a call to {@link #dscBeginSetup()} but before a call to
     * {@link #dscBeginPageSetup()}.
     * <p>
     * For comments to appear in the PageSetup section, this function should be
     * called after a call to {@link #dscBeginPageSetup()}.
     * <p>
     * Note that it is only necessary to call {@link #dscBeginPageSetup()} for the
     * first page of any surface. After a call to {@link Context#showPage()} or
     * {@link Context#copyPage()} comments are unambiguously directed to the
     * PageSetup section of the current page. But it doesn't hurt to call this
     * function at the beginning of every page as that consistency may make the
     * calling code simpler.
     * <p>
     * As a final note, cairo automatically generates several comments on its own.
     * As such, applications must not manually generate any of the following
     * comments:
     * <p>
     * Header section: {@code %!PS-Adobe-3.0}, {@code %%Creator},
     * {@code %%CreationDate}, {@code %%Pages}, {@code %%BoundingBox},
     * {@code %%DocumentData}, {@code %%LanguageLevel}, {@code %%EndComments}.
     * <p>
     * Setup section: {@code %%BeginSetup}, {@code %%EndSetup}
     * <p>
     * PageSetup section: {@code %%BeginPageSetup}, {@code %%PageBoundingBox},
     * {@code %%EndPageSetup}.
     * <p>
     * Other sections: {@code %%BeginProlog}, {@code %%EndProlog}, {@code %%Page},
     * {@code %%Trailer}, {@code %%EOF}
     * <p>
     * Here is an example sequence showing how this function might be used:
     * 
     * <pre>
     * var surface = PSSurface.create(filename, width, height);
     * ...
     * surface.dscComment("%%Title: My excellent document")
     *        .dscComment("%%Copyright: Copyright (C) 2006 Cairo Lover")
     *        .beginSetup()
     *        .dscComment("%%IncludeFeature: *MediaColor White");
     *        ...
     *        .beginPageSetup()
     *        .dscComment("%%IncludeFeature: *PageSize A3")
     *        .dscComment("%%IncludeFeature: *InputSlot LargeCapacity")
     *        .dscComment("%%IncludeFeature: *MediaType Glossy")
     *        .dscComment("%%IncludeFeature: *MediaColor Blue");
     * ... draw to first page here ..
     * context.showPage();
     * ...
     * surface.dscComment("%%IncludeFeature: *PageSize A5");
     * ...
     * </pre>
     * 
     * @param comment a comment string to be emitted into the PostScript output
     * @return the PostScript surface
     * @throws IllegalArgumentException if the comment string does not begin with a
     *                                  percent character (%) or the total length of
     *                                  the string (including any initial percent
     *                                  characters) exceeds 255 characters.
     * @since 1.2
     */
    public PSSurface dscComment(String comment) throws IllegalArgumentException {
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment commentPtr = Interop.allocateNativeString(comment, arena);
                cairo_ps_surface_dsc_comment.invoke(handle(), commentPtr);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status() != Status.SUCCESS) {
            throw new IllegalArgumentException(status().toString());
        }
        return this;
    }

    private static final MethodHandle cairo_ps_surface_dsc_comment = Interop.downcallHandle(
            "cairo_ps_surface_dsc_comment", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));
}
