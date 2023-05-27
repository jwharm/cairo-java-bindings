package org.freedesktop.cairo.drawing;

/**
 * The dash pattern to be used by cairo_stroke(). A dash pattern is specified by
 * dashes, an array of positive values. Each value provides the length of
 * alternate "on" and "off" portions of the stroke. The offset specifies an
 * offset into the pattern at which the stroke begins.
 * <p>
 * Each "on" segment will have caps applied as if the segment were a separate
 * sub-path. In particular, it is valid to use an "on" length of 0.0 with
 * CAIRO_LINE_CAP_ROUND or CAIRO_LINE_CAP_SQUARE in order to distributed dots or
 * squares along a path.
 * <p>
 * Note: The length values are in user-space units as evaluated at the time of
 * stroking. This is not necessarily the same as the user space at the time of
 * cairo_set_dash().
 * <p>
 * If {@code dashes.length} is 0 dashing is disabled.
 * <p>
 * If {@code dashes.length} is 1 a symmetric pattern is assumed with alternating
 * on and off portions of the size specified by the single value in dashes.
 * <p>
 * If any value in dashes is negative, or if all values are 0, then the Context
 * will be put into an error state with a status of CAIRO_STATUS_INVALID_DASH.
 * 
 * @param dashes an array specifying alternate lengths of on and off stroke
 *               portions
 * @param offset an offset into the dash pattern at which the stroke should
 *               start
 */
public record Dash(double[] dashes, double offset) {
}
