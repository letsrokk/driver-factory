package com.github.letsrokk.grid.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.letsrokk.grid.model.RemoteProxyRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GridApiProxyResponse {

    private Boolean success;
    private String id;
    private RemoteProxyRequest request;

}
