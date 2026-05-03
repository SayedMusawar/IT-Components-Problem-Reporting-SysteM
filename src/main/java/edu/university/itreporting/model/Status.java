package edu.university.itreporting.model;

public enum Status {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
