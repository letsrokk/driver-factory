package org.fxclub.qa.tests.grid_api;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import lombok.extern.log4j.Log4j2;
import org.fxclub.qa.factories.appium.AppiumFactory;
import org.fxclub.qa.tests.TestBase;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class GridApiTest extends TestBase<AppiumFactory> {

    @BeforeMethod
    public void setFactorySettings() {
        getDriverFactory()
                .setStartIWDP(true)
                .setNoReset(true)
                .setRemoteAddress("http://127.0.0.1:4444/wd/hub");
    }

    @DataProvider(parallel = true)
    private Iterator<Object[]> simpleParallelDataProvider() {
        List<Object[]> cases = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            cases.add(new Object[]{i});
        }

        return cases.iterator();
    }

    private static Map<String, AtomicInteger> deviceCounters = Collections.synchronizedMap(new HashMap<>());

    @Test(dataProvider = "simpleParallelDataProvider")
    public void doNothingTest(Integer counter) throws IOException, InterruptedException {
        AppiumDriver driver = getDriverFactory().getDriver();

        String deviceName =
                driver.getCapabilities().getCapability(MobileCapabilityType.DEVICE_NAME).toString();

        deviceCounters.computeIfAbsent(deviceName, k -> new AtomicInteger(0)).incrementAndGet();
    }

    @AfterClass
    public void printOutCounter() {
        deviceCounters.forEach((key, value) -> log.info(String.format("Device: %s, executions: %d", key, value.get())));
    }

}
