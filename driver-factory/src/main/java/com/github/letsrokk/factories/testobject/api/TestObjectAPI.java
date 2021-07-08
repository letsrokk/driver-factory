package com.github.letsrokk.factories.testobject.api;

import com.github.letsrokk.factories.DriverFactory;
import com.github.letsrokk.factories.appium.DeviceInfo;
import com.github.letsrokk.factories.appium.DeviceType;
import com.github.letsrokk.factories.testobject.TestObjectFactoryCapabilities;
import com.github.letsrokk.factories.testobject.api.reports.TestObjectTestReport;
import com.google.common.io.Files;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.openqa.selenium.Platform;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestObjectAPI {

    private static Logger logger = LogManager.getLogger(DriverFactory.class);

    private static final TestObjectRestService testObjectRestService;
    private static final String credentials;

    static {
        String apiEndpoint = TestObjectFactoryCapabilities.TESTOBJECT_API_ENDPOINT.toString();
        if(!apiEndpoint.endsWith("/"))
            apiEndpoint += "/";
        logger.info("TESTOBJECT: API endpoint: " + apiEndpoint);

        testObjectRestService = new Retrofit.Builder()
                .baseUrl(apiEndpoint)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(TestObjectRestService.class);

        credentials = Credentials.basic(TestObjectFactoryCapabilities.getTestobjectApiAccount(), TestObjectFactoryCapabilities.getTestobjectApiKey());
        logger.info("TESTOBJECT: account: " + TestObjectFactoryCapabilities.getTestobjectApiAccount());
    }

    public static TestObjectDevicesList getDevices() throws IOException {
        return testObjectRestService
                .getDevices(credentials)
                .execute()
                .body();
    }

    public static List<DeviceInfo> getAvailableDevices() throws IOException {
        final TestObjectDevicesList devicesList = getDevices();

        final TestObjectAvailableDevicesList availableList = testObjectRestService
                .getAvailableDevices(credentials)
                .execute()
                .body();

        List<DeviceInfo> eu = devicesList.getEU().stream()
                .filter(device -> Objects.requireNonNull(availableList).getEU().contains(device.getId()))
                .map(device -> DeviceInfo.builder()
                        .DEVICE_NAME(device.getName())
                        .PLATFORM(Platform.fromString(device.getOs()))
                        .PLATFORM_VERSION(device.getOsVersion())
                        .REAL(true)
                        .TYPE(device.getIsTablet() ? DeviceType.TABLET : DeviceType.PHONE)
                        .UDID(device.getId())
                        .CODE(device.getId())
                        .build()).collect(Collectors.toList());

        List<DeviceInfo> us = devicesList.getUS().stream()
                .filter(device -> Objects.requireNonNull(availableList).getUS().contains(device.getId()))
                .map(device -> DeviceInfo.builder()
                        .DEVICE_NAME(device.getName())
                        .PLATFORM(Platform.fromString(device.getOs()))
                        .PLATFORM_VERSION(device.getOsVersion())
                        .REAL(true)
                        .TYPE(device.getIsTablet() ? DeviceType.TABLET : DeviceType.PHONE)
                        .UDID(device.getId())
                        .CODE(device.getId())
                        .build()).collect(Collectors.toList());

        return eu;
    }

    public static List<AppInfo> getApplicationInfo(File app) throws IOException {
        return Optional
                .ofNullable(testObjectRestService.getAppInfo(credentials, generateMD5(app)).execute().body())
                .orElse(Collections.emptyList());
    }

    public static synchronized Integer uploadApp(File app) throws IOException {
        if(!app.exists()) {
            logger.error("TESTOBJECT: application not found: " + app.getAbsolutePath());
            throw new IllegalArgumentException("Applications Not Found: " + app.getAbsolutePath());
        }

        List<AppInfo> appInfo = getApplicationInfo(app);

        if(!appInfo.isEmpty()) {
            return appInfo.get(0).getId();
        } else {
            logger.info("TESTOBJECT: upload application: " + app.getAbsolutePath());

            RequestBody fbody = RequestBody.create(MediaType.parse("application/octet-stream"), app);
            Integer appId = testObjectRestService.upload(
                    credentials,
                    generateMD5(app),
                    app.getName(),
                    fbody
            ).execute().body();
            logger.info("TESTOBJECT: applications uploaded: " + appId);
            return appId;
        }
    }

    private static String generateMD5(File file) throws IOException {
        return DigestUtils.md5Hex(Files.toByteArray(file));
    }

    @SuppressWarnings("unchecked")
    public static void updateExecutionStatus(String sessionId, boolean passed) throws IOException {
        logger.debug(String.format("TESTOBJECT: update execution %s with status PASSED = %b", sessionId, passed));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("passed", passed);
        RequestBody jsonBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toJSONString());
        testObjectRestService.updateTest(sessionId, jsonBody).execute();
    }

    public static void skipExecution(String sessionId) throws IOException {
        logger.debug(String.format("TESTOBJECT: update execution %s with status SKIPPED", sessionId));

        testObjectRestService.skipTest(sessionId).execute();
    }

    public static TestObjectTestReport getTestReport(long testReportId) throws IOException {
        logger.debug(String.format("TESTOBJECT: get test report ID %s", testReportId));

        return testObjectRestService.getTestReport(credentials, testReportId).execute().body().getReport();
    }

    public static File downloadVideo(String videoId) throws IOException {
        logger.debug(String.format("TESTOBJECT: download video ID %s", videoId));

        byte[] byteArray = testObjectRestService.downloadVideo(credentials, videoId).execute().body().bytes();

        String tmpDir = System.getProperty("java.io.tmpdir");
        File mp4 = new File(tmpDir + "/" + videoId + ".mp4");
        Files.write(byteArray, mp4);

        return mp4;
    }
}
