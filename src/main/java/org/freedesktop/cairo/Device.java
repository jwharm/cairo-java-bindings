/* cairo-java-bindings - Java language bindings for cairo
 * Copyright (C) 2024 Jan-Willem Harmannij
 *
 * SPDX-License-Identifier: LGPL-2.1-or-later
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://www.gnu.org/licenses/>.
 */

package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Proxy;
import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.MemoryCleaner;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.ref.Cleaner;

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
 * {@link #flush()} to ensure that Cairo finishes all operations on the device
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
public class Device extends Proxy implements AutoCloseable {

    static {
        Cairo.ensureInitialized();
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
        MemoryCleaner.setFreeFunc(handle(), "cairo_device_destroy");
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
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * This function finishes the device and drops all references to external
     * resources. All surfaces, fonts and other objects created for this device will
     * be finished, too. Further operations on the device will not affect the device
     * but will instead trigger a {@link Status#DEVICE_FINISHED} error.
     * <p>
     * When the last call to {@link #destroy()} decreases the reference count to
     * zero, cairo will call {@code finish()} if it hasn't been called already,
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
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

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
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

    /**
     * This function returns the type of the device. See {@link DeviceType} for
     * available types.
     *
     * @return the type of the device
     * @since 1.10
     */
    public DeviceType getDeviceType() {
        try {
            int result = (int) cairo_device_get_type.invoke(handle());
            return DeviceType.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_device_get_type = Interop.downcallHandle("cairo_device_get_type",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Acquires the device for the current thread. This function will block until no
     * other thread has acquired the device. From then on, your thread owns the
     * device and no other thread will be able to acquire it until a matching call
     * to {@link #release()}. It is allowed to recursively acquire the device
     * multiple times from the same thread.
     * <p>
     * <strong>You must never acquire two different devices at the same time unless
     * this is explicitly allowed. Otherwise the possibility of deadlocks exist. As
     * various Cairo functions can acquire devices when called, these functions may
     * also cause deadlocks when you call them with an acquired device. So you must
     * not have a device acquired when calling them. These functions are marked in
     * the documentation.</strong>
     * <p>
     * After a successful call to {@code acquire()}, a matching call to
     * {@link #release()} is required.
     * 
     * @throws IllegalStateException if the device is in an error state and could
     *                               not be acquired.
     * @since 1.10
     */
    public void acquire() throws IllegalStateException {
        Status status;
        try {
            int result = (int) cairo_device_acquire.invoke(handle());
            status = Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status != Status.SUCCESS) {
            throw new IllegalStateException(status.toString());
        }
    }

    private static final MethodHandle cairo_device_acquire = Interop.downcallHandle("cairo_device_acquire",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

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
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

    /**
     * Returns the total elapsed time of the observation.
     *
     * @return the elapsed time, in nanoseconds.
     * @since 1.12
     */
    public double observerElapsed() {
        try {
            return (double) cairo_device_observer_elapsed.invoke(handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_device_observer_elapsed = Interop.downcallHandle(
            "cairo_device_observer_elapsed", FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS));

    /**
     * Returns the elapsed time of the fill operations.
     *
     * @return the elapsed time, in nanoseconds.
     * @since 1.12
     */
    public double observerFillElapsed() {
        try {
            return (double) cairo_device_observer_fill_elapsed.invoke(handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_device_observer_fill_elapsed = Interop.downcallHandle(
            "cairo_device_observer_fill_elapsed", FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS));

    /**
     * Returns the elapsed time of the glyph operations.
     *
     * @return the elapsed time, in nanoseconds.
     * @since 1.12
     */
    public double observerGlyphsElapsed() {
        try {
            return (double) cairo_device_observer_glyphs_elapsed.invoke(handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_device_observer_glyphs_elapsed = Interop.downcallHandle(
            "cairo_device_observer_glyphs_elapsed", FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS));

    /**
     * Returns the elapsed time of the mask operations.
     *
     * @return the elapsed time, in nanoseconds
     * @since 1.12
     */
    public double observerMaskElapsed() {
        try {
            return (double) cairo_device_observer_mask_elapsed.invoke(handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_device_observer_mask_elapsed = Interop.downcallHandle(
            "cairo_device_observer_mask_elapsed", FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS));

    /**
     * Returns the elapsed time of the paint operations.
     *
     * @return the elapsed time, in nanoseconds
     * @since 1.12
     */
    public double observerPaintElapsed() {
        try {
            return (double) cairo_device_observer_paint_elapsed.invoke(handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_device_observer_paint_elapsed = Interop.downcallHandle(
            "cairo_device_observer_paint_elapsed", FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS));

    /**
     * Prints the device log using the given OutputStream.
     *
     * @param stream the OutputStream
     * @throws IOException an unexpected error occured whil printing the device log
     * @since 1.12
     */
    public void observerPrint(OutputStream stream) throws IOException {
        if (stream == null) {
            return;
        }
        Status status;
        try {
            try (Arena arena = Arena.ofConfined()) {
                WriteFunc writeFunc = stream::write;
                int result = (int) cairo_device_observer_print.invoke(handle(), writeFunc.toCallback(arena),
                        MemorySegment.NULL);
                status = Status.of(result);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
        if (status != Status.SUCCESS) {
            throw new IOException(status.toString());
        }
    }

    private static final MethodHandle cairo_device_observer_print = Interop.downcallHandle(
            "cairo_device_observer_print",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Returns the elapsed time of the stroke operations.
     *
     * @return the elapsed time, in nanoseconds.
     * @since 1.12
     */
    public double observerStrokeElapsed() {
        try {
            return (double) cairo_device_observer_stroke_elapsed.invoke(handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_device_observer_stroke_elapsed = Interop.downcallHandle(
            "cairo_device_observer_stroke_elapsed", FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ValueLayout.ADDRESS));

    /**
     * Attach user data to the device. To remove user data from a device, call
     * this function with the key that was used to set it and {@code null} for
     * {@code userData}.
     *
     * @param  key      the key to attach the user data to
     * @param  userData the user data to attach to the device
     * @return the key
     * @throws NullPointerException if {@code key} is {@code null}
     * @since 1.4
     */
    public UserDataKey setUserData(UserDataKey key, MemorySegment userData) {
        Status status;
        try {
            int result = (int) cairo_device_set_user_data.invoke(handle(), key.handle(), userData, MemorySegment.NULL);
            status = Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
        return key;
    }

    private static final MethodHandle cairo_device_set_user_data = Interop.downcallHandle("cairo_device_set_user_data",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS));

    /**
     * Return user data previously attached to the device using the specified key.
     * If no user data has been attached with the given key this function returns
     * {@code null}.
     * <p>
     * The returned memory segment has zero length. It can be resized with
     * {@link MemorySegment#reinterpret(long)}.
     *
     * @param  key the UserDataKey the user data was attached to
     * @return the user data previously attached or {@code null}
     * @since 1.4
     */
    public MemorySegment getUserData(UserDataKey key) {
        if (key == null) {
            return null;
        }
        try {
            MemorySegment result = (MemorySegment) cairo_device_get_user_data.invoke(handle(), key.handle());
            return MemorySegment.NULL.equals(result) ? null : result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_device_get_user_data = Interop.downcallHandle("cairo_device_get_user_data",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Closing a device will invoke {@link #finish()}, which will flush the
     * device and drop all references to external resources. A closed device
     * cannot be used to perform drawing operations and cannot be reopened.
     * <p>
     * Although the Java bindings make an effort to properly dispose native
     * resources using a {@link Cleaner}, this is not guaranteed to work in
     * all situations. Users must therefore always call {@code close()} or
     * {@code finish()} on devices (either manually or with a
     * try-with-resources statement) or risk issues like resource exhaustion
     * and data loss.
     */
    @Override
    public void close() {
        finish();
    }

    /**
     * Get the CairoDevice GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_device_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_device_get_type = Interop.downcallHandle(
            "cairo_gobject_device_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
