package org.fxclub.qa.factories.utils;

import java.util.Optional;

public class EnvironmentUtils {

    static public String getProperty(String name, String defaultValue) {
        return Optional
                .ofNullable(System.getProperty(name, System.getenv(name)))
                .orElse(defaultValue);
    }

}
