package edu.university.itreporting.ui.panels;

import edu.university.itreporting.model.User;
import edu.university.itreporting.ui.AppTheme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class RoleDashboardPanel extends JPanel {
    private final JLabel welcomeLabel = new JLabel("Welcome");

    public RoleDashboardPanel(String roleTitle, String[] modules, Runnable logoutAction) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(AppTheme.TEXT_PRIMARY);
        JLabel roleLabel = new JLabel(roleTitle);
        roleLabel.setForeground(AppTheme.TEXT_SECONDARY);
        header.add(welcomeLabel, BorderLayout.WEST);
        header.add(roleLabel, BorderLayout.EAST);

        JPanel contentCard = new JPanel();
        contentCard.setBackground(AppTheme.BG_CARD);
        contentCard.setBorder(AppTheme.cardBorder());
        contentCard.setLayout(new BoxLayout(contentCard, BoxLayout.Y_AXIS));

        JLabel modulesTitle = new JLabel("Available Modules");
        modulesTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        modulesTitle.setForeground(AppTheme.TEXT_PRIMARY);
        modulesTitle.setAlignmentX(LEFT_ALIGNMENT);
        contentCard.add(modulesTitle);
        contentCard.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel modulesGrid = new JPanel(new GridLayout(0, 1, 8, 8));
        modulesGrid.setOpaque(false);
        for (String module : modules) {
            JLabel label = new JLabel("\u2022 " + module);
            label.setForeground(AppTheme.TEXT_SECONDARY);
            modulesGrid.add(label);
        }
        contentCard.add(modulesGrid);
        contentCard.add(Box.createVerticalGlue());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footer.setOpaque(false);
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(event -> logoutAction.run());
        footer.add(logoutButton);

        add(header, BorderLayout.NORTH);
        add(contentCard, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    public void setActiveUser(User user) {
        String text = user == null ? "Welcome" : "Welcome, " + user.getName();
        welcomeLabel.setText(text);
    }
}
