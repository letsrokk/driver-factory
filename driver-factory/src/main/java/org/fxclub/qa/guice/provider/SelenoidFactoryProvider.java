package org.fxclub.qa.guice.provider;

import com.google.inject.Provider;
import org.fxclub.qa.factories.selenoid.SelenoidFactory;

public class SelenoidFactoryProvider implements Provider<SelenoidFactory> {

    private static SelenoidFactory factory;

    @Override
    public SelenoidFactory get() {
        initFactory();
        return factory;
    }

    private synchronized void initFactory() {
        if (factory == null) {
            factory = new SelenoidFactory();
        }
    }

}
