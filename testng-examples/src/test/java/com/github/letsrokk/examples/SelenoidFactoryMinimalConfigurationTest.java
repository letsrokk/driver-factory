package com.github.letsrokk.examples;

import com.github.letsrokk.factories.DriverFactoryTestBase;
import com.github.letsrokk.factories.selenium.exceptions.UnsupportedBrowserException;
import com.github.letsrokk.factories.selenoid.SelenoidFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class SelenoidFactoryMinimalConfigurationTest extends DriverFactoryTestBase<SelenoidFactory> {

    @Test
    public void launchBrowserTest() throws UnsupportedBrowserException {
        WebDriver driver = getDriverFactory().getDriver();
        driver.get("https://google.com");
    }

}
