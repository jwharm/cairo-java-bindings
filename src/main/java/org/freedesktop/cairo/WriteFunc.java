package org.freedesktop.cairo;

import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * WriteFunc is the type of function which is called when a backend needs to
 * write data to an output stream. It is passed the closure which was specified
 * by the user at the time the write function was registered, the data to write
 * and the length of the data in bytes. The write function should throw
 * {@link IOException} if all the data was not successfully written.
 * 
 * @since 1.0
 */
@FunctionalInterface
public interface WriteFunc {

	/**
	 * The function to implement as callback in a write operation to an output
	 * stream.
	 * 
	 * @param data the data to write to the output stream
	 * @throws IOException to be thrown when an error occurs during the write
	 *                     operation
	 * @since 1.0
	 */
	public void write(byte[] data) throws IOException;

	/**
	 * The callback that is executed by native code. This method marshals the
	 * parameters and calls {@link #write(byte[])}.
	 * 
	 * @param closure ignored
	 * @param data    the buffer from which to read the data
	 * @param length  the amount of data to write
	 * @return {@link Status#SUCCESS} on success, or {@link Status#WRITE_ERROR} if
	 *         an IOException occured.
	 * @since 1.0
	 */
	default int upcall(MemorySegment closure, MemorySegment data, int length) {
		try (Arena arena = Arena.openConfined()) {
			byte[] bytes = MemorySegment.ofAddress(data.address(), length, arena.scope())
					.toArray(ValueLayout.JAVA_BYTE);
			try {
				write(bytes);
				return Status.SUCCESS.ordinal();
			} catch (IOException ioe) {
				return Status.WRITE_ERROR.ordinal();
			}
		}
	}

	/**
	 * Generates an upcall stub, a C function pointer that will call
	 * {@link #upcall(MemorySegment, MemorySegment, int)}.
	 * 
	 * @param scope the scope in which the upcall stub will be allocated
	 * @return the function pointer of the upcall stub
	 * @since 1.0
	 */
	default MemorySegment toCallback(SegmentScope scope) {
		try {
			FunctionDescriptor fdesc = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
					ValueLayout.ADDRESS, ValueLayout.JAVA_INT);
			MethodHandle handle = MethodHandles.lookup().findVirtual(WriteFunc.class, "upcall", fdesc.toMethodType());
			return Linker.nativeLinker().upcallStub(handle.bindTo(this), fdesc, scope);
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
