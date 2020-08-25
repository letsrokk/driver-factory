package org.fxclub.qa.testng;

import io.qameta.allure.Allure;
import org.fxclub.qa.factories.DriverFactory;
import org.fxclub.qa.factories.DriverFactoryTestBase;
import org.fxclub.qa.factories.testobject.TestObjectFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AllureAttachTestObjectLinks implements ITestListener {

    private static ThreadLocal<DriverFactory> factory = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult iTestResult) {
        factory.set(((DriverFactoryTestBase) iTestResult.getMethod().getInstance()).getDriverFactory());
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        attachTestObjectLiveViewLink();
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        attachTestObjectLiveViewLink();
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        attachTestObjectLiveViewLink();
    }

    private void attachTestObjectLiveViewLink() {
        try {
            if (factory.get() instanceof TestObjectFactory && factory.get().isDriverStarted()) {
                Allure.link("TestObjectLiveView", factory.get().getLiveViewUrl());
            }
        } catch (Exception ignore) {
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
