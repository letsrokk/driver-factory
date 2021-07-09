package com.github.letsrokk.factories.appium;

import com.github.letsrokk.DriverFactoryEnvs;
import com.github.letsrokk.browsermob.BrowserMobUtils;
import com.github.letsrokk.factories.DriverFactory;
import com.github.letsrokk.factories.selenium.MODE;
import com.github.letsrokk.factories.selenium.WTFWebDriver;
import com.github.letsrokk.factories.selenium.exceptions.UnsupportedBrowserException;
import com.github.letsrokk.factories.utils.EnvironmentUtils;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;
import io.appium.java_client.AppiumDriver;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@Singleton
public abstract class AbstractAppiumFactory implements DriverFactory {

    protected Logger logger = LogManager.getLogger(DriverFactory.class);

    protected static ThreadLocal<AppiumDriver> driver = new InheritableThreadLocal<>();

    protected static ThreadLocal<WTFWebDriver> extendedDriver = new InheritableThreadLocal<>();

    protected static ThreadLocal<MODE> DRIVER_MODE = InheritableThreadLocal.withInitial(
            () -> {
                String driverMode =
                        EnvironmentUtils.getProperty(DriverFactoryEnvs.DRIVER_MODE, MODE.local.name()).toLowerCase();
                return MODE.valueOf(driverMode);
            }
    );

    protected static ThreadLocal<Boolean> AUTO_WEBVIEW = new InheritableThreadLocal<>();

    protected static ThreadLocal<Boolean> CROSSWALK_APPLICATION = new InheritableThreadLocal<>();

    protected static ThreadLocal<URL> REMOTE_ADDRESS = InheritableThreadLocal.withInitial(() ->
            parseRemoteAddress(EnvironmentUtils.getProperty(DriverFactoryEnvs.REMOTE_ADDRESS, "http://127.0.0.1:4444/wd/hub"))
    );

    protected static ThreadLocal<Boolean> FULL_RESET = new InheritableThreadLocal<>();

    protected static ThreadLocal<Boolean> NO_RESET = new InheritableThreadLocal<>();

    protected static ThreadLocal<Boolean> START_IWDP = new InheritableThreadLocal<>();

    protected static ThreadLocal<String> AUTOMATION_NAME = new InheritableThreadLocal<>();

    protected static ThreadLocal<String> XCODE_CONFIG_FILE = InheritableThreadLocal.withInitial(() -> "~/.xcconfig");

    protected static ThreadLocal<ChromeOptions> CHROME_OPTIONS = InheritableThreadLocal.withInitial(ChromeOptions::new);

    @Override
    public Boolean isDriverStarted() {
        return driver.get() != null;
    }

    public AbstractAppiumFactory setAutoWebView(boolean isAutoWebView) {
        AUTO_WEBVIEW.set(isAutoWebView);
        return this;
    }

    public AbstractAppiumFactory setCrosswalkApplication(boolean isCrosswalkApplication) {
        CROSSWALK_APPLICATION.set(isCrosswalkApplication);
        return this;
    }

    /**
     * Set Appium Remote Address
     *
     * @param remoteAddress Appium address URL
     * @return AppiumFactory instance
     */
    public AbstractAppiumFactory setRemoteAddress(String remoteAddress) {
        return setRemoteAddress(parseRemoteAddress(remoteAddress));
    }

    @SneakyThrows
    private static URL parseRemoteAddress(String remoteAddress) {
        return new URL(remoteAddress);
    }

    /**
     * Set Driver Mode
     *
     * @param driverMode local / remote / grid
     * @return AppiumFactory Instance
     */
    public AbstractAppiumFactory setDriverMode(MODE driverMode) {
        DRIVER_MODE.set(driverMode);
        return this;
    }

    /**
     * Set Appium Remote Address
     *
     * @param remoteAddress Appium address URL
     * @return AppiumFactory instance
     */
    public AbstractAppiumFactory setRemoteAddress(URL remoteAddress) {
        REMOTE_ADDRESS.set(remoteAddress);
        return this;
    }

