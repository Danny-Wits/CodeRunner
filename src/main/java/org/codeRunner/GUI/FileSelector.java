package org.codeRunner.GUI;

import org.codeRunner.Scripts.Language;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Arrays;


public class FileSelector extends JFileChooser {
    Window parent;
     FileSelector(Window parent){
         this.setCurrentDirectory(new File("./"));
         this.parent=parent;
         this.setPreferredSize(new Dimension(720,540));

     }
     public String getFile(String title){
         String[]languageList=Language.getAvailableExtensions();
         FileNameExtensionFilter filter = new FileNameExtensionFilter("Source Code | "+Arrays.toString(languageList),languageList );
         this.setFileFilter(filter);
         this.setDialogTitle(title);
         int returnStatus = this.showOpenDialog(this.parent);
         if(returnStatus==JFileChooser.APPROVE_OPTION){
             File selectedFile=this.getSelectedFile();
             return selectedFile.getAbsolutePath();
         }
         return null;
     }
    public String saveFile(String title){
        String[]languageList=Language.getAvailableExtensions();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Source Code | "+Arrays.toString(languageList),languageList );
        this.setFileFilter(filter);
        this.setDialogTitle(title);
        int returnStatus = this.showSaveDialog(this.parent);
        if(returnStatus==JFileChooser.APPROVE_OPTION){
            File selectedFile=this.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }return null;
    }
}
