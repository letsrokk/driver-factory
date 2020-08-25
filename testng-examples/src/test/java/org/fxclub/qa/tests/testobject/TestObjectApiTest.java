package org.fxclub.qa.tests.testobject;

import org.assertj.core.api.Assertions;
import org.fxclub.qa.factories.appium.DeviceInfo;
import org.fxclub.qa.factories.testobject.TestObjectFactory;
import org.fxclub.qa.factories.testobject.api.TestObjectAPI;
import org.fxclub.qa.tests.TestBase;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class TestObjectApiTest extends TestBase<TestObjectFactory> {

    @Test
    public void getDevicesTest() throws IOException {
        List<DeviceInfo> availableDevices = TestObjectAPI.getAvailableDevices();
        Assertions.assertThat(availableDevices)
                .as("List of Available Devices")
                .isNotEmpty();
    }

}
