# cairo-java-bindings
Java language bindings for the Cairo graphics library using the JEP-434 Panama FFI

This is still a work in progress and not finished.

A simple usage example, ported from [`spiral.c`](https://gitlab.com/cairo/cairo-demos/-/blob/master/png/spiral.c) in the Cairo demos:

```java
import org.freedesktop.cairo.drawing.Context;
import org.freedesktop.cairo.surfaces.Format;
import org.freedesktop.cairo.surfaces.ImageSurface;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

/**
 * Draw an image with a spiral pattern and write it to a PNG file.
 */
public class DrawSpiral {

    public static void main(String[] args) {
        new DrawSpiral();
    }

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int STRIDE = WIDTH * 4;

    public DrawSpiral() {

        // Allocate a memory segment with the expected size.
        // The memory is released outside the try-block.
        try (Arena arena = Arena.openConfined()) {
            MemorySegment image = arena.allocate(STRIDE * HEIGHT);

            // Create an image surface
            var surface = ImageSurface.createForData(image, Format.ARGB32, WIDTH, HEIGHT, STRIDE);

            // Create a drawing context
            var cr = Context.create(surface);

            // Set the background
            cr.rectangle(0, 0, WIDTH, HEIGHT);
            cr.setSourceRGB(1, 1, 1);
            cr.fill();

            // Draw spiral
            drawSpiral(cr, WIDTH, HEIGHT);

            // Write surface to PNG file
            surface.writeToPNG("spiral.png");
        }
    }

    // Draw a spiral
    private void drawSpiral(Context cr, int w, int h) {
        double wd = 0.02 * w;
        double hd = 0.02 * h;

        int width = w - 2;
        int height = h - 2;

        cr.moveTo(width + 1, 1 - hd);
        for (int i = 0; i < 9; i++) {
            cr.relLineTo(0, height - hd * (2 * i - 1));
            cr.relLineTo(- (width - wd * (2 * i)), 0);
            cr.relLineTo(0, - (height - hd * (2 * i)));
            cr.relLineTo(width - wd * (2 * i + 1), 0);
        }

        cr.setSourceRGB(0, 0, 1);
        cr.stroke();
    }
}
```

The library is primarily meant to be used with [Java-GI](https://github.com/jwharm/java-gi), but can also be used independently.

## Building

The project can be built with [bld](https://rife2.com/bld).
- Run `./bld download` to initialize `bld`.
- Run `./bld jar jar-sources jar-javadoc` to build the project. The jar files can be found in the `build/dist/` directory.
