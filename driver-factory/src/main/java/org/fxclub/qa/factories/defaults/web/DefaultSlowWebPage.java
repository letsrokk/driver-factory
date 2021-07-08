package org.fxclub.qa.factories.defaults.web;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fxclub.qa.factories.DriverFactory;
import org.fxclub.qa.factories.selenium.WTFWebDriver;
import org.fxclub.qa.factories.selenium.WebDriverTimeouts;
import org.fxclub.qa.factories.selenium.exceptions.UnsupportedBrowserException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.SlowLoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Clock;
import java.util.concurrent.TimeUnit;

/**
 * Default Web Page template
 */
public abstract class DefaultSlowWebPage<K extends DefaultSlowWebPage<K, V>, V extends DriverFactory>
        extends SlowLoadableComponent<K> {

    protected final V factory;
    protected final RemoteWebDriver driver;
    protected final WTFWebDriver extendedDriver;
    protected final WebDriverWait wait;

    protected final WebDriverTimeouts timeouts = new WebDriverTimeouts();

    protected final Logger logger = LogManager.getLogger(DefaultSlowWebPage.class);

    @Inject
    public DefaultSlowWebPage(V factory, Clock clock, int timeoutInSeconds) throws UnsupportedBrowserException, IOException, InterruptedException {
        super(clock, timeoutInSeconds);
        this.factory = factory;
        this.driver = factory.getDriver();
        this.extendedDriver = factory.getExtendedDriver();
        this.wait = new WebDriverWait(driver, this.timeouts.getTimeout(WebDriverTimeouts.IMPLICITLY_WAIT, TimeUnit.SECONDS));

        PageFactory.initElements(driver, this);
    }

}
