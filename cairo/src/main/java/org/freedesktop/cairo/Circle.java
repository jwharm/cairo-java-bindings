package org.freedesktop.cairo;

/**
 * A circle defined by the x and y coordinates of the center and the radius.
 *
 * @param x x coordinate of the center
 * @param y y coordinate of the center
 * @param radius radius of the circle
 */
public record Circle(double x, double y, double radius) {
}
