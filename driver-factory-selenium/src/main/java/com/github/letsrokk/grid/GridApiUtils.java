package com.github.letsrokk.grid;

import com.github.letsrokk.grid.exceptions.GridApiException;
import com.github.letsrokk.grid.model.RemoteProxy;
import com.github.letsrokk.grid.model.RemoteProxyConfiguration;
import com.github.letsrokk.grid.model.RemoteSessionDetails;
import com.github.letsrokk.grid.model.WDSessionInfo;
import com.github.letsrokk.grid.response.GridApiProxyResponse;
import com.github.letsrokk.grid.response.GridApiSessionsResponse;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GridApiUtils {

    private GridRestService gridRestService;

    private GridApiUtils(GridRestService gridRestService) {
        this.gridRestService = gridRestService;
    }

    public static GridApiUtils create(URL remoteAddress) {
        String gridAddressBaseUrl
                = remoteAddress.getProtocol() + "://" + remoteAddress.getHost() + ":" + remoteAddress.getPort();
        GridRestService service = new Retrofit.Builder()
                .baseUrl(gridAddressBaseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(GridRestService.class);
        return new GridApiUtils(service);
    }

    @SneakyThrows
    public WDSessionInfo getSessionInfo(String sessionId) {
        return gridRestService.grid_api_testsession(sessionId).execute().body();
    }

    @SneakyThrows
    public RemoteProxyConfiguration getProxyConfiguration(String proxyId) {
        Response<GridApiProxyResponse> response =
                gridRestService.grid_api_proxy(proxyId).execute();

        RemoteProxyConfiguration proxyConfiguration;
        if (response.isSuccessful() && Objects.nonNull(response.body()) && response.body().getSuccess()) {
            proxyConfiguration = response.body().getRequest().getConfiguration();
        } else {
            throw new GridApiException();
        }

        return proxyConfiguration;
    }

    public List<RemoteProxy> getProxies() throws IOException {
        ResponseBody rawResponseBody
                = gridRestService.grid_console().execute().body();
        Document consoleResponse = Jsoup.parse(rawResponseBody.string());

        Elements proxyElements = consoleResponse.select("p.proxyid");
        List<String> proxyIds = proxyElements.stream()
                .map(proxyElement -> {
                    Matcher proxyIdMatcher =
                            Pattern.compile("id : (http://.*),.*").matcher(proxyElement.text());
                    String proxyId;
                    if (proxyIdMatcher.find()) {
                        proxyId = proxyIdMatcher.group(1);
                    } else {
                        proxyId = null;
                    }
                    return proxyId;
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<RemoteProxy> proxies =
                proxyIds.parallelStream()
                        .map(proxyId -> {
                            RemoteProxyConfiguration configuration = getProxyConfiguration(proxyId);
                            return RemoteProxy.builder()
                                    .id(proxyId)
                                    .configuration(configuration)
                                    .build();
                        })
                        .collect(Collectors.toList());

        return proxies;
    }

    public List<RemoteSessionDetails> getActiveSessions() throws IOException {
        Response<GridApiSessionsResponse> response =
                gridRestService.grid_api_sessions().execute();

        List<RemoteSessionDetails> activeSessions = new ArrayList<>();
        if (response.isSuccessful() && Objects.nonNull(response.body()) && response.body().getSuccess()) {
            response.body().getProxies().stream()
                    .map(proxy -> proxy.getSessions().getValue())
                    .forEach(activeSessions::addAll);
        }
        return activeSessions;
    }


}
