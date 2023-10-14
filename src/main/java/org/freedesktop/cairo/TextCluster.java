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
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

/**
 * The TextCluster structure holds information about a single <i>text
 * cluster</i>. A text cluster is a minimal mapping of some glyphs corresponding
 * to some UTF-8 text.
 * <p>
 * For a cluster to be valid, both {@code numBytes} and {@code numGlyphs} should
 * be non-negative, and at least one should be non-zero. Note that clusters with
 * zero glyphs are not as well supported as normal clusters. For example, PDF
 * rendering applications typically ignore those clusters when PDF text is being
 * selected.
 * <p>
 * See cairo_show_text_glyphs() for how clusters are used in advanced text
 * operations.
 * 
 * @since 1.8
 */
public class TextCluster extends Proxy {

    /**
     * The memory layout of the native C struct
     * 
     * @return the memory layout of the native C struct
     */
    static MemoryLayout getMemoryLayout() {
        return MemoryLayout.structLayout(
                ValueLayout.JAVA_INT.withName("num_bytes"), 
                ValueLayout.JAVA_INT.withName("num_glyphs"))
            .withName("cairo_text_cluster_t");
    }

    private static final VarHandle NUM_BYTES = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("num_bytes"));
    private static final VarHandle NUM_GLYPHS = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("num_glyphs"));

    /**
     * The number of bytes of UTF-8 text covered by the cluster
     * 
     * @return the number of bytes of UTF-8 text
     */
    public int numBytes() {
        return (int) NUM_BYTES.get(handle());
    }

    /**
     * The number of glyphs covered by cluster
     * 
     * @return the number of glyphs covered by cluster
     */
    public int numGlyphs() {
        return (int) NUM_GLYPHS.get(handle());
    }

    /**
     * Constructor used internally to instantiate a java TextCluster object for a
     * native {@code cairo_text_cluster_t} instance
     * 
     * @param address the memory address of the native {@code cairo_text_cluster_t}
     *                instance
     */
    public TextCluster(MemorySegment address) {
        super(address);
    }
}
