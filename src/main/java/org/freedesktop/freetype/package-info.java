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
