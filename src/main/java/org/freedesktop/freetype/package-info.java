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

/**
 * Minimal wrapper for FreeType, for use with cairo FTFontFace and FTScaledFont.
 * <p>
 * To use this wrapper, make sure that FreeType is installed and in the Java library path.
 * 
 * <ul>
 * <li>{@code libfreetype.6.so} on Linux
 * <li>{@code freetype-6.dll} on Windows
 * <li>{@code libfreetype.6.dylib} on MacOS
 * </ul>
 * 
 * Example usage of the wrapper, using a Windows-style path to load {@code arial.ttf}:
 * 
 * <pre>{@code
 * Library ftLib = Library.initFreeType();
 * Face ftFace = new Face(ftLib, "C:\\Windows\\Fonts\\arial.ttf", 0);
 * 
 * // Create a font face for FreeType
 * FTFontFace face = FTFontFace.create(ftFace, 0);
 * 
 * // Create a scaled font from the FreeType font backend
 * FTScaledFont scaledFont = FTScaledFont.create(face, matrix, ctm, options);
 * }</pre>
 */
package org.freedesktop.freetype;
