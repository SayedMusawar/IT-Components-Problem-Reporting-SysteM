package edu.university.itreporting.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Complaint {
    private final int memoId;
    private final String description;
    private Status status;
    private Priority priority;
    private final String location;
    private final String equipmentType;
    private final String equipmentId;
    private final LocalDate dateSubmitted;
    private final User owner;
    private ITStaff assignedStaff;
    private final List<ResolutionNote> notes = new ArrayList<>();
    private final List<Report> reports = new ArrayList<>();

    public Complaint(
            int memoId,
            String description,
            String location,
            String equipmentType,
            String equipmentId,
            LocalDate dateSubmitted,
            User owner
    ) {
        this.memoId = memoId;
        this.description = User.requireText(description, "description");
        this.location = User.requireText(location, "location");
        this.equipmentType = User.requireText(equipmentType, "equipment type");
        this.equipmentId = User.requireText(equipmentId, "equipment ID");
        this.dateSubmitted = Objects.requireNonNull(dateSubmitted, "date submitted is required.");
        this.owner = Objects.requireNonNull(owner, "owner is required.");
        this.status = Status.PENDING;
        this.priority = Priority.LOW;
    }

    public int getMemoId() {
        return memoId;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void updateStatus(Status status) {
        this.status = Objects.requireNonNull(status, "status is required.");
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = Objects.requireNonNull(priority, "priority is required.");
    }

    public String getLocation() {
        return location;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public LocalDate getDateSubmitted() {
        return dateSubmitted;
    }

    public User getOwner() {
        return owner;
    }

    public ITStaff getAssignedStaff() {
        return assignedStaff;
    }

    public void assignStaff(ITStaff assignedStaff) {
        this.assignedStaff = assignedStaff;
        if (assignedStaff != null) {
            assignedStaff.recordHandledComplaint(this);
        }
    }

    public void addNote(ResolutionNote note) {
        if (note != null) {
            notes.add(note);
        }
    }

    public List<ResolutionNote> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    public void linkReport(Report report) {
        if (report != null && !reports.contains(report)) {
            reports.add(report);
        }
    }

    public List<Report> getReports() {
        return Collections.unmodifiableList(reports);
    }

    @Override
    public String toString() {
        return "Memo #" + memoId + " [" + status + ", " + priority + "] " + equipmentType
                + " " + equipmentId + " at " + location;
    }
}
