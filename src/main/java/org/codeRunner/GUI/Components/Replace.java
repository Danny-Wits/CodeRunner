package org.codeRunner.GUI.Components;

import org.codeRunner.GUI.CodePanel;
import org.codeRunner.GUI.Interfaces.SettingPane;
import org.codeRunner.GUI.Window;

import javax.swing.*;
import java.awt.*;

public class Replace extends SettingPane {
    JLabel findL, replaceL;
    JTextField find,replace;
    Button findB,skipB,replaceB,replaceAllB;
    CodePanel currentPanel= Window.currentWindow.getCurrentCodePanel();
    int findIndex=0;
    @Override
    public void draw() {
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        title="Replace Words";
        showButton=false;
        JPanel findP=new JPanel(new BorderLayout(10,20));
        findL = new JLabel("Find");
        find=new JTextField();
        findP.add(findL,BorderLayout.WEST);
        findP.add(find,BorderLayout.CENTER);

        JPanel replaceP=new JPanel(new BorderLayout(10,20));
        replaceL=new JLabel("Replace");
        replace=new JTextField();
        replaceP.add(replaceL,BorderLayout.WEST);
        replaceP.add(replace,BorderLayout.CENTER);

        JPanel buttonP =new JPanel(new FlowLayout(FlowLayout.CENTER));

        findB=new Button("Find",(e)->findText(0));
        skipB=new Button("Skip",(e)->SkipText());
        replaceB=new Button("Replace",(e)->ReplaceText());
        replaceAllB=new Button("Replace All",(e)->ReplaceAllText());

        buttonP.add(findB);
        buttonP.add(skipB);
        buttonP.add(replaceB);
        buttonP.add(replaceAllB);

        panel.add(findP);
        panel.add(Box.createRigidArea(new Dimension(0,20)));
        panel.add(replaceP);
        panel.add(Box.createRigidArea(new Dimension(0,20)));
        panel.add(buttonP);
        panel.add(Box.createRigidArea(new Dimension(0,20)));


    }

    private void SkipText() {
        findText(findIndex);
    }

    private boolean ReplaceText() {
        int offset = findIndex-find.getText().length();
        if(offset<0)offset=0;
        return currentPanel.replaceText(find.getText(),replace.getText(),offset);
    }
    private void ReplaceAllText() {
     while (ReplaceText()){}
    }


    private void findText(int offset) {
        if(offset<0)offset=0;
        findIndex=offset;
        String text = find.getText();
        if(text==null)return;
        if(text.trim().isEmpty())return;
        findIndex=currentPanel.syntaxHighLighter.highLight(text,findIndex);
        currentPanel.setCursorPostition(findIndex);
    }

    @Override
    public void saved() {
        popup.dispose();
    }

    @Override
    public void canceled() {
    popup.dispose();
    }

    @Override
    public void loaded() {
        popup.setSize(400,200);
    }
}
