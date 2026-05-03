package edu.university.itreporting.util;

import edu.university.itreporting.repository.ComplaintRepository;
import edu.university.itreporting.repository.ReportRepository;
import edu.university.itreporting.repository.UserRepository;

public record SeedDataBundle(
        UserRepository userRepository,
        ComplaintRepository complaintRepository,
        ReportRepository reportRepository
) {
}
