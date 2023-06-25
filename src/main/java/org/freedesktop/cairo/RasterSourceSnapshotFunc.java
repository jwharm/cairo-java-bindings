package org.freedesktop.cairo;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * RasterSourceSnapshotFunc is the type of function which is called when the
 * pixel data needs to be preserved for later use during printing. This pattern
 * will be accessed again later, and it is expected to provide the pixel data
 * that was current at the time of snapshotting.
 * 
 * @since 1.12
 */
@FunctionalInterface
public interface RasterSourceSnapshotFunc {

    /**
     * Called when the pixel data needs to be preserved for later use during
     * printing. This pattern will be accessed again later, and it is expected to
     * provide the pixel data that was current at the time of snapshotting.
     * 
     * @param pattern the pattern being rendered from
     * @return {@link Status#SUCCESS} on success, or one of the {@link Status} error
     *         codes for failure.
     * @since 1.12
     */
    Status snapshot(RasterSource pattern);

    /**
     * The callback that is executed by native code. This method marshals the
     * parameters and calls {@link #snapshot(RasterSource)}.
     * 
     * @param pattern      the pattern being rendered from
     * @param callbackData ignored
     * @return {@link Status#SUCCESS} on success, or one of the {@link Status} error
     *         codes for failure.
     * @since 1.12
     */
    default int upcall(MemorySegment pattern, MemorySegment callbackData) {
        Status result = snapshot(new RasterSource(pattern));
        /*
         * Will throw a NPE if the callback function returns null. This is deliberate:
         * The snapshot function must always return a Status enum member; failing to do
         * so is a programming error that should fail fast and obvious.
         */
        return result.value();
    }

    /**
     * Generates an upcall stub, a C function pointer that will call
     * {@link #upcall(MemorySegment, MemorySegment)}.
     * 
     * @param scope the scope in which the upcall stub will be allocated
     * @return the function pointer of the upcall stub
     * @since 1.12
     */
    default MemorySegment toCallback(SegmentScope scope) {
        try {
            FunctionDescriptor fdesc = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS);
            MethodHandle handle = MethodHandles.lookup().findVirtual(RasterSourceSnapshotFunc.class, "upcall",
                    fdesc.toMethodType());
            return Linker.nativeLinker().upcallStub(handle.bindTo(this), fdesc, scope);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
