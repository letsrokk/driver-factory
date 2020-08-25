package org.fxclub.qa.factories.defaults.hybrid;

import com.google.inject.Inject;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.fxclub.qa.factories.DriverFactory;
import org.fxclub.qa.factories.appium.AbstractAppiumFactory;
import org.fxclub.qa.factories.selenium.WTFWebDriver;
import org.fxclub.qa.factories.selenium.WebDriverTimeouts;
import org.fxclub.qa.factories.selenium.decorators.WebDriverAwareDecorator;
import org.fxclub.qa.factories.selenium.exceptions.UnsupportedBrowserException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public abstract class DefaultHybridPage<K extends DefaultHybridPage<K, V>, V
        extends DriverFactory> extends LoadableComponent<K> {

    protected String anchor;

    protected V factory;
    protected RemoteWebDriver driver;
    protected WTFWebDriver extendedDriver;
    protected WebDriverWait wait;

    protected WebDriverTimeouts timeouts = new WebDriverTimeouts();

    @Inject
    public DefaultHybridPage(V factory) throws InterruptedException, UnsupportedBrowserException, IOException {
        this.factory = factory;
        this.driver = factory.getDriver();
        this.extendedDriver = factory.getExtendedDriver();
        this.wait = new WebDriverWait(driver, this.timeouts.getTimeout(WebDriverTimeouts.IMPLICITLY_WAIT, TimeUnit.SECONDS));

        if (factory instanceof AbstractAppiumFactory) {
            PageFactory.initElements(new AppiumFieldDecorator(this.driver), this);
        } else {
            PageFactory.initElements(new WebDriverAwareDecorator(new HtmlElementLocatorFactory(driver), driver), this);
        }
    }

}
