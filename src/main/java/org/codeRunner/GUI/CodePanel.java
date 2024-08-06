package org.codeRunner.GUI;

import org.codeRunner.GUI.Components.Button;
import org.codeRunner.Main;
import org.codeRunner.run.Code;
import org.codeRunner.run.FileSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

public class CodePanel extends JPanel implements ActionListener, KeyListener {
    public JTextArea codeArea;
    public String language;
    public String path;
    public String name;
    public JPanel header;
    Button closeButton;

    public CodePanel(String path) {
        super(new BorderLayout(), true);
        this.codeArea = new JTextArea(FileSystem.readWhole(path));
        this.language = Code.getExtension(path)[0];
        this.path = path;
        this.name = Code.getFileName(path);
        header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(5, 10, 5, 10));
        setTitle();
        setActions();
        this.add(header, BorderLayout.NORTH);
        setCodeArea();
    }

    //GUI
    private void setTitle() {
        JLabel label = new JLabel("Path : " + path);
        label.setFont(new Font(Font.MONOSPACED, Font.ITALIC, 16));
        header.add(label, BorderLayout.CENTER);
    }

    private void setActions() {
        closeButton = new Button("CLOSE", this, Window.DefaultFont,"Ctrl+Shift+X","");
        header.add(closeButton, BorderLayout.EAST);
    }

    private void setCodeArea() {
        codeArea.setBackground(Color.BLACK);
        codeArea.setForeground(Color.GREEN);
        codeArea.addKeyListener(this);
        codeArea.setBorder(new EmptyBorder(10, 20, 1, 10));
        codeArea.setSize(this.getWidth(), 700);
        codeArea.setFont(new Font("Bahnschrift", Font.PLAIN, 18));
        codeArea.setCaretColor(Color.cyan);
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
        return  FileSystem.getImage(path);
    }

    public String getDetails(){
        return String.format("Name: %S Lines:%d Size:%dKB Language: %S ",name,getLines(),getSizeOfFile(),language);
    }

    private int getLines() {
      return  FileSystem.getLineCount(path);
    }

    public File getFile(){
        return new File(this.path);
    }
    private int getSizeOfFile() {
    return FileSystem.getSize(path)/1024;
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


    public void saveFile() {
        FileSystem.writeWhole(getText(), this.path);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            Main.window.removeCurrentCodePanel();
        }
    }

    public int getCtrlKeyCode(Character c) {
        return ((int) c) - 96;
    }

    //SHORTCUT HANDLER
    @Override
    public void keyTyped(KeyEvent e) {
        if (!e.isControlDown()) return;
        int code = (int) e.getKeyChar();
        if (e.isShiftDown()){
            if (code == getCtrlKeyCode('s')) {
                Main.window.saveFileAs();
            }else if (code == getCtrlKeyCode('x')){
                Main.window.removeCurrentCodePanel();
            }
            return;
        }
        if (code == getCtrlKeyCode('r')) {
            Main.window.run();
        } else if (code == getCtrlKeyCode('s')) {
            Main.window.saveFile();
        }else if (code == getCtrlKeyCode('o')){
            Main.window.openFile();
        }
        else if (code == getCtrlKeyCode('o')){
            Main.window.openFile();
        }
        else if (code == getCtrlKeyCode('n')){
            Main.window.newFile();
        }

    }


    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
