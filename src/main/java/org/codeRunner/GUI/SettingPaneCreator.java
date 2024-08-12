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
    String title;
    public SettingPaneCreator(SettingPane settingPane) {
        setLayout(new BorderLayout());
        this.title= settingPane.title;
        content.add(settingPane.getPanel());
        add(content, BorderLayout.CENTER);
        JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        save = new Button("OK", settingPane.getActionListener());
        close = new Button("CANCEL", settingPane.getActionListener());
        buttonHolder.add(save);
        buttonHolder.add(close);
        add(buttonHolder, BorderLayout.SOUTH);
        popup = createPopup(this);
        settingPane.load(save, close, popup);
        popup.setLocation(centreX(),centreY());
    }

    public JDialog createPopup(SettingPaneCreator settingPaneCreator) {
        JDialog jDialog = new JDialog(Window.currentWindow,title, true);
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
