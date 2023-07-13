package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.freedesktop.cairo.Content;
import org.freedesktop.cairo.RasterSource;
import org.freedesktop.cairo.RasterSourceAcquireFunc;
import org.freedesktop.cairo.RasterSourceCopyFunc;
import org.freedesktop.cairo.RasterSourceFinishFunc;
import org.freedesktop.cairo.RasterSourceReleaseFunc;
import org.freedesktop.cairo.RasterSourceSnapshotFunc;
import org.freedesktop.cairo.Status;
import org.junit.jupiter.api.Test;

class RasterSourceTest {

    @Test
    void testCreate() {
        RasterSource source = RasterSource.create(Content.COLOR_ALPHA, 100, 100);
        assertEquals(Status.SUCCESS, source.status());
    }

    @Test
    void testSetAcquire() {
        RasterSource source = RasterSource.create(Content.COLOR_ALPHA, 100, 100);
        RasterSourceAcquireFunc f = (pattern, target, extents) -> null;
        RasterSourceReleaseFunc r = (pattern, target) -> {};
        source.setAcquire(f, r);
        assertEquals(Status.SUCCESS, source.status());
    }

    @Test
    void testGetAcquire() {
        RasterSource source = RasterSource.create(Content.COLOR_ALPHA, 100, 100);
        RasterSourceAcquireFunc f1 = (pattern, target, extents) -> null;
        RasterSourceReleaseFunc r1 = (pattern, target) -> {};
        source.setAcquire(f1, r1);
        RasterSourceAcquireFunc f2 = source.getAcquire();
        assertEquals(f1, f2);
        assertEquals(Status.SUCCESS, source.status());
    }

    @Test
    void testGetRelease() {
        RasterSource source = RasterSource.create(Content.COLOR_ALPHA, 100, 100);
        RasterSourceAcquireFunc f1 = (pattern, target, extents) -> null;
        RasterSourceReleaseFunc r1 = (pattern, target) -> {};
        source.setAcquire(f1, r1);
        RasterSourceReleaseFunc r2 = source.getRelease();
        assertEquals(r1, r2);
        assertEquals(Status.SUCCESS, source.status());
    }

    @Test
    void testSetSnapshot() {
        RasterSource source = RasterSource.create(Content.COLOR_ALPHA, 100, 100);
        RasterSourceSnapshotFunc s = (pattern) -> null;
        source.setSnapshot(s);
        assertEquals(Status.SUCCESS, source.status());
    }

    @Test
    void testGetSnapshot() {
        RasterSource source = RasterSource.create(Content.COLOR_ALPHA, 100, 100);
        RasterSourceSnapshotFunc s1 = (pattern) -> null;
        source.setSnapshot(s1);
        RasterSourceSnapshotFunc s2 = source.getSnapshot();
        assertEquals(s1, s2);
        assertEquals(Status.SUCCESS, source.status());
    }

    @Test
    void testSetCopy() {
        RasterSource source = RasterSource.create(Content.COLOR_ALPHA, 100, 100);
        RasterSourceCopyFunc c = (pattern, other) -> null;
        source.setCopy(c);
        assertEquals(Status.SUCCESS, source.status());
    }

    @Test
    void testGetCopy() {
        RasterSource source = RasterSource.create(Content.COLOR_ALPHA, 100, 100);
        RasterSourceCopyFunc c1 = (pattern, other) -> null;
        source.setCopy(c1);
        RasterSourceCopyFunc c2 = source.getCopy();
        assertEquals(c1, c2);
        assertEquals(Status.SUCCESS, source.status());
    }

    @Test
    void testSetFinish() {
        RasterSource source = RasterSource.create(Content.COLOR_ALPHA, 100, 100);
        RasterSourceFinishFunc f = (pattern) -> {};
        source.setFinish(f);
        assertEquals(Status.SUCCESS, source.status());
    }

    @Test
    void testGetFinish() {
        RasterSource source = RasterSource.create(Content.COLOR_ALPHA, 100, 100);
        RasterSourceFinishFunc f1 = (pattern) -> {};
        source.setFinish(f1);
        RasterSourceFinishFunc f2 = source.getFinish();
        assertEquals(f1, f2);
        assertEquals(Status.SUCCESS, source.status());
    }
}
