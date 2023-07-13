package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.freedesktop.cairo.Context;
import org.freedesktop.cairo.Format;
import org.freedesktop.cairo.ImageSurface;
import org.freedesktop.cairo.Path;
import org.freedesktop.cairo.PathElement;
import org.junit.jupiter.api.Test;

public class PathTest {

    @Test
    public void testPath() throws Exception {
        Context cr = Context.create(ImageSurface.create(Format.ARGB32, 120, 120));
        
        cr.moveTo(10, 20);
        cr.lineTo(30, 40);
        cr.curveTo(50, 60, 70, 80, 90, 100);
        cr.closePath(); // also adds an explicit move_to to the path
        
        var result = new StringBuilder();
        
        Path path = cr.copyPath();
        for (PathElement element : path) {
            switch (element) {
                case PathElement.MoveTo(double x, double y) -> result.append(String.format("move %.0f %.0f", x, y));
                case PathElement.LineTo(double x, double y) -> result.append(String.format("line %.0f %.0f", x, y));
                case PathElement.CurveTo(double x1, double y1, double x2, double y2, double x3, double y3) -> 
                        result.append(String.format("curve %.0f %.0f %.0f %.0f %.0f %.0f", x1, y1, x2, y2, x3, y3));
                case PathElement.ClosePath() -> result.append(String.format("close"));
            }
        }
        
        assertEquals("move 10 20" + "line 30 40" + "curve 50 60 70 80 90 100" + "close" + "move 10 20",
                result.toString());
    }
}
