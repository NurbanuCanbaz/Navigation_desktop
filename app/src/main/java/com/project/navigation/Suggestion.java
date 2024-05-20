package com.project.navigation;

public class Suggestion {
    private String name;
    private String plateNumber;
    private String feedback;

    public Suggestion() {
        // Default constructor required for calls to DataSnapshot.getValue(Suggestion.class)
    }

    public Suggestion(String name, String plateNumber, String feedback) {
        this.name = name;
        this.plateNumber = plateNumber;
        this.feedback = feedback;
    }

    public String getName() {
        return name;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getFeedback() {
        return feedback;
    }
}