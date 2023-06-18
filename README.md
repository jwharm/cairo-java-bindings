# cairo-java-bindings
Java language bindings for the [Cairo](https://www.cairographics.org) graphics library using the JEP-434 Panama FFI

This is still a work in progress and not finished.

A simple usage example, ported from [the first sample on this page](https://www.cairographics.org/samples/):

```java
import org.freedesktop.cairo.*;
import java.io.IOException;

public class CairoExample {

    public static void main(String[] args) throws IOException {
        // Create surface
        var surface = ImageSurface.create(Format.ARGB32, 300, 300);

        // Create drawing context
        var cr = Context.create(surface);

        double xc = 128.0;
        double yc = 128.0;
        double radius = 100.0;
        double angle1 = 45.0  * (Math.PI/180.0); // angles are specified
        double angle2 = 180.0 * (Math.PI/180.0); // in radians

        // Draw shapes
        cr.setLineWidth(10.0)
          .arc(xc, yc, radius, angle1, angle2)
          .stroke();

        cr.setSourceRGBA(1.0, 0.2, 0.2, 0.6)
          .setLineWidth(6.0)
          .arc(xc, yc, 10.0, 0.0, 2 * Math.PI)
          .fill();

        cr.arc(xc, yc, radius, angle1, angle1)
          .lineTo(xc, yc)
          .arc(xc, yc, radius, angle2, angle2)
          .lineTo(xc, yc)
          .stroke();

        // Write image to png file
        surface.writeToPNG("example.png");
    }
}
```

The library is primarily meant to be used with [Java-GI](https://github.com/jwharm/java-gi), but can also be used independently.

## Building

The project can be built with [bld](https://rife2.com/bld).
- Run `./bld download` to initialize `bld`.
- Run `./bld jar jar-sources jar-javadoc` to build the project. The jar files can be found in the `build/dist/` directory.
