package org.freedesktop.cairo.fonts;

import java.lang.foreign.MemorySegment;

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.ProxyInstance;

public class FontOptions extends ProxyInstance {

	{
		Interop.ensureInitialized();
	}

	/**
	 * Constructor used internally to instantiate a java FontOptions object for a
	 * native {@code cairo_font_options_t} instance
	 * 
	 * @param address the memory address of the native {@code cairo_font_options_t}
	 *                instance
	 */
	public FontOptions(MemorySegment address) {
		super(address);
		setDereferenceFunc("cairo_font_options_destroy");
	}

}
