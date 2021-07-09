package com.github.letsrokk.guice.module;

import com.google.inject.AbstractModule;
import com.github.letsrokk.factories.DriverFactory;
import com.github.letsrokk.factories.selenoid.SelenoidFactory;
import com.github.letsrokk.guice.provider.SelenoidFactoryProvider;

public class SelenoidFactoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DriverFactory.class).toProvider(SelenoidFactoryProvider.class);
        bind(SelenoidFactory.class).toProvider(SelenoidFactoryProvider.class);
    }

}
