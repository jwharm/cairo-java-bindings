# Cairo Java bindings
Java language bindings for the [cairo](https://www.cairographics.org) graphics library using the 
JEP-434 Panama FFI. The bindings are based on **cairo 1.16** and work with **JDK 20** (with preview 
features enabled). 

I created these language bindings primarily as a companion to the GObject-Introspection-based Java 
language bindings for Gtk and GStreamer generated with [Java-GI](https://github.com/jwharm/java-gi). 
The bindings depend on the `glib` module from Java-GI for a few common base classes and interfaces.

## Overview

### Java API

In general, the Java bindings match the cairo C API, but with a Java "coding style". C structs like 
`cairo_t`, `cairo_surface_t` and `cairo_matrix_t` are modeled with Java `Proxy` 
classes like `Context`, `Surface` and `Matrix`, and all flags and enumerations are 
available as Java enums. The proxy classes inherit when applicable: `RadialGradient` extends 
`Gradient`, which extends `Pattern`, and `ImageSurface` extends `Surface`. Types, 
functions and parameters follow Java (camel case) naming practices, so 
`cairo_move_to(*cr, x, y)` becomes `cr.moveTo(x, y)`. Out-parameters in the C API 
are mapped to return values in Java. Multiple out parameters (like coordinates) are mapped to a 
`Point`, `Circle` or `Rectangle` return type in Java.

### Resource allocation and disposal

Resources are allocated and deallocated automatically, so there is no need to manually dispose 
cairo resources in Java. However, please be aware that the disposal of proxy objects (like Context, 
surfaces, matrices and patterns) is initiated by the Java garbage collector, which does not know 
about the native resources, and might wait an indefinite amount of time before the objects are 
effectively disposed. Therefore, manual calls to `destroy()` are still possible in case the 
normal cleanup during GC is not sufficient to prevent resource exhaustion.

### Error handling

Cairo status codes are checked in the language binding, and throw exceptions 
(IllegalStateException, IllegalArgumentException or IOException) with the detailed status 
description (from `cairo_status_to_string()`). The exceptions are documented in the 
Javadoc, except for the `CAIRO_STATUS_NO_MEMORY` status, which is not documented and will 
throw a RuntimeException if it occurs. If your application consumes a lot of memory, add try-catch 
blocks for this situation where applicable.

### Other notable features

Some other features that the language bindings offer:

* In the `Context`, `Surface` and `Pattern` classes (like `Mesh`), methods that return 
  `void` in the C API, return `this` in Java, to allow method chaining.

* The `Path` class is iterable, and path traversal is implemented with `PathElement` 
  objects. The `PathElement` type is a sealed interface implemented by a record type for every 
  path operation. They can be iterated and processed with record patterns (JEP 440). See the 
  `Path` class javadoc for example code.

* The `cairo_set_user_data()` and `cairo_get_user_data()` functions (to attach 
  custom data to a cairo struct) are available in Java, with a twist. You can call 
  `setUserData()` to attach any Java object instance, and `getUserData()` to get it 
  back. Objects that can be marshaled to a native memory segment (primitive types, memory segments, 
  and other `Proxy` objects) will be attached to the native cairo struct. Other types will only 
  be attached to the Java object and will not be passed to cairo itself.

* I/O operations in cairo that are designed to work with streams accept Java `InputStream` and 
  `OutputStream` parameters.
  
* The `Surface` and `Device` classes implement `AutoCloseable` and can be used in 
  try-with-resources blocks. (The `close()` method calls the C `cairo_..._finish()` 
  function.)

* The cairo Script surface has been split into a `Script` class that inherits from 
  `Device`, and a `ScriptSurface` class that inherits from `Surface`.

* The functions for reading and comparing cairo version information are available in Java as static 
  methods in the `Cairo` class.

* Basic functionality is included to load fonts with FreeType2 for use with cairo.

## API Documentation

All API documentation is available as Javadoc, and has been reworked to use Javadoc syntax and 
cross-reference between Java classes and methods. You can 
[lookup the Javadoc online](https://jwharm.github.io/cairo-java-bindings/javadoc/), or download 
the javadoc or sources jar to use in your IDE.

## License

The bindings are available to be redistributed and/or modified under the terms of  the GNU Lesser 
General Public License (LGPL) version 2.1 (which is also one of the licenses of cairo itself.)

## Status

Some things that are still on the to-do list:

* Platform-specific surface types and font implementations

* The bindings should work on all platforms that support JDK 20 and cairo, but have only been 
  tested on Linux and Windows.

PRs and issue reports are welcome.

## Usage

Thanks to [jitpack.io](https://jitpack.io/#jwharm/cairo-java-bindings), you can simply include 
the library in your `gradle.build` or `pom.xml` file:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.jwharm:cairo-java-bindings:0.1.0'
}
```

Furthermore, you obviously need to have the cairo library version 1.16 installed on your system, 
or else the Java bindings have nothing to bind to. You also need to install JDK 20 (not JDK 19 or 
earlier, nor the JDK 21 early-access version), because the JEP-434 Panama FFI is slightly different 
between JDK versions.

Now, you can start developing with cairo in Java. Have fun! This is a simple example to get started, 
ported from [the first sample on this page](https://www.cairographics.org/samples/):

```java
import org.freedesktop.cairo.*;
import java.io.IOException;

public class CairoExample {

    public static void main(String[] args) throws IOException {
        // Create surface
        var surface = ImageSurface.create(Format.ARGB32, 300, 300);

        // Create drawing context
        var cr = Context.create(surface);

        double x = 128.0;
        double y = 128.0;
        double radius = 100.0;
        double angle1 = 45.0  * (Math.PI/180.0); // angles are specified
        double angle2 = 180.0 * (Math.PI/180.0); // in radians

        // Draw shapes
        cr.setLineWidth(10.0)
          .arc(x, y, radius, angle1, angle2)
          .stroke();

        cr.setSourceRGBA(1.0, 0.2, 0.2, 0.6)
          .setLineWidth(6.0)
          .arc(x, y, 10.0, 0.0, 2 * Math.PI)
          .fill();

        cr.arc(x, y, radius, angle1, angle1)
          .lineTo(x, y)
          .arc(x, y, radius, angle2, angle2)
          .lineTo(x, y)
          .stroke();

        // Write image to png file
        surface.writeToPNG("example.png");
    }
}
```

When compiling and running the application, make sure to enable preview features with 
`--enable-preview` and set the Java version to 20. To suppress warnings about unsafe native 
access, you can optionally add `--enable-native-access=ALL-UNNAMED`.

## Building and Contributing

The project can be built with [bld](https://rife2.com/bld), the pure-java build tool from the 
developers of Rife2. The build file is very simple, and can be found under the `src/bld` tree.

Because the entire `bld` build process is Java-based, as long as you have JDK 20 installed, you 
don't need to download any build tools, compilers, or development packages.

- Run `./bld download` to initialize `bld` and download dependencies.

- Run `./bld publish` to build the project. The jar files can be found in the `build/dist/` 
  directory, and are also published to MavenLocal.

- Run `./bld test` to run the tests.

You can run `./bld` to see all other operations that are available.

The repository contains IntelliJ IDEA project files, to let it recognize the `bld` project 
structure.

Please contribute PRs or log issues on [Github](https://github.com/jwharm/cairo-java-bindings).
