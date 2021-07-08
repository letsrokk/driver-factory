package com.github.letsrokk.factories.selenoid;

import java.util.Arrays;

public enum ScreenResolution {

    //4:3
    VGA_640x480(640, 480),
    SVGA_800x600(800, 600),
    XGA_1024x768(1024, 768),
    XGA_PLUS_1152x768(1152, 768),
    XGA_PLUS_1152x864(1152, 864),
    SXGA_1280x1024(1280, 1024),
    SXGA_PLUS_1400x1050(1400, 1050),
    UXGA_1600x1200(1600, 1200),
    QXGA_2048x1536(2048, 1536),

    //16:9 and 16:10
    WXGA_1280x768(1280, 768),
    WXGA_1280x800(1280, 800),
    WXGA_1366x768(1366, 768),
    WXGA_PLUS_1280x854(1280, 854),
    WXGA_PLUS_1440x900(1440, 900),
    WXGA_PLUS_1440x960(1440, 960),
    WSXGA_1600x900(1600, 900),
    WSXGA_1600x1024(1600, 1024),
    WSXGA_PLUS_1680x1050(1680, 1050),
    WUXGA_1920x1200(1920, 1200),
    WQXGA_2560x1600(2560, 1600),
    WQUXGA_3840x2400(3840, 2400),

    //Desktops and TVs
    HD_1280x720(1280, 720),
    FHD_1920x1080(1920, 1080),
    WQHD_2560x1440(2560, 1440),
    UHD4k_3840x2160(3840, 2160);

    private int width;
    private int height;
    private int depth;

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getDepth() {
        return this.depth;
    }

    public String toString() {
        return width + "x" + height + "x" + depth;
    }

    ScreenResolution(Integer width, Integer height) {
        this(width, height, 24);
    }

    ScreenResolution(Integer width, Integer height, Integer depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public static ScreenResolution lookup(final int width, final int height) {
        return Arrays.stream(ScreenResolution.class.getEnumConstants())
                .filter(resolution -> (resolution.getWidth() >= width && resolution.getHeight() >= height))
                .sorted()
                .findFirst()
                .orElse(UHD4k_3840x2160);
    }

}
