package edu.university.itreporting.ui.panels;

import edu.university.itreporting.model.Admin;
import edu.university.itreporting.model.ITStaff;
import edu.university.itreporting.model.Report;
import edu.university.itreporting.model.User;
import edu.university.itreporting.ui.AppShellController;
import edu.university.itreporting.ui.AppTheme;
import edu.university.itreporting.util.ValidationUtils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

public class AdminDashboardPanel extends JPanel {
    private final AppShellController shellController;
    private final Runnable logoutAction;
    private Admin activeUser;

    private final JLabel welcomeLabel = new JLabel("Welcome");
    private final JLabel feedbackLabel = new JLabel(" ");

    private final DefaultTableModel reportTableModel = new DefaultTableModel(
            new Object[]{"Report ID", "Generated Date", "Total", "Pending", "In Progress", "Resolved"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final DefaultTableModel staffTableModel = new DefaultTableModel(
            new Object[]{"User ID", "Staff ID", "Name", "Email"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTextArea reportSummaryArea = new JTextArea(8, 34);

    private final JTextField staffIdField = new JTextField();
    private final JTextField staffNameField = new JTextField();
    private final JTextField staffEmailField = new JTextField();
    private final JTextField staffPasswordField = new JTextField();

    public AdminDashboardPanel(AppShellController shellController, Runnable logoutAction) {
        this.shellController = shellController;
        this.logoutAction = logoutAction;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        add(buildHeader(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
    }

    public void setActiveUser(User user) {
        if (!(user instanceof Admin admin)) {
            throw new IllegalArgumentException("Admin dashboard requires Admin user.");
        }
        this.activeUser = admin;
        welcomeLabel.setText("Welcome, " + user.getName());
        feedbackLabel.setForeground(AppTheme.TEXT_SECONDARY);
        feedbackLabel.setText("Manage reports and IT staff accounts.");
        clearStaffForm();
        refreshReports();
        refreshStaff();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel roleLabel = new JLabel("System Admin", SwingConstants.RIGHT);
        roleLabel.setForeground(AppTheme.TEXT_SECONDARY);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(event -> logoutAction.run());
        right.add(roleLabel);
        right.add(logoutButton);

        header.add(welcomeLabel, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel buildContent() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(AppTheme.BG_CARD);
        card.setBorder(AppTheme.cardBorder());
        feedbackLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Reports", buildReportsTab());
        tabs.addTab("IT Staff", buildStaffTab());

        card.add(feedbackLabel, BorderLayout.NORTH);
        card.add(tabs, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildReportsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);
        JButton generateButton = new JButton("Generate Report");
        generateButton.addActionListener(event -> generateReport());
        JButton refreshButton = new JButton("Refresh Reports");
        refreshButton.addActionListener(event -> refreshReports());
        top.add(generateButton);
        top.add(refreshButton);

        JTable reportTable = new JTable(reportTableModel);
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reportTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int row = reportTable.getSelectedRow();
                if (row >= 0) {
                    Integer reportId = (Integer) reportTableModel.getValueAt(row, 0);
                    showReportSummary(reportId);
                }
            }
        });

        reportSummaryArea.setEditable(false);
        reportSummaryArea.setLineWrap(true);
        reportSummaryArea.setWrapStyleWord(true);
        reportSummaryArea.setText("Select a report row to view details.");

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(reportTable), BorderLayout.CENTER);
        panel.add(new JScrollPane(reportSummaryArea), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildStaffTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        JTable staffTable = new JTable(staffTableModel);
        staffTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        staffTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int row = staffTable.getSelectedRow();
                if (row >= 0) {
                    staffIdField.setText(String.valueOf(staffTableModel.getValueAt(row, 1)));
                    staffNameField.setText(String.valueOf(staffTableModel.getValueAt(row, 2)));
                    staffEmailField.setText(String.valueOf(staffTableModel.getValueAt(row, 3)));
                    staffPasswordField.setText("");
                }
            }
        });

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setOpaque(false);
        form.add(new JLabel("Staff ID"));
        form.add(staffIdField);
        form.add(new JLabel("Name"));
        form.add(staffNameField);
        form.add(new JLabel("Email"));
        form.add(staffEmailField);
        form.add(new JLabel("Password"));
        form.add(staffPasswordField);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        JButton createBtn = new JButton("Create");
        createBtn.addActionListener(event -> createStaff());
        JButton updateBtn = new JButton("Update");
        updateBtn.addActionListener(event -> updateStaff());
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(event -> deleteStaff());
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(event -> refreshStaff());
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(event -> clearStaffForm());
        actions.add(createBtn);
        actions.add(updateBtn);
        actions.add(deleteBtn);
        actions.add(refreshBtn);
        actions.add(clearBtn);

        JPanel right = new JPanel(new BorderLayout(0, 8));
        right.setOpaque(false);
        right.add(form, BorderLayout.NORTH);
        right.add(actions, BorderLayout.SOUTH);

        panel.add(new JScrollPane(staffTable), BorderLayout.CENTER);
        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    private void generateReport() {
        if (activeUser == null) {
            return;
        }
        try {
            Report report = shellController.generateReport(activeUser);
            feedbackLabel.setForeground(AppTheme.SUCCESS);
            feedbackLabel.setText("Report generated successfully. Report ID: " + report.getReportId());
            refreshReports();
            showReportSummary(report.getReportId());
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void refreshReports() {
        if (activeUser == null) {
            return;
        }
        List<Report> reports = shellController.viewReports(activeUser);
        reportTableModel.setRowCount(0);
        for (Report report : reports) {
            reportTableModel.addRow(new Object[]{
                    report.getReportId(),
                    report.getGeneratedDate(),
                    report.getTotalComplaints(),
                    report.countByStatus(edu.university.itreporting.model.Status.PENDING),
                    report.countByStatus(edu.university.itreporting.model.Status.IN_PROGRESS),
                    report.countByStatus(edu.university.itreporting.model.Status.RESOLVED)
            });
        }
    }

    private void showReportSummary(Integer reportId) {
        if (activeUser == null || reportId == null) {
            reportSummaryArea.setText("No report selected.");
            return;
        }
        Report report = shellController.viewReports(activeUser).stream()
                .filter(r -> r.getReportId() == reportId)
                .findFirst()
                .orElse(null);
        if (report == null) {
            reportSummaryArea.setText("Report not found.");
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Report ID: ").append(report.getReportId()).append("\n");
        builder.append("Generated Date: ").append(report.getGeneratedDate()).append("\n");
        builder.append("Total Complaints: ").append(report.getTotalComplaints()).append("\n");
        builder.append("Pending: ").append(report.countByStatus(edu.university.itreporting.model.Status.PENDING)).append("\n");
        builder.append("In Progress: ").append(report.countByStatus(edu.university.itreporting.model.Status.IN_PROGRESS)).append("\n");
        builder.append("Resolved: ").append(report.countByStatus(edu.university.itreporting.model.Status.RESOLVED)).append("\n");
        reportSummaryArea.setText(builder.toString());
    }

    private void refreshStaff() {
        if (activeUser == null) {
            return;
        }
        List<ITStaff> staffList = shellController.listITStaff(activeUser);
        staffTableModel.setRowCount(0);
        for (ITStaff staff : staffList) {
            staffTableModel.addRow(new Object[]{
                    staff.getUserId(),
                    staff.getStaffId(),
                    staff.getName(),
                    staff.getEmail()
            });
        }
    }

    private void createStaff() {
        if (activeUser == null) {
            return;
        }
        Integer staffId = parseStaffId();
        if (staffId == null) {
            return;
        }
        String name = staffNameField.getText().trim();
        String email = staffEmailField.getText().trim();
        String password = staffPasswordField.getText().trim();
        if (ValidationUtils.isBlank(name) || ValidationUtils.isBlank(email) || ValidationUtils.isBlank(password)) {
            showError("Name, email, and password are required for create.");
            return;
        }
        if (!ValidationUtils.isValidEmail(email)) {
            showError("Enter a valid email address.");
            return;
        }
        try {
            shellController.createITStaff(activeUser, staffId, name, email, password);
            feedbackLabel.setForeground(AppTheme.SUCCESS);
            feedbackLabel.setText("IT Staff account created successfully.");
            refreshStaff();
            clearStaffForm();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void updateStaff() {
        if (activeUser == null) {
            return;
        }
        Integer staffId = parseStaffId();
        if (staffId == null) {
            return;
        }
        String name = staffNameField.getText().trim();
        String email = staffEmailField.getText().trim();
        String password = staffPasswordField.getText().trim();
        if (!ValidationUtils.isBlank(email) && !ValidationUtils.isValidEmail(email)) {
            showError("Enter a valid email address.");
            return;
        }
        try {
            shellController.updateITStaff(
                    activeUser,
                    staffId,
                    name.isEmpty() ? null : name,
                    email.isEmpty() ? null : email,
                    password.isEmpty() ? null : password
            );
            feedbackLabel.setForeground(AppTheme.SUCCESS);
            feedbackLabel.setText("IT Staff account updated successfully.");
            refreshStaff();
            clearStaffForm();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteStaff() {
        if (activeUser == null) {
            return;
        }
        Integer staffId = parseStaffId();
        if (staffId == null) {
            return;
        }
        try {
            boolean removed = shellController.removeITStaff(activeUser, staffId);
            if (removed) {
                feedbackLabel.setForeground(AppTheme.SUCCESS);
                feedbackLabel.setText("IT Staff account removed successfully.");
                refreshStaff();
                clearStaffForm();
            } else {
                showError("Unable to remove IT Staff account.");
            }
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private Integer parseStaffId() {
        String raw = staffIdField.getText().trim();
        if (raw.isEmpty()) {
            showError("Staff ID is required.");
            return null;
        }
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException ex) {
            showError("Staff ID must be a number.");
            return null;
        }
    }

    private void clearStaffForm() {
        staffIdField.setText("");
        staffNameField.setText("");
        staffEmailField.setText("");
        staffPasswordField.setText("");
    }

    private void showError(String message) {
        feedbackLabel.setForeground(AppTheme.ERROR);
        feedbackLabel.setText(message);
    }
}
