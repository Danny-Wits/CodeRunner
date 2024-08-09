package org.codeRunner.run;

import org.codeRunner.GUI.ThemeEditor;
import org.codeRunner.GUI.Window;

import java.io.Serializable;
import java.util.HashMap;

public class Setting implements Serializable {
    public int  LookAndFeelIndex=2;
    public HashMap<String,Language> availableLanguages=new HashMap<>();
    public Setting() {
    }
    public Setting(int index){
        LookAndFeelIndex =ThemeEditor.currentLookAndFeelIndex;
        Language.languageList.forEach(this::addToMap);
    }
    private void addToMap(Language language){
        availableLanguages.put(language.extension,language);
    }
    public static void load(Setting setting){
        ThemeEditor.setTheme(Window.currentWindow,setting.LookAndFeelIndex);
    }
    public static Setting DefaultSetting(){
        return new Setting();
    }
}
