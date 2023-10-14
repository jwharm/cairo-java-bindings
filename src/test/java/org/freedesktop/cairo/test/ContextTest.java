package org.freedesktop.cairo.test;

import java.io.IOException;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContextTest {

    private Context createContext() {
        try {
            return Context.create(ImageSurface.create(Format.ARGB32, 120, 120));
        } catch (IOException ioe) {
            fail(ioe);
            throw new RuntimeException(ioe);
        }
    }

    private int getPathLength(Path path) {
        if (path == null)
            return 0;
        int count = 0;
        for (PathElement ignored : path)
            count++;
        return count;
    }

    @Test
    void testCreate() throws IOException {
        Context cr = Context.create(ImageSurface.create(Format.ARGB32, 120, 120));
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testStatus() {
        Context cr = createContext();
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testSaveRestore() {
        Context cr = createContext();
        cr.setLineCap(LineCap.ROUND)
          .save()
          .setLineCap(LineCap.SQUARE)
          .restore();
        assertEquals(LineCap.ROUND, cr.getLineCap());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testGetTarget() throws IOException {
        try (var surface = ImageSurface.create(Format.ARGB32, 120, 120)) {
            Context cr = Context.create(surface);
            assertNotNull(cr.getTarget());
            assertEquals(cr.getTarget().handle(), surface.handle());
            assertEquals(Status.SUCCESS, cr.status());
        }
    }

    @Test
    void testPushGroup() {
        Context cr = createContext();
        cr.pushGroup();
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testPushGroupWithContent() {
        Context cr = createContext();
        cr.pushGroupWithContent(Content.COLOR_ALPHA);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testPopGroup() {
        Context cr = createContext();
        cr.pushGroup();
        cr.popGroup();
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testPopGroupToSource() {
        Context cr = createContext();
        cr.pushGroup();
        cr.popGroupToSource();
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testGetGroupTarget() {
        Context cr = createContext();
        cr.getGroupTarget();
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testSetSourceRGB() {
        Context cr = createContext();
        cr.setSourceRGB(0, 0, 0);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testSetSourceRGBA() {
        Context cr = createContext();
        cr.setSourceRGBA(0, 0, 0, 0);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testSetSourcePattern() {
        Context cr = createContext();
        cr.setSource(SolidPattern.createRGB(0, 0, 0));
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testSetSourceSurfaceDoubleDouble() {
        Context cr = createContext();
        cr.setSource(ImageSurface.create(Format.ARGB32, 120, 120), 0, 0);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testGetSource() {
        Context cr = createContext();
        SurfacePattern source = cr.getSource();
        assertEquals(Status.SUCCESS, source.status());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testAntialias() {
        Context cr = createContext();
        cr.setAntialias(Antialias.GOOD);
        Antialias a = cr.getAntialias();
        assertEquals(Antialias.GOOD, a);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testDash() {
        Context cr = createContext();
        cr.setDash(new double[] { 4d, 5d, 3.5d, 0.001d }, 2);
        assertEquals(4, cr.getDashCount());
        double[] dash = cr.getDash();
        assertEquals(4, dash.length);
        assertEquals(4d, dash[0]);
        assertEquals(5d, dash[1]);
        assertEquals(3.5d, dash[2]);
        assertEquals(0.001d, dash[3]);
        assertEquals(2, cr.getDashOffset());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testFillRule() {
        Context cr = createContext();
        cr.setFillRule(FillRule.EVEN_ODD);
        assertEquals(FillRule.EVEN_ODD, cr.getFillRule());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testLineCap() {
        Context cr = createContext();
        cr.setLineCap(LineCap.SQUARE);
        assertEquals(LineCap.SQUARE, cr.getLineCap());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testLineJoin() {
        Context cr = createContext();
        cr.setLineJoin(LineJoin.ROUND);
        assertEquals(LineJoin.ROUND, cr.getLineJoin());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testLineWidth() {
        Context cr = createContext();
        cr.setLineWidth(3.5);
        assertEquals(3.5, cr.getLineWidth());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testMiterLimit() {
        Context cr = createContext();
        cr.setMiterLimit(13.05);
        assertEquals(13.05, cr.getMiterLimit());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testGetOperator() {
        Context cr = createContext();
        cr.setOperator(Operator.DEST_OVER);
        assertEquals(Operator.DEST_OVER, cr.getOperator());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testGetTolerance() {
        Context cr = createContext();
        cr.setTolerance(0.3);
        assertEquals(0.3, cr.getTolerance());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testClip() {
        Context cr = createContext();
        cr.clip();
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testClipPreserve() {
        Context cr = createContext();
        cr.clipPreserve();
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testClipExtents() {
        Context cr = createContext();
        cr.clipExtents();
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testInClip() {
        Context cr = createContext();
        assertTrue(cr.inClip(cr.getCurrentPoint().x(), cr.getCurrentPoint().y()));
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testResetClip() {
        Context cr = createContext();
        cr.resetClip();
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testCopyClipRectangleList() {
        Context cr = createContext();
        var rectangles = cr.copyClipRectangleList().rectangles();
        var rect = rectangles.get(0);
        assertEquals(1, rectangles.size());
        assertEquals(0d, rect.x());
        assertEquals(0d, rect.y());
        assertEquals(120d, rect.width());
        assertEquals(120d, rect.height());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testFill() {
        Context cr = createContext();
        cr.rectangle(10, 10, 10, 10);
        var path = cr.fill().copyPath();
        assertNotNull(path);
        assertFalse(path.iterator().hasNext()); // assert that path is cleared
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testFillPreserve() {
        Context cr = createContext();
        cr.rectangle(10, 10, 10, 10);
        var path = cr.fillPreserve().copyPath();
        assertNotNull(path);
        assertTrue(path.iterator().hasNext()); // assert that path is not cleared
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testFillExtents() {
        Context cr = createContext();
        var rect = cr.rectangle(5d, 10d, 10d, 10d).fillExtents();
        assertNotNull(rect);
        assertEquals(5d, rect.x());
        assertEquals(10d, rect.y());
        assertEquals(15d, rect.width());
        assertEquals(20d, rect.height());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testInFill() {
        Context cr = createContext();
        cr.rectangle(5d, 10d, 10d, 10d).fillExtents();
        assertFalse(cr.inFill(0, 2));
        assertTrue(cr.inFill(6, 11));
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testMaskPattern() {
        Context cr = createContext();
        cr.mask(SolidPattern.createRGB(0, 0, 0));
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testMaskSurfaceDoubleDouble() {
        Context cr = createContext();
        cr.mask(ImageSurface.create(Format.ARGB32, 120, 120), 0, 0);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testPaint() {
        Context cr = createContext();
        cr.paint();
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testPaintWithAlpha() {
        Context cr = createContext();
        cr.paintWithAlpha(0);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testStroke() {
        Context cr = createContext();
        cr.moveTo(10, 10);
        var path = cr.stroke().copyPath();
        assertNotNull(path);
        assertFalse(path.iterator().hasNext()); // assert that path is cleared
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testStrokePreserve() {
        Context cr = createContext();
        cr.moveTo(20, 10);
        var path = cr.strokePreserve().copyPath();
        assertNotNull(path);
        assertTrue(path.iterator().hasNext()); // assert that path is not cleared
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testStrokeExtents() {
        Context cr = createContext();
        cr.moveTo(20d, 10d);
        cr.lineTo(30d, 40d);
        Rectangle r = cr.strokeExtents();
        assertTrue(r.x() > 19d && r.x() < 20d);
        assertTrue(r.y() > 9d && r.y() < 10d);
        assertTrue(r.width() > 30d && r.width() < 31d);
        assertTrue(r.height() > 40d && r.height() < 41d);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testInStroke() {
        Context cr = createContext();
        cr.moveTo(20d, 10d);
        cr.lineTo(20d, 40d);
        assertFalse(cr.inStroke(0d, 20d));
        assertTrue(cr.inStroke(20d, 20d));
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testCopyPage() {
        Context cr = createContext();
        cr.copyPage();
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testShowPage() {
        Context cr = createContext();
        cr.showPage();
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testHairLine() {
        Context cr = createContext();
        assertFalse(cr.getHairLine());
        cr.setHairLine(true);
        assertTrue(cr.getHairLine());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testCopyPath() {
        Context cr = createContext();
        cr.moveTo(10d, 10d);
        cr.curveTo(15d, 20d, 25d, 25d, 10d, 40d);
        assertEquals(2, getPathLength(cr.copyPath()));
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testCopyPathFlat() {
        Context cr = createContext();
        cr.moveTo(10d, 10d);
        cr.curveTo(15d, 20d, 25d, 25d, 10d, 40d);
        assertEquals(14, getPathLength(cr.copyPathFlat()));
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testAppendPath() {
        Context cr = createContext();
        cr.moveTo(10, 10);
        cr.lineTo(20, 10);
        Path p = cr.copyPath();
        cr.appendPath(p);
        assertEquals(4, getPathLength(cr.copyPath()));
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testCurrentPoint() {
        Context cr = createContext();
        assertFalse(cr.hasCurrentPoint());
        cr.moveTo(10d, 20d);
        assertTrue(cr.hasCurrentPoint());
        var point = cr.getCurrentPoint();
        assertEquals(10d, point.x());
        assertEquals(20d, point.y());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testNewPath() {
        Context cr = createContext();
        cr.moveTo(10d, 20d);
        assertTrue(cr.hasCurrentPoint());
        cr.newPath();
        assertFalse(cr.hasCurrentPoint());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testSubPath() {
        Context cr = createContext();
        cr.moveTo(10d, 20d);
        assertTrue(cr.hasCurrentPoint());
        cr.newSubPath();
        assertFalse(cr.hasCurrentPoint());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testClosePath() {
        Context cr = createContext();
        cr.moveTo(10d, 15d)
          .lineTo(30d, 20d)
          .lineTo(25d, 40d)
          .closePath();
        Point p = cr.getCurrentPoint();
        assertEquals(10d, p.x());
        assertEquals(15d, p.y());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testArc() {
        Context cr = createContext();
        cr.arc(0, 0, 1, 0, 2 * Math.PI);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testArcNegative() {
        Context cr = createContext();
        cr.arcNegative(0, 0, 1, 0, 2 * Math.PI);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testCurveTo() {
        Context cr = createContext();
        cr.curveTo(0, 10, 10, 10, 20, 10);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testLineTo() {
        Context cr = createContext();
        cr.lineTo(30, 30);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testMoveTo() {
        Context cr = createContext();
        cr.moveTo(40, 30);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testRectangle() {
        Context cr = createContext();
        cr.rectangle(0, 0, 10, 10);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testGlyphPath() {
        Context cr = createContext();
        ScaledFont font = ScaledFont.create(ToyFontFace.create("Arial", FontSlant.NORMAL, FontWeight.NORMAL),
                Matrix.createIdentity(), Matrix.createIdentity(), FontOptions.create());
        Glyphs glyphs = font.textToGlyphs(0, 0, "test");
        assertEquals(4, glyphs.getNumGlyphs());
        var path = cr.glyphPath(glyphs).copyPath();
        assertNotNull(path);
        // I'm not sure how many path elements there are in the glyphs; for me it reports 103,
        // but I'm not sure if that will be the same on all systems and platforms. So let's
        // just check if there are more than 10
        assertTrue(getPathLength(path) > 10);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testTextPath() {
        Context cr = createContext();
        cr.textPath("test");
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testRelCurveTo() {
        Context cr = createContext();
        cr.moveTo(0, 0);
        cr.relCurveTo(10, 10, 10, 10, 10, 10);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testRelLineTo() {
        Context cr = createContext();
        cr.moveTo(0, 0);
        cr.relLineTo(10, 10);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testRelMoveTo() {
        Context cr = createContext();
        cr.moveTo(0, 0);
        cr.relMoveTo(10, 10);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testPathExtents() {
        Context cr = createContext();
        cr.rectangle(10, 20, 30, 40);
        Rectangle rect = cr.pathExtents();
        assertEquals(10, rect.x());
        assertEquals(20, rect.y());
        assertEquals(40, rect.width());
        assertEquals(60, rect.height());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testTranslate() {
        Context cr = createContext();
        cr.translate(10, 10);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testScale() {
        Context cr = createContext();
        cr.scale(1, 1);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testRotate() {
        Context cr = createContext();
        cr.rotate(0);
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testTransform() {
        Context cr = createContext();
        cr.transform(Matrix.createIdentity());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testMatrix() {
        Context cr = createContext();
        cr.setMatrix(Matrix.createIdentity());
        cr.getMatrix();
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testUserToDevice() {
        Context cr = createContext();
        Point p = cr.userToDevice(new Point(10, 20));
        assertEquals(10, p.x());
        assertEquals(20, p.y());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testUserToDeviceDistance() {
        Context cr = createContext();
        Point p = cr.userToDeviceDistance(new Point(10, 20));
        assertEquals(10, p.x());
        assertEquals(20, p.y());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testDeviceToUser() {
        Context cr = createContext();
        Point p = cr.deviceToUser(new Point(0, 0));
        assertEquals(0, p.x());
        assertEquals(0, p.y());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testDeviceToUserDistance() {
        Context cr = createContext();
        Point p = cr.deviceToUserDistance(new Point(0, 0));
        assertEquals(0, p.x());
        assertEquals(0, p.y());
        assertEquals(Status.SUCCESS, cr.status());
    }

    @Test
    void testUserData() {
        Context cr = createContext();
        int input = 12345;
        UserDataKey key = cr.setUserData(input);
        int output = (int) cr.getUserData(key);
        assertEquals(input, output);
        assertEquals(cr.status(), Status.SUCCESS);
    }
}
