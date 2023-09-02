package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Proxy;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

/**
 * A data structure for holding a rectangle.
 * 
 * @since 1.4
 */
public class Rectangle extends Proxy {

    /**
     * The memory layout of the native C struct
     * 
     * @return the memory layout of the native C struct
     */
    static MemoryLayout getMemoryLayout() {
        return MemoryLayout.structLayout(
                ValueLayout.JAVA_DOUBLE.withName("x"), 
                ValueLayout.JAVA_DOUBLE.withName("y"),
                ValueLayout.JAVA_DOUBLE.withName("width"),
                ValueLayout.JAVA_DOUBLE.withName("height"))
                .withName("cairo_rectangle_t");
    }

    private static final VarHandle X = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("x"));
    private static final VarHandle Y = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("y"));
    private static final VarHandle WIDTH = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("width"));
    private static final VarHandle HEIGHT = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("height"));

    /**
     * Get the x value of the Rectangle
     * 
     * @return the x value
     */
    public double x() {
        return (double) X.get(handle());
    }

    /**
     * Get the y value of the Rectangle
     * 
     * @return the y value
     */
    public double y() {
        return (double) Y.get(handle());
    }

    /**
     * Get the width value of the Rectangle
     * 
     * @return the width value
     */
    public double width() {
        return (double) WIDTH.get(handle());
    }

    /**
     * Get the height value of the Rectangle
     * 
     * @return the height value
     */
    public double height() {
        return (double) HEIGHT.get(handle());
    }

    /**
     * Constructor used internally to instantiate a java Rectangle object for a
     * native {@code cairo_rectangle_t} instance
     * 
     * @param address the memory address of the native {@code cairo_rectangle_t}
     *                instance
     */
    public Rectangle(MemorySegment address) {
        super(address);
    }

    /**
     * A data structure for holding a rectangle.
     * 
     * @param x      X coordinate of the left side of the rectangle
     * @param y      Y coordinate of the the top side of the rectangle
     * @param width  width of the rectangle
     * @param height height of the rectangle
     * @return the newly created rectangle
     */
    public static Rectangle create(double x, double y, double width, double height) {
        Rectangle rect = new Rectangle(SegmentAllocator.nativeAllocator(SegmentScope.auto()).allocate(getMemoryLayout()));
        X.set(rect.handle(), x);
        Y.set(rect.handle(), y);
        WIDTH.set(rect.handle(), width);
        HEIGHT.set(rect.handle(), height);
        return rect;
    }
    
    /**
     * String representation of this Rectangle
     * 
     * @return a String representation of this Rectangle
     */
    @Override
    public String toString() {
        return String.format("Rectangle address=%d x=%f y=%f width=%f height=%f", handle().address(), x(), y(), width(), height());
    }
}