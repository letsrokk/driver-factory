package com.github.letsrokk.tests.testobject;

import org.assertj.core.api.Assertions;
import com.github.letsrokk.factories.appium.DeviceInfo;
import com.github.letsrokk.factories.testobject.TestObjectFactory;
import com.github.letsrokk.factories.testobject.api.TestObjectAPI;
import com.github.letsrokk.tests.TestBase;
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
