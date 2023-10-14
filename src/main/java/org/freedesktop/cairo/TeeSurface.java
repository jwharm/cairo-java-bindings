package org.freedesktop.cairo;

import io.github.jwharm.cairobindings.Interop;
import io.github.jwharm.cairobindings.MemoryCleaner;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.List;

/**
 * The "tee" surface supports redirecting all its input to multiple surfaces.
 *
 * @see Surface
 * @since 1.10
 */
public final class TeeSurface extends Surface {

    static {
        Cairo.ensureInitialized();
    }

    // Keep a reference to Surfaces that are passed to the TeeSurface during its
    // lifetime.
    private final List<Surface> targets = new ArrayList<>();

    /**
     * Constructor used internally to instantiate a java TeeSurface object for a
     * native {@code cairo_surface_t} instance
     *
     * @param address the memory address of the native {@code cairo_surface_t}
     *                instance
     */
    public TeeSurface(MemorySegment address) {
        super(address);
    }

    /**
     * Creates a new "tee" surface.
     * <p>
     * The {@code primary} surface is used when querying surface options, like font
     * options and extents.
     * <p>
     * Operations performed on the tee surface will be replayed on any surface added
     * to it.
     *
     * @param primary the primary {@link Surface}
     * @return the newly created surface
     * @since 1.10
     */
    public static TeeSurface create(Surface primary) {
        TeeSurface surface;
        try {
            MemorySegment result = (MemorySegment) cairo_tee_surface_create.invoke(primary.handle());
            surface = new TeeSurface(result);
            surface.targets.add(primary); // The primary surface used to create the TeeSurface is always set at the zero index.
            MemoryCleaner.takeOwnership(surface.handle());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return surface;
    }

    private static final MethodHandle cairo_tee_surface_create = Interop.downcallHandle("cairo_tee_surface_create",
            FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Adds a new target surface to the list of replicas of a tee surface.
     *
     * @param target the surface to add
     * @since 1.10
     */
    public void add(Surface target) {
        try {
            cairo_tee_surface_add.invoke(handle(), target.handle());
            targets.add(target);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_tee_surface_add = Interop.downcallHandle("cairo_tee_surface_add",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));

    /**
     * Retrieves the replica surface at the given index.
     * <p>
     * The primary surface used to create the TeeSurface is always set at the zero
     * index.
     *
     * @param index the index of the replica to retrieve
     * @return the surface at the given index
     * @since 1.10
     */
    public Surface index(int index) {
        try {
            return targets.get(index);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes the given surface from the list of replicas of a tee surface.
     *
     * @param target the surface to remove
     * @since 1.10
     */
    public void remove(Surface target) {
        try {
            cairo_tee_surface_remove.invoke(handle(), target.handle());
            targets.remove(target); // the comparison will call Proxy.equals(), which compares the memory addresses
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle cairo_tee_surface_remove = Interop.downcallHandle("cairo_tee_surface_remove",
            FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS));
}
