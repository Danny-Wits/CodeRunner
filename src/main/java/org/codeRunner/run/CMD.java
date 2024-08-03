package org.codeRunner.run;

import org.codeRunner.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CMD {
    static List<String> run(String cmd,String dir){
        List<String>log=new ArrayList<>();
        if (cmd.isEmpty())return log;
        System.out.println("/c "+cmd);
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe","/c",cmd);
        if(!dir.equals("|")){
            processBuilder.directory(new File(dir));
        }
        try {
            Process process = processBuilder.start();
            BufferedReader outputReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String outputLine;
            while ((outputLine=outputReader.readLine())!=null){
                output.append(outputLine);
                output.append("\n");
            }
            BufferedReader errorReader =new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder error=new StringBuilder();
            String errorLine;
            while ((errorLine = errorReader.readLine())!=null){
                error.append(errorLine);
                error.append("\n");
            }
            log.add(output.toString());
            log.add(error.toString());
            if(!error.toString().isEmpty())Main.window.errorLog.setError(error.toString());
            return log;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
