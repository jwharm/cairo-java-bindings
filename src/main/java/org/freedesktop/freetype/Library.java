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

package org.freedesktop.freetype;

import io.github.jwharm.cairobindings.Proxy;
import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.MemoryCleaner;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Functions to start and end the usage of the FreeType library.
 * <p>
 * Note that {@link #version()} is of limited use because even a new release of
 * FreeType with only documentation changes increases the version number.
 */
public class Library extends Proxy {

    static {
        FreeType2.ensureInitialized();
    }

    /**
     * Constructor used internally to instantiate a java Library object for a native
     * {@code FT_Library} instance
     *
     * @param address the memory address of the native {@code FT_Library} instance
     */
    public Library(MemorySegment address) {
        super(address);
        MemoryCleaner.setFreeFunc(handle(), "FT_Done_FreeType");
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
     * Initialize a new FreeType library object.
     * 
     * @return a new library object
     * @throws UnsupportedOperationException when {@code FT_Init_FreeType} returns a
     *                                       non-zero error code
     */
    public static Library initFreeType() throws UnsupportedOperationException {
        try {
            MemorySegment pointer = SegmentAllocator.nativeAllocator(SegmentScope.auto())
                    .allocate(ValueLayout.ADDRESS.asUnbounded());
            int result = (int) FT_Init_FreeType.invoke(pointer);
            if (result != 0) {
                throw new UnsupportedOperationException(
                        "Error " + result + " occurred during FreeType library initialization");
            }
            Library library = new Library(pointer.get(ValueLayout.ADDRESS, 0));
            MemoryCleaner.takeOwnership(library.handle());
            return library;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle FT_Init_FreeType = Interop.downcallHandle(
            "FT_Init_FreeType", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Return the version of the FreeType library being used.
     * 
     * @return the major, minor and patch version numbers, formatted as
     *         {@code "%d.%d.%d"}
     */
    public String version() {
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment amajor = arena.allocate(ValueLayout.JAVA_INT);
                MemorySegment aminor = arena.allocate(ValueLayout.JAVA_INT);
                MemorySegment apatch = arena.allocate(ValueLayout.JAVA_INT);
                FT_Library_Version.invoke(handle(), amajor, aminor, apatch);
                int major = amajor.get(ValueLayout.JAVA_INT, 0);
                int minor = aminor.get(ValueLayout.JAVA_INT, 0);
                int patch = apatch.get(ValueLayout.JAVA_INT, 0);
                return String.format("%d.%d.%d", major, minor, patch);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle FT_Library_Version = Interop.downcallHandle(
            "FT_Library_Version", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS));
}
