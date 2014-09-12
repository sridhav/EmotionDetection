/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sound;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Pavani
 */

public class SelectedTestAndTrain implements ActionListener{

    /**
	 * @uml.property  name="frame"
	 * @uml.associationEnd  
	 */
    JFrame frame;
    /**
	 * @uml.property  name="panel"
	 * @uml.associationEnd  
	 */
    JPanel panel;
    /**
	 * @uml.property  name="panel2"
	 * @uml.associationEnd  
	 */
    JPanel panel2;
    /**
	 * @uml.property  name="speakerType"
	 * @uml.associationEnd  
	 */
    JComboBox speakerType;
    /**
	 * @uml.property  name="speakerType2"
	 * @uml.associationEnd  
	 */
    JComboBox speakerType2;
    /**
	 * @uml.property  name="textCode"
	 * @uml.associationEnd  
	 */
    JComboBox textCode;
    /**
	 * @uml.property  name="emotions"
	 * @uml.associationEnd  
	 */
    JComboBox emotions;
    /**
	 * @uml.property  name="test"
	 * @uml.associationEnd  
	 */
    JButton test;
    /**
	 * @uml.property  name="train"
	 * @uml.associationEnd  
	 */
    JButton train;
    /**
	 * @uml.property  name="cal"
	 * @uml.associationEnd  readOnly="true"
	 */
    private MFCC cal;

     /**
	 * @uml.property  name="accuracy" multiplicity="(0 -1)" dimension="2"
	 */
    String accuracy[][];

