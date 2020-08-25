package org.fxclub.qa.factories.selenium;

import lombok.Getter;

@Getter
public enum MobileDevice {

    //https://www.mydevice.io/#compare-devices
    IPHONE_X(375, 812, 3, "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"),
    IPAD_PRO(1366, 1024, 2, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Safari/605.1.15"),

    GALAXY_S8_PLUS(360, 740, 4, "Mozilla/5.0 (Linux; Android 7.0;SAMSUNG SM-G955F Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/5.2 Chrome/51.0.2704.106 Mobile Safari/537.36");

    private Integer width;
    private Integer height;
    private Integer pixelRatio;
    private String userAgent;

    MobileDevice(Integer width, Integer height, Integer pixelRatio, String userAgent) {
        this.width = width;
        this.height = height;
        this.pixelRatio = pixelRatio;
        this.userAgent = userAgent;
    }

}
