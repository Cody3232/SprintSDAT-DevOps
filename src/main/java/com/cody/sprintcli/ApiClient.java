package com.cody.sprintcli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {

    public static String getAirportsByCity(int cityId) {
        try {
            String endpointUrl = "http://localhost:8080/api/cities/" + cityId + "/airports";
            URL url = new URL(endpointUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            return response.toString(); // this is the raw JSON response
        } catch (Exception e) {
            return "Error fetching data: " + e.getMessage();
        }
    }

    public static String getAircraftByPassenger(int passengerId) {
        try {
            String endpointUrl = "http://localhost:8080/api/passengers/" + passengerId + "/aircraft";
            URL url = new URL(endpointUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            return response.toString();

        } catch (Exception e) {
            return "Error fetching data: " + e.getMessage();
        }
    }

    public static String getAirportsUsedByPassenger(int passengerId) {
        try {
            String endpointUrl = "http://localhost:8080/api/passengers/" + passengerId + "/airports";
            URL url = new URL(endpointUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            return response.toString();
        } catch (Exception e) {
            return "Error fetching data: " + e.getMessage();
        }
    }

    public static String getAirportsForAircraft(int aircraftId) {
        try {
            String endpointUrl = "http://localhost:8080/api/aircraft/" + aircraftId + "/airports";
            URL url = new URL(endpointUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            return response.toString();
        } catch (Exception e) {
            return "Error fetching data: " + e.getMessage();
        }
    }
}
