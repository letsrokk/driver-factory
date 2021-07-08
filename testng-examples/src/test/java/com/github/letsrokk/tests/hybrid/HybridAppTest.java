package com.github.letsrokk.tests.hybrid;

import com.github.letsrokk.tests.commons.hybrid.Pages;
import com.github.letsrokk.tests.commons.hybrid.github.GitHubMainPage;
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
