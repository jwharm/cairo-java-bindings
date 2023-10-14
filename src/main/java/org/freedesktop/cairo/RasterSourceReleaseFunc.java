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

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * RasterSourceReleaseFunc is the type of function which is called when the
 * pixel data is no longer being access by the pattern for the rendering
 * operation. Typically this function will simply destroy the surface created
 * during acquire.
 * 
 * @since 1.12
 */
@FunctionalInterface
public interface RasterSourceReleaseFunc {

    /**
     * Called when the pixel data is no longer being access by the pattern for the
     * rendering operation. Typically this function will simply destroy the surface
     * created during acquire.
     * 
     * @param pattern the pattern being rendered from
     * @param target  the surface created during acquire
     * @since 1.12
     */
    void release(RasterSource pattern, Surface target);

    /**
     * The callback that is executed by native code. This method marshals the
     * parameters and calls {@link #release(RasterSource, Surface)}.
     * 
     * @param pattern      the pattern being rendered from
     * @param callbackData ignored
     * @param target       the surface created during acquire
     * @since 1.12
     */
    default void upcall(MemorySegment pattern, MemorySegment callbackData, MemorySegment target) {
        release(new RasterSource(pattern), new Surface(target));
    }

    /**
     * Generates an upcall stub, a C function pointer that will call
     * {@link #upcall(MemorySegment, MemorySegment, MemorySegment)}.
     * 
     * @param scope the scope in which the upcall stub will be allocated
     * @return the function pointer of the upcall stub
     * @since 1.12
     */
    default MemorySegment toCallback(SegmentScope scope) {
        try {
            FunctionDescriptor fdesc = FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS);
            MethodHandle handle = MethodHandles.lookup().findVirtual(RasterSourceReleaseFunc.class, "upcall",
                    fdesc.toMethodType());
            return Linker.nativeLinker().upcallStub(handle.bindTo(this), fdesc, scope);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
