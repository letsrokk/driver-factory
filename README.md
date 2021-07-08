# Driver Factory

# Configuration

## Environment and System Properties

### Common Properties

- `DRIVER_FACTORY` - factory type
Supported: `appium`, `testobject`, `aws`, `selenoid` (equal to `selenium`)  
Default: `selenoid`
- `DRIVER_MODE` - WebDriver mode, defines whether execution should happen locally or on remote machine  
Supported: `local`, `remote`, `grid`  
Default: `local`
- `REMOTE_ADDRESS` - URL for Selenium Grid Hub or Remote Driver (for driver modes `remote` and `grid`)  
Default: `http://127.0.0.1:4444/wd/hub`

### Selenoid Factory Properties (`selenoid` and `selenium`)

- `BROWSER_NAME` - browser name  
Supported: `chrome`, `firefox`, `opera`  
Default: `chrome`

### Appium Factory Properties (`appium`)

- `APPLICATION_PATH` - application path (local file path or remote URL; `*.ipa` and `*.apk` supported)
- `PLATFORM_VERSION` - OS version (optional)  

`PLATFORM_NAME` is detected by application file provided

### TestObject Factory Properties

- `APPLICATION_PATH` - path to application file (local file path; `*.ipa` and `*.apk`)
- `PLATFORM_NAME` - platform name (optional; if not set, detected by application file provided)  
Supported: `android`, `ios`
- `PLATFORM_VERSION` - OS version (optional)
- `APPIUM_VERSION` - specific Appium version to use (optional)
- `TESTOBJECT_API_ACCOUNT` - TestObject API account name (for API calls; e.g. execution status updates, app uploads)
- `TESTOBJECT_API_KEY` - TestObject API key to use
- `TESTOBJECT_API_KEY_IOS` and `TESTOBJECT_API_KEY_ANDROID` - platform specific TestObject API keys (optional)

### BrowserMob Proxy Support

For Selenium-based factories implemented support for standalone BrowserMob Proxy instances:
- `BMP_HOST` - BrowserMob Proxy host
- `BMP_PORT` - BrowserMob Proxy port

Implemented BMP filters:
- `FileDownloader` - for assistance with File downloading process 
(usage example: [BrowserMobDownloadsTest.java](testng-examples/src/test/java/com/github/letsrokk/tests/web/BrowserMobDownloadsTest.java))

### Logging

Logger: [Apache Log4j 2](https://logging.apache.org/log4j/2.x/)  
Configuration example: [log4j2.xml](testng-examples/src/test/resources/log4j2.xml)

# Usage

## Selenoid Factory

### Minimal Configuration Example

```java
package com.github.letsrokk.examples;

import DriverFactoryTestBase;
import UnsupportedBrowserException;
import SelenoidFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class SelenoidFactoryMinimalConfigurationTest extends DriverFactoryTestBase<SelenoidFactory> {

    @Test
    public void launchBrowserTest() throws UnsupportedBrowserException {
        WebDriver driver = getDriverFactory().getDriver();
        driver.get("https://google.com");
    }

}
``` 

## Appium Factory

### Minimal Configuration Example

```java
package com.github.letsrokk.examples;

import org.assertj.core.api.Assertions;
import DriverFactoryTestBase;
import AppiumFactory;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class AppiumFactoryMinimalConfigurationTest extends DriverFactoryTestBase<AppiumFactory> {

    static {
        System.setProperty("APPLICATION_PATH", "src/test/resources/cordova-github.ipa");
    }

    @Test
    public void installAppTest() throws IOException, InterruptedException {
        SessionId sessionId = getDriverFactory().getDriver().getSessionId();
        Assertions.assertThat(sessionId).isNotNull();
    }

}
```

### Appium with Selenium Grid

Selenium Grid Hub:
```bash
% java -jar selenium-server-standalone.jar -role hub
```
Appium Node:
```bash
% appium --nodeconfig node-iphone-13.json --default-capabilities iphone-x-caps.json
```

Appium Node configuration example for iPhone:
```json5
{
    "capabilities": [
        {
            "browserName": "iPhone",
            "browserVersion": "13",
	    "platformName": "IOS",            
	    "maxInstances": 1
        }
    ],
    "configuration": {
        "cleanUpCycle": 2000,
        "hubHost": "127.0.0.1",
        "hubPort": "4444",
        "hubProtocol": "http",
        "maxSession": 1,
        "proxy": "org.openqa.grid.selenium.proxy.DefaultRemoteProxy",
        "register": true,
        "registerCycle": 5000,
        "timeout": 30000
    }
}
```
Appium Node `--default-capabilities` example for iPhone
```json5
{
    "deviceName": "iPhone X",
    "platformName": "IOS",
    "platformVersion": "13.2.2",
    "udid": "09384kjgnwoeij093jgiogweiong8492i923ng83",
    "wdaLocalPort": 8101
}
```

## Test Object Factory

### Minimal Configuration Example

```java
package com.github.letsrokk.examples;

import org.assertj.core.api.Assertions;
import DriverFactoryTestBase;
import TestObjectFactory;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestObjectMinimalConfigurationTest extends DriverFactoryTestBase<TestObjectFactory> {

    static {
        System.setProperty("APPLICATION_PATH", "src/test/resources/cordova-github.ipa");
        System.setProperty("TESTOBJECT_API_ACCOUNT", "[REDACTED]");
        System.setProperty("TESTOBJECT_API_KEY", "[REDACTED]");
    }

    @Test
    public void installAppTest() throws IOException {
        SessionId sessionId = getDriverFactory().getDriver().getSessionId();
        Assertions.assertThat(sessionId).isNotNull();
    }

}
```

# Guice - Dependency Injections

## Factories

TBD

## PageObjects

TBD