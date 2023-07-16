package org.freedesktop.freetype;

import io.github.jwharm.javagi.base.ProxyInstance;
import io.github.jwharm.javagi.interop.Interop;
import io.github.jwharm.javagi.interop.MemoryCleaner;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * A handle to a typographic face object. A face object models a given typeface,
 * in a given style.
 */
public class Face extends ProxyInstance {

    static {
        FreeType2.ensureInitialized();
    }

    /**
     * Constructor used internally to instantiate a java Face object for a native
     * {@code FT_Face} instance
     *
     * @param address the memory address of the native {@code FT_Face} instance
     */
    public Face(MemorySegment address) {
        super(address);
        MemoryCleaner.setFreeFunc(handle(), "FT_Done_Face");
    }

    /**
     * Invokes the cleanup action that is normally invoked during garbage collection.
     * If the instance is "owned" by the user, the {@code destroy()} function is run
     * to dispose the native instance.
     */
    public void destroy() {
        MemoryCleaner.free(handle());
    }

    /**
     * Call {@code FT_Open_Face} to open a font by its pathname.
     * 
     * @param library      the library resource
     * @param filepathname path to the font file
     * @param faceIndex    See <a href=
     *                     "https://freetype.org/freetype2/docs/reference/ft2-face_creation.html#ft_open_face">FT_Open_Face</a>
     *                     for a detailed description of this parameter.
     */
    public Face(Library library, String filepathname, long faceIndex) {
        super(constructNew(library, filepathname, faceIndex));
        MemoryCleaner.takeOwnership(handle());
    }

    /*
     * Helper function for the Face(library, filepathname, faceIndex) constructor
     */
    private static MemorySegment constructNew(Library library, String filepathname, long faceIndex) {
        try {
            MemorySegment pointer = SegmentAllocator.nativeAllocator(SegmentScope.auto())
                    .allocate(ValueLayout.ADDRESS.asUnbounded());
            try (Arena arena = Arena.openConfined()) {
                MemorySegment utf8 = Interop.allocateNativeString(filepathname, arena);
                int result = (int) FT_New_Face.invoke(library.handle(), utf8, faceIndex, pointer);
                if (result != 0) {
                    throw new UnsupportedOperationException(
                            "Error " + result + " occurred during FreeType FT_Face initialization");
                }
            }
            return pointer.get(ValueLayout.ADDRESS, 0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle FT_New_Face = Interop.downcallHandle("FT_New_Face",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG,
                    ValueLayout.ADDRESS));
}
