package org.freedesktop.cairo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;

/**
 * UserDataKey is used for attaching user data to cairo data structures.
 * <p>
 * The lifetime of a UserDataKey is connected to the lifetime of the Proxy object 
 * that is passed in the {@link #create(Proxy)} method.
 * 
 * @since 1.0
 */
public final class UserDataKey extends Proxy {

    static MemoryLayout getMemoryLayout() {
        return MemoryLayout.structLayout(
                ValueLayout.JAVA_INT.withName("unused")
            ).withName("cairo_user_data_key_t");
    }

    /**
     * Constructor used internally to instantiate a java UserDataKey object for a
     * native {@code UserDataKey} instance
     * 
     * @param address the memory address of the native {@code UserDataKey} instance
     */
    public UserDataKey(MemorySegment address) {
        super(address);
    }

    /**
     * Create a new UserDataKey
     * 
     * @param the Proxy object whose memory scope (lifetime) will be associated with
     *            the returned UserDataKey
     * @return the newly created UserDataKey
     */
    static UserDataKey create(Proxy proxy) {
        return new UserDataKey(SegmentAllocator.nativeAllocator(proxy.handle().scope()).allocate(getMemoryLayout()));
    }
}
