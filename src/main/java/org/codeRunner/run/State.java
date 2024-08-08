package org.codeRunner.run;

import java.io.Serializable;
import java.util.List;

public class State implements Serializable {
    public static final long serialversionUID =101L;
    public List<String> paths;
    public int preferredThemeIndex;
    public State(List<String> paths,int CurrentThemeIndex) {
        this.paths = paths;
        preferredThemeIndex=CurrentThemeIndex;
    }
}
