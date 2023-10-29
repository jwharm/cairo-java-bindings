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
import io.github.jwharm.cairobindings.MemoryCleaner;
import io.github.jwharm.cairobindings.Proxy;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * The {@code Glyphs} class represents an array of glyphs. It will also optionally
 * contain an array of clusters and the accompanying {@link TextClusterFlags}.
 */
public class Glyphs implements AutoCloseable {

    private Proxy glyphsPointer;
    private Proxy clustersPointer;
    private int numGlyphs;
    private int numClusters;
    private TextClusterFlags clusterFlags;

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
        MemoryCleaner.setFreeFunc(glyphsPtr, "cairo_glyph_free");
        MemoryCleaner.setFreeFunc(clustersPtr, "cairo_text_cluster_free");
        MemoryCleaner.takeOwnership(glyphsPtr);
        MemoryCleaner.takeOwnership(clustersPtr);
    }

    /**
     * Get a pointer to the glyphs array
     * @return a zero-length MemorySegment pointing to the glyphs array
     */
    public MemorySegment getGlyphsPointer() {
        return glyphsPointer.handle();
    }

    /**
     * Set a pointer to the glyphs array
     * @param glyphsPointer a MemorySegment pointing to the glyphs array
     */
    public void setGlyphsPointer(MemorySegment glyphsPointer) {
        MemoryCleaner.free(this.glyphsPointer.handle());
        this.glyphsPointer = new Proxy(glyphsPointer);
        MemoryCleaner.setFreeFunc(glyphsPointer, "cairo_glyph_free");
        MemoryCleaner.takeOwnership(glyphsPointer);
    }

    /**
     * Get the number of glyphs in the array
     * @return the number of glyphs
     */
    public int getNumGlyphs() {
        return numGlyphs;
    }

    /**
     * Set the number of glyphs in the array
     * @param numGlyphs the number of glyphs
     */
    public void setNumGlyphs(int numGlyphs) {
        this.numGlyphs = numGlyphs;
    }

    /**
     * Get a pointer to the clusters array
     * @return a zero-length MemorySegment pointing to the clusters array
     */
    public MemorySegment getClustersPointer() {
        return clustersPointer.handle();
    }

    /**
     * Set a pointer to the clusters array
     * @param clustersPointer the MemorySegment pointing to the clusters array
     */
    public void setClustersPointer(MemorySegment clustersPointer) {
        MemoryCleaner.free(this.clustersPointer.handle());
        this.clustersPointer = new Proxy(clustersPointer);
        MemoryCleaner.setFreeFunc(clustersPointer, "cairo_text_cluster_free");
        MemoryCleaner.takeOwnership(clustersPointer);
    }

    /**
     * Get the number of text clusters in the cluster array
     * @return the number of text clusters
     */
    public int getNumClusters() {
        return numClusters;
    }

    /**
     * Set the number of text clusters in the cluster array
     * @param numClusters the number of text clusters
     */
    public void setNumClusters(int numClusters) {
        this.numClusters = numClusters;
    }

    /**
     * Get the text cluster flags
     * @return the text cluster flags
     */
    public TextClusterFlags getClusterFlags() {
        return clusterFlags;
    }

    /**
     * Set the text cluster flags
     * @param clusterFlags the text cluster flags
     */
    public void setClusterFlags(TextClusterFlags clusterFlags) {
        this.clusterFlags = clusterFlags;
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

    /**
     * Allocates an array of {@link Glyph}s. This function is only useful in
     * implementations of {@link UserScaledFontTextToGlyphsFunc} where the user
     * needs to allocate an array of glyphs that cairo will free. For all other
     * uses, user can use their own allocation method for glyphs.
     * <p>
     * This function returns {@link MemorySegment#NULL} if {@code numGlyphs} is not
     * positive, or if out of memory. That means, the {@code NULL} return value
     * signals out-of-memory only if {@code numGlyphs} was positive.
     *
     * @param numGlyphs number of glyphs to allocate
     * @return the newly allocated array of glyphs
     * @since 1.8
     */
    public static MemorySegment allocateGlyphs(int numGlyphs) {
        try {
            return (MemorySegment) cairo_glyph_allocate.invoke(numGlyphs);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_glyph_allocate = Interop.downcallHandle(
            "cairo_glyph_allocate", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Allocates an array of {@link TextCluster}s. This function is only useful in
     * implementations of {@link UserScaledFontTextToGlyphsFunc} where the user
     * needs to allocate an array of text clusters that cairo will free. For all
     * other uses, user can use their own allocation method for text clusters.
     * <p>
     * This function returns {@link MemorySegment#NULL} if {@code numClusters} is
     * not positive, or if out of memory. That means, the {@code NULL} return value
     * signals out-of-memory only if {@code numClusters} was positive.
     *
     * @param numClusters number of TextClusters to allocate
     * @return the newly allocated array of text clusters
     * @since 1.8
     */
    public static MemorySegment allocateClusters(int numClusters) {
        try {
            return (MemorySegment) cairo_text_cluster_allocate.invoke(numClusters);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_text_cluster_allocate = Interop.downcallHandle(
            "cairo_text_cluster_allocate", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
}
