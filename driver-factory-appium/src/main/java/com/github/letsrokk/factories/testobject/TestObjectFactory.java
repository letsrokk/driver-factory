package com.github.letsrokk.factories.testobject;

import com.github.letsrokk.factories.appium.AbstractAppiumFactory;
import com.github.letsrokk.factories.appium.AutomationName;
import com.github.letsrokk.factories.appium.WTFAppiumDriver;
import com.github.letsrokk.grid.GridApiUtils;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.util.Optional;

@Singleton
public class TestObjectFactory extends AbstractAppiumFactory {

    // https://wiki.saucelabs.com/display/DOCS/Appium+Capabilities+for+Real+Device+Testing

    private static ThreadLocal<TestObjectCloud> testobject_cloud = InheritableThreadLocal.withInitial(() -> TestObjectCloud.EU);

    private static ThreadLocal<Boolean> testobject_phone_only = InheritableThreadLocal.withInitial(() -> false);
    private static ThreadLocal<Boolean> testobject_tablet_only = InheritableThreadLocal.withInitial(() -> false);
    private static ThreadLocal<Boolean> testobject_private_only = InheritableThreadLocal.withInitial(() -> false);

    private static ThreadLocal<String> testobject_suite_name = new InheritableThreadLocal<>();
    private static ThreadLocal<String> testobject_test_name = new InheritableThreadLocal<>();

    // TestObject-specific capabilities added to session
    private static ThreadLocal<String> testobject_device_session_id = new InheritableThreadLocal<>();
    private static ThreadLocal<Long> testobject_test_report_id = new InheritableThreadLocal<>();
    private static ThreadLocal<String> testobject_test_live_view_url = new InheritableThreadLocal<>();
    private static ThreadLocal<String> testobject_test_report_url = new InheritableThreadLocal<>();

    // Device Cashing

    private static ThreadLocal<String> testobject_device_cache_id = new InheritableThreadLocal<>();

    public TestObjectFactory setCacheId(String cacheId) {
        testobject_device_cache_id.set(cacheId);
        return this;
    }

    @Override
    public AppiumDriver getDriver() throws IOException {
        if (driver.get() == null) {
            DesiredCapabilities capabilities = TestObjectFactoryCapabilities.getCapabilities();

            Optional.ofNullable(FULL_RESET.get())
                    .ifPresent(v -> capabilities.setCapability(MobileCapabilityType.FULL_RESET, v));

            Optional.ofNullable(NO_RESET.get())
                    .ifPresent(v -> capabilities.setCapability(MobileCapabilityType.NO_RESET, v));

            if (testobject_suite_name.get() != null && StringUtils.isNotEmpty(testobject_suite_name.get())) {
                capabilities.setCapability(TestObjectFactoryCapabilities.TESTOBJECT_SUITE_NAME, testobject_suite_name.get());
            }

            if (testobject_test_name.get() != null && StringUtils.isNotEmpty(testobject_test_name.get())) {
                capabilities.setCapability(TestObjectFactoryCapabilities.TESTOBJECT_TEST_NAME, testobject_test_name.get());
            }

            Optional.ofNullable(AUTO_WEBVIEW.get())
                    .ifPresent(autoWebView -> capabilities.setCapability(MobileCapabilityType.AUTO_WEBVIEW, autoWebView));

            Optional.ofNullable(CROSSWALK_APPLICATION.get())
                    .ifPresent(crosswalkApp -> capabilities.setCapability("crosswalkApplication", crosswalkApp));

            capabilities.setCapability("phoneOnly", testobject_phone_only.get());
            capabilities.setCapability("tabletOnly", testobject_tablet_only.get());
            capabilities.setCapability("privateDevicesOnly", testobject_private_only.get());

            Optional.ofNullable(testobject_device_cache_id.get())
                    .ifPresent(v -> capabilities.setCapability(TestObjectFactoryCapabilities.TESTOBJECT_CACHE_ID, v));

            logger.info("APPIUM: testobject session started");
            logger.debug("APPIUM:\n" + capabilities.toString());
            if (capabilities.getPlatform().equals(Platform.ANDROID)) {
                String automationName = Optional.ofNullable(AUTOMATION_NAME.get())
                        .orElse(AutomationName.defaultANDROID());
                capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, automationName);

                ImmutableMap<String, Object> chromeOptions = getChromeOptions();
                if (!chromeOptions.isEmpty()) {
                    capabilities.setCapability(AndroidMobileCapabilityType.CHROME_OPTIONS, chromeOptions);
                }

                driver.set(new AndroidDriver(testobject_cloud.get().getEndPoint(), capabilities));
            } else if (capabilities.getPlatform().equals(Platform.IOS)) {
                String automationName = Optional.ofNullable(AUTOMATION_NAME.get())
                        .orElse(AutomationName.defaultIOS());
                capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, automationName);

                driver.set(new IOSDriver(testobject_cloud.get().getEndPoint(), capabilities));
            } else {
                throw new IllegalArgumentException("Unsupported platform: " + capabilities.getPlatform());
            }

