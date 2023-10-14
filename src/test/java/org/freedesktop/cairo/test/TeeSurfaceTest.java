package org.freedesktop.cairo.test;

import io.github.jwharm.cairobindings.Interop;
import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeeSurfaceTest {

    @Test
    void testCreate() {
        try (Surface primary = ImageSurface.create(Format.ARGB32, 120, 120)) {
            TeeSurface tee = TeeSurface.create(primary);
            assertEquals(Status.SUCCESS, tee.status());
        }
    }

    @Test
    void testIndex() {
        try (Surface primary = ImageSurface.create(Format.ARGB32, 120, 120);
             Surface target1 = ImageSurface.create(Format.ARGB32, 120, 120);
             Surface target2 = ImageSurface.create(Format.ARGB32, 120, 120)) {
            TeeSurface tee = TeeSurface.create(primary);
            tee.add(target1);
            tee.add(target2);

            assertEquals(primary, tee.index(0));
            assertEquals(target1, tee.index(1));
            assertEquals(target2, tee.index(2));

            /* We don't actually call `cairo_tee_surface_index` in the bindings,
             * but we call it in the method below, to assert that the results
             * are the same.
             */
            assertEquals(tee.index(0).handle(), cairo_tee_surface_index(tee, 0));
            assertEquals(tee.index(1).handle(), cairo_tee_surface_index(tee, 1));
            assertEquals(tee.index(2).handle(), cairo_tee_surface_index(tee, 2));

            assertEquals(Status.SUCCESS, tee.status());
        }
    }

    private MemorySegment cairo_tee_surface_index(Surface tee, int index) {
        MethodHandle cairo_tee_surface_index = Interop.downcallHandle("cairo_tee_surface_index",
                FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
        try {
            return (MemorySegment) cairo_tee_surface_index.invoke(tee.handle(), index);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddAndRemove() {
        try (Surface primary = ImageSurface.create(Format.ARGB32, 120, 120);
             Surface target = ImageSurface.create(Format.ARGB32, 120, 120)) {
            TeeSurface tee = TeeSurface.create(primary);
            tee.add(target);
            tee.remove(target);
            assertEquals(Status.SUCCESS, tee.status());
        }
    }
}
