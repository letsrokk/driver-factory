package org.fxclub.qa.tests.appium;

import org.assertj.core.api.Assertions;
import org.fxclub.qa.factories.appium.AppiumFactory;
import org.fxclub.qa.tests.TestBase;
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
