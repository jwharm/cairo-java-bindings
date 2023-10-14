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

import java.lang.foreign.MemorySegment;

/**
 * The script surface provides the ability to render to a native script that
 * matches the cairo drawing model. The scripts can be replayed using tools
 * under the util/cairo-script directory, or with cairo-perf-trace.
 * 
 * @see Surface
 * @see Script
 * @since 1.12
 */
public final class ScriptSurface extends Surface {

    static {
        Cairo.ensureInitialized();
    }

    /*
     * Keep a reference to the Script and the wrapped Surface instances during the
     * lifetime of the ScriptSurface.
     */
    Script script;
    Surface target;

    /**
     * Constructor used internally to instantiate a java ScriptSurface object for a
     * native {@code cairo_surface_t} instance
     * 
     * @param address the memory address of the native {@code cairo_surface_t}
     *                instance
     */
    public ScriptSurface(MemorySegment address) {
        super(address);
    }
}
