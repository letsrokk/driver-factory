package org.fxclub.qa.factories.selenoid;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AndroidSkin {

    QVGA_240x320_120("QVGA"),
    WQVGA400_240x400_120("WQVGA400"),
    WQVGA432_240x432_120("WQVGA432"),
    HVGA_320x480_160("HVGA"),
    WVGA800_480x800_240("WVGA800"),
    WVGA854_480x854_240("WVGA854"),
    WSVGA_1024x600_160("WSVGA"),
    WXGA720_720x1280_320("WXGA720"),
    WXGA800_1280x800_160("WXGA800"),
    WXGA800_7in_800x1280_213("WXGA800-7in");

    private String code;
}
