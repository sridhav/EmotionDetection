/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sound;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author Pavani
 */
public class TestWav {
   /**
 * @uml.property  name="testFile"
 */
File testFile=null;
   /**
 * @uml.property  name="files"
 * @uml.associationEnd  
 */
JFileChooser files;
    public File testWaveFile(){
       files=new JFileChooser();
       files.setCurrentDirectory(new File("."));
       files.setDialogTitle("Select File");
       files.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
     //  frame.add(files);
       //files.setVisible(true);
      
      
    //   files.showOpenDialog(null);
        if(files.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
          testFile=files.getSelectedFile();
        //  files.setVisible(false);

       }
       
       return testFile;
    }

   
}
