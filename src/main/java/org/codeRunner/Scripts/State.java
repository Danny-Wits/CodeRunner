package org.codeRunner.Scripts;

import org.codeRunner.GUI.ThemeEditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class State implements Serializable {
    public static final long serialversionUID =101L;
    public SettingState setting;
    public List<String> paths;
    public State(List<String> paths) {
        this.setting=new SettingState(ThemeEditor.currentLookAndFeelIndex);
        this.paths= paths;
    }
    public State(){
        this.setting= SettingState.DefaultSetting();
        this.paths= new ArrayList<>();
    }
}
