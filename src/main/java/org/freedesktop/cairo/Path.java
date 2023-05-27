package org.freedesktop.cairo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

import io.github.jwharm.cairobindings.ProxyInstance;

/*

Path
====

Suggested addition. API:

public PathIterator iterator();

PathIterator
============

Suggested addition. API:

public boolean hasNext();
public PathElement next();

PathElement
===========

Suggested addition. API:

public PathElementType getType();
public Point getPoint(int index);

PathElementType
===============

Added enumeration, values MOVE_TO, LINE_TO, CURVE_TO, CLOSE_PATH

*/

/**
 * A data structure for holding a path. This data structure serves as the return
 * value for cairo_copy_path() and cairo_copy_path_flat() as well the input
 * value for cairo_append_path().
 * <p>
 * See cairo_path_data_t for hints on how to iterate over the actual data within
 * the path.
 * <p>
 * The num_data member gives the number of elements in the data array. This
 * number is larger than the number of independent path portions (defined in
 * cairo_path_data_type_t), since the data includes both headers and coordinates
 * for each portion.
 * 
 * @since 1.0
 */
public class Path extends ProxyInstance {

	public static MemoryLayout getMemoryLayout() {
		return MemoryLayout.structLayout(
		        ValueLayout.JAVA_INT.withName("status"),
		        MemoryLayout.paddingLayout(32),
		        ValueLayout.ADDRESS.withName("data"),
		        ValueLayout.JAVA_INT.withName("num_data"),
		        MemoryLayout.paddingLayout(32)
		    ).withName("cairo_path_t");
	}

	private static final VarHandle STATUS = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("status"));
	
	/**
	 * Read the current error status from the Path
	 * 
	 * @return the current error status
	 */
	public Status status() {
		int result = (int) STATUS.get(handle());
		return Status.of(result);
	}
	
	/**
	 * Constructor used internally to instantiate a java Path object for a native
	 * {@code cairo_path_t} instance
	 * 
	 * @param address the memory address of the native {@code cairo_path_t} instance
	 */
	public Path(MemorySegment address) {
		super(address);
		setDestroyFunc("cairo_path_destroy");
	}
}
