package org.fxclub.qa.tests.hybrid;

import org.fxclub.qa.factories.DriverFactory;
import org.fxclub.qa.factories.appium.AbstractAppiumFactory;
import org.fxclub.qa.factories.selenium.BROWSER;
import org.fxclub.qa.factories.selenium.MODE;
import org.fxclub.qa.factories.selenium.MobileDevice;
import org.fxclub.qa.factories.selenoid.SelenoidFactory;
import org.fxclub.qa.tests.TestBase;
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