            String sessionId = driver.get().getSessionId().toString();

            Capabilities caps = driver.get().getCapabilities();
            testobject_test_report_id.set(Long.parseLong(caps.getCapability("testobject_test_report_id").toString()));
            testobject_device_session_id.set(caps.getCapability("testobject_device_session_id").toString());
            testobject_test_report_url.set(caps.getCapability("testobject_test_report_url").toString());
            testobject_test_live_view_url.set(caps.getCapability("testobject_test_live_view_url").toString());

            logger.info(String.format("APPIUM: testobject session created: %s (%s)", sessionId, getLiveViewUrl()));
        }
        return driver.get();
    }

    @Override
    public WTFAppiumDriver getExtendedDriver() throws IOException {
        WTFAppiumDriver driver = extendedDriver.get();
        if (driver == null) {
            driver = new WTFAppiumDriver(getDriver());
            extendedDriver.set(driver);
        }
        return driver;
    }

    @Override
    public GridApiUtils getGridApiUtils() {
        throw new UnsupportedOperationException("Grid Api is not available for TestObject Factory");
    }

    @Override
    public String getLiveViewUrl() {
        return testobject_test_live_view_url.get();
    }

    @Override
    public String getVideoLink() {
        throw new UnsupportedOperationException("Direct link to video recording is not available for TestObject");
    }

    public Long getTestObjectTestReportId() {
        return testobject_test_report_id.get();
    }

    @Override
    public Platform getPlatform() {
        if (driver.get() != null) {
            String testobjectPlatformName = Optional
                    .ofNullable(getCapabilities().getCapability("testobject_platform_name"))
                    .orElseGet(() -> getCapabilities().getPlatform())
                    .toString();
            return Platform.fromString(testobjectPlatformName);
        } else {
            return TestObjectFactoryCapabilities.getTestobjectPlatformName();
        }
    }

    /**
     * Set Test Suite name for Execution
     *
     * @param suiteName suite name
     * @return TestObjectFactory instance
     */
    public TestObjectFactory setSuiteName(String suiteName) {
        testobject_suite_name.set(suiteName);
        return this;
    }

    /**
     * Set Test Suite name for Execution
     *
     * @param testName test name
     * @return TestObjectFactory instance
     */
    public TestObjectFactory setTestName(String testName) {
        testobject_test_name.set(testName);
        return this;
    }

    /**
     * Set 'phoneOnly' capability
     *
     * @param phoneOnly true / false
     * @return TestObjectFactory instance
     */
    public TestObjectFactory setPhoneOnly(Boolean phoneOnly) {
        testobject_phone_only.set(phoneOnly);
        testobject_tablet_only.set(!phoneOnly);
        return this;
    }

    /**
     * Set 'tabletsOnly' capability
     *
     * @param tabletsOnly true / false
     * @return TestObjectFactory instance
     */
    public TestObjectFactory setTabletsOnly(Boolean tabletsOnly) {
        testobject_phone_only.set(!tabletsOnly);
        testobject_tablet_only.set(tabletsOnly);
        return this;
    }

    /**
     * Set 'privateDevicesOnly' capability
     *
     * @param privateDevicesOnly true / false
     * @return TestObjectFactory instance
     */
    public TestObjectFactory setPrivateDevicesOnly(Boolean privateDevicesOnly) {
        testobject_private_only.set(!privateDevicesOnly);
        return this;
    }

    /**
     * Set CLOUD endpoint
     *
     * @param cloud cloud location
     * @return TestObjectFactory instance
     */
    public TestObjectFactory setCloud(TestObjectCloud cloud) {
        testobject_cloud.set(cloud);
        return this;
    }

    /**
     * Set CLOUD endpoint
     *
     * @param cloud cloud location
     * @return TestObjectFactory instance
     */
    public TestObjectFactory setCloud(String cloud) {
        testobject_cloud.set(TestObjectCloud.lookup(cloud));
        return this;
    }


}
