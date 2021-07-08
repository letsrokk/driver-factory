package com.github.letsrokk.factories.testobject.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString(of = {"name", "osVersion"})
public class TestObjectDeviceInfo {

    private String id;
    private String name;
    private String modelNumber;

    private List<String> manufacturer;
    private Boolean isArm;
    private Boolean hasOnScreenButtons;
    private String abiType;
    private String os;
    private String osVersion;
    private Integer apiLevel;
    private BigDecimal screenSize;
    private Integer resolutionWidth;
    private Integer resolutionHeight;
    private Integer dpi;
    private Integer pixelsPerPoint;
    private Boolean isTablet;
    private String defaultOrientation;
    private Integer ramSize;
    private Integer internalStorageSize;
    private Integer sdCardSize;
    private Integer cpuCores;
    private Integer cpuFrequency;
    private Boolean supportsMockLocations;
    private Boolean supportsAppiumWebAppTesting;
    private Boolean supportsXcuiTest;
    private Boolean isKeyGuardDisabled;
    private Boolean isRooted;
    private Boolean isPrivate;
    private Boolean isAlternativeIoEnabled;
    private Boolean supportsGlobalProxy;
    private Boolean supportsMinicapSocketConnection;
    private String cpuType;
    private String deviceFamily;
    private Boolean supportsManualWebTesting;
    private String dpiName;

}
