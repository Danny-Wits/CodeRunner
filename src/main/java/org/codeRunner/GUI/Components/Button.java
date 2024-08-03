package org.codeRunner.GUI.Components;

import org.codeRunner.GUI.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Button extends JButton {
     public Button(String text, ActionListener actionListener,Font font){
         super(text);
         this.addActionListener(actionListener);
         this.setFont(font);
         styleSetting();
     }
    public Button(String text, ActionListener actionListener){
        super(text);
        this.addActionListener(actionListener);
        this.setFont(Window.DefaultFont);
        styleSetting();
    }

    private void styleSetting() {
        this.setFocusable(false);
        this.setBackground(Color.BLACK);
        this.setForeground(Color.WHITE);

    }
}
