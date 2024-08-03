package org.codeRunner.GUI;

import org.codeRunner.GUI.Components.Button;
import org.codeRunner.run.FileSystem;
import org.codeRunner.run.Runner;
import org.codeRunner.run.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class Window extends JFrame implements ActionListener {
    final int WIDTH = 1080;
    final int HEIGHT = 720;
    FileSelector fileSelector;
    public ErrorLog errorLog;
    Button newB;
    Button open;
    Button saveAs;
    Button save;
    Button run;
    ArrayList<CodePanel> codePanelList = new ArrayList<>();
    JTabbedPane codeTabs;
    Color backGroundColor = Color.black;
    public static final Font DefaultFont = new Font("DIGIFACE", Font.PLAIN, 16);

    public Window() {
        fileSelector = new FileSelector(this);
        setWindow();
        errorLog=new ErrorLog();
        this.add(errorLog,BorderLayout.SOUTH);
        newB = new Button("NEW", this, DefaultFont);
        save = new Button("SAVE", this, DefaultFont);
        saveAs = new Button("SAVE AS", this, DefaultFont);
        open = new Button("OPEN", this, DefaultFont);
        open.setToolTipText("Ctrl+O");
        run = new Button("RUN", this, DefaultFont);
        run.setToolTipText("Ctrl+R");

        setHeader();
        setCodeTab();
        this.add(codeTabs);
        loadCodePanels();
        this.setVisible(true);
        saveCodePanelsOnExit();
    }

    private void loadCodePanels() {
        ArrayList<CodePanel> tempList = (ArrayList<CodePanel>)
                FileSystem.loadState()
                        .paths.stream()
                        .map(CodePanel::new)
                        .collect(Collectors.toCollection(ArrayList::new));
        tempList.forEach(this::addCodePanel);
    }

    private void saveCodePanelsOnExit() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                State state = FileSystem.createState(codePanelList);
                codePanelList.forEach(CodePanel::saveFile);
                FileSystem.saveState(state);
                super.windowClosing(e);
            }
        });

    }


    private void setCodeTab() {
        UIManager.put("TabbedPane.selected", Color.lightGray);
        codeTabs = new JTabbedPane();
        codeTabs.setFont(DefaultFont);
    }

    public void reRender() {
        this.repaint();
    }

    //GUI
    private void setHeader() {
        JPanel head = new JPanel(new FlowLayout(FlowLayout.RIGHT), true);
        head.setBackground(backGroundColor);
        head.add(newB);
        head.add(save);
        head.add(saveAs);
        head.add(open);
        head.add(run);
        this.add(head, BorderLayout.NORTH);
    }

    private void setWindow() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setBounds(100, 50, WIDTH, HEIGHT);
        this.setBackground(Color.black);
        this.setTitle("CODE RUNNER");
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/icon.png")));
        this.setIconImage(icon.getImage());
        this.setBackground(Color.DARK_GRAY);
    }

    //Functionality
    void newFile() {
        String path = fileSelector.saveFile();
        if (path != null) {
            FileSystem.createFile(path);
            CodePanel codePanel = new CodePanel(path);
            addCodePanel(codePanel);
        }
    }

    void openFile() {
        String path = fileSelector.getFile();
        if (path != null) {
            CodePanel codePanel = new CodePanel(path);
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

    void saveFile() {
        currentCodePanel().saveFile();
    }

    void run() {
        saveFile();
        CodePanel codePanel = currentCodePanel();
        String language = codePanel.language;
        String path =codePanel.path;
        runTask(path);
    }


    //Panel Management
    void addCodePanel(CodePanel codePanel) {
        System.out.println(codePanelList.size());
        if (codePanel.isIn(codePanelList)) return;
        System.out.println("ADDED");
        codePanelList.add(codePanel);
        codeTabs.addTab(codePanel.name, codePanel.getIcon(), codePanel, codePanel.path);
        codeTabs.setSelectedIndex(codePanelList.size() - 1);
        reRender();
    }

    void removeCurrentCodePanel() {
        int index = codeTabs.getSelectedIndex();
        codePanelList.remove(index);
        codeTabs.remove(index);
    }

    CodePanel currentCodePanel() {
        return codePanelList.get(codeTabs.getSelectedIndex());
    }

    //User Input
    @Override
    public void actionPerformed(ActionEvent e) {
        var eventTrigger = e.getSource();
        if (eventTrigger == open) {
            openFile();
        } else if (eventTrigger == saveAs) {
            saveFileAs();
        } else if (eventTrigger == save) {
            saveFile();
        } else if (eventTrigger == run) {
            run();
        } else if (eventTrigger == newB) {
            newFile();
        }
    }

    public void runTask(String path) {
        Runner runner = new Runner(path);
        Thread runnerThread = new Thread(runner);
        runnerThread.start();
    }

}
