package com.github.letsrokk.factories.selenoid;

import com.github.letsrokk.DriverFactoryEnvs;
import com.github.letsrokk.browsermob.BrowserMobUtils;
import com.github.letsrokk.factories.DriverFactory;
import com.github.letsrokk.factories.selenium.*;
import com.github.letsrokk.factories.selenium.exceptions.UnsupportedBrowserException;
import com.github.letsrokk.factories.utils.EnvironmentUtils;
import io.appium.java_client.android.AndroidDriver;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.letsrokk.grid.GridApiUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * Selenium Factory, Selenium sessions dispatcher
 *
 * @author Dmitry Mayer
 */
public class SelenoidFactory implements DriverFactory {

    private Logger logger = LogManager.getLogger(DriverFactory.class);

    private static ThreadLocal<RemoteWebDriver> driver = new InheritableThreadLocal<>();
    private static ThreadLocal<WTFWebDriver> extendedDriver = new InheritableThreadLocal<>();
    private static ThreadLocal<GridApiUtils> gridApiUtils = new InheritableThreadLocal<>();

    private static ThreadLocal<MODE> mode = InheritableThreadLocal.withInitial(() -> {
        String driverMode =
                EnvironmentUtils.getProperty(DriverFactoryEnvs.DRIVER_MODE, MODE.local.name()).toLowerCase();
        return MODE.valueOf(driverMode);
    });

    private static ThreadLocal<BROWSER> browser = InheritableThreadLocal.withInitial(() -> {
        String driverMode =
                EnvironmentUtils.getProperty(DriverFactoryEnvs.BROWSER_NAME, BROWSER.chrome.name()).toLowerCase();
        return BROWSER.valueOf(driverMode);
    });

    private static ThreadLocal<UserAgent> userAgent = new InheritableThreadLocal<>();

    private static ThreadLocal<String> version = new InheritableThreadLocal<>();

    private static ThreadLocal<URL> remoteURL = InheritableThreadLocal.withInitial(() ->
            parseRemoteAddress(EnvironmentUtils.getProperty(DriverFactoryEnvs.REMOTE_ADDRESS, "http://127.0.0.1:4444/wd/hub"))
    );

    private static ThreadLocal<FirefoxProfile> firefoxProfile = new InheritableThreadLocal<>();
    private static ThreadLocal<MutableCapabilities> extraCapabilities = new InheritableThreadLocal<>();

    private static ThreadLocal<Boolean> isVNC = InheritableThreadLocal.withInitial(() -> false);
    private static ThreadLocal<Boolean> isVideo = InheritableThreadLocal.withInitial(() -> false);

    private static ThreadLocal<Integer> selenoidUiPort = InheritableThreadLocal.withInitial(() -> 8080);

    private static ThreadLocal<ScreenResolution> screenResolution = InheritableThreadLocal.withInitial(() -> ScreenResolution.FHD_1920x1080);
    private static ThreadLocal<String> testCaseDisplayName = new InheritableThreadLocal<>();

    private static ThreadLocal<Boolean> isBrowserMobProxy = InheritableThreadLocal.withInitial(() -> false);

    private static ThreadLocal<MobileDevice> isMobileEmulation = new InheritableThreadLocal<>();

    private static ThreadLocal<URL> baseUrl = new InheritableThreadLocal<>();

    //
    //  Android Simulators capabilities
    //

    private static ThreadLocal<URI> appPath = new InheritableThreadLocal<>();
    private static ThreadLocal<String> appPackage = new InheritableThreadLocal<>();
    private static ThreadLocal<String> appActivity = new InheritableThreadLocal<>();
    private static ThreadLocal<String> androidVersion = InheritableThreadLocal.withInitial(() -> "latest");
    private static ThreadLocal<AndroidSkin> androidSkin = InheritableThreadLocal.withInitial(() -> AndroidSkin.WXGA720_720x1280_320);

    /**
     * Default constructor with parameters: LOCAL mode, CHROME browser
     */
    public SelenoidFactory() {
        // empty constructor, default values already set
    }

    /**
     * Set webdriver mode
     *
     * @param mode webdriver mode (local, remote, grid)
     * @return SelenoidFactory instance
     */
    public SelenoidFactory setMode(MODE mode) {
        SelenoidFactory.mode.set(mode);
        return this;
    }

