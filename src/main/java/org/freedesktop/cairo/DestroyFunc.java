package org.freedesktop.cairo;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * DestroyFunc the type of function which is called when a data element is
 * destroyed. It is passed the pointer to the data element and should free any
 * memory and resources allocated for it.
 * 
 * @since 1.0
 */
@FunctionalInterface
public interface DestroyFunc {

    /**
     * The function to implement as callback in a destroy operation.
     * 
     * @since 1.0
     */
    void destroy();

    /**
     * The callback that is executed by native code. This method marshals the
     * parameters and calls {@link #destroy()}.
     * 
     * @param data the buffer into which to read the data
     * @since 1.0
     */
    default void upcall(MemorySegment data) {
        destroy();
    }

    /**
     * Generates an upcall stub, a C function pointer that will call
     * {@link #upcall(MemorySegment).
     * 
     * @param scope the scope in which the upcall stub will be allocated
     * @return the function pointer of the upcall stub
     * @since 1.0
     */
    default MemorySegment toCallback(SegmentScope scope) {
        try {
            FunctionDescriptor fdesc = FunctionDescriptor.ofVoid(ValueLayout.ADDRESS);
            MethodHandle handle = MethodHandles.lookup().findVirtual(DestroyFunc.class, "upcall", fdesc.toMethodType());
            return Linker.nativeLinker().upcallStub(handle.bindTo(this), fdesc, scope);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
