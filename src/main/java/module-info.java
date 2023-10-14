/**
 * This module contains Java language bindings for the <a href="https://www.cairographics.org">cairo</a>
 * graphics library using the JEP-434 Panama FFI. The bindings are based on <strong>cairo 1.16</strong>
 * and work with <strong>JDK 20</strong> (with preview features enabled).
 */
module org.freedesktop.cairo {
    requires static org.gnome.glib; // Optional dependency on java-gi when cairo-gobject is used
    exports org.freedesktop.cairo;
    exports org.freedesktop.freetype;
    exports io.github.jwharm.cairobindings;
}
