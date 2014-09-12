/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sound;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JApplet;
import javax.swing.JButton;

import javax.swing.JFileChooser;
import javax.swing.JFrame;


/**
 *
 * @author Pavani
 */
public class Training{
    //Button open;
    
    /**
	 * @uml.property  name="frame"
	 * @uml.associationEnd  readOnly="true"
	 */
    JFrame frame;
    /**
	 * @uml.property  name="directory"
	 */
    File directory=null;
    /**
	 * @uml.property  name="fileList" multiplicity="(0 -1)" dimension="1"
	 */
    File[] fileList;
    /**
	 * @uml.property  name="outputFile"
	 */
    File outputFile;
    /**
	 * @uml.property  name="trainingFile"
	 */
    File trainingFile;
     /**
	 * @uml.property  name="trainingFile2"
	 */
    File trainingFile2;
    //  File fileName
    
    /*public void init(){
        open=new Button("Select Folder");
        open.addActionListener((ActionListener) this);
        add(open);

    }*/

    
    public void Train(){
     /*   frame=new JFrame("File Chooser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(700, 500);*/

       JFileChooser files=new JFileChooser();
       files.setCurrentDirectory(new File("."));
       files.setDialogTitle("Select File");
       files.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
     //  frame.add(files);
       //files.setVisible(true);
       files.updateUI();
       CommandWindow.printToFrame("Select Folder that is to be Trained");
       if(files.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
          directory=files.getCurrentDirectory();  
         
       }
       if(directory!=null){
       fileList=directory.listFiles();
       CommandWindow.printToFrame("File List Obtained");
       File check=new File("./svmTraining/AllTrainerMFCC");
       if(check.exists()){
           check.delete();
       }
       File check2=new File("./svmTraining/AllTrainerMEDC");
       if(check2.exists()){
           check2.delete();
       }
       trainingFile=new File("./svmTraining/AllTrainerMFCC");

       trainingFile2=new File("./svmTraining/AllTrainerMEDC");



       for(int i=0;i<fileList.length;i++){

                fileComboWrite(fileList[i],trainingFile);
                fileComboWriteMEDC(fileList[i],trainingFile2);
       }
       try {
            System.out.println("MFCC SVM Training for "+trainingFile.getName());
            CommandWindow.printToFrame("MFCC SVM Training for "+trainingFile.getName());
            svmTrain(trainingFile.getName());
             System.out.println("Successful MFCC SVM Training for "+trainingFile.getName());
            CommandWindow.printToFrame("Successful MFCC SVM Training for "+trainingFile.getName());
           
            System.out.println("MEDC SVM Training for "+trainingFile2.getName());
            CommandWindow.printToFrame("MEDC SVM Training for "+trainingFile2.getName());
            svmTrain(trainingFile2.getName());
            System.out.println("Successful MEDC SVM Training for "+trainingFile2.getName());
            CommandWindow.printToFrame("Successful MEDC SVM Training for "+trainingFile2.getName());

        } catch (IOException ex) {
            Logger.getLogger(Training.class.getName()).log(Level.SEVERE, null, ex);
        }

        }
    }
    public void fileComboWrite(File fileList,File trainingFile){
                BufferedWriter ds = null;
                try {
                    ds = new BufferedWriter(new FileWriter(trainingFile,true));
                } catch (IOException ex) {
                    Logger.getLogger(Training.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {

                String ext;
                int index=fileList.getName().lastIndexOf(".");
                ext=fileList.getName().substring(index+1);
                // System.out.println(ext);
                if(ext.equals("wav") /*&& fileList[i].getName().substring(0, 2).equals("03")*/){
                    System.out.println("Appending MFCC Values of "+fileList.getName()+" to "+trainingFile.getName());
                    CommandWindow.printToFrame("Appending MFCC Values of "+fileList.getName()+" to "+trainingFile.getName());
                    MFCC z;
                    double[][] mfccVal=null;
                   
                    try {
                        z = new MFCC(fileList);
                        mfccVal=z.getFinalMFCC();
                       
                    } catch (WavFileException ex) {
                        Logger.getLogger(Training.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String zz=null;
                    //ds.write("val");
                    switch(fileList.getName().charAt(5)){
                        case 'W': zz="1 ";
                                    break;
                        case 'L': zz="2 ";
                                    break;
                        case 'E': zz="3 ";
                                    break;
                        case 'A': zz="4 ";
                                    break;
                        case 'F': zz="5 ";
                                    break;
                        case 'T':zz="6 ";
                                    break;
                         case 'N': zz="7 ";
                                    break;
                        default: zz="";
                                 break;
                    }
                   // outputFile=new File("./svmTraining/"+fileList[i].getName().substring(0, index));


                        //DataOutputStream ds=new DataOutputStream(new FileOutputStream(outputFile));
                       // ds.write(zz);
                       // int l=0;]
                       
                        for(int k=0;k<mfccVal.length;k++){
                               ds.write(zz);
                                int ll=0;
                            for(int j=0;j<mfccVal[k].length;j++){
                                   // ds.writeDouble(mfccVal[k][j]);

                                   String str=""+mfccVal[k][j];
                                   if(str.equals("NaN")||(mfccVal[k][j]==Double.NEGATIVE_INFINITY)|| (mfccVal[k][j]==Double.POSITIVE_INFINITY)){
                                       ds.write(ll+++":"+0.0+" ");
                                }
                                   else
                                   ds.write(ll+++":"+mfccVal[k][j]+" ");
                                   
                                   
                            }
                            //ds.write("\n");
                               ds.write("\n");
                           //  if(k<mfccVal.length-1)
                               //   ds.write(ll+++":"+firstDiff[k]+" ");
                          //    if(k<mfccVal.length-2)
                              //    ds.write(ll+++":"+secondDiff[k]+" ");
                           
                        }
                     

                     /* for(int j=0;j<medc.length;j++){
                               // ds.writeDouble(medc[j]);

                               ds.write(" "+l+++":"+medc[j]);
                        }*/

                }
                else{
                    System.out.println("Not a Wave File"+fileList.getName());
                    CommandWindow.printToFrame("Not a Wave File"+fileList.getName());
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Training.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Training.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                    ds.close();
                } catch (IOException ex) {
                    Logger.getLogger(Training.class.getName()).log(Level.SEVERE, null, ex);
                }
        
    }

     public void fileComboWriteMEDC(File fileList,File trainingFile){
                BufferedWriter ds = null;
                try {
                    ds = new BufferedWriter(new FileWriter(trainingFile,true));
                } catch (IOException ex) {
                    Logger.getLogger(Training.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {

                String ext;
                int index=fileList.getName().lastIndexOf(".");
                ext=fileList.getName().substring(index+1);
                // System.out.println(ext);
                if(ext.equals("wav") /*&& fileList[i].getName().substring(0, 2).equals("03")*/){
                    System.out.println("Appending MEDC Values of "+fileList.getName()+" to "+trainingFile.getName());
                      CommandWindow.printToFrame("Appending MEDC Values of "+fileList.getName()+" to "+trainingFile.getName());
                    MFCC z;
                    double[][] mfccVal=null;
                    double[] firstDiff=null;
                    double[] secondDiff=null;
                    try {
                        z = new MFCC(fileList);
                        mfccVal=z.getFinalMFCC();
                       
                        firstDiff=z.getFirstDiff();
                        secondDiff=z.getSecondDiff();
                    } catch (WavFileException ex) {
                        Logger.getLogger(Training.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String zz=null;
                    //ds.write("val");
                    switch(fileList.getName().charAt(5)){
                        case 'W': zz="1 ";
                                    break;
                        case 'L': zz="2 ";
                                    break;
                        case 'E': zz="3 ";
                                    break;
                        case 'A': zz="4 ";
                                    break;
                        case 'F': zz="5 ";
                                    break;
                        case 'T':zz="6 ";
                                    break;
                         case 'N': zz="7 ";
                                    break;
                        default: zz="";
                                 break;
                    }
                   // outputFile=new File("./svmTraining/"+fileList[i].getName().substring(0, index));


                        //DataOutputStream ds=new DataOutputStream(new FileOutputStream(outputFile));
                       // ds.write(zz);
                       // int l=0;]
                        
                        for(int k=0;k<mfccVal.length-2;k++){
                               ds.write(zz);
                                int l=0;
                            //ds.write("\n");
                              ds.write(l+++":"+firstDiff[k]+" ");
                                if(k<mfccVal.length-2)
                                    ds.write(l+++":"+secondDiff[k]+" ");
                                else
                                    ds.write(l+++":"+0.0+" ");
                             ds.write("\n");
                           //  if(k<mfccVal.length-1)
                               //   ds.write(ll+++":"+firstDiff[k]+" ");
                          //    if(k<mfccVal.length-2)
                              //    ds.write(ll+++":"+secondDiff[k]+" ");

                        }


                     /* for(int j=0;j<medc.length;j++){
                               // ds.writeDouble(medc[j]);

                               ds.write(" "+l+++":"+medc[j]);
                        }*/

                }
                else{
                    System.out.println("Not a Wave File"+fileList.getName());
                    CommandWindow.printToFrame("Not a Wave File"+fileList.getName());
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Training.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Training.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                    ds.close();
                } catch (IOException ex) {
                    Logger.getLogger(Training.class.getName()).log(Level.SEVERE, null, ex);
                }
           
    }
    private void svmTrain(String file) throws IOException{
       // File trainer=new File(file);
        Svm svm=new Svm();
        String out=svm.train(file);
        System.out.println(out);

    }

}
