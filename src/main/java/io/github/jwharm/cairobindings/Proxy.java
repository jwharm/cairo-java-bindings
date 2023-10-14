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

import java.lang.foreign.MemorySegment;

/**
 * Base type for a Java proxy object to an instance in native memory.
 */
public class Proxy {

    private final MemorySegment address;

    /**
     * Create a new {@code Proxy} object for an instance in native memory.
     * @param address the memory address of the instance
     */
    public Proxy(MemorySegment address) {
        this.address = address;
        MemoryCleaner.register(this);
    }

    /**
     * Get the memory address of the instance
     * @return the memory address of the instance
     */
    public MemorySegment handle() {
        return address;
    }

    /**
     * Returns the hashcode of the memory address
     * @return the hashcode of the memory address
     * @see MemorySegment#hashCode()
     */
    public int hashCode() {
        return address.hashCode();
    }

    /**
     * Checks whether the other object is a Proxy instance and the memory
     * addresses are equal.
     * @param obj another object
     * @return true when the other object is a Proxy instance and the memory
     *         addresses are equal, otherwise false.
     * @see MemorySegment#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Proxy other
                && address.equals(other.address);
    }
}
