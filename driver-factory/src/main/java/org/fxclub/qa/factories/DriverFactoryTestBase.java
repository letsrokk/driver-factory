package org.fxclub.qa.factories;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.fxclub.qa.guice.module.DriverFactoryModule;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Guice;

@Guice(modules = {DriverFactoryModule.class})
public class DriverFactoryTestBase<T extends DriverFactory> {

    @Inject
    private Provider<T> driverFactory;

    public T getDriverFactory() {
        return this.driverFactory.get();
    }

    @AfterMethod(alwaysRun = true)
    public void releaseSession() {
        getDriverFactory().releaseSession();
    }

}
