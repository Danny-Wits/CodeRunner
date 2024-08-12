package org.codeRunner.GUI;

import org.codeRunner.GUI.Components.MenuItem;
import org.codeRunner.Scripts.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.swing.SwingUtilities.updateComponentTreeUI;

public class Window extends JFrame implements ActionListener, KeyListener {
    final int WIDTH = 1080;
    final int HEIGHT = 720;
    FileSelector fileSelector;
    public ErrorLog errorLog;
    JMenuBar menuBar;
    JMenu fileMenu, editMenu, settingMenu, preferenceMenu;
    MenuItem newB, open, saveAs, save;
    MenuItem rename, move, undo, redo;
    MenuItem languageInfo, languageSetting;
    MenuItem theme;

    public ArrayList<CodePanel> codePanelList = new ArrayList<>();
    public JTabbedPane codeTabs;
    public static final Font DefaultFont = new Font("DIGIFACE", Font.PLAIN, 16);
    public static State state = FileSystem.loadState();
    public static Window currentWindow;
    public static JPanel WelcomePane = new JPanel();
    public JPanel center = new JPanel();

    public Window() {
        currentWindow = this;
        fileSelector = new FileSelector(this);
        menuBar = new JMenuBar();
        setWelcomePane();
        setMenuButtons();
        setWindow();

        errorLog = new ErrorLog();
        this.add(errorLog, BorderLayout.SOUTH);
        setCodeTab();
        loadSetting();
        loadCodePanels();
        if (codePanelList.isEmpty()) this.add(WelcomePane, BorderLayout.CENTER);
        this.setVisible(true);
        saveCodePanelsOnExit();
    }


