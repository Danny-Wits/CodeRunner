package org.codeRunner.run;

import java.io.*;

public class FileSystem {

    public static String readWhole(String path){
        File file = new File(path);
        StringBuilder code = new StringBuilder();
        if (!file.isFile()) {
            return "FILE NOT FOUND";
        }
        try {
            BufferedReader bufferedReader=new BufferedReader(new FileReader(file));
            String outputLine;
            while ((outputLine=bufferedReader.readLine())!=null){
                code.append(outputLine);
                code.append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            return "FILE NOT FOUND";
        }
        return code.toString();
    }
    public static boolean writeWhole(String content,String path){
        File file = new File(path);
        StringBuilder code = new StringBuilder();
        if (!file.isFile()) {
            return false;
        }
        try {
            BufferedWriter bufferedWriter= new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static void createFile(String path) {
            File file = new File(path);
            try {
                file.createNewFile();
            } catch (IOException e) {
            throw new RuntimeException(e);
            }
    }
}
