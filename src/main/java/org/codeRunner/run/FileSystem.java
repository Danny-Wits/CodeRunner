package org.codeRunner.run;

import org.codeRunner.GUI.CodePanel;
import org.codeRunner.Main;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileSystem {
    //User Dir
    static final String homeDir=System.getProperty("user.home");
    static final String serFilePath =homeDir+"/savedState.ser";
    //FILE IO
    public static boolean isFile(String batPath) {
        File f = new File(batPath);
        return f.isFile();
    }
    public static int getSize(String path){
        int size=-1;
        File file = new File(path);
        if (!file.isFile())return size;
        size=(int)(file.length());
        return size;
    }
    public static int getLineCount(String path){
        File file= new File(path);
        int lineCount=0;
        if(!file.isFile())return lineCount;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while (br.readLine()!=null)lineCount++;
            br.close();
            return lineCount;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public static String readWhole(String path) {
        File file = new File(path);
        StringBuilder code = new StringBuilder();
        if (!file.isFile()) {
            return "FILE NOT FOUND";
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String outputLine;
            while ((outputLine = bufferedReader.readLine()) != null) {
                code.append(outputLine);
                code.append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            return "FILE NOT FOUND";
        }
        return code.toString();
    }

    public static boolean writeWhole(String content, String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        }
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
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

    public static ImageIcon getImage(String path){
        URL url = Main.class.getResource(path);
        if (url == null) return null;
        return new ImageIcon(url);
    }

    //StateManagement
    public static State createState(List<CodePanel> list) {
        System.out.println("Creating State");
        return new State(list.stream().map(e -> e.path).collect(Collectors.toList()));
    }

    public static void saveState(State state) {
        System.out.println("Saving State");
        try {
            FileOutputStream file = new FileOutputStream(serFilePath);
            ObjectOutputStream outputStream = new ObjectOutputStream(file);
            outputStream.writeObject(state);
            outputStream.close();
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static State loadState() {
        System.out.println("Loading State");
        File serFile = new File(serFilePath);
        if(!serFile.isFile())return new State(new ArrayList<>());
        try {
            FileInputStream file = new FileInputStream(serFilePath);
            ObjectInputStream outputStream = new ObjectInputStream(file);
            State state = (State) outputStream.readObject();
            outputStream.close();
            file.close();
            return state;
        } catch (FileNotFoundException e) {
            return new State(new ArrayList<String>());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
