package org.codeRunner.GUI;

import org.codeRunner.GUI.Components.Button;
import org.codeRunner.run.Code;
import org.codeRunner.run.FileSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class CodePanel extends JPanel implements ActionListener {
    public JTextArea codeArea;
    public String language;
    public String path;
    public String name;
    JPanel header;
    public JLabel headerLabel;
    Button runButton;
    Button closeButton;


    public CodePanel(String path) {
        super(new BorderLayout(), true);
        setDetails(path);
        header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(5, 10, 5, 10));
        setTitle();
        setActions();
        this.add(header, BorderLayout.NORTH);
        setCodeArea();
    }

    private void setDetails(String path) {
        this.codeArea = new JTextArea(FileSystem.readWhole(path));
        this.language = Code.getExtension(path)[0];
        this.path = path;
        this.name = Code.getFileName(path);
    }

    //GUI
    private void setTitle() {
        headerLabel = new JLabel(getDetails());
        headerLabel.setFont(new Font(Font.MONOSPACED, Font.ITALIC, 16));
        header.add(headerLabel, BorderLayout.CENTER);
    }

    private void setActions() {
        JPanel buttonBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        runButton = new Button("RUN", this, Window.DefaultFont, "Ctrl+R", "/assets/runFile.png");
        buttonBox.add(runButton);
        closeButton = new Button("CLOSE", this, Window.DefaultFont, "Ctrl+Shift+X", "/assets/closeFile.png");
        buttonBox.add(closeButton);
        header.add(buttonBox, BorderLayout.EAST);
    }

    private void setCodeArea() {
        //codeArea.setBackground(Color.BLACK);
        //codeArea.setForeground(Color.GREEN);
        codeArea.addKeyListener(Window.currentWindow);
        codeArea.setBorder(new EmptyBorder(10, 20, 1, 10));
        codeArea.setSize(this.getWidth(), 700);
        codeArea.setFont(new Font("Bahnschrift", Font.PLAIN, 18));
        //codeArea.setCaretColor(Color.cyan);
        codeArea.setCaretPosition(0);
        JScrollPane scrollPane = new JScrollPane(codeArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    //Functionality
    public String getText() {
        return this.codeArea.getText();
    }

    public Icon getIcon() {
        String path = String.format("/assets/%sLogo.png", language);
        return FileSystem.getImage(path);
    }

    public String getDetails() {
        return String.format("%S Lines:%d Size:%dKB Language:%S  Path:%s", name, getLines(), getSizeOfFile(), language, path);
    }

    private int getLines() {
        return FileSystem.getLineCountInString(getText());
    }

    int locationIn(ArrayList<CodePanel> codePanelList) {
        for (int i = 0; i < codePanelList.size(); i++) {
            CodePanel codePanel = codePanelList.get(i);
            if (this.path.equals(codePanel.path)) {
                return i;
            }
        }
        return -1;
    }


    //FILE MANAGEMENT
    public void saveFile() {
        reloadHeader();
        FileSystem.writeWhole(getText(), this.path);
    }

    public void renameFile(String newName) {
        if(newName==null)return;
        if(newName.isEmpty()||newName.contains(".")){
            Window.currentWindow.Message("INVALID FILE NAME");
            return;
        }
        String newPath = Code.getParentFolder(path) + newName +"."+ Code.getExtension(path)[0];
        if (newPath.equals(path)) return;
        System.out.println(newPath);
        FileSystem.move(path, newPath);
        Window.currentWindow.removeCurrentCodePanel();
        Window.currentWindow.addCodePanel(new CodePanel(newPath));
    }

    public void moveFile(String newPath) {
        if (newPath.equals(path)) return;
        System.out.println(newPath);
        FileSystem.move(path, newPath);
        Window.currentWindow.removeCurrentCodePanel();
        Window.currentWindow.addCodePanel(new CodePanel(newPath));
    }

    public File getFile() {

        return new File(this.path);
    }

    private int getSizeOfFile() {

        return FileSystem.getSize(path) / 1024;
    }


    //Action Management
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == runButton) {
            Window.currentWindow.run();
        } else if (e.getSource() == closeButton) {
            Window.currentWindow.removeCurrentCodePanel();
        }
    }



    void reloadHeader() {
        headerLabel.setText(getDetails());
    }
}
