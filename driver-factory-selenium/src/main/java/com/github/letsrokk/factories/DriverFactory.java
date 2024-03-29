package com.github.letsrokk.factories;

import com.github.letsrokk.factories.selenium.WTFWebDriver;
import com.github.letsrokk.factories.selenium.exceptions.UnsupportedBrowserException;
import com.github.letsrokk.grid.GridApiUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;

public interface DriverFactory {

    RemoteWebDriver getDriver() throws UnsupportedBrowserException, IOException, InterruptedException;

    WTFWebDriver getExtendedDriver() throws UnsupportedBrowserException, IOException, InterruptedException;

    GridApiUtils getGridApiUtils();

    Boolean isDriverStarted();

    Capabilities getCapabilities();

    Platform getPlatform();

    void releaseSession();

    RemoteWebDriver restartDriver() throws UnsupportedBrowserException, IOException, InterruptedException;

    void reloadApp();

    String getLiveViewUrl();

    String getVideoLink();
}