    /**
     * Set 'fullReset' capability
     * Reset strategies description: http://appium.io/docs/en/writing-running-appium/other/reset-strategies/index.html
     *
     * @param fullReset true / false
     * @return AppiumFactory instance
     */
    public AbstractAppiumFactory setFullReset(boolean fullReset) {
        FULL_RESET.set(fullReset);
        return this;
    }

    /**
     * Set 'noReset' capability
     * Reset strategies description: http://appium.io/docs/en/writing-running-appium/other/reset-strategies/index.html
     *
     * @param noReset true / false
     * @return AppiumFactory instance
     */
    public AbstractAppiumFactory setNoReset(boolean noReset) {
        NO_RESET.set(noReset);
        return this;
    }

    /**
     * Set 'xcodeConfigFile' capability
     *
     * @param xcodeConfigFile default is ~/.xcconfig
     * @return AppiumFactory instance
     */
    public AbstractAppiumFactory setXcodeConfigFile(String xcodeConfigFile) {
        XCODE_CONFIG_FILE.set(xcodeConfigFile);
        return this;
    }

    /**
     * Set 'automationName' capability
     * Available Appium drivers: http://appium.io/docs/en/about-appium/getting-started/#driver-specific-setup
     *
     * @param automationName XCUITest, UiAutomator2, etc.
     * @return AppiumFactory instance
     */
    public AbstractAppiumFactory setAutomationName(String automationName) {
        AUTOMATION_NAME.set(automationName);
        return this;
    }

    /**
     * Set 'startIWDP' capability
     *
     * @param startIWDP true / false
     * @return AppiumFactory instance
     */
    public AbstractAppiumFactory setStartIWDP(boolean startIWDP) {
        START_IWDP.set(startIWDP);
        return this;
    }

    /**
     * Set 'chromeOption' capability
     *
     * @param name  capability name
     * @param value value
     * @return AppiumFactory instance
     */
    public AbstractAppiumFactory setChromeOption(String name, Object value) {
        CHROME_OPTIONS.get().setOption(name, value);
        return this;
    }

    /**
     * Set 'w3c mode' capability
     *
     * @param isEnabled w3c for ChromeDriver enabled (true / false)
     * @return AppiumFactory instance
     */
    public AbstractAppiumFactory setW3CMode(boolean isEnabled) {
        setChromeOption(ChromeOptions.W3C_MODE, isEnabled);
        return this;
    }

    public ImmutableMap<String, Object> getChromeOptions() {
        return ImmutableMap.copyOf(CHROME_OPTIONS.get().getOptions());
    }

    @Override
    public Capabilities getCapabilities() {
        return Optional.ofNullable(driver.get())
                .orElseThrow(() -> new IllegalArgumentException("Driver is not set"))
                .getCapabilities();
    }

    /**
     * Release RemoteWebDriver session
     */
    @Override
    public void releaseSession() {
        Optional.ofNullable(driver.get())
                .ifPresent(session -> {
                    releaseSession(session);
                    driver.remove();
                    extendedDriver.remove();
                });
    }

    private void releaseSession(RemoteWebDriver driver) {
        if (driver != null) {
            String sessionId = driver.getSessionId().toString();
            logger.info("APPIUM: session released: " + sessionId);
            try {
                driver.quit();
            } catch (WebDriverException e) {
                logger.error("APPIUM: session released: " + e.getMessage());
            }
        }
        BrowserMobUtils.stopProxy();
    }

    @Override
    public AppiumDriver restartDriver() throws IOException, InterruptedException, UnsupportedBrowserException {
        releaseSession();
        return (AppiumDriver<org.openqa.selenium.WebElement>) getDriver();
    }

    @Override
    public void reloadApp() {
        Optional.ofNullable(driver.get()).ifPresent(appiumDriver -> {
            if (appiumDriver.getCapabilities().getPlatform().equals(Platform.IOS)) {
                appiumDriver.resetApp();
            } else {
                appiumDriver.closeApp();
                appiumDriver.launchApp();
            }
        });
    }

}
