package com.github.letsrokk.grid.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteSessionDetails {

    private String id;
    private Capabilities capabilities;

}
