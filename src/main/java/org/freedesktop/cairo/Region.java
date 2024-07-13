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

import io.github.jwharm.cairobindings.Proxy;
import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.MemoryCleaner;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * A {@link Region} represents a set of integer-aligned rectangles.
 * <p>
 * Regions are a simple graphical data type representing an area of
 * integer-aligned rectangles. They are often used on raster surfaces to track
 * areas of interest, such as change or clip areas.
 * <p>
 * It allows set-theoretical operations like {@link Region#union} and
 * {@link Region#intersect} to be performed on them.
 * 
 * @since 1.10
 */
public class Region extends Proxy {

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Constructor used internally to instantiate a java Region object for a native
     * {@code cairo_region_t} instance
     * 
     * @param address the memory address of the native {@code cairo_region_t}
     *                instance
     */
    public Region(MemorySegment address) {
        super(address);
        MemoryCleaner.setFreeFunc(handle(), "cairo_region_destroy");
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
     * Allocates a new empty region object.
     * 
     * @return A newly allocated Region
     * @since 1.10
     */
    public static Region create() {
        Region region;
        try {
            MemorySegment result = (MemorySegment) cairo_region_create.invoke();
            region = new Region(result);
            MemoryCleaner.takeOwnership(region.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (region.status() == Status.NO_MEMORY) {
            throw new RuntimeException(region.status().toString());
        }
        return region;
    }

    private static final MethodHandle cairo_region_create = Interop.downcallHandle("cairo_region_create",
            FunctionDescriptor.of(ValueLayout.ADDRESS));

    /**
     * Allocates a new region object containing {@code rectangle}.
     *
     * @param  rectangle a {@link RectangleInt}
     * @return A newly allocated Region
     * @since 1.10
     */
    public static Region create(RectangleInt rectangle) {
        Region region;
        try {
            MemorySegment result = (MemorySegment) cairo_region_create_rectangle
                    .invoke(rectangle == null ? MemorySegment.NULL : rectangle.handle());
            region = new Region(result);
            MemoryCleaner.takeOwnership(region.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (region.status() == Status.NO_MEMORY) {
            throw new RuntimeException(region.status().toString());
        }
        return region;
    }

    private static final MethodHandle cairo_region_create_rectangle = Interop.downcallHandle(
            "cairo_region_create_rectangle", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Allocates a new region object containing the union of all given
     * {@code rects}.
     * 
     * @param rects an array of rectangles
     * @return A newly allocated Region
     * @since 1.10
     */
    public static Region create(RectangleInt[] rects) {
        Region region;
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment rectsPtr = MemorySegment.NULL;
                if (rects != null) {
                    rectsPtr = arena.allocate(ValueLayout.ADDRESS, rects.length);
                    for (int i = 0; i < rects.length; i++) {
                        rectsPtr.setAtIndex(ValueLayout.ADDRESS, i,
                                rects[i] == null ? MemorySegment.NULL : rects[i].handle());
                    }
                }
                MemorySegment result = (MemorySegment) cairo_region_create_rectangles.invoke(rectsPtr,
                        rects == null ? 0 : rects.length);
                region = new Region(result);
                MemoryCleaner.takeOwnership(region.handle());
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (region.status() == Status.NO_MEMORY) {
            throw new RuntimeException(region.status().toString());
        }
        return region;
    }

    private static final MethodHandle cairo_region_create_rectangles = Interop.downcallHandle(
            "cairo_region_create_rectangles",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Allocates a new region object copying the area from this region.
     *
     * @return A newly allocated Region
     * @since 1.10
     */
    public Region copy() {
        Region copy;
        try {
            MemorySegment result = (MemorySegment) cairo_region_copy.invoke(handle());
            copy = new Region(result);
            MemoryCleaner.takeOwnership(copy.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (copy.status() == Status.NO_MEMORY) {
            throw new RuntimeException(copy.status().toString());
        }
        return copy;
    }

    private static final MethodHandle cairo_region_copy = Interop.downcallHandle("cairo_region_copy",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Checks whether an error has previous occurred for this region object.
     * 
     * @return {@link Status#SUCCESS} or {@link Status#NO_MEMORY}
     * @since 1.10
     */
    public Status status() {
        try {
            int result = (int) cairo_region_status.invoke(handle());
            return Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_region_status = Interop.downcallHandle("cairo_region_status",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Gets the bounding rectangle of region as a {@link RectangleInt}
     * 
     * @param extents rectangle into which to store the extents
     * @since 1.10
     */
    public void getExtents(RectangleInt extents) {
        try {
            cairo_region_get_extents.invoke(handle(), extents.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_region_get_extents = Interop.downcallHandle("cairo_region_get_extents",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Returns the number of rectangles contained in this region.
     * 
     * @return The number of rectangles contained in this region.
     * @since 1.10
     */
    public int numRectangles() {
        try {
            return (int) cairo_region_num_rectangles.invoke(handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_region_num_rectangles = Interop.downcallHandle(
            "cairo_region_num_rectangles", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Returns the {@code nth} rectangle from this region.
     * 
     * @param nth       a number indicating which rectangle should be returned
     * @param rectangle a RectangleInt
     * @since 1.10
     */
    public void getRectangle(int nth, RectangleInt rectangle) {
        try {
            cairo_region_get_rectangle.invoke(handle(), nth, rectangle.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_region_get_rectangle = Interop.downcallHandle("cairo_region_get_rectangle",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Checks whether this region is empty.
     * 
     * @return {@code true} if region is empty, {@code false} if it isn't.
     * @since 1.10
     */
    public boolean isEmpty() {
        try {
            int result = (int) cairo_region_is_empty.invoke(handle());
            return result != 0;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_region_is_empty = Interop.downcallHandle("cairo_region_is_empty",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Checks whether ({@code x}, {@code y}) is contained in this region.
     * 
     * @param x the x coordinate of a point
     * @param y the y coordinate of a point
     * @return {@code true} if ({@code x}, {@code y}) is contained in this region,
     *         {@code false} if it is not.
     * @since 1.10
     */
    public boolean containsPoint(int x, int y) {
        try {
            int result = (int) cairo_region_contains_point.invoke(handle(), x, y);
            return result != 0;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_region_contains_point = Interop
            .downcallHandle("cairo_region_contains_point", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

    /**
     * Checks whether {@code rectangle} is inside, outside or partially contained in
     * this region
     * 
     * @param rectangle a {@link RectangleInt}
     * @return {@link RegionOverlap#IN} if {@code rectangle} is entirely inside this
     *         region, {@link RegionOverlap#OUT} if {@code rectangle} is entirely
     *         outside this region, or {@link RegionOverlap#PART} if
     *         {@code rectangle} is partially inside and partially outside this
     *         region.
     * @since 1.10
     */
    public RegionOverlap containsRectangle(RectangleInt rectangle) {
        try {
            int result = (int) cairo_region_contains_rectangle.invoke(handle(),
                    rectangle == null ? MemorySegment.NULL : rectangle.handle());
            return RegionOverlap.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_region_contains_rectangle = Interop.downcallHandle(
            "cairo_region_contains_rectangle",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Compares whether this region is equivalent to {@code other}. {@code null} as
     * an argument is equal to itself, but not to any non-{@code null} region.
     * 
     * @param other a {@link Region} or {@code null}
     * @return {@code true} if both regions contained the same coverage,
     *         {@code false} if it is not or any region is in an error status.
     * @since 1.10
     */
    public boolean equal(Region other) {
        // This method could be used to override equals(Object other), but then
        // we should override hashcode() too, and there is no straightforward
        // way to do that. So we keep it as a separate method (at least for now).
        try {
            int result = (int) cairo_region_equal.invoke(handle(), other == null ? MemorySegment.NULL : other.handle());
            return result != 0;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_region_equal = Interop.downcallHandle("cairo_region_equal",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Translates this region by ({@code dx}, {@code dy}).
     * 
     * @param dx Amount to translate in the x direction
     * @param dy Amount to translate in the y direction
     * @since 1.10
     */
    public void translate(int dx, int dy) {
        try {
            cairo_region_translate.invoke(handle(), dx, dy);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_region_translate = Interop.downcallHandle("cairo_region_translate",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

    /**
     * Computes the intersection of this region with {@code other} and places the
     * result in this region
     * 
     * @param other another {@link Region}
     * @since 1.10
     */
    public void intersect(Region other) {
        Status status;
        try {
            int result = (int) cairo_region_intersect.invoke(handle(),
                    other == null ? MemorySegment.NULL : other.handle());
            status = Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
    }

    private static final MethodHandle cairo_region_intersect = Interop.downcallHandle("cairo_region_intersect",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Computes the intersection of this region with {@code rectangle} and places
     * the result in this region
     * 
     * @param rectangle a {@link Rectangle}
     * @since 1.10
     */
    public void intersect(RectangleInt rectangle) {
        Status status;
        try {
            int result = (int) cairo_region_intersect_rectangle.invoke(handle(),
                    rectangle == null ? MemorySegment.NULL : rectangle.handle());
            status = Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
    }

    private static final MethodHandle cairo_region_intersect_rectangle = Interop.downcallHandle(
            "cairo_region_intersect_rectangle",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Subtracts {@code other} from this region and places the result in this region
     * 
     * @param other another {@link Region}
     * @since 1.10
     */
    public void subtract(Region other) {
        Status status;
        try {
            int result = (int) cairo_region_subtract.invoke(handle(),
                    other == null ? MemorySegment.NULL : other.handle());
            status = Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
    }

    private static final MethodHandle cairo_region_subtract = Interop.downcallHandle("cairo_region_subtract",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Subtracts {@code rectangle} from this region and places the result in this
     * region
     * 
     * @param rectangle a {@link Rectangle}
     * @since 1.10
     */
    public void subtract(RectangleInt rectangle) {
        Status status;
        try {
            int result = (int) cairo_region_subtract_rectangle.invoke(handle(),
                    rectangle == null ? MemorySegment.NULL : rectangle.handle());
            status = Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
    }

    private static final MethodHandle cairo_region_subtract_rectangle = Interop.downcallHandle(
            "cairo_region_subtract_rectangle",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Computes the union of this region with {@code other} and places the result in
     * this region
     * 
     * @param other another {@link Region}
     * @since 1.10
     */
    public void union(Region other) {
        Status status;
        try {
            int result = (int) cairo_region_union.invoke(handle(), other == null ? MemorySegment.NULL : other.handle());
            status = Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
    }

    private static final MethodHandle cairo_region_union = Interop.downcallHandle("cairo_region_union",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Computes the union of this region with {@code rectangle} and places the
     * result in this region
     * 
     * @param rectangle a {@link Rectangle}
     * @since 1.10
     */
    public void union(RectangleInt rectangle) {
        Status status;
        try {
            int result = (int) cairo_region_union_rectangle.invoke(handle(),
                    rectangle == null ? MemorySegment.NULL : rectangle.handle());
            status = Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
    }

    private static final MethodHandle cairo_region_union_rectangle = Interop.downcallHandle(
            "cairo_region_union_rectangle",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Computes the exclusive difference of this region with {@code other} and
     * places the result in this region. That is, this region will be set to contain
     * all areas that are either in this region or in {@code other}, but not in
     * both.
     * 
     * @param other another {@link Region}
     * @since 1.10
     */
    public void xor(Region other) {
        Status status;
        try {
            int result = (int) cairo_region_xor.invoke(handle(), other == null ? MemorySegment.NULL : other.handle());
            status = Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
    }

    private static final MethodHandle cairo_region_xor = Interop.downcallHandle("cairo_region_xor",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Computes the exclusive difference of this region with {@code rectangle} and
     * places the result in this region. That is, this region will be set to contain
     * all areas that are either in this region or in {@code rectangle}, but not in
     * both.
     * 
     * @param rectangle a {@link Rectangle}
     * @since 1.10
     */
    public void xor(RectangleInt rectangle) {
        Status status;
        try {
            int result = (int) cairo_region_xor_rectangle.invoke(handle(),
                    rectangle == null ? MemorySegment.NULL : rectangle.handle());
            status = Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
    }

    private static final MethodHandle cairo_region_xor_rectangle = Interop.downcallHandle("cairo_region_xor_rectangle",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Get the CairoRegion GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_region_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_region_get_type = Interop.downcallHandle(
            "cairo_gobject_region_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
