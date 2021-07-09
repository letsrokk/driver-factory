package com.github.letsrokk.factories.testobject;

import java.net.URL;
import java.util.Arrays;

public enum TestObjectCloud {

    EU(TestObjectFactoryCapabilities.TESTOBJECT_APPIUM_ENDPOINT_EU),
    US(TestObjectFactoryCapabilities.TESTOBJECT_APPIUM_ENDPOINT_US);

    private URL endpoint;

    TestObjectCloud(URL endpoint){
        this.endpoint = endpoint;
    }

    public URL getEndPoint() {
        return endpoint;
    }

    public static TestObjectCloud lookup(final String cloudString){
        return Arrays.stream(TestObjectCloud.class.getEnumConstants())
                .filter(contant -> contant.toString().equalsIgnoreCase(cloudString))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such TestObjectCloud exists: " + cloudString));
    }

}
