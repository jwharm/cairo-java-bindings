package io.github.jwharm.cairobindings.bld;

import rife.bld.Project;

import java.util.List;

import static rife.bld.dependencies.Repository.*;
import static rife.bld.dependencies.Scope.*;

/**
 * Build definition for the cairo-java-bindings project.
 * <p>
 * Run {@code ./bld download jar} on the command-line to download dependencies,
 * compile the project and create a {@code cairo-1.6.0-0.1.jar} file.
 */
public class CairoBindingsBuild extends Project {
	
    public CairoBindingsBuild() {
        pkg = "io.github.jwharm.cairobindings.bld";
        name = "cairo";
        version = version(1,16,0,"-0.1");
        javaRelease = 20;

        compileOperation().compileOptions().enablePreview();

        repositories = List.of(MAVEN_CENTRAL);
        scope(test)
            .include(dependency("org.junit.jupiter", "junit-jupiter", version(5,9,3)))
            .include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1,9,3)));
    }

    public static void main(String[] args) {
        new CairoBindingsBuild().start(args);
    }
}