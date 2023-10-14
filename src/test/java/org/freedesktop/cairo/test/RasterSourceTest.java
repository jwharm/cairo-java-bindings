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
    void testAcquireAndRelease() {
        RasterSource source = RasterSource.create(Content.COLOR_ALPHA, 100, 100);
        RasterSourceAcquireFunc acquireFunc = (pattern, target, extents) -> null;
        RasterSourceReleaseFunc releaseFunc = (pattern, target) -> {};
        source.setAcquire(acquireFunc, releaseFunc);
        assertEquals(acquireFunc, source.getAcquire());
        assertEquals(releaseFunc, source.getRelease());
        assertEquals(Status.SUCCESS, source.status());
    }

    @Test
    void testSnapshot() {
        RasterSource source = RasterSource.create(Content.COLOR_ALPHA, 100, 100);
        RasterSourceSnapshotFunc s1 = (pattern) -> null;
        source.setSnapshot(s1);
        RasterSourceSnapshotFunc s2 = source.getSnapshot();
        assertEquals(s1, s2);
        assertEquals(Status.SUCCESS, source.status());
    }

    @Test
    void testCopy() {
        RasterSource source = RasterSource.create(Content.COLOR_ALPHA, 100, 100);
        RasterSourceCopyFunc c1 = (pattern, other) -> null;
        source.setCopy(c1);
        RasterSourceCopyFunc c2 = source.getCopy();
        assertEquals(c1, c2);
        assertEquals(Status.SUCCESS, source.status());
    }

    @Test
    void testFinish() {
        RasterSource source = RasterSource.create(Content.COLOR_ALPHA, 100, 100);
        RasterSourceFinishFunc f1 = (pattern) -> {};
        source.setFinish(f1);
        RasterSourceFinishFunc f2 = source.getFinish();
        assertEquals(f1, f2);
        assertEquals(Status.SUCCESS, source.status());
    }
}
