package org.freedesktop.cairo;

/**
 * {@code PDFMetadata} is used by the cairo_pdf_surface_set_metadata() function
 * specify the metadata to set.
 * 
 * @since 1.16
 */
public enum PDFMetadata {

	/**
	 * The document title
	 * 
	 * @since 1.16
	 */
	TITLE,

	/**
	 * The document author
	 * 
	 * @since 1.16
	 */
	AUTHOR,

	/**
	 * The document subject
	 * 
	 * @since 1.16
	 */
	SUBJECT,

	/**
	 * The document keywords
	 * 
	 * @since 1.16
	 */
	KEYWORDS,

	/**
	 * The document creator
	 * 
	 * @since 1.16
	 */
	CREATOR,

	/**
	 * The document creation date
	 * 
	 * @since 1.16
	 */
	CREATE_DATE,

	/**
	 * The document modification date
	 * 
	 * @since 1.16
	 */
	MOD_DATE;

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
	public static PDFMetadata of(int ordinal) {
		return values()[ordinal];
	}
}
