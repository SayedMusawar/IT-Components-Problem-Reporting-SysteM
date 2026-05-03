package edu.university.itreporting.repository;

import edu.university.itreporting.model.User;
import edu.university.itreporting.model.UserRole;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private final Map<Integer, User> usersById = new LinkedHashMap<>();

    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user is required.");
        }
        usersById.put(user.getUserId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(int userId) {
        return Optional.ofNullable(usersById.get(userId));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        String normalized = email.trim().toLowerCase(Locale.ROOT);
        return usersById.values().stream()
                .filter(user -> user.getEmail().trim().toLowerCase(Locale.ROOT).equals(normalized))
                .findFirst();
    }

    @Override
    public List<User> findByRole(UserRole role) {
        List<User> results = new ArrayList<>();
        for (User user : usersById.values()) {
            if (user.getRole() == role) {
                results.add(user);
            }
        }
        return results;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(usersById.values());
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    @Override
    public boolean deleteById(int userId) {
        return usersById.remove(userId) != null;
    }
}
