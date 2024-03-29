package com.github.letsrokk.testng;

import com.github.letsrokk.factories.DriverFactory;
import com.github.letsrokk.factories.selenium.MODE;
import com.github.letsrokk.factories.selenoid.SelenoidFactory;
import com.github.letsrokk.guice.module.SelenoidFactoryModule;
import com.google.inject.Guice;
import com.google.inject.Provider;
import io.qameta.allure.Attachment;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AllureAttachSelenoidVideos implements ITestListener {

    private Provider<DriverFactory> factory = Guice.createInjector(new SelenoidFactoryModule()).getProvider(DriverFactory.class);

    @Override
    public void onTestStart(ITestResult iTestResult) {

    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        checkFactoryModeAndEmbedVideo();
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        checkFactoryModeAndEmbedVideo();
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    private void checkFactoryModeAndEmbedVideo() {
        if (factory.get() instanceof SelenoidFactory
                && factory.get().isDriverStarted()
                && ((SelenoidFactory) factory.get()).getMode() == MODE.grid
                && ((SelenoidFactory) factory.get()).getVideo()) {
            embedSelenoidVideoLink();
        }
    }

    @Attachment(value = "Selenoid Video Recording", type = "text/html", fileExtension = ".html")
    private String embedSelenoidVideoLink() {
        try {
            return "<html><body><video width='100%' height='100%' controls autoplay>" +
                    "<source src='" + factory.get().getVideoLink() + "' type='video/mp4'>" +
                    "</video></body></html>";
        } catch (Exception e) {
            return "<html><body>Error happened while attaching page source: \n" + e.getMessage() + "</body></html>";
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
