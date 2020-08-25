package org.fxclub.qa.examples;

import org.fxclub.qa.factories.DriverFactoryTestBase;
import org.fxclub.qa.factories.selenium.exceptions.UnsupportedBrowserException;
import org.fxclub.qa.factories.selenoid.SelenoidFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class SelenoidFactoryMinimalConfigurationTest extends DriverFactoryTestBase<SelenoidFactory> {

    @Test
    public void launchBrowserTest() throws UnsupportedBrowserException {
        WebDriver driver = getDriverFactory().getDriver();
        driver.get("https://google.com");
    }

}
