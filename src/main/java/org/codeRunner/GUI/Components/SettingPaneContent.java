package org.codeRunner.GUI.Components;

import javax.swing.*;
import java.awt.event.ActionListener;

public interface SettingPaneContent {
    void load(Button ok, Button cancel, JDialog popup);
    ActionListener getActionListener();
    JPanel getPanel();
}
