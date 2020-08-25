package org.fxclub.qa.browsermob.rest;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * https://github.com/lightbody/browsermob-proxy#rest-api
 */
public interface BmpRestClient {

    @GET("proxy")
    Call<ProxyList> listProxy();

    @POST("proxy")
    Call<ProxyInfo> createProxy();

    @FormUrlEncoded
    @POST("proxy")
    Call<ProxyInfo> createProxy(@Field("port") int port, @Field("useEcc") boolean useEcc, @Field("trustAllServers") boolean trustAllServers);

    @PUT("proxy/{port}/timeout")
    Call<ResponseBody> timeout(@Path("port") int port, @Body String jsonBody);

    @DELETE("proxy/{port}")
    Call<ResponseBody> stopProxy(@Path("port") int port);

    @POST("proxy/{port}/filter/response")
    Call<ResponseBody> setResponseFilter(@Path("port") int port, @Body RequestBody body);

    @POST("proxy/{port}/filter/request")
    @Headers("Content-Type: text/plain")
    Call<ResponseBody> setRequestFilter(@Path("port") int port, @Body String filter);

}
