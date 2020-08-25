package org.fxclub.qa.tests.web;

import org.apache.tika.Tika;
import org.assertj.core.api.Assertions;
import org.fxclub.qa.browsermob.BrowserMobUtils;
import org.fxclub.qa.browsermob.annotations.BrowserMobDownloader;
import org.fxclub.qa.browsermob.rest.FileDownloader;
import org.fxclub.qa.factories.selenium.BROWSER;
import org.fxclub.qa.factories.selenium.WTFWebDriver;
import org.fxclub.qa.factories.selenium.exceptions.UnsupportedBrowserException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@Test(singleThreaded = true)
public class BrowserMobDownloadsTest extends SelenoidTestBase {

    @BeforeMethod
    public void setBrowser() {
        getDriverFactory().setBrowser(BROWSER.chrome);
    }

    @BeforeMethod
    public void checkBrowserMobAnnotation(Method m, ITestContext ctx) {
        Optional<BrowserMobDownloader> annotation = Optional.ofNullable(m.getAnnotation(BrowserMobDownloader.class));
        if (annotation.isPresent()) {
            getDriverFactory().setBrowserMobProxy(true);
            BrowserMobUtils.setResponseFilter(
                    new FileDownloader()
                            .withContents(annotation.get().contentTypes())
                            .withCharset(annotation.get().fileCharset())
                            .requestBody()
            );
        } else {
            getDriverFactory().setBrowserMobProxy(false);
        }
    }

    @BrowserMobDownloader(contentTypes = {FileDownloader.CONTENT_TYPE_ZIP, FileDownloader.CONTENT_TYPE_TAR_GZ})
    @Test
    public void testStartBrowserWithBrowserMobProxyActive() throws UnsupportedBrowserException, IOException {
        WebDriver driver = getDriverFactory().getDriver();

        File downloadedZip = new WTFWebDriver(driver).download("https://github.com/letsrokk/pipelines-shared-library/archive/1.0.zip");

        File downloadedTarGz = new WTFWebDriver(driver).download("https://github.com/letsrokk/pipelines-shared-library/archive/1.0.tar.gz");

        getDriverFactory().releaseSession();

        Assert.assertTrue(downloadedZip.exists(), "File successfully downloaded");
        Assert.assertEquals(new Tika().detect(downloadedZip), "application/zip", "Content Type for downloaded file");

        Assert.assertTrue(downloadedTarGz.exists(), "File successfully downloaded");
        Assert.assertEquals(new Tika().detect(downloadedTarGz), "application/gzip", "Content Type for downloaded file");
    }

    @Test
    public void testStartBrowserWithBrowserMobProxyInactive() {
        Assert.assertFalse(getDriverFactory().isBrowserMobProxy(), "isBrowserMobProxy");
    }

    @Test
    @BrowserMobDownloader(fileCharset = "windows-1251", contentTypes = {FileDownloader.CONTENT_TYPE_TEXT})
    public void downloadNonUtfEncodingTest() throws UnsupportedBrowserException, IOException, InterruptedException {
        WebDriver driver = getDriverFactory().getDriver();
        File downloadedTxt = new WTFWebDriver(driver)
                .download("https://raw.githubusercontent.com/letsrokk/example-files/master/file_with_win1251.txt");

        getDriverFactory().releaseSession();
        Assertions.assertThat(downloadedTxt.exists())
                .as("File successfully downloaded")
                .isTrue();
        Assertions.assertThat(new Tika().detect(downloadedTxt))
                .as("Content Type for downloaded file")
                .isEqualTo("text/plain");

        List<String> lines = Files.readAllLines(downloadedTxt.toPath());

        Assertions.assertThat(lines)
                .containsExactly(
                        "DriverFactoryDownloadExample",
                        "ВерсияФормата=1.01",
                        "Кодировка=Windows-1251"
                );
    }

}
