package org.codeRunner.Scripts;

import org.codeRunner.Main;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class FileSystem {
    //User Dir
    static final String homeDir = System.getProperty("user.home");
    static final String serFilePath = homeDir + "/savedState.ser";

    //FILE IO
    public static boolean isFile(String batPath) {
        File f = new File(batPath);
        return f.isFile();
    }

    public static int getSize(String path) {
        int size = -1;
        File file = new File(path);
        if (!file.isFile()) return size;
        size = (int) (file.length());
        return size;
    }

    public static Boolean move(String source, String target) {
        File sourceFile = new File(source);
        File targetFile = new File(target);
        if (!sourceFile.isFile() || targetFile.isFile()) {
            return false;
        }
        return sourceFile.renameTo(targetFile);

    }

    public static int getLineCountInFile(String path) {
        File file = new File(path);
        int lineCount = 0;
        if (!file.isFile()) return lineCount;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while (br.readLine() != null) lineCount++;
            br.close();
            return lineCount;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getLineCountInString(String text) {

        return (int) text.lines().count();
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

    public static ImageIcon getImageIcon(String path) {
        URL url = Main.class.getResource(path);
        if (url == null) {
            System.out.println("IMAGE NOT FOUND");
            return null;
        }
//        ImageIcon imageIcon=
//        imageIcon=new ImageIcon(imageIcon.getImage().getScaledInstance(24,24, Image.SCALE_SMOOTH));
        return new ImageIcon(url);
    }

    //State Management
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
        if (!serFile.isFile()) return new State();
        try {
            FileInputStream file = new FileInputStream(serFilePath);
            ObjectInputStream outputStream = new ObjectInputStream(file);
            State state = (State) outputStream.readObject();
            outputStream.close();
            file.close();
            return state;
        } catch (FileNotFoundException e) {
            return new State();
        } catch (IOException | ClassNotFoundException e) {
            saveState(new State());
            return new State();
        }
    }

    //Browser
    public static void goToUrl(String URL){
        try {
            Desktop.getDesktop().browse(new URI(URL));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
