package com.github.letsrokk.tests;

import com.github.letsrokk.factories.DriverFactory;
import com.github.letsrokk.factories.DriverFactoryTestBase;
import com.github.letsrokk.factories.selenoid.SelenoidFactory;
import com.github.letsrokk.factories.testobject.TestObjectFactory;
import com.github.letsrokk.factories.testobject.TestObjectTestResultsWatcher;
import com.github.letsrokk.testng.AllureAttachScreenshots;
import com.github.letsrokk.testng.AllureAttachTestObjectLinks;
import com.github.letsrokk.testng.TestNGConsoleProgressListener;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Arrays;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;

@Listeners({
        TestObjectTestResultsWatcher.class,
        AllureAttachTestObjectLinks.class,
        AllureAttachScreenshots.class,
        TestNGConsoleProgressListener.class
})
public class TestBase<T extends DriverFactory> extends DriverFactoryTestBase<T> {

    @BeforeMethod
    public void setSelenoidTestCaseName(Method method, Object[] params) {
        if (getDriverFactory() instanceof SelenoidFactory) {
            ((SelenoidFactory) getDriverFactory())
                    .setTestCaseDisplayName(method.getName() + getParametersString(params));
        }
    }

    @BeforeMethod
    public void setSetTestObjectCapabilities(ITestContext testContext, Method method, Object[] params) {
        if (getDriverFactory() instanceof TestObjectFactory) {
            ((TestObjectFactory) getDriverFactory())
                    .setSuiteName(testContext.getSuite().getName())
                    .setTestName(method.getName() + getParametersString(params));

            ((TestObjectFactory) getDriverFactory())
                    .setPhoneOnly(true)
                    .setCloud(System.getProperty("TESTOBJECT_CLOUD", "eu"));
        }
    }

    private String getParametersString(Object[] params) {
        if (Arrays.isNullOrEmpty(params)) {
            return "";
        } else {
            return ": " + StringUtils.join(params, ";");
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        getDriverFactory().releaseSession();
    }
}
