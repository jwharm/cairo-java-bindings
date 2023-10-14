package org.freedesktop.cairo;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * A generic callback function for surface operations.
 *
 * @since 1.12
 */
@FunctionalInterface
public interface SurfaceObserverCallback {

    /**
     * A generic callback function for surface operations.
     *
     * @param target the observed surface
     * @since 1.12
     */
    void run(Surface target);

    /**
     * The callback that is executed by native code. This method marshals the
     * parameters and calls {@link #run}.
     *
     * @param observer the {@link SurfaceObserver}, ignored
     * @param target   the observed surface
     * @param data     ignored
     * @since 1.12
     */
    default void upcall(MemorySegment observer, MemorySegment target, MemorySegment data) {
        run(new Surface(target));
    }

    /**
     * Generates an upcall stub, a C function pointer that will call
     * {@link #upcall}.
     *
     * @param scope the scope in which the upcall stub will be allocated
     * @return the function pointer of the upcall stub
     * @since 1.12
     */
    default MemorySegment toCallback(SegmentScope scope) {
        try {
            FunctionDescriptor fdesc = FunctionDescriptor.ofVoid(ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS);
            MethodHandle handle = MethodHandles.lookup().findVirtual(
                    SurfaceObserverCallback.class, "upcall", fdesc.toMethodType());
            return Linker.nativeLinker().upcallStub(handle.bindTo(this), fdesc, scope);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
