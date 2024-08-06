package org.codeRunner.GUI;

import org.codeRunner.run.Code;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;


public class FileSelector extends JFileChooser {
    Window parent;
     FileSelector(Window parent){
         this.setCurrentDirectory(new File("./"));
         this.parent=parent;
         this.setPreferredSize(new Dimension(720,540));

         FileNameExtensionFilter filter = new FileNameExtensionFilter("Source Code ", Code.getAvailableLanguages());
         this.setFileFilter(filter);
     }
     public String getFile(){
         int returnStatus = this.showOpenDialog(this.parent);
         if(returnStatus==JFileChooser.APPROVE_OPTION){
             File selectedFile=this.getSelectedFile();
             return selectedFile.getAbsolutePath();
         }
         return null;
     }
    public String saveFile(){
        int returnStatus = this.showSaveDialog(this.parent);
        if(returnStatus==JFileChooser.APPROVE_OPTION){
            File selectedFile=this.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }return null;
    }
}
