package com.cody.sprintcli;

public class ApiConfig {
    private final String baseUrl;
    private final int connectTimeoutMs;
    private final int readTimeoutMs;

    public ApiConfig() {
        this.baseUrl = getenvOrDefault("API_BASE_URL", "http://localhost:8080");
        this.connectTimeoutMs = parseIntEnv("API_CONNECT_TIMEOUT_MS", 5000);
        this.readTimeoutMs = parseIntEnv("API_READ_TIMEOUT_MS", 8000);
    }

    public String getBaseUrl() {
        return baseUrl;
    }
    public int getConnectTimeoutMs(){
        return connectTimeoutMs;
    }
    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    private static String getenvOrDefault(String key, String def) {
        String envValue = System.getenv(key);
        return (envValue == null || envValue.isBlank()) ? def : envValue;
    }
    private static int parseIntEnv(String key, int def) {
        try {
            String envValue = System.getenv(key);
            return (envValue == null || envValue.isBlank()) ? def : Integer.parseInt(envValue.trim());
        } catch (NumberFormatException e){
            return def;
        }
    }
}
