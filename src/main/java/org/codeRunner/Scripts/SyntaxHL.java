package org.codeRunner.Scripts;

import org.codeRunner.GUI.CodePanel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class SyntaxHL{
    StyledDocument document;
    Language language;
    CodePanel parent;

    public SyntaxHL(CodePanel parent,StyledDocument document,Language language){
        this.document=document;
        this.language=language;
        this.parent=parent;

    }
    public void highLight(){
        String text = getText();
        if (text == null) return;
        processCode(text);
    }

    private void processCode(String text) {
        String pattern = getPattern();
        int pos =0;
        int length;
        Color DefaultColor=UIManager.getColor("TextField.foreground");
        for (String s : text.split("\\W")) {
            length=s.length();
            pos=text.indexOf(s,pos);
            if(s.matches(pattern)){
                document.setCharacterAttributes(pos,length,getStyle(language.keywordColor),true);
            }else{
                document.setCharacterAttributes(pos,length,getStyle(DefaultColor),true);
            }
            pos+=length;
        }
    }

    String getPattern() {
        StringBuilder pattern =new StringBuilder();
        language.keywords.forEach(e->{
            pattern.append("\\b").append(e).append("\\b").append("|");
        });
        return pattern.toString();
    }
    Style getStyle(Color keyWordColor) {
        Style style = document.addStyle("KeyWordColor", null);
        StyleConstants.setForeground(style, keyWordColor);
        return style;
    }
    String getText() {
        int length = document.getLength();
        if (length==0) return null;
        String text;
        try {
            text = document.getText(0, length);
        } catch (BadLocationException e) {
            text = null;
        }
        return text;
    }


}
