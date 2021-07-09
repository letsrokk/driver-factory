package com.github.letsrokk.factories.defaults.hybrid;

import com.github.letsrokk.factories.DriverFactory;
import com.github.letsrokk.factories.appium.AbstractAppiumFactory;
import com.github.letsrokk.factories.appium.WTFAppiumDriver;
import com.github.letsrokk.factories.selenium.WebDriverTimeouts;
import com.github.letsrokk.factories.selenium.exceptions.UnsupportedBrowserException;
import com.google.inject.Inject;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public abstract class DefaultHybridPage<K extends DefaultHybridPage<K, V>, V
        extends DriverFactory> extends LoadableComponent<K> {

    protected String anchor;

    protected V factory;
    protected RemoteWebDriver driver;
    protected WTFAppiumDriver extendedDriver;
    protected WebDriverWait wait;

    protected WebDriverTimeouts timeouts = new WebDriverTimeouts();

    @Inject
    public DefaultHybridPage(V factory) throws InterruptedException, UnsupportedBrowserException, IOException {
        this.factory = factory;
        this.driver = factory.getDriver();
        this.extendedDriver = (WTFAppiumDriver) factory.getExtendedDriver();
        this.wait = new WebDriverWait(driver, this.timeouts.getTimeout(WebDriverTimeouts.IMPLICITLY_WAIT, TimeUnit.SECONDS));

        if (factory instanceof AbstractAppiumFactory) {
            PageFactory.initElements(new AppiumFieldDecorator(this.driver), this);
        } else {
            PageFactory.initElements(driver, this);
        }
    }

}
