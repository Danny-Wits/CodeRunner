package org.codeRunner.run;

import java.util.List;

public class Code {

    enum compiler{
        C("gcc"),
        CPP("g++"),
        JAVA("javac");
        final String value;
        compiler(String value){this.value=value;}
    }
    static boolean compile(String path){
        String compileCMD="";
        System.out.println("Compiling");
        String extension=getExtension(path)[0];
        switch (extension) {
            case "c" ->
                    compileCMD = String.format("%s \"%s\" -o \"%s\"", compiler.C.value, path, getExtension(path)[1]);
            case "cpp" ->
                    compileCMD = String.format("%s \"%s\" -o \"%s\"", compiler.CPP.value, path, getExtension(path)[1]);
            case "java" -> {
                compileCMD = String.format("%s %s", compiler.JAVA.value, getFileName(path));
                String error = CMD.run(compileCMD, getParentFolder(path)).get(1);
                return error.isEmpty();
            }
        }
        System.out.println(compileCMD);
        String error=CMD.run(compileCMD,"|").get(1);
        return error.isEmpty();
    }
    static String run(String path){
       String extension = getExtension(path)[0];
       System.out.println("Running");
       String runCMD="";
        switch (extension) {
            case "exe" -> runCMD = String.format("start cmd /k \"%s\"", path);
            case "class" -> {
                runCMD = String.format("start cmd /k java %s", getExtension(getFileName(path))[1]);
                List<String> result = CMD.run(runCMD, getParentFolder(path));
                return result.getFirst();
            }
        }
        List<String>result=CMD.run(runCMD,"|");
        return result.getFirst();
    }

    //Helper Functions;
    static String[] getExtension(String path) {
        int limit = 0;
        for (int i = path.length() - 1; i >= 0; i--) {
            if (path.charAt(i) == '.') {
                limit = i;
                break;
            }
        }
        return new String[]{path.substring(limit+1),path.substring(0, limit)};
    }
    static String getParentFolder(String path) {
        int limit = 0;
        for (int i = path.length() - 1; i >= 0; i--) {
            if (path.charAt(i) == '\\'||path.charAt(i) == '/') {
                limit = i;
                break;
            }
        }
        return path.substring(0, limit+1);
    }
    static String getFileName(String path){
        int limit = 0;
        for (int i = path.length() - 1; i >= 0; i--) {
            if (path.charAt(i) == '\\'||path.charAt(i) == '/') {
                limit = i;
                break;
            }
        }
        return path.substring(limit+1);
    }

}
