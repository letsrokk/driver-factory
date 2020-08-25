package org.fxclub.qa.tests.commons.web;

import com.google.inject.Injector;
import org.fxclub.qa.factories.defaults.web.DefaultWebPage;
import org.fxclub.qa.guice.module.DriverFactoryModule;

public class Pages {

    private static final Injector pageInjector = com.google.inject.Guice.createInjector(new DriverFactoryModule());

    public static <T extends DefaultWebPage> T getPage(Class<T> page) {
        return pageInjector.getInstance(page);
    }

}
