package org.fxclub.qa.factories.testobject.api.reports;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestObjectReportsResponse {

    private TestObjectTestReport report;
    private String deviceContextId;

}
