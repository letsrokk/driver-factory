package org.fxclub.qa.tests.hybrid;

import org.fxclub.qa.tests.commons.hybrid.Pages;
import org.fxclub.qa.tests.commons.hybrid.github.GitHubMainPage;
import org.testng.annotations.Test;

public class HybridAppTest extends HybridTestBase {

    @Test
    public void hybridGitHubSignInTest() {
        Pages.getPage(GitHubMainPage.class)
                .get()
                .navigation()
                .gotoSignIn();
        System.out.println();
    }

}
