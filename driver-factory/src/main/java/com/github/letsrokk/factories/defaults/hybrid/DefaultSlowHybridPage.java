package com.github.letsrokk.factories.defaults.hybrid;

import com.github.letsrokk.factories.DriverFactory;
import com.github.letsrokk.factories.appium.AbstractAppiumFactory;
import com.github.letsrokk.factories.selenium.WTFWebDriver;
import com.github.letsrokk.factories.selenium.WebDriverTimeouts;
import com.google.inject.Inject;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import com.github.letsrokk.factories.selenium.exceptions.UnsupportedBrowserException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.SlowLoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Clock;
import java.util.concurrent.TimeUnit;

public abstract class DefaultSlowHybridPage<K extends DefaultSlowHybridPage<K, V>, V
        extends DriverFactory> extends SlowLoadableComponent<K> {

    protected String anchor;

    protected V factory;
    protected RemoteWebDriver driver;
    protected WTFWebDriver extendedDriver;
    protected WebDriverWait wait;

    protected WebDriverTimeouts timeouts = new WebDriverTimeouts();

    @Inject
    public DefaultSlowHybridPage(V factory, Clock clock, int timeoutInSeconds) throws InterruptedException, UnsupportedBrowserException, IOException {
        super(clock, timeoutInSeconds);
        this.factory = factory;
        this.driver = factory.getDriver();
        this.extendedDriver = factory.getExtendedDriver();
        this.wait = new WebDriverWait(driver, this.timeouts.getTimeout(WebDriverTimeouts.IMPLICITLY_WAIT, TimeUnit.SECONDS));

        if (factory instanceof AbstractAppiumFactory) {
            PageFactory.initElements(new AppiumFieldDecorator(this.driver), this);
        } else {
            PageFactory.initElements(driver, this);
        }
    }

}
