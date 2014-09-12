/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sound;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Pavani
 */

public class CommandWindow {
    public static JFrame frame=null;
    public static JScrollPane sc;
    public static JTextArea ta;
    public static void printToFrame(String val){
        if(frame!=null){
        ta.append(val+"\n");
        }
        else
            initialise();
    }

    public static void initialise(){
  
            frame=new JFrame("Command Wndow");
            frame.setLayout(new BorderLayout());
            ta=new JTextArea(5,60);
            sc=new JScrollPane(ta);
            frame.add(sc,BorderLayout.LINE_END);
            frame.setVisible(true);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

}
