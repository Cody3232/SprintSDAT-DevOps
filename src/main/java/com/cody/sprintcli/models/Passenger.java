package com.cody.sprintcli.models;

public class Passenger {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + phoneNumber + ")";
    }
}
