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
	DRM,

	/**
	 * The device is of type OpenGL
	 * 
	 * @since 1.10
	 */
	GL,

	/**
	 * The device is of type script
	 * 
	 * @since 1.10
	 */
	SCRIPT,

	/**
	 * The device is of type xcb
	 * 
	 * @since 1.10
	 */
	XCB,

	/**
	 * The device is of type xlib
	 * 
	 * @since 1.10
	 */
	XLIB,

	/**
	 * The device is of type XML
	 * 
	 * @since 1.10
	 */
	XML,

	/**
	 * The device is of type cogl
	 * 
	 * @since 1.12
	 */
	COGL,

	/**
	 * The device is of type win32
	 * 
	 * @since 1.12
	 */
	WIN32,

	/**
	 * The device is invalid
	 * 
	 * @since 1.10
	 */
	INVALID;

	/**
	 * Returns the enum constant for the given ordinal (its position in the enum
	 * declaration).
	 * 
	 * @param ordinal the position in the enum declaration, starting from zero
	 * @return the enum constant for the given ordinal
	 */
	public static DeviceType of(int ordinal) {
		return values()[ordinal];
	}
}
