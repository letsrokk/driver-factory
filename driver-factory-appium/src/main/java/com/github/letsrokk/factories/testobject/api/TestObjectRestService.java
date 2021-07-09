package com.github.letsrokk.factories.testobject.api;

import com.github.letsrokk.factories.testobject.api.reports.TestObjectReportsResponse;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by majer-dy on 11/05/2017.
 */
public interface TestObjectRestService {

    @GET("rest/v2/devices")
    Call<TestObjectDevicesList> getDevices(@Header("Authorization") String authorization);

    @GET("rest/v2/devices/available")
    Call<TestObjectAvailableDevicesList> getAvailableDevices(@Header("Authorization") String authorization);

    @GET("storage/app")
    Call<List<AppInfo>> getAppInfo(
            @Header("Authorization") String authorization,
            @Query("appIdentifier") String md5
    );

    @Headers("Content-Type: application/octet-stream")
    @POST("storage/upload")
    Call<Integer> upload(
            @Header("Authorization") String authorization,
            @Header("App-Identifier") String md5,
            @Header("App-DisplayName") String appName,
            @Body RequestBody appBinary
    );

    @PUT("rest/v2/appium/session/{sessionId}/test")
    Call<String> updateTest(@Path("sessionId") String sessionId, @Body RequestBody jsonBody);

    @PUT("rest/v2/appium/session/{sessionId}/skiptest")
    Call<String> skipTest(@Path("sessionId") String sessionId);

    @GET("rest/v2/reports/{testReportId}")
    Call<TestObjectReportsResponse> getTestReport(
            @Header("Authorization") String authorization,
            @Path("testReportId") long testReportId
    );

    @Streaming
    @GET("rest/v2/video/{videoId}")
    Call<ResponseBody> downloadVideo(
            @Header("Authorization") String authorization,
            @Path("videoId") String videoId
    );
}
