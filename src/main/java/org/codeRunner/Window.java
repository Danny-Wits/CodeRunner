package org.codeRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Window extends JFrame implements ActionListener {
    final int WIDTH=700;
    final int HEIGHT=700;
    JFileChooser fileChooser;
    JButton open;
    JButton run;
    JTextArea text;
    Window(){
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.setBounds(100,100,WIDTH,HEIGHT);
        text=new JTextArea();
        text.setColumns(40);
        text.setRows(60);
        text.setLineWrap(true);
        this.add(text);
        run=new JButton("RUN");
        run.addActionListener(this);
        this.add(run);
        open=new JButton("OPEN");
        open.addActionListener(this);
        this.add(open);

        this.pack();
        this.setVisible(true);
    }
    void openFile()  {
        fileChooser=new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./"));
        this.add(fileChooser);
        int returnStatus = fileChooser.showOpenDialog(this);
        if(returnStatus==JFileChooser.APPROVE_OPTION){
            File selectedFile=fileChooser.getSelectedFile();
            try {
                BufferedReader bufferedReader=new BufferedReader(new FileReader(selectedFile));
                StringBuilder code = new StringBuilder();
                String outputLine;
                while ((outputLine=bufferedReader.readLine())!=null){
                    code.append(outputLine);
                    code.append("\n");
                }
                text.setText(code.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            System.out.println(selectedFile.getAbsolutePath());
            run(selectedFile.getAbsolutePath());
        }

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==open){
            openFile();
        }
        else if(e.getSource()==run){
            try {
                FileWriter test=new FileWriter("src/main/java/org/codeRunner/test.c");
                test.write(text.getText());
                test.close();
                run(new File("src/main/java/org/codeRunner/test.c").getAbsolutePath());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public void run(String path){
        Runner runner=new Runner(path);
        Thread runnerThread = new Thread(runner);
        runnerThread.start();
    }
}
