/* cairo-java-bindings - Java language bindings for cairo
 * Copyright (C) 2024 Jan-Willem Harmannij
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

/**
 * This module contains Java language bindings for the <a href="https://www.cairographics.org">cairo</a>
 * graphics library using the JEP-454 Panama FFI. The bindings are based on <strong>cairo 1.18</strong>
 * and work with <strong>JDK 22</strong> or later.
 */
module org.freedesktop.cairo {
    requires static org.gnome.gobject; // Optional dependency on java-gi when cairo-gobject is used
    exports org.freedesktop.cairo;
    exports org.freedesktop.freetype;
    exports io.github.jwharm.cairobindings;
}
