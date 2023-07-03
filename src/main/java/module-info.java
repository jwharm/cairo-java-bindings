/**
 * This module contains Java language bindings for the <a href="https://www.cairographics.org">cairo</a>
 * graphics library using the JEP-434 Panama FFI. The bindings are based on <strong>cairo 1.16</strong>
 * and work with <strong>JDK 20</strong> (with preview features enabled).
 * <p>
 * The {@code org.freedesktop.freetype} package contains a very minimal wrapper to use the freetype font
 * backend for cairo instead of the default font API.
 */
module org.freedesktop.cairo {
    requires org.gnome.glib;
    exports org.freedesktop.cairo;
    exports org.freedesktop.freetype;
}
