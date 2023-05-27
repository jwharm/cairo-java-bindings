package org.freedesktop.cairo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

import io.github.jwharm.cairobindings.ProxyInstance;

public class PathData extends ProxyInstance {

	/**
	 * The memory layout of the native C struct
	 * 
	 * @return the memory layout of the native C struct
	 */
	public static MemoryLayout getMemoryLayout() {
	    return MemoryLayout.unionLayout(
	            MemoryLayout.structLayout(
	                ValueLayout.JAVA_INT.withName("type"),
	                ValueLayout.JAVA_INT.withName("length")
	            ).withName("header"),
	            MemoryLayout.structLayout(
	            		ValueLayout.JAVA_DOUBLE.withName("x"),
	            		ValueLayout.JAVA_DOUBLE.withName("y")
	            ).withName("point")
	        ).withName("cairo_path_data_t");
	}
	
	private static final VarHandle TYPE = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("type"));
	private static final VarHandle LENGTH = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("length"));
	private static final VarHandle X = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("x"));
	private static final VarHandle Y = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("y"));
	
	/**
	 * Read the type field of the header
	 * 
	 * @return the type
	 */
	public PathDataType type() {
		int result = (int) TYPE.get(handle());
		return PathDataType.of(result);
	}
	
	/**
	 * Read the length field of the header
	 * 
	 * @return the length
	 */
	public int length() {
		return (int) LENGTH.get(handle());
	}
	
	/**
	 * Read the x field of the point
	 * 
	 * @return the x value
	 */
	public double x() {
		return (double) X.get(handle());
	}
	
	/**
	 * Read the y field of the point
	 * 
	 * @return the y value
	 */
	public double y() {
		return (double) Y.get(handle());
	}
	
	/**
	 * Constructor used internally to instantiate a java PathData object for a
	 * native {@code cairo_path_data_t} instance
	 * 
	 * @param address the memory address of the native {@code cairo_path_data_t}
	 *                instance
	 */
	public PathData(MemorySegment address) {
		super(address);
	}
}
