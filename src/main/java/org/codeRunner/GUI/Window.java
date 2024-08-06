package org.codeRunner.GUI;

import org.codeRunner.GUI.Components.Button;
import org.codeRunner.GUI.Components.Label;
import org.codeRunner.run.FileSystem;
import org.codeRunner.run.Runner;
import org.codeRunner.run.State;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class Window extends JFrame implements ActionListener, ChangeListener {
    final int WIDTH = 1080;
    final int HEIGHT = 720;
    FileSelector fileSelector;
    public Label currentFileInfo;
    public ErrorLog errorLog;
    Button newB;
    Button open;
    Button saveAs;
    Button save;
    Button run;
    ArrayList<CodePanel> codePanelList = new ArrayList<>();
    JTabbedPane codeTabs;
    Color backgroundColor = Color.black;
    public static final Font DefaultFont = new Font("DIGIFACE", Font.PLAIN, 16);

    public Window() {
        fileSelector = new FileSelector(this);
        currentFileInfo=new Label("PLEASE OPEN A FILE",new Font(Font.DIALOG_INPUT,Font.BOLD,18));

        setWindow();
        errorLog = new ErrorLog();
        this.add(errorLog, BorderLayout.SOUTH);
        setButtons();

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


    public void reRender() {
        this.repaint();
    }

    //GUI
    private void setButtons() {
        newB = new Button("NEW", this, DefaultFont,"Ctrl+N","/assets/newFile.png");

        save = new Button("SAVE", this, DefaultFont,"Ctrl+S","/assets/saveFile.png");

        saveAs = new Button("SAVE AS", this, DefaultFont,"Ctrl+Shift+S","/assets/saveFileAs.png");

        open = new Button("OPEN", this, DefaultFont,"Ctrl+O","/assets/openFile.png");

        run = new Button("RUN", this, DefaultFont,"Ctrl+R","/assets/runFile.png");
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

    private void setHeader() {

        JPanel head = new JPanel(new BorderLayout(),true);
        head.setBackground(backgroundColor);
        head.setBorder(new EmptyBorder(0,15,0,2));

        JPanel leftHead=new JPanel();
        leftHead.setLayout(new BoxLayout(leftHead,BoxLayout.X_AXIS));
        leftHead.setBackground(backgroundColor);
        currentFileInfo.setAlignmentY(Component.CENTER_ALIGNMENT);
        leftHead.add(currentFileInfo);
        head.add(leftHead,BorderLayout.CENTER);

        JPanel rightHead = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rightHead.setBackground(backgroundColor);
        rightHead.add(newB);
        rightHead.add(save);
        rightHead.add(saveAs);
        rightHead.add(open);
        rightHead.add(run);
        head.add(rightHead,BorderLayout.EAST);

        this.add(head, BorderLayout.NORTH);
    }

    private void setCodeTab() {
        codeTabs = new JTabbedPane();
        codeTabs.setFont(DefaultFont);
        codeTabs.addChangeListener(this);
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
            FileSystem.writeWhole(getCurrentCodePanel().codeArea.getText(), path);
        }
    }

    void saveFile() {
        CodePanel current =getCurrentCodePanel();
        current.saveFile();
        currentFileInfo.setText(current.getDetails());
    }

    void run() {
        saveFile();
        CodePanel codePanel = getCurrentCodePanel();
        String path = codePanel.path;
        runTask(path);
    }


    //Panel Management
    void addCodePanel(CodePanel codePanel) {
        System.out.println(codePanelList.size());
        int location = codePanel.locationIn(codePanelList);
        if (location != -1) {
            codeTabs.setSelectedIndex(location);
            return;
        }
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

    CodePanel getCurrentCodePanel() {
        int index=codeTabs.getSelectedIndex();
        if(index==-1)return null;
        return codePanelList.get(index);
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
    @Override
    public void stateChanged(ChangeEvent e) {
        CodePanel current=getCurrentCodePanel();
     if(current==null){
         currentFileInfo.setText("PLEASE OPEN A FILE");
     }
     else{
         currentFileInfo.setText(current.getDetails());
     }

    }
    public void runTask(String path) {
        Runner runner = new Runner(path);
        Thread runnerThread = new Thread(runner);
        runnerThread.start();
    }


}
