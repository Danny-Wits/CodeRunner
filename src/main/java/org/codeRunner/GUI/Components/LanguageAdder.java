package org.codeRunner.GUI.Components;

import org.codeRunner.GUI.Interfaces.SettingPane;
import org.codeRunner.GUI.ThemeEditor;
import org.codeRunner.GUI.Window;
import org.codeRunner.Scripts.Language;

import javax.swing.*;
import java.awt.*;

public class LanguageAdder extends SettingPane {
    JLabel nameL, extensionL, compilerL, colorL, keywordsL, urlL;
    JTextField name, extension, compiler, url;
    Button colorB;
    Color color = Color.orange;
    JTextArea keywords;

    @Override
    public void draw() {
        title = "ADD LANGUAGE";
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        nameL = new JLabel("NAME");
        name = new JTextField(20);
        extensionL = new JLabel("EXTENSION");
        extension = new JTextField(20);
        compilerL = new JLabel("COMPILER");
        compiler = new JTextField(20);
        colorL = new JLabel("KEYWORD COLOR");
        JPanel colorPanel = new JPanel(new GridLayout(1, 2));
        colorL.setForeground(color);
        colorB = new Button("SELECT COLOR", (e) -> {
            color = ThemeEditor.getColor();
            colorL.setForeground(color);
        });
        colorPanel.add(colorL);
        colorPanel.add(colorB);
        keywordsL = new JLabel("KEYWORDS");
        keywords = new JTextArea(5, 20);
        urlL = new JLabel("Download Url");
        url = new JTextField(20);

        panel.add(nameL);
        panel.add(name);
        panel.add(extensionL);
        panel.add(extension);
        panel.add(compilerL);
        panel.add(compiler);
        panel.add(colorPanel);
        panel.add(keywordsL);
        panel.add(keywords);
        panel.add(urlL);
        panel.add(url);
        for (Component component : panel.getComponents()) {
            ((JComponent) component).setAlignmentX(Component.LEFT_ALIGNMENT);
        }
    }

    @Override
    public void saved() {
        System.out.println("adding language");
        for (Component component : panel.getComponents()) {
            if (component.getClass() == JTextField.class) {
                if (((JTextField) component).getText().trim().isEmpty()) {
                    Window.currentWindow.message("Invalid Inputs");
                    return;
                }
            }
        }
        Language newLanguage = new Language(name.getText(), extension.getText(), compiler.getText(), color, Language.parseKeywordString(keywords.getText()), url.getText());
        if (!newLanguage.name.equals(Language.INVALID)) {
            popup.dispose();
            LanguageSetting.current.refresh();
        }
    }

    @Override
    public void canceled() {
        popup.dispose();
    }


    @Override
    public void loaded() {
        popup.setSize(360, 480);
    }

}
