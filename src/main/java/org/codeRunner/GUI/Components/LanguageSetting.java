package org.codeRunner.GUI.Components;

import org.codeRunner.GUI.Interfaces.SettingPane;
import org.codeRunner.GUI.ThemeEditor;
import org.codeRunner.GUI.Window;
import org.codeRunner.Scripts.Language;
import org.codeRunner.Scripts.SettingProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LanguageSetting extends SettingPane {
    Button add;
    Button delete;
    List<Language>addedLanguageList;
    public static LanguageSetting current;
    public LanguageSetting(){
        current=this;
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        Language.languageList.forEach(e -> {
            LanguagePane languagePane = new LanguagePane(e);
            languagePane.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(languagePane);
        });

        add=new Button("ADD",(e)->{addToList();});
        delete=new Button("DELETE",(e)->{deleteFromList();});
        JPanel buttonHolder=new JPanel(new GridLayout(1,2));
        buttonHolder.add(add);
        buttonHolder.add(delete);
        panel.add(buttonHolder);
    }

    private void addToList() {
        SettingProcessor.getLanguageAdder();
    }

    private void deleteFromList() {
    }
    private void deleteFromList(List<Language> languageList) {
    }

    @Override
    public void loaded(){
        popup.setSize(720,360);
    }

    @Override
    public void saved() {
    popup.dispose();
    }

    @Override
    public void canceled() {
        deleteFromList(addedLanguageList);
        popup.dispose();
    }


    private static class LanguagePane extends JPanel implements ActionListener {
        JLabel name;
        Button keywordColor;
        Button keywords;
        Language language;
        LanguagePane(Language language){
            this.language=language;
            setLayout(new GridLayout(1,3));
            name=new JLabel(language.name);
            keywordColor=new Button("Edit Keyword Color",this);
            keywords=new Button("Edit Keywords",this);
            add(name);
            add(keywordColor);
            add(keywords);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==keywordColor){
                language.setKeywordColor(ThemeEditor.getColor(language.keywordColor));
            }else if(e.getSource()==keywords){
                keywordsEditor();
            }
        }

        private void keywordsEditor() {
           String keywords= Window.currentWindow.input("EDIT THE KEYWORDS FOR "+language.name,language.getKeywordString());
            if (keywords.isEmpty())return;
            language.setKeywords(keywords);
        }
    }
}
