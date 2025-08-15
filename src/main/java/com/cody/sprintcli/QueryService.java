package com.cody.sprintcli;

import com.cody.sprintcli.models.Airport;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.*;

/** Compact QueryService: streaming parse + minimal sanitizers to avoid huge/malformed nested fields. */
public class QueryService {
    private final ApiClient client;
    private final Gson gson;

    public QueryService(ApiClient client) {
        this.client = client;
        this.gson = new GsonBuilder().setLenient().create();
    }

    // ---------------- Q1: Airports in a city (DOM parse into Airport POJO) ----------------
    public String airportsInCity(int cityId) {
        try {
            String json = client.getAirportsByCity(cityId);
            List<Airport> airports = parseAirports(json);
            if (airports.isEmpty()) return "No airports found for city ID " + cityId + ".";
            StringBuilder display = new StringBuilder();
            for (int idx = 0; idx < airports.size(); idx++) {
                Airport ap = airports.get(idx);
                display.append(idx + 1).append(") ")
                        .append(isBlank(ap.getName()) ? "(unnamed)" : ap.getName())
                        .append(" (").append(isBlank(ap.getCode()) ? "???" : ap.getCode()).append(")")
                        .append(" [id: ").append(ap.getId()).append("]\n");
            }
            return display.toString().trim();
        } catch (IOException e) {
            String msg = e.getMessage() == null ? "" : e.getMessage();
            if (msg.contains("404")) return "City not found (id " + cityId + ").";
            return "Request failed: " + msg;
        }
    }

    // ---------------- Q2: Aircraft flown by a passenger (sanitize airports[], then stream) ----------------
    public String aircraftForPassenger(int passengerId) {
        try {
            String raw = client.getAircraftByPassenger(passengerId);
            String sanitized = sanitizeJson(raw, "airports", "[]"); // remove recursive airport blobs
            List<AircraftLite> list = parseAircraftList(sanitized);
            if (list.isEmpty()) return "No aircraft found for passenger ID " + passengerId + ".";

            StringBuilder display = new StringBuilder();
            int lineNo = 0;
            for (AircraftLite ac : list) {
                String label = !isBlank(ac.model) ? ac.model
                        : !isBlank(ac.type) ? ac.type
                        : !isBlank(ac.registration) ? ac.registration
                        : "(unnamed aircraft)";
                display.append(++lineNo).append(") ").append(label);
                if (ac.id > 0) display.append(" [id: ").append(ac.id).append("]");
                if (!isBlank(ac.registration)) display.append(" tail: ").append(ac.registration);
                display.append('\n');
            }
            return display.toString().trim();

        } catch (IOException e) {
            String msg = e.getMessage() == null ? "" : e.getMessage();
            if (msg.contains("404")) return "Passenger not found (id " + passengerId + ").";
            return "Request failed: " + msg;
        } catch (Exception e) {
            return "Request failed: " + e.getMessage();
        }
    }

    // ---------------- Q3: Airports used by an aircraft (null-out city, then stream) ----------------
    public String airportsForAircraft(int aircraftId) {
        try {
            String raw = client.getAircraftById(aircraftId);
            String sanitized = sanitizeJson(raw, "city", "null"); // keep airports[], kill giant city trees
            List<AirportLite> airports = collectAirportsFromJson(sanitized, false);
            if (airports.isEmpty()) return "No airports found for aircraft ID " + aircraftId + ".";

            StringBuilder display = new StringBuilder();
            for (int idx = 0; idx < airports.size(); idx++) {
                AirportLite ap = airports.get(idx);
                display.append(idx + 1).append(") ")
                        .append(isBlank(ap.name) ? "(unnamed)" : ap.name)
                        .append(" (").append(isBlank(ap.code) ? "???" : ap.code).append(")")
                        .append(" [id: ").append(ap.id).append("]\n");
            }
            return display.toString().trim();

        } catch (IOException e) {
            String msg = e.getMessage() == null ? "" : e.getMessage();
            if (msg.contains("404")) return "Aircraft not found (id " + aircraftId + ").";
            return "Request failed: " + msg;
        } catch (Exception e) {
            return "Request failed: " + e.getMessage();
        }
    }

    // ---------------- Q4: Airports used by a passenger (null-out city on aircraft list, then stream & dedup) ----------------
    public String airportsForPassenger(int passengerId) {
        try {
            String raw = client.getAircraftByPassenger(passengerId);
            String sanitized = sanitizeJson(raw, "city", "null"); // list of aircraft, each with airports[]; nuke city
            List<AirportLite> airports = collectAirportsFromJson(sanitized, true);
            if (airports.isEmpty()) return "No airports found for passenger ID " + passengerId + ".";

            StringBuilder display = new StringBuilder();
            for (int idx = 0; idx < airports.size(); idx++) {
                AirportLite ap = airports.get(idx);
                display.append(idx + 1).append(") ")
                        .append(isBlank(ap.name) ? "(unnamed)" : ap.name)
                        .append(" (").append(isBlank(ap.code) ? "???" : ap.code).append(")")
                        .append(" [id: ").append(ap.id).append("]\n");
            }
            return display.toString().trim();

        } catch (IOException e) {
            String msg = e.getMessage() == null ? "" : e.getMessage();
            if (msg.contains("404")) return "Passenger not found (id " + passengerId + ").";
            return "Request failed: " + msg;
        } catch (Exception e) {
            return "Request failed: " + e.getMessage();
        }
    }

