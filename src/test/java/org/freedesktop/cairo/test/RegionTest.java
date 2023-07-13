package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.freedesktop.cairo.RectangleInt;
import org.freedesktop.cairo.Region;
import org.freedesktop.cairo.RegionOverlap;
import org.freedesktop.cairo.Status;
import org.junit.jupiter.api.Test;

class RegionTest {

    @Test
    void testCreate() {
        Region region = Region.create();
        assertEquals(Status.SUCCESS, region.status());
    }

    @Test
    void testCreateRectangleInt() {
        Region region = Region.create(RectangleInt.create(10, 20, 100, 200));
        assertEquals(Status.SUCCESS, region.status());
    }

    @Test
    void testCreateRectangleIntArray() {
        Region region = Region.create(
                new RectangleInt[] { RectangleInt.create(10, 20, 100, 200), RectangleInt.create(20, 30, 100, 200) });
        assertEquals(Status.SUCCESS, region.status());
    }

    @Test
    void testCopy() {
        Region region1 = Region.create(RectangleInt.create(10, 20, 100, 200));
        Region region2 = region1.copy();
        assertTrue(region1.equal(region2));
        assertEquals(Status.SUCCESS, region1.status());
    }

    @Test
    void testGetExtents() {
        Region region = Region.create(RectangleInt.create(10, 20, 100, 200));
        RectangleInt r = region.getExtents();
        assertTrue(r.toString().endsWith("x=10 y=20 width=100 height=200"));
        assertEquals(Status.SUCCESS, region.status());
    }

    @Test
    void testNumRectangles() {
        Region region = Region.create(RectangleInt.create(10, 20, 100, 200));
        int num = region.numRectangles();
        assertEquals(1, num);
        assertEquals(Status.SUCCESS, region.status());
    }

    @Test
    void testGetRectangle() {
        Region region = Region.create(RectangleInt.create(10, 20, 100, 200));
        RectangleInt r = region.getRectangle(0);
        assertTrue(r.toString().endsWith("x=10 y=20 width=100 height=200"));
        assertEquals(Status.SUCCESS, region.status());
    }

    @Test
    void testIsEmpty() {
        Region region1 = Region.create();
        Region region2 = Region.create(RectangleInt.create(10, 20, 100, 200));
        assertTrue(region1.isEmpty());
        assertFalse(region2.isEmpty());
        assertEquals(Status.SUCCESS, region1.status());
        assertEquals(Status.SUCCESS, region2.status());
    }

    @Test
    void testContainsPoint() {
        Region region = Region.create(RectangleInt.create(10, 20, 100, 200));
        assertTrue(region.containsPoint(10, 20));
        assertFalse(region.containsPoint(9, 20));
        assertEquals(Status.SUCCESS, region.status());
    }

    @Test
    void testContainsRectangle() {
        Region region = Region.create(RectangleInt.create(10, 20, 100, 200));
        assertEquals(RegionOverlap.IN, region.containsRectangle(RectangleInt.create(10, 20, 100, 200)));
        assertEquals(RegionOverlap.PART, region.containsRectangle(RectangleInt.create(11, 21, 100, 200)));
        assertEquals(RegionOverlap.OUT, region.containsRectangle(RectangleInt.create(111, 221, 100, 200)));
        assertEquals(Status.SUCCESS, region.status());
    }

    @Test
    void testEqual() {
        Region region1 = Region.create(RectangleInt.create(10, 20, 100, 200));
        Region region2 = Region.create(RectangleInt.create(10, 20, 100, 200));
        Region region3 = Region.create(RectangleInt.create(11, 20, 100, 200));
        assertTrue(region1.equal(region2));
        assertFalse(region1.equal(region3));
        assertEquals(Status.SUCCESS, region1.status());
    }

    @Test
    void testTranslate() {
        Region region = Region.create(RectangleInt.create(10, 20, 100, 200));
        region.translate(2, -2);
        assertEquals(12, region.getExtents().x());
        assertEquals(18, region.getExtents().y());
        assertEquals(Status.SUCCESS, region.status());
    }

