package edu.university.itreporting.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Faculty extends User {
    private final List<Complaint> complaints = new ArrayList<>();

    public Faculty(int userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    public void addComplaint(Complaint complaint) {
        if (complaint != null && !complaints.contains(complaint)) {
            complaints.add(complaint);
        }
    }

    public List<Complaint> getComplaints() {
        return Collections.unmodifiableList(complaints);
    }

    @Override
    public UserRole getRole() {
        return UserRole.FACULTY;
    }
}
