package com.github.letsrokk.tests.commons.hybrid.github;

import com.google.inject.Inject;
import com.github.letsrokk.factories.DriverFactory;
import com.github.letsrokk.factories.defaults.hybrid.DefaultHybridPage;
import com.github.letsrokk.factories.selenium.exceptions.UnsupportedBrowserException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.io.IOException;

public class GitHubSignInPage extends DefaultHybridPage<GitHubSignInPage, DriverFactory> {

    @FindBy(css = "div.auth-form")
    private WebElement authForm;

    @Inject
    public GitHubSignInPage(DriverFactory factory) throws InterruptedException, UnsupportedBrowserException, IOException {
        super(factory);
    }

    @Override
    protected void load() {
        extendedDriver.waitElementVisible(authForm);
    }

    @Override
    protected void isLoaded() throws Error {
        Assert.assertTrue(extendedDriver.isElementDisplayed(authForm));
    }

}
