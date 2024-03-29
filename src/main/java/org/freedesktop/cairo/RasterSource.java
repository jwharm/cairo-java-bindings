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
import java.lang.ref.Cleaner;

/**
 * A user pattern providing raster data.
 * <p>
 * The raster source provides the ability to supply arbitrary pixel data whilst
 * rendering. The pixels are queried at the time of rasterisation by means of
 * user callback functions, allowing for the ultimate flexibility. For example,
 * in handling compressed image sources, you may keep a MRU cache of
 * decompressed images and decompress sources on the fly and discard old ones to
 * conserve memory.
 * <p>
 * For the raster source to be effective, you must at least specify the acquire
 * and release callbacks which are used to retrieve the pixel data for the
 * region of interest and demark when it can be freed afterwards. Other
 * callbacks are provided for when the pattern is copied temporarily during
 * rasterisation, or more permanently as a snapshot in order to keep the pixel
 * data available for printing.
 */
public class RasterSource extends Pattern {

    static {
        Cairo.ensureInitialized();
    }

    // Cleaner used to close the arena
    private static final Cleaner CLEANER = Cleaner.create();

    // Arena used to allocate the upcall stubs for the callback functions
    private final Arena arena = Arena.ofShared();

    // Callback functions
    private RasterSourceAcquireFunc acquire = null;
    private RasterSourceReleaseFunc release = null;
    private RasterSourceSnapshotFunc snapshot = null;
    private RasterSourceCopyFunc copy = null;
    private RasterSourceFinishFunc finish = null;

    /**
     * Creates a new user pattern for providing pixel data.
     * <p>
     * Use the setter functions to associate callbacks with the returned pattern.
     * The only mandatory callback is acquire.
     * 
     * @param content content type for the pixel data that will be returned. Knowing
     *                the content type ahead of time is used for analysing the
     *                operation and picking the appropriate rendering path.
     * @param width   maximum size of the sample area
     * @param height  maximum size of the sample area
     * @return a newly created {@link Pattern} of type {@link RasterSource}
     * @since 1.2
     */
    public static RasterSource create(Content content, int width, int height) {
        try {
            MemorySegment result = (MemorySegment) cairo_pattern_create_raster_source.invoke(
                    MemorySegment.NULL, content.getValue(), width, height);
            RasterSource pattern = new RasterSource(result);
            MemoryCleaner.takeOwnership(pattern.handle());
            return pattern;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_create_raster_source = Interop.downcallHandle(
        "cairo_pattern_create_raster_source", FunctionDescriptor.of(ValueLayout.ADDRESS,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

    /**
     * Specifies the callbacks used to generate the image surface for a rendering
     * operation (acquire) and the function used to cleanup that surface afterwards.
     * <p>
     * The {@code acquire} callback should create a surface (preferably an image
     * surface created to match the target using
     * {@link Surface#createSimilarImage(Surface, Format, int, int)}) that defines
     * at least the region of interest specified by extents. The surface is allowed
     * to be the entire sample area, but if it does contain a subsection of the
     * sample area, the surface extents should be provided by setting the device
     * offset (along with its width and height) using
     * {@link Surface#setDeviceOffset(double, double)}.
     * 
     * @param acquire acquire callback
     * @param release release callback
     * @since 1.12
     */
    public void setAcquire(RasterSourceAcquireFunc acquire, RasterSourceReleaseFunc release) {
        try {
            cairo_raster_source_pattern_set_acquire.invoke(handle(),
                    acquire == null ? MemorySegment.NULL : acquire.toCallback(arena),
                            release == null ? MemorySegment.NULL : release.toCallback(arena));
            this.acquire = acquire;
            this.release = release;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_raster_source_pattern_set_acquire = Interop.downcallHandle(
            "cairo_raster_source_pattern_set_acquire",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Queries the current acquire callback
     * 
     * @return the current acquire callback
     * @since 1.2
     */
    public RasterSourceAcquireFunc getAcquire() {
        return this.acquire;
    }

    /**
     * Queries the current release callback.
     * 
     * @return the current release callback
     * @since 1.2
     */
    public RasterSourceReleaseFunc getRelease() {
        return this.release;
    }

    /**
     * Sets the callback that will be used whenever a snapshot is taken of the
     * pattern, that is whenever the current contents of the pattern should be
     * preserved for later use. This is typically invoked whilst printing.
     * 
     * @param snapshot the pattern to update
     * @since 1.12
     */
    public void setSnapshot(RasterSourceSnapshotFunc snapshot) {
        try {
            cairo_raster_source_pattern_set_snapshot.invoke(handle(),
                    snapshot == null ? MemorySegment.NULL : snapshot.toCallback(arena));
            this.snapshot = snapshot;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_raster_source_pattern_set_snapshot = Interop.downcallHandle(
            "cairo_raster_source_pattern_set_snapshot",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Queries the current snapshot callback.
     * 
     * @return the current snapshot callback
     * @since 1.12
     */
    public RasterSourceSnapshotFunc getSnapshot() {
        return this.snapshot;
    }

    /**
     * Updates the copy callback which is used whenever a temporary copy of the
     * pattern is taken.
     * 
     * @param copy the copy callback
     * @since 1.12
     */
    public void setCopy(RasterSourceCopyFunc copy) {
        try {
            cairo_raster_source_pattern_set_copy.invoke(handle(),
                    copy == null ? MemorySegment.NULL : copy.toCallback(arena));
            this.copy = copy;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_raster_source_pattern_set_copy = Interop.downcallHandle(
            "cairo_raster_source_pattern_set_copy", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Queries the current copy callback.
     * 
     * @return the current copy callback
     * @since 1.12
     */
    public RasterSourceCopyFunc getCopy() {
        return this.copy;
    }

    /**
     * Updates the finish callback which is used whenever a pattern (or a copy
     * thereof) will no longer be used.
     * 
     * @param finish the finish callback
     * @since 1.12
     */
    public void setFinish(RasterSourceFinishFunc finish) {
        try {
            cairo_raster_source_pattern_set_finish.invoke(handle(),
                    finish == null ? MemorySegment.NULL : finish.toCallback(arena));
            this.finish = finish;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_raster_source_pattern_set_finish = Interop.downcallHandle(
            "cairo_raster_source_pattern_set_finish",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Queries the current finish callback.
     * 
     * @return the current finish callback
     * @since 1.12
     */
    public RasterSourceFinishFunc getFinish() {
        return this.finish;
    }

    /**
     * Constructor used internally to instantiate a java RasterSource object for a
     * native {@code cairo_pattern_t} instance
     *
     * @param address the memory address of the native {@code cairo_pattern_t}
     *                instance
     */
    public RasterSource(MemorySegment address) {
        super(address);

        // Setup a Cleaner to close the Arena and release the allocated memory for
        // the callback functions
        CleanupAction cleanup = new CleanupAction(arena);
        CLEANER.register(this, cleanup);
    }

    // Static class to separate the cleanup logic from the object being cleaned
    private record CleanupAction(Arena arena) implements Runnable {
        @Override public void run() {
            arena.close();
        }
    }
}
