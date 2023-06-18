package org.freedesktop.cairo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

import io.github.jwharm.cairobindings.ProxyInstance;

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
public class TextCluster extends ProxyInstance {

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
