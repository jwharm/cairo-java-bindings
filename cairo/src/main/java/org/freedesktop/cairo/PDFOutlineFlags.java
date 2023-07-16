package org.freedesktop.cairo;

/**
 * {@code PDFOutlineFlags} is used by the cairo_pdf_surface_add_outline()
 * function specify the attributes of an outline item. These flags may be
 * bitwise-or'd to produce any combination of flags.
 * 
 * @since 1.16
 */
public enum PDFOutlineFlags {

    /**
     * The outline item defaults to open in the PDF viewer
     * 
     * @since 1.16
     */
    OPEN,

    /**
     * The outline item is displayed by the viewer in bold text
     * 
     * @since 1.16
     */
    BOLD,

    /**
     * The outline item is displayed by the viewer in italic text
     * 
     * @since 1.16
     */
    ITALIC;

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
    public static PDFOutlineFlags of(int ordinal) {
        return values()[ordinal];
    }
}
