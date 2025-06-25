package com.cody.sprintcli.models;

public class City {
    private int id;
    private String name;
    private String state;
    private int population;

    // Constructor
    public City(int id, String name, String state, int population) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.population = population;
    }

    // Getters (returns the value of a private field)
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public int getPopulation() {
        return population;
    }

    @Override
    public String toString() {
        return name + ", " + state + " (Pop: " + population + ")";
    }
}
