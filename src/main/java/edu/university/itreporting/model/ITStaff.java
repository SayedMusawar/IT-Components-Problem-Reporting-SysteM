package edu.university.itreporting.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ITStaff extends User {
    private final int staffId;
    private final List<Complaint> handledComplaints = new ArrayList<>();

    public ITStaff(int userId, int staffId, String name, String email, String password) {
        super(userId, name, email, password);
        this.staffId = staffId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void recordHandledComplaint(Complaint complaint) {
        if (complaint != null && !handledComplaints.contains(complaint)) {
            handledComplaints.add(complaint);
        }
    }

    public List<Complaint> getHandledComplaints() {
        return Collections.unmodifiableList(handledComplaints);
    }

    @Override
    public UserRole getRole() {
        return UserRole.IT_STAFF;
    }
}
