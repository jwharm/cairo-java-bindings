package org.freedesktop.cairo;

/**
 * The PathElement interface is a sealed type that permits four records:
 * 
 * <ul>
 * <li>{@link PathElement.MoveTo} that corresponds with {@link Context#moveTo(double, double)}
 * <li>{@link PathElement.LineTo} that corresponds with {@link Context#lineTo(double, double)}
 * <li>{@link PathElement.CurveTo} that corresponds with {@link Context#curveTo(double, double, double, double, double, double)}
 * <li>{@link PathElement.ClosePath} that corresponds with {@link Context#closePath()}
 * </ul>
 * 
 * See {@link Path} and for more information about working with paths.
 */
public sealed interface PathElement permits PathElement.MoveTo, PathElement.LineTo, PathElement.CurveTo, PathElement.ClosePath {
	
	/**
	 * A {@link PathDataType#MOVE_TO} path element
	 */
    record MoveTo(double x, double y) implements PathElement {}
    
    /**
     * A {@link PathDataType#LINE_TO} path element
     */
    record LineTo(double x, double y) implements PathElement {}
    
    /**
     * A {@link PathDataType#CURVE_TO} path element
     */
    record CurveTo(double x1, double y1, double x2, double y2, double x3, double y3) implements PathElement {}
    
    /**
     * A {@link PathDataType#CLOSE_PATH} path element
     */
    record ClosePath() implements PathElement {}
}
