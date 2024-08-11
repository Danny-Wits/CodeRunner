package org.codeRunner.Scripts;

import org.codeRunner.GUI.Components.LanguageAdder;
import org.codeRunner.GUI.Components.LanguageInfo;
import org.codeRunner.GUI.Components.LanguageSetting;
import org.codeRunner.GUI.SettingPaneCreator;

public class SettingProcessor {
    public static void getLanguageInfoPane(){
        (new SettingPaneCreator(new LanguageInfo())).open();
    }

    public static void getLanguageSettingPane() {
        (new SettingPaneCreator(new LanguageSetting())).open();
    }
    public static void getLanguageAdder() {
        (new SettingPaneCreator(new LanguageAdder())).open();
    }
}
