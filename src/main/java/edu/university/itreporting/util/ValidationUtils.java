package edu.university.itreporting.util;

import java.util.regex.Pattern;

public final class ValidationUtils {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private ValidationUtils() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean hasMinLength(String value, int minLength) {
        return value != null && value.trim().length() >= minLength;
    }

    public static boolean isValidEmail(String email) {
        if (isBlank(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
}
