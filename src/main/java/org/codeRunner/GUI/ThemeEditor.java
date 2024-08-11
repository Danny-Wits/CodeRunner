package org.codeRunner.GUI;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SwingUtilities.updateComponentTreeUI;

public class ThemeEditor {
    public static int currentLookAndFeelIndex = 2;
    public static LookAndFeel[] availableThemes = {new FlatDarkLaf(), new FlatLightLaf(), new FlatDarculaLaf(), new FlatIntelliJLaf(), new FlatMacLightLaf(), new FlatMacDarkLaf()};
    public static String[] availableThemesName = {"DARK", "LIGHT", "DARCULA", "INTELLIJ", "MAC LIGHT", "MAC DARK"};

    public static void changeThemePrompt(Window parent) {
        int index = JOptionPane.showOptionDialog(parent, "SELECT YOUR THEME", "THEME SELECTOR", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, availableThemesName, availableThemesName[currentLookAndFeelIndex]);
        if (index == -1) {
            return;
        }
        currentLookAndFeelIndex = index;
        setTheme(parent, currentLookAndFeelIndex);
    }

    public static void setTheme(Window parent, int index) {
        try {
            currentLookAndFeelIndex = index;
            UIManager.setLookAndFeel(availableThemes[currentLookAndFeelIndex]);
            updateComponentTreeUI(parent);
            Thread t = new Thread(() -> updateComponentTreeUI(parent.fileSelector));
            t.setDaemon(true);
            t.start();

        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }

    public static Color getColor() {
        Color color = JColorChooser.showDialog(Window.currentWindow,"PICK A COLOUR",Color.ORANGE);
        if (color == null) return Color.ORANGE;
        return color;
    }

    public static Color getColor(Color defaultColor) {
        Color color = JColorChooser.showDialog(Window.currentWindow,"PICK A COLOUR",defaultColor);
        if (color == null) return defaultColor;
        return color;
    }
}
