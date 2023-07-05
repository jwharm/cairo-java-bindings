package org.freedesktop.cairo;

import io.github.jwharm.javagi.base.ProxyInstance;
import io.github.jwharm.javagi.interop.MemoryCleaner;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;
import java.util.List;

/**
 * A data structure for holding a dynamically allocated array of rectangles.
 * 
 * @since 1.4
 */
public class RectangleList extends ProxyInstance {

    static {
        Cairo.ensureInitialized();
    }

    /**
     * The memory layout of the native C struct
     * 
     * @return the memory layout of the native C struct
     */
    static MemoryLayout getMemoryLayout() {
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
     * Read the status field of the RectangleList
     * 
     * @return the status
     */
    public Status status() {
        int result = (int) STATUS.get(handle());
        return Status.of(result);
    }

    /**
     * Read the rectangles field of the RectangleList. The field is an unmodifiable
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
        MemoryCleaner.setFreeFunc(handle(), "cairo_rectangle_list_destroy");
    }

    /**
     * Invokes the cleanup action that is normally invoked during garbage collection.
     * If the instance is "owned" by the user, the {@code destroy()} function is run
     * to dispose the native instance.
     */
    public void destroy() {
        MemoryCleaner.free(handle());
    }

    /**
     * A data structure for holding a dynamically allocated array of rectangles.
     * 
     * @param  status     Error status of the rectangle list
     * @param  rectangles List containing the rectangles
     * @return the newly created RectangleList
     */
    public static RectangleList create(Status status, List<Rectangle> rectangles) {
        RectangleList rectangleList = new RectangleList(SegmentAllocator.nativeAllocator(SegmentScope.auto()).allocate(getMemoryLayout()));
        STATUS.set(rectangleList.handle(), status.getValue());
        NUM_RECTANGLES.set(rectangleList.handle(), rectangles == null ? 0 : rectangles.size());
        if (rectangles == null || rectangles.isEmpty()) {
            return rectangleList;
        }
        MemorySegment array = SegmentAllocator.nativeAllocator(rectangleList.handle().scope())
                .allocateArray(Rectangle.getMemoryLayout(), rectangles.size());
        for (int i = 0; i < rectangles.size(); i++) {
            MemorySegment rectangle = rectangles.get(i).handle();
            MemorySegment src = MemorySegment.ofAddress(rectangle.address(), Rectangle.getMemoryLayout().byteSize(),
                    rectangle.scope());
            MemorySegment dst = array.asSlice(i * Rectangle.getMemoryLayout().byteSize());
            dst.copyFrom(src);
        }
        return rectangleList;
    }
}
