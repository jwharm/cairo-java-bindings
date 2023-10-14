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

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.List;

/**
 * A surface that exists solely to watch what another surface is doing.
 *
 * @see Surface
 */
public final class SurfaceObserver extends Surface {

    static {
        Cairo.ensureInitialized();
    }

    // Keep a reference to the callback functions that are passed to the
    // Observer during its lifetime.
    @SuppressWarnings("unused")
    private final List<SurfaceObserverCallback> callbacks = new ArrayList<>();

    /**
     * Constructor used internally to instantiate a java SurfaceObserver object for
     * a native {@code cairo_surface_t} instance
     *
     * @param address the memory address of the native {@code cairo_surface_t}
     *                instance
     */
    public SurfaceObserver(MemorySegment address) {
        super(address);
    }

    /**
     * Create a new surface that exists solely to watch another is doing. In the
     * process it will log operations and times, which are fast, which are slow,
     * which are frequent, etc.
     * <p>
     * The mode parameter can be set to either {@link SurfaceObserverMode#NORMAL} or
     * {@link SurfaceObserverMode#RECORD_OPERATIONS}, to control whether or not the
     * internal observer should record operations.
     *
     * @param target an existing surface for which the observer will watch
     * @param mode   sets the mode of operation (normal vs. record)
     * @return the newly allocated surface
     * @since 1.12
     */
    public static SurfaceObserver create(Surface target, SurfaceObserverMode mode) {
        SurfaceObserver surface;
        try {
            MemorySegment result = (MemorySegment) cairo_surface_create_observer.invoke(target.handle(), mode.getValue());
            surface = new SurfaceObserver(result);
            MemoryCleaner.takeOwnership(surface.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return surface;
    }

    private static final MethodHandle cairo_surface_create_observer = Interop.downcallHandle("cairo_surface_create_observer",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Adds a callback for fill operations on the observed surface.
     *
     * @param func callback function for fill operations
     * @return this SurfaceObserver
     * @since 1.12
     */
    public SurfaceObserver addFillCallback(SurfaceObserverCallback func) {
        try {
            int ignored = (int) cairo_surface_observer_add_fill_callback.invoke(handle(),
                    func == null ? MemorySegment.NULL : func.toCallback(handle().scope()), MemorySegment.NULL);
            callbacks.add(func);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    private static final MethodHandle cairo_surface_observer_add_fill_callback = Interop.downcallHandle(
            "cairo_surface_observer_add_fill_callback",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Adds a callback for finish operations on the observed surface.
     *
     * @param func callback function for the finish operation
     * @return this SurfaceObserver
     * @since 1.12
     */
    public SurfaceObserver addFinishCallback(SurfaceObserverCallback func) {
        try {
            int ignored = (int) cairo_surface_observer_add_finish_callback.invoke(handle(),
                    func == null ? MemorySegment.NULL : func.toCallback(handle().scope()), MemorySegment.NULL);
            callbacks.add(func);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    private static final MethodHandle cairo_surface_observer_add_finish_callback = Interop.downcallHandle(
            "cairo_surface_observer_add_finish_callback",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Adds a callback for flush operations on the observed surface.
     *
     * @param func callback for flush operations
     * @return this SurfaceObserver
     * @since 1.12
     */
    public SurfaceObserver addFlushCallback(SurfaceObserverCallback func) {
        try {
            int ignored = (int) cairo_surface_observer_add_flush_callback.invoke(handle(),
                    func == null ? MemorySegment.NULL : func.toCallback(handle().scope()), MemorySegment.NULL);
            callbacks.add(func);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    private static final MethodHandle cairo_surface_observer_add_flush_callback = Interop.downcallHandle(
            "cairo_surface_observer_add_flush_callback",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Adds a callback for glyph operations on the observed surface.
     *
     * @param func callback function for glyph operations
     * @return this SurfaceObserver
     * @since 1.12
     */
    public SurfaceObserver addGlyphsCallback(SurfaceObserverCallback func) {
        try {
            int ignored = (int) cairo_surface_observer_add_glyphs_callback.invoke(handle(),
                    func == null ? MemorySegment.NULL : func.toCallback(handle().scope()), MemorySegment.NULL);
            callbacks.add(func);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    private static final MethodHandle cairo_surface_observer_add_glyphs_callback = Interop.downcallHandle(
            "cairo_surface_observer_add_glyphs_callback",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Adds a callback for mask operations on the observed surface.
     *
     * @param func callback function for mask operations
     * @return this SurfaceObserver
     * @since 1.12
     */
    public SurfaceObserver addMaskCallback(SurfaceObserverCallback func) {
        try {
            int ignored = (int) cairo_surface_observer_add_mask_callback.invoke(handle(),
                    func == null ? MemorySegment.NULL : func.toCallback(handle().scope()), MemorySegment.NULL);
            callbacks.add(func);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    private static final MethodHandle cairo_surface_observer_add_mask_callback = Interop.downcallHandle(
            "cairo_surface_observer_add_mask_callback",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Adds a callback for paint operations on the observed surface.
     *
     * @param func callback function for paint operations
     * @return this SurfaceObserver
     * @since 1.12
     */
    public SurfaceObserver addPaintCallback(SurfaceObserverCallback func) {
        try {
            int ignored = (int) cairo_surface_observer_add_paint_callback.invoke(handle(),
                    func == null ? MemorySegment.NULL : func.toCallback(handle().scope()), MemorySegment.NULL);
            callbacks.add(func);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    private static final MethodHandle cairo_surface_observer_add_paint_callback = Interop.downcallHandle(
            "cairo_surface_observer_add_paint_callback",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Adds a callback for stroke operations on the observed surface.
     *
     * @param func callback function for stroke operations
     * @return this SurfaceObserver
     * @since 1.12
     */
    public SurfaceObserver addStrokeCallback(SurfaceObserverCallback func) {
        try {
            int ignored = (int) cairo_surface_observer_add_stroke_callback.invoke(handle(),
                    func == null ? MemorySegment.NULL : func.toCallback(handle().scope()), MemorySegment.NULL);
            callbacks.add(func);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    private static final MethodHandle cairo_surface_observer_add_stroke_callback = Interop.downcallHandle(
            "cairo_surface_observer_add_stroke_callback",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Returns the total observation time.
     *
     * @return the elapsed time, in nanoseconds
     * @since 1.12
     */
    public double elapsed() {
        try {
            return (double) cairo_surface_observer_elapsed.invoke(handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_observer_elapsed = Interop.downcallHandle(
            "cairo_surface_observer_elapsed", FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS));

    /**
     * Prints the observer log using the given callback.
     *
     * @param writeFunc callback for writing on a stream
     * @since 1.12
     */
    public void print(WriteFunc writeFunc) {
        try {
            int ignored = (int) cairo_surface_observer_print.invoke(handle(),
                    writeFunc == null ? MemorySegment.NULL : writeFunc.toCallback(handle().scope()), MemorySegment.NULL);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_surface_observer_print = Interop.downcallHandle(
            "cairo_surface_observer_print",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
}
