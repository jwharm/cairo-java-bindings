package org.freedesktop.cairo.utilities;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

import io.github.jwharm.cairobindings.ProxyInstance;

/**
 * A data structure for holding a rectangle with integer coordinates.
 * 
 * @since 1.10
 */
public class RectangleInt extends ProxyInstance {

	/**
	 * The memory layout of the native C struct
	 * 
	 * @return the memory layout of the native C struct
	 */
	public static MemoryLayout getMemoryLayout() {
		return MemoryLayout.structLayout(
				ValueLayout.JAVA_INT.withName("x"), 
				ValueLayout.JAVA_INT.withName("y"), 
				ValueLayout.JAVA_INT.withName("width"), 
				ValueLayout.JAVA_INT.withName("height"))
			.withName("cairo_rectangle_int_t");
	}

	private static final VarHandle X = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("x"));
	private static final VarHandle Y = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("y"));
	private static final VarHandle WIDTH = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("width"));
	private static final VarHandle HEIGHT = getMemoryLayout()
			.varHandle(MemoryLayout.PathElement.groupElement("height"));

	/**
	 * Get the x value of the RectangleInt
	 * 
	 * @return the x value
	 */
	public int x() {
		return (int) X.get(handle());
	}

	/**
	 * Get the y value of the RectangleInt
	 * 
	 * @return the y value
	 */
	public int y() {
		return (int) Y.get(handle());
	}

	/**
	 * Get the width value of the RectangleInt
	 * 
	 * @return the width value
	 */
	public int width() {
		return (int) WIDTH.get(handle());
	}

	/**
	 * Get the height value of the RectangleInt
	 * 
	 * @return the height value
	 */
	public int height() {
		return (int) HEIGHT.get(handle());
	}

	/**
	 * Constructor used internally to instantiate a java RectangleInt object for a
	 * native {@code cairo_rectangle_int_t} instance
	 * 
	 * @param address the memory address of the native {@code cairo_rectangle_int_t}
	 *                instance
	 */
	public RectangleInt(MemorySegment address) {
		super(address);
	}

	/**
	 * A data structure for holding a rectangle with integer coordinates.
	 * 
	 * @param x      X coordinate of the left side of the rectangle
	 * @param y      Y coordinate of the the top side of the rectangle
	 * @param width  width of the rectangle
	 * @param height height of the rectangle
	 */
	public RectangleInt(int x, int y, int width, int height) {
		super(SegmentAllocator.nativeAllocator(SegmentScope.auto()).allocate(getMemoryLayout()));
	}
}