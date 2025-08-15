package com.cody.sprintcli.models;

import java.util.List;

public class Aircraft {
    private int id;
    private String type;
    private String airlineName;
    private int numberOfPassengers;
    private List<Airport> airports; // used for “airports for aircraft”

    public int getId() { return id; }
    public String getType() { return type; }
    public String getAirlineName() { return airlineName; }
    public int getNumberOfPassengers() { return numberOfPassengers; }
    public List<Airport> getAirports() { return airports; }

    @Override
    public String toString() {
        return "#" + id + " " + type + " (" + airlineName + ")";
    }
}
