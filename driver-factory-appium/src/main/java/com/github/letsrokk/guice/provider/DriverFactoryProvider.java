package com.github.letsrokk.guice.provider;

import com.github.letsrokk.DriverFactoryEnvs;
import com.github.letsrokk.factories.DriverFactory;
import com.github.letsrokk.factories.appium.AppiumFactory;
import com.github.letsrokk.factories.selenoid.SelenoidFactory;
import com.github.letsrokk.factories.testobject.TestObjectFactory;
import com.github.letsrokk.factories.utils.EnvironmentUtils;
import com.google.inject.Provider;

public class DriverFactoryProvider implements Provider<DriverFactory> {

    private static DriverFactory factory;

    @Override
    public DriverFactory get() {
        initFactory();
        return factory;
    }

    private synchronized void initFactory() {
        if (factory == null) {
            switch (getDriverFactoryEnvVar()) {
                case "appium":
                    factory = new AppiumFactory();
                    break;
                case "testobject":
                    factory = new TestObjectFactory();
                    break;
                case "selenium":
                case "selenoid":
                default:
                    factory = new SelenoidFactory();
                    break;
            }
        }
    }

    private String getDriverFactoryEnvVar() {
        return EnvironmentUtils.getProperty(DriverFactoryEnvs.DRIVER_FACTORY, "SELENOID").toLowerCase();
    }
}
