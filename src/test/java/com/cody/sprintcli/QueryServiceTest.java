package com.cody.sprintcli;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class QueryServiceTest {

    @Test
    void airportsInCity_formatsList_fromArrayJson() throws Exception {
        // sample JSON
        String json = """
          [
            {"id":1,"name":"St. John's International Airport","code":"YYT"},
            {"id":2,"name":"Gander International Airport","code":"YQX"}
          ]
        """;

        ApiClient mockClient = mock(ApiClient.class);
        when(mockClient.getAirportsByCity(1)).thenReturn(json);

        QueryService service = new QueryService(mockClient);
        String out = service.airportsInCity(1);

        assertTrue(out.contains("1) St. John's International Airport (YYT) [id: 1]"));
        assertTrue(out.contains("2) Gander International Airport (YQX) [id: 2]"));
    }

    @Test
    void airportsInCity_handlesNotFound() throws Exception {
        ApiClient mockClient = mock(ApiClient.class);
        when(mockClient.getAirportsByCity(999))
                .thenThrow(new java.io.IOException("HTTP 404 GET http://localhost:8080/..."));

        QueryService service = new QueryService(mockClient);
        String out = service.airportsInCity(999);

        assertEquals("City not found (id 999).", out);
    }

    @Test
    void airportsInCity_handlesEmptyArray() throws Exception {
        ApiClient mockClient = mock(ApiClient.class);
        when(mockClient.getAirportsByCity(2)).thenReturn("[]");

        QueryService service = new QueryService(mockClient);
        String out = service.airportsInCity(2);

        assertEquals("No airports found for city ID 2.", out);
    }

    @org.junit.jupiter.api.Test
    void aircraftForPassenger_formatsList_fromArrayJson() throws Exception {
        String json = """
      [
        {"id":101,"model":"Boeing 737-800","registration":"C-ABCD"},
        {"id":102,"model":"Airbus A320","registration":"C-EFGH"}
      ]
    """;

        com.cody.sprintcli.ApiClient mockClient = org.mockito.Mockito.mock(com.cody.sprintcli.ApiClient.class);
        org.mockito.Mockito.when(mockClient.getAircraftByPassenger(1)).thenReturn(json);

        QueryService service = new QueryService(mockClient);
        String out = service.aircraftForPassenger(1);

        org.junit.jupiter.api.Assertions.assertTrue(out.contains("1) Boeing 737-800 [id: 101] tail: C-ABCD"));
        org.junit.jupiter.api.Assertions.assertTrue(out.contains("2) Airbus A320 [id: 102] tail: C-EFGH"));
    }

    @org.junit.jupiter.api.Test
    void aircraftForPassenger_handlesNotFound() throws Exception {
        com.cody.sprintcli.ApiClient mockClient = org.mockito.Mockito.mock(com.cody.sprintcli.ApiClient.class);
        org.mockito.Mockito.when(mockClient.getAircraftByPassenger(999))
                .thenThrow(new java.io.IOException("HTTP 404 GET http://localhost:8080/..."));

        QueryService service = new QueryService(mockClient);
        String out = service.aircraftForPassenger(999);

        org.junit.jupiter.api.Assertions.assertEquals("Passenger not found (id 999).", out);
    }

    @org.junit.jupiter.api.Test
    void aircraftForPassenger_handlesEmptyArray() throws Exception {
        com.cody.sprintcli.ApiClient mockClient = org.mockito.Mockito.mock(com.cody.sprintcli.ApiClient.class);
        org.mockito.Mockito.when(mockClient.getAircraftByPassenger(2)).thenReturn("[]");

        QueryService service = new QueryService(mockClient);
        String out = service.aircraftForPassenger(2);

        org.junit.jupiter.api.Assertions.assertEquals("No aircraft found for passenger ID 2.", out);
    }
    @org.junit.jupiter.api.Test
    void airportsForAircraft_formatsList_fromArrayJson() throws java.io.IOException {
        com.cody.sprintcli.ApiClient client = org.mockito.Mockito.mock(com.cody.sprintcli.ApiClient.class);
        com.cody.sprintcli.QueryService svc = new com.cody.sprintcli.QueryService(client);

        int aircraftId = 123;
        String json = """
      [
        {"id":1,"name":"Heathrow","code":"LHR"},
        {"id":2,"name":"Gatwick","code":"LGW"}
      ]
      """;
        org.mockito.Mockito.when(client.getAirportsForAircraft(aircraftId)).thenReturn(json);

        String out = svc.airportsForAircraft(aircraftId);
        org.junit.jupiter.api.Assertions.assertEquals(
                "1) Heathrow (LHR) [id: 1]\n2) Gatwick (LGW) [id: 2]",
                out
        );
    }

    @org.junit.jupiter.api.Test
    void airportsForAircraft_handlesEmptyArray() throws java.io.IOException {
        com.cody.sprintcli.ApiClient client = org.mockito.Mockito.mock(com.cody.sprintcli.ApiClient.class);
        com.cody.sprintcli.QueryService svc = new com.cody.sprintcli.QueryService(client);

        int aircraftId = 123;
        org.mockito.Mockito.when(client.getAirportsForAircraft(aircraftId)).thenReturn("[]");

        String out = svc.airportsForAircraft(aircraftId);
        org.junit.jupiter.api.Assertions.assertEquals(
                "No airports found for aircraft ID 123.", out
        );
    }

    @org.junit.jupiter.api.Test
    void airportsForAircraft_handlesNotFound() throws java.io.IOException {
        com.cody.sprintcli.ApiClient client = org.mockito.Mockito.mock(com.cody.sprintcli.ApiClient.class);
        com.cody.sprintcli.QueryService svc = new com.cody.sprintcli.QueryService(client);

        int aircraftId = 123;
        org.mockito.Mockito.when(client.getAirportsForAircraft(aircraftId))
                .thenThrow(new java.io.IOException("HTTP 404 Not Found"));

        String out = svc.airportsForAircraft(aircraftId);
        org.junit.jupiter.api.Assertions.assertEquals(
                "Aircraft not found (id 123).", out
        );
    }
    @org.junit.jupiter.api.Test
    void airportsForPassenger_formatsList_fromArrayJson() throws java.io.IOException {
        com.cody.sprintcli.ApiClient client = org.mockito.Mockito.mock(com.cody.sprintcli.ApiClient.class);
        com.cody.sprintcli.QueryService svc = new com.cody.sprintcli.QueryService(client);

        int passengerId = 7;
        String json = """
      [
        {"id":10,"name":"Pearson","code":"YYZ"},
        {"id":20,"name":"St. John's","code":"YYT"}
      ]
      """;
        org.mockito.Mockito.when(client.getAirportsUsedByPassenger(passengerId)).thenReturn(json);

        String out = svc.airportsForPassenger(passengerId);
        org.junit.jupiter.api.Assertions.assertEquals(
                "1) Pearson (YYZ) [id: 10]\n2) St. John's (YYT) [id: 20]",
                out
        );
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("airportsForPassenger returns friendly message on empty list")
    void airportsForPassenger_handlesEmptyArray() throws java.io.IOException {
        com.cody.sprintcli.ApiClient client = org.mockito.Mockito.mock(com.cody.sprintcli.ApiClient.class);
        com.cody.sprintcli.QueryService svc = new com.cody.sprintcli.QueryService(client);

        int passengerId = 7;
        org.mockito.Mockito.when(client.getAirportsUsedByPassenger(passengerId)).thenReturn("[]");

        String out = svc.airportsForPassenger(passengerId);
        org.junit.jupiter.api.Assertions.assertEquals(
                "No airports found for passenger ID 7.", out
        );
    }

    @org.junit.jupiter.api.Test
    void airportsForPassenger_handlesNotFound() throws java.io.IOException {
        com.cody.sprintcli.ApiClient client = org.mockito.Mockito.mock(com.cody.sprintcli.ApiClient.class);
        com.cody.sprintcli.QueryService svc = new com.cody.sprintcli.QueryService(client);

        int passengerId = 7;
        org.mockito.Mockito.when(client.getAirportsUsedByPassenger(passengerId))
                .thenThrow(new java.io.IOException("HTTP 404 Not Found"));

        String out = svc.airportsForPassenger(passengerId);
        org.junit.jupiter.api.Assertions.assertEquals(
                "Passenger not found (id 7).", out
        );
    }
}
