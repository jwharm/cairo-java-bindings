package org.freedesktop.cairo.surfaces;

import java.lang.foreign.MemorySegment;

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.ProxyInstance;

public class Device extends ProxyInstance {

	{
		Interop.ensureInitialized();
	}

	/**
	 * Constructor used internally to instantiate a java Device object for a native
	 * {@code cairo_device_t} instance
	 * 
	 * @param address the memory address of the native {@code cairo_device_t}
	 *                instance
	 */
	public Device(MemorySegment address) {
		super(address);
		setDereferenceFunc("cairo_device_destroy");
	}

}
