package org.codeRunner.Scripts;

import org.codeRunner.GUI.Components.Replace;
import org.codeRunner.GUI.Components.LanguageAdder;
import org.codeRunner.GUI.Components.LanguageInfo;
import org.codeRunner.GUI.Components.LanguageSetting;
import org.codeRunner.GUI.SettingPaneCreator;
import org.codeRunner.GUI.ThemeEditor;
import org.codeRunner.GUI.Window;

public class SettingProcessor {
    public static void getLanguageInfoPane(){
        (new SettingPaneCreator(new LanguageInfo())).open();
    }
    public static void getLanguageSettingPane() {
        (new SettingPaneCreator(new LanguageSetting())).open();
    }
    public static void getLanguageAdderPane() {
        (new SettingPaneCreator(new LanguageAdder())).open();
    }
    public static void getReplacePane() {
        (new SettingPaneCreator(new Replace())).open();
    }

    public static void load(SettingState state){
        ThemeEditor.setTheme(Window.currentWindow,state.LookAndFeelIndex);
        Language.languageList=state.languageList;
    }
    public static SettingState DefaultSetting(){
        SettingState state = new  SettingState();
        state.setDefault();
        return state;
    }
}
