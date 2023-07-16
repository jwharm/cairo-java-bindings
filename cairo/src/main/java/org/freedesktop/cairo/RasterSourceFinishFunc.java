package org.freedesktop.cairo;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * RasterSourceFinishFunc is the type of function which is called when the
 * pattern (or a copy thereof) is no longer required.
 * 
 * @since 1.12
 */
@FunctionalInterface
public interface RasterSourceFinishFunc {

    /**
     * Called when the pattern (or a copy thereof) is no longer required.
     * 
     * @param pattern the pattern being rendered from
     * @since 1.12
     */
    void finish(RasterSource pattern);

    /**
     * The callback that is executed by native code. This method marshals the
     * parameters and calls {@link #finish(RasterSource)}.
     * 
     * @param pattern      the pattern being rendered from
     * @param callbackData ignored
     * @since 1.12
     */
    default void upcall(MemorySegment pattern, MemorySegment callbackData) {
        finish(new RasterSource(pattern));
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
            FunctionDescriptor fdesc = FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS);
            MethodHandle handle = MethodHandles.lookup().findVirtual(RasterSourceFinishFunc.class, "upcall",
                    fdesc.toMethodType());
            return Linker.nativeLinker().upcallStub(handle.bindTo(this), fdesc, scope);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
