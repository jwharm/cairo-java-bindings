package org.freedesktop.cairo;

import java.lang.foreign.MemorySegment;

import io.github.jwharm.cairobindings.Interop;

/**
 * The script surface provides the ability to render to a native script that
 * matches the cairo drawing model. The scripts can be replayed using tools
 * under the util/cairo-script directory, or with cairo-perf-trace.
 * 
 * @see Surface
 * @see Script
 * @since 1.12
 */
public final class ScriptSurface extends Surface {

	{
		Interop.ensureInitialized();
	}

	/*
	 * Keep a reference to the Script and the wrapped Surface instances during the
	 * lifetime of the ScriptSurface.
	 */
	Script script;
	Surface target;

	/**
	 * Constructor used internally to instantiate a java ScriptSurface object for a
	 * native {@code cairo_surface_t} instance
	 * 
	 * @param address the memory address of the native {@code cairo_surface_t}
	 *                instance
	 */
	public ScriptSurface(MemorySegment address) {
		super(address);
	}
}
