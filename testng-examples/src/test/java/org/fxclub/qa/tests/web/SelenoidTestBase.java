package org.fxclub.qa.tests.web;

import org.fxclub.qa.factories.selenium.MODE;
import org.fxclub.qa.factories.selenium.UserAgent;
import org.fxclub.qa.factories.selenoid.ScreenResolution;
import org.fxclub.qa.factories.selenoid.SelenoidFactory;
import org.fxclub.qa.tests.TestBase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SelenoidTestBase extends TestBase<SelenoidFactory> {

    @DataProvider(parallel = true)
    public Iterator<Object[]> userAgentsDataProvider() {
        List<Object[]> cases = new ArrayList<>();
        cases.add(new Object[]{UserAgent.MOBILE});
        cases.add(new Object[]{UserAgent.TABLET});
        cases.add(new Object[]{UserAgent.DESKTOP});
        return cases.iterator();
    }

    @BeforeMethod
    public void setSelenoidCapabilities() {
        getDriverFactory()
                .setMode(MODE.grid)
                .setVNC(true)
                .setScreenResolution(ScreenResolution.UHD4k_3840x2160);
    }

}
