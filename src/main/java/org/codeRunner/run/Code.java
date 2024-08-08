package org.codeRunner.run;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Code {
    public static List<String> JavaKeyword = List.of("int", "class", "char", "public", "static", "void", "new", "return", "if", "else", "for", "while", "try", "catch", "import", "package", "extends", "implements", "interface", "this", "super", "final", "abstract", "synchronized", "throw", "throws", "private", "protected", "public", "null", "true", "false", "instanceof", "assert", "enum", "default", "break", "continue", "switch", "case", "do", "finally", "goto");
    public static List<String> PythonKeywords = List.of("def", "return", "if", "else", "elif", "for", "while", "try", "except", "finally", "with", "as", "import", "from", "class", "self", "lambda", "global", "nonlocal", "assert", "pass", "break", "continue", "del", "yield", "raise", "True", "False", "None", "and", "or", "not", "in", "is", "async", "await");
    public static List<String> CKeywords = List.of("int", "auto", "return", "if", "else", "for", "while", "do", "switch", "case", "break", "continue", "typedef", "struct", "union", "enum", "sizeof", "static", "volatile", "const", "extern", "inline", "goto", "register", "signed", "unsigned", "void", "char", "float", "double", "short", "long", "default");
    public static List<String> CPPKeywords = List.of("int", "auto", "return", "if", "else", "for", "while", "do", "switch", "case", "break", "continue", "typedef", "struct", "union", "enum", "sizeof", "static", "volatile", "const", "extern", "inline", "goto", "register", "signed", "unsigned", "void", "char", "float", "double", "short", "long", "default", "class", "public", "private", "protected", "namespace", "using", "virtual", "friend", "template", "try", "catch", "throw", "new", "delete", "this", "nullptr", "override", "final", "constexpr");

    public enum LANGUAGES {
        C("gcc", "c", CKeywords),
        Java("javac", "java", JavaKeyword),
        CPP("g++", "cpp",CPPKeywords),
        Python("python", "py", PythonKeywords);
        public final String Compiler;
        public final String Extension;
        public final List<String> keywords;

        LANGUAGES(String compiler, String extension, List<String> keywords) {
            this.Compiler = compiler;
            this.Extension = extension;
            this.keywords = keywords;
        }

        public String getExtension() {
            return this.Extension;
        }
    }

    public static String[] getAvailableLanguages() {
        ArrayList<String> languageList = new ArrayList<>();
        Arrays.stream(LANGUAGES.values()).forEach(e -> languageList.add(e.Extension));
        return languageList.toArray(new String[]{});
    }

    static boolean compile(String path) {
        String compileCMD = "";
        System.out.println("Compiling");
        String extension = getExtension(path)[0];
        switch (extension) {
            case "c" ->
                    compileCMD = String.format("%s \"%s\" -o \"%s\"", LANGUAGES.C.Compiler, path, getExtension(path)[1]);
            case "cpp" ->
                    compileCMD = String.format("%s \"%s\" -o \"%s\"", LANGUAGES.CPP.Compiler, path, getExtension(path)[1]);
            case "java" -> {
                compileCMD = String.format("%s %s", LANGUAGES.Java.Compiler, getFileNameWithExtension(path));
                String error = CMD.run(compileCMD, getParentFolder(path)).get(1);
                return error.isEmpty();
            }
            case "py" -> {
                return run(path).get(1).isEmpty();
            }
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
        if (!FileSystem.isFile(batPath)) FileSystem.createFile(batPath);
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
                FileSystem.writeWhole(String.format("timeout /t 1\ncls\n%s %s\npause", LANGUAGES.Python.Compiler, path), batPath);
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
