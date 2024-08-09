package org.codeRunner.GUI.Components;

import org.codeRunner.GUI.Window;
import org.codeRunner.run.Code;
import org.codeRunner.run.FileSystem;
import org.codeRunner.run.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LanguageSetting extends JPanel implements ActionListener,SettingPaneContent {
    public Button ok;
    public Button cancel;
    public JDialog popup;

    public LanguageSetting() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Language.languageList.forEach(e -> {
            LanguagePane languagePane = new LanguagePane(e);
            languagePane.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(languagePane);
        });
    }

    @Override
    public void load(Button ok, Button cancel,JDialog popup){
        this.ok=ok;
        this.cancel=cancel;
        this.popup=popup;
        popup.setSize(new Dimension(480, 240));
    }

    @Override
    public ActionListener getActionListener() {
        return this;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==ok){
            popup.dispose();
        }if(e.getSource()==cancel){
            popup.dispose();
        }
    }

    static class LanguagePane extends JPanel implements ActionListener {
        JPanel title;
        Button install;
        Button check;
        Language language;

        LanguagePane(Language language) {
            setLayout(new GridLayout(1,3));
            this.language = language;
            title = new JPanel(new GridLayout(1,2));
            JLabel name = new JLabel(language.name);
            JLabel installed=new JLabel();
            Thread thread = new Thread(()->{
                if(Code.checkInstall(language))installed.setIcon(FileSystem.getImageIcon("/assets/tickIconSmall.png"));
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
