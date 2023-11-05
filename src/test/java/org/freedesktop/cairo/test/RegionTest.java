package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.freedesktop.cairo.RectangleInt;
import org.freedesktop.cairo.Region;
import org.freedesktop.cairo.RegionOverlap;
import org.freedesktop.cairo.Status;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;

class RegionTest {

    @Test
    void testCreate() {
        Region region = Region.create();
        assertEquals(Status.SUCCESS, region.status());
    }

    @Test
    void testCreateRectangleInt() {
        try (Arena arena = Arena.ofConfined()) {
            Region region = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            assertEquals(Status.SUCCESS, region.status());
        }
    }

    @Test
    void testCreateRectangleIntArray() {
        try (Arena arena = Arena.ofConfined()) {
            Region region = Region.create(
                    new RectangleInt[]{
                            RectangleInt.create(arena, 10, 20, 100, 200),
                            RectangleInt.create(arena, 20, 30, 100, 200)
                    });
            assertEquals(Status.SUCCESS, region.status());
        }
    }

    @Test
    void testCopy() {
        try (Arena arena = Arena.ofConfined()) {
            Region region1 = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            Region region2 = region1.copy();
            assertTrue(region1.equal(region2));
            assertEquals(Status.SUCCESS, region1.status());
        }
    }

    @Test
    void testGetExtents() {
        try (Arena arena = Arena.ofConfined()) {
            Region region = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            RectangleInt r = RectangleInt.create(arena, 0, 0, 0, 0);
            region.getExtents(r);
            assertTrue(r.toString().endsWith("x=10 y=20 width=100 height=200"));
            assertEquals(Status.SUCCESS, region.status());
        }
    }

    @Test
    void testNumRectangles() {
        try (Arena arena = Arena.ofConfined()) {
            Region region = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            int num = region.numRectangles();
            assertEquals(1, num);
            assertEquals(Status.SUCCESS, region.status());
        }
    }

    @Test
    void testGetRectangle() {
        try (Arena arena = Arena.ofConfined()) {
            Region region = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            RectangleInt r = RectangleInt.create(arena, 0, 0, 0, 0);
            region.getRectangle(0, r);
            assertTrue(r.toString().endsWith("x=10 y=20 width=100 height=200"));
            assertEquals(Status.SUCCESS, region.status());
        }
    }

    @Test
    void testIsEmpty() {
        try (Arena arena = Arena.ofConfined()) {
            Region region1 = Region.create();
            Region region2 = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            assertTrue(region1.isEmpty());
            assertFalse(region2.isEmpty());
            assertEquals(Status.SUCCESS, region1.status());
            assertEquals(Status.SUCCESS, region2.status());
        }
    }

    @Test
    void testContainsPoint() {
        try (Arena arena = Arena.ofConfined()) {
            Region region = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            assertTrue(region.containsPoint(10, 20));
            assertFalse(region.containsPoint(9, 20));
            assertEquals(Status.SUCCESS, region.status());
        }
    }

    @Test
    void testContainsRectangle() {
        try (Arena arena = Arena.ofConfined()) {
            Region region = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            assertEquals(RegionOverlap.IN, region.containsRectangle(RectangleInt.create(arena, 10, 20, 100, 200)));
            assertEquals(RegionOverlap.PART, region.containsRectangle(RectangleInt.create(arena, 11, 21, 100, 200)));
            assertEquals(RegionOverlap.OUT, region.containsRectangle(RectangleInt.create(arena, 111, 221, 100, 200)));
            assertEquals(Status.SUCCESS, region.status());
        }
    }

    @Test
    void testEqual() {
        try (Arena arena = Arena.ofConfined()) {
            Region region1 = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            Region region2 = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            Region region3 = Region.create(RectangleInt.create(arena, 11, 20, 100, 200));
            assertTrue(region1.equal(region2));
            assertFalse(region1.equal(region3));
            assertEquals(Status.SUCCESS, region1.status());
        }
    }

    @Test
    void testTranslate() {
        try (Arena arena = Arena.ofConfined()) {
            Region region = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            region.translate(2, -2);
            var extents = RectangleInt.create(arena, 0, 0, 0, 0);
            region.getExtents(extents);
            assertEquals(12, extents.x());
            assertEquals(18, extents.y());
            assertEquals(Status.SUCCESS, region.status());
        }
    }

    @Test
    void testIntersectRegion() {
        try (Arena arena = Arena.ofConfined()) {
            Region region1 = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            Region region2 = Region.create(RectangleInt.create(arena, 50, 80, 100, 200));
            region1.intersect(region2);

            var extents1 = RectangleInt.create(arena, 0, 0, 0, 0);
            var extents2 = RectangleInt.create(arena, 0, 0, 0, 0);
            region1.getExtents(extents1);
            region2.getExtents(extents2);

            assertTrue(extents1.toString().endsWith("x=50 y=80 width=60 height=140"));
            assertTrue(extents2.toString().endsWith("x=50 y=80 width=100 height=200"));
            assertEquals(Status.SUCCESS, region1.status());
            assertEquals(Status.SUCCESS, region2.status());
        }
    }

