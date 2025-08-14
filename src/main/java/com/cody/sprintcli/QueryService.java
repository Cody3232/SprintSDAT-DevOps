package com.cody.sprintcli;

import com.cody.sprintcli.models.Airport;
import com.cody.sprintcli.models.Aircraft;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QueryService {
    private final ApiClient client;
    private final Gson gson;

    public QueryService(ApiClient client) {
        this.client = client;
        this.gson = new GsonBuilder().setLenient().create();
    }

    /** Q1: Airports in a city (formatted string for CLI output) */
    public String airportsInCity(int cityId) {
        try {
            String json = client.getAirportsByCity(cityId);
            List<Airport> airports = parseAirports(json);
            if (airports.isEmpty()) {
                return "No airports found for city ID " + cityId + ".";
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < airports.size(); i++) {
                Airport a = airports.get(i);
                sb.append(i + 1).append(") ")
                        .append(a.getName() == null || a.getName().isBlank() ? "(unnamed)" : a.getName())
                        .append(" (").append(a.getCode() == null || a.getCode().isBlank() ? "???" : a.getCode()).append(")")
                        .append(" [id: ").append(a.getId()).append("]")
                        .append('\n');
            }
            return sb.toString().trim();
        } catch (IOException e) {
            String msg = e.getMessage() == null ? "" : e.getMessage();
            if (msg.contains("404")) return "City not found (id " + cityId + ").";
            return "Request failed: " + msg;
        }
    }

    /** Q2: Aircraft flown by a passenger (formatted for CLI) */
    public String aircraftForPassenger(int passengerId) {
        try {
            String json = client.getAircraftByPassenger(passengerId);
            List<Aircraft> aircraft = parseAircraft(json);
            if (aircraft.isEmpty()) {
                return "No aircraft found for passenger ID " + passengerId + ".";
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < aircraft.size(); i++) {
                Aircraft a = aircraft.get(i);
                String label =
                        (a.getModel() != null && !a.getModel().isBlank()) ? a.getModel()
                                : (a.getType() != null && !a.getType().isBlank()) ? a.getType()
                                : (a.getRegistration() != null && !a.getRegistration().isBlank()) ? a.getRegistration()
                                : "(unnamed aircraft)";
                sb.append(i + 1).append(") ")
                        .append(label)
                        .append(" [id: ").append(a.getId()).append("]");
                if (a.getRegistration() != null && !a.getRegistration().isBlank()) {
                    sb.append(" tail: ").append(a.getRegistration());
                }
                sb.append('\n');
            }
            return sb.toString().trim();
        } catch (IOException e) {
            String msg = e.getMessage() == null ? "" : e.getMessage();
            if (msg.contains("404")) return "Passenger not found (id " + passengerId + ").";
            return "Request failed: " + msg;
        }
    }

    /** Q3: Airports used by an aircraft (formatted for CLI) */
    public String airportsForAircraft(int aircraftId) {
        try {
            String json = client.getAirportsForAircraft(aircraftId);
            List<Airport> airports = parseAirports(json);
            if (airports.isEmpty()) {
                return "No airports found for aircraft ID " + aircraftId + ".";
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < airports.size(); i++) {
                Airport a = airports.get(i);
                sb.append(i + 1).append(") ")
                        .append(a.getName() == null || a.getName().isBlank() ? "(unnamed)" : a.getName())
                        .append(" (").append(a.getCode() == null || a.getCode().isBlank() ? "???" : a.getCode()).append(")")
                        .append(" [id: ").append(a.getId()).append("]")
                        .append('\n');
            }
            return sb.toString().trim();
        } catch (IOException e) {
            String msg = e.getMessage() == null ? "" : e.getMessage();
            if (msg.contains("404")) return "Aircraft not found (id " + aircraftId + ").";
            return "Request failed: " + msg;
        }
    }

    /** Q4: Airports used by a passenger (formatted for CLI) */
    public String airportsForPassenger(int passengerId) {
        try {
            String json = client.getAirportsUsedByPassenger(passengerId);
            List<Airport> airports = parseAirports(json);
            if (airports.isEmpty()) {
                return "No airports found for passenger ID " + passengerId + ".";
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < airports.size(); i++) {
                Airport a = airports.get(i);
                sb.append(i + 1).append(") ")
                        .append(a.getName() == null || a.getName().isBlank() ? "(unnamed)" : a.getName())
                        .append(" (").append(a.getCode() == null || a.getCode().isBlank() ? "???" : a.getCode()).append(")")
                        .append(" [id: ").append(a.getId()).append("]")
                        .append('\n');
            }
            return sb.toString().trim();
        } catch (IOException e) {
            String msg = e.getMessage() == null ? "" : e.getMessage();
            if (msg.contains("404")) return "Passenger not found (id " + passengerId + ").";
            return "Request failed: " + msg;
        }
    }

    // -------- helpers: airports --------

    private List<Airport> parseAirports(String json) {
        if (json == null || json.isBlank()) return List.of();
        JsonElement root = JsonParser.parseString(json);

        if (root.isJsonArray()) {
            return fromAirportArray(root.getAsJsonArray());
        }

        if (root.isJsonObject()) {
            JsonObject obj = root.getAsJsonObject();
            for (String key : new String[]{"airports", "data", "content", "items"}) {
                if (obj.has(key) && obj.get(key).isJsonArray()) {
                    return fromAirportArray(obj.getAsJsonArray(key));
                }
            }
            if (looksLikeAirport(obj)) {
                Airport single = gson.fromJson(obj, Airport.class);
                List<Airport> one = new ArrayList<>();
                one.add(single);
                return one;
            }
        }
        return List.of();
    }

    private List<Airport> fromAirportArray(JsonArray arr) {
        Type listType = new TypeToken<List<Airport>>() {}.getType();
        return gson.fromJson(arr, listType);
    }

    private boolean looksLikeAirport(JsonObject obj) {
        return obj.has("name") || obj.has("code") || obj.has("id");
    }

    // -------- helpers: aircraft --------

    private List<Aircraft> parseAircraft(String json) {
        if (json == null || json.isBlank()) return List.of();
        JsonElement root = JsonParser.parseString(json);

        if (root.isJsonArray()) {
            return fromAircraftArray(root.getAsJsonArray());
        }

        if (root.isJsonObject()) {
            JsonObject obj = root.getAsJsonObject();
            for (String key : new String[]{"aircraft", "airplanes", "data", "content", "items"}) {
                if (obj.has(key) && obj.get(key).isJsonArray()) {
                    return fromAircraftArray(obj.getAsJsonArray(key));
                }
            }
            if (looksLikeAircraft(obj)) {
                Aircraft single = gson.fromJson(obj, Aircraft.class);
                List<Aircraft> one = new ArrayList<>();
                one.add(single);
                return one;
            }
        }
        return List.of();
    }

    private List<Aircraft> fromAircraftArray(JsonArray arr) {
        Type listType = new TypeToken<List<Aircraft>>() {}.getType();
        return gson.fromJson(arr, listType);
    }

    private boolean looksLikeAircraft(JsonObject obj) {
        return obj.has("id") || obj.has("model") || obj.has("type") || obj.has("registration");
    }
}
