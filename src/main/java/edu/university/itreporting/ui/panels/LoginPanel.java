package edu.university.itreporting.ui.panels;

import edu.university.itreporting.ui.AppTheme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.function.BiConsumer;

public class LoginPanel extends JPanel {
    private final JTextField emailField = new JTextField(26);
    private final JPasswordField passwordField = new JPasswordField(26);
    private final JLabel messageLabel = new JLabel("Enter your credentials to continue", SwingConstants.CENTER);

    public LoginPanel(BiConsumer<String, String> loginHandler) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 46));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppTheme.BG_CARD);
        card.setBorder(AppTheme.cardBorder());
        card.setPreferredSize(new Dimension(520, 340));

        JLabel title = new JLabel("IT Components Problem Reporting");
        title.setFont(AppTheme.titleFont());
        title.setForeground(AppTheme.TEXT_PRIMARY);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Login to access your role dashboard");
        subtitle.setForeground(AppTheme.TEXT_SECONDARY);
        subtitle.setAlignmentX(CENTER_ALIGNMENT);

        messageLabel.setForeground(AppTheme.TEXT_SECONDARY);
        messageLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel form = new JPanel(new GridLayout(4, 1, 8, 8));
        form.setOpaque(false);
        JLabel emailLabel = new JLabel("Email");
        JLabel passLabel = new JLabel("Password");
        form.add(emailLabel);
        form.add(emailField);
        form.add(passLabel);
        form.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(CENTER_ALIGNMENT);
        loginButton.addActionListener(event -> loginHandler.accept(
                emailField.getText().trim(),
                new String(passwordField.getPassword())
        ));

        passwordField.addActionListener(event -> loginButton.doClick());
        emailField.addActionListener(event -> passwordField.requestFocusInWindow());

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(messageLabel);
        card.add(Box.createRigidArea(new Dimension(0, 18)));
        card.add(form);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        card.add(loginButton);

        add(card);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
    }

    public void setMessage(String message, boolean error) {
        messageLabel.setText(message == null ? "" : message);
        messageLabel.setForeground(error ? AppTheme.ERROR : AppTheme.TEXT_SECONDARY);
    }
}
