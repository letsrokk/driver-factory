package com.github.letsrokk.factories.defaults.web;

import com.github.letsrokk.factories.DriverFactory;
import com.github.letsrokk.factories.selenium.WTFWebDriver;
import com.github.letsrokk.factories.selenium.WebDriverTimeouts;
import com.google.inject.Inject;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.SlowLoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Default Web Page template
 */
public abstract class DefaultSlowWebPage<K extends DefaultSlowWebPage<K, V>, V extends DriverFactory>
        extends SlowLoadableComponent<K> {

    protected final V factory;

    protected final WebDriverTimeouts timeouts = new WebDriverTimeouts();

    protected final Logger logger = LogManager.getLogger(DefaultSlowWebPage.class);

    @Inject
    public DefaultSlowWebPage(V factory, Clock clock, int timeoutInSeconds) {
        super(clock, timeoutInSeconds);
        this.factory = factory;
    }

    @SneakyThrows
    protected WebDriver getDriver() {
        return this.factory.getDriver();
    }

    @SneakyThrows
    protected WTFWebDriver getExtendedDriver() {
        return this.factory.getExtendedDriver();
    }

    @SneakyThrows
    protected WebDriverWait getWebDriverWait() {
        long timeoutInSeconds = this.timeouts.getTimeout(WebDriverTimeouts.IMPLICITLY_WAIT, TimeUnit.SECONDS);
        return new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutInSeconds));
    }

}
