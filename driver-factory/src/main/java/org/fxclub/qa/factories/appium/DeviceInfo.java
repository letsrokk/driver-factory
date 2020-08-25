package org.fxclub.qa.factories.appium;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.Platform;

@Getter
@Setter
@Builder
public class DeviceInfo {

    private String CODE;

    private DeviceType TYPE;
    private Platform PLATFORM;
    private String PLATFORM_VERSION;
    private String DEVICE_NAME;
    private String BROWSER_NAME;
    private String BROWSER_VERSION;

    private boolean REAL;

    private String UDID;

    public boolean isIOS() {
        return PLATFORM.equals(Platform.IOS);
    }

    public boolean isANDROID() {
        return PLATFORM.equals(Platform.ANDROID);
    }

    @Override
    public String toString() {
        return PLATFORM + " " + PLATFORM_VERSION + " " + TYPE;
    }

}