    public void selectionFrame(){
        frame=new JFrame("Select-Train-Test");
        frame.setVisible(true);
        frame.setSize(700,200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String spkr[]={"03 - male, 31 years old",
        "08 - female, 34 years",
        "09 - female, 21 years",
        "10 - male, 32 years",
        "11 - male, 26 years",
        "12 - male, 30 years",
        "13 - female, 32 years",
        "14 - female, 35 years",
        "15 - male, 25 years",
        "16 - female, 31 years"};

        String text[]={
            "a01",
            "a02",
            "a04",
            "a05",
            "a07",
            "b01",
            "b02",
            "b03",
            "b09",
            "b10"
        };

        String emo[]={
         "A/W anger",
        "B/L boredom",
        "D/E disgust",
        "F/A anxiety/fear",
        "H/F happiness",
        "S/T sadness",
        "N neutral"
        };
        Container pane=frame.getContentPane();
        panel=new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        pane.setLayout(new BorderLayout());


       speakerType=new JComboBox(spkr);
        speakerType.setSelectedIndex(4);
       textCode=new JComboBox(text);
        textCode.setSelectedIndex(4);
         emotions=new JComboBox(emo);
        emotions.setSelectedIndex(4);

        test=new JButton("Create Test File");
        test.addActionListener((ActionListener) this);

        panel.add(speakerType);
        panel.add(textCode);
        panel.add(emotions);
        panel.add(test);

        panel2=new JPanel();
        panel2.setLayout(new FlowLayout());
        panel2.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        speakerType2=new JComboBox(spkr);
        speakerType2.setSelectedIndex(4);
        train=new JButton("Train Individual Sets");
        train.addActionListener( (ActionListener) this);

        panel2.add(speakerType2);
        panel2.add(train);

        frame.add(panel,BorderLayout.PAGE_START);
        frame.add(panel2,BorderLayout.PAGE_END);
        frame.pack();
      }

      public void actionPerformed(ActionEvent ae){
           // accuracy=new String[2][7];
          if(ae.getActionCommand().equals("Create Test File")){
               
              int speaker=speakerType.getSelectedIndex();
              int text=textCode.getSelectedIndex();
              int emo=emotions.getSelectedIndex();
              String file="";
              String type="";
              switch(speaker){
                  case 0: file=file+"03";
                          break;
                  case 1: file=file+"08";
                          break;
                  case 2: file=file+"09";
                          break;
                  case 3: file=file+"10";
                          break;
                  case 4: file=file+"11";
                          break;
                  case 5: file=file+"12";
                          break;
                  case 6: file=file+"13";
                          break;
                  case 7: file=file+"14";
                          break;
                  case 8: file=file+"15";
                          break;
                  case 9: file=file+"16";
                          break;
              }
              switch(text){
                  case 0: file=file+"a01";
                          break;
                  case 1: file=file+"a02";
                          break;
                  case 2: file=file+"a04";
                          break;
                  case 3: file=file+"a05";
                          break;
                  case 4: file=file+"a07";
                          break;
                  case 5: file=file+"b01";
                          break;
                  case 6: file=file+"b02";
                          break;
                  case 7: file=file+"b03";
                          break;
                  case 8: file=file+"b09";
                          break;
                  case 9: file=file+"b10";
                          break;
                  
              }
              switch(emo){
                  case 0: file=file+"W";
                            type="1 ";
                          break;
                  case 1: file=file+"L";
                            type="2 ";
                          break;
                  case 2: file=file+"E";
                            type="3 ";
                          break;
                  case 3: file=file+"A";
                            type="4 ";
                          break;
                  case 4: file=file+"F";
                            type="5 ";
                          break;
                  case 5: file=file+"T";
                            type="6 ";
                          break;
                  case 6: file=file+"N";
                            type="7 ";
                          break;
                  
              }
             
              File check=new File("./trainWavSet/" + file + "a.wav");
              if(file.length()==6 && check.exists()){
               // System.out.println("Converting MFCC  "+check.getName());
                try {
                 //   cal = new MFCC(new File("./trainWavSet/" + file + "a.wav"));
                     MainInterface x=new MainInterface();
                    for(int i=1;i<=7;i++){
                      
                         x.runMFCC(check,file,i);
                         x.svmCommand(file, i);

                    }
                    accuracy=x.getAccuracy();
                     String str="\tAnger\tBoredom\tDisgust\tAnxiety\tHappiness\tSadness\tNeutral\n";

                 for(int i=0;i<2;i++){
                    if(i==0)
                        str=str+"MFCC\t";
                    else
                        str=str+"MEDC\t";
                     for(int j=0;j<7;j++){
                         str=str+accuracy[i][j]+"\t";
                     }
                     str=str+"\n";
                 }
                 x.setDisplay(str);
                 //repaint();
                 System.out.println(str);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SelectedTestAndTrain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(SelectedTestAndTrain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (WavFileException ex) {
                    Logger.getLogger(SelectedTestAndTrain.class.getName()).log(Level.SEVERE, null, ex);
                }
               
              }
               else{
                   System.out.println("File Not Found "+check.getName());
                   CommandWindow.printToFrame("File Not Found "+check.getName());
               }
            }
         
          if(ae.getActionCommand().equals("Train Individual Sets")){
              String file="";
              File direc=new File("./trainWavSet/");
              File[] fileList;
              int speaker=speakerType2.getSelectedIndex();
              switch(speaker){
                  case 0: file=file+"03";
                          break;
                  case 1: file=file+"08";
                          break;
                  case 2: file=file+"09";
                          break;
                  case 3: file=file+"10";
                          break;
                  case 4: file=file+"11";
                          break;
                  case 5: file=file+"12";
                          break;
                  case 6: file=file+"13";
                          break;
                  case 7: file=file+"14";
                          break;
                  case 8: file=file+"15";
                          break;
                  case 9: file=file+"16";
                          break;
              }
            fileList=direc.listFiles();
             File check=new File("./svmTraining/"+file+"MFCC");
               if(check.exists()){
                   check.delete();
               }
              File check2=new File("./svmTraining/"+file+"MEDC");
               if(check2.exists()){
                   check2.delete();
               }

            File trainingFile=new File("./svmTraining/"+file+"MFCC");

            File trainingFile2=new File("./svmTraining/"+file+"MEDC");

            Training xx=new Training();
            for(int i=0;i<fileList.length;i++){
                    String temp=fileList[i].getName().substring(0,2);
                    if(temp.equals(file)){
                         System.out.println(fileList[i]);
                         xx.fileComboWrite(fileList[i],trainingFile);
                          xx.fileComboWrite(fileList[i],trainingFile2);
                      }
              }

          }
          

      }

}
