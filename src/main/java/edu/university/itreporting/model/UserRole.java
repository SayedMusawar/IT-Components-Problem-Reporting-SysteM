package edu.university.itreporting.model;

public enum UserRole {
    STUDENT("Student"),
    FACULTY("Faculty"),
    IT_STAFF("IT Staff"),
    ADMIN("System Admin");

    private final String displayName;

    UserRole(String displayName) {
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
