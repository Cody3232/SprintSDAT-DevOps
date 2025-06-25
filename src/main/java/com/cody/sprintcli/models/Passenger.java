package com.cody.sprintcli.models;

public class Passenger {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    // Constructor
    public Passenger(int id, String firstName, String lastName, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // For printing
    @Override
    public String toString() {
        return firstName + " " + lastName + " (Phone: " + phoneNumber + ")";
    }
}
