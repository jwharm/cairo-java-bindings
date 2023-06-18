package org.freedesktop.cairo;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import io.github.jwharm.cairobindings.Interop;

/**
 * This class contains global declarations that do not belong in a specific
 * cairo class definition.
 */
public final class Cairo {

	static {
		Interop.ensureInitialized();
	}
	
	// Prohibit instantiation
	private Cairo() {
	}

	/**
	 * Returns the version of the cairo library as a human-readable string of the
	 * form "X.Y.Z".
	 * <p>
	 * Cairo has a three-part version number scheme. In this scheme, we use even vs.
	 * odd numbers to distinguish fixed points in the software vs. in-progress
	 * development, (such as from git instead of a tar file, or as a "snapshot" tar
	 * file as opposed to a "release" tar file).
	 * 
	 * <pre>
	 * 	 _____ Major. Always 1, until we invent a new scheme.
	 * 	/  ___ Minor. Even/Odd = Release/Snapshot (tar files) or Branch/Head (git)
	 * 	| /  _ Micro. Even/Odd = Tar-file/git
	 * 	| | /
	 * 	1.0.0
	 * </pre>
	 * 
	 * Here are a few examples of versions that one might see.
	 * 
	 * <pre>
	 * 	Releases
	 * 	--------
	 * 	1.0.0 - A major release
	 * 	1.0.2 - A subsequent maintenance release
	 * 	1.2.0 - Another major release
	 * 	 
	 * 	Snapshots
	 * 	---------
	 * 	1.1.2 - A snapshot (working toward the 1.2.0 release)
	 * 	 
	 * 	In-progress development (eg. from git)
	 * 	--------------------------------------
	 * 	1.0.1 - Development on a maintenance branch (toward 1.0.2 release)
	 * 	1.1.1 - Development on head (toward 1.1.2 snapshot and 1.2.0 release)
	 * </pre>
	 * 
	 * <strong>Compatibility</strong>
	 * <p>
	 * The API/ABI compatibility guarantees for various versions are as follows.
	 * First, let's assume some cairo-using application code that is successfully
	 * using the API/ABI "from" one version of cairo. Then let's ask the question
	 * whether this same code can be moved "to" the API/ABI of another version of
	 * cairo. Moving from a release to any later version (release, snapshot,
	 * development) is always guaranteed to provide compatibility. Moving from a
	 * snapshot to any later version is not guaranteed to provide compatibility,
	 * since snapshots may introduce new API that ends up being removed before the
	 * next release. Moving from an in-development version (odd micro component) to
	 * any later version is not guaranteed to provide compatibility. In fact,
	 * there's not even a guarantee that the code will even continue to work with
	 * the same in-development version number. This is because these numbers don't
	 * correspond to any fixed state of the software, but rather the many states
	 * between snapshots and releases.
	 * 
	 * @return a string containing the version.
	 * @since 1.0
	 */
	public static String version() {
		try {
			MemorySegment result = (MemorySegment) cairo_version_string.invoke();
			if (MemorySegment.NULL.equals(result)) {
				return null;
			}
			return result.getUtf8String(0);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_version_string = Interop.downcallHandle("cairo_version_string",
			FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded()), false);
}
