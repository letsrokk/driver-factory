package com.github.letsrokk.examples;

import org.assertj.core.api.Assertions;
import com.github.letsrokk.factories.DriverFactoryTestBase;
import com.github.letsrokk.factories.appium.AppiumFactory;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.Test;

import java.io.IOException;

public class AppiumFactoryMinimalConfigurationTest extends DriverFactoryTestBase<AppiumFactory> {

    static {
        System.setProperty("APPLICATION_PATH", "src/test/resources/cordova-github.ipa");
    }

    @Test
    public void installAppTest() throws IOException, InterruptedException {
        SessionId sessionId = getDriverFactory().getDriver().getSessionId();
        Assertions.assertThat(sessionId).isNotNull();
    }

}
