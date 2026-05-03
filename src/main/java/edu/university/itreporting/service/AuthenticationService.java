package edu.university.itreporting.service;

import edu.university.itreporting.model.User;
import edu.university.itreporting.repository.UserRepository;

import java.util.Optional;

public class AuthenticationService {
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> login(String email, String password) {
        if (email == null || password == null) {
            return Optional.empty();
        }
        String normalizedEmail = email.trim();
        String normalizedPassword = password.trim();
        if (normalizedEmail.isEmpty() || normalizedPassword.isEmpty()) {
            return Optional.empty();
        }
        return userRepository.findByEmail(normalizedEmail)
                .filter(user -> user.checkPassword(normalizedPassword));
    }
}
