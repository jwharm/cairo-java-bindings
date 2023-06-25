package org.freedesktop.cairo;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;

/**
 * Utility class that loads the native cairo library, and contains functions to
 * create method handles and allocate memory.
 */
public class Interop {

    private final static SymbolLookup symbolLookup;
    private final static Linker linker = Linker.nativeLinker();

    // Load the cairo library during class initialization.
    // This is triggered by calling Interop.ensureInitialized(), and will run only
    // once.
    static {
        SymbolLookup loaderLookup = SymbolLookup.loaderLookup();
        symbolLookup = name -> loaderLookup.find(name).or(() -> linker.defaultLookup().find(name));

        LibLoad.loadLibrary("cairo");
    }

    /**
     * Ensures the Interop class initializer has loaded the cairo library.
     */
    public static void ensureInitialized() {
    }

    /**
     * Prevent instantiation
     */
    private Interop() {
    }

    /**
     * Creates a method handle that is used to call the native function with the
     * provided name and function descriptor.
     * 
     * @param name  Name of the native function
     * @param fdesc Function descriptor of the native function
     * @return the MethodHandle
     */
    public static MethodHandle downcallHandle(String name, FunctionDescriptor fdesc) {
        return symbolLookup.find(name).map(addr -> linker.downcallHandle(addr, fdesc)).orElse(null);
    }

    /**
     * Creates a method handle that is used to call the native function at the
     * provided memory address.
     * 
     * @param symbol Memory address of the native function
     * @param fdesc  Function descriptor of the native function
     * @return the MethodHandle
     */
    public static MethodHandle downcallHandle(MemorySegment symbol, FunctionDescriptor fdesc) {
        return linker.downcallHandle(symbol, fdesc);
    }

    /**
     * Null-safe wrapper around {@link SegmentAllocator#allocateUtf8String(String)}.
     * <p>
     * When the provided string is null or an empty string (""), returns
     * {@code MemorySegment.NULL}; otherwise, allocates a native string using
     * SegmentAllocator.allocateUtf8String(String).
     * 
     * @param string    the string to allocate as a native string (utf8 char*)
     * @param allocator the segment allocator to use
     * @return the allocated MemorySegment
     */
    public static MemorySegment allocateString(String string, SegmentAllocator allocator) {
        return (string == null || "".equals(string)) ? MemorySegment.NULL : allocator.allocateUtf8String(string);
    }
}
