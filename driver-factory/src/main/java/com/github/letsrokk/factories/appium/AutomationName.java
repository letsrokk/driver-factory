package com.github.letsrokk.factories.appium;

public class AutomationName {

    // iOS
    final public static String XCUITest = "XCUITest";
    final public static String UIAutomation = "UIAutomation";

    // Android
    final public static String Espresso = "Espresso";
    final public static String UiAutomator2 = "UiAutomator2";
    final public static String UiAutomator = "UiAutomator";

    public static String defaultIOS() {
        return XCUITest;
    }

    public static String defaultANDROID() {
        return UiAutomator2;
    }

}
