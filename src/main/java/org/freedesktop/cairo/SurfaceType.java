package org.freedesktop.cairo;

/**
 * SurfaceType is used to describe the type of a given surface. The surface
 * types are also known as "backends" or "surface backends" within cairo.
 * <p>
 * The type of a surface is determined by the function used to create it, which
 * will generally be of the form of a {@code create() function}, (though see
 * {@link Surface#createSimilar(Surface, Content, int, int)} as well).
 * <p>
 * The surface type can be queried with {@link Surface#getType()}
 * <p>
 * The various {@link Surface} functions can be used with surfaces of any type,
 * but some backends also provide type-specific methods that are only available
 * on a surface of the appropriate class.
 * <p>
 * New entries may be added in future versions.
 * 
 * @since 1.2
 */
public enum SurfaceType {

	/**
	 * The surface is of type image
	 * 
	 * @since 1.2
	 */
	IMAGE,

	/**
	 * The surface is of type pdf
	 * 
	 * @since 1.2
	 */
	PDF,

	/**
	 * The surface is of type ps
	 * 
	 * @since 1.2
	 */
	PS,

	/**
	 * The surface is of type xlib
	 * 
	 * @since 1.2
	 */
	XLIB,

	/**
	 * The surface is of type xcb
	 * 
	 * @since 1.2
	 */
	XCB,

	/**
	 * The surface is of type glitz
	 * 
	 * @since 1.2
	 */
	GLITZ,

	/**
	 * The surface is of type quartz
	 * 
	 * @since 1.2
	 */
	QUARTZ,

	/**
	 * The surface is of type win32
	 * 
	 * @since 1.2
	 */
	WIN32,

	/**
	 * The surface is of type beos
	 * 
	 * @since 1.2
	 */
	BEOS,

	/**
	 * The surface is of type directfb
	 * 
	 * @since 1.2
	 */
	DIRECTFB,

	/**
	 * The surface is of type svg
	 * 
	 * @since 1.2
	 */
	SVG,

	/**
	 * The surface is of type os2
	 * 
	 * @since 1.4
	 */
	OS2,

	/**
	 * The surface is a win32 printing surface
	 * 
	 * @since 1.6
	 */
	WIN32_PRINTING,

	/**
	 * The surface is of type quartz_image
	 * 
	 * @since 1.6
	 */
	QUARTZ_IMAGE,

	/**
	 * The surface is of type script
	 * 
	 * @since 1.10
	 */
	SCRIPT,

	/**
	 * The surface is of type Qt
	 * 
	 * @since 1.10
	 */
	QT,

	/**
	 * The surface is of type recording
	 * 
	 * @since 1.10
	 */
	RECORDING,

	/**
	 * The surface is a OpenVG surface
	 * 
	 * @since 1.10
	 */
	VG,

	/**
	 * The surface is of type OpenGL
	 * 
	 * @since 1.10
	 */
	GL,

	/**
	 * The surface is of type Direct Render Manager
	 * 
	 * @since 1.10
	 */
	DRM,

	/**
	 * The surface is of type 'tee' (a multiplexing surface)
	 * 
	 * @since 1.10
	 */
	TEE,

	/**
	 * The surface is of type XML (for debugging)
	 * 
	 * @since 1.10
	 */
	XML,

	SKIA,

	/**
	 * The surface is a subsurface created with CAIRO_SURFACE_create_for_rectangle()
	 * 
	 * @since 1.10
	 */
	SUBSURFACE,

	/**
	 * This surface is of type Cogl
	 * 
	 * @since 1.12
	 */
	COGL;

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
	public static SurfaceType of(int ordinal) {
		return values()[ordinal];
	}
}
