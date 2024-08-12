package org.codeRunner.Scripts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class State implements Serializable {
    public static final long serialversionUID =101L;
    public SettingState setting;
    public List<String> paths;
    public State(List<String> paths) {
        this.setting=new SettingState();
        this.paths= paths;
    }
    public State(){
        this.setting= SettingProcessor.DefaultSetting();
        this.paths= new ArrayList<>();
    }
}
