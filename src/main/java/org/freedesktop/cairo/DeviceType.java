package org.freedesktop.cairo;

/**
 * DeviceType is used to describe the type of a given device. The
 * devices types are also known as "backends" within cairo.
 * <p>
 * The device type can be queried with {@link Device#getType()}.
 * <p>
 * The various {@link Device} functions can be used with devices of any type,
 * but some backends also provide type-specific functions that are available 
 * as methods in the class for the appropriate device.
 * <p>
 * New entries may be added in future versions.
 * 
 * @since 1.10
 */
public enum DeviceType {

    /**
     * The device is of type Direct Render Manager
     * 
     * @since 1.10
     */
    DRM(0),

    /**
     * The device is of type OpenGL
     * 
     * @since 1.10
     */
    GL(1),

    /**
     * The device is of type script
     * 
     * @since 1.10
     */
    SCRIPT(2),

    /**
     * The device is of type xcb
     * 
     * @since 1.10
     */
    XCB(3),

    /**
     * The device is of type xlib
     * 
     * @since 1.10
     */
    XLIB(4),

    /**
     * The device is of type XML
     * 
     * @since 1.10
     */
    XML(5),

    /**
     * The device is of type cogl
     * 
     * @since 1.12
     */
    COGL(6),

    /**
     * The device is of type win32
     * 
     * @since 1.12
     */
    WIN32(7),

    /**
     * The device is invalid
     * 
     * @since 1.10
     */
    INVALID(-1);

    private final int value;

    DeviceType(int value) {
        this.value = value;
    }

    /**
     * Return the value of this enum
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the enum member for the given value.
     *
     * @param value the value of the enum member
     * @return the enum member for the given value
     */
    public static DeviceType of(int value) {
        if (value == 0) {
            return DRM;
        } else if (value == 1) {
            return GL;
        } else if (value == 2) {
            return SCRIPT;
        } else if (value == 3) {
            return XCB;
        } else if (value == 4) {
            return XLIB;
        } else if (value == 5) {
            return XML;
        } else if (value == 6) {
            return COGL;
        } else if (value == 7) {
            return WIN32;
        } else if (value == -1) {
            return INVALID;
        } else {
            throw new IllegalArgumentException("No DeviceType enum with value " + value);
        }
    }
}