    /**
     * Get currecnt webdriver mode
     *
     * @return mode
     */
    public MODE getMode() {
        return mode.get();
    }

    /**
     * Set browser
     *
     * @param browser browser (firefox, chrome)
     * @return SelenoidFactory instance
     */
    public SelenoidFactory setBrowser(BROWSER browser) {
        SelenoidFactory.browser.set(browser);
        return this;
    }

    /**
     * Set browser version
     *
     * @param version browser version
     * @return SelenoidFactory instance
     */
    public SelenoidFactory setVersion(String version) {
        SelenoidFactory.version.set(version);
        return this;
    }

    /**
     * Set Firefox Profile
     *
     * @param profile FirefoxProfile
     * @return SelenoidFactory instance
     */
    public SelenoidFactory setFirefoxProfile(FirefoxProfile profile) {
        SelenoidFactory.firefoxProfile.set(profile);
        return this;
    }

    /**
     * Set Extra Capabilities
     *
     * @param extraCapabilities extra capabilities (ChromeOptions, FirefoxOptions, OperaOptions, MutableCapabilities)
     * @return SelenoidFactory instance
     */
    public SelenoidFactory setExtraCapabilities(MutableCapabilities extraCapabilities) {
        SelenoidFactory.extraCapabilities.set(extraCapabilities);
        return this;
    }

    /**
     * Set RemoteWebDriver URL
     *
     * @param remoteURL remote URL for RemoteWebDriver or GRID Hub
     * @return SeleniufFactory instance
     */
    public SelenoidFactory setRemoteURL(String remoteURL) {
        setRemoteURL(parseRemoteAddress(remoteURL));
        return this;
    }

    @SneakyThrows
    private static URL parseRemoteAddress(String remoteAddress) {
        return new URL(remoteAddress);
    }

    /**
     * Set remote / GRID URL
     *
     * @param remoteURL RemoteWebDriver / GRID URL
     * @return SelenoidFactory instance
     */
    public SelenoidFactory setRemoteURL(URL remoteURL) {
        SelenoidFactory.remoteURL.set(remoteURL);
        return this;
    }

    /**
     * Enable VNC in Selenoid nodes
     *
     * @param isVNC is VNC enabled
     * @return SelenoidFactory instance
     */
    public SelenoidFactory setVNC(boolean isVNC) {
        SelenoidFactory.isVNC.set(isVNC);
        return this;
    }

    /**
     * Enable video recording in Selenoid nodes
     *
     * @param isVideo enableVideo capability value
     * @return SelenoidFactory instance
     */
    public SelenoidFactory setVideo(boolean isVideo) {
        SelenoidFactory.isVideo.set(isVideo);
        return this;
    }

    /**
     * Get enableVideo capability value
     *
     * @return enableVideo value
     */
    public boolean getVideo() {
        return isVideo.get();
    }

    /**
     * Set Selenoid UI port
     *
     * @param port por number (8080 default)
     * @return SelenoidFactory instance
     */
    public SelenoidFactory setSelenoidUiPort(Integer port) {
        selenoidUiPort.set(port);
        return this;
    }

    /**
     * Set Test Case Display Name for Selenoid UI
     *
     * @param displayName test case name
     * @return SelenoidFactory instance
     */
    public SelenoidFactory setTestCaseDisplayName(String displayName) {
        SelenoidFactory.testCaseDisplayName.set(displayName);
        return this;
    }

    /**
     * Start Browser with BrowserMobDownloader Proxy enabled
     *
     * @param isBrowserMobProxy is proxy enabled
     * @return SelenoidFactory instance
     */
    public SelenoidFactory setBrowserMobProxy(boolean isBrowserMobProxy) {
        SelenoidFactory.isBrowserMobProxy.set(isBrowserMobProxy);
        return this;
    }

    public boolean isBrowserMobProxy() {
        return isBrowserMobProxy.get();
    }

    /**
     * Set screen resolution in browser container
     *
     * @param resolution ScreenResolution enum
     * @return Selenoid Factory instance
     */
    public SelenoidFactory setScreenResolution(ScreenResolution resolution) {
        SelenoidFactory.screenResolution.set(resolution);
        return this;
    }

