package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.MemoryCleaner;
import io.github.jwharm.cairobindings.Proxy;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;

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
     * Represents an empty glyphs array
     */
    public static Glyphs empty() {
        var glyphsPtr = MemorySegment.allocateNative(ValueLayout.ADDRESS, SegmentScope.auto());
        return new Glyphs(glyphsPtr, 0, MemorySegment.NULL, 0, null);
    }

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
