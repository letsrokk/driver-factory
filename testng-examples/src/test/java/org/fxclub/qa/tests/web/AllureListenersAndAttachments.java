package org.fxclub.qa.tests.web;

import com.google.common.io.Files;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Link;
import org.fxclub.qa.factories.selenium.BROWSER;
import org.fxclub.qa.factories.selenium.MobileDevice;
import org.fxclub.qa.factories.selenium.exceptions.UnsupportedBrowserException;
import org.fxclub.qa.factories.selenoid.ScreenResolution;
import org.fxclub.qa.tests.commons.web.GooogleSearchPage;
import org.fxclub.qa.tests.commons.web.Pages;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class AllureListenersAndAttachments extends SelenoidTestBase {

    @BeforeMethod
    public void setBrowser() {
        getDriverFactory().setBrowser(BROWSER.chrome);
    }

    @DataProvider(parallel = true)
    public Iterator<Object[]> screenResolutionsProvider() {
        List<Object[]> resolutions = new ArrayList<>();

        //4x3
        resolutions.add(new Object[]{ScreenResolution.SVGA_800x600});
        resolutions.add(new Object[]{ScreenResolution.SXGA_1280x1024});

        //16:9 and 16:10
        resolutions.add(new Object[]{ScreenResolution.WXGA_PLUS_1440x900});
        resolutions.add(new Object[]{ScreenResolution.WUXGA_1920x1200});

        //4k UHD
        resolutions.add(new Object[]{ScreenResolution.UHD4k_3840x2160});

        return resolutions.iterator();
    }

    @Test(dataProvider = "screenResolutionsProvider")
    public void storeSelenoidVideoForDifferentResolutionsTest(ScreenResolution resolution) throws InterruptedException {
        getDriverFactory()
                .setScreenResolution(resolution)
                .setVideo(true);

        Pages.getPage(GooogleSearchPage.class).get()
                .search("Selenoid enableVideo: " + resolution.toString());
        Thread.sleep(1000);

        String videoLink = getDriverFactory().getVideoLink();

        getDriverFactory().releaseSession();
        Thread.sleep(5000);

        Allure.link("Selenoid Video", videoLink);

        await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
            HttpURLConnection connection = (HttpURLConnection) new URL(videoLink).openConnection();
            connection.setRequestMethod("HEAD");
            assertThat(connection.getResponseCode())
                    .as("Video File Request Response")
                    .isEqualTo(HttpURLConnection.HTTP_OK);
        });
    }

    @Test
    public void storeSelenoidVideoForMobileEmulationModeTest() throws InterruptedException {
        getDriverFactory()
                .setVideo(true)
                .setMobileEmulation(MobileDevice.IPHONE_X);

        Pages.getPage(GooogleSearchPage.class).get()
                .search("Selenoid enableVideo in MobileEmulation mode");
        Thread.sleep(1000);

        String videoLink = getDriverFactory().getVideoLink();

        getDriverFactory().releaseSession();
        Thread.sleep(5000);

        Allure.link("Selenoid Video", videoLink);

        await().atMost(15, TimeUnit.SECONDS).untilAsserted(() -> {
            HttpURLConnection connection = (HttpURLConnection) new URL(videoLink).openConnection();
            connection.setRequestMethod("HEAD");
            assertThat(connection.getResponseCode())
                    .as("Video File Request Response")
                    .isEqualTo(HttpURLConnection.HTTP_OK);
        });
    }

    @Test
    public void takeScreenshotTest() throws UnsupportedBrowserException {
        Pages.getPage(GooogleSearchPage.class).get()
                .search("RemoteWebdriver TakeScreenshot");

        TakesScreenshot ts = getDriverFactory().getDriver();
        File screenshot = ts.getScreenshotAs(OutputType.FILE);

        Assert.assertTrue(screenshot.exists());

        Allure.addByteAttachmentAsync("Screenshot", "image/png", () -> {
            try {
                return Files.toByteArray(screenshot);
            } catch (IOException e) {
                return new byte[0];
            }
        });
    }

}
