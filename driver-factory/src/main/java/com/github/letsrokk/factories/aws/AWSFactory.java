package com.github.letsrokk.factories.aws;

import com.google.inject.Singleton;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import lombok.extern.log4j.Log4j2;
import com.github.letsrokk.factories.appium.AbstractAppiumFactory;
import com.github.letsrokk.factories.selenium.WTFWebDriver;
import com.github.letsrokk.grid.GridApiUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.util.Optional;

@Log4j2
@Singleton
public class AWSFactory extends AbstractAppiumFactory {

    @Override
    public AppiumDriver getDriver() throws IOException, InterruptedException {
        if (driver.get() == null) {
            DesiredCapabilities capabilities = AWSCapabilities.getCapabilities();

            Optional.ofNullable(FULL_RESET.get())
                    .ifPresent(v -> capabilities.setCapability(MobileCapabilityType.FULL_RESET, v));

            Optional.ofNullable(NO_RESET.get())
                    .ifPresent(v -> capabilities.setCapability(MobileCapabilityType.NO_RESET, v));

            Optional.ofNullable(AUTO_WEBVIEW.get())
                    .ifPresent(autoWebView -> capabilities.setCapability(MobileCapabilityType.AUTO_WEBVIEW, autoWebView));

            if (capabilities.getPlatform().equals(Platform.ANDROID)) {
                driver.set(new AndroidDriver(REMOTE_ADDRESS.get(), capabilities));
            } else if (capabilities.getPlatform().equals(Platform.IOS)) {
                Optional.ofNullable(START_IWDP.get())
                        .ifPresent(v -> capabilities.setCapability("startIWDP", v));

                driver.set(new IOSDriver(REMOTE_ADDRESS.get(), capabilities));
            } else {
                throw new IllegalArgumentException("Unsupported platform: " + capabilities.getPlatform());
            }
            String sessionId = driver.get().getSessionId().toString();
            logger.info("AWS: session created: " + sessionId);
        }
        return driver.get();
    }

    @Override
    public WTFWebDriver getExtendedDriver() throws IOException, InterruptedException {
        WTFWebDriver driver = extendedDriver.get();
        if (driver == null) {
            driver = new WTFWebDriver(getDriver());
            extendedDriver.set(driver);
        }
        return driver;
    }

    @Override
    public GridApiUtils getGridApiUtils() {
        throw new UnsupportedOperationException("Grid Api is not available for AWS Factory");
    }

    @Override
    public Platform getPlatform() {
        if (driver.get() != null) {
            return getCapabilities().getPlatform();
        } else {
            return AWSCapabilities.getPlatform();
        }
    }

    @Override
    public String getLiveViewUrl() {
        throw new UnsupportedOperationException("Unsupported by AWSFactory");
    }

    @Override
    public String getVideoLink() {
        throw new UnsupportedOperationException("Unsupported by AWSFactory");
    }

}
