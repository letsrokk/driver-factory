package com.github.letsrokk.tests.web;

import com.github.letsrokk.factories.selenium.MODE;
import com.github.letsrokk.factories.selenium.UserAgent;
import com.github.letsrokk.factories.selenoid.ScreenResolution;
import com.github.letsrokk.factories.selenoid.SelenoidFactory;
import com.github.letsrokk.tests.TestBase;
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
