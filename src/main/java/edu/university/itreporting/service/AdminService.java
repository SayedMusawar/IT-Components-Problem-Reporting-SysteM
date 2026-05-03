package edu.university.itreporting.service;

import edu.university.itreporting.model.Admin;
import edu.university.itreporting.model.Complaint;
import edu.university.itreporting.model.ITStaff;
import edu.university.itreporting.model.Report;
import edu.university.itreporting.model.User;
import edu.university.itreporting.model.UserRole;
import edu.university.itreporting.repository.ComplaintRepository;
import edu.university.itreporting.repository.ReportRepository;
import edu.university.itreporting.repository.UserRepository;
import edu.university.itreporting.util.IdSequence;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class AdminService {
    private final UserRepository userRepository;
    private final ComplaintRepository complaintRepository;
    private final ReportRepository reportRepository;
    private final IdSequence userIdSequence;
    private final IdSequence reportIdSequence;

    public AdminService(
            UserRepository userRepository,
            ComplaintRepository complaintRepository,
            ReportRepository reportRepository
    ) {
        this.userRepository = userRepository;
        this.complaintRepository = complaintRepository;
        this.reportRepository = reportRepository;
        this.userIdSequence = new IdSequence(nextUserIdSeed(userRepository.findAll()));
        this.reportIdSequence = new IdSequence(nextReportIdSeed(reportRepository.findAll()));
    }

    public Report generateReport(Admin admin) {
        requireAdmin(admin);
        List<Complaint> complaints = complaintRepository.findAll();
        if (complaints.isEmpty()) {
            throw new IllegalStateException("No complaints available for report generation.");
        }
        Report report = new Report(reportIdSequence.next(), LocalDate.now(), complaints);
        reportRepository.save(report);
        admin.addReport(report);
        return report;
    }

    public List<Report> viewReports(Admin admin) {
        requireAdmin(admin);
        return reportRepository.findAll();
    }

    public ITStaff createITStaff(Admin admin, int staffId, String name, String email, String password) {
        requireAdmin(admin);
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        if (findITStaffByStaffId(staffId) != null) {
            throw new IllegalArgumentException("Staff ID is already in use.");
        }
        ITStaff staff = new ITStaff(userIdSequence.next(), staffId, name, email, password);
        userRepository.save(staff);
        return staff;
    }

    public ITStaff updateITStaff(Admin admin, int staffId, String name, String email, String password) {
        requireAdmin(admin);
        ITStaff existing = findITStaffRequired(staffId);
        if (email != null && !email.trim().equalsIgnoreCase(existing.getEmail()) && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        if (name != null && !name.trim().isEmpty()) {
            existing.setName(name);
        }
        if (email != null && !email.trim().isEmpty()) {
            existing.setEmail(email);
        }
        if (password != null && !password.trim().isEmpty()) {
            existing.changePassword(password);
        }
        userRepository.save(existing);
        return existing;
    }

    public boolean removeITStaff(Admin admin, int staffId) {
        requireAdmin(admin);
        ITStaff existing = findITStaffRequired(staffId);
        return userRepository.deleteById(existing.getUserId());
    }

    public List<ITStaff> listITStaff(Admin admin) {
        requireAdmin(admin);
        return userRepository.findByRole(UserRole.IT_STAFF).stream()
                .filter(ITStaff.class::isInstance)
                .map(ITStaff.class::cast)
                .sorted(Comparator.comparingInt(ITStaff::getStaffId))
                .toList();
    }

    private ITStaff findITStaffRequired(int staffId) {
        ITStaff staff = findITStaffByStaffId(staffId);
        if (staff == null) {
            throw new NoSuchElementException("IT Staff with ID " + staffId + " not found.");
        }
        return staff;
    }

    private ITStaff findITStaffByStaffId(int staffId) {
        return userRepository.findByRole(UserRole.IT_STAFF).stream()
                .filter(ITStaff.class::isInstance)
                .map(ITStaff.class::cast)
                .filter(staff -> staff.getStaffId() == staffId)
                .findFirst()
                .orElse(null);
    }

    private static int nextUserIdSeed(List<User> users) {
        return users.stream()
                .mapToInt(User::getUserId)
                .max()
                .orElse(0) + 1;
    }

    private static int nextReportIdSeed(List<Report> reports) {
        return reports.stream()
                .mapToInt(Report::getReportId)
                .max()
                .orElse(5000) + 1;
    }

    private static void requireAdmin(Admin admin) {
        if (admin == null || admin.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Only admin can perform this action.");
        }
    }
}
