package com.github.letsrokk.tests.commons.hybrid;

import com.google.inject.Injector;
import com.github.letsrokk.factories.defaults.hybrid.DefaultHybridPage;
import com.github.letsrokk.guice.module.SelenoidFactoryModule;

public class Pages {

    private static final Injector pageInjector = com.google.inject.Guice.createInjector(new SelenoidFactoryModule());

    public static <T extends DefaultHybridPage> T getPage(Class<T> page) {
        return pageInjector.getInstance(page);
    }

}
