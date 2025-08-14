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
    @Test
    void airportsForAircraft_formats_whenArrayJson() throws Exception {
        ApiClient api = mock(ApiClient.class);
        when(api.getAirportsForAircraft(7)).thenReturn(
                "[{\"id\":1,\"name\":\"St. John’s Intl\",\"code\":\"YYT\"}]"
        );
        QueryService svc = new QueryService(api);
        assertEquals("1) St. John’s Intl (YYT) [id: 1]", svc.airportsForAircraft(7));
    }

    @Test
    void airportsForPassenger_formats_whenArrayJson() throws Exception {
        ApiClient api = mock(ApiClient.class);
        when(api.getAirportsUsedByPassenger(3)).thenReturn(
                "[{\"id\":10,\"name\":\"Pearson\",\"code\":\"YYZ\"}]"
        );
        QueryService svc = new QueryService(api);
        assertEquals("1) Pearson (YYZ) [id: 10]", svc.airportsForPassenger(3));
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
    void airportsForAircraft_formatsList_fromArrayJson() throws Exception {
        String json = """
      [
        {"id":11,"name":"St. John's International","code":"YYT"},
        {"id":12,"name":"Toronto Pearson","code":"YYZ"}
      ]
    """;

        com.cody.sprintcli.ApiClient mockClient = org.mockito.Mockito.mock(com.cody.sprintcli.ApiClient.class);
        org.mockito.Mockito.when(mockClient.getAirportsForAircraft(123)).thenReturn(json);

        QueryService service = new QueryService(mockClient);
        String out = service.airportsForAircraft(123);

        org.junit.jupiter.api.Assertions.assertTrue(out.contains("1) St. John's International (YYT) [id: 11]"));
        org.junit.jupiter.api.Assertions.assertTrue(out.contains("2) Toronto Pearson (YYZ) [id: 12]"));
    }

    @org.junit.jupiter.api.Test
    void airportsForAircraft_handlesNotFound() throws Exception {
        com.cody.sprintcli.ApiClient mockClient = org.mockito.Mockito.mock(com.cody.sprintcli.ApiClient.class);
        org.mockito.Mockito.when(mockClient.getAirportsForAircraft(999))
                .thenThrow(new java.io.IOException("HTTP 404 GET http://localhost:8080/..."));

        QueryService service = new QueryService(mockClient);
        String out = service.airportsForAircraft(999);

        org.junit.jupiter.api.Assertions.assertEquals("Aircraft not found (id 999).", out);
    }

    @org.junit.jupiter.api.Test
    void airportsForAircraft_handlesEmptyArray() throws Exception {
        com.cody.sprintcli.ApiClient mockClient = org.mockito.Mockito.mock(com.cody.sprintcli.ApiClient.class);
        org.mockito.Mockito.when(mockClient.getAirportsForAircraft(2)).thenReturn("[]");

        QueryService service = new QueryService(mockClient);
        String out = service.airportsForAircraft(2);

        org.junit.jupiter.api.Assertions.assertEquals("No airports found for aircraft ID 2.", out);
    }
    @org.junit.jupiter.api.Test
    void airportsForPassenger_formatsList_fromArrayJson() throws Exception {
        String json = """
      [
        {"id":21,"name":"Halifax Stanfield","code":"YHZ"},
        {"id":22,"name":"Ottawa Macdonald–Cartier","code":"YOW"}
      ]
    """;

        com.cody.sprintcli.ApiClient mockClient = org.mockito.Mockito.mock(com.cody.sprintcli.ApiClient.class);
        org.mockito.Mockito.when(mockClient.getAirportsUsedByPassenger(5)).thenReturn(json);

        QueryService service = new QueryService(mockClient);
        String out = service.airportsForPassenger(5);

        org.junit.jupiter.api.Assertions.assertTrue(out.contains("1) Halifax Stanfield (YHZ) [id: 21]"));
        org.junit.jupiter.api.Assertions.assertTrue(out.contains("2) Ottawa Macdonald–Cartier (YOW) [id: 22]"));
    }

    @org.junit.jupiter.api.Test
    void airportsForPassenger_handlesNotFound() throws Exception {
        com.cody.sprintcli.ApiClient mockClient = org.mockito.Mockito.mock(com.cody.sprintcli.ApiClient.class);
        org.mockito.Mockito.when(mockClient.getAirportsUsedByPassenger(999))
                .thenThrow(new java.io.IOException("HTTP 404 GET http://localhost:8080/..."));

        QueryService service = new QueryService(mockClient);
        String out = service.airportsForPassenger(999);

        org.junit.jupiter.api.Assertions.assertEquals("Passenger not found (id 999).", out);
    }

    @org.junit.jupiter.api.Test
    void airportsForPassenger_handlesEmptyArray() throws Exception {
        com.cody.sprintcli.ApiClient mockClient = org.mockito.Mockito.mock(com.cody.sprintcli.ApiClient.class);
        org.mockito.Mockito.when(mockClient.getAirportsUsedByPassenger(7)).thenReturn("[]");

        QueryService service = new QueryService(mockClient);
        String out = service.airportsForPassenger(7);

        org.junit.jupiter.api.Assertions.assertEquals("No airports found for passenger ID 7.", out);
    }
}
