package org.fxclub.qa.grid.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fxclub.qa.grid.model.RemoteProxyRequest;

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
