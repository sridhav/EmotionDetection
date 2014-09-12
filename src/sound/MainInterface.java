/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sound;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Pavani
 */
public class MainInterface extends JApplet implements ActionListener {

    /**
	 * Initialization method that will be called after the applet is loaded into the browser.
	 * @uml.property  name="buttons"
	 * @uml.associationEnd  
	 */
    JPanel buttons;
    public static JTextArea display;
    /**
	 * @uml.property  name="retrain"
	 * @uml.associationEnd  
	 */
    JButton retrain;
    /**
	 * @uml.property  name="testFile"
	 * @uml.associationEnd  
	 */
    JButton testFile;
    /**
	 * @uml.property  name="testMicFile"
	 * @uml.associationEnd  
	 */
    JButton testMicFile;
    /**
	 * @uml.property  name="exit"
	 * @uml.associationEnd  
	 */
    JButton exit;
    /**
	 * @uml.property  name="selectDatasetFile"
	 * @uml.associationEnd  
	 */
    JButton selectDatasetFile;
    /**
	 * @uml.property  name="displayCmdWindow"
	 * @uml.associationEnd  
	 */
    JButton displayCmdWindow;
    /**
	 * @uml.property  name="bottom"
	 * @uml.associationEnd  
	 */
    JPanel bottom;
    /**
	 * @uml.property  name="label"
	 * @uml.associationEnd  
	 */
    JLabel label;
    /**
	 * @uml.property  name="outputData"
	 */
    String outputData="";
     /**
	 * @uml.property  name="pane"
	 */
    Container pane;
     /**
	 * @uml.property  name="accuracy" multiplicity="(0 -1)" dimension="2"
	 */
    String accuracy[][]=new String[2][7];
    @Override
    public void init() {
        // TODO start asynchronous download of heavy resources

          pane=this.getContentPane();
        
          bottom=new JPanel();
  

        retrain=new JButton("Re-Train Dataset");

        testFile=new JButton("Test WAV File");

        testMicFile=new JButton("Test MIC File");

        displayCmdWindow=new JButton("Display Command Window");

        exit=new JButton("Exit Applet");

        selectDatasetFile=new JButton("Select Train Test");

        buttons=new JPanel();

         bottom=new JPanel();

        display=new JTextArea();

        display.setEditable(false);
      // buttons.setLayout(new BoxLayout(buttons,BoxLayout.PAGE_AXIS));

        // buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        FlowLayout flow=new FlowLayout();

        buttons.setLayout(flow);

        buttons.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        buttons.setSize(300,pane.getHeight());

        bottom.setLayout(new BoxLayout(bottom, BoxLayout.PAGE_AXIS));

     //    display.setLayout(new BoxLayout(display,BoxLayout.PAGE_AXIS));

         addButton(buttons,retrain);

         addButton(buttons,testFile);

         addButton(buttons,testMicFile);

         addButton(buttons,selectDatasetFile);

         addButton(buttons,displayCmdWindow);

         addButton(buttons,exit);

         setSize(900, 300);

    //    bottom.add(buttons,BorderLayout.PAGE_END);
        label=new JLabel("Output Display");

        label.setVisible(true);
        label.setAlignmentX(CENTER_ALIGNMENT);

        display.setRows(5);
        display.setAutoscrolls(true);
       // display.setAlignmentX(CENTER_ALIGNMENT);
      //  display.set

        bottom.add(label);
        bottom.add(display);

        pane.add(buttons,BorderLayout.PAGE_START);  
        pane.add(bottom,BorderLayout.PAGE_END);

        new java.util.Timer().schedule(
        new java.util.TimerTask() {
            @Override
            public void run() {
                paint();
            }
        },
        2000
        );

    }

    // TODO overwrite start(), stop() and destroy() methods

    public void addButton(JPanel panel, JButton button){
         panel.add(button);

         //button.setPreferredSize(new Dimension(200, panel.getHeight()));

         panel.add(Box.createRigidArea(new Dimension(0,10)));

         button.addActionListener(this);
         
    }

