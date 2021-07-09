package com.github.letsrokk.tests.commons.web;

import com.google.inject.Injector;
import com.github.letsrokk.factories.defaults.web.DefaultWebPage;
import com.github.letsrokk.guice.module.SelenoidFactoryModule;

public class Pages {

    private static final Injector pageInjector = com.google.inject.Guice.createInjector(new SelenoidFactoryModule());

    public static <T extends DefaultWebPage> T getPage(Class<T> page) {
        return pageInjector.getInstance(page);
    }

}
