package org.codeRunner.Scripts;

import org.codeRunner.GUI.CodePanel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class SyntaxHL {
    StyledDocument document;
    Language language;
    CodePanel parent;

    public SyntaxHL(CodePanel parent, StyledDocument document, Language language) {
        this.document = document;
        this.language = language;
        this.parent = parent;

    }

    public void highLight() {
        String text = getText();
        if (text == null) return;
        processCode(text);
    }

    private void processCode(String text) {
        String pattern = getPattern();
        int pos = 0;
        int length;
        Color DefaultColor = UIManager.getColor("TextField.foreground");
        for (String word : text.split("\\W")) {
            length = word.length();
            pos = text.indexOf(word, pos);
            if (word.matches(pattern)) {
                setColor(pos, length, getStyle(language.keywordColor));
            } else {
                setColor(pos, length, getStyle(DefaultColor));
            }
            pos += length;
        }
    }

    public void setColor(int pos, int length, Style style) {
        document.setCharacterAttributes(pos, length, style, true);
    }

    public int highLight(String word,int start) {
        highLight();
        String text = getText();
        if (text == null) return -1;
        if (text.trim().isEmpty()) return -1;
        int pos=0;
        int length;
        int firstMatchIndex = -1;
        boolean flag = true;
        for (String textW : text.split("\\W")) {
            pos = text.indexOf(textW, pos);
            length = textW.length();
            if (textW.matches(word)) {
                if (flag && pos>=start) {
                    firstMatchIndex = pos+length;
                    flag = false;
                }
                if(pos>=start){
                    setColor(pos, length, getStyle(Color.RED));
                }
            }
            pos += length;
        }
        return firstMatchIndex;
    }

    String getPattern() {
        StringBuilder pattern = new StringBuilder();
        language.keywords.forEach(e -> {
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
        if (length == 0) return null;
        String text;
        try {
            text = document.getText(0, length);
        } catch (BadLocationException e) {
            text = null;
        }
        return text;
    }


}
