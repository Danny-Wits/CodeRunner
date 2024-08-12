package org.codeRunner.Scripts;

import org.codeRunner.GUI.ThemeEditor;
import org.codeRunner.GUI.Window;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SettingState implements Serializable {
    public int  LookAndFeelIndex;
    public List<Language>languageList=new ArrayList<>();
    public SettingState(){
        LookAndFeelIndex =ThemeEditor.currentLookAndFeelIndex;
        languageList=Language.languageList;
    }
    public void setDefault(){
        LookAndFeelIndex=2;
        languageList=new ArrayList<>(List.of(Language.C,Language.CPP,Language.Java,Language.Python));
    }

    public static void load(SettingState setting){
        ThemeEditor.setTheme(Window.currentWindow,setting.LookAndFeelIndex);
    }

}
