package io.github.jwharm.cairobindings;

import java.lang.foreign.MemorySegment;

/**
 * Represents an instance of a proxy object with a handle to an object 
 * in native memory.
 */
public interface Proxy {

    /**
     * Get the native memory address of the object
     * @return the native memory address
     */
    MemorySegment handle();
}
