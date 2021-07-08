package com.github.letsrokk.tests.appium;

import org.assertj.core.api.Assertions;
import com.github.letsrokk.factories.appium.AppiumFactory;
import com.github.letsrokk.tests.TestBase;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.Test;

import java.io.IOException;

public class AppiumTest extends TestBase<AppiumFactory> {

    @Test
    public void installAppTest() throws IOException, InterruptedException {
        SessionId sessionId = getDriverFactory().getDriver().getSessionId();
        Assertions.assertThat(sessionId).isNotNull();
    }

}
