package org.freedesktop.cairo;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import io.github.jwharm.cairobindings.Interop;

/**
 * SVGVersion is used to describe the version number of the SVG specification
 * that a generated SVG file will conform to.
 * 
 * @since 1.2
 */
public enum SVGVersion {

	/**
	 * The version 1.1 of the SVG specification.
	 * 
	 * @since 1.2
	 */
	VERSION_1_1,

	/**
	 * The version 1.2 of the SVG specification.
	 * 
	 * @since 1.2
	 */
	VERSION_1_2;

	{
		Interop.ensureInitialized();
	}

	/**
	 * Returns the enum constant for the given ordinal (its position in the enum
	 * declaration).
	 * 
	 * @param ordinal the position in the enum declaration, starting from zero
	 * @return the enum constant for the given ordinal
	 */
	public static SVGVersion of(int ordinal) {
		return values()[ordinal];
	}

	/**
	 * Used to retrieve the list of supported versions. See
	 * {@link SVGSurface#restrictToVersion(SVGVersion)}.
	 * 
	 * @return supported version list
	 * @since 1.2
	 */
	public static SVGVersion[] getVersions() {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment versionsPtr = arena.allocate(ValueLayout.ADDRESS);
				MemorySegment numVersionsPtr = arena.allocate(ValueLayout.JAVA_INT);
				cairo_svg_get_versions.invoke(versionsPtr, numVersionsPtr);
				int numVersions = numVersionsPtr.get(ValueLayout.JAVA_INT, 0);
				int[] versionInts = MemorySegment.ofAddress(versionsPtr.address(), numVersions, arena.scope())
						.toArray(ValueLayout.JAVA_INT);
				SVGVersion[] versions = new SVGVersion[numVersions];
				for (int i = 0; i < versionInts.length; i++) {
					versions[i] = SVGVersion.of(versionInts[i]);
				}
				return versions;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_svg_get_versions = Interop.downcallHandle("cairo_svg_get_versions",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Get the string representation of this version. This function will return
	 * {@code null} if version isn't valid. See {@link #getVersions()} for a way to
	 * get the list of valid versions.
	 * 
	 * @return the string associated this version.
	 * @since 1.2
	 */
	@Override
	public String toString() {
		try {
			MemorySegment result = (MemorySegment) cairo_svg_version_to_string.invoke(ordinal());
			return Interop.getStringFrom(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_svg_version_to_string = Interop.downcallHandle(
			"cairo_svg_version_to_string",
			FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded(), ValueLayout.JAVA_INT), false);
}
