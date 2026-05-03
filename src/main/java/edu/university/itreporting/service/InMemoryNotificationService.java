package edu.university.itreporting.service;

import edu.university.itreporting.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryNotificationService implements NotificationService {
    private final Map<Integer, List<String>> messagesByUserId = new LinkedHashMap<>();

    @Override
    public void notifyUser(User user, String message) {
        if (user == null || message == null || message.trim().isEmpty()) {
            return;
        }
        messagesByUserId
                .computeIfAbsent(user.getUserId(), id -> new ArrayList<>())
                .add(message.trim());
    }

    @Override
    public List<String> getNotificationsForUser(int userId) {
        return Collections.unmodifiableList(messagesByUserId.getOrDefault(userId, List.of()));
    }
}
