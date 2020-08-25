package org.fxclub.qa.grid.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fxclub.qa.grid.model.RemoteProxy;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GridApiSessionsResponse {

    private List<RemoteProxy> proxies;
    private Boolean success;

}
