package org.fxclub.qa.factories.selenium;

public enum UserAgent {

    MOBILE("Mozilla/5.0 (Linux; Android 6.0; HTC One M9 Build/MRA58K) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/52.0.2743.98 Mobile Safari/537.36"),
    TABLET("Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) " +
            "Version/4.0.4 Mobile/7B334b Safari/531.21.10"),
    DESKTOP("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_2; en-us) AppleWebKit/531.21.8 (KHTML, like Gecko) " +
            "Version/4.0.4 Safari/531.21.10");

    private String userAgent;

    UserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserAgentString() {
        return userAgent;
    }

}
