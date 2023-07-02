package org.freedesktop.freetype;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import org.freedesktop.cairo.Interop;
import org.freedesktop.cairo.Proxy;

/**
 * Functions to start and end the usage of the FreeType library.
 * <p>
 * Note that {@link #version()} is of limited use because even a new release of
 * FreeType with only documentation changes increases the version number.
 */
public class Library extends Proxy {

    public Library(MemorySegment address) {
        super(address);
        setDestroyFunc("FT_Done_FreeType");
    }

    /**
     * Initialize a new FreeType library object.
     * 
     * @return a new library object
     * @throws UnsupportedOperationException when {@code FT_Init_FreeType} returns a
     *                                       non-zero error code
     */
    public static Library initFreeType() throws UnsupportedOperationException {
        try {
            MemorySegment pointer = SegmentAllocator.nativeAllocator(SegmentScope.auto())
                    .allocate(ValueLayout.ADDRESS.asUnbounded());
            int result = (int) FT_Init_FreeType.invoke(pointer);
            if (result != 0) {
                throw new UnsupportedOperationException(
                        "Error " + result + " occurred during FreeType library initialization");
            }
            Library library = new Library(pointer.get(ValueLayout.ADDRESS, 0));
            library.takeOwnership();
            return library;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle FT_Init_FreeType = Interop.downcallHandle("FT_Init_FreeType",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Return the version of the FreeType library being used.
     * 
     * @return the major, minor and patch version numbers, formatted as
     *         {@code "%d.%d.%d"}
     */
    public String version() {
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment amajor = arena.allocate(ValueLayout.JAVA_INT);
                MemorySegment aminor = arena.allocate(ValueLayout.JAVA_INT);
                MemorySegment apatch = arena.allocate(ValueLayout.JAVA_INT);
                FT_Library_Version.invoke(handle(), amajor, aminor, apatch);
                int major = amajor.get(ValueLayout.JAVA_INT, 0);
                int minor = aminor.get(ValueLayout.JAVA_INT, 0);
                int patch = apatch.get(ValueLayout.JAVA_INT, 0);
                return String.format("%d.%d.%d", major, minor, patch);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle FT_Library_Version = Interop.downcallHandle("FT_Library_Version",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS));
}
