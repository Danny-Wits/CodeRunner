package org.codeRunner.GUI.Interfaces;

import org.codeRunner.GUI.Components.Button;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Following Properties are available before load : <br>
 * 1. public JPanel panel=new JPanel(); <br>
 * 2. public String title="SETTING"; <br>
 * 3. public boolean showButton;<br>
 * <p>
 *  Following Properties are available after load : <br>
 * 1.  public Button ok = null; <br>
 * 2.   public  Button cancel = null; <br>
 * 3.   public JDialog popup = null; <br>
 * <p>
 *     **/
public abstract class SettingPane implements ActionListener {
    public Button ok = null;
    public  Button cancel = null;
    public JDialog popup = null;
    public JPanel panel=new JPanel();
    public String title="SETTING";
    public boolean showButton=true;
    public SettingPane(){
        draw();
    }
    public void load(Button ok, Button cancel,JDialog popup){
        this.ok=ok;
        this.cancel=cancel;
        this.popup=popup;
        loaded();
    }
    public JPanel getPanel(){
        return panel;
    };
    public ActionListener getActionListener( ){return this;}
    public abstract void draw();
    public abstract void saved();
    public abstract void canceled();
    public abstract void loaded();
    public void refresh(){
        panel.removeAll();
        draw();
        panel.revalidate();
    };
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==ok){
           saved();
        }if(e.getSource()==cancel){
            canceled();
        }
    };

}
