package org.codeRunner.run;

import org.codeRunner.GUI.ThemeEditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class State implements Serializable {
    public static final long serialversionUID =101L;
    public Setting setting;
    public List<String> paths;
    public State(List<String> paths) {
        this.setting=new Setting(ThemeEditor.currentLookAndFeelIndex);
        this.paths= paths;
    }
    public State(){
        this.setting=Setting.DefaultSetting();
        this.paths= new ArrayList<>();
    }
}
