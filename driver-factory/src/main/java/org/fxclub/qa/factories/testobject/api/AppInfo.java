package org.fxclub.qa.factories.testobject.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString(of = {"id", "name", "type", "versionCode", "deviceFamily", "platform"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppInfo {

    private Integer id;
    private String externalIdentifier;
    private String name;
    private String type;
    private String usage;
    private Long creationTime;
    private String url;
    private String nativeCode;
    private String version;
    private String versionCode;
    private String packageName;
    private String defaultActivity;
    private String nameFromFile;
    private Integer minSdkLevel;
    private Integer targetSdkLevel;
    private String minOsVersion;
    private String targetOsVersion;
    private List<String> abiTypes;
    private String deviceFamily;
    private String hockeyAppId;
    private String hockeyAppVersionId;
    private String robotiumTests;
    private String testRunnerClass;
    private String platform;
    private Boolean hasIcon;
    private Boolean archived;
    private String xctestBundlePath;
    private String appCenterReleaseId;

}
