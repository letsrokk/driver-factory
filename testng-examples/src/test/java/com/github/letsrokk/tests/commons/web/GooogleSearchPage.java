package com.github.letsrokk.tests.commons.web;

import com.google.inject.Inject;
import org.assertj.core.api.Assertions;
import com.github.letsrokk.factories.defaults.web.DefaultWebPage;
import com.github.letsrokk.factories.selenium.exceptions.UnsupportedBrowserException;
import com.github.letsrokk.factories.selenoid.SelenoidFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;

public class GooogleSearchPage extends DefaultWebPage<GooogleSearchPage, SelenoidFactory> {

    @Inject
    public GooogleSearchPage(SelenoidFactory factory) throws UnsupportedBrowserException, IOException, InterruptedException {
        super(factory);
    }

    @FindBy(name = "q")
    private WebElement search_input;

    public GooogleSearchPage search(String searchString) {
        search_input.sendKeys(searchString);
        return this;
    }

    public GooogleSearchPage noSuchElementException() {
        getDriver().findElement(By.id("NoSuchElementExceptionId"));
        return this;
    }

    @Override
    public void load() {
        getDriver().get("http://google.me");
        getWebDriverWait().until(ExpectedConditions.visibilityOf(search_input));
    }

    @Override
    public void isLoaded() {
        Assertions.assertThat(getExtendedDriver().isElementDisplayed(search_input))
                .as("Search Input Field is displayed")
                .isTrue();
    }
}
