package com.github.letsrokk.guice.module;

import com.github.letsrokk.factories.DriverFactory;
import com.github.letsrokk.factories.selenoid.SelenoidFactory;
import com.github.letsrokk.guice.provider.DriverFactoryProvider;
import com.google.inject.AbstractModule;

public class DriverFactoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DriverFactory.class).toProvider(DriverFactoryProvider.class);
    }

}
