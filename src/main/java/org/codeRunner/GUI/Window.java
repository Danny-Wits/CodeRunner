package org.codeRunner.GUI;

import org.codeRunner.GUI.Components.Button;
import org.codeRunner.run.Code;
import org.codeRunner.run.FileSystem;
import org.codeRunner.run.Runner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Window extends JFrame implements ActionListener {
    final int WIDTH = 1080;
    final int HEIGHT = 720;
    FileSelector fileSelector;
    Button open;
    Button saveAs;
    Button save;
    Button run;
    ArrayList<CodePanel> openCodePanels = new ArrayList<>();
    JTabbedPane codeTabs;
    Color backGroundColor = Color.black;
    final Font DefaultFont = new Font("DIGIFACE", Font.PLAIN, 16);

    public Window() {
        fileSelector = new FileSelector(this);
        setWindow();
        save = new Button("SAVE", this, DefaultFont);
        saveAs = new Button("SAVE AS", this, DefaultFont);
        open = new Button("OPEN", this, DefaultFont);
        run = new Button("RUN", this, DefaultFont);

        setHeader();
        setCodeTab();
        this.add(codeTabs);
        this.setVisible(true);
    }

    private void setCodeTab() {
        UIManager.put("TabbedPane.selected", Color.lightGray);
        codeTabs = new JTabbedPane();
        codeTabs.setFocusable(false);
        codeTabs.setFont(DefaultFont);
        String path=System.getProperty("java.io.tmpdir") + "test.cpp";
        String sourceCode = FileSystem.readWhole(path);
        String language = Code.getExtension(path)[0];
        CodePanel codePanel = new CodePanel(sourceCode, language, path);
        CodePanel codePanel1 = new CodePanel(sourceCode+"   ", language, path);
        addCodePanel(codePanel);
        addCodePanel(codePanel1);
    }

    public void reRender() {
        this.repaint();
    }

    private void setHeader() {
        JPanel head = new JPanel(new FlowLayout(FlowLayout.RIGHT), true);
        head.setBackground(backGroundColor);
        head.add(save);
        head.add(saveAs);
        head.add(open);
        head.add(run);
        this.add(head, BorderLayout.NORTH);
    }

    private void setWindow() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setBounds(100,50,WIDTH, HEIGHT);
        this.setBackground(Color.black);
        this.setTitle("CODE RUNNER");
        ImageIcon icon = new ImageIcon("src/main/resources/assets/icon.png");
        this.setIconImage(icon.getImage());
        this.setBackground(Color.DARK_GRAY);
    }


    void openFile() {
        String path = fileSelector.getFile();
        if (path != null) {
            String sourceCode = FileSystem.readWhole(path);
            String language = Code.getExtension(path)[0];
            CodePanel codePanel = new CodePanel(sourceCode, language, path);
            addCodePanel(codePanel);
        }
    }

    void saveFileAs() {
        String path = fileSelector.saveFile();
        if (path != null) {
            FileSystem.createFile(path);
            FileSystem.writeWhole(currentCodePanel().codeArea.getText(), path);
        }
    }
    void saveFile(){
         FileSystem.writeWhole(currentCodePanel().getText(), currentCodePanel().path);
    }
    void run() {
        CodePanel codePanel = currentCodePanel();
        String language = codePanel.language;
        String path = System.getProperty("java.io.tmpdir") + "test." + language;
        FileSystem.createFile(path);
        FileSystem.writeWhole(codePanel.getText(), path);
        runTask(path);
    }

    void addCodePanel(CodePanel codePanel) {
        openCodePanels.add(codePanel);
        codeTabs.addTab(codePanel.name, codePanel.getIcon(), codePanel, codePanel.path);
        codeTabs.setSelectedIndex(openCodePanels.size()-1);
        reRender();
    }

    CodePanel currentCodePanel() {
        return openCodePanels.get(codeTabs.getSelectedIndex());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var eventTrigger = e.getSource();
        if (eventTrigger == open) {
            openFile();
        } else if (eventTrigger == saveAs) {
            saveFileAs();
        }
        else if (eventTrigger == save) {
            saveFile();
        }else if (eventTrigger == run) {
            run();
        }
    }

    public void runTask(String path) {
        Runner runner = new Runner(path);
        Thread runnerThread = new Thread(runner);
        runnerThread.start();
    }
}
