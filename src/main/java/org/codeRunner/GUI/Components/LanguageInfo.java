package org.codeRunner.GUI.Components;

import org.codeRunner.GUI.Interfaces.SettingPane;
import org.codeRunner.GUI.Window;
import org.codeRunner.Scripts.Code;
import org.codeRunner.Scripts.FileSystem;
import org.codeRunner.Scripts.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LanguageInfo extends SettingPane {

    @Override
    public void draw() {
        title = "LANGUAGE INFO";
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        Language.languageList.forEach(e -> {
            LanguagePane languagePane = new LanguagePane(e);
            languagePane.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(languagePane);
        });
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
        popup.setSize(540, 360);
    }

    @Override
    public void refresh() {

    }


    private static class LanguagePane extends JPanel implements ActionListener {
        JPanel title;
        Button install;
        Button check;
        Language language;

        LanguagePane(Language language) {
            setLayout(new GridLayout(1, 3));
            this.language = language;
            title = new JPanel(new GridLayout(1, 2));
            JLabel name = new JLabel(language.name);
            JLabel installed = new JLabel();
            Thread thread = new Thread(() -> {
                if (Code.checkInstall(language))
                    installed.setIcon(FileSystem.getImageIcon("/assets/tickIconSmall.png"));
            });
            thread.start();
            title.add(name);
            title.add(installed);
            install = new Button("Install", this);
            check = new Button("Check Installation", this);
            add(title);
            add(install);
            add(check);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == check) {
                Window.currentWindow.message(language.name + " is " + ((Code.checkInstall(language)) ? "" : "not ") + "installed");
            } else if (e.getSource() == install) {
                FileSystem.goToUrl(language.URL);
            }
        }
    }
}
