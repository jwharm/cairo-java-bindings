package org.freedesktop.cairo.surfaces;

/**
 * cairo_surface_type_t is used to describe the type of a given surface. The
 * surface types are also known as "backends" or "surface backends" within
 * cairo.
 * <p>
 * The type of a surface is determined by the function used to create it, which
 * will generally be of the form cairo_type_surface_create(), (though see
 * cairo_surface_create_similar() as well).
 * <p>
 * The surface type can be queried with cairo_surface_get_type()
 * <p>
 * The various cairo_surface_t functions can be used with surfaces of any type,
 * but some backends also provide type-specific functions that must only be
 * called with a surface of the appropriate type. These functions have names
 * that begin with cairo_type_surface such as cairo_image_surface_get_width().
 * <p>
 * The behavior of calling a type-specific function with a surface of the wrong
 * type is undefined.
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
	CAIRO_SURFACE_TYPE_IMAGE,

	/**
	 * The surface is of type pdf
	 * 
	 * @since 1.2
	 */
	CAIRO_SURFACE_TYPE_PDF,

	/**
	 * The surface is of type ps
	 * 
	 * @since 1.2
	 */
	CAIRO_SURFACE_TYPE_PS,

	/**
	 * The surface is of type xlib
	 * 
	 * @since 1.2
	 */
	CAIRO_SURFACE_TYPE_XLIB,

	/**
	 * The surface is of type xcb
	 * 
	 * @since 1.2
	 */
	CAIRO_SURFACE_TYPE_XCB,

	/**
	 * The surface is of type glitz
	 * 
	 * @since 1.2
	 */
	CAIRO_SURFACE_TYPE_GLITZ,

	/**
	 * The surface is of type quartz
	 * 
	 * @since 1.2
	 */
	CAIRO_SURFACE_TYPE_QUARTZ,

	/**
	 * The surface is of type win32
	 * 
	 * @since 1.2
	 */
	CAIRO_SURFACE_TYPE_WIN32,

	/**
	 * The surface is of type beos
	 * 
	 * @since 1.2
	 */
	CAIRO_SURFACE_TYPE_BEOS,

	/**
	 * The surface is of type directfb
	 * 
	 * @since 1.2
	 */
	CAIRO_SURFACE_TYPE_DIRECTFB,

	/**
	 * The surface is of type svg
	 * 
	 * @since 1.2
	 */
	CAIRO_SURFACE_TYPE_SVG,

	/**
	 * The surface is of type os2
	 * 
	 * @since 1.4
	 */
	CAIRO_SURFACE_TYPE_OS2,

	/**
	 * The surface is a win32 printing surface
	 * 
	 * @since 1.6
	 */
	CAIRO_SURFACE_TYPE_WIN32_PRINTING,

	/**
	 * The surface is of type quartz_image
	 * 
	 * @since 1.6
	 */
	CAIRO_SURFACE_TYPE_QUARTZ_IMAGE,

	/**
	 * The surface is of type script
	 * 
	 * @since 1.10
	 */
	CAIRO_SURFACE_TYPE_SCRIPT,

	/**
	 * The surface is of type Qt
	 * 
	 * @since 1.10
	 */
	CAIRO_SURFACE_TYPE_QT,

	/**
	 * The surface is of type recording
	 * 
	 * @since 1.10
	 */
	CAIRO_SURFACE_TYPE_RECORDING,

	/**
	 * The surface is a OpenVG surface
	 * 
	 * @since 1.10
	 */
	CAIRO_SURFACE_TYPE_VG,

	/**
	 * The surface is of type OpenGL
	 * 
	 * @since 1.10
	 */
	CAIRO_SURFACE_TYPE_GL,

	/**
	 * The surface is of type Direct Render Manager
	 * 
	 * @since 1.10
	 */
	CAIRO_SURFACE_TYPE_DRM,

	/**
	 * The surface is of type 'tee' (a multiplexing surface)
	 * 
	 * @since 1.10
	 */
	CAIRO_SURFACE_TYPE_TEE,

	/**
	 * The surface is of type XML (for debugging)
	 * 
	 * @since 1.10
	 */
	CAIRO_SURFACE_TYPE_XML,

	CAIRO_SURFACE_TYPE_SKIA,

	/**
	 * The surface is a subsurface created with CAIRO_SURFACE_create_for_rectangle()
	 * 
	 * @since 1.10
	 */
	CAIRO_SURFACE_TYPE_SUBSURFACE,

	/**
	 * This surface is of type Cogl
	 * 
	 * @since 1.12
	 */
	CAIRO_SURFACE_TYPE_COGL;

}
