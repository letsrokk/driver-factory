package com.github.letsrokk.tests.web;

import com.github.letsrokk.factories.selenium.BROWSER;
import com.github.letsrokk.factories.selenium.UserAgent;
import com.github.letsrokk.factories.selenium.WTFWebDriver;
import com.github.letsrokk.factories.selenium.exceptions.UnsupportedBrowserException;
import com.github.letsrokk.tests.commons.web.GooogleSearchPage;
import com.github.letsrokk.tests.commons.web.Pages;
import org.assertj.core.api.Assertions;
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
