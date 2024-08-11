package org.codeRunner.GUI;

import org.codeRunner.GUI.Components.Button;
import org.codeRunner.GUI.Interfaces.SettingPane;

import javax.swing.*;
import java.awt.*;


public class SettingPaneCreator extends JPanel {
    JPanel content = new JPanel();
    Button save;
    Button close;
    JDialog popup;

    public SettingPaneCreator(SettingPane settingPaneContent) {
        setLayout(new BorderLayout());
        content.add(settingPaneContent.getPanel());
        add(content, BorderLayout.CENTER);
        JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        save = new Button("OK", settingPaneContent.getActionListener());
        close = new Button("CANCEL", settingPaneContent.getActionListener());
        buttonHolder.add(save);
        buttonHolder.add(close);
        add(buttonHolder, BorderLayout.SOUTH);
        popup = createPopup(this);
        settingPaneContent.load(save, close, popup);
        popup.setLocation(centreX(),centreY());
    }

    public JDialog createPopup(SettingPaneCreator settingPaneCreator) {
        JDialog jDialog = new JDialog(Window.currentWindow, "SETTING", true);
        jDialog.getContentPane().add(settingPaneCreator);
        return jDialog;
    }

    private int centreX() {
        return Window.currentWindow.getX() + Window.currentWindow.getWidth() / 2 - popup.getWidth() / 2;
    }

    private int centreY() {
        return Window.currentWindow.getY() + Window.currentWindow.getHeight() / 2 - popup.getHeight() / 2;
    }

    public void open() {
        popup.setVisible(true);
    }

    public void close() {
        popup.dispose();
    }

}
