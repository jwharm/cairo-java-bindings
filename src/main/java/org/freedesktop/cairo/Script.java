package org.freedesktop.cairo;

import java.io.OutputStream;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import io.github.jwharm.cairobindings.Interop;

/**
 * Output device for use with a {@link ScriptSurface}.
 * <p>
 * The script surface provides the ability to render to a native script that
 * matches the cairo drawing model. The scripts can be replayed using tools
 * under the util/cairo-script directory, or with cairo-perf-trace.
 * 
 * @see Surface, ScriptSurface, Device
 * @since 1.12
 */
public class Script extends Device {

	{
		Interop.ensureInitialized();
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
		Status status = null;
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment filenamePtr = (filename == null) ? MemorySegment.NULL
						: arena.allocateUtf8String(filename);
				MemorySegment result = (MemorySegment) cairo_script_create.invoke(filenamePtr);
				Script script = new Script(result);
				script.takeOwnership();
				status = script.status();
				return script;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			if (status == Status.NO_MEMORY) {
				throw new RuntimeException(status.toString());
			}
		}
	}

	private static final MethodHandle cairo_script_create = Interop.downcallHandle("cairo_script_create",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Creates a output device for emitting the script, used when creating the
	 * individual surfaces.
	 * 
	 * @param stream {@link OutputStream} passed the bytes written to the script
	 * @return the newly created device
	 * @since 1.12
	 */
	public static Script create(OutputStream stream) {
		Status status = null;
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
			Script script = new Script(result);
			script.takeOwnership();
			if (stream != null) {
				script.callbackAllocation = writeFuncPtr; // Keep the memory segment of the upcall stub alive
			}
			status = script.status();
			return script;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			if (status == Status.NO_MEMORY) {
				throw new RuntimeException(status.toString());
			}
		}
	}

	private static final MethodHandle cairo_script_create_for_stream = Interop.downcallHandle(
			"cairo_script_create_for_stream",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

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
		} finally {
			if (status() == Status.NO_MEMORY) {
				throw new RuntimeException(status().toString());
			}
		}
	}

	private static final MethodHandle cairo_script_from_recording_surface = Interop.downcallHandle(
			"cairo_script_from_recording_surface", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS),
			false);

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
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Change the output mode of the script
	 * 
	 * @param mode the new mode
	 * @since 1.12
	 */
	public void setMode(ScriptMode mode) {
		try {
			cairo_script_set_mode.invoke(handle(), mode.value());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_script_set_mode = Interop.downcallHandle("cairo_script_set_mode",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT), false);

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
		Status status = null;
		try {
			MemorySegment result = (MemorySegment) cairo_script_surface_create.invoke(handle(), content.value(), width,
					height);
			ScriptSurface surface = new ScriptSurface(result);
			surface.takeOwnership();
			surface.script = this; // keep the Script instance alive
			status = surface.status();
			return surface;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			if (status == Status.NO_MEMORY) {
				throw new RuntimeException(status.toString());
			}
		}
	}

	private static final MethodHandle cairo_script_surface_create = Interop.downcallHandle(
			"cairo_script_surface_create", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS,
					ValueLayout.JAVA_INT, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE),
			false);

	/**
	 * Create a proxy surface that will render to {@code target} and record the
	 * operations to this script (output device).
	 * 
	 * @param target a target surface to wrap
	 * @return the newly created surface.
	 * @since 1.12
	 */
	public ScriptSurface createScriptSurfaceForTarget(Surface target) {
		Status status = null;
		try {
			MemorySegment result = (MemorySegment) cairo_script_surface_create_for_target.invoke(handle(),
					target == null ? MemorySegment.NULL : target.handle());
			ScriptSurface surface = new ScriptSurface(result);
			surface.takeOwnership();
			surface.script = this; // keep the Script instance alive
			surface.target = target; // keep the target Surface instance alive
			status = surface.status();
			return surface;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			if (status == Status.NO_MEMORY) {
				throw new RuntimeException(status.toString());
			}
		}
	}

	private static final MethodHandle cairo_script_surface_create_for_target = Interop.downcallHandle(
			"cairo_script_surface_create_for_target",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

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
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT), false);
}
