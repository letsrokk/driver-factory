package org.fxclub.qa.grid.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openqa.selenium.Platform;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Capabilities {

    private String browserName;
    private String browserVersion;

    //
    // Mobile Capabilities
    //
    private String deviceName;
    private Platform platformName;
    private String platformVersion;
    private String udid;

}