    /**
     * Set screen resolution in browser container.
     * Calculate closest standart resolution based on specified width and height
     *
     * @param width  screen width
     * @param height screen height
     * @return Selenoid Factory instance
     */
    public SelenoidFactory setScreenResolution(int width, int height) {
        ScreenResolution matchedResolution = ScreenResolution.lookup(width, height);
        screenResolution.set(matchedResolution);
        return this;
    }

    /**
     * Set custom UserAgent
     *
     * @param userAgent User Agent type
     * @return Selenoid Factory instance
     */
    public SelenoidFactory setUserAgent(UserAgent userAgent) {
        SelenoidFactory.userAgent.set(userAgent);
        return this;
    }

    /**
     * Enable or diable Responsive Mode in Chrome
     *
     * @param isMobileEmulation true / false
     * @return Selenoid Factory instance
     */
    public SelenoidFactory setMobileEmulation(boolean isMobileEmulation) {
        if (isMobileEmulation) {
            List<MobileDevice> devices = Arrays.asList(MobileDevice.values());
            Collections.shuffle(devices);
            setMobileEmulation(devices.get(0));
        }
        return this;
    }

    /**
     * Responsive Mode with specific device configuration in Chrome
     *
     * @param mobileDevice mobile device
     * @return Selenoid Factory instance
     */
    public SelenoidFactory setMobileEmulation(MobileDevice mobileDevice) {
        isMobileEmulation.set(mobileDevice);
        return this;
    }

    /**
     * Set base URL to open after driver start
     *
     * @param baseUrl base url
     * @return Selenoid Factory instance
     */
    public SelenoidFactory setBaseUrl(URL baseUrl) {
        SelenoidFactory.baseUrl.set(baseUrl);
        return this;
    }

    /**
     * Get Base URL value
     * @return base URL
     */
    public URL getBaseUrl() {
        return SelenoidFactory.baseUrl.get();
    }

    /**
     * Set APP path for Android Simulators
     *
     * @param appPath App Path
     * @return Selenoid Factory instance
     */
    public SelenoidFactory setAppPath(String appPath) {
        return setAppPath(URI.create(appPath));
    }

    /**
     * Set APP path for Android Simulators
     *
     * @param appPath App Path
     * @return Selenoid Factory instance
     */
    public SelenoidFactory setAppPath(URI appPath) {
        SelenoidFactory.appPath.set(appPath);
        return this;
    }

    /**
     * Set AppPackage for Android Simulators
     *
     * @param appPackage App Package
     * @return Selenoid Factory instance
     */
    public SelenoidFactory setAppPackage(String appPackage) {
        SelenoidFactory.appPackage.set(appPackage);
        return this;
    }

    /**
     * Set AppActivity for Android Simulators
     *
     * @param appActivity App Path
     * @return Selenoid Factory instance
     */
    public SelenoidFactory setAppActivity(String appActivity) {
        SelenoidFactory.appActivity.set(appActivity);
        return this;
    }

    /**
     * Set Android Skin for Android Simulators
     *
     * @param skin Android Skin
     * @return Selenoid Factory instance
     */
    public SelenoidFactory setAndroidSkin(AndroidSkin skin) {
        SelenoidFactory.androidSkin.set(skin);
        return this;
    }

    /**
     * Checkout WebDriver session from the available pool
     *
     * @return WebDriver instance
     * @throws UnsupportedBrowserException Unsupported Browser Exception
     */
    @Override
    public RemoteWebDriver getDriver() throws UnsupportedBrowserException {
        if (driver.get() == null) {
            RemoteWebDriver newDriver = createSession();
            driver.set(newDriver);
            if (mode.get() == MODE.grid && isVNC.get()) {
                String sessionViewUrl = getLiveViewUrl();
                logger.info("SELENIUM: session created: " + driver.get().toString() + " (" + sessionViewUrl + ")");
            } else {
                logger.info("SELENIUM: session created: " + driver.get().toString());
            }
        }
        return driver.get();
    }

    @Override
    public WTFWebDriver getExtendedDriver() throws UnsupportedBrowserException {
        WTFWebDriver driver = extendedDriver.get();
        if (driver == null) {
            driver = new WTFWebDriver(getDriver());
            extendedDriver.set(driver);
        }
        return driver;
    }

    @Override
    public GridApiUtils getGridApiUtils() {
        GridApiUtils utils = gridApiUtils.get();
        if (utils == null) {
            utils = GridApiUtils.create(remoteURL.get());
            gridApiUtils.set(utils);
        }
        return utils;
    }

