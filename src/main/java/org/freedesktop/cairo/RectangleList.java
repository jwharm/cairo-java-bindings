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
import io.github.jwharm.cairobindings.MemoryCleaner;

import java.lang.foreign.*;
import java.lang.invoke.VarHandle;
import java.util.List;

/**
 * A data structure for holding a dynamically allocated array of rectangles.
 * 
 * @since 1.4
 */
public class RectangleList extends Proxy {

    static {
        Cairo.ensureInitialized();
    }

    /**
     * The memory layout of the native C struct
     * 
     * @return the memory layout of the native C struct
     */
    static MemoryLayout getMemoryLayout() {
        return MemoryLayout.structLayout(
                ValueLayout.JAVA_INT.withName("status"), 
                MemoryLayout.paddingLayout(4),
                ValueLayout.ADDRESS.withName("rectangles"), 
                ValueLayout.JAVA_INT.withName("num_rectangles"), 
                MemoryLayout.paddingLayout(4))
                .withName("cairo_rectangle_list_t");
    }

    private static final VarHandle STATUS = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("status"));
    private static final VarHandle RECTANGLES = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("rectangles"));
    private static final VarHandle NUM_RECTANGLES = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("num_rectangles"));

    /**
     * Read the status field of the RectangleList
     * 
     * @return the status
     */
    public Status status() {
        int result = (int) STATUS.get(handle());
        return Status.of(result);
    }

    /**
     * Read the rectangles field of the RectangleList. The field is an unmodifiable
     * List.
     * 
     * @return the list of rectangles
     */
    public List<Rectangle> rectangles() {
        int length = (int) NUM_RECTANGLES.get(handle());
        long segmentSize = Rectangle.getMemoryLayout().byteSize() * length;
        MemorySegment array = ((MemorySegment) RECTANGLES.get(handle())).reinterpret(segmentSize);
        return array.elements(Rectangle.getMemoryLayout()).map(Rectangle::new).toList();
    }

    /**
     * Constructor used internally to instantiate a java RectangleList object for a
     * native {@code cairo_rectangle_list_t} instance
     * 
     * @param address the memory address of the native
     *                {@code cairo_rectangle_list_t} instance
     */
    public RectangleList(MemorySegment address) {
        super(address.reinterpret(getMemoryLayout().byteSize()));
        MemoryCleaner.setFreeFunc(handle(), "cairo_rectangle_list_destroy");
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
     * A data structure for holding a dynamically allocated array of rectangles.
     *
     * @param  arena      the arena in which memory for the Rectangle is allocated
     * @param  status     error status of the rectangle list
     * @param  rectangles list containing the rectangles
     * @return the newly created RectangleList
     */
    public static RectangleList create(Arena arena, Status status, List<Rectangle> rectangles) {
        RectangleList rectangleList = new RectangleList(arena.allocate(getMemoryLayout()));
        STATUS.set(rectangleList.handle(), status.getValue());
        NUM_RECTANGLES.set(rectangleList.handle(), rectangles == null ? 0 : rectangles.size());
        if (rectangles == null || rectangles.isEmpty()) {
            return rectangleList;
        }
        MemorySegment array = arena.allocate(Rectangle.getMemoryLayout(), rectangles.size());
        for (int i = 0; i < rectangles.size(); i++) {
            MemorySegment src = rectangles.get(i).handle().reinterpret(Rectangle.getMemoryLayout().byteSize(), arena, null);
            MemorySegment dst = array.asSlice(i * Rectangle.getMemoryLayout().byteSize());
            dst.copyFrom(src);
        }
        return rectangleList;
    }
}
