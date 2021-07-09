package com.github.letsrokk.browsermob;

import com.github.letsrokk.browsermob.rest.BmpRestClient;
import com.github.letsrokk.browsermob.rest.ProxyInfo;
import net.lightbody.bmp.client.ClientUtil;
import okhttp3.RequestBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.letsrokk.factories.selenoid.SelenoidFactory;
import org.openqa.selenium.Proxy;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Optional;

public class BrowserMobUtils {

    private static Logger logger = LogManager.getLogger(SelenoidFactory.class);

    private static ThreadLocal<BmpRestClient> bmpRestClient = new InheritableThreadLocal<>();

    private static ThreadLocal<String> proxyHost = new InheritableThreadLocal<>();
    private static ThreadLocal<ProxyInfo> proxyInfo = new InheritableThreadLocal<>();

    private static ThreadLocal<RequestBody> responseFilter = new InheritableThreadLocal<>();

    /**
     * Init BrowserMob proxy
     * @throws UnknownHostException unknown BMP host provided
     */
    public static void initProxy() throws UnknownHostException {
        final String browsermobHost = Optional.ofNullable(System.getProperty("BMP_HOST", System.getenv("BMP_HOST")))
                .orElse(Inet4Address.getLocalHost().getHostAddress());
        proxyHost.set(browsermobHost);

        final String browsermobPort = Optional.ofNullable(System.getProperty("BMP_PORT", System.getenv("BMP_PORT")))
                .orElse("9090");

        bmpRestClient.set(
                new Retrofit.Builder()
                        .baseUrl("http://"+browsermobHost+":"+browsermobPort+"")
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build()
                        .create(BmpRestClient.class)
        );
    }

    /**
     * Get instance of Selenium proxy
     * @return Selenium proxy configuration
     * @throws UnknownHostException unknown proxy host
     */
    public static Proxy getSeleniumProxy() throws UnknownHostException {
        return ClientUtil.createSeleniumProxy(new InetSocketAddress(InetAddress.getByName(proxyHost.get()), proxyInfo.get().getPort()));
    }

    /**
     * Set BMP proxy filter
     * @param filter filter
     */
    public static void setResponseFilter(final RequestBody filter){
        responseFilter.set(filter);
    }

    /**
     * Start Proxy
     * @throws IOException unknown proxy exception
     */
    public static void startProxy() throws IOException {
        proxyInfo.set(bmpRestClient.get().createProxy().execute().body());
        Optional.ofNullable(responseFilter.get())
                .ifPresent(filter -> {
                    try {
                        bmpRestClient.get().setResponseFilter(proxyInfo.get().getPort(), filter).execute();
                    } catch (IOException e) {
                        logger.error("BrowserMob Proxy response filter was not registred", e);
                    }
                });
    }

    /**
     * Stop Proxy
     */
    public static void stopProxy() {
        Optional.ofNullable(bmpRestClient.get())
                .ifPresent(client -> {
                    try{
                        client.stopProxy(proxyInfo.get().getPort()).execute();
                    } catch (IOException e){
                        logger.warn("BrowserMob Proxy stop error", e);
                    }
                });
        proxyInfo.remove();
        bmpRestClient.remove();
    }
}
