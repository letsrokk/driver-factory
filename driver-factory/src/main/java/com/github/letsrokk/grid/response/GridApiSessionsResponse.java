package com.github.letsrokk.grid.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.letsrokk.grid.model.RemoteProxy;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GridApiSessionsResponse {

    private List<RemoteProxy> proxies;
    private Boolean success;

}
