package com.cody.sprintcli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {

    public static String getDataFromEndpoint(String endpointUrl) {
        StringBuilder response = new StringBuilder();

        try {
            // Create a connection
            URL url = new URL(endpointUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            in.close();
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Error calling API: " + e.getMessage());
        }

        return response.toString();
    }
}
