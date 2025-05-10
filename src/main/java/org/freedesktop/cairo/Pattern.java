/* cairo-java-bindings - Java language bindings for cairo
 * Copyright (C) 2024-2025 Jan-Willem Harmannij
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

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * Sources for drawing.
 * <p>
 * A {@link Pattern} represents a source when drawing onto a surface. There are
 * different subtypes of Pattern, for different types of sources; for example,
 * cairo_pattern_create_rgb() creates a pattern for a solid opaque color.
 * <p>
 * Other than various cairo_pattern_create_type() functions, some of the pattern
 * types can be implicitly created using various cairo_set_source_type()
 * functions; for example cairo_set_source_rgb().
 * <p>
 * The type of a pattern can be queried with cairo_pattern_get_type().
 * <p>
 * Memory management of cairo_pattern_t is done with cairo_pattern_reference()
 * and cairo_pattern_destroy().
 * 
 * @since 1.0
 */
public class Pattern extends Proxy {

    static {
        Cairo.ensureInitialized();
    }

    // Keep a reference to the matrix during the lifetime of the Pattern.
    Matrix matrix;

    /**
     * Constructor used internally to instantiate a java Pattern object for a native
     * {@code cairo_pattern_t} instance
     * 
     * @param address the memory address of the native {@code cairo_pattern_t}
     *                instance
     */
    public Pattern(MemorySegment address) {
        super(address);
        MemoryCleaner.setFreeFunc(handle(), "cairo_pattern_destroy");
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
     * Checks whether an error has previously occurred for this pattern.
     * 
     * @return {@link Status#SUCCESS}, {@link Status#NO_MEMORY},
     *         {@link Status#INVALID_MATRIX}, {@link Status#PATTERN_TYPE_MISMATCH},
     *         or {@link Status#INVALID_MESH_CONSTRUCTION}.
     * @since 1.0
     */
    public Status status() {
        try {
            int result = (int) cairo_pattern_status.invoke(handle());
            return Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_status = Interop.downcallHandle("cairo_pattern_status",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Sets the mode to be used for drawing outside the area of a pattern. See
     * {@link Extend} for details on the semantics of each extend strategy.
     * <p>
     * The default extend mode is {@link Extend#NONE} for surface patterns and
     * {@link Extend#PAD} for gradient patterns.
     * 
     * @param extend a {@link Extend} describing how the area outside of the pattern
     *               will be drawn
     * @since 1.0
     */
    public void setExtend(Extend extend) {
        try {
            cairo_pattern_set_extend.invoke(handle(), extend.getValue());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_set_extend = Interop.downcallHandle("cairo_pattern_set_extend",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Gets the current extend mode for a pattern. See {@link Extend} for details on
     * the semantics of each extend strategy.
     * 
     * @return the current extend strategy used for drawing the pattern.
     * @since 1.0
     */
    public Extend getExtend() {
        try {
            int result = (int) cairo_pattern_get_extend.invoke(handle());
            return Extend.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_get_extend = Interop.downcallHandle("cairo_pattern_get_extend",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Sets the filter to be used for resizing when using this pattern. See
     * {@link Filter} for details on each filter.
     * <p>
     * Note that you might want to control filtering even when you do not have an
     * explicit {@link Pattern} object, (for example when using
     * {@link Context#setSource(Surface, double, double)}). In these cases, it is
     * convenient to use {@link Context#getSource()} to get access to the pattern
     * that cairo creates implicitly. For example:
     * 
     * <pre>
     * cr.setSourceSurface(image, x, y);
     * pattern.setFilter(cr.getSource(), Filter.NEAREST);
     * </pre>
     * 
     * @param filter a {@link Filter} describing the filter to use for resizing the
     *               pattern
     * @since 1.0
     */
    public void setFilter(Filter filter) {
        try {
            cairo_pattern_set_filter.invoke(handle(), filter.getValue());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_set_filter = Interop.downcallHandle("cairo_pattern_set_filter",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Gets the current filter for a pattern. See {@link Filter} for details on each
     * filter.
     * 
     * @return the current filter used for resizing the pattern.
     * @since 1.0
     */
    public Filter getFilter() {
        try {
            int result = (int) cairo_pattern_get_filter.invoke(handle());
            return Filter.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_get_filter = Interop.downcallHandle("cairo_pattern_get_filter",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Sets the pattern's transformation matrix to {@code matrix}. This matrix is a
     * transformation from user space to pattern space.
     * <p>
     * When a pattern is first created it always has the identity matrix for its
     * transformation matrix, which means that pattern space is initially identical
     * to user space.
     * <p>
     * Important: Please note that the direction of this transformation matrix is
     * from user space to pattern space. This means that if you imagine the flow
     * from a pattern to user space (and on to device space), then coordinates in
     * that flow will be transformed by the inverse of the pattern matrix.
     * <p>
     * For example, if you want to make a pattern appear twice as large as it does
     * by default the correct code to use is:
     * 
     * <pre>
     * Matrix matrix = Matrix.createScale(0.5, 0.5);
     * pattern.setMatrix(matrix);
     * </pre>
     * 
     * Meanwhile, using values of 2.0 rather than 0.5 in the code above would cause
     * the pattern to appear at half of its default size.
     * <p>
     * Also, please note the discussion of the user-space locking semantics of
     * {@link Context#setSource(Pattern)}.
     * 
     * @param matrix a {@link Matrix}
     * @since 1.0
     */
    public void setMatrix(Matrix matrix) {
        try {
            cairo_pattern_set_matrix.invoke(handle(), matrix == null ? MemorySegment.NULL : matrix.handle());
            this.matrix = matrix;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_set_matrix = Interop.downcallHandle("cairo_pattern_set_matrix",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Stores the pattern's transformation matrix into {@code matrix}.
     * 
     * @param matrix return value for the matrix
     * @since 1.0
     */
    public void getMatrix(Matrix matrix) {
        try {
            cairo_pattern_get_matrix.invoke(handle(), matrix == null ? MemorySegment.NULL : matrix.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_get_matrix = Interop.downcallHandle("cairo_pattern_get_matrix",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Get the pattern's type. See {@link PatternType} for available types.
     * 
     * @return The type of pattern.
     * @since 1.2
     */
    public PatternType getPatternType() {
        try {
            int result = (int) cairo_pattern_get_type.invoke(handle());
            return PatternType.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_get_type = Interop.downcallHandle("cairo_pattern_get_type",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Attach user data to the pattern. To remove user data from a pattern, call
     * this function with the key that was used to set it and {@code null} for
     * {@code userData}.
     *
     * @param  key      the key to attach the user data to
     * @param  userData the user data to attach to the pattern
     * @return the key
     * @throws NullPointerException if {@code key} is {@code null}
     * @since 1.4
     */
    public UserDataKey setUserData(UserDataKey key, MemorySegment userData) {
        Status status;
        try {
            int result = (int) cairo_pattern_set_user_data.invoke(handle(), key.handle(), userData, MemorySegment.NULL);
            status = Status.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (status == Status.NO_MEMORY) {
            throw new RuntimeException(status.toString());
        }
        return key;
    }

    private static final MethodHandle cairo_pattern_set_user_data = Interop.downcallHandle("cairo_pattern_set_user_data",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS));

    /**
     * Return user data previously attached to the pattern using the specified key.
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
            MemorySegment result = (MemorySegment) cairo_pattern_get_user_data.invoke(handle(), key.handle());
            return MemorySegment.NULL.equals(result) ? null : result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_get_user_data = Interop.downcallHandle("cairo_pattern_get_user_data",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Set the dithering mode of the rasterizer used for drawing shapes. This value
     * is a hint, and a particular backend may or may not support a particular
     * value. At the current time, only pixman is supported.
     *
     * @param dither a {@link Dither} describing the new dithering mode
     * @since 1.18
     */
    public void setDither(Dither dither) {
        try {
            cairo_pattern_set_dither.invoke(handle(), dither.getValue());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_set_dither = Interop.downcallHandle("cairo_pattern_set_dither",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

    /**
     * Gets the current dithering mode, as set by {@link #setDither(Dither)}.
     *
     * @return the current dithering mode.
     * @since 1.18
     */
    public Dither getDither() {
        try {
            int result = (int) cairo_pattern_get_dither.invoke(handle());
            return Dither.of(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_pattern_get_dither = Interop.downcallHandle("cairo_pattern_get_dither",
            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

    /**
     * Get the CairoPattern GType
     * @return the GType
     */
    public static org.gnome.glib.Type getType() {
        try {
            long result = (long) cairo_gobject_pattern_get_type.invoke();
            return new org.gnome.glib.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_pattern_get_type = Interop.downcallHandle(
            "cairo_gobject_pattern_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
