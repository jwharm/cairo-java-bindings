package org.freedesktop.cairo;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * RasterSourceAcquireFunc is the type of function which is called when a
 * pattern is being rendered from. It should create a surface that provides the
 * pixel data for the region of interest as defined by extents, though the
 * surface itself does not have to be limited to that area. For convenience the
 * surface should probably be of image type, created with
 * {@link Surface#createSimilarImage(Surface, Format, int, int)} for the target
 * (which enables the number of copies to be reduced during transfer to the
 * device). Another option, might be to return a similar surface to the target
 * for explicit handling by the application of a set of cached sources on the
 * device. The region of sample data provided should be defined using
 * {@link Surface#setDeviceOffset(double, double)} to specify the top-left
 * corner of the sample data (along with width and height of the surface).
 * 
 * @since 1.12
 */
@FunctionalInterface
public interface RasterSourceAcquireFunc {

	/**
	 * Called when a pattern is being rendered from. It should create a surface that
	 * provides the pixel data for the region of interest as defined by extents,
	 * though the surface itself does not have to be limited to that area. For
	 * convenience the surface should probably be of image type, created with
	 * {@link Surface#createSimilarImage(Surface, Format, int, int)} for the target
	 * (which enables the number of copies to be reduced during transfer to the
	 * device). Another option, might be to return a similar surface to the target
	 * for explicit handling by the application of a set of cached sources on the
	 * device. The region of sample data provided should be defined using
	 * {@link Surface#setDeviceOffset(double, double)} to specify the top-left
	 * corner of the sample data (along with width and height of the surface).
	 * 
	 * @param pattern the pattern being rendered from
	 * @param target  the rendering target surface
	 * @param extents rectangular region of interest in pixels in sample space
	 * @return a {@link Surface}
	 * @since 1.12
	 */
	public Surface acquire(RasterSource pattern, Surface target, RectangleInt extents);

	/**
	 * The callback that is executed by native code. This method marshals the
	 * parameters and calls {@link #acquire(RasterSource, Surface, RectangleInt)}.
	 * 
	 * @param pattern      the pattern being rendered from
	 * @param callbackData ignored
	 * @param target       the rendering target surface
	 * @param extents      rectangular region of interest in pixels in sample space
	 * @return a {@link Surface}
	 * @since 1.12
	 */
	default MemorySegment upcall(MemorySegment pattern, MemorySegment callbackData, MemorySegment target,
			MemorySegment extents) {
		Surface result = acquire(new RasterSource(pattern), new Surface(target), new RectangleInt(extents));
		return result == null ? MemorySegment.NULL : result.handle();
	}

	/**
	 * Generates an upcall stub, a C function pointer that will call
	 * {@link #upcall(MemorySegment, MemorySegment, MemorySegment, MemorySegment)}.
	 * 
	 * @param scope the scope in which the upcall stub will be allocated
	 * @return the function pointer of the upcall stub
	 * @since 1.12
	 */
	default MemorySegment toCallback(SegmentScope scope) {
		try {
			FunctionDescriptor fdesc = FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS,
					ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS);
			MethodHandle handle = MethodHandles.lookup().findVirtual(RasterSourceAcquireFunc.class, "upcall",
					fdesc.toMethodType());
			return Linker.nativeLinker().upcallStub(handle.bindTo(this), fdesc, scope);
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
