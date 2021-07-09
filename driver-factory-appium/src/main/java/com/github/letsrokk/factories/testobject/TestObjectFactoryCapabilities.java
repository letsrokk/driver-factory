package com.github.letsrokk.factories.testobject;

import com.github.letsrokk.DriverFactoryEnvs;
import com.github.letsrokk.factories.appium.DeviceInfo;
import com.github.letsrokk.factories.testobject.api.TestObjectAPI;
import com.github.letsrokk.factories.utils.EnvironmentUtils;
import io.appium.java_client.remote.MobileCapabilityType;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestObjectFactoryCapabilities {

    public static final String TESTOBJECT_API_KEY = "testobject_api_key";

    public static final String TESTOBJECT_APP_ID = "testobject_app_id";

    public static final String TESTOBJECT_APPIUM_VERSION = "appiumVersion";

    public static final String TESTOBJECT_SUITE_NAME = "testobject_suite_name";
    public static final String TESTOBJECT_TEST_NAME = "testobject_test_name";

    public static final String TESTOBJECT_CACHE_ID = "cacheId";

    public static final URL TESTOBJECT_API_ENDPOINT = getURL("https://app.testobject.com/api");

    public static final URL TESTOBJECT_APPIUM_ENDPOINT_US = getURL("https://us1-manual.app.testobject.com/wd/hub");
    public static final URL TESTOBJECT_APPIUM_ENDPOINT_EU = getURL("https://appium.testobject.com/wd/hub");

    //
    //
    //

    public static final String TESTOBJECT_APPIUM_VERSION_LATEST = "1.17.1";

    //
    //
    //

    public static DesiredCapabilities getCapabilities() throws IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 600);

        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, getTestobjectPlatformName());
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, getTestobjectPlatformVersion());

        capabilities.setCapability(TESTOBJECT_APPIUM_VERSION, getAppiumVersion());

        if (getApplication().exists()) {
            capabilities.setCapability(TESTOBJECT_APP_ID, getTestObjectApplicationId().toString());
        } else {
            if (capabilities.getPlatform().is(Platform.IOS)) {
                capabilities.setBrowserName("Safari");
            } else {
                capabilities.setBrowserName("Chrome");
            }
        }

        capabilities.setCapability(TESTOBJECT_API_KEY, getTestobjectApiKey(capabilities.getPlatform()));

        return capabilities;
    }

    private static String getAppiumVersion() {
        String appiumVersion = StringUtils.trimToNull(
                System.getProperty("APPIUM_VERSION", System.getenv("APPIUM_VERSION"))
        );
        return Optional.ofNullable(appiumVersion).orElse(TESTOBJECT_APPIUM_VERSION_LATEST);
    }

    private static Integer getTestObjectApplicationId() throws IOException {
        return TestObjectAPI.uploadApp(getApplication());
    }

    public static String getTestobjectApiAccount() {
        return System.getProperty("TESTOBJECT_API_ACCOUNT", System.getenv("TESTOBJECT_API_ACCOUNT"));
    }

    public static String getTestobjectApiKey() {
        return System.getProperty("TESTOBJECT_API_KEY", System.getenv("TESTOBJECT_API_KEY"));
    }

    private static String getTestobjectApiKey(Platform platform) {
        String testobject_api_key = platform.is(Platform.ANDROID)
                ? System.getProperty("TESTOBJECT_API_KEY_ANDROID", System.getenv("TESTOBJECT_API_KEY_ANDROID"))
                : System.getProperty("TESTOBJECT_API_KEY_IOS", System.getenv("TESTOBJECT_API_KEY_IOS"));

        if (StringUtils.isEmpty(testobject_api_key)) {
            testobject_api_key = getTestobjectApiKey();
        }

        return Objects.requireNonNull(testobject_api_key);
    }

    public static Platform getTestobjectPlatformName() {
        String platformNameString = System.getProperty(DriverFactoryEnvs.PLATFORM_NAME, System.getenv(DriverFactoryEnvs.PLATFORM_NAME));
        if (StringUtils.isEmpty(platformNameString)) {
            platformNameString = getPlatformFromAppFile().toString();
        }
        Platform platformName = Platform.fromString(platformNameString);
        return Objects.requireNonNull(platformName);
    }

    private static String getTestobjectPlatformVersion() {
        String platformVersion = System.getProperty(DriverFactoryEnvs.PLATFORM_VERSION, System.getenv(DriverFactoryEnvs.PLATFORM_VERSION));
        return Optional.ofNullable(platformVersion).orElse("");
    }

    private static List<DeviceInfo> getIOSDevices() throws IOException {
        return TestObjectAPI.getAvailableDevices().stream()
                .filter(DeviceInfo::isIOS)
                .collect(Collectors.toList());
    }

    private static List<DeviceInfo> getAndroidDevices() throws IOException {
        return TestObjectAPI.getAvailableDevices().stream()
                .filter(DeviceInfo::isANDROID)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public static File getApplication() {
        String APPLICATION_PATH =
                EnvironmentUtils.getProperty(DriverFactoryEnvs.APPLICATION_PATH, "dummy.app");

        File applicationFile = new File(APPLICATION_PATH);

        if (!applicationFile.exists() || !applicationFile.toURI().getScheme().equals("file")) {
            throw new IllegalArgumentException(String.format("APPLICATION_PATH does not exist or is not local file: %s", applicationFile.getPath()));
        }

        return applicationFile;
    }

    private static Platform getPlatformFromAppFile() {
        if (!getApplication().exists()) {
            throw new IllegalArgumentException("APPLICATION_PATH is incorrect");
        }
        return getApplication().getName().endsWith("apk")
                ? Platform.ANDROID
                : Platform.IOS;
    }

    private static Optional<String> getBundleid() {
        return Optional.ofNullable(System.getProperty("bundleId", System.getenv("bundleId")));
    }

    private static URL getURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
