/* cairo-java-bindings - Java language bindings for cairo
 * Copyright (C) 2023-2024 Jan-Willem Harmannij
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

import io.github.jwharm.cairobindings.Interop;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * SurfaceType is used to describe the type of a given surface. The surface
 * types are also known as "backends" or "surface backends" within cairo.
 * <p>
 * The type of a surface is determined by the function used to create it, which
 * will generally be of the form of a {@code create() function}, (though see
 * {@link Surface#createSimilar(Surface, Content, int, int)} as well).
 * <p>
 * The surface type can be queried with {@link Surface#getType()}
 * <p>
 * The various {@link Surface} functions can be used with surfaces of any type,
 * but some backends also provide type-specific methods that are only available
 * on a surface of the appropriate class.
 * <p>
 * New entries may be added in future versions.
 * 
 * @since 1.2
 */
public enum SurfaceType {

    /**
     * The surface is of type image
     * 
     * @since 1.2
     */
    IMAGE,

    /**
     * The surface is of type pdf
     * 
     * @since 1.2
     */
    PDF,

    /**
     * The surface is of type ps
     * 
     * @since 1.2
     */
    PS,

    /**
     * The surface is of type xlib
     * 
     * @since 1.2
     */
    XLIB,

    /**
     * The surface is of type xcb
     * 
     * @since 1.2
     */
    XCB,

    /**
     * The surface is of type glitz
     * 
     * @since 1.2
     * @deprecated 1.18 (glitz support have been removed, this surface type will
     *             never be set by cairo)
     */
    @Deprecated
    GLITZ,

    /**
     * The surface is of type quartz
     * 
     * @since 1.2
     */
    QUARTZ,

    /**
     * The surface is of type win32
     * 
     * @since 1.2
     */
    WIN32,

    /**
     * The surface is of type beos
     * 
     * @since 1.2
     * @deprecated 1.18 (beos support have been removed, this surface type will
     *             never be set by cairo)
     */
    @Deprecated
    BEOS,

    /**
     * The surface is of type directfb
     * 
     * @since 1.2
     * @deprecated 1.18 (directfb support have been removed, this surface type will
     *             never be set by cairo)
     */
    @Deprecated
    DIRECTFB,

    /**
     * The surface is of type svg
     * 
     * @since 1.2
     */
    SVG,

    /**
     * The surface is of type os2
     * 
     * @since 1.4
     * @deprecated 1.18 (os2 support have been removed, this surface type will
     *             never be set by cairo)
     */
    @Deprecated
    OS2,

    /**
     * The surface is a win32 printing surface
     * 
     * @since 1.6
     */
    WIN32_PRINTING,

    /**
     * The surface is of type quartz_image
     * 
     * @since 1.6
     */
    QUARTZ_IMAGE,

    /**
     * The surface is of type script
     * 
     * @since 1.10
     */
    SCRIPT,

    /**
     * The surface is of type Qt
     * 
     * @since 1.10
     * @deprecated 1.18 (Qt support have been removed, this surface type will
     *             never be set by cairo)
     */
    @Deprecated
    QT,

    /**
     * The surface is of type recording
     * 
     * @since 1.10
     */
    RECORDING,

    /**
     * The surface is a OpenVG surface
     * 
     * @since 1.10
     * @deprecated 1.18 (OpenVG support have been removed, this surface type will
     *             never be set by cairo)
     */
    @Deprecated
    VG,

    /**
     * The surface is of type OpenGL
     * 
     * @since 1.10
     * @deprecated 1.18 (OpenGL support have been removed, this surface type will
     *             never be set by cairo)
     */
    @Deprecated
    GL,

    /**
     * The surface is of type Direct Render Manager
     * 
     * @since 1.10
     * @deprecated 1.18 (DRM support have been removed, this surface type will
     *             never be set by cairo)
     */
    @Deprecated
    DRM,

    /**
     * The surface is of type 'tee' (a multiplexing surface)
     * 
     * @since 1.10
     */
    TEE,

    /**
     * The surface is of type XML (for debugging)
     * 
     * @since 1.10
     */
    XML,

    /**
     * The surface is of type Skia
     *
     * @since 1.10
     * @deprecated 1.18 (Skia support have been removed, this surface type will
     *             never be set by cairo)
     */
    @Deprecated
    SKIA,

    /**
     * The surface is a subsurface created with {@link Surface#createForRectangle}
     * 
     * @since 1.10
     */
    SUBSURFACE,

    /**
     * This surface is of type Cogl
     * 
     * @since 1.12
     * @deprecated 1.18 (Cogl support have been removed, this surface type will
     *             never be set by cairo)
     */
    @Deprecated
    COGL;

    static {
        Cairo.ensureInitialized();
    }

    /**
     * Return the value of this enum
     * @return the value
     */
    public int getValue() {
        return ordinal();
    }

    /**
     * Returns the enum constant for the given ordinal (its position in the enum
     * declaration).
     * 
     * @param ordinal the position in the enum declaration, starting from zero
     * @return the enum constant for the given ordinal
     */
    public static SurfaceType of(int ordinal) {
        return values()[ordinal];
    }

    /**
     * Get the CairoSurfaceType GType
     * @return the GType
     */
    public static org.gnome.gobject.Type getType() {
        try {
            long result = (long) cairo_gobject_surface_type_get_type.invoke();
            return new org.gnome.gobject.Type(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_gobject_surface_type_get_type = Interop.downcallHandle(
            "cairo_gobject_surface_type_get_type", FunctionDescriptor.of(ValueLayout.JAVA_LONG));
}
