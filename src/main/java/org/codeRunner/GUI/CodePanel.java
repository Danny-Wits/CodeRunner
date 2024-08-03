package org.codeRunner.GUI;

import org.codeRunner.run.Code;

import javax.swing.*;
import java.awt.*;

public class CodePanel extends JPanel {
    public JTextArea codeArea;
    public String language;
    public String path;
    public String name;

    public CodePanel(String code, String language, String path) {
        super(new BorderLayout(), true);
        this.codeArea = new JTextArea(code);
        this.language = language;
        this.path = path;
        this.name = Code.getFileName(path);
        setCodeArea();
    }

    private void setCodeArea() {
        codeArea.setBackground(Color.black);
        codeArea.setForeground(Color.white);
        codeArea.setSize(this.getWidth(), 700);
        codeArea.setFont(new Font("Bahnschrift", Font.PLAIN, 18));
        codeArea.setCaretColor(Color.cyan);
        JScrollPane scrollPane=new JScrollPane(codeArea);
        scrollPane.setForeground(Color.black);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public String getText() {
        return this.codeArea.getText();
    }
    public Icon getIcon(){
        String path = String.format("src/main/resources/assets/%sLogo.png",language);
        return new ImageIcon(path);
    }
    
}
