package org.freedesktop.cairo;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.ProxyInstance;

/**
 * Devices are the abstraction Cairo employs for the rendering system used by a
 * {@link Surface}. You can get the device of a surface using
 * {@link Surface#getDevice()}.
 * <p>
 * Devices are created using custom functions specific to the rendering system
 * you want to use. See the documentation for the surface types for those
 * functions.
 * <p>
 * An important function that devices fulfill is sharing access to the rendering
 * system between Cairo and your application. If you want to access a device
 * directly that you used to draw to with Cairo, you must first call
 * {@link flush()} to ensure that Cairo finishes all operations on the device
 * and resets it to a clean state.
 * <p>
 * Cairo also provides the functions {@link #acquire()} and {@link #release()}
 * to synchronize access to the rendering system in a multithreaded environment.
 * This is done internally, but can also be used by applications.
 * <p>
 * <strong>Please refer to the documentation of each backend for additional
 * usage requirements, guarantees provided, and interactions with existing
 * surface API of the device functions for surfaces of that type.</strong>
 * 
 * @since 1.10
 */
public class Device extends ProxyInstance {

	{
		Interop.ensureInitialized();
	}

	/**
	 * Constructor used internally to instantiate a java Device object for a native
	 * {@code cairo_device_t} instance
	 * 
	 * @param address the memory address of the native {@code cairo_device_t}
	 *                instance
	 */
	public Device(MemorySegment address) {
		super(address);
		setDestroyFunc("cairo_device_destroy");
	}

	/**
	 * Checks whether an error has previously occurred for this device.
	 * 
	 * @return {@link Status#SUCCESS} on success or an error code if the device is
	 *         in an error state.
	 * @since 1.10
	 */
	public Status status() {
		try {
			int result = (int) cairo_device_status.invoke(handle());
			return Status.of(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_device_status = Interop.downcallHandle("cairo_device_status",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * This function finishes the device and drops all references to external
	 * resources. All surfaces, fonts and other objects created for this device will
	 * be finished, too. Further operations on the device will not affect the device
	 * but will instead trigger a {@link Status#DEVICE_FINISHED} error.
	 * <p>
	 * When the last call to {@link #destroy()} decreases the reference count to
	 * zero, cairo will call {@link #finish()} if it hasn't been called already,
	 * before freeing the resources associated with the device.
	 * <p>
	 * This function may acquire devices.
	 * 
	 * @since 1.10
	 */
	public void finish() {
		try {
			cairo_device_finish.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_device_finish = Interop.downcallHandle("cairo_device_finish",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * Finish any pending operations for the device and also restore any temporary
	 * modifications cairo has made to the device's state. This function must be
	 * called before switching from using the device with Cairo to operating on it
	 * directly with native APIs. If the device doesn't support direct access, then
	 * this function does nothing.
	 * <p>
	 * This function may acquire devices.
	 * 
	 * @since 1.10
	 */
	public void flush() {
		try {
			cairo_device_flush.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_device_flush = Interop.downcallHandle("cairo_device_flush",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	/**
	 * This function returns the type of the device. See {@link DeviceType} for
	 * available types.
	 * 
	 * @return the type of the device
	 * @since 1.10
	 */
	public DeviceType getType() {
		try {
			int result = (int) cairo_device_get_type.invoke(handle());
			return DeviceType.of(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_device_get_type = Interop.downcallHandle("cairo_device_get_type",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Acquires the device for the current thread. This function will block until no
	 * other thread has acquired the device. From then on, your thread owns the
	 * device and no other thread will be able to acquire it until a matching call
	 * to cairo_device_release(). It is allowed to recursively acquire the device
	 * multiple times from the same thread.
	 * <p>
	 * <strong>You must never acquire two different devices at the same time unless
	 * this is explicitly allowed. Otherwise the possibility of deadlocks exist. As
	 * various Cairo functions can acquire devices when called, these functions may
	 * also cause deadlocks when you call them with an acquired device. So you must
	 * not have a device acquired when calling them. These functions are marked in
	 * the documentation.</strong>
	 * <p>
	 * After a successful call to {@link #acquire()}, a matching call to
	 * {@link #release()} is required.
	 * 
	 * @throws IllegalStateException if the device is in an error state and could
	 *                               not be acquired.
	 * @since 1.10
	 */
	public void acquire() throws IllegalStateException {
		Status status = null;
		try {
			int result = (int) cairo_device_acquire.invoke(handle());
			status = Status.of(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			if (status != Status.SUCCESS) {
				throw new IllegalStateException(status.toString());
			}
		}
	}

	private static final MethodHandle cairo_device_acquire = Interop.downcallHandle("cairo_device_acquire",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS), false);

	/**
	 * Releases a device previously acquired using {@link #acquire()}. See that
	 * function for details.
	 * 
	 * @since 1.10
	 */
	public void release() {
		try {
			cairo_device_release.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_device_release = Interop.downcallHandle("cairo_device_release",
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false);

	public double observerElapsed() {
		try {
			return (double) cairo_device_observer_elapsed.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_device_observer_elapsed = Interop.downcallHandle(
			"cairo_device_observer_elapsed", FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS),
			false);

	public double observerFillElapsed() {
		try {
			return (double) cairo_device_observer_fill_elapsed.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_device_observer_fill_elapsed = Interop.downcallHandle(
			"cairo_device_observer_fill_elapsed", FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS),
			false);

	public double observerGlyphsElapsed() {
		try {
			return (double) cairo_device_observer_glyphs_elapsed.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_device_observer_glyphs_elapsed = Interop.downcallHandle(
			"cairo_device_observer_glyphs_elapsed", FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS),
			false);

	public double observerMaskElapsed() {
		try {
			return (double) cairo_device_observer_mask_elapsed.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_device_observer_mask_elapsed = Interop.downcallHandle(
			"cairo_device_observer_mask_elapsed", FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS),
			false);

	public double observerPaintElapsed() {
		try {
			return (double) cairo_device_observer_paint_elapsed.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_device_observer_paint_elapsed = Interop.downcallHandle(
			"cairo_device_observer_paint_elapsed", FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS),
			false);

	public void observerPrint(OutputStream stream) throws IllegalArgumentException, IOException {
		Status status = null;
		try {
			try (Arena arena = Arena.openConfined()) {
				WriteFunc writeFunc = stream::write;
				int result = (int) cairo_device_observer_print.invoke(handle(), writeFunc.toCallback(arena.scope()),
						MemorySegment.NULL);
				status = Status.of(result);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			if (status == Status.NO_MEMORY) {
				throw new RuntimeException(status.toString());
			}
			if (status != Status.SUCCESS) {
				throw new IOException(status.toString());
			}
		}
	}

	private static final MethodHandle cairo_device_observer_print = Interop.downcallHandle(
			"cairo_device_observer_print",
			FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS),
			false);

	public double observerStrokeElapsed() {
		try {
			return (double) cairo_device_observer_stroke_elapsed.invoke(handle());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_device_observer_stroke_elapsed = Interop.downcallHandle(
			"cairo_device_observer_stroke_elapsed", FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS),
			false);
}