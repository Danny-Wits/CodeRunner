package org.codeRunner.run;

import java.util.List;

public class Code {

    enum compiler {
        C("gcc"),
        CPP("g++"),
        JAVA("javac");
        final String value;

        compiler(String value) {
            this.value = value;
        }
    }

    static boolean compile(String path) {
        String compileCMD = "";
        System.out.println("Compiling");
        String extension = getExtension(path)[0];
        switch (extension) {
            case "c" ->
                    compileCMD = String.format("%s \"%s\" -o \"%s\"", compiler.C.value, path, getExtension(path)[1]);
            case "cpp" ->
                    compileCMD = String.format("%s \"%s\" -o \"%s\"", compiler.CPP.value, path, getExtension(path)[1]);
            case "java" -> {
                compileCMD = String.format("%s %s", compiler.JAVA.value, getFileNameWithExtension(path));
                String error = CMD.run(compileCMD, getParentFolder(path)).get(1);
                return error.isEmpty();
            }
        }
        System.out.println(compileCMD);
        String error = CMD.run(compileCMD, "|").get(1);
        return error.isEmpty();
    }

    static String run(String path) {
        String extension = getExtension(path)[0];
        System.out.println("Running");
        String runCMD;
        String batPath = System.getProperty("java.io.tmpdir") + "run.bat";
        FileSystem.createFile(batPath);
        runCMD = String.format("start cmd /c \"%s\"", batPath);
        switch (extension) {
            case "exe" -> {
                FileSystem.writeWhole(String.format("timeout /t 1\ncls\n\"%s\"\npause", path), batPath);
                List<String> result = CMD.run(runCMD, "|");
                return result.getFirst();
            }
            case "class" -> {
                FileSystem.writeWhole(String.format("timeout /t 1\ncls\njava %s\npause", getFileName(path)), batPath);
                List<String> result = CMD.run(runCMD, getParentFolder(path));
                return result.getFirst();
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

    static String getParentFolder(String path) {
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
