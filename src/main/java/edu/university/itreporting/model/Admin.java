package edu.university.itreporting.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Admin extends User {
    private final int adminId;
    private final List<Report> reports = new ArrayList<>();

    public Admin(int userId, int adminId, String name, String email, String password) {
        super(userId, name, email, password);
        this.adminId = adminId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void addReport(Report report) {
        if (report != null && !reports.contains(report)) {
            reports.add(report);
        }
    }

    public List<Report> getReports() {
        return Collections.unmodifiableList(reports);
    }

    @Override
    public UserRole getRole() {
        return UserRole.ADMIN;
    }
}
