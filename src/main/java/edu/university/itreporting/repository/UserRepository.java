package edu.university.itreporting.repository;

import edu.university.itreporting.model.User;
import edu.university.itreporting.model.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(int userId);

    Optional<User> findByEmail(String email);

    List<User> findByRole(UserRole role);

    List<User> findAll();

    boolean existsByEmail(String email);

    boolean deleteById(int userId);
}
