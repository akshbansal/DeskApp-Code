package com.im.groupChat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;


import com.im.utils.Constants;

//public class TestChooser {
//  public static void main(String[] argv) throws Exception {
//    final JFileChooser chooser = new JFileChooser();
////    
//    chooser.setSelectedFile(new File(chooser.getCurrentDirectory(), "abc.txt"));
//    chooser.addPropertyChangeListener(new PropertyChangeListener() {
//      public void propertyChange(PropertyChangeEvent evt) {
//        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
//          JFileChooser chooser = (JFileChooser) evt.getSource();
//          File oldDir = (File) evt.getOldValue();
//          File newDir = (File) evt.getNewValue();
//
//          File curDir = chooser.getCurrentDirectory();
//          chooser.setSelectedFile(new File(newDir, "abc.txt"));
//          if(Constants.showConsole) System.out.println("oldDir;;;"+oldDir);
//          if(Constants.showConsole) System.out.println("newDir;;;"+newDir);
//          if(Constants.showConsole) System.out.println("curDir;;;"+curDir);
//        }
//      }
//    });
//    JFrame f = new JFrame();
//    f.add(chooser);
//    f.setVisible(true);
//  }
//  
//}
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
 
public class TestChooser {
 
    public static void main(String[] args) throws IOException {
        //text file, should be opening in default text editor
        File file = new File("D:\\testingdownloadfile\\test.txt");
         
        //first check if Desktop is supported by Platform or not
        if(!Desktop.isDesktopSupported()){
            if(Constants.showConsole) System.out.println("Desktop is not supported");
            return;
        }
         
        Desktop desktop = Desktop.getDesktop();
        if(file.exists()) desktop.open(file);
         
        //let's try to open PDF file
        file = new File("/Users/pankaj/java.pdf");
        if(file.exists()) desktop.open(file);
    }
 
}
