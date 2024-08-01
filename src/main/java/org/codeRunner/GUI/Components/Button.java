package org.codeRunner.GUI.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Button extends JButton {
     public Button(String text, ActionListener actionListener,Font font){
         super(text);
         this.setFocusable(false);
         this.setFont(font);
         this.setBackground(Color.BLACK);
         this.setForeground(Color.WHITE);
         this.addActionListener(actionListener);
     }
}
