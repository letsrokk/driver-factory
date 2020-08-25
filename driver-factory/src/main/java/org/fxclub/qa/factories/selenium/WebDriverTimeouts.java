package org.fxclub.qa.factories.selenium;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by majer-dy on 27/09/2016.
 */
public class WebDriverTimeouts {

    public static final String POPUP_OPEN_CLOSE = "popup.open.close";
    public static final String ALERT = "wait.alert";
    public static final String ELEMENT_VISIBILITY = "visibility.check";
    public static final String WAIT_URL_CONTAINS = "wait.url.contains";
    public static final String AJAX = "ajax.timeout";
    public static final String DOM_READY = "dom.ready.state";
    public static final String ELEMENT_CLICKABLE = "element.clickable.timeout";
    public static final String IMPLICITLY_WAIT = "implicitly.wait.timeout";

    public static final String SESSION_CHECKOUT = "session.checkout.timeout";
    public static final String SESSION_CREATOR_INTERVAL = "implicitly.wait.timeout";
    public static final String SESSION_CHECKER_INTERVAL = "session.checker.interval";
    public static final String SESSION_IS_IDLE = "session.idle.timeout";
    public static final String SESSION_SHUTDOWN = "session.shutdown.timeout";

    private Properties timeouts = loadProperties();

    private Properties loadProperties() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("timeouts.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public Long getTimeout(String timeout) {
        return getTimeout(timeout, TimeUnit.MILLISECONDS);
    }

    public Long getTimeout(String timeout, TimeUnit timeUnit) {
        long timeoutLong = Long.parseLong(timeouts.getProperty(timeout));
        return timeUnit.convert(timeoutLong, TimeUnit.MILLISECONDS);
    }

}
