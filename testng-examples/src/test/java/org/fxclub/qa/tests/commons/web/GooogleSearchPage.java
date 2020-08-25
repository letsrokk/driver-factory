package org.fxclub.qa.tests.commons.web;

import com.google.inject.Inject;
import org.assertj.core.api.Assertions;
import org.fxclub.qa.factories.defaults.web.DefaultWebPage;
import org.fxclub.qa.factories.selenium.exceptions.UnsupportedBrowserException;
import org.fxclub.qa.factories.selenoid.SelenoidFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

import java.io.IOException;

public class GooogleSearchPage extends DefaultWebPage<GooogleSearchPage, SelenoidFactory> {

    @Inject
    public GooogleSearchPage(SelenoidFactory factory) throws UnsupportedBrowserException, IOException, InterruptedException {
        super(factory);
    }

    @FindBy(name = "q")
    private HtmlElement search_input;

    public GooogleSearchPage search(String searchString) {
        search_input.sendKeys(searchString);
        return this;
    }

    public GooogleSearchPage noSuchElementException() {
        driver.findElement(By.id("NoSuchElementExceptionId"));
        return this;
    }

    @Override
    public void load() {
        driver.get("http://google.me");
        wait.until(ExpectedConditions.visibilityOf(search_input));
    }

    @Override
    public void isLoaded() {
        Assertions.assertThat(search_input.exists()).as("Search Input Field is displayed").isTrue();
    }
}
