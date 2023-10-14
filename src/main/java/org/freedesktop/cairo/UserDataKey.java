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

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;

/**
 * UserDataKey is used for attaching user data to cairo data structures.
 * <p>
 * The lifetime of a UserDataKey is connected to the lifetime of the Proxy object 
 * that is passed in the {@link #create(Proxy)} method.
 * 
 * @since 1.0
 */
public final class UserDataKey extends Proxy {

    static MemoryLayout getMemoryLayout() {
        return MemoryLayout.structLayout(
                ValueLayout.JAVA_INT.withName("unused")
            ).withName("cairo_user_data_key_t");
    }

    /**
     * Constructor used internally to instantiate a java UserDataKey object for a
     * native {@code UserDataKey} instance
     * 
     * @param address the memory address of the native {@code UserDataKey} instance
     */
    public UserDataKey(MemorySegment address) {
        super(address);
    }

    /**
     * Create a new UserDataKey
     * 
     * @param proxy the ProxyInstace object whose memory scope (lifetime) will be
     *              associated with the returned UserDataKey
     * @return the newly created UserDataKey
     */
    static UserDataKey create(Proxy proxy) {
        return new UserDataKey(SegmentAllocator.nativeAllocator(proxy.handle().scope()).allocate(getMemoryLayout()));
    }
}
