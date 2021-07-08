package org.fxclub.qa.factories.selenium;

import com.google.common.io.Files;
import com.google.common.primitives.Ints;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.fxclub.qa.factories.ExtendedDriver;
import org.fxclub.qa.factories.selenoid.SelenoidFactory;
import org.fxclub.qa.grid.GridApiUtils;
import org.fxclub.qa.grid.model.WDSessionInfo;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Extended WebDriver
 */
public class WTFWebDriver implements ExtendedDriver {

    private static Logger logger = LogManager.getLogger(SelenoidFactory.class);

    private WebDriver originalDriver;
    private WebDriverTimeouts timeouts;
    private GridApiUtils gridApiUtils;

    public WTFWebDriver(WebDriver originalDriver) {
        this.originalDriver = originalDriver;
        this.timeouts = new WebDriverTimeouts();
        this.gridApiUtils = null;
    }

    /**
     * Wait for AJAX requests to be complete
     */
    public void waitForAjax() {
        WebDriverWait wait = new WebDriverWait(originalDriver, timeouts.getTimeout(WebDriverTimeouts.AJAX, TimeUnit.SECONDS));
        wait.until(WTFExpectedConditions.ajaxComplete());
    }

    /**
     * Wait for DOM to be loaded (readyState == interactive or complete)
     */
    public void waitDOMReady() {
        WebDriverWait wait = new WebDriverWait(originalDriver, timeouts.getTimeout(WebDriverTimeouts.DOM_READY, TimeUnit.SECONDS));
        wait.until(WTFExpectedConditions.domReadyState());
    }

