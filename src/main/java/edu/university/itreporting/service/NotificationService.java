package edu.university.itreporting.service;

import edu.university.itreporting.model.User;

import java.util.List;

public interface NotificationService {
    void notifyUser(User user, String message);

    List<String> getNotificationsForUser(int userId);
}
