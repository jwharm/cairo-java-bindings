package org.freedesktop.cairo;

import io.github.jwharm.javagi.base.Proxy;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * UserDataStore is a utility class that is used to get and set user data on cairo types that
 * support it. The store internally uses a {@link ConcurrentHashMap}. It is safe
 * to use in a multi-threaded environment, but {@code null} keys are not
 * supported.
 */
class UserDataStore {

    private final Map<UserDataKey, Object> userData = new ConcurrentHashMap<>();
    private final SegmentScope scope;
    
    /**
     * Create a new UserDataStore.
     * 
     * @param scope The memory scope that will be used to create native data
     *              segments (see {@link #dataSegment(Object)}).
     */
    UserDataStore(SegmentScope scope) {
        this.scope = scope;
    }

    /**
     * Set the key to the provided value in the store
     * @param key the key
     * @param data the value
     * @throws NullPointerException if the key is {@code null}
     */
    void set(UserDataKey key, Object data) {
        userData.put(key, data);
    }
    
    /**
     * Get the user data from the store that was set with the provided key
     * @param key the key
     * @return the user data, or {@code null} if the key is null
     */
    Object get(UserDataKey key) {
        return key == null ? null : userData.get(key);
    }
    
    /**
     * Allocate a native memory segment that contains the value of this key. This
     * will work for primitive types, {@link MemorySegment} and {@link Proxy}
     * instances. For all other classes, this will return
     * {@link MemorySegment#NULL}.
     * 
     * @param value the value to put in a newly allocated memory segment
     * @return the newly allocated memory segment
     */
    MemorySegment dataSegment(Object value) {
        MemorySegment data;
        switch (value) {
            case MemorySegment m -> data = m;
            case Proxy p -> data = p.handle();
            case Boolean b -> {
                data = SegmentAllocator.nativeAllocator(scope).allocate(ValueLayout.JAVA_BOOLEAN);
                data.set(ValueLayout.JAVA_BOOLEAN, 0, b);
            }
            case Byte b -> {
                data = SegmentAllocator.nativeAllocator(scope).allocate(ValueLayout.JAVA_BYTE);
                data.set(ValueLayout.JAVA_BYTE, 0, b);
            }
            case Character c -> {
                data = SegmentAllocator.nativeAllocator(scope).allocate(ValueLayout.JAVA_CHAR);
                data.set(ValueLayout.JAVA_CHAR, 0, c);
            }
            case Double d -> {
                data = SegmentAllocator.nativeAllocator(scope).allocate(ValueLayout.JAVA_DOUBLE);
                data.set(ValueLayout.JAVA_DOUBLE, 0, d);
            }
            case Float f -> {
                data = SegmentAllocator.nativeAllocator(scope).allocate(ValueLayout.JAVA_FLOAT);
                data.set(ValueLayout.JAVA_FLOAT, 0, f);
            }
            case Integer i -> {
                data = SegmentAllocator.nativeAllocator(scope).allocate(ValueLayout.JAVA_INT);
                data.set(ValueLayout.JAVA_INT, 0, i);
            }
            case Long l -> {
                data = SegmentAllocator.nativeAllocator(scope).allocate(ValueLayout.JAVA_LONG);
                data.set(ValueLayout.JAVA_LONG, 0, l);
            }
            case Short s -> {
                data = SegmentAllocator.nativeAllocator(scope).allocate(ValueLayout.JAVA_SHORT);
                data.set(ValueLayout.JAVA_SHORT, 0, s);
            }
            default -> {
                data = MemorySegment.NULL;
            }
        }
        return data;
    }
}
