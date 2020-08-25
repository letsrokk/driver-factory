package org.fxclub.qa.guice.provider;

import com.google.inject.Provider;
import org.fxclub.qa.DriverFactoryEnvs;
import org.fxclub.qa.factories.DriverFactory;
import org.fxclub.qa.factories.appium.AppiumFactory;
import org.fxclub.qa.factories.aws.AWSFactory;
import org.fxclub.qa.factories.selenoid.SelenoidFactory;
import org.fxclub.qa.factories.testobject.TestObjectFactory;
import org.fxclub.qa.factories.utils.EnvironmentUtils;

import java.util.Optional;

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
                case "aws":
                    factory = new AWSFactory();
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
