package io.github.jwharm.cairobindings.bld;

import rife.bld.Project;

import java.util.List;

import static rife.bld.dependencies.Repository.*;
import static rife.bld.dependencies.Scope.*;

public class CairoBindingsBuild extends Project {
    public CairoBindingsBuild() {
        pkg = "io.github.jwharm.cairobindings.bld";
        name = "cairo";
        mainClass = "io.github.jwharm.cairobindings.bld.CairoBindingsMain";
        version = version(1,16,0);

        compileOperation().compileOptions()
            .release(20)
            .enablePreview();

        downloadSources = true;
        repositories = List.of(MAVEN_CENTRAL, RIFE2_RELEASES);
        scope(test)
            .include(dependency("org.junit.jupiter", "junit-jupiter", version(5,9,3)))
            .include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1,9,3)));
    }

    public static void main(String[] args) {
        new CairoBindingsBuild().start(args);
    }
}