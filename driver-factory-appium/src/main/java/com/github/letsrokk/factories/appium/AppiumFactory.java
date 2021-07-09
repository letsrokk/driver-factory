package com.github.letsrokk.factories.appium;

import com.github.letsrokk.grid.GridApiUtils;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.util.Optional;

@Log4j2
@Singleton
public class AppiumFactory extends AbstractAppiumFactory {

    private static ThreadLocal<GridApiUtils> gridApiUtils = new InheritableThreadLocal<>();

    @Override
    public AppiumDriver getDriver() throws IOException, InterruptedException {
        if (driver.get() == null) {
            DesiredCapabilities capabilities = AppiumCapabilities.getCapabilities(DRIVER_MODE.get(), getGridApiUtils());

            Optional.ofNullable(FULL_RESET.get())
                    .ifPresent(v -> capabilities.setCapability(MobileCapabilityType.FULL_RESET, v));

            Optional.ofNullable(NO_RESET.get())
                    .ifPresent(v -> capabilities.setCapability(MobileCapabilityType.NO_RESET, v));

            Optional.ofNullable(AUTO_WEBVIEW.get())
                    .ifPresent(autoWebView -> capabilities.setCapability(MobileCapabilityType.AUTO_WEBVIEW, autoWebView));

            if (capabilities.getPlatform().equals(Platform.ANDROID)) {
                String automationName = Optional.ofNullable(AUTOMATION_NAME.get())
                        .orElse(AutomationName.defaultANDROID());
                capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, automationName);

                ImmutableMap<String, Object> chromeOptions = getChromeOptions();
                if (!chromeOptions.isEmpty()) {
                    capabilities.setCapability(AndroidMobileCapabilityType.CHROME_OPTIONS, chromeOptions);
                }

                driver.set(new AndroidDriver(REMOTE_ADDRESS.get(), capabilities));
            } else if (capabilities.getPlatform().equals(Platform.IOS)) {
                String automationName = Optional.ofNullable(AUTOMATION_NAME.get())
                        .orElse(AutomationName.defaultIOS());
                capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, automationName);

                Optional.ofNullable(XCODE_CONFIG_FILE.get())
                        .ifPresent(v -> capabilities.setCapability(IOSMobileCapabilityType.XCODE_CONFIG_FILE, v));

                Optional.ofNullable(START_IWDP.get())
                        .ifPresent(v -> capabilities.setCapability(IOSMobileCapabilityType.START_IWDP, v));

                driver.set(new IOSDriver(REMOTE_ADDRESS.get(), capabilities));
            } else {
                throw new IllegalArgumentException("Unsupported platform: " + capabilities.getPlatform());
            }
            String sessionId = driver.get().getSessionId().toString();
            logger.info("APPIUM: session created: " + sessionId);
        }
        return driver.get();
    }

    @Override
    public WTFAppiumDriver getExtendedDriver() throws IOException, InterruptedException {
        WTFAppiumDriver driver = extendedDriver.get();
        if (driver == null) {
            driver = new WTFAppiumDriver(getDriver());
            extendedDriver.set(driver);
        }
        return driver;
    }

    @Override
    public GridApiUtils getGridApiUtils() {
        GridApiUtils utils = gridApiUtils.get();
        if (utils == null) {
            utils = GridApiUtils.create(REMOTE_ADDRESS.get());
            gridApiUtils.set(utils);
        }
        return utils;
    }

    @Override
    public Platform getPlatform() {
        if (driver.get() != null) {
            return getCapabilities().getPlatform();
        } else {
            return AppiumCapabilities.getPlatform();
        }
    }

    @Override
    public String getLiveViewUrl() {
        throw new UnsupportedOperationException("Unsupported by AppiumFactory");
    }

    @Override
    public String getVideoLink() {
        throw new UnsupportedOperationException("Unsupported by AppiumFactory");
    }

}
