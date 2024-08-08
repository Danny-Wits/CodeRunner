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
    public SyntaxHL(CodePanel parent,StyledDocument document,String language){
        this.document=document;
        this.language=language;
        this.parent=parent;
        switch (language){
            case "c"-> {keywords=Code.LANGUAGES.C.keywords;}
            case "cpp"-> {keywords=Code.LANGUAGES.CPP.keywords;}
            case "java"-> {keywords=Code.LANGUAGES.Java.keywords;}
            case "py"-> {keywords=Code.LANGUAGES.Python.keywords;}
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
        for (String s : text.split("\\W")) {
            if(s.matches(pattern)){
                document.setCharacterAttributes(pos=text.indexOf(s,pos),s.length(),getStyle(Color.orange),true);
            }else{
                document.setCharacterAttributes(pos=text.indexOf(s,pos),s.length(),getStyle(UIManager.getColor("TextField.foreground")),true);
            };
        }
    }

    private String getPattern() {
        StringBuilder pattern =new StringBuilder();
        keywords.forEach(e->{
            pattern.append("\\b").append(e).append("\\b").append("|");
        });
        return pattern.toString();
    }
    private  Style getStyle(Color keyWordColor) {
        Style style = document.addStyle("KeyWordColor", null);
        StyleConstants.setForeground(style, keyWordColor);
        return style;
    }
    private String getText() {
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
