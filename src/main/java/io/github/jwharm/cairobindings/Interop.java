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

package io.github.jwharm.cairobindings;

import java.lang.foreign.*;
import java.lang.invoke.*;

/**
 * The Interop class contains functionality for interoperability with native code.
 */
public final class Interop {

    private final static SymbolLookup symbolLookup;
    private final static Linker linker = Linker.nativeLinker();

    static {
        SymbolLookup loaderLookup = SymbolLookup.loaderLookup();
        symbolLookup = name -> loaderLookup.find(name).or(() -> linker.defaultLookup().find(name));
    }

    // Prevent instantiation
    private Interop() {}

    /**
     * Reinterpret a zero-length MemorySegment to the size of
     * {@code layout}
     *
     * @param address the address with {@code layout} memory layout
     * @param layout the actual memory layout
     * @return a MemorySegment with the size of {@code layout}
     */
    public static MemorySegment reinterpret(MemorySegment address, MemoryLayout layout) {
        if (address.byteSize() == layout.byteSize())
            return address;
        return MemorySegment.ofAddress(address.address(), layout.byteSize());
    }

    /**
     * Reinterpret a zero-length MemorySegment to the requested size
     *
     * @param address the address of a memory segment with the requested size
     * @param size the requested size
     * @return the memory segment with the requested size
     */
    public static MemorySegment reinterpret(MemorySegment address, long size) {
        if (address.byteSize() == size)
            return address;
        return MemorySegment.ofAddress(address.address(), size);
    }

    /**
     * Creates a method handle that is used to call the native function with
     * the provided name and function descriptor. The method handle is cached
     * and reused in subsequent lookups.
     * @param name Name of the native function
     * @param fdesc Function descriptor of the native function
     * @return the MethodHandle
     */
    public static MethodHandle downcallHandle(String name, FunctionDescriptor fdesc) {
        return symbolLookup
                .find(name)
                .map(addr -> linker.downcallHandle(addr, fdesc))
                .orElse(null);
    }

    /**
     * Allocate a native string using SegmentAllocator.allocateUtf8String(String).
     * @param string the string to allocate as a native string (utf8 char*)
     * @param allocator the segment allocator to use
     * @return the allocated MemorySegment
     */
    public static MemorySegment allocateNativeString(String string, SegmentAllocator allocator) {
        return string == null ? MemorySegment.NULL : allocator.allocateUtf8String(string);
    }
}
