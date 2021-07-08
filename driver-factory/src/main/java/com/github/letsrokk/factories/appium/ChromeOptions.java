package com.github.letsrokk.factories.appium;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class ChromeOptions {

    final static public String W3C_MODE = "w3c";

    private Map<String, Object> options = new HashMap<>();

    public ChromeOptions setOption(String name, Object value) {
        this.options.put(name, value);
        return this;
    }

}
