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

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * RasterSourceCopyFunc is the type of function which is called when the pattern
 * gets copied as a normal part of rendering.
 * 
 * @since 1.12
 */
@FunctionalInterface
public interface RasterSourceCopyFunc {

    /**
     * Called when the pattern gets copied as a normal part of rendering.
     * 
     * @param pattern the pattern that was copied to
     * @param other   the pattern being used as the source for the copy
     * @return {@link Status#SUCCESS} on success, or one of the {@link Status} error
     *         codes for failure.
     * @since 1.12
     */
    Status copy(RasterSource pattern, RasterSource other);

    /**
     * The callback that is executed by native code. This method marshals the
     * parameters and calls {@link #copy(RasterSource, RasterSource)}.
     * 
     * @param pattern      the pattern being rendered from
     * @param callbackData ignored
     * @param other        the pattern being used as the source for the copy
     * @return {@link Status#SUCCESS} on success, or one of the {@link Status} error
     *         codes for failure.
     * @since 1.12
     */
    default int upcall(MemorySegment pattern, MemorySegment callbackData, MemorySegment other) {
        Status result = copy(new RasterSource(pattern), new RasterSource(other));
        /*
         * Will throw a NPE if the callback function returns null. This is deliberate:
         * The snapshot function must always return a Status enum member; failing to do
         * so is a programming error that should fail fast and obvious.
         */
        return result.getValue();
    }

    /**
     * Generates an upcall stub, a C function pointer that will call
     * {@link #upcall}.
     *
     * @param arena the arena in which the upcall stub will be allocated
     * @return the function pointer of the upcall stub
     * @since 1.12
     */
    default MemorySegment toCallback(Arena arena) {
        try {
            FunctionDescriptor fdesc = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS);
            MethodHandle handle = MethodHandles.lookup().findVirtual(RasterSourceCopyFunc.class, "upcall",
                    fdesc.toMethodType());
            return Linker.nativeLinker().upcallStub(handle.bindTo(this), fdesc, arena);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
