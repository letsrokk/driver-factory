package org.fxclub.qa.factories.selenium.elements;

import org.fxclub.qa.factories.selenium.WTFWebDriver;
import org.fxclub.qa.factories.selenium.WebDriverTimeouts;
import org.fxclub.qa.factories.selenium.decorators.WebDriverAware;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

import java.util.concurrent.TimeUnit;

public class WTFHtmlElement extends HtmlElement implements WebDriverAware {

    protected WebDriver driver;
    protected WTFWebDriver extendedDriver;

    protected WebDriverTimeouts timeouts = new WebDriverTimeouts();
    protected WebDriverWait wait;

    @Override
    public void setWebDriver(WebDriver driver) {
        this.driver = driver;
        this.extendedDriver = new WTFWebDriver(driver);
        this.wait = new WebDriverWait(driver, this.timeouts.getTimeout(WebDriverTimeouts.IMPLICITLY_WAIT, TimeUnit.SECONDS));
    }
}
