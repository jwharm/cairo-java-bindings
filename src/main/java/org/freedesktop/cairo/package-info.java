/* cairo-java-bindings - Java language bindings for cairo
 * Copyright (C) 2023 Jan-Willem Harmannij
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

/**
 * This package contains Java language bindings for the <a href="https://www.cairographics.org">cairo</a>
 * graphics library using the JEP-434 Panama FFI. The bindings are based on <strong>cairo 1.16</strong>
 * and work with <strong>JDK 20</strong> (with preview features enabled).
 * <p>
 * These language bindings were primarily created as a companion to the GObject-based language
 * bindings for Gtk and GStreamer generated with
 * <a href="https://github.com/jwharm/java-gi">Java-GI</a>, but they can also be used independently.
 * There are no external dependencies.
 * <h2>Overview</h2>
 * <h3>Java API</h3>
 * In general, the Java bindings match the cairo C API, but with a Java "coding style". C structs like
 * {@code cairo_t}, {@code cairo_surface_t} and {@code cairo_matrix_t} are modeled with Java {@code Proxy}
 * classes like {@link Context}, {@link Surface} and {@link Matrix}, and all flags and enumerations are
 * available as Java enums. The proxy classes inherit when applicable: {@link RadialGradient} extends
 * {@link Gradient}, which extends {@link Pattern}, and {@link ImageSurface} extends {@link Surface}. Types,
 * functions and parameters follow Java (camel case) naming practices, so
 * {@code cairo_move_to(*cr, x, y)} becomes {@code cr.moveTo(x, y)}. Out-parameters in the C API
 * are mapped to return values in Java. Multiple out parameters (like coordinates) are mapped to a
 * {@link Point} or {@link Rectangle} return type in Java.
 * <h3>Resource allocation and disposal</h3>
 * Resources are allocated and deallocated automatically, so there is no need to manually dispose
 * cairo resources in Java. However, please be aware that the disposal of proxy objects (like Context,
 * surfaces, matrices and patterns) is initiated by the Java garbage collector, which does not know
 * about the native resources, and might wait an indefinite amount of time before the objects are
 * effectively disposed. Therefore, manual calls to {@code destroy()} are still possible in case the
 * normal cleanup during GC is not sufficient to prevent resource exhaustion.
 * <h3>Error handling</h3>
 * Cairo status codes are checked in the language binding, and throw exceptions
 * (IllegalStateException, IllegalArgumentException or IOException) with the detailed status
 * description (from {@code cairo_status_to_string()}). The exceptions are documented in the
 * Javadoc, except for the {@code CAIRO_STATUS_NO_MEMORY} status, which is not documented and will
 * throw a RuntimeException if it occurs. If your application consumes a lot of memory, add try-catch
 * blocks for this situation where applicable.
 * <h3>Other notable features</h3>
 * Some other features that the language bindings offer:
 * <ul>
 * <li>In the {@link Context}, {@link Surface} and {@link Pattern} classes (like {@link Mesh}), methods
 *     that return {@code void} in the C API, return {@code this} in Java, to allow method chaining.
 *
 * <li>The {@link Path} class is iterable, and path traversal is implemented with {@link PathElement}
 *     objects. The {@link PathElement} type is a sealed interface implemented by a record type for every
 *     path operation. They can be iterated and processed with record patterns (JEP 440). See the
 *     {@link Path} class javadoc for example code.
 *
 * <li>The {@code cairo_set_user_data()} and {@code cairo_get_user_data()} functions (to attach
 *     custom data to a cairo struct) are available in Java, with a twist. You can call
 *     {@code setUserData()} to attach any Java object instance, and {@code getUserData()} to get it
 *     back. Objects that can be marshaled to a native memory segment (primitive types, memory segments,
 *     and other {@code Proxy} objects) will be attached to the native cairo struct. Other types will
 *     only be attached to the Java object and will not be passed to cairo itself.
 *
 * <li>I/O operations in cairo that are designed to work with streams accept Java {@link java.io.InputStream}
 *     and {@link java.io.OutputStream} parameters.
 *
 * <li>The {@link Surface} and {@link Device} classes implement {@link AutoCloseable} and can be used in
 *     try-with-resources blocks. (The {@code close()} method calls the C {@code cairo_..._finish()}
 *     function.)
 *
 * <li>The cairo Script surface has been split into a {@link Script} class that inherits from
 *     {@link Device}, and a {@link ScriptSurface} class that inherits from {@link Surface}.
 *
 * <li>The functions for reading and comparing cairo version information are available in Java as static
 *     methods in the {@link Cairo} class.
 * </ul>
 * <h2>API Documentation</h2>
 * All API documentation is available as Javadoc, and has been reworked to use Javadoc syntax and
 * cross-reference between Java classes and methods. You can lookup the Javadoc online, or download
 * the javadoc or sources jar to use in your IDE.
 * <h2>License</h2>
 * The bindings are available to be redistributed and/or modified under the terms of  the GNU Lesser
 * General Public License (LGPL) version 2.1 (which is also one of the licenses of cairo itself.)
 * <h2>Status</h2>
 * This software is still a work in progress:
 * <ul>
 * <li>Platform-specific surface types and font implementations are not (yet) available in the bindings.
 * <li>Test coverage is not complete yet.
 * <li>The bindings should work on all platforms that support JDK 20 and cairo, but have mostly been
 *     tested on Linux for now.
 * </ul>
 * <p>
 * PRs and issue reports are welcome.
 */
package org.freedesktop.cairo;