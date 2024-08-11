package org.codeRunner.GUI.Components;

import org.codeRunner.Scripts.FileSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Button extends JButton {
    public Button(String text, ActionListener actionListener, Font font, String tip, String iconPath) {
        super(text);
        this.setToolTipText(tip);
        if (!iconPath.isEmpty()) this.setIcon(FileSystem.getImageIcon(iconPath));
        this.addActionListener(actionListener);
        //this.setFont(font);
        styleSetting();
    }

    public Button(String text, ActionListener actionListener) {
        super(text);
        this.addActionListener(actionListener);
       // this.setFont(Window.DefaultFont);
        styleSetting();
    }

    private void styleSetting() {
        this.setFocusable(false);
    }
}