    // ===================== DOM parse for Q1 =====================
    private static boolean isBlank(String value) { return value == null || value.isBlank(); }

    private List<Airport> parseAirports(String json) {
        if (isBlank(json)) return List.of();
        JsonElement root = JsonParser.parseString(json);
        if (root.isJsonArray()) return airportsFromArray(root.getAsJsonArray());
        if (root.isJsonObject()) {
            JsonObject obj = root.getAsJsonObject();
            for (String key : new String[]{"airports", "data", "content", "items"}) {
                if (obj.has(key) && obj.get(key).isJsonArray()) return airportsFromArray(obj.getAsJsonArray(key));
            }
            return List.of(gson.fromJson(obj, Airport.class));
        }
        return List.of();
    }

    private List<Airport> airportsFromArray(JsonArray arr) {
        Type listType = new TypeToken<List<Airport>>() {}.getType();
        return gson.fromJson(arr, listType);
    }

    // ===================== Streaming models =====================
    private static record AircraftLite(int id, String model, String type, String registration) {}
    private static record AirportLite(int id, String name, String code) {}

    // ===================== Streaming parse helpers =====================
    private List<AircraftLite> parseAircraftList(String json) throws IOException {
        List<AircraftLite> result = new ArrayList<>();
        if (isBlank(json)) return result;

        try (JsonReader reader = new JsonReader(new StringReader(json))) {
            reader.setLenient(true);
            JsonToken firstToken = reader.peek();

            if (firstToken == JsonToken.BEGIN_ARRAY) {
                reader.beginArray();
                while (reader.hasNext()) {
                    if (reader.peek() == JsonToken.BEGIN_OBJECT) result.add(readAircraft(reader));
                    else reader.skipValue();
                }
                reader.endArray();
                return result;
            }

            if (firstToken == JsonToken.BEGIN_OBJECT) {
                reader.beginObject();
                boolean handled = false;
                while (reader.hasNext()) {
                    String field = reader.nextName();
                    JsonToken token = reader.peek();
                    if (!handled && token == JsonToken.BEGIN_OBJECT) {
                        result.add(readAircraft(reader)); handled = true;
                    } else if (token == JsonToken.BEGIN_ARRAY &&
                            (field.equals("aircraft") || field.equals("airplanes") ||
                                    field.equals("data") || field.equals("content") || field.equals("items"))) {
                        reader.beginArray();
                        while (reader.hasNext()) {
                            if (reader.peek() == JsonToken.BEGIN_OBJECT) result.add(readAircraft(reader));
                            else reader.skipValue();
                        }
                        reader.endArray();
                        handled = true;
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            }
        }
        return result;
    }

    private AircraftLite readAircraft(JsonReader reader) throws IOException {
        int id = 0; String model = null, type = null, registration = null;
        reader.beginObject();
        while (reader.hasNext()) {
            String field = reader.nextName();
            switch (field) {
                case "id", "aircraftId" -> id = nextInt(reader);
                case "model", "name", "aircraftModel" -> model = nextString(reader);
                case "type", "aircraftType", "category" -> type = nextString(reader);
                case "registration", "tail", "tailNumber", "registrationNumber" -> registration = nextString(reader);
                default -> reader.skipValue();
            }
        }
        reader.endObject();
        return new AircraftLite(id, model, type, registration);
    }

    /** Collect airports from either a single aircraft object or an array of aircraft; optionally dedup. */
    private List<AirportLite> collectAirportsFromJson(String json, boolean deduplicate) throws IOException {
        if (isBlank(json)) return List.of();
        LinkedHashMap<Integer, AirportLite> seen = new LinkedHashMap<>();
        List<AirportLite> result = deduplicate ? new ArrayList<>() : null;

        try (JsonReader reader = new JsonReader(new StringReader(json))) {
            reader.setLenient(true);
            if (reader.peek() == JsonToken.BEGIN_ARRAY) {
                reader.beginArray();
                while (reader.hasNext()) {
                    if (reader.peek() == JsonToken.BEGIN_OBJECT) collectAirportsFromOneObject(reader, seen, result, deduplicate);
                    else reader.skipValue();
                }
                reader.endArray();
            } else if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                collectAirportsFromOneObject(reader, seen, result, deduplicate);
            }
        }

        return deduplicate ? new ArrayList<>(seen.values())
                : new ArrayList<>(seen.values()); // same return, kept for clarity
    }

    private void collectAirportsFromOneObject(JsonReader reader,
                                              LinkedHashMap<Integer, AirportLite> seen,
                                              List<AirportLite> result,
                                              boolean deduplicate) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String field = reader.nextName();
            if ("airports".equals(field) && reader.peek() == JsonToken.BEGIN_ARRAY) {
                reader.beginArray();
                while (reader.hasNext()) {
                    if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                        AirportLite ap = readAirport(reader);
                        if (ap.id != 0 && !seen.containsKey(ap.id)) {
                            seen.put(ap.id, ap);
                            if (!deduplicate && result != null) result.add(ap);
                        }
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }

    private AirportLite readAirport(JsonReader reader) throws IOException {
        int id = 0; String name = null, code = null;
        reader.beginObject();
        while (reader.hasNext()) {
            String field = reader.nextName();
            switch (field) {
                case "id" -> id = nextInt(reader);
                case "name" -> name = nextString(reader);
                case "code", "iataCode", "icao", "airportCode" -> code = nextString(reader);
                case "city" -> reader.skipValue(); // already nulled by sanitizer, but skip defensively
                default -> reader.skipValue();
            }
        }
        reader.endObject();
        return new AirportLite(id, name, code);
    }

    private int nextInt(JsonReader reader) throws IOException {
        JsonToken token = reader.peek();
        if (token == JsonToken.NUMBER) return (int) reader.nextLong();
        if (token == JsonToken.STRING) {
            try { return Integer.parseInt(reader.nextString()); }
            catch (NumberFormatException ignore) { return 0; }
        }
        reader.skipValue(); return 0;
    }

    private String nextString(JsonReader reader) throws IOException {
        JsonToken token = reader.peek();
        if (token == JsonToken.NULL) { reader.nextNull(); return null; }
        if (token == JsonToken.STRING) return reader.nextString();
        if (token == JsonToken.NUMBER) return String.valueOf(reader.nextLong());
        reader.skipValue(); return null;
    }

    // ===================== Minimal, reusable sanitizer =====================
    /**
     * Replace every `"key": <value>` with `"key":<replacementLiteral>` in a JSON string.
     * Works outside of quoted strings and skips exactly one JSON value (object/array/string/primitive).
     */
    private String sanitizeJson(String json, String key, String replacementLiteral) {
        if (json == null || json.isBlank()) return json;

        String quotedKey = "\"" + key + "\"";
        StringBuilder out = new StringBuilder(json.length());
        int index = 0, length = json.length();
        boolean insideString = false, escapeNext = false;

        while (index < length) {
            if (!insideString && index + quotedKey.length() <= length && json.regionMatches(index, quotedKey, 0, quotedKey.length())) {
                out.append(quotedKey).append(':').append(replacementLiteral);
                index += quotedKey.length();

                // Skip whitespace and colon in original
                while (index < length && Character.isWhitespace(json.charAt(index))) index++;
                if (index < length && json.charAt(index) == ':') index++;
                while (index < length && Character.isWhitespace(json.charAt(index))) index++;

                index = skipOneJsonValue(json, index, length);
                continue;
            }

            char currentChar = json.charAt(index++);
            out.append(currentChar);
            if (insideString) {
                if (escapeNext) escapeNext = false;
                else if (currentChar == '\\') escapeNext = true;
                else if (currentChar == '"') insideString = false;
            } else if (currentChar == '"') {
                insideString = true;
            }
        }
        return out.toString();
    }

    /** Advance from {@code start} past exactly one JSON value (object/array/string/primitive). */
    private int skipOneJsonValue(String json, int start, int length) {
        if (start >= length) return start;
        char first = json.charAt(start);

        if (first == '{' || first == '[') { // object/array
            char open = first, close = (first == '{') ? '}' : ']';
            int depth = 1, i = start + 1;
            boolean inStr = false, esc = false;
            while (i < length && depth > 0) {
                char ch = json.charAt(i++);
                if (inStr) { if (esc) esc = false; else if (ch == '\\') esc = true; else if (ch == '"') inStr = false; }
                else { if (ch == '"') inStr = true; else if (ch == open) depth++; else if (ch == close) depth--; }
            }
            return i;
        }
        if (first == '"') { // string
            int i = start + 1; boolean esc = false;
            while (i < length) {
                char ch = json.charAt(i++);
                if (esc) { esc = false; continue; }
                if (ch == '\\') { esc = true; continue; }
                if (ch == '"') break;
            }
            return i;
        }
        int i = start; // primitive: read until delimiter
        while (i < length && ",}]".indexOf(json.charAt(i)) == -1) i++;
        return i;
    }
}
