package com.github.letsrokk.factories.testobject;

import com.github.letsrokk.factories.DriverFactory;
import com.github.letsrokk.factories.testobject.api.TestObjectAPI;
import com.github.letsrokk.guice.module.DriverFactoryModule;
import com.google.inject.Guice;
import com.google.inject.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestObjectTestResultsWatcher implements ITestListener {

    private Logger logger = LogManager.getLogger(TestObjectTestResultsWatcher.class);

    private Provider<DriverFactory> factory = Guice.createInjector(new DriverFactoryModule()).getProvider(DriverFactory.class);

    @Override
    public void onTestStart(ITestResult iTestResult) {
        //do nothing
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        if (factory.get() instanceof TestObjectFactory && factory.get().isDriverStarted()) {
            updateExecutionStatus(true);
        }
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        if (factory.get() instanceof TestObjectFactory) {
            updateExecutionStatus(false);
        }
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        if (factory.get() instanceof TestObjectFactory) {
            skipExecution();
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        // do nothing
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        // do nothing
    }

    @Override
    public void onFinish(ITestContext testContext) {
        // do nothing
    }

    private void updateExecutionStatus(boolean passed) {
        try {
            if (factory.get().isDriverStarted()) {
                TestObjectAPI.updateExecutionStatus(factory.get().getDriver().getSessionId().toString(), passed);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void skipExecution() {
        try {
            if (factory.get().isDriverStarted()) {
                TestObjectAPI.skipExecution(factory.get().getDriver().getSessionId().toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}
