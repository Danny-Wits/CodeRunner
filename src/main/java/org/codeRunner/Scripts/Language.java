package org.codeRunner.Scripts;

import org.codeRunner.GUI.Window;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Language implements Serializable {

    public String name;
    public String extension;
    public String compiler;
    public List<String> keywords;
    public Color keywordColor;
    public String URL;
    public static List<Language> languageList = new ArrayList<>();
    public static String INVALID = "7732385261082445741";


    public Language(String name, String extension, String compiler, Color keywordColor, List<String> keywords, String URL) {
        this.name = name;
        this.extension = extension;
        this.compiler = compiler;
        this.keywordColor = keywordColor;
        this.keywords = keywords;
        this.URL = URL;
        if(!this.name.equals("Unknown")){
            addLanguage(extension);
        }
    }

    private void addLanguage(String extension) {
        boolean flag = true;
        for (Language language : languageList) {
            if (language.extension.equals(extension)) {
                flag = false;
                break;
            }
        }
        if (flag) {
            languageList.add(this);
        } else {
            Window.currentWindow.message("Two Languages can't have the same extension");
            this.name=INVALID;
        }
    }

    private static List<String> JavaKeyword = List.of("int", "class", "char", "public", "static", "void", "new", "return", "if", "else", "for", "while", "try", "catch", "import", "package", "extends", "implements", "interface", "this", "super", "final", "abstract", "synchronized", "throw", "throws", "private", "protected", "public", "null", "true", "false", "instanceof", "assert", "enum", "default", "break", "continue", "switch", "case", "do", "finally", "goto");
    private static List<String> PythonKeywords = List.of("def", "return", "if", "else", "elif", "for", "while", "try", "except", "finally", "with", "as", "import", "from", "class", "self", "lambda", "global", "nonlocal", "assert", "pass", "break", "continue", "del", "yield", "raise", "True", "False", "None", "and", "or", "not", "in", "is", "async", "await");
    private static List<String> CKeywords = List.of("int", "auto", "return", "if", "else", "for", "while", "do", "switch", "case", "break", "continue", "typedef", "struct", "union", "enum", "sizeof", "static", "volatile", "const", "extern", "inline", "goto", "register", "signed", "unsigned", "void", "char", "float", "double", "short", "long", "default");
    private static List<String> CPPKeywords = List.of("int", "auto", "return", "if", "else", "for", "while", "do", "switch", "case", "break", "continue", "typedef", "struct", "union", "enum", "sizeof", "static", "volatile", "const", "extern", "inline", "goto", "register", "signed", "unsigned", "void", "char", "float", "double", "short", "long", "default", "class", "public", "private", "protected", "namespace", "using", "virtual", "friend", "template", "try", "catch", "throw", "new", "delete", "this", "nullptr", "override", "final", "constexpr");

    public static Language C = new Language("C", "c", "gcc", Color.orange, CKeywords, "https://sourceforge.net/projects/gcc-win64/");
    public static Language CPP = new Language("CPP", "cpp", "g++", Color.orange, CPPKeywords, "https://sourceforge.net/projects/gcc-win64/");
    public static Language Java = new Language("Java", "java", "javaC", Color.orange, JavaKeyword, "https://www.java.com/en/download/");
    public static Language Python = new Language("Python", "py", "python", Color.orange, PythonKeywords, "https://www.python.org/downloads/");
    public static Language Unknown = new Language("Unknown", "", "cd", Color.orange, new ArrayList<>(), "www.google.com");

    public static String[] getAvailableExtensions() {
        return languageList.stream().map(e -> e.extension).toList().toArray(new String[languageList.size()]);
    }

    public static Language getLanguage(String path) {
        String extension = Code.getExtension(path)[0];
        for (Language language : languageList) {
            if (language.extension.equals(extension)) {
                return language;
            }
        }
        return Unknown;
    }

    public void setKeywordColor(Color keywordColor) {
        this.keywordColor = keywordColor;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = parseKeywordString(keywords);
    }

    public static List<String> parseKeywordString(String keyword) {
        return Arrays.stream(keyword.trim().split(",")).collect(Collectors.toList());
    }

    public String getKeywordString() {
        StringBuilder stringBuilder = new StringBuilder();
        keywords.forEach(e -> {
            stringBuilder.append(e).append(",");
        });
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    public static List<String> getLanguageNames() {
        return languageList.stream().map(e -> e.name).toList();
    }

    public static Language find(String extension) {
        for (Language language : languageList) {
            if (language.extension.equals(extension)) {
                return language;
            }
        }
        return null;

    }

    public static void remove(int index) {
        languageList.remove(index);
    }

    public static void remove(Language language) {
        languageList.remove(language);
    }
}
