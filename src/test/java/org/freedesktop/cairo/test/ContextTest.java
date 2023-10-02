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

    @Test
    void testCreate() {
        try {
            assertEquals(Context.create(ImageSurface.create(Format.ARGB32, 120, 120)).status(), Status.SUCCESS);
        } catch (IOException ioe) {
            fail(ioe);
        }
    }

    @Test
    void testStatus() {
        Context cr = createContext();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSave() {
        Context cr = createContext();
        cr.save();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testRestore() {
        Context cr = createContext();
        cr.save();
        cr.restore();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetTarget() {
        Context cr = createContext();
        cr.getTarget();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testPushGroup() {
        Context cr = createContext();
        cr.pushGroup();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testPushGroupWithContent() {
        Context cr = createContext();
        cr.pushGroupWithContent(Content.COLOR_ALPHA);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testPopGroup() {
        Context cr = createContext();
        cr.pushGroup();
        cr.popGroup();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testPopGroupToSource() {
        Context cr = createContext();
        cr.pushGroup();
        cr.popGroupToSource();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetGroupTarget() {
        Context cr = createContext();
        cr.getGroupTarget();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSetSourceRGB() {
        Context cr = createContext();
        cr.setSourceRGB(0, 0, 0);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSetSourceRGBA() {
        Context cr = createContext();
        cr.setSourceRGBA(0, 0, 0, 0);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSetSourcePattern() {
        Context cr = createContext();
        cr.setSource(SolidPattern.createRGB(0, 0, 0));
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSetSourceSurfaceDoubleDouble() {
        Context cr = createContext();
        cr.setSource(ImageSurface.create(Format.ARGB32, 120, 120), 0, 0);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetSource() {
        Context cr = createContext();
        SurfacePattern source = cr.getSource();
        assertEquals(source.status(), Status.SUCCESS);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSetAntialias() {
        Context cr = createContext();
        cr.setAntialias(Antialias.FAST);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetAntialias() {
        Context cr = createContext();
        cr.setAntialias(Antialias.GOOD);
        Antialias a = cr.getAntialias();
        assertEquals(a, Antialias.GOOD);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSetDash() {
        Context cr = createContext();
        cr.setDash(new double[] { 4d, 5d }, 0);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetDashCount() {
        Context cr = createContext();
        cr.setDash(new double[] { 4d, 5d }, 0);
        int count = cr.getDashCount();
        assertEquals(count, 2);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetDash() {
        Context cr = createContext();
        cr.setDash(new double[] { 4d, 5d }, 0);
        double[] dash = cr.getDash();
        assertEquals(dash.length, 2);
        assertEquals(dash[0], 4d);
        assertEquals(dash[1], 5d);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetDashOffset() {
        Context cr = createContext();
        cr.setDash(new double[] { 4d, 5d }, 1d);
        assertEquals(cr.getDashOffset(), 1d);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSetFillRule() {
        Context cr = createContext();
        cr.setFillRule(FillRule.EVEN_ODD);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetFillRule() {
        Context cr = createContext();
        cr.setFillRule(FillRule.EVEN_ODD);
        assertEquals(cr.getFillRule(), FillRule.EVEN_ODD);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSetLineCap() {
        Context cr = createContext();
        cr.setLineCap(LineCap.ROUND);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetLineCap() {
        Context cr = createContext();
        cr.setLineCap(LineCap.SQUARE);
        assertEquals(cr.getLineCap(), LineCap.SQUARE);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSetLineJoin() {
        Context cr = createContext();
        cr.setLineJoin(LineJoin.BEVEL);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetLineJoin() {
        Context cr = createContext();
        cr.setLineJoin(LineJoin.ROUND);
        assertEquals(cr.getLineJoin(), LineJoin.ROUND);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSetLineWidth() {
        Context cr = createContext();
        cr.setLineWidth(2);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetLineWidth() {
        Context cr = createContext();
        cr.setLineWidth(3);
        assertEquals(cr.getLineWidth(), 3);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSetMiterLimit() {
        Context cr = createContext();
        cr.setMiterLimit(2);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetMiterLimit() {
        Context cr = createContext();
        cr.setMiterLimit(3);
        assertEquals(cr.getMiterLimit(), 3);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSetOperator() {
        Context cr = createContext();
        cr.setOperator(Operator.ADD);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetOperator() {
        Context cr = createContext();
        cr.setOperator(Operator.DEST_OVER);
        assertEquals(cr.getOperator(), Operator.DEST_OVER);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSetTolerance() {
        Context cr = createContext();
        cr.setTolerance(2);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetTolerance() {
        Context cr = createContext();
        cr.setTolerance(3);
        assertEquals(cr.getTolerance(), 3);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testClip() {
        Context cr = createContext();
        cr.resetClip();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testClipPreserve() {
        Context cr = createContext();
        cr.clipPreserve();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testClipExtents() {
        Context cr = createContext();
        cr.clipExtents();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testInClip() {
        Context cr = createContext();
        cr.inClip(cr.getCurrentPoint().x(), cr.getCurrentPoint().y());
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testResetClip() {
        Context cr = createContext();
        cr.resetClip();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testCopyClipRectangleList() {
        Context cr = createContext();
        cr.rectangle(10, 10, 20, 20);
        cr.copyClipRectangleList();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testFill() {
        Context cr = createContext();
        cr.fill();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testFillPreserve() {
        Context cr = createContext();
        cr.fillPreserve();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testFillExtents() {
        Context cr = createContext();
        Rectangle r = cr.fillExtents();
        assertNotNull(r);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testInFill() {
        Context cr = createContext();
        cr.inFill(0, 0);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testMaskPattern() {
        Context cr = createContext();
        cr.mask(SolidPattern.createRGB(0, 0, 0));
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testMaskSurfaceDoubleDouble() {
        Context cr = createContext();
        cr.mask(ImageSurface.create(Format.ARGB32, 120, 120), 0, 0);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testPaint() {
        Context cr = createContext();
        cr.paint();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testPaintWithAlpha() {
        Context cr = createContext();
        cr.paintWithAlpha(0);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testStroke() {
        Context cr = createContext();
        cr.moveTo(10, 10);
        cr.stroke();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testStrokePreserve() {
        Context cr = createContext();
        cr.moveTo(20, 10);
        cr.strokePreserve();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testStrokeExtents() {
        Context cr = createContext();
        Rectangle r = cr.strokeExtents();
        assertNotNull(r);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testInStroke() {
        Context cr = createContext();
        cr.inStroke(0, 0);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testCopyPage() {
        Context cr = createContext();
        cr.copyPage();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testShowPage() {
        Context cr = createContext();
        cr.showPage();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSetHairLine() {
        Context cr = createContext();
        cr.setHairLine(true);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetHairLine() {
        Context cr = createContext();
        cr.setHairLine(true);
        assertTrue(cr.getHairLine());
        cr.setHairLine(false);
        assertFalse(cr.getHairLine());
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testCopyPath() {
        Context cr = createContext();
        cr.copyPath();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testCopyPathFlat() {
        Context cr = createContext();
        cr.copyPathFlat();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testAppendPath() {
        Context cr = createContext();
        cr.moveTo(10, 10);
        cr.lineTo(20, 10);
        Path p = cr.copyPath();
        cr.appendPath(p);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testHasCurrentPoint() {
        Context cr = createContext();
        cr.hasCurrentPoint();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetCurrentPoint() {
        Context cr = createContext();
        cr.getCurrentPoint();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testNewPath() {
        Context cr = createContext();
        cr.newPath();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testNewSubPath() {
        Context cr = createContext();
        cr.newSubPath();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testClosePath() {
        Context cr = createContext();
        cr.closePath();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testArc() {
        Context cr = createContext();
        cr.arc(0, 0, 1, 0, 2 * Math.PI);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testArcNegative() {
        Context cr = createContext();
        cr.arcNegative(0, 0, 1, 0, 2 * Math.PI);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testCurveTo() {
        Context cr = createContext();
        cr.curveTo(0, 10, 10, 10, 20, 10);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testLineTo() {
        Context cr = createContext();
        cr.lineTo(30, 30);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testMoveTo() {
        Context cr = createContext();
        cr.moveTo(40, 30);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testRectangle() {
        Context cr = createContext();
        cr.rectangle(0, 0, 10, 10);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGlyphPath() {
        Context cr = createContext();
        cr.glyphPath(null);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testTextPath() {
        Context cr = createContext();
        cr.textPath("test");
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testRelCurveTo() {
        Context cr = createContext();
        cr.moveTo(0, 0);
        cr.relCurveTo(10, 10, 10, 10, 10, 10);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testRelLineTo() {
        Context cr = createContext();
        cr.moveTo(0, 0);
        cr.relLineTo(10, 10);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testRelMoveTo() {
        Context cr = createContext();
        cr.moveTo(0, 0);
        cr.relMoveTo(10, 10);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testPathExtents() {
        Context cr = createContext();
        cr.pathExtents();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testTranslate() {
        Context cr = createContext();
        cr.translate(10, 10);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testScale() {
        Context cr = createContext();
        cr.scale(1, 1);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testRotate() {
        Context cr = createContext();
        cr.rotate(0);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testTransform() {
        Context cr = createContext();
        cr.transform(Matrix.createIdentity());
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSetMatrix() {
        Context cr = createContext();
        cr.setMatrix(Matrix.createIdentity());
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetMatrix() {
        Context cr = createContext();
        cr.getMatrix();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testIdentityMatrix() {
        Context cr = createContext();
        cr.identityMatrix();
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testUserToDevice() {
        Context cr = createContext();
        cr.userToDevice(new Point(0, 0));
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testUserToDeviceDistance() {
        Context cr = createContext();
        cr.userToDeviceDistance(new Point(0, 0));
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testDeviceToUser() {
        Context cr = createContext();
        cr.deviceToUser(new Point(0, 0));
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testDeviceToUserDistance() {
        Context cr = createContext();
        cr.deviceToUserDistance(new Point(0, 0));
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testSetUserData() {
        Context cr = createContext();
        cr.setUserData(12345);
        assertEquals(cr.status(), Status.SUCCESS);
    }

    @Test
    void testGetUserData() {
        Context cr = createContext();
        int input = 12345;
        UserDataKey key = cr.setUserData(input);
        int output = (int) cr.getUserData(key);
        assertEquals(input, output);
        assertEquals(cr.status(), Status.SUCCESS);
    }
}
