package edu.university.itreporting.ui;

import edu.university.itreporting.model.Admin;
import edu.university.itreporting.model.User;
import edu.university.itreporting.model.UserRole;
import edu.university.itreporting.model.ITStaff;
import edu.university.itreporting.model.Priority;
import edu.university.itreporting.model.Report;
import edu.university.itreporting.model.ResolutionNote;
import edu.university.itreporting.model.Status;
import edu.university.itreporting.service.AuthenticationService;
import edu.university.itreporting.service.AdminService;
import edu.university.itreporting.service.ComplaintService;
import edu.university.itreporting.service.NotificationService;

import edu.university.itreporting.model.Complaint;

import java.util.List;
import java.util.Optional;

public class AppShellController {
    public enum DashboardTarget {
        USER,
        IT_STAFF,
        ADMIN
    }

    private final AuthenticationService authenticationService;
    private final ComplaintService complaintService;
    private final NotificationService notificationService;
    private final AdminService adminService;

    public AppShellController(AuthenticationService authenticationService) {
        this(authenticationService, null, null, null);
    }

    public AppShellController(
            AuthenticationService authenticationService,
            ComplaintService complaintService,
            NotificationService notificationService
    ) {
        this(authenticationService, complaintService, notificationService, null);
    }

    public AppShellController(
            AuthenticationService authenticationService,
            ComplaintService complaintService,
            NotificationService notificationService,
            AdminService adminService
    ) {
        this.authenticationService = authenticationService;
        this.complaintService = complaintService;
        this.notificationService = notificationService;
        this.adminService = adminService;
    }

    public Optional<User> authenticate(String email, String password) {
        return authenticationService.login(email, password);
    }

    public DashboardTarget resolveDashboard(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user is required.");
        }
        return switch (user.getRole()) {
            case STUDENT, FACULTY -> DashboardTarget.USER;
            case IT_STAFF -> DashboardTarget.IT_STAFF;
            case ADMIN -> DashboardTarget.ADMIN;
        };
    }

    public Complaint submitComplaint(
            User submitter,
            String location,
            String equipmentType,
            String equipmentId,
            String description
    ) {
        requireComplaintService();
        return complaintService.submitComplaint(submitter, location, equipmentType, equipmentId, description);
    }

    public List<Complaint> viewSubmittedComplaints(User submitter) {
        requireComplaintService();
        return complaintService.viewSubmittedComplaints(submitter);
    }

    public Complaint trackComplaintStatus(User submitter, int memoId) {
        requireComplaintService();
        return complaintService.trackComplaintStatus(submitter, memoId);
    }

    public List<String> getUserNotifications(User user) {
        if (notificationService == null || user == null) {
            return List.of();
        }
        return notificationService.getNotificationsForUser(user.getUserId());
    }

    public List<Complaint> viewAllComplaints(ITStaff actor) {
        requireComplaintService();
        return complaintService.viewAllComplaints(actor);
    }

    public List<Complaint> filterComplaints(
            ITStaff actor,
            String location,
            String equipmentType,
            Status status,
            Priority priority
    ) {
        requireComplaintService();
        return complaintService.filterComplaints(actor, location, equipmentType, status, priority);
    }

    public Complaint assignPriority(ITStaff actor, int memoId, Priority priority) {
        requireComplaintService();
        return complaintService.assignPriority(actor, memoId, priority);
    }

    public Complaint updateStatus(ITStaff actor, int memoId, Status status) {
        requireComplaintService();
        return complaintService.updateStatus(actor, memoId, status);
    }

    public ResolutionNote addResolutionNote(ITStaff actor, int memoId, String text) {
        requireComplaintService();
        return complaintService.addResolutionNote(actor, memoId, text);
    }

    public Report generateReport(Admin admin) {
        requireAdminService();
        return adminService.generateReport(admin);
    }

    public List<Report> viewReports(Admin admin) {
        requireAdminService();
        return adminService.viewReports(admin);
    }

    public ITStaff createITStaff(Admin admin, int staffId, String name, String email, String password) {
        requireAdminService();
        return adminService.createITStaff(admin, staffId, name, email, password);
    }

    public ITStaff updateITStaff(Admin admin, int staffId, String name, String email, String password) {
        requireAdminService();
        return adminService.updateITStaff(admin, staffId, name, email, password);
    }

    public boolean removeITStaff(Admin admin, int staffId) {
        requireAdminService();
        return adminService.removeITStaff(admin, staffId);
    }

    public List<ITStaff> listITStaff(Admin admin) {
        requireAdminService();
        return adminService.listITStaff(admin);
    }

    private void requireComplaintService() {
        if (complaintService == null) {
            throw new IllegalStateException("Complaint service is not configured in shell controller.");
        }
    }

    private void requireAdminService() {
        if (adminService == null) {
            throw new IllegalStateException("Admin service is not configured in shell controller.");
        }
    }
}
