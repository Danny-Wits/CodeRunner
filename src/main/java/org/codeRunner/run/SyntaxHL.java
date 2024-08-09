package org.codeRunner.run;

import org.codeRunner.GUI.CodePanel;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.List;

public class SyntaxHL{
    StyledDocument document;
    String language;
    CodePanel parent;
    List<String>keywords;
    Color keywordColor;

    public SyntaxHL(CodePanel parent,StyledDocument document,String language){
        this.document=document;
        this.language=language;
        this.parent=parent;
        switch (language){
            case "c"-> {keywords=Language.C.keywords;}
            case "cpp"-> {keywords=Language.CPP.keywords;}
            case "java"-> {keywords=Language.Java.keywords;}
            case "py"-> {keywords=Language.Python.keywords;}
        }
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
                document.setCharacterAttributes(pos,length,getStyle(Color.orange),true);
            }else{
                document.setCharacterAttributes(pos,length,getStyle(DefaultColor),true);
            };
            pos+=length;
        }
    }

    String getPattern() {
        StringBuilder pattern =new StringBuilder();
        keywords.forEach(e->{
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

    public void setKeywordColor(Color keywordColor) {
        this.keywordColor = keywordColor;
    }
}
