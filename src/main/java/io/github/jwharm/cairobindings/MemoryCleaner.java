/* cairo-java-bindings - Java language bindings for cairo
 * Copyright (C) 2023-2025 Jan-Willem Harmannij
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

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.ref.Cleaner;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class keeps a cache of all memory addresses for which a Proxy object was created.
 * <p>
 * When a new Proxy object is created, a {@link Cleaner} is attached to it. When the Proxy
 * object is garbage-collected, the memory is released using the specified free-func.
 * <p>
 * When ownership of a memory address belongs elsewhere, the cleaner must not free the
 * memory. Ownership is enabled/disabled with {@link #takeOwnership(MemorySegment)} and
 * {@link #yieldOwnership(MemorySegment)}.
 */
public final class MemoryCleaner {

    private static final Cleaner CLEANER = Cleaner.create();
    private static final Map<MemorySegment, Cached> cache = new ConcurrentHashMap<>();

    // Prevent instantiation
    private MemoryCleaner() {}

    /**
     * Register the memory address of this proxy to be cleaned when the proxy
     * gets garbage-collected.
     *
     * @param proxy The proxy instance
     */
    public static void register(Proxy proxy) {
        cache.computeIfAbsent(proxy.handle(), address -> {
            var finalizer = new StructFinalizer(address);
            var cleanable = CLEANER.register(proxy, finalizer);
            return new Cached(false, null, cleanable);
        });
    }

    /**
     * Register a specialized cleanup function for this memory address.
     *
     * @param address the memory address
     * @param freeFunc the specialized cleanup function to call
     */
    public static void setFreeFunc(MemorySegment address, String freeFunc) {
        cache.computeIfPresent(address, (_, cached) ->
                new Cached(cached.owned, freeFunc, cached.cleanable));
    }

    /**
     * Take ownership of this memory address: when all proxy objects are garbage-collected,
     * the memory will automatically be released.
     *
     * @param address the memory address
     */
    public static void takeOwnership(MemorySegment address) {
        cache.computeIfPresent(address, (_, cached) ->
                new Cached(true, cached.freeFunc, cached.cleanable));
    }

    /**
     * Yield ownership of this memory address: when all proxy objects are garbage-collected,
     * the memory will not be released.
     *
     * @param address the memory address
     */
    public static void yieldOwnership(MemorySegment address) {
        cache.computeIfPresent(address, (_, cached) ->
                new Cached(false, cached.freeFunc, cached.cleanable));
    }

    /**
     * Run the {@link StructFinalizer} associated with this memory address, by invoking
     * {@link Cleaner.Cleanable#clean()}.
     *
     * @param address the memory address to free
     */
    public static void free(MemorySegment address) {
        var cached = cache.get(address);
        if (cached != null) {
            cached.cleanable.clean();
        }
    }

    /**
     * This record type is cached for each memory address.
     *
     * @param owned whether this address is owned (should be cleaned)
     * @param freeFunc an (optional) specialized function that will release the native memory
     * @param cleanable an action to free the instance
     */
    private record Cached(boolean owned, String freeFunc, Cleaner.Cleanable cleanable) {}

    /**
     * This callback is run by the {@link Cleaner} when a struct or union instance has become
     * unreachable, to free the native memory.
     *
     * @param address the memory address to free
     */
    private record StructFinalizer(MemorySegment address) implements Runnable {

        /**
         * This method is run by the {@link Cleaner} when the last Proxy object for this
         * memory address is garbage-collected.
         */
        public void run() {
            Cached cached = cache.remove(address);
            if (cached == null) {
                return;
            }

            if (!cached.owned) {
                return;
            }

            if (cached.freeFunc == null) {
                return;
            }

            if (!address.scope().isAlive()) {
                return;
            }

            // run the free-function
            try {
                Interop.downcallHandle(
                        cached.freeFunc,
                        FunctionDescriptor.ofVoid(ValueLayout.ADDRESS)
                ).invokeExact(address);
            } catch (Throwable e) {
                throw new AssertionError("Unexpected exception occurred: ", e);
            }
        }
    }
}
