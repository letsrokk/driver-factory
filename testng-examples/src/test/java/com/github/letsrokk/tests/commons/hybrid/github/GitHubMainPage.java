package com.github.letsrokk.tests.commons.hybrid.github;

import com.google.inject.Inject;
import com.github.letsrokk.factories.DriverFactory;
import com.github.letsrokk.factories.defaults.hybrid.DefaultHybridPage;
import com.github.letsrokk.factories.selenium.exceptions.UnsupportedBrowserException;
import com.github.letsrokk.tests.commons.hybrid.Pages;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.io.IOException;

public class GitHubMainPage extends DefaultHybridPage<GitHubMainPage, DriverFactory> {

    @FindBy(css = "a[aria-label=Homepage]")
    private WebElement mainPageLink;

    @FindBy(css = "div.application-main")
    private WebElement divApplicationMain;

    @FindBy(css = "a[href=\"/forgot_password\"]")
    private WebElement gotoForgotPassword;

    @Inject
    public GitHubMainPage(DriverFactory factory) throws InterruptedException, UnsupportedBrowserException, IOException {
        super(factory);
    }

    @Override
    protected void load() {
        mainPageLink.click();
        extendedDriver.waitElementVisible(divApplicationMain);
    }

    @Override
    protected void isLoaded() throws Error {
        Assert.assertTrue(extendedDriver.isElementDisplayed(divApplicationMain));
    }

    public GitHubNavWidget navigation() {
        return Pages.getPage(GitHubNavWidget.class).get();
    }
}
