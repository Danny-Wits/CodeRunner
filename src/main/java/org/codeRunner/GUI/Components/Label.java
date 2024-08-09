package org.codeRunner.GUI.Components;

import javax.swing.*;
import java.awt.*;

public class Label extends JLabel {
    public Label(String text){
        this.setText(text);
        //this.setFont(font);
        this.setBackground(Color.BLACK);
        this.setForeground(Color.WHITE);
    }
}