    @Test
    void testIntersectRectangleInt() {
        try (Arena arena = Arena.ofConfined()) {
            Region region = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            RectangleInt rect = RectangleInt.create(arena, 50, 80, 100, 200);
            region.intersect(rect);
            RectangleInt extents = RectangleInt.create(arena, 0, 0, 0, 0);
            region.getExtents(extents);
            assertTrue(extents.toString().endsWith("x=50 y=80 width=60 height=140"));
            assertEquals(Status.SUCCESS, region.status());
        }
    }

    @Test
    void testSubtractRegion() {
        try (Arena arena = Arena.ofConfined()) {
            Region region1 = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            Region region2 = Region.create(RectangleInt.create(arena, 20, 20, 100, 200));
            region1.subtract(region2);

            var extents1 = RectangleInt.create(arena, 0, 0, 0, 0);
            var extents2 = RectangleInt.create(arena, 0, 0, 0, 0);
            region1.getExtents(extents1);
            region2.getExtents(extents2);

            assertTrue(extents1.toString().endsWith("x=10 y=20 width=10 height=200"));
            assertTrue(extents2.toString().endsWith("x=20 y=20 width=100 height=200"));
            assertEquals(Status.SUCCESS, region1.status());
            assertEquals(Status.SUCCESS, region2.status());
        }
    }

    @Test
    void testSubtractRectangleInt() {
        try (Arena arena = Arena.ofConfined()) {
            Region region = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            RectangleInt rect = RectangleInt.create(arena, 20, 20, 100, 200);
            region.subtract(rect);
            RectangleInt extents = RectangleInt.create(arena, 0, 0, 0, 0);
            region.getExtents(extents);
            assertTrue(extents.toString().endsWith("x=10 y=20 width=10 height=200"));
            assertEquals(Status.SUCCESS, region.status());
        }
    }

    @Test
    void testUnionRegion() {
        try (Arena arena = Arena.ofConfined()) {
            Region region1 = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            Region region2 = Region.create(RectangleInt.create(arena, 20, 20, 100, 200));
            region1.union(region2);

            var extents1 = RectangleInt.create(arena, 0, 0, 0, 0);
            var extents2 = RectangleInt.create(arena, 0, 0, 0, 0);
            region1.getExtents(extents1);
            region2.getExtents(extents2);

            assertTrue(extents1.toString().endsWith("x=10 y=20 width=110 height=200"));
            assertTrue(extents2.toString().endsWith("x=20 y=20 width=100 height=200"));
            assertEquals(Status.SUCCESS, region1.status());
            assertEquals(Status.SUCCESS, region2.status());
        }
    }

    @Test
    void testUnionRectangleInt() {
        try (Arena arena = Arena.ofConfined()) {
            Region region = Region.create(RectangleInt.create(arena, 10, 20, 100, 200));
            RectangleInt rect = RectangleInt.create(arena, 20, 20, 100, 200);
            region.union(rect);
            RectangleInt extents = RectangleInt.create(arena, 0, 0, 0, 0);
            region.getExtents(extents);
            assertTrue(extents.toString().endsWith("x=10 y=20 width=110 height=200"));
            assertEquals(Status.SUCCESS, region.status());
        }
    }

    @Test
    void testXorRegion() {
        try (Arena arena = Arena.ofConfined()) {
            Region region1 = Region.create(RectangleInt.create(arena, 0, 0, 50, 50));
            Region region2 = Region.create(RectangleInt.create(arena, 0, 0, 50, 20));
            region1.xor(region2);

            var extents1 = RectangleInt.create(arena, 0, 0, 0, 0);
            var extents2 = RectangleInt.create(arena, 0, 0, 0, 0);
            region1.getExtents(extents1);
            region2.getExtents(extents2);

            assertTrue(extents1.toString().endsWith("x=0 y=20 width=50 height=30"));
            assertTrue(extents2.toString().endsWith("x=0 y=0 width=50 height=20"));
            assertEquals(Status.SUCCESS, region1.status());
            assertEquals(Status.SUCCESS, region2.status());
        }
    }

    @Test
    void testXorRectangleInt() {
        try (Arena arena = Arena.ofConfined()) {
            Region region = Region.create(RectangleInt.create(arena, 0, 0, 50, 50));
            RectangleInt rect = RectangleInt.create(arena, 0, 0, 50, 20);
            region.xor(rect);
            RectangleInt extents = RectangleInt.create(arena, 0, 0, 0, 0);
            region.getExtents(extents);
            assertTrue(extents.toString().endsWith("x=0 y=20 width=50 height=30"));
            assertEquals(Status.SUCCESS, region.status());
        }
    }
}
