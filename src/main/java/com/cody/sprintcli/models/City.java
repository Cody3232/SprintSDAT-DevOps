package com.cody.sprintcli.models;

public class City {
    private int id;
    private String name;
    private String state;
    private int population;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getState() { return state; }
    public int getPopulation() { return population; }

    @Override
    public String toString() {
        return name + ", " + state + " (Pop: " + population + ")";
    }
}
