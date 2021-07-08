package com.github.letsrokk.factories.testobject.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class TestObjectAvailableDevicesList {

    @JsonProperty("EU")
    private List<String> EU;

    @JsonProperty("US")
    private List<String> US;

}
