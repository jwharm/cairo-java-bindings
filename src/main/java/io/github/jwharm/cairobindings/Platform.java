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

/**
 * The Platform class provides utility functions to retrieve the runtime platform and
 * and check if a function is supported on the runtime platform.
 */
public final class Platform {

    private static String runtimePlatform = null;

    // Prevent instantiation
    private Platform() {}

    /**
     * Determine the runtime platform
     * @return the runtime platform: "windows", "linux" or "macos"
     */
    public static String getRuntimePlatform() {
        if (runtimePlatform == null) {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("windows")) {
                runtimePlatform = "windows";
            } else if (osName.contains("linux")) {
                runtimePlatform = "linux";
            } else {
                runtimePlatform = "macos";
            }
        }
        return runtimePlatform;
    }
}
