package edu.university.itreporting.ui;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Font;

public final class AppTheme {
    public static final Color BG_APP = new Color(245, 247, 250);
    public static final Color BG_CARD = new Color(255, 255, 255);
    public static final Color TEXT_PRIMARY = new Color(32, 38, 51);
    public static final Color TEXT_SECONDARY = new Color(97, 106, 121);
    public static final Color ACCENT = new Color(26, 115, 232);
    public static final Color ERROR = new Color(198, 40, 40);
    public static final Color SUCCESS = new Color(46, 125, 50);
    public static final Color BORDER = new Color(219, 224, 232);
    private static final Font BASE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Border CARD_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(22, 24, 22, 24)
    );

    private AppTheme() {
    }

    public static void install() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException ignored) {
            // If Nimbus is not available, Swing falls back to the default LAF.
        }
        UIManager.put("defaultFont", BASE_FONT);
        UIManager.put("control", BG_APP);
        UIManager.put("info", BG_CARD);
        UIManager.put("nimbusBase", ACCENT);
        UIManager.put("nimbusFocus", new Color(120, 170, 240));
        UIManager.put("nimbusSelectionBackground", ACCENT);
        UIManager.put("text", TEXT_PRIMARY);
        UIManager.put("TextField.background", BG_CARD);
        UIManager.put("TextField.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground", TEXT_PRIMARY);
        UIManager.put("Button.background", ACCENT);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Panel.background", BG_APP);
    }

    public static Font baseFont() {
        return BASE_FONT;
    }

    public static Font titleFont() {
        return TITLE_FONT;
    }

    public static Border cardBorder() {
        return CARD_BORDER;
    }
}
