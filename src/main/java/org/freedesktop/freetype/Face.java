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

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * A handle to a typographic face object. A face object models a given typeface,
 * in a given style.
 */
public class Face extends Proxy {

    static {
        FreeType2.ensureInitialized();
    }

    // The Arena in which the handle to the Face object was allocated.
    private Arena allocator = null;

    /**
     * Constructor used internally to instantiate a java Face object for a native
     * {@code FT_Face} instance
     *
     * @param address the memory address of the native {@code FT_Face} instance
     */
    public Face(MemorySegment address) {
        super(address);
    }

    /**
     * Call {@code FT_Open_Face} to open a font by its pathname.
     * <p>
     * The resulting Face instance must be cleaned up manually with a call to
     * {@link #doneFace()}.
     *
     * @param library      the library resource
     * @param filepathname path to the font file
     * @param faceIndex    See <a href=
     *                     "https://freetype.org/freetype2/docs/reference/ft2-face_creation.html#ft_open_face">FT_Open_Face</a>
     *                     for a detailed description of this parameter.
     * @return the newly created Face instance
     * @throws UnsupportedOperationException when {@code FT_Init_FreeType} returns a
     *                                       non-zero error code
     */
    public static Face newFace(Library library, String filepathname, long faceIndex) {
        Arena allocator = Arena.ofConfined();
        try {
            MemorySegment pointer = allocator.allocate(ValueLayout.ADDRESS.withTargetLayout(ValueLayout.ADDRESS));
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment utf8 = Interop.allocateNativeString(filepathname, arena);
                int result = (int) FT_New_Face.invoke(library.handle(), utf8, faceIndex, pointer);
                if (result != 0) {
                    throw new UnsupportedOperationException(
                            "Error " + result + " occurred during FreeType FT_Face initialization");
                }
                Face face = new Face(pointer.get(ValueLayout.ADDRESS, 0));
                face.allocator = allocator;
                return face;
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle FT_New_Face = Interop.downcallHandle("FT_New_Face",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG,
                    ValueLayout.ADDRESS));

    /**
     * Discard a given face object, as well as all of its child slots and sizes.
     */
    public void doneFace() {
        try {
            int ignored = (int) FT_Done_Face.invoke(handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (allocator != null) {
            allocator.close();
            allocator = null;
        }
    }

    private static final MethodHandle FT_Done_Face = Interop.downcallHandle("FT_Done_Face",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
}
