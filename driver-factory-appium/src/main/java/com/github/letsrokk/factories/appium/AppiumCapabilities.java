package com.github.letsrokk.factories.appium;

import com.github.letsrokk.DriverFactoryEnvs;
import com.github.letsrokk.factories.selenium.MODE;
import com.github.letsrokk.grid.GridApiUtils;
import com.github.letsrokk.grid.model.Capabilities;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AppiumCapabilities {

    public static DesiredCapabilities getCapabilities(MODE driverMode, GridApiUtils gridApiUtils) throws IOException, InterruptedException {
        String appPath = getApplication();
        Platform platform = getPlatform();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (driverMode == MODE.local) {
            List<DeviceInfo> devices = platform.equals(Platform.ANDROID)
                    ? getAndroidDevices()
                    : getIOSDevices();

            DeviceInfo deviceInfo = devices.stream()
                    .filter(DeviceInfo::isREAL)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No device available"));

            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, deviceInfo.getPLATFORM());
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, deviceInfo.getPLATFORM_VERSION());

            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceInfo.getDEVICE_NAME());
            capabilities.setCapability(MobileCapabilityType.UDID, deviceInfo.getUDID());

            capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, deviceInfo.getBROWSER_NAME());

            capabilities.setCapability(MobileCapabilityType.APP, appPath);

            // Set new command timeout in Seconds
            capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 600);
        } else if (driverMode == MODE.grid) {
            boolean hasVersion = StringUtils.isNotEmpty(getPlatformVersion());
            List<DeviceInfo> devices = getGridCapabilities(gridApiUtils);

            DeviceInfo deviceInfo = devices.stream()
                    .filter(device -> device.getPLATFORM() == platform)
                    .filter(device -> {
                        String platformVersion = getPlatformVersion();
                        return !hasVersion
                                || StringUtils.startsWith(device.getBROWSER_VERSION(), platformVersion);
                    })
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No device available"));

            capabilities.setCapability(CapabilityType.PLATFORM_NAME, deviceInfo.getPLATFORM());
            capabilities.setCapability(CapabilityType.BROWSER_NAME, deviceInfo.getDEVICE_NAME());
            if (hasVersion) {
                capabilities.setCapability(CapabilityType.BROWSER_VERSION, deviceInfo.getPLATFORM_VERSION());
            }
            capabilities.setCapability(MobileCapabilityType.APP, appPath);
        } else {
            throw new IllegalArgumentException(String.format("Driver mode %s is not supported", driverMode.name()));
        }

        return capabilities;
    }

    private static List<DeviceInfo> getGridCapabilities(GridApiUtils gridApiUtils) throws IOException, InterruptedException {
        List<DeviceInfo> devices =
                gridApiUtils.getProxies().stream()
                        .map(proxy -> {
                            Capabilities capabilities = proxy
                                    .getConfiguration()
                                    .getCapabilities()
                                    .get(0);
                            return DeviceInfo.builder()
                                    .DEVICE_NAME(capabilities.getDeviceName())
                                    .BROWSER_NAME(capabilities.getBrowserName())
                                    .BROWSER_VERSION(capabilities.getBrowserVersion())
                                    .PLATFORM(capabilities.getPlatformName())
                                    .PLATFORM_VERSION(capabilities.getPlatformVersion())
                                    .UDID(capabilities.getUdid())
                                    .REAL(true)
                                    .build();
                        })
                        .filter(distinctByBrowserCapability(
                                d -> String.format("%s+%s", d.getBROWSER_NAME(), d.getBROWSER_VERSION())
                        ))
                        .collect(Collectors.toList());
        return devices;
    }

    private static <T> Predicate<T> distinctByBrowserCapability(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private static String getPlatformVersion() {
        String platformVersion = System.getProperty(DriverFactoryEnvs.PLATFORM_VERSION, System.getenv(DriverFactoryEnvs.PLATFORM_VERSION));
        return Optional.ofNullable(platformVersion).orElse("");
    }

    private static List<DeviceInfo> getIOSDevices() throws IOException, InterruptedException {
        String output = Terminal.execute("instruments", "-s", "devices");

        String baseRegEx = "((iPhone|iPad) [0-9 a-zA-Z().+]+) \\(([0-9.]+)\\) \\[([a-zA-Z0-9-]+)\\]";

        List<DeviceInfo> devices = new ArrayList<>();

        Pattern simulators = Pattern.compile("^" + baseRegEx + " \\(Simulator\\)$", Pattern.MULTILINE);
        Matcher matcher = simulators.matcher(output);
        while (matcher.find()) {
            String name = matcher.group(1);
            String type = matcher.group(2);
            String version = matcher.group(3);
            String uuid = matcher.group(4);

            DeviceInfo deviceInfo = DeviceInfo.builder()
                    .DEVICE_NAME(name)
                    .TYPE(type.equalsIgnoreCase("iPhone") ? DeviceType.PHONE : DeviceType.TABLET)
                    .PLATFORM(Platform.IOS)
                    .PLATFORM_VERSION(version)
                    .UDID(uuid)
                    .REAL(false)
                    .build();

            devices.add(deviceInfo);
        }

        Pattern realDevices = Pattern.compile("^" + baseRegEx + "$", Pattern.MULTILINE);
        matcher = realDevices.matcher(output);
        while (matcher.find()) {
            String name = matcher.group(1);
            String type = matcher.group(2);
            String version = matcher.group(3);
            String uuid = matcher.group(4);

            DeviceInfo deviceInfo = DeviceInfo.builder()
                    .DEVICE_NAME(name)
                    .BROWSER_NAME(name)
                    .TYPE(type.equalsIgnoreCase("iPhone") ? DeviceType.PHONE : DeviceType.TABLET)
                    .PLATFORM(Platform.IOS)
                    .PLATFORM_VERSION(version)
                    .UDID(uuid)
                    .REAL(true)
                    .build();

            devices.add(deviceInfo);
        }
        return devices;
    }

    private static List<DeviceInfo> getAndroidDevices() throws IOException, InterruptedException {
        List<DeviceInfo> devices = new ArrayList<>();

        String output = Terminal.execute("adb", "devices");

        String regex = "^([0-9a-zA-Z]+).*device$";
        Pattern simulators = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = simulators.matcher(output);
        while (matcher.find()) {
            String uuid = matcher.group(1);

            String buildCharacterestics = getAndroidDeviceInfo(uuid, "ro.build.characteristics");
            String version = getAndroidDeviceInfo(uuid, "ro.build.version.release");
            String vendor = getAndroidDeviceInfo(uuid, "ro.product.brand");
            String model = getAndroidDeviceInfo(uuid, "ro.product.model");

            DeviceInfo deviceInfo = DeviceInfo.builder()
                    .DEVICE_NAME(vendor + " " + model)
                    .BROWSER_NAME(vendor + " " + model)
                    .TYPE(buildCharacterestics.toLowerCase().contains("phone") ? DeviceType.PHONE : DeviceType.TABLET)
                    .PLATFORM(Platform.ANDROID)
                    .PLATFORM_VERSION(version)
                    .UDID(uuid)
                    .REAL(true)
                    .build();

            devices.add(deviceInfo);
        }
        return devices;
    }

    private static String getAndroidDeviceInfo(String uuid, String param) throws IOException, InterruptedException {
        return StringUtils.trimToEmpty(Terminal.execute("adb", "-s", uuid, "shell", "getprop", param));
    }

    private static String getApplication() {
        URI app = URI.create(
                System.getProperty(DriverFactoryEnvs.APPLICATION_PATH, System.getenv(DriverFactoryEnvs.APPLICATION_PATH))
        );
        if (!app.isAbsolute() || app.getScheme().equals("file")) {
            return new File(app.getPath()).getAbsolutePath();
        } else {
            return app.toString();
        }
    }

    public static Platform getPlatform() {
        return getApplication().endsWith("apk")
                ? Platform.ANDROID
                : Platform.IOS;
    }

}