    @Test
    void testIntersectRegion() {
        Region region1 = Region.create(RectangleInt.create(10, 20, 100, 200));
        Region region2 = Region.create(RectangleInt.create(50, 80, 100, 200));
        region1.intersect(region2);
        assertTrue(region1.getExtents().toString().endsWith("x=50 y=80 width=60 height=140"));
        assertTrue(region2.getExtents().toString().endsWith("x=50 y=80 width=100 height=200"));
        assertEquals(Status.SUCCESS, region1.status());
        assertEquals(Status.SUCCESS, region2.status());
    }

    @Test
    void testIntersectRectangleInt() {
        Region region = Region.create(RectangleInt.create(10, 20, 100, 200));
        RectangleInt rect = RectangleInt.create(50, 80, 100, 200);
        region.intersect(rect);
        assertTrue(region.getExtents().toString().endsWith("x=50 y=80 width=60 height=140"));
        assertEquals(Status.SUCCESS, region.status());
    }

    @Test
    void testSubtractRegion() {
        Region region1 = Region.create(RectangleInt.create(10, 20, 100, 200));
        Region region2 = Region.create(RectangleInt.create(20, 20, 100, 200));
        region1.subtract(region2);
        assertTrue(region1.getExtents().toString().endsWith("x=10 y=20 width=10 height=200"));
        assertTrue(region2.getExtents().toString().endsWith("x=20 y=20 width=100 height=200"));
        assertEquals(Status.SUCCESS, region1.status());
        assertEquals(Status.SUCCESS, region2.status());
    }

    @Test
    void testSubtractRectangleInt() {
        Region region = Region.create(RectangleInt.create(10, 20, 100, 200));
        RectangleInt rect = RectangleInt.create(20, 20, 100, 200);
        region.subtract(rect);
        assertTrue(region.getExtents().toString().endsWith("x=10 y=20 width=10 height=200"));
        assertEquals(Status.SUCCESS, region.status());
    }

    @Test
    void testUnionRegion() {
        Region region1 = Region.create(RectangleInt.create(10, 20, 100, 200));
        Region region2 = Region.create(RectangleInt.create(20, 20, 100, 200));
        region1.union(region2);
        assertTrue(region1.getExtents().toString().endsWith("x=10 y=20 width=110 height=200"));
        assertTrue(region2.getExtents().toString().endsWith("x=20 y=20 width=100 height=200"));
        assertEquals(Status.SUCCESS, region1.status());
        assertEquals(Status.SUCCESS, region2.status());
    }

    @Test
    void testUnionRectangleInt() {
        Region region = Region.create(RectangleInt.create(10, 20, 100, 200));
        RectangleInt rect = RectangleInt.create(20, 20, 100, 200);
        region.union(rect);
        assertTrue(region.getExtents().toString().endsWith("x=10 y=20 width=110 height=200"));
        assertEquals(Status.SUCCESS, region.status());
    }

    @Test
    void testXorRegion() {
        Region region1 = Region.create(RectangleInt.create(0, 0, 50, 50));
        Region region2 = Region.create(RectangleInt.create(0, 0, 50, 20));
        region1.xor(region2);
        assertTrue(region1.getExtents().toString().endsWith("x=0 y=20 width=50 height=30"));
        assertTrue(region2.getExtents().toString().endsWith("x=0 y=0 width=50 height=20"));
        assertEquals(Status.SUCCESS, region1.status());
        assertEquals(Status.SUCCESS, region2.status());
    }

    @Test
    void testXorRectangleInt() {
        Region region = Region.create(RectangleInt.create(0, 0, 50, 50));
        RectangleInt rect = RectangleInt.create(0, 0, 50, 20);
        region.xor(rect);
        assertTrue(region.getExtents().toString().endsWith("x=0 y=20 width=50 height=30"));
        assertEquals(Status.SUCCESS, region.status());
    }

}
