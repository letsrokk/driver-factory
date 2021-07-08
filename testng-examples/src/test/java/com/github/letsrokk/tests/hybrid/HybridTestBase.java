package com.github.letsrokk.tests.hybrid;

import com.github.letsrokk.factories.DriverFactory;
import com.github.letsrokk.factories.appium.AbstractAppiumFactory;
import com.github.letsrokk.factories.selenium.BROWSER;
import com.github.letsrokk.factories.selenium.MODE;
import com.github.letsrokk.factories.selenium.MobileDevice;
import com.github.letsrokk.factories.selenoid.SelenoidFactory;
import com.github.letsrokk.tests.TestBase;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URI;

public class HybridTestBase extends TestBase<DriverFactory> {

    @BeforeMethod
    public void setAutoWebView() {
        if (getDriverFactory() instanceof AbstractAppiumFactory) {
            ((AbstractAppiumFactory) getDriverFactory())
                    .setAutoWebView(true)
                    .setStartIWDP(true)
                    .setW3CMode(false);
        }
    }

    @BeforeMethod
    public void setSelenoidCapabilities() throws MalformedURLException {
        if (getDriverFactory() instanceof SelenoidFactory) {
            ((SelenoidFactory) getDriverFactory())
                    .setBaseUrl(URI.create("https://github.com").toURL())
                    .setMode(MODE.grid)
                    .setBrowser(BROWSER.chrome)
                    .setMobileEmulation(MobileDevice.IPHONE_X)
                    .setVNC(true);
        }
    }

}
