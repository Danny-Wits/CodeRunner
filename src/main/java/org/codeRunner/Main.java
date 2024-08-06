package org.codeRunner;

import org.codeRunner.GUI.Window;

import javax.swing.*;

public class Main {
    public static Window window;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        window=new Window();

    }
}
