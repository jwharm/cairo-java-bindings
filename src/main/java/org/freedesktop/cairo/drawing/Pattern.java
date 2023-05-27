package org.freedesktop.cairo.drawing;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.ProxyInstance;

public class Pattern extends ProxyInstance {

	{
		Interop.ensureInitialized();
	}

	public static MemoryLayout getMemoryLayout() {
		return null;
	}

	/**
	 * Constructor used internally to instantiate a java Pattern object for a native
	 * {@code cairo_pattern_t} instance
	 * 
	 * @param address the memory address of the native {@code cairo_pattern_t}
	 *                instance
	 */
	public Pattern(MemorySegment address) {
		super(address);
	}

}
