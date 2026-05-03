package edu.university.itreporting.ui.panels;

import edu.university.itreporting.model.Complaint;
import edu.university.itreporting.model.ITStaff;
import edu.university.itreporting.model.Priority;
import edu.university.itreporting.model.Status;
import edu.university.itreporting.model.User;
import edu.university.itreporting.ui.AppShellController;
import edu.university.itreporting.ui.AppTheme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

public class ITStaffDashboardPanel extends JPanel {
    private static final String[] LOCATIONS = {
            "Any", "Room 1", "Room 2", "Room 3","Room 4", "Room 5", "Room 6", "Room 7", "Room 8",
            "Room 9", "Room 10", "Room 11","Room 12","Mehboob/PC lab","Library","Hassan Abidi Lab","Khyber Lab",
            "Rafaqat Lab","Call Lab","Hall A"
    };
    private static final String[] EQUIPMENT_TYPES = {
            "Any", "Projector", "Monitor", "Mouse", "Keyboard", "Network Cable", "PC", "Printer"
    };

    private final AppShellController shellController;
    private final Runnable logoutAction;
    private ITStaff activeUser;
    private List<Complaint> currentRows = List.of();

    private final JLabel welcomeLabel = new JLabel("Welcome");
    private final JLabel feedbackLabel = new JLabel(" ");
    private final DefaultTableModel complaintTableModel = new DefaultTableModel(
            new Object[]{"Memo ID", "Status", "Priority", "Location", "Equipment", "Owner", "Submitted"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JComboBox<String> locationFilter = new JComboBox<>(LOCATIONS);
    private final JComboBox<String> equipmentFilter = new JComboBox<>(EQUIPMENT_TYPES);
    private final JComboBox<String> statusFilter = new JComboBox<>(new String[]{"Any", "Pending", "In Progress", "Resolved"});
    private final JComboBox<String> priorityFilter = new JComboBox<>(new String[]{"Any", "Low", "Medium", "High"});
    private final JTextField actionMemoIdField = new JTextField();
    private final JComboBox<Priority> assignPriorityCombo = new JComboBox<>(Priority.values());
    private final JComboBox<Status> updateStatusCombo = new JComboBox<>(Status.values());
    private final JTextArea noteArea = new JTextArea(5, 24);
    private final JTextArea detailArea = new JTextArea(10, 34);

    public ITStaffDashboardPanel(AppShellController shellController, Runnable logoutAction) {
        this.shellController = shellController;
        this.logoutAction = logoutAction;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
    }

    public void setActiveUser(User user) {
        if (!(user instanceof ITStaff itStaff)) {
            throw new IllegalArgumentException("IT Staff dashboard requires ITStaff user.");
        }
        this.activeUser = itStaff;
        welcomeLabel.setText("Welcome, " + user.getName());
        feedbackLabel.setForeground(AppTheme.TEXT_SECONDARY);
        feedbackLabel.setText("Use filters and actions below to process complaints.");
        actionMemoIdField.setText("");
        noteArea.setText("");
        detailArea.setText("Select a complaint row to inspect details.");
        loadAllComplaints();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel roleLabel = new JLabel("IT Staff", SwingConstants.RIGHT);
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
        tabs.addTab("All Complaints", buildAllComplaintsTab());
        tabs.addTab("Actions", buildActionsTab());

        card.add(feedbackLabel, BorderLayout.NORTH);
        card.add(tabs, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildAllComplaintsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Location"));
        filterPanel.add(locationFilter);
        filterPanel.add(new JLabel("Equipment"));
        filterPanel.add(equipmentFilter);
        filterPanel.add(new JLabel("Status"));
        filterPanel.add(statusFilter);
        filterPanel.add(new JLabel("Priority"));
        filterPanel.add(priorityFilter);

        JButton applyFiltersButton = new JButton("Apply Filters");
        applyFiltersButton.addActionListener(event -> applyFilters());
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(event -> {
            locationFilter.setSelectedIndex(0);
            equipmentFilter.setSelectedIndex(0);
            statusFilter.setSelectedIndex(0);
            priorityFilter.setSelectedIndex(0);
            loadAllComplaints();
        });
        filterPanel.add(applyFiltersButton);
        filterPanel.add(resetButton);

        JTable table = new JTable(complaintTableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0 && row < currentRows.size()) {
                    Complaint complaint = currentRows.get(row);
                    actionMemoIdField.setText(String.valueOf(complaint.getMemoId()));
                    showComplaintDetails(complaint);
                }
            }
        });

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildActionsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        JPanel left = new JPanel(new GridLayout(0, 1, 8, 8));
        left.setOpaque(false);
        left.add(new JLabel("Memo ID"));
        left.add(actionMemoIdField);

        left.add(new JLabel("Assign Priority"));
        JPanel pRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pRow.setOpaque(false);
        JButton assignPriorityButton = new JButton("Apply");
        assignPriorityButton.addActionListener(event -> assignPriority());
        pRow.add(assignPriorityCombo);
        pRow.add(Box.createRigidArea(new Dimension(8, 0)));
        pRow.add(assignPriorityButton);
        left.add(pRow);

        left.add(new JLabel("Update Status"));
        JPanel sRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        sRow.setOpaque(false);
        JButton updateStatusButton = new JButton("Apply");
        updateStatusButton.addActionListener(event -> updateStatus());
        sRow.add(updateStatusCombo);
        sRow.add(Box.createRigidArea(new Dimension(8, 0)));
        sRow.add(updateStatusButton);
        left.add(sRow);

        left.add(new JLabel("Resolution Note"));
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);
        left.add(new JScrollPane(noteArea));
        JButton addNoteButton = new JButton("Add Note");
        addNoteButton.addActionListener(event -> addNote());
        left.add(addNoteButton);

