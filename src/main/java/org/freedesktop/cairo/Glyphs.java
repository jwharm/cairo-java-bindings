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

import io.github.jwharm.cairobindings.MemoryCleaner;
import io.github.jwharm.cairobindings.Proxy;

import java.lang.foreign.MemorySegment;

/**
 * The {@code Glyphs} class represents an array of glyphs. It will also optionally
 * contain an array of clusters and the accompanying {@link TextClusterFlags}.
 */
public class Glyphs implements AutoCloseable {

    private final Proxy glyphsPointer;
    private final Proxy clustersPointer;
    private final int numGlyphs;
    private final int numClusters;
    private final TextClusterFlags clusterFlags;

    /**
     * Constructor used internally to instantiate a java Glyphs object for a native
     * array of glyphs and clusters
     *
     * @param glyphsPtr   the memory address of the native {@code cairo_glyph_t}
     *                    array
     * @param numGlyphs   the number of glyphs
     * @param clustersPtr the memory address of the native
     *                    {@code cairo_text_cluster_t} array
     * @param numClusters the number of clusters
     * @param clusterFlags the text cluster flags
     */
    Glyphs(MemorySegment glyphsPtr, int numGlyphs, MemorySegment clustersPtr, int numClusters, TextClusterFlags clusterFlags) {
        this.glyphsPointer = new Proxy(glyphsPtr);
        this.numGlyphs = numGlyphs;
        this.clustersPointer = new Proxy(clustersPtr);
        this.numClusters = numClusters;
        this.clusterFlags = clusterFlags;
        MemoryCleaner.setFreeFunc(this.glyphsPointer.handle(), "cairo_glyph_free");
        MemoryCleaner.setFreeFunc(this.clustersPointer.handle(), "cairo_text_cluster_free");
        MemoryCleaner.takeOwnership(this.glyphsPointer.handle());
        MemoryCleaner.takeOwnership(this.clustersPointer.handle());
    }

    /**
     * Get a pointer to the glyphs array
     * @return a zero-length MemorySegment pointing to the glyphs array
     */
    public MemorySegment getGlyphsPointer() {
        return glyphsPointer.handle();
    }

    /**
     * Get the number of glyphs in the array
     * @return the number of glyphs
     */
    public int getNumGlyphs() {
        return numGlyphs;
    }

    /**
     * Get a pointer to the clusters array
     * @return a zero-length MemorySegment pointing to the clusters array
     */
    public MemorySegment getClustersPointer() {
        return clustersPointer.handle();
    }

    /**
     * Get the number of text clusters in the cluster array
     * @return the number of text clusters
     */
    public int getNumClusters() {
        return numClusters;
    }

    /**
     * Get the text cluster flags
     * @return the text cluster flags
     */
    public TextClusterFlags getClusterFlags() {
        return clusterFlags;
    }

    /**
     * Free the glyphs array using {@code cairo_glyph_free} and the clusters array
     * using {@code cairo_text_cluster_free}.
     */
    @Override
    public void close() {
        MemoryCleaner.free(glyphsPointer.handle());
        MemoryCleaner.free(clustersPointer.handle());
    }
}
