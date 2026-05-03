package edu.university.itreporting.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Report {
    private final int reportId;
    private final LocalDate generatedDate;
    private final List<Complaint> complaints;

    public Report(int reportId, LocalDate generatedDate, List<Complaint> complaints) {
        this.reportId = reportId;
        this.generatedDate = Objects.requireNonNull(generatedDate, "generated date is required.");
        this.complaints = new ArrayList<>(Objects.requireNonNull(complaints, "complaints are required."));
        this.complaints.forEach(complaint -> complaint.linkReport(this));
    }

    public int getReportId() {
        return reportId;
    }

    public LocalDate getGeneratedDate() {
        return generatedDate;
    }

    public List<Complaint> getComplaints() {
        return Collections.unmodifiableList(complaints);
    }

    public int getTotalComplaints() {
        return complaints.size();
    }

    public long countByStatus(Status status) {
        return complaints.stream()
                .filter(complaint -> complaint.getStatus() == status)
                .count();
    }

    public long countByPriority(Priority priority) {
        return complaints.stream()
                .filter(complaint -> complaint.getPriority() == priority)
                .count();
    }

    public String generateSummary() {
        return "Report #" + reportId
                + " | Generated: " + generatedDate
                + " | Total: " + getTotalComplaints()
                + " | Pending: " + countByStatus(Status.PENDING)
                + " | In Progress: " + countByStatus(Status.IN_PROGRESS)
                + " | Resolved: " + countByStatus(Status.RESOLVED);
    }
}
