package com.github.letsrokk.examples;

import org.assertj.core.api.Assertions;
import com.github.letsrokk.factories.DriverFactoryTestBase;
import com.github.letsrokk.factories.testobject.TestObjectFactory;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestObjectMinimalConfigurationTest extends DriverFactoryTestBase<TestObjectFactory> {

    static {
        System.setProperty("APPLICATION_PATH", "src/test/resources/cordova-github.ipa");
        System.setProperty("TESTOBJECT_API_ACCOUNT", "[REDACTED]");
        System.setProperty("TESTOBJECT_API_KEY", "[REDACTED]");
    }

    @Test
    public void installAppTest() throws IOException {
        SessionId sessionId = getDriverFactory().getDriver().getSessionId();
        Assertions.assertThat(sessionId).isNotNull();
    }

}
