package com.cody.sprintcli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class ApiClient {
    private final ApiConfig config;

    public ApiClient(ApiConfig config) {
        this.config = config;
    }

    /** Generic GET that all helpers use. Throws on non-2xx with status/snippet. */
    public String get(String path, Map<String,String> queryParams) throws IOException {
        URL url = buildUrl(path, queryParams);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(config.getConnectTimeoutMs());
        conn.setReadTimeout(config.getReadTimeoutMs());

        int code = conn.getResponseCode();
        InputStream stream = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
        String body = readBody(stream);
        conn.disconnect();

        if (code >= 200 && code < 300) {
            return body == null ? "" : body;
        } else {
            String snippet = (body == null) ? "" : (body.length() > 300 ? body.substring(0, 300) + "..." : body);
            throw new IOException("HTTP " + code + " GET " + url + (snippet.isEmpty() ? "" : " — " + snippet));
        }
    }

    private URL buildUrl(String path, Map<String,String> query) throws MalformedURLException {
        String base = config.getBaseUrl();
        StringBuilder sb = new StringBuilder();
        if (base.endsWith("/") && path.startsWith("/")) {
            sb.append(base, 0, base.length() - 1).append(path);
        } else if (!base.endsWith("/") && !path.startsWith("/")) {
            sb.append(base).append("/").append(path);
        } else {
            sb.append(base).append(path);
        }
        if (query != null && !query.isEmpty()) {
            sb.append('?').append(query.entrySet().stream()
                    .map(e -> enc(e.getKey()) + "=" + enc(e.getValue()))
                    .collect(Collectors.joining("&")));
        }
        return new URL(sb.toString());
    }

    private static String enc(String s) {
        try { return URLEncoder.encode(s, StandardCharsets.UTF_8.toString()); }
        catch (Exception e) { return s; }
    }

    private static String readBody(InputStream in) throws IOException {
        if (in == null) return "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder(); String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }

    // wrappers (instance methods, no hardcoded base URL)
    public String getAirportsByCity(int cityId) throws java.io.IOException {
        return get("/api/cities/" + cityId + "/airports", java.util.Map.of());
    }
    public String getAircraftByPassenger(int passengerId) throws java.io.IOException {
        return get("/api/passengers/" + passengerId + "/aircraft", java.util.Map.of());
    }
    public String getAirportsUsedByPassenger(int passengerId) throws java.io.IOException {
        return get("/api/passengers/" + passengerId + "/airports", java.util.Map.of());
    }
    public String getAirportsForAircraft(int aircraftId) throws java.io.IOException {
        return get("/api/aircraft/" + aircraftId + "/airports", java.util.Map.of());
    }
}
