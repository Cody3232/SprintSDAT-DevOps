package com.cody.sprintcli.models;

public class Aircraft {
    private int id;
    private String model;
    private String type;
    private String registration;

    // Needed by Gson
    public Aircraft() {}

    public Aircraft(int id, String model, String type, String registration) {
        this.id = id;
        this.model = model;
        this.type = type;
        this.registration = registration;
    }

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getType() {
        return type;
    }

    public String getRegistration() {
        return registration;
    }

    @Override
    public String toString() {
        String label = (model != null && !model.isBlank()) ? model
                : (type != null && !type.isBlank()) ? type
                : (registration != null && !registration.isBlank()) ? registration
                : "(unnamed aircraft)";
        return label + " [id: " + id + (registration != null && !registration.isBlank() ? ", tail: " + registration : "") + "]";
    }
}
