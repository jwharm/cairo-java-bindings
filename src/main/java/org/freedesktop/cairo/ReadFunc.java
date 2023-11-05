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

import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * ReadFunc is the type of function which is called when a backend needs to read
 * data from an input stream. It is passed the closure which was specified by
 * the user at the time the read function was registered, the buffer to read the
 * data into and the length of the data in bytes. The read function should throw
 * {@link IOException} if all the data was not successfully read.
 * 
 * @since 1.0
 */
@FunctionalInterface
public interface ReadFunc {

    /**
     * The function to implement as callback in a read operation from an input
     * stream.
     * 
     * @param length the amount of data to read
     * @return data the data read from the input stream
     * @throws IOException to be thrown when an error occurs during the read
     *                     operation
     * @since 1.0
     */
    byte[] read(int length) throws IOException;

    /**
     * The callback that is executed by native code. This method marshals the
     * parameters and calls {@link #read(int)}.
     * 
     * @param closure ignored
     * @param data    the buffer into which to read the data
     * @param length  the amount of data to read
     * @return {@link Status#SUCCESS} on success, or {@link Status#READ_ERROR} if an
     *         IOException occured or 0 bytes (or null) was returned from
     *         {@code read()}.
     * @since 1.0
     */
    default int upcall(MemorySegment closure, MemorySegment data, int length) {
        if (length <= 0) {
            return Status.SUCCESS.getValue();
        }
        try {
            byte[] bytes = read(length);
            if (bytes == null || bytes.length == 0) {
                return Status.READ_ERROR.getValue();
            }
            data.reinterpret(length).asByteBuffer().put(bytes);
            return Status.SUCCESS.getValue();
        } catch (IOException ioe) {
            return Status.READ_ERROR.getValue();
        }
    }

    /**
     * Generates an upcall stub, a C function pointer that will call
     * {@link #upcall}.
     *
     * @param arena the arena in which the upcall stub will be allocated
     * @return the function pointer of the upcall stub
     * @since 1.0
     */
    default MemorySegment toCallback(Arena arena) {
        try {
            FunctionDescriptor fdesc = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT);
            MethodHandle handle = MethodHandles.lookup().findVirtual(ReadFunc.class, "upcall", fdesc.toMethodType());
            return Linker.nativeLinker().upcallStub(handle.bindTo(this), fdesc, arena);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
