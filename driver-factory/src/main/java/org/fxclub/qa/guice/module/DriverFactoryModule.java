package org.fxclub.qa.guice.module;

import com.google.inject.AbstractModule;
import org.fxclub.qa.factories.DriverFactory;
import org.fxclub.qa.factories.selenoid.SelenoidFactory;
import org.fxclub.qa.guice.provider.DriverFactoryProvider;
import org.fxclub.qa.guice.provider.SelenoidFactoryProvider;

public class DriverFactoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DriverFactory.class).toProvider(DriverFactoryProvider.class);
        bind(SelenoidFactory.class).toProvider(SelenoidFactoryProvider.class);
    }

}
