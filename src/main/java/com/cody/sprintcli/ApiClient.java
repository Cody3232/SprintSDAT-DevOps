package com.cody.sprintcli;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    private final String baseUrl;
    private final HttpClient http = HttpClient.newHttpClient();

    public ApiClient(ApiConfig config) {
        String cfg = (config != null && config.getBaseUrl() != null) ? config.getBaseUrl() : "";
        // keep whatever the user gives, but strip trailing slashes
        this.baseUrl = (cfg.isBlank() ? "http://localhost:8080" : cfg).replaceAll("/+$", "");
    }

    // --- public endpoints (use safe joiner) ---
    public String getAirportsByCity(int cityId) throws IOException {
        return get(u("/api/airports/byCity/" + cityId));
    }

    public String getAircraftByPassenger(int passengerId) throws IOException {
        return get(u("/api/aircraft/byPassenger/" + passengerId));
    }

    public String getAirportsForAircraft(int aircraftId) throws IOException {
        return get(u("/api/airports/byAircraft/" + aircraftId));
    }

    public String getAirportsUsedByPassenger(int passengerId) throws IOException {
        return get(u("/api/airports/byPassenger/" + passengerId));
    }
    public String getAircraftById(int aircraftId) throws IOException {
        return get(baseUrl + "/api/aircraft/" + aircraftId);
    }

    // --- helpers ---
    /** Safely join baseUrl + path, avoiding double "/api". */
    private String u(String path) {
        String root = baseUrl.replaceAll("/+$", "");
        String p = path.startsWith("/") ? path : "/" + path;
        if (root.endsWith("/api") && p.startsWith("/api/")) {
            p = p.substring(4); // drop the leading "/api" from the path
        }
        return root + p;
    }

    private String get(String url) throws IOException {
        try {
            System.out.println("DEBUG GET " + url);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 404) throw new IOException("404 Not Found");
            if (res.statusCode() >= 400) throw new IOException("HTTP " + res.statusCode());
            return res.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        } catch (IllegalArgumentException e) {
            throw new IOException("Bad URL: " + url, e);
        }
    }
}
