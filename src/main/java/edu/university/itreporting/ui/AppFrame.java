package edu.university.itreporting.ui;

import edu.university.itreporting.ui.panels.AdminDashboardPanel;
import edu.university.itreporting.model.User;
import edu.university.itreporting.ui.panels.ITStaffDashboardPanel;
import edu.university.itreporting.ui.panels.LoginPanel;
import edu.university.itreporting.ui.panels.UserDashboardPanel;
import edu.university.itreporting.util.ValidationUtils;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Dimension;

public class AppFrame extends JFrame {
    private static final String CARD_LOGIN = "login";
    private static final String CARD_USER = "user";
    private static final String CARD_IT = "it";
    private static final String CARD_ADMIN = "admin";

    private final AppShellController shellController;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardHost = new JPanel(cardLayout);

    private final LoginPanel loginPanel;
    private final UserDashboardPanel userDashboard;
    private final ITStaffDashboardPanel itDashboard;
    private final AdminDashboardPanel adminDashboard;

    public AppFrame(AppShellController shellController) {
        this.shellController = shellController;
        setTitle("IT Components Problem Reporting System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);

        loginPanel = new LoginPanel(this::handleLogin);
        userDashboard = new UserDashboardPanel(shellController, this::handleLogout);
        itDashboard = new ITStaffDashboardPanel(shellController, this::handleLogout);
        adminDashboard = new AdminDashboardPanel(shellController, this::handleLogout);

        cardHost.add(loginPanel, CARD_LOGIN);
        cardHost.add(userDashboard, CARD_USER);
        cardHost.add(itDashboard, CARD_IT);
        cardHost.add(adminDashboard, CARD_ADMIN);
        add(cardHost);

        showLogin();
    }

    private void handleLogin(String email, String password) {
        String normalizedEmail = email == null ? "" : email.trim();
        String normalizedPassword = password == null ? "" : password.trim();

        if (ValidationUtils.isBlank(normalizedEmail) || ValidationUtils.isBlank(normalizedPassword)) {
            loginPanel.setMessage("Email and password are required.", true);
            return;
        }
        if (!ValidationUtils.isValidEmail(normalizedEmail)) {
            loginPanel.setMessage("Enter a valid email address.", true);
            return;
        }
        try {
            shellController.authenticate(normalizedEmail, normalizedPassword).ifPresentOrElse(this::showDashboardForUser, () ->
                    loginPanel.setMessage("Invalid email or password. Please try again.", true)
            );
        } catch (Exception ex) {
            loginPanel.setMessage("Login failed: " + ex.getMessage(), true);
        }
    }

    private void showDashboardForUser(User user) {
        loginPanel.setMessage("Login successful.", false);
        switch (shellController.resolveDashboard(user)) {
            case USER -> {
                userDashboard.setActiveUser(user);
                cardLayout.show(cardHost, CARD_USER);
            }
            case IT_STAFF -> {
                itDashboard.setActiveUser(user);
                cardLayout.show(cardHost, CARD_IT);
            }
            case ADMIN -> {
                adminDashboard.setActiveUser(user);
                cardLayout.show(cardHost, CARD_ADMIN);
            }
        }
    }

    private void handleLogout() {
        showLogin();
    }

    private void showLogin() {
        loginPanel.setMessage("Enter your credentials to continue", false);
        cardLayout.show(cardHost, CARD_LOGIN);
    }
}
