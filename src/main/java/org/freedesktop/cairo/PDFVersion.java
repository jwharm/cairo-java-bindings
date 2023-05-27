package org.freedesktop.cairo;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import io.github.jwharm.cairobindings.Interop;

/**
 * {@code PDFVersion} is used to describe the version number of the PDF
 * specification that a generated PDF file will conform to.
 * 
 * @since 1.10
 */
public enum PDFVersion {

	/**
	 * The version 1.4 of the PDF specification.
	 * 
	 * @since 1.10
	 */
	VERSION_1_4,

	/**
	 * The version 1.5 of the PDF specification.
	 * 
	 * @since 1.10
	 */
	VERSION_1_5;

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
	public static PDFVersion of(int ordinal) {
		return values()[ordinal];
	}

	/**
	 * Used to retrieve the list of supported versions. See
	 * {@link PDFSurface#restrictToVersion(PDFVersion)}.
	 * 
	 * @return supported version list
	 * @since 1.10
	 */
	public static PDFVersion[] getVersions() {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment versionsPtr = arena.allocate(ValueLayout.ADDRESS);
				MemorySegment numVersionsPtr = arena.allocate(ValueLayout.JAVA_INT);
				cairo_pdf_get_versions.invoke(versionsPtr, numVersionsPtr);
				int numVersions = numVersionsPtr.get(ValueLayout.JAVA_INT, 0);
				int[] versionInts = MemorySegment.ofAddress(versionsPtr.address(), numVersions, arena.scope())
						.toArray(ValueLayout.JAVA_INT);
				PDFVersion[] versions = new PDFVersion[numVersions];
				for (int i = 0; i < versionInts.length; i++) {
					versions[i] = PDFVersion.of(versionInts[i]);
				}
				return versions;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_pdf_get_versions = Interop.downcallHandle("cairo_pdf_get_versions",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Get the string representation of this version. This function will return
	 * {@code null} if version isn't valid. See {@link #getVersions()} for a way to
	 * get the list of valid versions.
	 * 
	 * @return the string associated this version.
	 * @since 1.10
	 */
	@Override
	public String toString() {
		try {
			MemorySegment result = (MemorySegment) cairo_pdf_version_to_string.invoke(ordinal());
			return Interop.getStringFrom(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_pdf_version_to_string = Interop.downcallHandle(
			"cairo_pdf_version_to_string",
			FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded(), ValueLayout.JAVA_INT), false);
}
