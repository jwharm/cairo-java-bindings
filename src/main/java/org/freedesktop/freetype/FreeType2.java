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

import io.github.jwharm.cairobindings.LibLoad;
import io.github.jwharm.cairobindings.Platform;

/**
 * This class contains global declarations that do not belong in a specific
 * FreeType class definition.
 */
public class FreeType2 {

    static {
        switch (Platform.getRuntimePlatform()) {
            case "linux" -> LibLoad.loadLibrary("libfreetype.so.6");
            case "windows" -> LibLoad.loadLibrary("libfreetype-6.dll");
            case "macos" -> LibLoad.loadLibrary("libfreetype.6.dylib");
        }
    }

    /**
     * Ensures the class initializer has loaded the freetype library.
     */
    public static void ensureInitialized() {
    }

    // Prohibit instantiation
    private FreeType2() {
    }
}
