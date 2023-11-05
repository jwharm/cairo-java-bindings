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

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * SVGVersion is used to describe the version number of the SVG specification
 * that a generated SVG file will conform to.
 * 
 * @since 1.2
 */
public enum SVGVersion {

    /**
     * The version 1.1 of the SVG specification.
     * 
     * @since 1.2
     */
    VERSION_1_1,

    /**
     * The version 1.2 of the SVG specification.
     * 
     * @since 1.2
     */
    VERSION_1_2;

    /**
     * Return the value of this enum
     * @return the value
     */
    public int getValue() {
        return ordinal();
    }

    /**
     * Returns the enum constant for the given ordinal (its position in the enum
     * declaration).
     * 
     * @param ordinal the position in the enum declaration, starting from zero
     * @return the enum constant for the given ordinal
     */
    public static SVGVersion of(int ordinal) {
        return values()[ordinal];
    }

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Used to retrieve the list of supported versions. See
     * {@link SVGSurface#restrictToVersion(SVGVersion)}.
     * 
     * @return supported version list
     * @since 1.2
     */
    public static SVGVersion[] getVersions() {
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment versionsPtr = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment numVersionsPtr = arena.allocate(ValueLayout.JAVA_INT);
                cairo_svg_get_versions.invoke(versionsPtr, numVersionsPtr);
                int numVersions = numVersionsPtr.get(ValueLayout.JAVA_INT, 0);
                int[] versionInts = versionsPtr.reinterpret(ValueLayout.JAVA_INT.byteSize() * numVersions).toArray(ValueLayout.JAVA_INT);
                SVGVersion[] versions = new SVGVersion[numVersions];
                for (int i = 0; i < versionInts.length; i++) {
                    versions[i] = SVGVersion.of(versionInts[i]);
                }
                return versions;
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_svg_get_versions = Interop.downcallHandle("cairo_svg_get_versions",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Get the string representation of this version. This function will return
     * {@code null} if version isn't valid. See {@link #getVersions()} for a way to
     * get the list of valid versions.
     * 
     * @return the string associated this version.
     * @since 1.2
     */
    @Override
    public String toString() {
        try {
            MemorySegment result = (MemorySegment) cairo_svg_version_to_string.invoke(getValue());
            if (MemorySegment.NULL.equals(result)) {
                return null;
            }
            return result.reinterpret(Integer.MAX_VALUE).getUtf8String(0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_svg_version_to_string = Interop.downcallHandle(
            "cairo_svg_version_to_string",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
}
