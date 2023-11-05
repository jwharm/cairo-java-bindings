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

package org.freedesktop.cairo;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * {@code UserScaledFontInitFunc} is the type of function which is called when a
 * scaled-font needs to be created for a user font-face.
 * <p>
 * The cairo context {@code cr} is not used by the caller, but is prepared in font
 * space, similar to what the cairo contexts passed to the renderGlyph method will
 * look like. The callback can use this context for extents computation for example.
 * After the callback is called, {@code cr} is checked for any error status.
 * <p>
 * The {@code extents} argument is where the user font sets the font extents for
 * {@code scaledFont}. It is in font space, which means that for most cases its
 * ascent and descent members should add to 1.0. {@code extents} is preset to hold a
 * value of 1.0 for ascent, height, and maxXAdvance, and 0.0 for descent and
 * maxYAdvance members.
 * <p>
 * The callback is optional. If not set, default font extents as described in the
 * previous paragraph will be used.
 * <p>
 * Note that {@code scaledFont} is not fully initialized at this point and trying to
 * use it for text operations in the callback will result in deadlock.
 *
 * @since 1.8
 */
@FunctionalInterface
public interface UserScaledFontInitFunc {

    /**
     * Called when a scaled-font needs to be created for a user font-face.
     *
     * @param scaledFont the scaled-font being created
     * @param cr         a cairo context, in font space
     * @param extents    font extents to fill in, in font space
     * @throws Exception when an error occurs. Throwing an exception will trigger a
     *                   {@link Status#USER_FONT_ERROR} return value to native
     *                   code.
     * @since 1.8
     */
    void init(UserScaledFont scaledFont, Context cr, FontExtents extents) throws Exception;

    /**
     * The callback that is executed by native code. This method marshals the
     * parameters and calls {@link #init}.
     *
     * @param scaledFont the scaled-font being created
     * @param cr         a cairo context, in font space
     * @param extents    font extents to fill in, in font space
     * @return {@link Status#SUCCESS} upon success, or
     *         {@link Status#USER_FONT_ERROR} if an exception was thrown.
     * @since 1.8
     */
    default int upcall(MemorySegment scaledFont, MemorySegment cr, MemorySegment extents) {
        try {
            init(new UserScaledFont(scaledFont), new Context(cr), new FontExtents(extents));
            return Status.SUCCESS.getValue();
        } catch (Exception e) {
            return Status.USER_FONT_ERROR.getValue();
        }
    }

    /**
     * Generates an upcall stub, a C function pointer that will call
     * {@link #upcall}.
     *
     * @param arena the arena in which the upcall stub will be allocated
     * @return the function pointer of the upcall stub
     * @since 1.8
     */
    default MemorySegment toCallback(Arena arena) {
        try {
            FunctionDescriptor fdesc = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS);
            MethodHandle handle = MethodHandles.lookup().findVirtual(
                    UserScaledFontInitFunc.class, "upcall", fdesc.toMethodType());
            return Linker.nativeLinker().upcallStub(handle.bindTo(this), fdesc, arena);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
