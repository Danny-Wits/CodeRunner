package org.codeRunner.Scripts;

import javax.swing.*;

public class Runner implements Runnable{
    String path;
    public Runner(String Path){
      this.path=Path;
    }
    @Override
    public void run() {
        if(Code.compile(this.path)){
            final String extension = Code.getExtension(this.path)[0];
            switch (extension) {
                case "c", "cpp" -> System.out.println(Code.run(Code.getExtension(path)[1] + ".exe"));
                case "java" -> System.out.println(Code.run(Code.getExtension(path)[1] + ".class"));
            }
        }else {
            JOptionPane.showMessageDialog(null,"Compilation Failed");
        }

    }
}
