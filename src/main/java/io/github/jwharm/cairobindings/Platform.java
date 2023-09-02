package io.github.jwharm.cairobindings;

/**
 * The Platform class provides utility functions to retrieve the runtime platform and
 * and check if a function is supported on the runtime platform.
 */
public final class Platform {

    private static String runtimePlatform = null;

    // Prevent instantiation
    private Platform() {}

    /**
     * Determine the runtime platform
     * @return the runtime platform: "windows", "linux" or "macos"
     */
    public static String getRuntimePlatform() {
        if (runtimePlatform == null) {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("windows")) {
                runtimePlatform = "windows";
            } else if (osName.contains("linux")) {
                runtimePlatform = "linux";
            } else {
                runtimePlatform = "macos";
            }
        }
        return runtimePlatform;
    }
}
