package org.freedesktop.cairo;

/**
 * A color with red, green, blue and alpha components
 *
 * @param red   red component of color
 * @param green green component of color
 * @param blue  blue component of color
 * @param alpha alpha component of color
 * @since 1.18
 */
public record RGBA(double red, double green, double blue, double alpha) {
}
