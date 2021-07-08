package com.github.letsrokk.factories.testobject.api.reports;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.github.letsrokk.factories.testobject.api.TestObjectDeviceInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class TestObjectTestReport {

    private String type;
    private TestObjectProjectPath projectPath;

    private Long id;
    private String UUID;

    private String sessionId;
    private String videoId;

    private TestObjectDeviceInfo deviceDescriptor;
    private String dataCenterId;

    private String status;
    private Boolean running;

    private String systemError;
    private String systemErrorType;
    private Boolean withSystemError;
    private String testResultStatus;

    private String sharedLinkCode;
}
