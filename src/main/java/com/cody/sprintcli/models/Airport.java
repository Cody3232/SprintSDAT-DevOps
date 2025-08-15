package com.cody.sprintcli.models;

public class Airport {
    private int id;
    private String name;
    private String code;
    private City city; // keep only a shallow city to avoid recursion

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public City getCity() { return city; }

    @Override
    public String toString() {
        return name + " [" + code + "]" + (city != null ? " - " + city.getName() : "");
    }
}
