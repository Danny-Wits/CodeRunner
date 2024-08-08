package org.codeRunner.GUI;

import org.codeRunner.GUI.Components.MenuItem;
import org.codeRunner.run.FileSystem;
import org.codeRunner.run.Runner;
import org.codeRunner.run.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class Window extends JFrame implements ActionListener, KeyListener {
    final int WIDTH = 1080;
    final int HEIGHT = 720;
    FileSelector fileSelector;
    public ErrorLog errorLog;
    JMenuBar menuBar;
    JMenu fileMenu, editMenu, preferenceMenu;
    MenuItem newB, open, saveAs, save, rename, move, theme;
    ArrayList<CodePanel> codePanelList = new ArrayList<>();
    JTabbedPane codeTabs;
    public static final Font DefaultFont = new Font("DIGIFACE", Font.PLAIN, 16);
    public static State state = FileSystem.loadState();
    public static Window currentWindow;

    public Window() {
        currentWindow = this;
        fileSelector = new FileSelector(this);
        menuBar = new JMenuBar();
        ThemeEditor.setTheme(this, state.preferredThemeIndex);

        setMenuButtons();
        setWindow();

        errorLog = new ErrorLog();
        this.add(errorLog, BorderLayout.SOUTH);

        setCodeTab();

        this.add(codeTabs);
        loadCodePanels();
        this.setVisible(true);
        saveCodePanelsOnExit();
    }


    private void loadCodePanels() {
        ArrayList<CodePanel> tempList =
                state.paths.stream()
                        .map(CodePanel::new)
                        .collect(Collectors.toCollection(ArrayList::new));
        tempList.forEach(this::addCodePanel);
    }

    private void saveCodePanelsOnExit() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                State state = FileSystem.createState(codePanelList, ThemeEditor.currentLookAndFeelIndex);
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
    private void setMenuButtons() {
        newB = new MenuItem("New", this, DefaultFont, "Ctrl+N", "/assets/newFile.png");

        save = new MenuItem("Save", this, DefaultFont, "Ctrl+S", "/assets/saveFile.png");

        saveAs = new MenuItem("Save as", this, DefaultFont, "Ctrl+Shift+S", "/assets/saveFileAs.png");

        open = new MenuItem("Open", this, DefaultFont, "Ctrl+O", "/assets/openFile.png");

        rename = new MenuItem("Rename", this, DefaultFont, "Ctrl+Shift+R", "/assets/renameFile.png");

        move = new MenuItem("Move", this, DefaultFont, "Ctrl+Q", "/assets/moveFile.png");

        theme = new MenuItem("Theme", this, DefaultFont, "Ctrl+T", "/assets/moveFile.png");

        fileMenu = new JMenu("FILE");
        fileMenu.add(newB);
        fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.add(open);

        editMenu = new JMenu("EDIT");
        editMenu.add(rename);
        editMenu.add(move);

        preferenceMenu = new JMenu("PREFERENCES");
        preferenceMenu.add(theme);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(preferenceMenu);
    }

    private void setWindow() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setBounds(100, 50, WIDTH, HEIGHT);
        this.setBackground(Color.black);
        this.setTitle("CODE RUNNER");
        this.setJMenuBar(menuBar);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/icon.png")));
        this.setIconImage(icon.getImage());
        this.setBackground(Color.DARK_GRAY);
    }

    private void setCodeTab() {
        codeTabs = new JTabbedPane();
        codeTabs.setFont(DefaultFont);
        codeTabs.setFocusable(false);
    }

    //Functionality
    void newFile() {
        String path = fileSelector.saveFile("New File");
        if (path != null) {
            FileSystem.createFile(path);
            CodePanel codePanel = new CodePanel(path);
            addCodePanel(codePanel);
        }
    }

    void openFile() {
        String path = fileSelector.getFile("Open File");
        if (path != null) {
            CodePanel codePanel = new CodePanel(path);
            addCodePanel(codePanel);
        }
    }

    void saveFileAs() {
        String path = fileSelector.saveFile("Save File As");
        if (path != null) {
            FileSystem.createFile(path);
            FileSystem.writeWhole(getCurrentCodePanel().codeArea.getText(), path);
        }
    }

    void saveFile() {
        CodePanel current = getCurrentCodePanel();
        current.saveFile();
    }

    void renameFile() {
        String newName = input("ENTER NEW NAME WITHOUT EXTENSION:").trim();
        getCurrentCodePanel().renameFile(newName);
    }

    void moveFile() {
        String newPath = fileSelector.saveFile("Move File");
        getCurrentCodePanel().moveFile(newPath);
    }

    void run() {
        saveFile();
        CodePanel codePanel = getCurrentCodePanel();
        String path = codePanel.path;
        runTask(path);
    }

    void changeTheme() {
        ThemeEditor.changeThemePrompt(this);
        codePanelList.forEach(CodePanel::reload);

    }

    void undo() {
        CodePanel codePanel = getCurrentCodePanel();
        if (codePanel == null) return;
        if (codePanel.undoManager.canUndo()) codePanel.undoManager.undo();
    }

    void redo() {
        CodePanel codePanel = getCurrentCodePanel();
        if (codePanel == null) return;
        if (codePanel.undoManager.canRedo()) codePanel.undoManager.redo();
    }

    //IO
    String input(String prompt) {
        String name = JOptionPane.showInputDialog(this, prompt);
        if (name == null) name = "";
        return name;
    }

    void Message(String message, boolean flag) {
        if (flag) Message(message);
    }

    void Message(String message) {
        JOptionPane.showMessageDialog(this, message, "INFOMATION", JOptionPane.INFORMATION_MESSAGE);
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
        int index = codeTabs.getSelectedIndex();
        if (index == -1) return null;
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
        } else if (eventTrigger == newB) {
            newFile();
        } else if (eventTrigger == rename) {
            renameFile();
        } else if (eventTrigger == move) {
            moveFile();
        } else if (eventTrigger == theme) {
            changeTheme();
        }
    }

    public static int getCtrlKeyCode(Character c) {
        return ((int) c) - 96;
    }


    //SHORTCUT HANDLER
    @Override
    public void keyTyped(KeyEvent e) {
        if (!e.isControlDown()) return;
        int code = (int) e.getKeyChar();
        if (e.isShiftDown()) {
            if (code == getCtrlKeyCode('s')) {
                saveFileAs();
            } else if (code == getCtrlKeyCode('x')) {
                removeCurrentCodePanel();
            } else if (code == getCtrlKeyCode('r')) {
                renameFile();
            }
            return;
        }
        if (code == getCtrlKeyCode('r')) {
            run();
        } else if (code == getCtrlKeyCode('s')) {
            saveFile();
        } else if (code == getCtrlKeyCode('o')) {
            openFile();
        } else if (code == getCtrlKeyCode('q')) {
            moveFile();
        } else if (code == getCtrlKeyCode('n')) {
            newFile();
        } else if (code == getCtrlKeyCode('t')) {
            changeTheme();
        } else if (code == getCtrlKeyCode('z')) {
            undo();
        } else if (code == getCtrlKeyCode('y')) {
            redo();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
       CodePanel current= getCurrentCodePanel();
      if(current!=null){
          current.reload();
      }
    }

    public void runTask(String path) {
        Runner runner = new Runner(path);
        Thread runnerThread = new Thread(runner);
        runnerThread.start();
    }


}
