package io.github.jwharm.cairobindings;

import java.lang.foreign.MemorySegment;

/**
 * Base type for a Java proxy object to an instance in native memory.
 */
public class Proxy {

    private final MemorySegment address;

    /**
     * Create a new {@code Proxy} object for an instance in native memory.
     * @param address the memory address of the instance
     */
    public Proxy(MemorySegment address) {
        this.address = address;
        MemoryCleaner.register(this);
    }

    /**
     * Get the memory address of the instance
     * @return the memory address of the instance
     */
    public MemorySegment handle() {
        return address;
    }

    /**
     * Returns the hashcode of the memory address
     * @return the hashcode of the memory address
     * @see MemorySegment#hashCode()
     */
    public int hashCode() {
        return address.hashCode();
    }

    /**
     * Checks whether the other object is a Proxy instance and the memory
     * addresses are equal.
     * @param obj another object
     * @return true when the other object is a Proxy instance and the memory
     *         addresses are equal, otherwise false.
     * @see MemorySegment#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Proxy other
                && address.equals(other.address);
    }
}
