package org.fxclub.qa.testng;

import com.google.inject.Guice;
import com.google.inject.Provider;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.fxclub.qa.factories.DriverFactory;
import org.fxclub.qa.factories.selenium.exceptions.UnsupportedBrowserException;
import org.fxclub.qa.guice.module.DriverFactoryModule;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;

public class AllureAttachPageSource implements ITestListener {

    private Provider<DriverFactory> factory = Guice.createInjector(new DriverFactoryModule()).getProvider(DriverFactory.class);

    @Override
    public void onTestStart(ITestResult iTestResult) {

    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        checkFactoryModeAndAttachPageSource();
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    private void checkFactoryModeAndAttachPageSource() {
        if (factory.get().isDriverStarted()) {
            attachPageSource();
        }
    }

    @Attachment(value = "Failed Page Source", type = "text/plain")
    private void attachPageSource() {
        try {
            Allure.addAttachment("Failed Page Source", "text/plain", factory.get().getDriver().getPageSource());
        } catch (WebDriverException | IOException | InterruptedException | UnsupportedBrowserException e) {
            Allure.addAttachment("Failed Page Source", "text/plain", e.getMessage());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }

}
