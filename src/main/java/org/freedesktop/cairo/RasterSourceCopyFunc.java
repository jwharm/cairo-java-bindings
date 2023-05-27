package org.freedesktop.cairo;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * RasterSourceCopyFunc is the type of function which is called when the pattern
 * gets copied as a normal part of rendering.
 * 
 * @since 1.12
 */
@FunctionalInterface
public interface RasterSourceCopyFunc {

	/**
	 * Called when the pattern gets copied as a normal part of rendering.
	 * 
	 * @param pattern the pattern that was copied to
	 * @param other   the pattern being used as the source for the copy
	 * @return {@link Status#SUCCESS} on success, or one of the {@link Status} error
	 *         codes for failure.
	 * @since 1.12
	 */
	public Status copy(RasterSource pattern, RasterSource other);

	/**
	 * The callback that is executed by native code. This method marshals the
	 * parameters and calls {@link #copy(RasterSource, RasterSource)}.
	 * 
	 * @param pattern      the pattern being rendered from
	 * @param callbackData ignored
	 * @param other        the pattern being used as the source for the copy
	 * @return {@link Status#SUCCESS} on success, or one of the {@link Status} error
	 *         codes for failure.
	 * @since 1.12
	 */
	default int upcall(MemorySegment pattern, MemorySegment callbackData, MemorySegment other) {
		Status result = copy(new RasterSource(pattern), new RasterSource(other));
		/*
		 * Will throw a NPE if the callback function returns null. This is deliberate:
		 * The snapshot function must always return a Status enum member; failing to do
		 * so is a programming error that should fail fast and obvious.
		 */
		return result.ordinal();
	}

	/**
	 * Generates an upcall stub, a C function pointer that will call
	 * {@link #upcall(MemorySegment, MemorySegment, MemorySegment)}.
	 * 
	 * @param scope the scope in which the upcall stub will be allocated
	 * @return the function pointer of the upcall stub
	 * @since 1.12
	 */
	default MemorySegment toCallback(SegmentScope scope) {
		try {
			FunctionDescriptor fdesc = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
					ValueLayout.ADDRESS, ValueLayout.ADDRESS);
			MethodHandle handle = MethodHandles.lookup().findVirtual(RasterSourceCopyFunc.class, "upcall",
					fdesc.toMethodType());
			return Linker.nativeLinker().upcallStub(handle.bindTo(this), fdesc, scope);
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
