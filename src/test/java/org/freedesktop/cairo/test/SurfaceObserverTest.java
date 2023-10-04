package org.freedesktop.cairo.test;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SurfaceObserverTest {

    @Test
    void testCreate() {
        try (Surface surface = ImageSurface.create(Format.ARGB32, 120, 120);
             SurfaceObserver observer1 = SurfaceObserver.create(surface, SurfaceObserverMode.NORMAL);
             SurfaceObserver observer2 = SurfaceObserver.create(surface, SurfaceObserverMode.RECORD_OPERATIONS)) {
            assertEquals(Status.SUCCESS, observer1.status());
            assertEquals(Status.SUCCESS, observer2.status());
        }
    }

    @Test
    void testAddFillCallback() throws IOException {
        AtomicBoolean flag = new AtomicBoolean(false);
        try (SurfaceObserver observer = SurfaceObserver.create(
                ImageSurface.create(Format.ARGB32, 120, 120),
                SurfaceObserverMode.RECORD_OPERATIONS)) {
            observer.addFillCallback(target -> flag.set(true));
            Context.create(observer)
                    .rectangle(20, 20, 30, 30)
                    .fill();
            assertTrue(flag.get());
            assertEquals(Status.SUCCESS, observer.status());
        }
    }

    @Test
    void testAddFinishCallback() {
        AtomicBoolean flag = new AtomicBoolean(false);
        try (SurfaceObserver observer = SurfaceObserver.create(
                ImageSurface.create(Format.ARGB32, 120, 120),
                SurfaceObserverMode.RECORD_OPERATIONS)) {
            observer.addFinishCallback(target -> flag.set(true));
            observer.finish();
            assertTrue(flag.get());
            assertEquals(Status.SUCCESS, observer.status());
        }
    }

    @Test
    void testAddFlushCallback() {
        AtomicBoolean flag = new AtomicBoolean(false);
        try (SurfaceObserver observer = SurfaceObserver.create(
                ImageSurface.create(Format.ARGB32, 120, 120),
                SurfaceObserverMode.RECORD_OPERATIONS)) {
            observer.addFlushCallback(target -> flag.set(true));
            observer.flush();
            assertTrue(flag.get());
            assertEquals(Status.SUCCESS, observer.status());
        }
    }

    @Test
    void testAddGlyphsCallback() throws IOException {
        AtomicBoolean flag = new AtomicBoolean(false);
        try (SurfaceObserver observer = SurfaceObserver.create(
                ImageSurface.create(Format.ARGB32, 120, 120),
                SurfaceObserverMode.RECORD_OPERATIONS)) {
            observer.addGlyphsCallback(target -> flag.set(true));
            Context.create(observer).showText("test");
            assertTrue(flag.get());
            assertEquals(Status.SUCCESS, observer.status());
        }
    }

    @Disabled
    @Test
    void testAddMaskCallback() throws IOException {
        AtomicBoolean flag = new AtomicBoolean(false);
        try (SurfaceObserver observer = SurfaceObserver.create(
                ImageSurface.create(Format.ARGB32, 120, 120),
                SurfaceObserverMode.RECORD_OPERATIONS)) {
            observer.addMaskCallback(target -> flag.set(true));
            Context.create(observer).mask(
                    ImageSurface.create(Format.ARGB32, 100, 100), 0, 0);
            assertTrue(flag.get());
            assertEquals(Status.SUCCESS, observer.status());
        }
    }

    @Test
    void testAddPaintCallback() throws IOException {
        AtomicBoolean flag = new AtomicBoolean(false);
        try (SurfaceObserver observer = SurfaceObserver.create(
                ImageSurface.create(Format.ARGB32, 120, 120),
                SurfaceObserverMode.RECORD_OPERATIONS)) {
            observer.addPaintCallback(target -> flag.set(true));
            Context.create(observer)
                    .setSource(SolidPattern.createRGBA(1.0, 1.0, 1.0, 1.0))
                    .paint();
            assertTrue(flag.get());
            assertEquals(Status.SUCCESS, observer.status());
        }
    }

    @Test
    void testAddStrokeCallback() throws IOException {
        AtomicBoolean flag = new AtomicBoolean(false);
        try (SurfaceObserver observer = SurfaceObserver.create(
                ImageSurface.create(Format.ARGB32, 120, 120),
                SurfaceObserverMode.RECORD_OPERATIONS)) {
            observer.addStrokeCallback(target -> flag.set(true));
            Context.create(observer)
                    .moveTo(10, 10)
                    .lineTo(20, 20)
                    .stroke();
            assertTrue(flag.get());
            assertEquals(Status.SUCCESS, observer.status());
        }
    }

    @Test
    void testElapsed() throws IOException {
        try (SurfaceObserver observer = SurfaceObserver.create(
                ImageSurface.create(Format.ARGB32, 120, 120),
                SurfaceObserverMode.RECORD_OPERATIONS)) {
            // draw something to ensure elapsed time > 0
            Context.create(observer)
                    .rectangle(20, 20, 30, 30)
                    .fill();
            assertTrue(observer.elapsed() > 0.0);
            assertEquals(Status.SUCCESS, observer.status());
        }
    }

    @Test
    void testPrint() {
        AtomicBoolean flag = new AtomicBoolean(false);
        try (SurfaceObserver observer = SurfaceObserver.create(
                ImageSurface.create(Format.ARGB32, 120, 120),
                SurfaceObserverMode.RECORD_OPERATIONS)) {
            observer.print(bytes -> flag.set(true));
            assertTrue(flag.get());
            assertEquals(Status.SUCCESS, observer.status());
        }
    }
}
