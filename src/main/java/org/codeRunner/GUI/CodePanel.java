package org.codeRunner.GUI;

import org.codeRunner.GUI.Components.Button;
import org.codeRunner.Scripts.Code;
import org.codeRunner.Scripts.FileSystem;
import org.codeRunner.Scripts.Language;
import org.codeRunner.Scripts.SyntaxHL;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class CodePanel extends JPanel implements ActionListener {
    JTextPane codeArea;
    StyledDocument codeAreaDoc;
    public SyntaxHL syntaxHighLighter;
    UndoManager undoManager;
    Language language;
    public String path;
    String name;
    JPanel header;
    JLabel headerLabel;
    Button runButton;
    Button closeButton;
    int prevLength;

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
        this.codeArea = new JTextPane();
        this.codeArea.setText(FileSystem.readWhole(path));
        this.language = Language.getLanguage(path);
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
        JLabel runnable=new JLabel();
        Thread runCheck=new Thread(()->{if (isRunnable())runnable.setIcon(FileSystem.getImageIcon("/assets/tickIcon.png"));});
        runCheck.start();
        buttonBox.add(runnable);
        runButton = new Button("RUN", this, Window.DefaultFont, "Ctrl+R", "/assets/runFile.png");
        buttonBox.add(runButton);
        closeButton = new Button("CLOSE", this, Window.DefaultFont, "Ctrl+Shift+X", "/assets/closeFile.png");
        buttonBox.add(closeButton);
        header.add(buttonBox, BorderLayout.EAST);
    }

    private void setCodeArea() {
        codeArea.addKeyListener(Window.currentWindow);
        codeArea.setBorder(new EmptyBorder(10, 20, 1, 10));
        codeArea.setSize(this.getWidth(), 700);
        codeArea.setFont(new Font("Bahnschrift", Font.PLAIN, 18));
        codeArea.setCaretPosition(0);
        codeArea.getText();
        codeAreaDoc=codeArea.getStyledDocument();
        undoManager=new UndoManager();
        prevLength=codeAreaDoc.getLength();
        codeAreaDoc.addUndoableEditListener(e -> {
            int length = codeAreaDoc.getLength();
            if(prevLength!=length){
                undoManager.addEdit(e.getEdit());
                prevLength=length;
            }
        });
        syntaxHighLighter=new SyntaxHL(this,codeAreaDoc,language);
        syntaxHighLighter.highLight();
        JScrollPane scrollPane = new JScrollPane(codeArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    //Functionality
    public String getText() {
        return this.codeArea.getText();
    }
    public boolean isRunnable(){
       return Code.checkInstall(language);
    }

    public Icon getIcon() {
        String path = String.format("/assets/%sLogo.png", language.extension);
        return FileSystem.getImageIcon(path);
    }

    public String getDetails() {
        return String.format("%S Lines:%d Size:%dKB Language:%S  Path:%s", name, getLines(), getSizeOfFile(), language.name, path);
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
        reload();
        FileSystem.writeWhole(getText(), this.path);
    }

    public void renameFile(String newName) {
        if(newName==null)return;
        if(newName.isEmpty()||newName.contains(".")){
            Window.currentWindow.message("INVALID FILE NAME");
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
        if(newPath==null)return;
        if (newPath.equals(path)) return;
        System.out.println(newPath);
        FileSystem.move(path, newPath);
        Window.currentWindow.removeCurrentCodePanel();
        Window.currentWindow.addCodePanel(new CodePanel(newPath));
    }

    public File getFile() {

        return new File(this.path);
    }

    public int getSizeOfFile() {

        return FileSystem.getSize(path) / 1024;
    }

    public void setCursorPostition(int index){
        if(index<0||index>getText().length())return;
        codeArea.setCaretPosition(index);
    }

    public  void findText(String text ) {
        if(text==null)return;
        if(text.trim().isEmpty())return;
        setCursorPostition(syntaxHighLighter.highLight(text,0));
    }

    public  boolean replaceText(String find,String replace,int offset){
        if(find==null)return false;
        if(find.trim().isEmpty()) return false;
        int pos=0;
        int length;
        int index=-1;
        String text=getText();
        for (String textW : text.split("\\W")) {
            pos = text.indexOf(textW, pos);
            length = textW.length();
            if (textW.matches(find)) {
                index=pos;
                break;
            }
            pos += length;
        }
        if(index==-1||index>text.length())return false;
        try {
            codeAreaDoc.remove(index,find.length());
            codeAreaDoc.insertString(index,replace,null);
            undoManager.discardAllEdits();
            return true;
        } catch (BadLocationException e) {
            return false;
        }
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
    public void reload() {
        headerLabel.setText(getDetails());
        syntaxHighLighter.highLight();
    }


}
