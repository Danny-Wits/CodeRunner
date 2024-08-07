package org.codeRunner.run;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Code {

    public enum LANGUAGES {
        C("gcc","c"),
        Java("javac","java"),
        CPP("g++","cpp"),
        Python("python","py");
        public final String Compiler;
        public final String Extension;
        LANGUAGES(String compiler , String extension){
            this.Compiler=compiler;
            this.Extension=extension;
        }
        public String getExtension(){
            return this.Extension;
        }
    }

    public static String[] getAvailableLanguages(){
        ArrayList<String>languageList=new ArrayList<>();
        Arrays.stream(LANGUAGES.values()).forEach(e->languageList.add(e.Extension));
        return languageList.toArray(new String[]{});
    }

    static boolean compile(String path) {
        String compileCMD = "";
        System.out.println("Compiling");
        String extension = getExtension(path)[0];
        switch (extension) {
            case "c"->
                    compileCMD = String.format("%s \"%s\" -o \"%s\"", LANGUAGES.C.Compiler, path, getExtension(path)[1]);
            case "cpp"->
                    compileCMD = String.format("%s \"%s\" -o \"%s\"", LANGUAGES.CPP.Compiler, path, getExtension(path)[1]);
            case "java"  -> {
                compileCMD = String.format("%s %s",LANGUAGES.Java.Compiler, getFileNameWithExtension(path));
                String error = CMD.run(compileCMD, getParentFolder(path)).get(1);
                return error.isEmpty();
            }
            case "py" -> {return run(path).get(1).isEmpty();}
        }
        System.out.println(compileCMD);
        String error = CMD.run(compileCMD, "|").get(1);
        return error.isEmpty();
    }

    static List<String> run(String path) {
        String extension = getExtension(path)[0];
        System.out.println("Running");
        String runCMD;
        String batPath = System.getProperty("java.io.tmpdir") + "run.bat";
        if(!FileSystem.isFile(batPath)) FileSystem.createFile(batPath);
        runCMD = String.format("start cmd /c \"%s\"", batPath);
        switch (extension) {
            case "exe" -> {
                FileSystem.writeWhole(String.format("timeout /t 1\ncls\n\"%s\"\npause", path), batPath);
                return CMD.run(runCMD, "|");
            }
            case "class" -> {
                FileSystem.writeWhole(String.format("timeout /t 1\ncls\njava %s\npause", getFileName(path)), batPath);
                return CMD.run(runCMD, getParentFolder(path));
            }
            case "py" -> {
                FileSystem.writeWhole(String.format("timeout /t 1\ncls\n%s %s\npause",LANGUAGES.Python.Compiler,path),batPath);
                return CMD.run(runCMD, getParentFolder(path));
            }
            default -> {
                return null;
            }
        }
    }

    //Helper Functions;
    public static String[] getExtension(String path) {
        int limit = 0;
        for (int i = path.length() - 1; i >= 0; i--) {
            if (path.charAt(i) == '.') {
                limit = i;
                break;
            }
        }
        return new String[]{path.substring(limit + 1), path.substring(0, limit)};
    }

    public static String getParentFolder(String path) {
        int limit = 0;
        for (int i = path.length() - 1; i >= 0; i--) {
            if (path.charAt(i) == '\\' || path.charAt(i) == '/') {
                limit = i;
                break;
            }
        }
        return path.substring(0, limit + 1);
    }

    static String getFileNameWithExtension(String path) {
        int limit = 0;
        for (int i = path.length() - 1; i >= 0; i--) {
            if (path.charAt(i) == '\\' || path.charAt(i) == '/') {
                limit = i;
                break;
            }
        }
        return path.substring(limit + 1);
    }

    public static String getFileName(String path) {
        return getExtension(getFileNameWithExtension(path))[1];
    }
}
