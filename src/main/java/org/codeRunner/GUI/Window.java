package org.codeRunner.GUI;

import org.codeRunner.GUI.Components.Button;
import org.codeRunner.GUI.Components.Label;
import org.codeRunner.run.FileSystem;
import org.codeRunner.run.Runner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Window extends JFrame implements ActionListener {
    final int WIDTH=700;
    final int HEIGHT=700;
    FileSelector fileSelector;
    Button open;
    Button save;
    Button run;
    JTextArea text;
    JComboBox<String>languageSelector;
    Color backGroundColor =Color.black;
    final Font DefaultFont=new Font("DIGIFACE", Font.PLAIN,16);

    public Window(){
        fileSelector=new FileSelector(this);
        setWindow();
        Label languageLabel=new Label("Language",DefaultFont);
        setSelector();

        run=new Button("RUN",this,DefaultFont);
        open=new Button("OPEN",this,DefaultFont);
        save=new Button("SAVE",this,DefaultFont);

        setHeader(languageLabel);

        setTextArea();
        this.setVisible(true);
    }

    private void setHeader(Label languageLabel) {
        JPanel head = new JPanel(new FlowLayout(FlowLayout.RIGHT),true);
        head.setBackground(backGroundColor);
        head.add(languageLabel);
        head.add(languageSelector);
        head.add(save);
        head.add(open);
        head.add(run);
        this.add(head,BorderLayout.NORTH);
    }

    private void setWindow() {

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setSize(WIDTH,HEIGHT);
        this.setTitle("CODE RUNNER");
        ImageIcon icon = new ImageIcon("src/main/resources/assets/icon.png");
        System.out.println(icon.getDescription());
        this.setIconImage(icon.getImage());
        this.setBackground(Color.DARK_GRAY);
    }

    private void setSelector() {
        languageSelector=new JComboBox<>(new String[]{"c","cpp","java"});
        languageSelector.setSelectedIndex(0);
        languageSelector.setBackground(backGroundColor);
        languageSelector.setFont(DefaultFont);
        languageSelector.setFocusable(false);
        languageSelector.setForeground(Color.white);
    }

    private void setTextArea() {
        text=new JTextArea();
        text.setBackground(Color.DARK_GRAY);
        text.setForeground(Color.lightGray);
        text.setSize(this.getWidth(),700);
        text.setFont(new Font(Font.DIALOG,Font.PLAIN,16));
        text.setCaretColor(Color.cyan);
        this.add(text,BorderLayout.CENTER);
    }

    void openFile()  {
        String path = fileSelector.getFile();
        if(path!=null){
            text.setText(FileSystem.readWhole(path));
        }
    }
    void saveFile(){
        String path = fileSelector.saveFile();
        if(path!=null){
            FileSystem.createFile(path);
            FileSystem.writeWhole(text.getText(),path);
        }
    }
    void run(){
        String language= Objects.requireNonNull(languageSelector.getSelectedItem()).toString();
        String path = System.getProperty("java.io.tmpdir")+"test."+language;
        FileSystem.createFile(path);
        FileSystem.writeWhole(text.getText(),path);
        runTask(path);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        var eventTrigger=e.getSource();
        if(eventTrigger==open){
            openFile();
        }
        else if(eventTrigger==save){
            saveFile();
        }
        else if(eventTrigger==run){
           run();
        }
    }
    public void runTask(String path){
        Runner runner=new Runner(path);
        Thread runnerThread = new Thread(runner);
        runnerThread.start();
    }
}
