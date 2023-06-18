package org.freedesktop.cairo;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

import io.github.jwharm.cairobindings.Interop;

/**
 * PostScriptLevel is used to describe the language level of the PostScript
 * Language Reference that a generated PostScript file will conform to.
 * 
 * @since 1.6
 */
public enum PostScriptLevel {

	/**
	 * The language level 2 of the PostScript specification.
	 * 
	 * @since 1.6
	 */
	LEVEL_2,

	/**
	 * The language level 3 of the PostScript specification.
	 * 
	 * @since 1.6
	 */
	LEVEL_3;

	/**
	 * Return the value of this enum
	 * @return the value
	 */
	public int value() {
		return ordinal();
	}

	/**
	 * Returns the enum constant for the given ordinal (its position in the enum
	 * declaration).
	 * 
	 * @param ordinal the position in the enum declaration, starting from zero
	 * @return the enum constant for the given ordinal
	 */
	public static PostScriptLevel of(int ordinal) {
		return values()[ordinal];
	}

	static {
		Interop.ensureInitialized();
	}

	/**
	 * Used to retrieve the list of supported levels. See
	 * {@link PostScriptSurface#restrictToLevel(PostScriptLevel)}.
	 * 
	 * @return supported level list
	 * @since 1.6
	 */
	public static PostScriptLevel[] getLevels() {
		try {
			try (Arena arena = Arena.openConfined()) {
				MemorySegment levelsPtr = arena.allocate(ValueLayout.ADDRESS);
				MemorySegment numLevelsPtr = arena.allocate(ValueLayout.JAVA_INT);
				cairo_ps_get_levels.invoke(levelsPtr, numLevelsPtr);
				int numLevels = numLevelsPtr.get(ValueLayout.JAVA_INT, 0);
				int[] levelInts = MemorySegment.ofAddress(levelsPtr.address(), numLevels, arena.scope())
						.toArray(ValueLayout.JAVA_INT);
				PostScriptLevel[] levels = new PostScriptLevel[numLevels];
				for (int i = 0; i < levelInts.length; i++) {
					levels[i] = PostScriptLevel.of(levelInts[i]);
				}
				return levels;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_ps_get_levels = Interop.downcallHandle("cairo_ps_get_levels",
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS), false);

	/**
	 * Get the string representation of this level. This function will return
	 * {@code null} if the level isn't valid. See {@link #getLevels()} for a way to
	 * get the list of valid levels.
	 * 
	 * @return the string associated to this level.
	 * @since 1.6
	 */
	@Override
	public String toString() {
		try {
			MemorySegment result = (MemorySegment) cairo_ps_level_to_string.invoke(ordinal());
			return Interop.getStringFrom(result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static final MethodHandle cairo_ps_level_to_string = Interop.downcallHandle("cairo_ps_level_to_string",
			FunctionDescriptor.of(ValueLayout.ADDRESS.asUnbounded(), ValueLayout.JAVA_INT), false);
}
