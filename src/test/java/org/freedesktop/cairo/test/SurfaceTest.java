package org.freedesktop.cairo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.freedesktop.cairo.*;
import org.junit.jupiter.api.Test;

class SurfaceTest {

	@Test
	void testCreateSimilar() {
		Surface s1 = ImageSurface.create(Format.ARGB32, 120, 120);
		Surface s2 = Surface.createSimilar(s1, Content.COLOR_ALPHA, 120, 120);
		assertEquals(s2.status(), Status.SUCCESS);
	}

	@Test
	void testCreateSimilarImage() {
		Surface s1 = ImageSurface.create(Format.ARGB32, 120, 120);
		Surface s2 = Surface.createSimilarImage(s1, Format.ARGB32, 120, 120);
		assertEquals(s2.status(), Status.SUCCESS);
	}

	@Test
	void testCreateForRectangle() {
		Surface s1 = ImageSurface.create(Format.ARGB32, 120, 120);
		Surface s2 = Surface.createForRectangle(s1, 50, 50, 20, 20);
		assertEquals(s2.status(), Status.SUCCESS);
	}

	@Test
	void testStatus() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testFinish() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.finish();
		assertEquals(s.status(), Status.SURFACE_FINISHED);
	}

	@Test
	void testFlush() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.flush();
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testGetDevice() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.getDevice();
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testGetFontOptions() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.getFontOptions(null);
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testGetContent() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.getContent();
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testMarkDirty() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.markDirty();
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testMarkDirtyRectangle() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.markDirtyRectangle(0, 0, 10, 10);
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testSetDeviceOffset() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.setDeviceOffset(0, 0);
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testGetDeviceOffset() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.getDeviceOffset();
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testGetDeviceScale() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.getDeviceScale();
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testSetDeviceScale() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.setDeviceScale(1, 1);
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testSetFallbackResolution() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.setFallbackResolution(1, 1);
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testGetFallbackResolution() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.getFallbackResolution();
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testGetType() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.getType();
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testCopyPage() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.copyPage();
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testShowPage() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.showPage();
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testHasShowTextGlyphs() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.hasShowTextGlyphs();
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testSetMimeData() {
		fail("Not yet implemented");
	}

	@Test
	void testGetMimeData() {
		fail("Not yet implemented");
	}

	@Test
	void testSupportsMimeType() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.supportsMimeType(MimeType.PNG);
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testMapToImage() {
		Surface s = ImageSurface.create(Format.ARGB32, 120, 120);
		s.mapToImage(RectangleInt.create(0, 0, 120, 120));
		assertEquals(s.status(), Status.SUCCESS);
	}

	@Test
	void testUnmapImage() {
		Surface s1 = ImageSurface.create(Format.ARGB32, 120, 120);
		ImageSurface s2 = s1.mapToImage(RectangleInt.create(0, 0, 120, 120));
		s1.unmapImage(s2);
		assertEquals(s1.status(), Status.SUCCESS);
	}

}
