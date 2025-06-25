package com.cody.sprintcli.models;

public class Aircraft {
    private int id;
    private String type;
    private String airlineName;
    private int numberOfPassengers;

    // Constructor
    public Aircraft(int id, String type, String airlineName, int numberOfPassengers) {
        this.id = id;
        this.type = type;
        this.airlineName = airlineName;
        this.numberOfPassengers = numberOfPassengers;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    // toString method for clean output
    @Override
    public String toString() {
        return type + " (" + airlineName + ") - Capacity: " + numberOfPassengers;
    }
}
