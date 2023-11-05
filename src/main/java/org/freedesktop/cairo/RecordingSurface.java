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

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.MemoryCleaner;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * A recording surface records all drawing operations.
 * <p>
 * A recording surface is a surface that records all drawing operations at the
 * highest level of the surface backend interface, (that is, the level of
 * {@code paint}, {@code mask}, {@code stroke}, {@code fill}, and
 * {@code showTextGlyphs}). The recording surface can then be "replayed" against
 * any target surface by using it as a source surface.
 * <p>
 * If you want to replay a surface so that the results in target will be
 * identical to the results that would have been obtained if the original
 * operations applied to the recording surface had instead been applied to the
 * target surface, you can use code like this:
 * 
 * <pre>
 * Context cr = Context.create(target);
 * cr.setSourceSurface(recordingSurface, 0.0, 0.0);
 * cr.paint();
 * </pre>
 * 
 * A recording surface is logically unbounded, i.e. it has no implicit
 * constraint on the size of the drawing surface. However, in practice this is
 * rarely useful as you wish to replay against a particular target surface with
 * known bounds. For this case, it is more efficient to specify the target
 * extents to the recording surface upon creation.
 * <p>
 * The recording phase of the recording surface is careful to snapshot all
 * necessary objects (paths, patterns, etc.), in order to achieve accurate
 * replay.
 * 
 * @see Surface
 */
public final class RecordingSurface extends Surface {

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Constructor used internally to instantiate a java RecordingSurface object for
     * a native {@code cairo_surface_t} instance
     * 
     * @param address the memory address of the native {@code cairo_surface_t}
     *                instance
     */
    public RecordingSurface(MemorySegment address) {
        super(address);
    }

    /**
     * Creates a recording-surface which can be used to record all drawing
     * operations at the highest level (that is, the level of {@code paint},
     * {@code mask}, {@code stroke}, {@code fill}, and {@code showTextGlyphs}). The
     * recording surface can then be "replayed" against any target surface by using
     * it as a source to drawing operations.
     * <p>
     * The recording phase of the recording surface is careful to snapshot all
     * necessary objects (paths, patterns, etc.), in order to achieve accurate
     * replay.
     * 
     * @param content the content of the recording surface
     * @param extents the extents to record in pixels, can be {@code null} to record
     *                unbounded operations.
     * @return the newly created surface
     * @since 1.10
     */
    public static RecordingSurface create(Content content, Rectangle extents) {
        Status status;
        RecordingSurface surface;
        try {
            MemorySegment result = (MemorySegment) cairo_recording_surface_create.invoke(content.getValue(),
                    extents == null ? MemorySegment.NULL : extents.handle());
            surface = new RecordingSurface(result);
            MemoryCleaner.takeOwnership(surface.handle());
            status = surface.status();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
        return surface;
    }

    private static final MethodHandle cairo_recording_surface_create = Interop.downcallHandle(
            "cairo_recording_surface_create",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Measures the extents of the operations stored within the recording-surface.
     * This is useful to compute the required size of an image surface (or
     * equivalent) into which to replay the full sequence of drawing operations.
     * 
     * @return a rectangle with the x- and y-coordinates of the top-left, the width
     *         and the height of the ink bounding box
     * @since 1.10
     */
    public Rect inkExtents() {
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment ptrs = arena.allocateArray(ValueLayout.JAVA_DOUBLE, 4);
                long size = ValueLayout.JAVA_DOUBLE.byteSize();
                cairo_recording_surface_ink_extents.invoke(handle(), ptrs, ptrs.asSlice(size), ptrs.asSlice(2 * size),
                        ptrs.asSlice(3 * size));
                double[] values = ptrs.toArray(ValueLayout.JAVA_DOUBLE);
                return new Rect(values[0], values[1], values[2], values[3]);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_recording_surface_ink_extents = Interop
            .downcallHandle("cairo_recording_surface_ink_extents", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Get the extents of the recording-surface.
     * 
     * @param extents the Rectangle to be assigned the extents
     * @throws IllegalStateException if the surface is not bounded, or in an error
     *                               state
     * @since 1.12
     */
    public void getExtents(Rectangle extents) throws IllegalStateException {
        int result;
        try {
            result = (int) cairo_recording_surface_get_extents.invoke(handle(), extents.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (result == 0) {
            throw new IllegalStateException();
        }
    }

    private static final MethodHandle cairo_recording_surface_get_extents = Interop.downcallHandle(
            "cairo_recording_surface_get_extents",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
}
