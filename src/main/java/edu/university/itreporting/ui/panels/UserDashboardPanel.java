package edu.university.itreporting.ui.panels;

import edu.university.itreporting.model.Complaint;
import edu.university.itreporting.model.ResolutionNote;
import edu.university.itreporting.model.User;
import edu.university.itreporting.ui.AppShellController;
import edu.university.itreporting.ui.AppTheme;
import edu.university.itreporting.util.ValidationUtils;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

public class UserDashboardPanel extends JPanel {
    private static final String[] LOCATIONS = {
            "Room 1", "Room 2", "Room 3","Room 4", "Room 5", "Room 6", "Room 7", "Room 8",
            "Room 9", "Room 10", "Room 11","Room 12","Mehboob/PC lab","Library","Hassan Abidi Lab","Khyber Lab",
            "Rafaqat Lab","Call Lab","Hall A"
    };
    private static final String[] EQUIPMENT_TYPES = {
            "Projector", "Monitor", "Mouse", "Keyboard", "Network Cable", "PC", "Printer"
    };

    private final AppShellController shellController;
    private final Runnable logoutAction;
    private User activeUser;

    private final JLabel welcomeLabel = new JLabel("Welcome");
    private final JLabel feedbackLabel = new JLabel(" ");
    private final JComboBox<String> locationCombo = new JComboBox<>(LOCATIONS);
    private final JComboBox<String> equipmentTypeCombo = new JComboBox<>(EQUIPMENT_TYPES);
    private final JTextField equipmentIdField = new JTextField();
    private final JTextArea descriptionArea = new JTextArea(4, 24);
    private final JTextField trackMemoField = new JTextField();
    private final JTextArea trackDetailsArea = new JTextArea(10, 36);
    private final JTextArea notificationsArea = new JTextArea(8, 36);
    private final DefaultTableModel complaintsTableModel = new DefaultTableModel(
            new Object[]{"Memo ID", "Status", "Priority", "Location", "Equipment", "Submitted"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public UserDashboardPanel(AppShellController shellController, Runnable logoutAction) {
        this.shellController = shellController;
        this.logoutAction = logoutAction;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
    }

    public void setActiveUser(User user) {
        this.activeUser = user;
        welcomeLabel.setText(user == null ? "Welcome" : "Welcome, " + user.getName());
        feedbackLabel.setForeground(AppTheme.TEXT_SECONDARY);
        feedbackLabel.setText("Use the tabs below to submit and track your memos.");
        refreshComplaints();
        refreshNotifications();
        clearTrackingView();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel roleLabel = new JLabel("Student / Faculty", SwingConstants.RIGHT);
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

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Submit Memo", wrapScrollable(buildSubmitTab()));
        tabs.addTab("My Complaints", wrapScrollable(buildComplaintsTab()));
        tabs.addTab("Track Status", wrapScrollable(buildTrackTab()));
        tabs.addTab("Notifications", wrapScrollable(buildNotificationsTab()));

        feedbackLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        card.add(feedbackLabel, BorderLayout.NORTH);
        card.add(tabs, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildSubmitTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);

        JPanel form = new JPanel(new GridLayout(8, 1, 6, 6));
        form.setOpaque(false);
        form.add(new JLabel("Location"));
        form.add(locationCombo);
        form.add(new JLabel("Equipment Type"));
        form.add(equipmentTypeCombo);
        form.add(new JLabel("Equipment ID / PC Number"));
        form.add(equipmentIdField);
        form.add(new JLabel("Problem Description"));
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        form.add(descScroll);

        JButton submitButton = new JButton("Submit Complaint");
        submitButton.addActionListener(event -> submitComplaint());
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonRow.setOpaque(false);
        buttonRow.add(submitButton);

        panel.add(form, BorderLayout.NORTH);
        panel.add(buttonRow, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildComplaintsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        JTable table = new JTable(complaintsTableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton refreshButton = new JButton("Refresh List");
        refreshButton.addActionListener(event -> refreshComplaints());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        top.setOpaque(false);
        top.add(refreshButton);

        panel.add(top, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildTrackTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);

        JPanel input = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        input.setOpaque(false);
        trackMemoField.setPreferredSize(new Dimension(140, 30));
        JButton trackButton = new JButton("Track");
        trackButton.addActionListener(event -> trackComplaint());
        input.add(new JLabel("Memo ID: "));
        input.add(trackMemoField);
        input.add(Box.createRigidArea(new Dimension(8, 0)));
        input.add(trackButton);

        trackDetailsArea.setEditable(false);
        trackDetailsArea.setLineWrap(true);
        trackDetailsArea.setWrapStyleWord(true);
        JScrollPane detailsScroll = new JScrollPane(trackDetailsArea);

        panel.add(input, BorderLayout.NORTH);
        panel.add(detailsScroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildNotificationsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        notificationsArea.setEditable(false);
        notificationsArea.setLineWrap(true);
        notificationsArea.setWrapStyleWord(true);

        JButton refreshButton = new JButton("Refresh Notifications");
        refreshButton.addActionListener(event -> refreshNotifications());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        top.setOpaque(false);
        top.add(refreshButton);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(notificationsArea), BorderLayout.CENTER);
        return panel;
    }

    private void submitComplaint() {
        if (activeUser == null) {
            return;
        }
        String equipmentId = equipmentIdField.getText().trim();
        String description = descriptionArea.getText().trim();
        if (ValidationUtils.isBlank(equipmentId) || ValidationUtils.isBlank(description)) {
            feedbackLabel.setForeground(AppTheme.ERROR);
            feedbackLabel.setText("Please fill required fields: Equipment ID and Problem Description.");
            return;
        }
        if (!ValidationUtils.hasMinLength(description, 8)) {
            feedbackLabel.setForeground(AppTheme.ERROR);
            feedbackLabel.setText("Problem description should be at least 8 characters.");
            return;
        }
        try {
            Complaint complaint = shellController.submitComplaint(
                    activeUser,
                    (String) locationCombo.getSelectedItem(),
                    (String) equipmentTypeCombo.getSelectedItem(),
                    equipmentId,
                    description
            );
            feedbackLabel.setForeground(AppTheme.SUCCESS);
            feedbackLabel.setText("Complaint submitted successfully. Memo ID: " + complaint.getMemoId());
            equipmentIdField.setText("");
            descriptionArea.setText("");
            refreshComplaints();
            refreshNotifications();
        } catch (Exception ex) {
            feedbackLabel.setForeground(AppTheme.ERROR);
            feedbackLabel.setText(ex.getMessage());
        }
    }

    private void refreshComplaints() {
        complaintsTableModel.setRowCount(0);
        if (activeUser == null) {
            return;
        }
        List<Complaint> complaints = shellController.viewSubmittedComplaints(activeUser);
        for (Complaint complaint : complaints) {
            complaintsTableModel.addRow(new Object[]{
                    complaint.getMemoId(),
                    complaint.getStatus().getDisplayName(),
                    complaint.getPriority().getDisplayName(),
                    complaint.getLocation(),
                    complaint.getEquipmentType() + " [" + complaint.getEquipmentId() + "]",
                    complaint.getDateSubmitted()
            });
        }
    }

    private void trackComplaint() {
        if (activeUser == null) {
            return;
        }
        String rawMemoId = trackMemoField.getText().trim();
        if (rawMemoId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a memo ID.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int memoId;
        try {
            memoId = Integer.parseInt(rawMemoId);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Memo ID must be a number.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Complaint complaint = shellController.trackComplaintStatus(activeUser, memoId);
            StringBuilder builder = new StringBuilder();
            builder.append("Memo ID: ").append(complaint.getMemoId()).append("\n");
            builder.append("Status: ").append(complaint.getStatus().getDisplayName()).append("\n");
            builder.append("Priority: ").append(complaint.getPriority().getDisplayName()).append("\n");
            builder.append("Location: ").append(complaint.getLocation()).append("\n");
            builder.append("Equipment: ").append(complaint.getEquipmentType())
                    .append(" [").append(complaint.getEquipmentId()).append("]\n");
            builder.append("Description: ").append(complaint.getDescription()).append("\n");
            builder.append("\nResolution Notes:\n");
            if (complaint.getNotes().isEmpty()) {
                builder.append("No notes yet.");
            } else {
                for (ResolutionNote note : complaint.getNotes()) {
                    builder.append("- ").append(note.getDate())
                            .append(" | ").append(note.getText()).append("\n");
                }
            }
            trackDetailsArea.setText(builder.toString());
        } catch (Exception ex) {
            trackDetailsArea.setText("Unable to track complaint: " + ex.getMessage());
        }
    }

    private void refreshNotifications() {
        if (activeUser == null) {
            notificationsArea.setText("");
            return;
        }
        List<String> notifications = shellController.getUserNotifications(activeUser);
        if (notifications.isEmpty()) {
            notificationsArea.setText("No notifications yet.");
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (String notification : notifications) {
            builder.append("- ").append(notification).append("\n");
        }
        notificationsArea.setText(builder.toString());
    }

    private void clearTrackingView() {
        trackMemoField.setText("");
        trackDetailsArea.setText("Enter a memo ID to see detailed status and notes.");
    }

    private JScrollPane wrapScrollable(JPanel content) {
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }
}
