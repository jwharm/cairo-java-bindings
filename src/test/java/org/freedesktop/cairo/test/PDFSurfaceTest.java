package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;

class PDFSurfaceTest {

	@Test
	void testCreateStringIntInt() {
		PDFSurface s = PDFSurface.create("", 120, 120);
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testCreateOutputStreamIntInt() {
		AtomicBoolean success = new AtomicBoolean();
		OutputStream stream = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				success.set(true);
			}
		};
		PDFSurface s = PDFSurface.create(stream, 120, 120);
		assertTrue(success.get());
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testRestrictToVersion() {
		PDFSurface s = PDFSurface.create("", 120, 120);
		s.restrictToVersion(PDFVersion.VERSION_1_4);
	}

	@Test
	void testSetSize() {
		PDFSurface s = PDFSurface.create("", 120, 120);
		s.setSize(100, 100);
	}

	@Test
	void testAddOutline() {
		PDFSurface s = PDFSurface.create("", 120, 120);
		s.addOutline(PDFSurface.CAIRO_PDF_OUTLINE_ROOT, "test", "test", PDFOutlineFlags.ITALIC);
	}

	@Test
	void testSetMetadata() {
		PDFSurface s = PDFSurface.create("", 120, 120);
		s.setMetadata(PDFMetadata.TITLE, "test document");
	}

	@Test
	void testSetPageLabel() {
		PDFSurface s = PDFSurface.create("", 120, 120);
		s.setPageLabel("label");
	}

	@Test
	void testSetThumbnailSize() {
		PDFSurface s = PDFSurface.create("", 120, 120);
		s.setThumbnailSize(30, 30);
	}
}
