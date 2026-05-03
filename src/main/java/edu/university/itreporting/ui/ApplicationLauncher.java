package edu.university.itreporting.ui;

import edu.university.itreporting.service.AdminService;
import edu.university.itreporting.service.AuthenticationService;
import edu.university.itreporting.service.ComplaintService;
import edu.university.itreporting.service.InMemoryNotificationService;
import edu.university.itreporting.service.NotificationService;
import edu.university.itreporting.util.SeedDataBundle;
import edu.university.itreporting.util.SeedDataInitializer;

import javax.swing.SwingUtilities;

public final class ApplicationLauncher {
    private ApplicationLauncher() {
    }

    public static void main(String[] args) {
        AppTheme.install();
        SeedDataBundle seedData = SeedDataInitializer.initializeDefaultData();
        AuthenticationService authenticationService = new AuthenticationService(seedData.userRepository());
        NotificationService notificationService = new InMemoryNotificationService();
        ComplaintService complaintService = new ComplaintService(
                seedData.complaintRepository(),
                seedData.userRepository(),
                notificationService
        );
        AdminService adminService = new AdminService(
                seedData.userRepository(),
                seedData.complaintRepository(),
                seedData.reportRepository()
        );
        AppShellController shellController = new AppShellController(
                authenticationService,
                complaintService,
                notificationService,
                adminService
        );

        SwingUtilities.invokeLater(() -> {
            AppFrame frame = new AppFrame(shellController);
            frame.setVisible(true);
        });
    }
}