    /**
     * Execute JS on page
     *
     * @param script JS script
     * @param args   arguments (can be used in script as arguments[n])
     * @return JS execution result
     */
    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) originalDriver).executeScript(script, args);
    }

    /**
     * Trigger Blur effect on page
     */
    public void triggerBlur() {
        executeScript("document.activeElement.blur();");
    }

    /**
     * Check if Element is present and visible
     *
     * @param locator By locator
     * @return true / false
     */
    public boolean isElementDisplayed(By locator) {
        try {
            return originalDriver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException ignore) {
            return false;
        }
    }

    /**
     * Check if Element is present and visible
     *
     * @param element Page Element
     * @return true / false
     */
    public boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException ignore) {
            return false;
        }
    }

    /**
     * Check if element is present on page
     *
     * @param locator By locator
     * @return true / false
     */
    public boolean isElementPresent(By locator) {
        try {
            return originalDriver.findElements(locator).size() > 0;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Trigger JQuery event for element: focus
     *
     * @param <T>     Element extended from WebElement
     * @param element Page Element
     * @return Page Element
     */
    public <T extends WebElement> T focus(T element) {
        return triggerJQueryEvent(element, "focus");
    }

    /**
     * Trigger JQuery event for element: on key up
     *
     * @param <T>     Element extended from WebElement
     * @param element Page Element
     * @return Page Element
     */
    public <T extends WebElement> T keyUp(T element) {
        return triggerJQueryEvent(element, "keyup");
    }

    /**
     * Trigger JQuery event for element: blur
     *
     * @param <T>     Element extended from WebElement
     * @param element Page Element
     * @return Page Element
     */
    public <T extends WebElement> T blur(T element) {
        return triggerJQueryEvent(element, "blur");
    }

    /**
     * Trigger JQuery event for element: on click
     *
     * @param <T>     Element extended from WebElement
     * @param element Page Element
     * @return Page Element
     */
    public <T extends WebElement> T onClick(T element) {
        return triggerJQueryEvent(element, "click");
    }

    /**
     * Trigger JQuery event for element: on change
     *
     * @param <T>     Element extended from WebElement
     * @param element Page Element
     * @return Page Element
     */
    public <T extends WebElement> T onChange(T element) {
        return triggerJQueryEvent(element, "change");
    }

    /**
     * Trigger JQuery event for element: on mouse enter
     *
     * @param <T>     Element extended from WebElement
     * @param element Page Element
     * @return Page Element
     */
    public <T extends WebElement> T mouseEnter(T element) {
        return triggerJQueryEvent(element, "mouseenter");
    }

    private <T extends WebElement> T triggerJQueryEvent(T element, String event) {
        executeScript("jQuery(arguments[0]).trigger(\"" + event + "\")", element);
        return element;
    }

    /**
     * Set VALUE for element using JQuery
     *
     * @param element Page Element
     * @param value   Value
     */
    public void jQuerySendKeys(WebElement element, String value) {
        executeScript("jQuery(arguments[0]).val(\"" + value + "\")", element);
    }

    /**
     * Click on element using JS
     *
     * @param <T>        Element extended from WebElement
     * @param webElement element
     * @return Page Element
     */
    public <T extends WebElement> T clickWithJS(T webElement) {
        executeScript("arguments[0].click();", webElement);
        return webElement;
    }

    /**
     * Set VALUE for element using JS
     *
     * @param <T>        Element extended from WebElement
     * @param element    Page Element
     * @param keysToSend value
     * @return element
     */
    public <T extends WebElement> T sendKeysWithJS(T element, String keysToSend) {
        executeScript("arguments[0].value = '" + keysToSend + "' ", element);
        return element;
    }

    /**
     * Clear VALUE for element using JS
     *
     * @param <T>     Element extended from WebElement
     * @param element Page Element
     * @return Page Element
     */
    public <T extends WebElement> T clearWithJS(T element) {
        return sendKeysWithJS(element, "");
    }

    /**
     * Get outterHtml for Element
     *
     * @param webElement element
     * @return Outter HTML
     */
    public Document getOutterHtml(WebElement webElement) {
        String outerHTML = (String) executeScript("return arguments[0].outerHTML;", webElement);
        return Jsoup.parse("<table>" + outerHTML + "</table>");
    }

    /**
     * Wait for element to become visible
     *
     * @param locator By locator
     * @return found element
     */
    public WebElement waitElementVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(originalDriver, timeouts.getTimeout(WebDriverTimeouts.ELEMENT_VISIBILITY, TimeUnit.SECONDS));
        wait.until(WTFExpectedConditions.visibilityOf(locator, true));
        return originalDriver.findElement(locator);
    }

    /**
     * Wait for element to become visible
     *
     * @param <T>     Element extended from WebElement
     * @param element Page Element
     * @return Page Element
     */
    public <T extends WebElement> T waitElementVisible(T element) {
        WebDriverWait wait = new WebDriverWait(originalDriver, timeouts.getTimeout(WebDriverTimeouts.ELEMENT_VISIBILITY, TimeUnit.SECONDS));
        wait.until(WTFExpectedConditions.visibilityOf(element, true));
        return element;
    }

    /**
     * Wait for element to become invisible
     *
     * @param element Page Element
     */
    public void waitElementVanish(WebElement element) {
        WebDriverWait wait = new WebDriverWait(originalDriver, timeouts.getTimeout(WebDriverTimeouts.ELEMENT_VISIBILITY, TimeUnit.SECONDS));
        wait.until(WTFExpectedConditions.visibilityOf(element, false));
    }

    /**
     * Wait for element to become invisible
     *
     * @param locator by locator
     */
    public void waitElementVanish(By locator) {
        WebDriverWait wait = new WebDriverWait(originalDriver, timeouts.getTimeout(WebDriverTimeouts.ELEMENT_VISIBILITY, TimeUnit.SECONDS));
        wait.until(WTFExpectedConditions.visibilityOf(locator, false));
    }

    /**
     * Get and Accept Alert message
     *
     * @return Alert message text
     */
    public String getAlert() {
        return getAlert(true);
    }

    /**
     * Get Alert message
     *
     * @param isAccept accept (true) or dismiss alert (false)
     * @return Alert message
     */
    public String getAlert(boolean isAccept) {
        Alert alert = originalDriver.switchTo().alert();
        String text = alert.getText();
        if (isAccept)
            alert.accept();
        else
            alert.dismiss();
        return text;
    }

    /**
     * Wait Alert and accept it
     *
     * @return Alert message
     */
    public String waitForAlert() {
        return waitForAlert(true);
    }

    /**
     * Wait Alert and perform action
     *
     * @param isAccept accept / dismiss alert
     * @return Alert message
     */
    public String waitForAlert(boolean isAccept) {
        WebDriverWait wait = new WebDriverWait(originalDriver, timeouts.getTimeout(WebDriverTimeouts.ALERT, TimeUnit.SECONDS));
        wait.until(ExpectedConditions.alertIsPresent());
        return getAlert(isAccept);
    }

    /**
     * Switch Window by Index
     *
     * @param index index strating form 0
     */
    public void switchToWindow(int index) {
        List<String> windows = new ArrayList<>(originalDriver.getWindowHandles());
        originalDriver.switchTo().window(windows.get(index));
    }

    /**
     * Scroll Element into view using JS (WEB only)
     *
     * @param <T>     Element extended from WebElement
     * @param element Page Element
     * @return element
     */
    public <T extends WebElement> T scrollIntoView(T element) {
        executeScript("arguments[0].scrollIntoView(true);", element);
        return element;
    }

    /**
     * Type in keys using Actions
     *
     * @param keys keys to type
     */
    public void sendKeys(CharSequence... keys) {
        new Actions(originalDriver).sendKeys(keys).build().perform();
    }

    /**
     * Click on Element and wait for Pop Up to appear
     *
     * @param element Page Element
     */
    public void clickAndWaitPopUp(WebElement element) {
        int count = originalDriver.getWindowHandles().size();
        element.click();

        WebDriverWait wait = new WebDriverWait(originalDriver, timeouts.getTimeout(WebDriverTimeouts.POPUP_OPEN_CLOSE, TimeUnit.SECONDS));
        wait.until(ExpectedConditions.numberOfWindowsToBe(count + 1));
        switchToWindow(count);
    }

    /**
     * Take Screenshot
     *
     * @param <X>  OutputType for Screenshot
     * @param TYPE Output Type for Screenshot
     * @return Screenshot in format of Output Type
     */
    public <X> X getScreenshotAs(OutputType<X> TYPE) {
        return ((TakesScreenshot) originalDriver).getScreenshotAs(TYPE);
    }

    /**
     * Wait for Element to become clickable
     *
     * @param <T>     Element extended from WebElement
     * @param element Page Element
     * @return Page Element
     */
    public <T extends WebElement> T waitElementClickable(T element) {
        WebDriverWait wait = new WebDriverWait(originalDriver, timeouts.getTimeout(WebDriverTimeouts.ELEMENT_CLICKABLE, TimeUnit.SECONDS));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        return element;
    }

    /**
     * Select value for SELECT type element by value
     *
     * @param select Select element
     * @param values Options to select
     * @return Select element
     */
    public Select selectByValue(Select select, String... values) {
        Arrays.asList(values).stream().forEach(select::selectByValue);
        return select;
    }

    /**
     * Check if Text is present on the page
     *
     * @param text text
     * @return true / false
     */
    public boolean isTextPresent(String text) {
        String pageText = originalDriver.findElement(By.tagName("body")).getText();
        return StringUtils.containsIgnoreCase(pageText, text);
    }

    /**
     * Wait for Current URL to contain value
     *
     * @param url value
     * @return current URL
     */
    public String waitURLContains(String url) {
        WebDriverWait wait = new WebDriverWait(originalDriver, timeouts.getTimeout(WebDriverTimeouts.WAIT_URL_CONTAINS, TimeUnit.SECONDS));
        wait.until(ExpectedConditions.urlContains(url));
        return originalDriver.getCurrentUrl();
    }

    /**
     * Add attribute to element using JS
     *
     * @param <T>       Element extended from WebElement
     * @param element   Page Element
     * @param attribute attribute name
     * @param value     attribute value
     * @return Page Element
     */
    public <T extends WebElement> T addAttribute(T element, String attribute, Object value) {
        executeScript(
                "arguments[0].setAttribute(arguments[1], arguments[2])",
                element, attribute, value
        );
        return element;
    }

    /**
     * Remove attribute for element
     *
     * @param <T>       Element extended from WebElement
     * @param element   Page Element
     * @param attribute attribute name
     * @return element
     */
    public <T extends WebElement> T removeAttribute(T element, String attribute) {
        executeScript(
                "arguments[0].removeAttribute(arguments[1])",
                element, attribute
        );
        return element;
    }

    /**
     * Click on ELEMENT and wait for another element on page to be refreshed
     *
     * @param clickElement         click element
     * @param elementToBeRefreshed refresh element
     */
    public void clickAndWaitForRefresh(WebElement clickElement, WebElement elementToBeRefreshed) {
        addAttribute(elementToBeRefreshed, "wait_refresh", true);

        WebDriverWait wait = new WebDriverWait(originalDriver, timeouts.getTimeout(WebDriverTimeouts.AJAX, TimeUnit.SECONDS));
        clickElement.click();
        wait.until(ExpectedConditions.not(
                ExpectedConditions.attributeToBeNotEmpty(elementToBeRefreshed, "wait_refresh")
        ));
    }

    /**
     * Click on element and wait for pop up to be closed
     *
     * @param element Page Element
     * @return alert message (if appeared)
     */
    public String clickAndWaitPopToClose(WebElement element) {
        return clickAndWaitPopToClose(element, true);
    }

    /**
     * Click on element and wait for pop up to be closed
     *
     * @param element     Page Element
     * @param acceptAlert accept (true) or dismiss (false) alert if appeared
     * @return alert message (if appeared)
     */
    public String clickAndWaitPopToClose(WebElement element, boolean acceptAlert) {
        int windowsCount = originalDriver.getWindowHandles().size();
        element.click();

        String alertMessage;
        try {
            waitForAjax();
            alertMessage = getAlert(acceptAlert);
        } catch (UnhandledAlertException unhandledAlertException) {
            alertMessage = getAlert(acceptAlert);
        } catch (NoAlertPresentException e) {
            alertMessage = "";
        }

        WebDriverWait wait = new WebDriverWait(originalDriver, timeouts.getTimeout(WebDriverTimeouts.POPUP_OPEN_CLOSE, TimeUnit.SECONDS));
        wait.until(ExpectedConditions.numberOfWindowsToBe(windowsCount - 1));

        List<String> handles = new ArrayList<>(originalDriver.getWindowHandles());
        Collections.reverse(handles);
        originalDriver.switchTo().window(handles.get(0));
        return alertMessage;
    }

    /**
     * Type in to RichEdit element
     *
     * @param INSTANCE_ID RichEdit instance ID
     * @param TEXT        text
     */
    public void sendKeysRichTextEdit(String INSTANCE_ID, String TEXT) {
        executeScript("CKEDITOR.instances[\"" + INSTANCE_ID + "\"].setData('" + TEXT + "')");
    }

    /**
     * Get value from RichEdit element
     *
     * @param INSTANCE_ID RichEdit instance ID
     * @return text
     */
    public String getValueRichTextEdit(String INSTANCE_ID) {
        String TEXT = (String) executeScript("return CKEDITOR.instances[\"" + INSTANCE_ID + "\"].getData()");
        if (TEXT == null)
            TEXT = "";
        return TEXT;
    }

    /**
     * get OutOfView offset for Element
     *
     * @param element elemnt
     * @return offset in pixels ([x; y])
     */
    public int[] getOutOfViewOffset(WebElement element) {
        int windowSizeX = Ints.checkedCast((long) executeScript("return document.body.clientWidth"));
        int windowSizeY = Ints.checkedCast((long) executeScript("return document.body.clientHeight"));

        Point elementLocation = element.getLocation();
        Dimension elementSize = element.getSize();

        int coordinatesX = elementLocation.getX() + (elementSize.getWidth() / 2) + 1;
        int coordinatesY = elementLocation.getY() + (elementSize.getHeight() / 2) + 1;

        int offsetX = windowSizeX < coordinatesX ? windowSizeX - coordinatesX : 0;
        int offsetY = windowSizeY < coordinatesY ? windowSizeY - coordinatesY : 0;

        return new int[]{offsetX, offsetY};
    }

    /**
     * Get Selenium Grid node IP
     *
     * @return IP
     * @throws IOException exception if node IP is unreachable
     */
    public String getNodeIP() throws IOException {
        WDSessionInfo sessionInfo = getSessionInfo();
        return sessionInfo.getProxyId();
    }

    /**
     * Get RemoteWebDriver Session Info
     *
     * @return session info
     */
    public SessionId getSessionId() {
        return ((RemoteWebDriver) originalDriver).getSessionId();
    }

    private WDSessionInfo getSessionInfo() throws IOException {
        return gridApiUtils.getSessionInfo(getSessionId().toString());
    }

    /**
     * Check is alert is displayed
     *
     * @return true / false
     */
    public boolean isAlertPresent() {
        try {
            originalDriver.getTitle();
            return false;
        } catch (UnhandledAlertException alertException) {
            return true;
        }
    }

    /**
     * Drag and Drop element using Actions
     *
     * @param <T>     Element extended from WebElement
     * @param element Page Element
     * @param x       X coordinat
     * @param y       Y coordinat
     * @return element
     */
    public <T extends WebElement> T dragAndDrop(T element, int x, int y) {
        new Actions(originalDriver).dragAndDropBy(element, x, y).build().perform();
        return element;
    }

    /**
     * Get Screenshot of an element
     *
     * @param element Page Element
     * @return scrrenshot File
     * @throws IOException exception if file write failed
     */
    public File getScreenshotOfElement(WebElement element) throws IOException {
        File screenshot = getScreenshotAs(OutputType.FILE);
        BufferedImage fullImg = ImageIO.read(screenshot);
        Point point = element.getLocation();
        Dimension size = element.getSize();
        int eleWidth = size.getWidth();
        int eleHeight = size.getHeight();
        BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
        ImageIO.write(eleScreenshot, "png", screenshot);
        return screenshot;
    }

    /**
     * Download contents of base64 encoded string. Test should be market with @BrowserMobDownloader annotation
     *
     * @return dowloaded file
     */
    public File download() {
        return processDownloadedFile();
    }

    /**
     * Download file by clicking a button. Test should be market with @BrowserMobDownloader annotation
     *
     * @param button button to click for download to start
     * @return downloaded file
     */
    public File download(WebElement button) {
        if (StringUtils.equals(button.getAttribute("target"), "_blank")) {
            removeAttribute(button, "target");
        }
        button.click();
        return processDownloadedFile();
    }

    /**
     * Download file by navigating to URL. Test should be market with @BrowserMobDownloader annotation
     *
     * @param url URL to driver.get(?) to
     * @return downloaded file
     */
    public File download(String url) {
        originalDriver.get(url);
        return processDownloadedFile();
    }

    @NotNull
    private File processDownloadedFile() {
        String base64EncodedString = waitElementVisible(By.id("downloadedContent")).getText();

        File downloadedFile = new File(System.getProperty("user.dir") +
                File.separator + "target" +
                File.separator + "downloads" +
                File.separator + UUID.randomUUID());
        try {
            if (!downloadedFile.getParentFile().exists())
                downloadedFile.getParentFile().mkdirs();
            Files.write(Base64.getDecoder().decode(base64EncodedString), downloadedFile);
        } catch (IOException | IllegalArgumentException e) {
            Charset charset = Charset.forName("Windows-1251");
            String charsetString = charset.toString();
            charsetString.getBytes(charset);

            String base64s;
            try {
                base64s = Base64.getEncoder().encodeToString(charsetString.getBytes("Windows-1251"));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            throw new WebDriverException("Could not process downloaded file: " + e.getMessage());
        }

        try {
            Tika memeUtils = new Tika();
            String mimeType = memeUtils.detect(downloadedFile);
            String extension = MimeTypes.getDefaultMimeTypes().forName(mimeType).getExtension();

            File renamedFile = new File(downloadedFile.getParentFile().getAbsolutePath() +
                    File.separator + downloadedFile.getName() + extension);
            Files.move(downloadedFile, renamedFile);
            return renamedFile;
        } catch (IOException | MimeTypeException e) {
            logger.warn("Could not rename downloaded file", e);
            return downloadedFile;
        }
    }

    /**
     * Change LOCATION.HASH
     *
     * @param anchor anchor (hash) value
     */
    public void anchor(String anchor) {
        String hash = "#" + StringUtils.trimToEmpty(anchor);
        executeScript("location.hash='" + hash + "'");
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
