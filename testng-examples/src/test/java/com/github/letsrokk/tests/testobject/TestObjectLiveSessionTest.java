package com.github.letsrokk.tests.testobject;

import org.assertj.core.api.Assertions;
import com.github.letsrokk.factories.selenium.exceptions.UnsupportedBrowserException;
import com.github.letsrokk.factories.testobject.TestObjectFactory;
import com.github.letsrokk.tests.TestBase;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.regex.Pattern;

public class TestObjectLiveSessionTest extends TestBase<TestObjectFactory> {

    @Test
    public void attachTestObjectLiveViewLinksTest() throws IOException {
        getDriverFactory().getDriver();
        String liveViewUrl = getDriverFactory().getLiveViewUrl();
        Assertions.assertThat(liveViewUrl)
                .as("Live View URL")
                .matches(Pattern.compile("https://app.testobject.com/#/[a-zA-Z0-9]+/[a-zA-Z0-9\\-]+/appium/view/[0-9]+\\?.*"));
    }

    @Test
    public void startAppWithAutoWebViewTrueTest() throws InterruptedException, IOException, UnsupportedBrowserException {
        SessionId sessionId = getDriverFactory()
                .setAutoWebView(true)
                .getDriver()
                .getSessionId();
        Assertions.assertThat(sessionId)
                .as("Session Id")
                .isNotNull();
    }

}
