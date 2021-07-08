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

public class GitHubNavWidget extends DefaultHybridPage<GitHubNavWidget, DriverFactory> {

    @FindBy(css = "div.HeaderMenu")
    private WebElement headerMenu;

    @FindBy(css = "button[aria-label=\"Toggle navigation\"]")
    private WebElement toggleNavMenu;

    @FindBy(css = "a[href=\"/login\"]")
    private WebElement singInLink;

    @Inject
    public GitHubNavWidget(DriverFactory factory) throws InterruptedException, UnsupportedBrowserException, IOException {
        super(factory);
    }

    @Override
    protected void load() {
        toggleNavMenu.click();
    }

    @Override
    protected void isLoaded() throws Error {
        Assert.assertTrue(extendedDriver.isElementDisplayed(headerMenu));
    }

    public GitHubSignInPage gotoSignIn() {
        singInLink.click();
        return Pages.getPage(GitHubSignInPage.class).get();
    }
}
