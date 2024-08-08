package org.codeRunner.GUI.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuItem extends JMenuItem {

    public MenuItem(String text, ActionListener actionListener, Font font, String tip, String iconPath){
        super(text);
        // this.setFont(font);
        this.addActionListener(actionListener);
        //this.setIcon(FileSystem.getImageIcon(iconPath));
        this.setToolTipText(tip);
    }
}