    @Override
    public void start(){
         getAppletContext().showStatus("Sridhar");
         
    }

    public void actionPerformed(ActionEvent ae){
        //display.revalidate();
        if(ae.getActionCommand().equals("Re-Train Dataset")){
           outputData="Training Button Clicked";
            CommandWindow.printToFrame(outputData);
           // display.setText(outputData+"\n");
          //  repaint();
            Training k=new Training();
            k.Train();
        }
        else if(ae.getActionCommand().equals("Test WAV File")){
           outputData="Test Wave file Button Clicked";
            CommandWindow.printToFrame(outputData);
           // display.setText(outputData+"\n");     
            TestWav k=new TestWav();
            File in=k.testWaveFile();
            if(in!=null){
            String name=in.getName();
            String outPath=name.substring(0, name.length()-4);
            System.out.println(outPath);
            try {
                for(int i=1;i<=7;i++){
                    runMFCC(in, outPath,i);
                    svmCommand(outPath,i);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WavFileException ex) {
                Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
            outputData="Accuracy Obtained See the Output Window";
            CommandWindow.printToFrame(outputData);
                accuracyDisplay();
            }
           
        }
        else if(ae.getActionCommand().equals("Test MIC File")){
             outputData="Set Your Microphone ON. Click on Capture";
             CommandWindow.printToFrame(outputData);
          //   display.setText(outputData+"\n");
             repaint();
             JFrame frame = new Capture("temp.wav");
             frame.pack();
             frame.show();

             
          
        }
        else if(ae.getActionCommand().equals("Exit Applet")){
              outputData="Exiting Applet";
             CommandWindow.printToFrame(outputData);
             destroy();
            System.exit(0);
           
        }

        else if(ae.getActionCommand().equals("Select Train Test")){
            outputData="Select data from Training Dataset";
            CommandWindow.printToFrame(outputData);
            SelectedTestAndTrain xx=new SelectedTestAndTrain();
            xx.selectionFrame();
            
        }
        else if(ae.getActionCommand().equals("Display Command Window")){
            CommandWindow.initialise();
            CommandWindow.printToFrame("Command Window Initialised");
        }

    }

    public void runMFCC(File inputFile,String outputFile,int type) throws FileNotFoundException, WavFileException, IOException{
        String outp1="./test/"+outputFile+"MFCC"+type;
        String outp2="./test/"+outputFile+"MEDC"+type;

        File check=new File(outp1);
       if(check.exists()){
           check.delete();
       }
       File check2=new File(outp2);
       if(check2.exists()){
           check2.delete();
       }

        MFCC temp=new MFCC(inputFile);

         CommandWindow.printToFrame(outputData);
        // display.setText("converting MFCC"+inputFile.getName()+"\n");
         //repaint();
         temp.writeToFileMFCC(new File(outp1),""+type+" ");
         System.out.println("Creating MFCC values for"+inputFile.getName()+" to "+outp1);
         outputData="Creating MFCC values for"+inputFile.getName()+" to "+outp1;
         //display.setText("converting MEDC"+inputFile.getName()+"\n");
        // repaint();
         temp.writeToFileMEDC(new File(outp2),""+type+" ");
         System.out.println("Creating MEDC values for"+inputFile.getName()+" to "+outp2);
         outputData="Creating MEDC values for"+inputFile.getName()+" to "+outp2;
         CommandWindow.printToFrame(outputData);
    }

    public void svmCommand(String file,int i) throws IOException{
        Svm k=new Svm();
        File x1=new File("./test/"+file+"MFCC"+i);
        File x2=new File("./test/"+file+"MEDC"+i);

        System.out.println("Predicting MFCC "+file);
         outputData="Predicting MFCC "+file;
         CommandWindow.printToFrame(outputData);
      //  display.setText("Predicting MFCC "+file+"\n");
       // repaint();
        
        String out=k.predict(x1,"prateek.model","outMFCC"+i);
       // System.out.println(out);
       accuracy[0][i-1]=out.substring(out.indexOf("=")+1,out.indexOf("(classification)"));

         String sub1=accuracy[0][i-1].substring(0,accuracy[0][i-1].indexOf("%"));
         if(sub1.contains(".")){
                  String temp=sub1.substring(sub1.indexOf("."),sub1.length());
                  String temp2=sub1.substring(0,sub1.indexOf("."));
                  if(temp.length()>3)
                         sub1=temp2+""+temp.substring(0, 3);
                  else
                         sub1=temp2+""+temp;
        }
         String sub2=accuracy[0][i-1].substring(accuracy[0][i-1].indexOf("("),accuracy[0][i-1].indexOf(")")+1);
         sub2="";

       accuracy[0][i-1]=sub1+"% "+sub2+"";

        System.out.println("Predicting MEDC "+file);
        outputData="Predicting MEDC "+file;
         CommandWindow.printToFrame(outputData);
        //display.setText("Predicting MEDC "+file+"\n");
        //repaint();
        String out2=k.predict(x2,"alltrainermedc.model","outMEDC"+i);
       // System.out.println(out2);
         accuracy[1][i-1]=out2.substring(out2.indexOf("=")+1,out2.indexOf("(classification)"));
        
         sub1=accuracy[1][i-1].substring(0,accuracy[1][i-1].indexOf("%"));
         if(sub1.contains(".")){
                  String temp=sub1.substring(sub1.indexOf("."),sub1.length());
                  String temp2=sub1.substring(0,sub1.indexOf("."));
                  if(temp.length()>3)
                         sub1=temp2+""+temp.substring(0, 3);
                  else
                         sub1=temp2+""+temp;
        }
         sub2=accuracy[1][i-1].substring(accuracy[1][i-1].indexOf("("),accuracy[1][i-1].indexOf(")")+1);

         sub2="";
       accuracy[1][i-1]=sub1+"% "+sub2+"";
    }

    public void paint() {
        
    }

    public void accuracyDisplay(){
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
         setDisplay(str);
         System.out.println(str);
    }

/*    public void accuracyDisplay2() throws FileNotFoundException, IOException{
        File[] list;
        File x=new File("./out");
        list=x.listFiles();
        for(int i=0;i<list.length;i++){
                if(list[i].getName().contains("MFCC")){
                    char str=list[i].getName().charAt(list[i].getName().length()-1);
                    int ff=Integer.parseInt(str+"");
                    accuracy[0][ff-1]=""+checkVals(list[i])+"%";
                }
                if(list[i].getName().contains("MEDC")){
                   char str=list[i].getName().charAt(list[i].getName().length()-1);
                    int ff=Integer.parseInt(str+"");
                    accuracy[1][ff-1]=""+checkVals(list[i])+"%";
                }
                }
        accuracyDisplay();


    }

    public double checkVals(File file) throws FileNotFoundException, IOException{

        BufferedReader br=new BufferedReader(new FileReader(file));
        String in="";

        int val[]=new int[2];
        double val2[]=new double[7];
        while((in=br.readLine())!=null){

           /* switch(Integer.parseInt(in)){
                case 1: val[1]++;
                        break;
                case 2: val[2]++;
                        break;
                case 3: val[3]++;
                        break;
                case 4: val[4]++;
                        break;
                case 5: val[5]++;
                        break;
                 case 6: val[6]++;
                        break;
                 case 7: val[7]++;
                        break

            }
            val[0]++;
        }

        double max=val[0];
        for(int i=1;i<7;i++){
                if(val[i]>val[i+1]){
                    max=val[i];
               }
        }
        

        

        return (double)(max*100)/(double)val[0];

    }


*/

    /**
	 * @return
	 * @uml.property  name="accuracy"
	 */
    public String[][] getAccuracy(){
        return accuracy;
    }

    public void setDisplay(String val){
        display.setText(val);
    }
}