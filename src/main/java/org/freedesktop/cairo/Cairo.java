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

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.LibLoad;
import io.github.jwharm.cairobindings.Platform;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * This class contains global declarations that do not belong in a specific
 * cairo class definition.
 */
public final class Cairo {

    static {
        switch (Platform.getRuntimePlatform()) {
            case "linux" -> LibLoad.loadLibrary("libcairo.so.2");
            case "windows" -> LibLoad.loadLibrary("libcairo-2.dll");
            case "macos" -> LibLoad.loadLibrary("libcairo.2.dylib");
        }
        try {
            switch (Platform.getRuntimePlatform()) {
                case "linux" -> LibLoad.loadLibrary("libcairo-gobject.so.2");
                case "windows" -> LibLoad.loadLibrary("libcairo-gobject-2.dll");
                case "macos" -> LibLoad.loadLibrary("libcairo-gobject.2.dylib");
            }
        } catch (Throwable ignored) {
            // libcairo-gobject is an optional dependency
        }
    }

    /**
     * Ensures the class initializer has loaded the cairo library.
     */
    public static void ensureInitialized() {
    }

    // Prohibit instantiation
    private Cairo() {
    }

    /**
     * encodes the given cairo version into an integer. The numbers returned by
     * {@link #version()} are encoded in the same way. Two encoded
     * version numbers can be compared as integers. The encoding ensures that later
     * versions compare greater than earlier versions.
     * 
     * @param major the major component of the version number
     * @param minor the minor component of the version number
     * @param micro the micro component of the version number
     * @return the encoded version.
     * @since 1.0
     */
    public static int versionEncode(int major, int minor, int micro) {
        return (major * 10000) + (minor * 100) + micro;
    }

    /**
     * Encodes the given cairo version into a string. The numbers returned by
     * {@link #versionString()} are encoded in the same way.
     * 
     * @param major the major component of the version number
     * @param minor the minor component of the version number
     * @param micro the micro component of the version number
     * @return a string literal containing the version.
     * @since 1.8
     */
    public static String versionStringize(int major, int minor, int micro) {
        return major + "." + minor + "." + micro;
    }

    /**
     * Returns the version of the cairo library encoded in a single integer. The
     * encoding ensures that later versions compare greater than earlier versions.
     * <p>
     * A run-time comparison to check that cairo's version is greater than or equal
     * to version X.Y.Z could be performed as follows:
     * 
     * <pre>
     * if (Cairo.version() >= versionEncode(X,Y,Z)) {...}
     * </pre>
     * 
     * @return the encoded version.
     * @see #versionString()
     * @since 1.0
     */
    public static int version() {
        try {
            return (int) cairo_version.invoke();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_version = Interop.downcallHandle("cairo_version",
            FunctionDescriptor.of(ValueLayout.JAVA_INT));

    /**
     * Returns the version of the cairo library as a human-readable string of the
     * form "X.Y.Z".
     * <p>
     * Cairo has a three-part version number scheme. In this scheme, we use even vs.
     * odd numbers to distinguish fixed points in the software vs. in-progress
     * development, (such as from git instead of a tar file, or as a "snapshot" tar
     * file as opposed to a "release" tar file).
     * 
     * <pre>
     * 	 _____ Major. Always 1, until we invent a new scheme.
     * 	/  ___ Minor. Even/Odd = Release/Snapshot (tar files) or Branch/Head (git)
     * 	| /  _ Micro. Even/Odd = Tar-file/git
     * 	| | /
     * 	1.0.0
     * </pre>
     * 
     * Here are a few examples of versions that one might see.
     * 
     * <pre>
     * 	Releases
     * 	--------
     * 	1.0.0 - A major release
     * 	1.0.2 - A subsequent maintenance release
     * 	1.2.0 - Another major release
     * 	 
     * 	Snapshots
     * 	---------
     * 	1.1.2 - A snapshot (working toward the 1.2.0 release)
     * 	 
     * 	In-progress development (eg. from git)
     * 	--------------------------------------
     * 	1.0.1 - Development on a maintenance branch (toward 1.0.2 release)
     * 	1.1.1 - Development on head (toward 1.1.2 snapshot and 1.2.0 release)
     * </pre>
     * 
     * <strong>Compatibility</strong>
     * <p>
     * The API/ABI compatibility guarantees for various versions are as follows.
     * First, let's assume some cairo-using application code that is successfully
     * using the API/ABI "from" one version of cairo. Then let's ask the question
     * whether this same code can be moved "to" the API/ABI of another version of
     * cairo. Moving from a release to any later version (release, snapshot,
     * development) is always guaranteed to provide compatibility. Moving from a
     * snapshot to any later version is not guaranteed to provide compatibility,
     * since snapshots may introduce new API that ends up being removed before the
     * next release. Moving from an in-development version (odd micro component) to
     * any later version is not guaranteed to provide compatibility. In fact,
     * there's not even a guarantee that the code will even continue to work with
     * the same in-development version number. This is because these numbers don't
     * correspond to any fixed state of the software, but rather the many states
     * between snapshots and releases.
     * 
     * @return a string containing the version.
     * @since 1.0
     */
    public static String versionString() {
        try {
            MemorySegment result = (MemorySegment) cairo_version_string.invoke();
            if (MemorySegment.NULL.equals(result)) {
                return null;
            }
            return result.reinterpret(Integer.MAX_VALUE).getUtf8String(0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_version_string = Interop.downcallHandle("cairo_version_string",
            FunctionDescriptor.of(ValueLayout.ADDRESS));
}