    @Override
    public String getLiveViewUrl() {
        String liveViewUrl;
        try {
            liveViewUrl = new URL(
                    remoteURL.get().getProtocol(),
                    remoteURL.get().getHost(),
                    selenoidUiPort.get(),
                    "/#/sessions/" + driver.get().getSessionId().toString()
            ).toString();
        } catch (MalformedURLException e) {
            liveViewUrl = "http://localhost/incorrect/live/view/link";
        }
        return liveViewUrl;
    }

    @Override
    public String getVideoLink() {
        String videoUrl;
        try {
            videoUrl = new URL(
                    remoteURL.get().getProtocol(),
                    remoteURL.get().getHost(),
                    remoteURL.get().getPort(),
                    "/video/" + driver.get().getSessionId().toString() + ".mp4"
            ).toString();
        } catch (MalformedURLException e) {
            videoUrl = "http://localhost/incorrect/video/link";
        }
        return videoUrl;
    }

    @Override
    public Boolean isDriverStarted() {
        return driver.get() != null;
    }

    @Override
    public Capabilities getCapabilities() {
        return Optional.ofNullable(driver.get())
                .orElseThrow(() -> new IllegalArgumentException("Driver is not set"))
                .getCapabilities();
    }

    @Override
    public Platform getPlatform() {
        if (driver.get() != null) {
            return getCapabilities().getPlatform();
        } else {
            return Platform.getCurrent();
        }
    }

    /**
     * Release WebDriver session
     */
    @Override
    public void releaseSession() {
        Optional.ofNullable(driver.get())
                .ifPresent(webDriver -> {
                    releaseSession(webDriver);
                    driver.remove();
                    extendedDriver.remove();
                });
    }

    /**
     * Reload application - refresh page
     */
    public void reloadApp() {
        Optional.ofNullable(driver.get())
                .ifPresent(webDriver -> webDriver.navigate().refresh());
    }

    /**
     * Restart WebDriver
     *
     * @return RemoteWebDriver instance
     * @throws UnsupportedBrowserException Unsupported Browser Exception
     */
    public RemoteWebDriver restartDriver() throws UnsupportedBrowserException {
        releaseSession();
        return getDriver();
    }

    private void releaseSession(WebDriver driver) {
        if (driver != null) {
            String sessionId = driver.toString();
            logger.info("SELENIUM: session released: " + sessionId);
            try {
                driver.quit();
            } catch (WebDriverException e) {
                logger.error("SELENIUM: session released: " + e.getMessage());
            }
        }
        BrowserMobUtils.stopProxy();
    }