    private void loadSetting() {
        SettingProcessor.load(state.setting);

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
                State state = new State(codePanelList.stream().map((codePanel) -> codePanel.path).toList());
                codePanelList.forEach(CodePanel::saveFile);
                FileSystem.saveState(state);
                super.windowClosing(e);
            }
        });

    }

    public void reRender() {
        this.revalidate();
        updateComponentTreeUI(Window.WelcomePane);
    }

    public void reCheckLanguage() {
        codePanelList.forEach(e -> {
            e.language = Language.getLanguage(e.path);
            e.reload();
        });
    }

    //GUI
    private void setMenuButtons() {
        newB = new MenuItem("New", this, DefaultFont, "Ctrl+N", "/assets/newFile.png");
        save = new MenuItem("Save", this, DefaultFont, "Ctrl+S", "/assets/saveFile.png");
        saveAs = new MenuItem("Save as", this, DefaultFont, "Ctrl+Shift+S", "/assets/saveFileAs.png");
        open = new MenuItem("Open", this, DefaultFont, "Ctrl+O", "/assets/openFile.png");

        rename = new MenuItem("Rename", this, DefaultFont, "Ctrl+Shift+R", "/assets/renameFile.png");
        move = new MenuItem("Move", this, DefaultFont, "Ctrl+Q", "/assets/moveFile.png");
        undo = new MenuItem("Undo", this, DefaultFont, "Ctrl+Z", "/assets/moveFile.png");
        redo = new MenuItem("Redo", this, DefaultFont, "Ctrl+Y", "/assets/moveFile.png");

        languageInfo = new MenuItem("Info", this, DefaultFont, "Language Setting", "/assets/moveFile.png");
        languageSetting = new MenuItem("Setting", this, DefaultFont, "Language Setting", "/assets/moveFile.png");

        theme = new MenuItem("Theme", this, DefaultFont, "Ctrl+T", "/assets/moveFile.png");


        fileMenu = new JMenu("FILE");
        fileMenu.add(newB);
        fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.add(open);

        editMenu = new JMenu("EDIT");
        editMenu.add(rename);
        editMenu.add(move);
        editMenu.add(undo);
        editMenu.add(redo);

        settingMenu = new JMenu("SETTING");
        JMenu language = new JMenu("Language");
        language.add(languageInfo);
        language.add(languageSetting);
        settingMenu.add(language);

        preferenceMenu = new JMenu("PREFERENCES");
        preferenceMenu.add(theme);


        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(settingMenu);
        menuBar.add(preferenceMenu);
    }

    private void setWindow() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setBounds(100, 50, WIDTH, HEIGHT);
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

    private void setWelcomePane() {
        WelcomePane.setLayout(new BoxLayout(WelcomePane, BoxLayout.Y_AXIS));
        Font def_font = (Font) UIManager.get("Label.font");
        UIManager.put("Label.font", new Font("Consolas", Font.PLAIN, 18));
        JPanel holder = new JPanel();

        JLabel welcome = new JLabel("WELCOME TO CODE RUNNER");
        welcome.setFont(new Font("Calibre Light", Font.PLAIN, 26));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcome.setBorder(new EmptyBorder(20, 40, 40, 40));
        WelcomePane.add(welcome);
        final EmptyBorder paddingBorder = new EmptyBorder(20, 40, 20, 40);
        JLabel features = new JLabel("Why Choose Code Runner".toUpperCase());
        features.setFont(new Font("Calibre Light", Font.PLAIN, 22));
        holder = new JPanel();
        holder.setLayout(new GridLayout(4, 1, 0, 5));
        holder.add(features);
        holder.add(new JLabel("<html><body>1.Instant Code Execution: Run your code in real-time, with immediate feedback and results.</body></html>"));
        holder.add(new JLabel("<html><body>2.Multi-Language Support: Experiment with different programming languages all in one place.</body></html>"));
        holder.add(new JLabel("<html><body>3.User-Friendly Interface: Enjoy a clean, intuitive design that makes coding a breeze.</body></html>"));
        holder.setBorder(paddingBorder);


        WelcomePane.add(holder);

        JLabel getStarted = new JLabel("Get Started!");
        getStarted.setFont(new Font("Calibre Light", Font.PLAIN, 22));
        holder = new JPanel();
        holder.setLayout(new GridLayout(3, 1, 10, 5));
        holder.add(getStarted);
        holder.add(new JLabel("<html><body>1.Goto File>>Open or Press Ctrl+O to open a file</body></html>"));
        holder.add(new JLabel("<html><body>2.Press the Run Button or Ctrl + R to run the program file</body></html>"));
        holder.setBorder(paddingBorder);
        WelcomePane.add(holder);
        updateComponentTreeUI(WelcomePane);
        UIManager.put("Label.font", def_font);
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

    void undoChange() {
        CodePanel codePanel = getCurrentCodePanel();
        if (codePanel == null) return;
        if (codePanel.undoManager.canUndo()) {
            codePanel.undoManager.undo();
            codePanel.prevLength = codePanel.codeAreaDoc.getLength();
        }
    }

    void redoChange() {
        CodePanel codePanel = getCurrentCodePanel();
        if (codePanel == null) return;
        if (codePanel.undoManager.canRedo()) {
            codePanel.undoManager.redo();
            codePanel.prevLength = codePanel.codeAreaDoc.getLength();

        }
    }

    void languageInfoPane() {

        SettingProcessor.getLanguageInfoPane();
    }

    void languageSettingPane() {

        SettingProcessor.getLanguageSettingPane();
    }

    //IO
    public String input(String prompt) {
        String name = JOptionPane.showInputDialog(this, prompt);
        if (name == null) name = "";
        return name;
    }

    public String input(String prompt, String defaultValue) {
        String name = JOptionPane.showInputDialog(this, prompt, defaultValue);
        if (name == null) name = "";
        return name;
    }


    public void message(String message, boolean flag) {
        if (flag) message(message);
    }

    public void message(String message) {
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
        if (WelcomePane.isVisible()) {
            WelcomePane.setVisible(false);
            this.add(codeTabs, BorderLayout.CENTER);
            updateComponentTreeUI(codeTabs);
        }
        codePanelList.add(codePanel);
        codeTabs.addTab(codePanel.name, codePanel.getIcon(), codePanel, codePanel.path);
        codeTabs.setSelectedIndex(codePanelList.size() - 1);
    }

    void removeCurrentCodePanel() {
        int index = codeTabs.getSelectedIndex();
        codePanelList.remove(index);
        codeTabs.remove(index);
//        if(codePanelList.isEmpty()){
//            WelcomePane.setVisible(true);
//        }
//        reRender();
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
        } else if (eventTrigger == languageInfo) {
            languageInfoPane();
        } else if (eventTrigger == languageSetting) {
            languageSettingPane();
        } else if (eventTrigger == undo) {
            undoChange();
        } else if (eventTrigger == redo) {
            redoChange();
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
            undoChange();
        } else if (code == getCtrlKeyCode('y')) {
            redoChange();
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        CodePanel current = getCurrentCodePanel();
        if (current != null) {
            current.reload();
        }
    }

    public void runTask(String path) {
        Runner runner = new Runner(path);
        Thread runnerThread = new Thread(runner);
        runnerThread.start();
    }


}
