package io.github.jwharm.cairobindings.bld;

import static rife.bld.dependencies.Repository.MAVEN_CENTRAL;
import static rife.bld.dependencies.Repository.MAVEN_LOCAL;
import static rife.bld.dependencies.Scope.compile;
import static rife.bld.dependencies.Scope.test;

import java.util.List;

import rife.bld.Project;
import rife.bld.publish.PublishDeveloper;
import rife.bld.publish.PublishInfo;
import rife.bld.publish.PublishLicense;

/**
 * Build definition for the cairo-java-bindings project.
 * <p>
 * Run {@code ./bld download publish} on the command-line to download
 * dependencies, compile the project and create a {@code cairo-x.y.z-i.j.jar}
 * file.
 * <p>
 * Version x.y.z is the version of cairo, the i.j qualifier is the version of
 * the Java bindings.
 */
public class CairoBindingsBuild extends Project {

    public CairoBindingsBuild() {
        pkg = "org.freedesktop.cairo";
        name = "cairo";
        version = version(1,16).withQualifier("0.1");
        javaRelease = 20;

        compileOperation().compileOptions()
            .modulePath(libCompileDirectory())
            .enablePreview();

        testOperation().javaOptions()
            .enablePreview()
            .enableNativeAccess(List.of("ALL-UNNAMED"));
        
        javadocOperation().javadocOptions()
            .modulePath(libCompileDirectory())
            .enablePreview()
            .quiet();

        repositories = List.of(MAVEN_CENTRAL, MAVEN_LOCAL);

        scope(compile)
            .include(dependency("io.github.jwharm.javagi", "glib", version("2.76-0.6")));

        scope(test)
            .include(dependency("org.junit.jupiter", "junit-jupiter", version(5,9,3)))
            .include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1,9,3)));
        
        publishOperation()
            .repository(MAVEN_LOCAL)
	        .info(new PublishInfo()
	            .groupId("io.github.jwharm.cairobindings")
                .artifactId("cairo")
                .description("Java bindings for cairo")
                .url("https://github.com/jwharm/cairo-java-bindings")
                .developer(new PublishDeveloper()
                    .id("jwharm")
                    .name("Jan-Willem Harmannij")
                    .url("https://github.com/jwharm"))
                .license(new PublishLicense()
                    .name("GNU Lesser General Public License, version 3")
                    .url("https://www.gnu.org/licenses/lgpl-3.0.txt")));
    }

    public static void main(String[] args) {
        new CairoBindingsBuild().start(args);
    }
}