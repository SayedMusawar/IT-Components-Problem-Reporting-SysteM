package edu.university.itreporting.model;

import java.util.Objects;

public abstract class User {
    private final int userId;
    private String name;
    private String email;
    private String password;

    protected User(int userId, String name, String email, String password) {
        this.userId = userId;
        this.name = requireText(name, "name");
        this.email = requireText(email, "email");
        this.password = requireText(password, "password");
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = requireText(name, "name");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = requireText(email, "email");
    }

    public boolean checkPassword(String candidatePassword) {
        return password.equals(candidatePassword);
    }

    public void changePassword(String newPassword) {
        this.password = requireText(newPassword, "password");
    }

    public abstract UserRole getRole();

    protected static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return value.trim();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof User user)) {
            return false;
        }
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return getRole() + " #" + userId + " - " + name;
    }
}
