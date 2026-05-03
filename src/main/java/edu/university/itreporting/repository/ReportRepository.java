package edu.university.itreporting.repository;

import edu.university.itreporting.model.Report;

import java.util.List;
import java.util.Optional;

public interface ReportRepository {
    Report save(Report report);

    Optional<Report> findById(int reportId);

    List<Report> findAll();

    boolean deleteById(int reportId);
}
