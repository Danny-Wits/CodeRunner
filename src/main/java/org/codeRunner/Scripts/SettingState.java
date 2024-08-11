package org.codeRunner.Scripts;

import org.codeRunner.GUI.ThemeEditor;
import org.codeRunner.GUI.Window;

import java.io.Serializable;
import java.util.HashMap;

public class SettingState implements Serializable {
    public int  LookAndFeelIndex=2;
    public HashMap<String,Language> availableLanguages=new HashMap<>();
    public SettingState() {
    }
    public SettingState(int index){
        LookAndFeelIndex =ThemeEditor.currentLookAndFeelIndex;
        Language.languageList.forEach(this::addToMap);
    }
    private void addToMap(Language language){
        availableLanguages.put(language.extension,language);
    }
    public static void load(SettingState setting){
        ThemeEditor.setTheme(Window.currentWindow,setting.LookAndFeelIndex);
    }
    public static SettingState DefaultSetting(){
        return new SettingState();
    }
}
