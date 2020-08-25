package org.fxclub.qa.factories.testobject.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class TestObjectDevicesList {

    @JsonProperty("EU")
    private List<TestObjectDeviceInfo> EU;

    @JsonProperty("US")
    private List<TestObjectDeviceInfo> US;

}
