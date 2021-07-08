package com.github.letsrokk.grid.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteSession {

    private String sessionId;
    private Integer status;
    private List<RemoteSessionDetails> value;

}
