package org.freedesktop.cairo;

/**
 * Whether operations should be recorded.
 *
 * @see SurfaceObserver
 * @see Surface
 */
public enum SurfaceObserverMode {

    /**
     * no recording is done
     *
     * @since 1.12
     */
    NORMAL,

    /**
     * operations are recorded
     *
     * @since 1.12
     */
    RECORD_OPERATIONS;

    /**
     * Return the value of this enum
     * @return the value
     */
    public int getValue() {
        return ordinal();
    }

    /**
     * Returns the enum constant for the given ordinal (its position in the enum
     * declaration).
     *
     * @param ordinal the position in the enum declaration, starting from zero
     * @return the enum constant for the given ordinal
     */
    public static SurfaceObserverMode of(int ordinal) {
        return values()[ordinal];
    }
}