        detailArea.setEditable(false);
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        detailArea.setText("Select a complaint row to inspect details.");

        panel.add(left, BorderLayout.WEST);
        panel.add(new JScrollPane(detailArea), BorderLayout.CENTER);
        return panel;
    }

    private void loadAllComplaints() {
        if (activeUser == null) {
            return;
        }
        currentRows = shellController.viewAllComplaints(activeUser);
        populateComplaintTable(currentRows);
    }

    private void applyFilters() {
        if (activeUser == null) {
            return;
        }
        String location = selectedOrNull((String) locationFilter.getSelectedItem());
        String equipment = selectedOrNull((String) equipmentFilter.getSelectedItem());
        Status status = statusFromFilter((String) statusFilter.getSelectedItem());
        Priority priority = priorityFromFilter((String) priorityFilter.getSelectedItem());
        currentRows = shellController.filterComplaints(activeUser, location, equipment, status, priority);
        populateComplaintTable(currentRows);
        feedbackLabel.setForeground(AppTheme.TEXT_SECONDARY);
        feedbackLabel.setText("Filtered results: " + currentRows.size() + " complaint(s).");
    }

    private void populateComplaintTable(List<Complaint> complaints) {
        complaintTableModel.setRowCount(0);
        for (Complaint complaint : complaints) {
            complaintTableModel.addRow(new Object[]{
                    complaint.getMemoId(),
                    complaint.getStatus().getDisplayName(),
                    complaint.getPriority().getDisplayName(),
                    complaint.getLocation(),
                    complaint.getEquipmentType() + " [" + complaint.getEquipmentId() + "]",
                    complaint.getOwner().getName(),
                    complaint.getDateSubmitted()
            });
        }
    }

    private void assignPriority() {
        Integer memoId = parseMemoId();
        if (memoId == null) {
            return;
        }
        try {
            Complaint updated = shellController.assignPriority(activeUser, memoId, (Priority) assignPriorityCombo.getSelectedItem());
            feedbackLabel.setForeground(AppTheme.SUCCESS);
            feedbackLabel.setText("Priority updated for Memo #" + updated.getMemoId() + ".");
            loadAllComplaints();
            showComplaintDetails(updated);
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void updateStatus() {
        Integer memoId = parseMemoId();
        if (memoId == null) {
            return;
        }
        try {
            Complaint updated = shellController.updateStatus(activeUser, memoId, (Status) updateStatusCombo.getSelectedItem());
            feedbackLabel.setForeground(AppTheme.SUCCESS);
            feedbackLabel.setText("Status updated for Memo #" + updated.getMemoId() + ".");
            loadAllComplaints();
            showComplaintDetails(updated);
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void addNote() {
        Integer memoId = parseMemoId();
        if (memoId == null) {
            return;
        }
        String text = noteArea.getText().trim();
        if (text.isEmpty()) {
            showError("Please write a note before saving.");
            return;
        }
        try {
            shellController.addResolutionNote(activeUser, memoId, text);
            feedbackLabel.setForeground(AppTheme.SUCCESS);
            feedbackLabel.setText("Resolution note saved for Memo #" + memoId + ".");
            noteArea.setText("");
            loadAllComplaints();
            Complaint complaint = currentRows.stream()
                    .filter(c -> c.getMemoId() == memoId)
                    .findFirst()
                    .orElse(null);
            if (complaint != null) {
                showComplaintDetails(complaint);
            }
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private Integer parseMemoId() {
        String raw = actionMemoIdField.getText().trim();
        if (raw.isEmpty()) {
            showError("Enter a Memo ID or select a complaint from the table.");
            return null;
        }
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException ex) {
            showError("Memo ID must be a valid number.");
            return null;
        }
    }

    private void showComplaintDetails(Complaint complaint) {
        StringBuilder builder = new StringBuilder();
        builder.append("Memo ID: ").append(complaint.getMemoId()).append("\n");
        builder.append("Owner: ").append(complaint.getOwner().getName()).append("\n");
        builder.append("Status: ").append(complaint.getStatus().getDisplayName()).append("\n");
        builder.append("Priority: ").append(complaint.getPriority().getDisplayName()).append("\n");
        builder.append("Location: ").append(complaint.getLocation()).append("\n");
        builder.append("Equipment: ").append(complaint.getEquipmentType())
                .append(" [").append(complaint.getEquipmentId()).append("]\n");
        builder.append("Description: ").append(complaint.getDescription()).append("\n");
        builder.append("\nNotes:\n");
        if (complaint.getNotes().isEmpty()) {
            builder.append("No notes yet.");
        } else {
            complaint.getNotes().forEach(note ->
                    builder.append("- ").append(note.getDate())
                            .append(" | ").append(note.getText()).append("\n")
            );
        }
        detailArea.setText(builder.toString());
    }

    private void showError(String message) {
        feedbackLabel.setForeground(AppTheme.ERROR);
        feedbackLabel.setText(message);
    }

    private static String selectedOrNull(String value) {
        if (value == null || "Any".equalsIgnoreCase(value)) {
            return null;
        }
        return value;
    }

    private static Status statusFromFilter(String value) {
        return switch (value) {
            case "Pending" -> Status.PENDING;
            case "In Progress" -> Status.IN_PROGRESS;
            case "Resolved" -> Status.RESOLVED;
            default -> null;
        };
    }

    private static Priority priorityFromFilter(String value) {
        return switch (value) {
            case "Low" -> Priority.LOW;
            case "Medium" -> Priority.MEDIUM;
            case "High" -> Priority.HIGH;
            default -> null;
        };
    }
}
