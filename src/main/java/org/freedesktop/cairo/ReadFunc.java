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
 * ReadFunc is the type of function which is called when a backend needs to read
 * data from an input stream. It is passed the closure which was specified by
 * the user at the time the read function was registered, the buffer to read the
 * data into and the length of the data in bytes. The read function should throw
 * {@link IOException} if all the data was not successfully read.
 * 
 * @since 1.0
 */
@FunctionalInterface
public interface ReadFunc {

    /**
     * The function to implement as callback in a read operation from an input
     * stream.
     * 
     * @param length the amount of data to read
     * @return data the data read from the input stream
     * @throws IOException to be thrown when an error occurs during the read
     *                     operation
     * @since 1.0
     */
    byte[] read(int length) throws IOException;

    /**
     * The callback that is executed by native code. This method marshals the
     * parameters and calls {@link #read(int)}.
     * 
     * @param closure ignored
     * @param data    the buffer into which to read the data
     * @param length  the amount of data to read
     * @return {@link Status#SUCCESS} on success, or {@link Status#READ_ERROR} if an
     *         IOException occured or 0 bytes (or null) was returned from
     *         {@code read()}.
     * @since 1.0
     */
    default int upcall(MemorySegment closure, MemorySegment data, int length) {
        if (length <= 0) {
            return Status.SUCCESS.getValue();
        }
        try (Arena arena = Arena.openConfined()) {
            try {
                byte[] bytes = read(length);
                if (bytes == null || bytes.length == 0) {
                    return Status.READ_ERROR.getValue();
                }
                MemorySegment.ofAddress(data.address(), length).asByteBuffer().put(bytes);
                return Status.SUCCESS.getValue();
            } catch (IOException ioe) {
                return Status.READ_ERROR.getValue();
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
            MethodHandle handle = MethodHandles.lookup().findVirtual(ReadFunc.class, "upcall", fdesc.toMethodType());
            return Linker.nativeLinker().upcallStub(handle.bindTo(this), fdesc, scope);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
