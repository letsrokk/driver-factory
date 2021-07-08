package com.github.letsrokk.factories.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.List;

public class WTFExpectedConditions {

    public static ExpectedCondition<Boolean> visibilityOf(final WebElement element, final boolean expected) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return isDisplayed(element) == expected;
            }

            @Override
            public String toString() {
                return "visibility of " + element;
            }

            private Boolean isDisplayed(WebElement element) {
                try {
                    return element.isDisplayed();
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    return false;
                }
            }
        };
    }

    public static ExpectedCondition<Boolean> presenceOf(final WebElement element) {
        return presenceOf(element, true);
    }

    public static ExpectedCondition<Boolean> absenceOf(final WebElement element) {
        return presenceOf(element, false);
    }

    public static ExpectedCondition<Boolean> presenceOf(final WebElement element, final boolean expected) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return isPresent(element) == expected;
            }

            @Override
            public String toString() {
                return "visibility == " + expected + " of " + element;
            }

            private Boolean isPresent(WebElement element) {
                try {
                    return element.isDisplayed();
                } catch (WebDriverException e) {
                    return false;
                }
            }
        };
    }

    public static ExpectedCondition<List<WebElement>> presenceOf(final List<WebElement> elements) {
        return new ExpectedCondition<List<WebElement>>() {
            public List<WebElement> apply(WebDriver driver) {
                try {
                    if (elements.size() > 0)
                        return elements;
                    else
                        return null;
                } catch (Exception e) {
                    return null;
                }
            }

            public String toString() {
                return "presense of elements";
            }
        };
    }

    public static ExpectedCondition<Boolean> visibilityOf(final By by, final boolean expected) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return isDisplayed(driver, by) == expected;
            }

            @Override
            public String toString() {
                return "visibility == " + expected + " of " + by;
            }

            private Boolean isDisplayed(WebDriver driver, By by) {
                try {
                    WebElement element = driver.findElement(by);
                    return element.isDisplayed();
                } catch (WebDriverException e) {
                    return false;
                }
            }
        };
    }

    public static ExpectedCondition<Boolean> ajaxComplete() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return (boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");
                } catch (UndeclaredThrowableException e) {
                    InvocationTargetException invocationException = (InvocationTargetException) e.getUndeclaredThrowable();
                    WebDriverException wdExeption = (WebDriverException) invocationException.getTargetException();
                    throw wdExeption;
                }
            }
        };
    }

    public static ExpectedCondition<Boolean> domReadyState() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return (boolean) ((JavascriptExecutor) driver).executeScript("return (document.readyState == 'interactive' || document.readyState == 'complete')");
            }
        };
    }

    public static ExpectedCondition<Boolean> elementIsMoving(final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return !isMoving(element);
            }

            public boolean isMoving(WebElement element) {
                Point location = element.getLocation();
                Dimension size = element.getSize();
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                if (element.getLocation().equals(location) && element.getSize().equals(size)) {
                    return false;
                } else {
                    return true;
                }
            }
        };
    }


    public void waitForElementStopMoving(WebElement element) throws Exception {
        long timer = System.currentTimeMillis();
        Point location = null;
        Dimension size = null;
        for (; ; ) {
            location = element.getLocation();
            size = element.getSize();
            if (element.getLocation().equals(location) && element.getSize().equals(size)) {
                break;
            }
            if (System.currentTimeMillis() - timer > 60000)
                throw new Exception("Элемент " + element + " не зафиксирован в течении 1 минуты");
            Thread.sleep(1000);
        }
    }

    public static ExpectedCondition<Boolean> urlContainsAnyOf(final String... fractions) {
        return new ExpectedCondition<Boolean>() {
            private String currentUrl = "";

            public Boolean apply(WebDriver driver) {
                this.currentUrl = driver.getCurrentUrl();

                return Arrays.stream(fractions)
                        .anyMatch(fraction -> this.currentUrl != null && this.currentUrl.contains(fraction));
            }

            public String toString() {
                return String.format("url to contain any of \"%s\". Current url: \"%s\"", Arrays.asList(fractions).toString(), this.currentUrl);
            }
        };
    }
}
