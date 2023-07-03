package org.freedesktop.cairo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * A data structure for holding a path. This data structure serves as the return
 * value for {@link Context#copyPath()} and {@link Context#copyPathFlat()} as
 * well the input value for {@link Context#appendPath(Path)}.
 * <p>
 * The Path class implements {@link Iterable}. The iterator loops through the
 * {@code cairo_path_data_t} segments in native memory and returns
 * {@link PathElement} instances. You can use
 * <a href="https://openjdk.org/jeps/440">record patterns</a> in a switch
 * expression to process the elements:
 * 
 * <pre>
 * Path path = cr.copyPath();
 * for (PathElement element : path) {
 *     switch (element) {
 *         case PathElement.MoveTo(double x, double y) -> doMoveToThings(x, y);
 *         case PathElement.LineTo(double x, double y) -> doLineToThings(x, y);
 *         case PathElement.CurveTo(double x1, double y1, double x2, double y2, double x3, double y3) -> doCurveToThings(x1, y1, x2, y2, x3, y3);
 *         case PathElement.ClosePath() -> doClosePathThings();
 *     }
 * }
 * </pre>
 * 
 * @since 1.0
 */
public class Path extends ProxyInstance implements Iterable<PathElement> {

    static MemoryLayout getMemoryLayout() {
        return MemoryLayout.structLayout(
                ValueLayout.JAVA_INT.withName("status"),
                MemoryLayout.paddingLayout(32),
                ValueLayout.ADDRESS.asUnbounded().withName("data"),
                ValueLayout.JAVA_INT.withName("num_data"),
                MemoryLayout.paddingLayout(32)
            ).withName("cairo_path_t");
    }

    private static final VarHandle STATUS = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("status"));
    private static final VarHandle DATA = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("data"));
    private static final VarHandle NUM_DATA = getMemoryLayout().varHandle(MemoryLayout.PathElement.groupElement("num_data"));

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
     * Return a stream of data segments with the {@link PathData} memorylayout. The
     * number of segments is equal to {@link #numData()}.
     * 
     * @return a stream of data segments
     */
    private Stream<MemorySegment> data() {
        // Read the `data` field (a pointer with unbounded size)
        MemorySegment dataSegment = (MemorySegment) DATA.get(handle());
        // Construct a SequenceLayout, based on the size that is specified in the
        // `num_data` field
        MemoryLayout sequenceLayout = MemoryLayout.sequenceLayout(numData(), PathData.getMemoryLayout());
        // Create a slice that fits the exact size of the SequenceLayout
        MemorySegment slice = dataSegment.asSlice(0, sequenceLayout.byteSize());
        // Split the slice into PathData blocks
        return slice.elements(PathData.getMemoryLayout());
    }

    /**
     * Return the value of the {@code num_data} field
     * 
     * @return the value of the {@code num_data} field
     */
    private int numData() {
        return (int) NUM_DATA.get(handle());
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

    /**
     * Iterates through the path.
     * 
     * @return an iterator that produces {@link PathElement} record instances of
     *         type MoveTo, LineTo, CurveTo or ClosePath for all elements in the
     *         path.
     */
    @Override
    public Iterator<PathElement> iterator() {
        return new Iterator<>() {

            /*
             * Iterates through the `data` memory segment. Each element has a PathData
             * memory layout.
             */
            private final Iterator<MemorySegment> dataIterator = data().iterator();

            @Override
            public boolean hasNext() {
                return dataIterator.hasNext();
            }

            @Override
            public PathElement next() {
                // Get the header element
                PathData header = new PathData(dataIterator.next());

                // Read the number of steps; this could be more than the necessary amount, in
                // which case we must ignore the remaining steps. (The header counts as a step)
                int stepsRemaining = header.length() - 1;
                try {
                    // Return the PathElement record that corresponds with the type
                    return switch (header.type()) {
                    case MOVE_TO -> {
                        stepsRemaining--;
                        PathData data = new PathData(dataIterator.next());
                        yield new PathElement.MoveTo(data.x(), data.y());
                    }
                    case LINE_TO -> {
                        stepsRemaining--;
                        PathData data = new PathData(dataIterator.next());
                        yield new PathElement.LineTo(data.x(), data.y());
                    }
                    case CURVE_TO -> {
                        stepsRemaining -= 3;
                        PathData data1 = new PathData(dataIterator.next());
                        PathData data2 = new PathData(dataIterator.next());
                        PathData data3 = new PathData(dataIterator.next());
                        yield new PathElement.CurveTo(data1.x(), data1.y(), data2.x(), data2.y(), data3.x(), data3.y());
                    }
                    case CLOSE_PATH -> new PathElement.ClosePath();
                    };
                } finally {
                    // Advance the iterator any remaining steps
                    while (stepsRemaining > 0) {
                        stepsRemaining--;
                        dataIterator.next();
                    }
                }
            }
        };
    }
}
