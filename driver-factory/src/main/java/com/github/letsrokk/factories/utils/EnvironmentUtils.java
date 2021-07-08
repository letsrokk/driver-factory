package com.github.letsrokk.factories.utils;

import java.util.Optional;

public class EnvironmentUtils {

    static public String getProperty(String name, String defaultValue) {
        return Optional
                .ofNullable(System.getProperty(name, System.getenv(name)))
                .orElse(defaultValue);
    }

}
