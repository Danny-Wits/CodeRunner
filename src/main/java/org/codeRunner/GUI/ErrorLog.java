package org.codeRunner.GUI;

import org.codeRunner.GUI.Components.Button;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class ErrorLog extends JPanel implements ActionListener {
    JTextPane errorPane;
    Button clear;
    Button close;

    ErrorLog() {
        this.setLayout(new BorderLayout());
        this.setVisible(false);
        this.setPreferredSize(new Dimension(0,200));
        errorPane = new JTextPane();
        errorPane.setBorder(new EmptyBorder(10,10,10,10));
        errorPane.setFont(new Font(Font.MONOSPACED,Font.PLAIN,18));
        JScrollPane scrollPane = new JScrollPane(errorPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonHolder = new JPanel();
        buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.Y_AXIS));
        clear = new Button("CLEAR", this);
        clear.setAlignmentX(Container.CENTER_ALIGNMENT);

        close = new Button("CLOSE", this);
        close.setAlignmentX(Container.CENTER_ALIGNMENT);


        buttonHolder.add(clear);
        buttonHolder.add(close);
        this.add(buttonHolder, BorderLayout.EAST);
    }

    public String getError() {
        return errorPane.getText();
    }

    public void setError(String error) {
        this.setVisible(true);
        errorPane.setText(error);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == close) {
            this.setVisible(false);
        } else if (e.getSource() == clear) {
            setError("");
        }
    }

}