    /**
     * Create WebDriver instance
     *
     * @return WebDriver instance
     * @throws UnsupportedBrowserException Unsupported Browser Exception
     */
    private RemoteWebDriver createSession() throws UnsupportedBrowserException {
        Optional.ofNullable(java.util.logging.Logger.getLogger("org.openqa.selenium.remote"))
                .ifPresent(logger -> logger.setLevel(java.util.logging.Level.SEVERE));

        RemoteWebDriver driver;
        MutableCapabilities capabilities;
        switch (browser.get()) {
            case firefox:
                if (firefoxProfile.get() == null)
                    firefoxProfile.set(new FirefoxProfile());
                firefoxProfile.get().setAcceptUntrustedCertificates(true);
                firefoxProfile.get().setAssumeUntrustedCertificateIssuer(true);

                if (userAgent.get() != null) {
                    firefoxProfile.get().setPreference(
                            "general.useragent.override",
                            userAgent.get().getUserAgentString()
                    );
                }

                capabilities = new FirefoxOptions();

                ((FirefoxOptions) capabilities).setProfile(firefoxProfile.get());

                if (extraCapabilities.get() != null) {
                    capabilities.merge(extraCapabilities.get());
                }
                break;
            case chrome:
                capabilities = new ChromeOptions();

                ((ChromeOptions) capabilities).addArguments(
                        "--ignore-certificate-errors"
                );

                if (userAgent.get() != null) {
                    ((ChromeOptions) capabilities).addArguments("--user-agent=" + userAgent.get().getUserAgentString());
                }

                if (isMobileEmulation.get() != null) {
                    MobileDevice mobileDevice = isMobileEmulation.get();

                    Map<String, Object> deviceMetrics = new HashMap<>();
                    deviceMetrics.put("width", mobileDevice.getWidth());
                    deviceMetrics.put("height", mobileDevice.getHeight());
                    deviceMetrics.put("pixelRatio", mobileDevice.getPixelRatio());

                    setScreenResolution(mobileDevice.getWidth(), mobileDevice.getHeight());

                    Map<String, Object> mobileEmulation = new HashMap<>();
                    mobileEmulation.put("deviceMetrics", deviceMetrics);
                    mobileEmulation.put("userAgent", isMobileEmulation.get().getUserAgent());

                    ((ChromeOptions) capabilities).setExperimentalOption("mobileEmulation", mobileEmulation);

                    ((ChromeOptions) capabilities).addArguments(
                            "--start-fullscreen"
                    );
                }

                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                ((ChromeOptions) capabilities).setExperimentalOption("prefs", prefs);

                if (extraCapabilities.get() != null) {
                    capabilities.merge(extraCapabilities.get());
                }
                break;
            case opera:
                capabilities = new OperaOptions();
                if (extraCapabilities.get() != null) {
                    capabilities.merge(extraCapabilities.get());
                }
                break;
            case android:
                capabilities = new DesiredCapabilities();
                ((DesiredCapabilities) capabilities).setBrowserName(browser.get().name());
                ((DesiredCapabilities) capabilities).setVersion(androidVersion.get());

                if (appPath.get() != null) {
                    capabilities.setCapability("app", appPath.get());
                } else {
                    capabilities.setCapability("appPackage", appPackage.get());
                    capabilities.setCapability("appActivity", appActivity.get());
                }

                capabilities.setCapability("skin", androidSkin.get().getCode());
                break;
            default:
                throw new UnsupportedBrowserException("Browser is not currently supported: " + browser);
        }

        if (mode.get() == MODE.grid || mode.get() == MODE.remote) {
            capabilities.setCapability("screenResolution", screenResolution.get().toString());

            if (StringUtils.isNotEmpty(version.get()))
                capabilities.setCapability(CapabilityType.VERSION, version.get());
            if (isVNC.get())
                capabilities.setCapability("enableVNC", isVNC.get());
            if (isVideo.get())
                capabilities.setCapability("enableVideo", isVideo.get());
            if (!StringUtils.isEmpty(testCaseDisplayName.get()))
                capabilities.setCapability("name", testCaseDisplayName.get());
        }

        if (isBrowserMobProxy()) {
            try {
                BrowserMobUtils.initProxy();
                BrowserMobUtils.startProxy();
                Proxy proxy = BrowserMobUtils.getSeleniumProxy();
                capabilities.setCapability(CapabilityType.PROXY, proxy);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        switch (mode.get()) {
            case grid:
            case remote:
                URL remoteAddress = remoteURL.get();
                if (browser.get() == BROWSER.android) {
                    driver = new AndroidDriver(remoteAddress, capabilities);
                } else {
                    driver = new RemoteWebDriver(remoteAddress, capabilities);
                }
                // Support for file uploads
                driver.setFileDetector(new LocalFileDetector());
                break;
            default: {
                switch (browser.get()) {
                    case firefox:
                        driver = new FirefoxDriver((FirefoxOptions) capabilities);
                        break;
                    case chrome:
                        driver = new ChromeDriver((ChromeOptions) capabilities);
                        break;
                    case opera:
                        driver = new OperaDriver((OperaOptions) capabilities);
                        break;
                    default:
                        throw new UnsupportedBrowserException("Browser is not currently supported: " + browser);
                }
            }
        }

        //Maximize browser window in GRID (excluding Google Chrome in Mobile Emulation mode)
        if (mode.get() != MODE.local && browser.get() == BROWSER.chrome && isMobileEmulation.get() == null) {
            driver.manage().window().maximize();
        } else if (mode.get() != MODE.local && browser.get() != BROWSER.chrome) {
            ScreenResolution resolution = screenResolution.get();
            Dimension dimension = new Dimension(resolution.getWidth(), resolution.getHeight());
            driver.manage().window().setSize(dimension);
        }

        if (baseUrl.get() != null) {
            driver.get(baseUrl.get().toString());
        }

        return driver;
    }

}
