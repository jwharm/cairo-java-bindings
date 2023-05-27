package org.freedesktop.cairo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;
import java.util.List;

import io.github.jwharm.cairobindings.ProxyInstance;

/**
 * A data structure for holding a dynamically allocated array of rectangles.
 * 
 * @since 1.4
 */
public class RectangleList extends ProxyInstance {

	/**
	 * The memory layout of the native C struct
	 * 
	 * @return the memory layout of the native C struct
	 */
	public static MemoryLayout getMemoryLayout() {
		return MemoryLayout.structLayout(
				ValueLayout.JAVA_INT.withName("status"), 
				MemoryLayout.paddingLayout(32),
				ValueLayout.ADDRESS.withName("rectangles"), 
				ValueLayout.JAVA_INT.withName("num_rectangles"), 
				MemoryLayout.paddingLayout(32))
			.withName("cairo_rectangle_list_t");
	}

	private static final VarHandle STATUS = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("status"));
	private static final VarHandle RECTANGLES = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("rectangles"));
	private static final VarHandle NUM_RECTANGLES = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("num_rectangles"));

	/**
	 * Get the status field of the RectangleList
	 * 
	 * @return the status
	 */
	public Status status() {
		int result = (int) STATUS.get(handle());
		return Status.of(result);
	}

	/**
	 * Get the rectangles field of the RectangleList. The field is an unmodifiable
	 * List.
	 * 
	 * @return the list of rectangles
	 */
	public List<Rectangle> rectangles() {
		MemorySegment address = (MemorySegment) RECTANGLES.get(handle());
		int length = (int) NUM_RECTANGLES.get(handle());
		// MemorySegment.elements() only works for >1 elements
		if (length == 1) {
			return List.of(new Rectangle(address));
		}
		long segmentSize = Rectangle.getMemoryLayout().byteSize() * length;
		MemorySegment array = MemorySegment.ofAddress(address.address(), segmentSize, handle().scope());
		return array.elements(Rectangle.getMemoryLayout()).map(Rectangle::new).toList();
	}

	/**
	 * Constructor used internally to instantiate a java RectangleList object for a
	 * native {@code cairo_rectangle_list_t} instance
	 * 
	 * @param address the memory address of the native
	 *                {@code cairo_rectangle_list_t} instance
	 */
	public RectangleList(MemorySegment address) {
		super(address);
		setDestroyFunc("cairo_rectangle_list_destroy");
	}

	/**
	 * A data structure for holding a dynamically allocated array of rectangles.
	 * 
	 * @param status     Error status of the rectangle list
	 * @param rectangles List containing the rectangles
	 */
	public RectangleList(Status status, List<Rectangle> rectangles) {
		super(SegmentAllocator.nativeAllocator(SegmentScope.auto()).allocate(getMemoryLayout()));
		STATUS.set(handle(), status.ordinal());
		NUM_RECTANGLES.set(handle(), rectangles.size());
		if (rectangles.isEmpty()) {
			return;
		}
		MemorySegment array = SegmentAllocator.nativeAllocator(handle().scope())
				.allocateArray(Rectangle.getMemoryLayout(), rectangles.size());
		for (int i = 0; i < rectangles.size(); i++) {
			MemorySegment rectangle = rectangles.get(i).handle();
			MemorySegment src = MemorySegment.ofAddress(rectangle.address(), Rectangle.getMemoryLayout().byteSize(),
					rectangle.scope());
			MemorySegment dst = array.asSlice(i * Rectangle.getMemoryLayout().byteSize());
			dst.copyFrom(src);
		}
	}
}