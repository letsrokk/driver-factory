package com.github.letsrokk.factories.defaults.web;

import com.github.letsrokk.factories.DriverFactory;
import com.github.letsrokk.factories.selenium.WTFWebDriver;
import com.github.letsrokk.factories.selenium.WebDriverTimeouts;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.letsrokk.factories.selenium.exceptions.UnsupportedBrowserException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Default Web Page template
 */
public abstract class DefaultWebPage<K extends DefaultWebPage<K, V>, V extends DriverFactory>
        extends LoadableComponent<K> {

    protected final V factory;
    protected final WebDriverTimeouts timeouts = new WebDriverTimeouts();

    protected final Logger logger = LogManager.getLogger(DefaultWebPage.class);

    @Inject
    public DefaultWebPage(V factory) {
        this.factory = factory;
    }

    protected WebDriver getDriver() throws UnsupportedBrowserException, IOException, InterruptedException {
        return this.factory.getDriver();
    }

    protected WTFWebDriver getExtendedDriver() throws UnsupportedBrowserException, IOException, InterruptedException {
        return this.factory.getExtendedDriver();
    }

    protected WebDriverWait getWebDriverWait() throws UnsupportedBrowserException, IOException, InterruptedException {
        long timeoutInSeconds = this.timeouts.getTimeout(WebDriverTimeouts.IMPLICITLY_WAIT, TimeUnit.SECONDS);
        return new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutInSeconds));
    }
}
