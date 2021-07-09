package com.github.letsrokk.factories.appium;

import com.github.letsrokk.factories.selenium.WTFWebDriver;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.*;

/**
 * Extended WebDriver
 */
public class WTFAppiumDriver extends WTFWebDriver {

    public WTFAppiumDriver(WebDriver originalDriver) {
        super(originalDriver);
    }

    /**
     * Scroll element into view (Appium / Mobile only; iOS)
     *
     * @param element Page Element
     */
    public void mobileScrollIntoView(RemoteWebElement element) {
        String elementID = element.getId();
        HashMap<String, Object> scrollObject = new HashMap<>();
        scrollObject.put("element", elementID);
        scrollObject.put("toVisible", true);
        executeScript("mobile:scroll", scrollObject);
    }

    /**
     * Switch context to WebView (Appium / Mobile only)
     *
     * @throws NoSuchElementException      WebView not available
     * @throws UnsupportedCommandException Not Implemented
     */
    @SuppressWarnings("unchecked")
    public void switchToWebViewContext() throws NoSuchElementException, UnsupportedCommandException {
        if (originalDriver instanceof AppiumDriver) {
            Set<String> contexts = ((AppiumDriver) originalDriver).getContextHandles();
            String webviewContext = contexts
                    .stream()
                    .filter(c -> c.startsWith("WEBVIEW"))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("WEBVIEW is not avilable"));
            ((AppiumDriver) originalDriver).context(webviewContext);
        } else {
            throw new UnsupportedCommandException("Switching context to WEBVIEW available only in Appium");
        }
    }

    /**
     * Switch context to Native App (Appium / Mobile only)
     *
     * @throws UnsupportedCommandException Not Implemented
     */
    public void switchToNativeContext() throws NoSuchElementException, UnsupportedCommandException {
        if (originalDriver instanceof AppiumDriver) {
            ((AppiumDriver) originalDriver).context("NATIVE_APP");
        } else {
            throw new UnsupportedCommandException("Switching context to WEBVIEW available only in Appium");
        }
    }

}
