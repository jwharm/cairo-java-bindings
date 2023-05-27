package org.freedesktop.cairo.drawing;

import java.lang.foreign.MemorySegment;

import io.github.jwharm.cairobindings.ProxyInstance;

public class Glyph extends ProxyInstance {

	/**
	 * Constructor used internally to instantiate a java Glyph object for a native
	 * {@code cairo_glyph_t} instance
	 * 
	 * @param address the memory address of the native {@code cairo_glyph_t}
	 *                instance
	 */
	public Glyph(MemorySegment address) {
		super(address);
	}
}
