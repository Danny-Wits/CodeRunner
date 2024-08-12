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
    List<Language> addedLanguageList;
    public static LanguageSetting current;

    @Override
    public void draw() {
        title = "LANGUAGE SETTING";
        current = this;
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        Language.languageList.forEach(e -> {
            LanguagePane languagePane = new LanguagePane(e);
            languagePane.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(languagePane);
        });
        add = new Button("ADD", (e) -> {
            addToList();
        });
        delete = new Button("DELETE", (e) -> {
            deleteFromList();
        });
        JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonHolder.add(add);
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
    public void loaded() {
        popup.setSize(720, 360);
    }

    @Override
    public void saved() {
        Window.currentWindow.reCheckLanguage();
        popup.dispose();
    }

    @Override
    public void canceled() {
        deleteFromList(addedLanguageList);
        popup.dispose();
    }


    private static class LanguagePane extends JPanel implements ActionListener {
        JLabel name;
        Button compiler;
        Button keywordColor;
        Button keywords;
        Button delete;
        Language language;

        LanguagePane(Language language) {

            this.language = language;
            setLayout(new GridLayout(1, 5));
            name = new JLabel(language.name);
            compiler = new Button(language.compiler, this);
            keywordColor = new Button("Keyword-Color", this);
            keywordColor.setForeground(language.keywordColor);
            keywords = new Button("Edit Keywords", this);
            delete = new Button("Delete", this);
            add(name);
            add(compiler);
            add(keywordColor);
            add(keywords);
            add(delete);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == keywordColor) {
                language.setKeywordColor(ThemeEditor.getColor(language.keywordColor));
                keywordColor.setForeground(language.keywordColor);
            } else if (source == keywords) {
                keywordsEditor();
            } else if (source == delete) {
                deleteLanguage();
            } else if (source == compiler) {
                String compilerPath = Window.currentWindow.input("EDIT THE COMPILER/INTERPRETER", language.compiler);
                if (compilerPath == null || compilerPath.trim().isEmpty()) {
                    return;
                }
                language.compiler = compilerPath;
                compiler.setText(language.compiler);
            }
        }

        private void keywordsEditor() {
            String keywords = Window.currentWindow.input("EDIT THE KEYWORDS FOR " + language.name, language.getKeywordString());
            if (keywords.isEmpty()) return;
            language.setKeywords(keywords);
        }

        private void deleteLanguage() {
            Language.remove(language);
            LanguageSetting.current.refresh();
        }
    }
}
