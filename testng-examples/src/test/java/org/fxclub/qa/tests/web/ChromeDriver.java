package org.fxclub.qa.tests.web;

import org.assertj.core.api.Assertions;
import org.fxclub.qa.factories.selenium.BROWSER;
import org.fxclub.qa.factories.selenium.UserAgent;
import org.fxclub.qa.factories.selenium.WTFWebDriver;
import org.fxclub.qa.factories.selenium.exceptions.UnsupportedBrowserException;
import org.fxclub.qa.tests.commons.web.GooogleSearchPage;
import org.fxclub.qa.tests.commons.web.Pages;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ChromeDriver extends SelenoidTestBase {

    @BeforeMethod
    public void setBrowser() {
        getDriverFactory().setBrowser(BROWSER.chrome);
    }

    @Test
    public void searchGoogle() {
        Pages.getPage(GooogleSearchPage.class).get()
                .search("WebDriver LoadableComponent");
    }

    @Test(expectedExceptions = {NoSuchElementException.class})
    public void noSuchElementExceptionTest() {
        Pages.getPage(GooogleSearchPage.class).get()
                .noSuchElementException();
    }

    @Test(dataProvider = "userAgentsDataProvider")
    public void changeDeviceType(UserAgent userAgent) throws UnsupportedBrowserException {
        getDriverFactory().releaseSession();

        getDriverFactory().setUserAgent(userAgent);
        String userAgentString = (String) new WTFWebDriver(getDriverFactory().getDriver()).executeScript("return navigator.userAgent;");

        Assertions.assertThat(userAgentString)
                .as("User Agent")
                .isEqualTo(userAgent.getUserAgentString());
    }

    @Test
    public void maximizeWindowTest() throws UnsupportedBrowserException {
        Pages.getPage(GooogleSearchPage.class).get()
                .search("Maximize Window On Start Test");

        Dimension dimension = getDriverFactory().getDriver().manage().window().getSize();

        //UHD4k_3840x2160
        Assertions.assertThat(dimension.getWidth())
                .as("Window Width")
                .isEqualTo(3840);
        Assertions.assertThat(dimension.getHeight())
                .as("Window Height")
                .isEqualTo(2160);
    }
}
