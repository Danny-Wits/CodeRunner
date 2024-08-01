package org.codeRunner.GUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;


public class FileSelector extends JFileChooser {
    Window parent=null;
     FileSelector(Window parent){
         this.setCurrentDirectory(new File("./"));
         this.parent=parent;
         FileNameExtensionFilter filter = new FileNameExtensionFilter("Source Code ","c", "cpp", "java");
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
