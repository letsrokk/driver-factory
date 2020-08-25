package org.fxclub.qa.factories.aws;

import io.appium.java_client.remote.MobileCapabilityType;
import org.fxclub.qa.factories.appium.DeviceInfo;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;

public class AWSCapabilities {

    public static DesiredCapabilities getCapabilities() throws IOException, InterruptedException {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        DeviceInfo deviceInfo = DeviceInfo.builder()
                .DEVICE_NAME(getDeviceName())
                .PLATFORM(getPlatform())
                .UDID(getUUID())
                .REAL(true)
                .build();

        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, deviceInfo.getPLATFORM());

        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceInfo.getDEVICE_NAME());
        if (getPlatform() == Platform.IOS) {
            capabilities.setCapability(MobileCapabilityType.UDID, deviceInfo.getUDID());
        }

        // Set new command timeout in Seconds
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 600);

        return capabilities;
    }

    private static File getApplication() {
        return new File(System.getProperty("DEVICEFARM_APP_PATH", System.getenv("DEVICEFARM_APP_PATH")));
    }

    public static Platform getPlatform() {
        return Platform.fromString(
                System.getProperty("DEVICEFARM_DEVICE_PLATFORM_NAME", System.getenv("DEVICEFARM_DEVICE_PLATFORM_NAME"))
        );
    }

    private static String getDeviceName() {
        return System.getProperty("DEVICEFARM_DEVICE_NAME", System.getenv("DEVICEFARM_DEVICE_NAME"));
    }

    private static String getUUID() {
        return System.getProperty("DEVICEFARM_DEVICE_UDID", System.getenv("DEVICEFARM_DEVICE_UDID"));
    }

}
