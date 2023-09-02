package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.MemoryCleaner;

import java.io.OutputStream;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Output device for use with a {@link ScriptSurface}.
 * <p>
 * The script surface provides the ability to render to a native script that
 * matches the cairo drawing model. The scripts can be replayed using tools
 * under the util/cairo-script directory, or with cairo-perf-trace.
 * 
 * @see Surface
 * @see ScriptSurface
 * @see Device
 * @since 1.12
 */
public class Script extends Device {

    static {
        Cairo.ensureInitialized();
    }

    /*
     * Initialized by {@link #create(OutputStream)} to keep a reference to the
     * memory segment for the upcall stub alive during the lifetime of the
     * ScriptSurface instance.
     */
    @SuppressWarnings("unused")
    private MemorySegment callbackAllocation;

    /**
     * Constructor used internally to instantiate a java ScriptSurface object for a
     * native {@code cairo_device_t} instance
     * 
     * @param address the memory address of the native {@code cairo_device_t}
     *                instance
     */
    public Script(MemorySegment address) {
        super(address);
    }

    /**
     * Creates a output device for emitting the script, used when creating the
     * individual surfaces.
     * 
     * @param filename the name (path) of the file to write the script to
     * @return the newly created device
     * @since 1.12
     */
    public static Script create(String filename) {
        Script script;
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment filenamePtr = Interop.allocateNativeString(filename, arena);
                MemorySegment result = (MemorySegment) cairo_script_create.invoke(filenamePtr);
                script = new Script(result);
                MemoryCleaner.takeOwnership(script.handle());
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (script.status() == Status.NO_MEMORY) {
            throw new RuntimeException(script.status().toString());
        }
        return script;
    }

    private static final MethodHandle cairo_script_create = Interop.downcallHandle("cairo_script_create",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Creates a output device for emitting the script, used when creating the
     * individual surfaces.
     * 
     * @param stream {@link OutputStream} passed the bytes written to the script
     * @return the newly created device
     * @since 1.12
     */
    public static Script create(OutputStream stream) {
        Script script;
        try {
            MemorySegment writeFuncPtr;
            if (stream != null) {
                WriteFunc writeFunc = stream::write;
                writeFuncPtr = writeFunc.toCallback(SegmentScope.auto());
            } else {
                writeFuncPtr = MemorySegment.NULL;
            }
            MemorySegment result = (MemorySegment) cairo_script_create_for_stream.invoke(writeFuncPtr,
                    MemorySegment.NULL);
            script = new Script(result);
            MemoryCleaner.takeOwnership(script.handle());
            if (stream != null) {
                script.callbackAllocation = writeFuncPtr; // Keep the memory segment of the upcall stub alive
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (script.status() == Status.NO_MEMORY) {
            throw new RuntimeException(script.status().toString());
        }
        return script;
    }

    private static final MethodHandle cairo_script_create_for_stream = Interop.downcallHandle(
            "cairo_script_create_for_stream",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Converts the record operations in recordingSurface into a script.
     * 
     * @param recordingSurface the recording surface to replay
     * @since 1.12
     */
    public void from(RecordingSurface recordingSurface) {
        try {
            cairo_script_from_recording_surface.invoke(handle(),
                    recordingSurface == null ? MemorySegment.NULL : recordingSurface.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status() == Status.NO_MEMORY) {
            throw new RuntimeException(status().toString());
        }
    }

    private static final MethodHandle cairo_script_from_recording_surface = Interop.downcallHandle(
            "cairo_script_from_recording_surface", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Queries the script for its current output mode.
     * 
     * @return the current output mode of the script
     * @since 1.12
     */
    public ScriptMode getMode() {
        try {
            int result = (int) cairo_script_get_mode.invoke(handle());
            return ScriptMode.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_script_get_mode = Interop.downcallHandle("cairo_script_get_mode",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Change the output mode of the script
     * 
     * @param mode the new mode
     * @since 1.12
     */
    public void setMode(ScriptMode mode) {
        try {
            cairo_script_set_mode.invoke(handle(), mode.getValue());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_script_set_mode = Interop.downcallHandle("cairo_script_set_mode",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Create a new surface that will emit its rendering through this script (output
     * device)
     * 
     * @param content the content of the surface
     * @param width   width in pixels
     * @param height  height in pixels
     * @return the newly created surface
     * @since 1.12
     */
    public ScriptSurface createScriptSurface(Content content, double width, double height) {
        ScriptSurface surface;
        try {
            MemorySegment result = (MemorySegment) cairo_script_surface_create.invoke(handle(), content.getValue(), width,
                    height);
            surface = new ScriptSurface(result);
            MemoryCleaner.takeOwnership(surface.handle());
            surface.script = this; // keep the Script instance alive
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (surface.status() == Status.NO_MEMORY) {
            throw new RuntimeException(surface.status().toString());
        }
        return surface;
    }

    private static final MethodHandle cairo_script_surface_create = Interop.downcallHandle(
            "cairo_script_surface_create", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE));

    /**
     * Create a proxy surface that will render to {@code target} and record the
     * operations to this script (output device).
     * 
     * @param target a target surface to wrap
     * @return the newly created surface.
     * @since 1.12
     */
    public ScriptSurface createScriptSurfaceForTarget(Surface target) {
        ScriptSurface surface;
        try {
            MemorySegment result = (MemorySegment) cairo_script_surface_create_for_target.invoke(handle(),
                    target == null ? MemorySegment.NULL : target.handle());
            surface = new ScriptSurface(result);
            MemoryCleaner.takeOwnership(surface.handle());
            surface.script = this; // keep the Script instance alive
            surface.target = target; // keep the target Surface instance alive
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (surface.status() == Status.NO_MEMORY) {
            throw new RuntimeException(surface.status().toString());
        }
        return surface;
    }

    private static final MethodHandle cairo_script_surface_create_for_target = Interop.downcallHandle(
            "cairo_script_surface_create_for_target",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Emit a string verbatim into the script.
     * 
     * @param comment the string to emit
     * @since 1.12
     */
    public void writeComment(String comment) {
        try {
            try (Arena arena = Arena.openConfined()) {
                MemorySegment commentPtr = Interop.allocateNativeString(comment, arena);
                cairo_script_write_comment.invoke(handle(), commentPtr, comment == null ? 0 : comment.length());
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_script_write_comment = Interop.downcallHandle("cairo_script_write_comment",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
}
