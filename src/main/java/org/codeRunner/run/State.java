package org.codeRunner.run;

import java.io.Serializable;
import java.util.List;

public class State implements Serializable {
    private static final long serialversionUID =101010;
    public List<String> paths;
    public State(List<String> paths) {
        this.paths = paths;
    }
}
