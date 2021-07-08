package com.github.letsrokk.grid;

import okhttp3.ResponseBody;
import com.github.letsrokk.grid.model.WDSessionInfo;
import com.github.letsrokk.grid.response.GridApiProxyResponse;
import com.github.letsrokk.grid.response.GridApiSessionsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by majer-dy on 11/05/2017.
 */
public interface GridRestService {

    @GET("grid/api/testsession")
    Call<WDSessionInfo> grid_api_testsession(@Query("session") String sessionId);

    @GET("grid/api/sessions")
    Call<GridApiSessionsResponse> grid_api_sessions();

    @GET("grid/console")
    Call<ResponseBody> grid_console();

    @GET("grid/api/proxy")
    Call<GridApiProxyResponse> grid_api_proxy(@Query("id") String proxyId);

}
