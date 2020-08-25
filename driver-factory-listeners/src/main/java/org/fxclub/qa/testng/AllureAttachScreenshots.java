package org.fxclub.qa.testng;

import com.google.inject.Guice;
import com.google.inject.Provider;
import io.qameta.allure.Allure;
import org.fxclub.qa.factories.DriverFactory;
import org.fxclub.qa.factories.selenium.WTFWebDriver;
import org.fxclub.qa.factories.selenium.exceptions.UnsupportedBrowserException;
import org.fxclub.qa.guice.module.DriverFactoryModule;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class AllureAttachScreenshots implements ITestListener {

    private Provider<DriverFactory> factory = Guice.createInjector(new DriverFactoryModule()).getProvider(DriverFactory.class);

    @Override
    public void onTestStart(ITestResult iTestResult) {

    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        checkFactoryModeAndAttachScreenshot();
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    private void checkFactoryModeAndAttachScreenshot() {
        if (factory.get().isDriverStarted()) {
            attachScreenshot();
        }
    }

    private void attachScreenshot() {
        try {
            byte[] screenshot = new WTFWebDriver(factory.get().getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Failure Screenshot", "image/png", new ByteArrayInputStream(screenshot), ".png");
        } catch (UnsupportedBrowserException | InterruptedException | IOException | WebDriverException e) {
            Allure.addAttachment("Failure Screenshot", "text/plain", e.getMessage());
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
