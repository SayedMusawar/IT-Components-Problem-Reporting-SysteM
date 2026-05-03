package edu.university.itreporting.repository;

import edu.university.itreporting.model.Report;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryReportRepository implements ReportRepository {
    private final Map<Integer, Report> reportsById = new LinkedHashMap<>();

    @Override
    public Report save(Report report) {
        if (report == null) {
            throw new IllegalArgumentException("report is required.");
        }
        reportsById.put(report.getReportId(), report);
        return report;
    }

    @Override
    public Optional<Report> findById(int reportId) {
        return Optional.ofNullable(reportsById.get(reportId));
    }

    @Override
    public List<Report> findAll() {
        return new ArrayList<>(reportsById.values());
    }

    @Override
    public boolean deleteById(int reportId) {
        return reportsById.remove(reportId) != null;
    }
}
