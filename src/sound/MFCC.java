/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sound;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Sridhar Vemula
 */
public final class MFCC {
    /**
	 * @uml.property  name="inputSignal" multiplicity="(0 -1)" dimension="1"
	 */
    public short inputSignal[]=null;
    //Sampling Rate
    private final static double samplingRate=16000.0;
    //Frame Length
    private final static int frameLength=512;
    //Shift Interval for Windowing
    /**
	 * @uml.property  name="shiftInterval"
	 */
    public int shiftInterval=frameLength/2;
    
    /**
	 * @uml.property  name="frames" multiplicity="(0 -1)" dimension="2"
	 */
    private double frames[][];
    
    private final static double a=0.95;

    private final static double hammingConstant=0.46;
    
    private final static int numMelFilters=23;
    
    private final static double lowerFrequency=133.334;
    
    private final static double upperFrequency=6855.4976;
    
    /**
	 * @uml.property  name="numCep"
	 */
    public int numCep=13;
    
    /**
	 * @uml.property  name="mFCCk" multiplicity="(0 -1)" dimension="2"
	 */
    private double MFCCk[][];

  //  private double MFCCk2[][];

    /**
	 * @uml.property  name="mEDC" multiplicity="(0 -1)" dimension="1"
	 */
    private double MEDC[];

    /**
	 * @uml.property  name="firstDiff" multiplicity="(0 -1)" dimension="1"
	 */
    private double firstDiff[];

    /**
	 * @uml.property  name="secondDiff" multiplicity="(0 -1)" dimension="1"
	 */
    private double secondDiff[];

    /**
	 * @uml.property  name="finalMEDC" multiplicity="(0 -1)" dimension="1"
	 */
    private double finalMEDC[];

    /**
	 * @uml.property  name="fFT"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    public fft FFT;

    public MFCC(File inputFile) throws FileNotFoundException, IOException, WavFileException {
        //shiftInterval=getShiftInterval(inputFile);
        
        double[] inputSignal2=null;
            try {
                convertToShort(inputFile);
            } catch (Exception ex) {
                Logger.getLogger(MFCC.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        inputSignal2=preProcess(inputSignal);

        framing(inputSignal2);

        hammingWindow();
        int k=0;
        
        MFCCk=new double[frames.length][numCep];
        MEDC=new double[frames.length];
        firstDiff=new double[frames.length-1];
        secondDiff=new double[frames.length-2];
        finalMEDC=new double[2*frames.length-3];
      //   MFCC2 l=new MFCC2();
      //  MFCCk2=l.process(inputSignal, samplingRate);
        for(int i=0;i<frames.length;i++){

            FFT=new fft();

            double[] bin=magnitudeSpectrum(frames[i]);

            int[] cbin=centerFrequency();

            double[] fbank=melFilter(bin,cbin);

            double[] trans=nonLinearTransform(fbank);

            double[] cepc=cepCoeff(trans);

            for(int j=0;j<numCep;j++){
                MFCCk[i][j]=cepc[j];
             //  System.out.println("MFCC"+k+++" "+MFCCk[i][j]+"\t"+frames.length);
            }

            MEDC[i]=getMEDC2(fbank);
             }
             k=0;
             for(int i=0;i<MEDC.length-1;i++){
                   firstDiff[i]=MEDC[i+1]-MEDC[i];
                   finalMEDC[k]=firstDiff[i];
                 // System.out.println("MEDC"+k+" "+finalMEDC[k]+"\t"+frames.length);
                   k++;
            }

            for(int i=0;i<MEDC.length-2;i++){
                    secondDiff[i]=firstDiff[i+1]-firstDiff[i];
                    finalMEDC[k]=secondDiff[i];
                 //  System.out.println("MEDC"+k+" "+finalMEDC[k]+"\t"+frames.length);
                   k++;
             }

            
    }

  /*  private int getShiftInterval(File inputFile) throws IOException, WavFileException{
        WavFile wav;
        double numFrames;
        wav=WavFile.openWavFile(inputFile);

        numFrames=wav.getFrames();
        
        double val=numFrames/samplingRate;

        int shiftInt=(int) Math.ceil((double)(frameLength/4)*val);
        System.out.println(shiftInt);
        return shiftInt;
    }*/

    /**
	 * @return
	 * @uml.property  name="firstDiff"
	 */
    public double[] getFirstDiff(){
        return firstDiff;
    }
    /**
	 * @return
	 * @uml.property  name="secondDiff"
	 */
    public double[] getSecondDiff(){
        return secondDiff;
    }

    public void writeToFileMEDC(File outputFile,String type) throws IOException{

       if(outputFile.exists()){
           outputFile.delete();
       }

          BufferedWriter ds = null;
                try {
                    ds = new BufferedWriter(new FileWriter(outputFile));
                } catch (IOException ex) {
                    Logger.getLogger(Training.class.getName()).log(Level.SEVERE, null, ex);
                }
               
       for(int k=0;k<MFCCk.length-1;k++){
             int l=0;
             ds.write(type);

         /* for(int j=0;j<MFCCk[k].length;j++){
                   // ds.writeDouble(mfccVal[k][j]);
                   //int l=0;
                   String str=""+MFCCk[k][j];
                   if(str.equals("NaN")||(MFCCk[k][j]==Double.NEGATIVE_INFINITY)|| (MFCCk[k][j]==Double.POSITIVE_INFINITY)){
                       ds.write(l+++":"+0.0+" ");
                }
                   else
                   ds.write(l+++":"+MFCCk[k][j]+" ");


            }*/
              
               
                ds.write(l+++":"+firstDiff[k]+" ");
                if(k<MFCCk.length-2)
                    ds.write(l+++":"+secondDiff[k]+" ");
                else
                    ds.write(l+++":"+0.0+" ");
                  ds.write("\n");
        }
           /*for(int j=0;j<medc.length;j++){
               // ds.writeDouble(medc[j]);

               ds.write(" "+l+++":"+medc[j]);
        }*/
     //   ds.write("\n");

        ds.close();
        }

    public void writeToFileMFCC(File outputFile,String type) throws IOException{
             if(outputFile.exists()){
           outputFile.delete();
       }
          BufferedWriter ds = null;
                try {
                    ds = new BufferedWriter(new FileWriter(outputFile));
                } catch (IOException ex) {
                    Logger.getLogger(Training.class.getName()).log(Level.SEVERE, null, ex);
                }
                
       for(int k=0;k<MFCCk.length;k++){
            int l=0;
             ds.write(type);
              
           for(int j=0;j<MFCCk[k].length;j++){
                   // ds.writeDouble(mfccVal[k][j]);
                   //int l=0;
                   String str=""+MFCCk[k][j];
                   if(str.equals("NaN")||(MFCCk[k][j]==Double.NEGATIVE_INFINITY)|| (MFCCk[k][j]==Double.POSITIVE_INFINITY)){
                       ds.write(l+++":"+0.0+" ");
                }
                   else
                   ds.write(l+++":"+MFCCk[k][j]+" ");

                   
            }
              ds.write("\n");
               // if(k<MFCCk.length-1)
               //     ds.write(l+++":"+firstDiff[k]+" ");
               // if(k<MFCCk.length-2)
                  //  ds.write(l+++":"+secondDiff[k]+" ");
               
        }
           /*for(int j=0;j<medc.length;j++){
               // ds.writeDouble(medc[j]);

               ds.write(" "+l+++":"+medc[j]);
        }*/
     //   ds.write("\n");
         
        ds.close();
        }



    /**
	 * @return
	 * @uml.property  name="finalMEDC"
	 */
    public double[] getFinalMEDC(){
        return finalMEDC;
    }

    public double[][]getFinalMFCC(){
        return MFCCk;
    }

    private double getMEDC(double[] fbank){
        double energy=0;
        for (int i=0;i<fbank.length;i++){
            double temp=1;
            for(int j=0;j<fbank.length;j++){
                if(j!=i) 
                temp=temp*Math.log(fbank[i]/fbank[j]);
            }
            energy=energy+(fbank[i]/temp);
        }
        energy=energy*fact(fbank.length-1);

      /*  for(int i=0;i<fbank.length-1;i++){
               firstDiff[i]=energy[i+1]-energy[i];
               combined[k]=firstDiff[i];
               k++;
        }

        for(int i=0;i<fbank.length-2;i++){
                secondDiff[i]=firstDiff[i+1]-firstDiff[i];
                combined[k]=secondDiff[i];
               k++;
        }*/
        
        return energy;
    }

    private double getMEDC2(double[] fbank){

        double energy=0;
        for(int i=0;i<fbank.length;i++){
            energy=energy+Math.log(fbank[i]);
        }
        energy=energy/fbank.length;
        energy=Math.exp(energy);

        return energy;

    }

    private double fact(int val){
        double factorial=1;
        for(int i=1;i<=val;i++)
            factorial=factorial*i;
        return factorial;
    }

        private double[] cepCoeff(double[] trans){

            double[] cep=new double[numCep];

            for(int i=0;i<cep.length;i++){
                for(int j=1;j<=numMelFilters;j++){
                    cep[i]+=trans[j-1]*Math.cos(Math.PI*i/numMelFilters*(j-0.5));
                }
            }
            return cep;
        }

        private double[] nonLinearTransform(double[] fbank){
            double[] temp=new double[fbank.length];
            double FLOOR=-50;
            for(int i=0;i<fbank.length;i++){
                temp[i]=Math.log(fbank[i]);
                if(fbank[i]<FLOOR) temp[i]=FLOOR;
            }
            return temp;
        }

        public double[] melFilter(double bin[], int cbin[]){
        double temp[] = new double[numMelFilters + 2];

        for (int k = 1; k <= numMelFilters; k++){
            double num1 = 0, num2 = 0;

            for (int i = cbin[k - 1]; i <= cbin[k]; i++){
                num1 += ((i - cbin[k - 1] + 1) / (cbin[k] - cbin[k-1] + 1)) * bin[i];
            }

            for (int i = cbin[k] + 1; i <= cbin[k + 1]; i++){
                num2 += (1 - ((i - cbin[k]) / (cbin[k + 1] - cbin[k] + 1))) * bin[i];
            }

            temp[k] = num1 + num2;
        }

        double fbank[] = new double[numMelFilters];
        for (int i = 0; i < numMelFilters; i++){
            fbank[i] = temp[i + 1];
        }

        return fbank;
    }

    private int[] centerFrequency(){

        int cbin[]=new int[numMelFilters+2];

        cbin[0]=(int)(lowerFrequency/samplingRate*frameLength);
        cbin[cbin.length-1]=(frameLength/2);

        for(int i=1;i<=numMelFilters;i++){
            double[] mel=new double[2];
            mel[0]=2595*(Math.log(1+lowerFrequency/700)/Math.log(10));
            mel[1]=2595*(Math.log(1+(samplingRate/2)/700)/Math.log(10));

            double temp=mel[0]+((mel[1]-mel[0])/(numMelFilters+1))*i;

            double temp2=700*(Math.pow(10,temp/2595)-1);
            cbin[i]=(int)Math.round(temp2/samplingRate*frameLength);

        }
        return cbin;
    }

    private double[] magnitudeSpectrum(double[] frame){
        double[] magSpectrum=new double[frame.length];
        
        fft.computeFFT(frame);
        
        for(int i=0;i<frame.length;i++){
            magSpectrum[i]=Math.pow(fft.real[i]*fft.real[i]+fft.imag[i]*fft.imag[i], 0.5);
        }
      return magSpectrum;  
    }



    private void hammingWindow(){

        double w[]=new double[frameLength];
       // System.out.println("hamming Window");
        for(int i=0;i<frameLength;i++){
            w[i]=(double)(1-hammingConstant)-(double)(hammingConstant*(Math.cos((2*Math.PI*i)/(frameLength-1))));
        }

        for(int i=0;i<frames.length;i++){
            for(int j=0;j<frameLength;j++){
                frames[i][j]*=w[j];

                  //System.out.println(frames[i][j]);
            }
        }



    }


    private void framing(double[] inputSignal2) throws ArrayIndexOutOfBoundsException{

        double numFrames=(((double)inputSignal2.length/(double)(frameLength-shiftInterval)));

        if(numFrames/(int)(numFrames)!=1){
            numFrames=numFrames+1;
        }

       // System.out.println(numFrames);

        double[] paddedSignal=new double[(int)numFrames*frameLength];

        for(int i=0;i<inputSignal2.length;i++){
            paddedSignal[i]=inputSignal2[i];
        }

      //  System.out.println("fRAMING");
     //   System.out.println(inputSignal2.length);
        frames=new double[(int)numFrames][frameLength];
        for(int i=0;i<(int)numFrames;i++){
            for(int j=0;j<frameLength;j++){
                frames[i][j]=paddedSignal[i*(frameLength-shiftInterval)+j];
    //            System.out.println(frames[i][j]);
            }
        }


    }


    private double[] preProcess(short[] inputSignal){
        double[] outputSignal=new double[inputSignal.length];
       //  System.out.println("PreProcess");
        for(int i=1;i<inputSignal.length;i++){
            outputSignal[i]=inputSignal[i]-a*inputSignal[i-1];
         //   System.out.println(outputSignal[i]);
        }
        return outputSignal;
    }



    private void convertToShort(File inputFile) throws FileNotFoundException, IOException {
     //System.out.println("Conversion");
        FileInputStream in=new FileInputStream(inputFile);
        long length=inputFile.length();
        byte byteArray[]=new byte[(int)length];

        int off=0;
        int red=0;
        while(off<byteArray.length &&(red=in.read(byteArray,off,byteArray.length-off))>0){
            off+=red;
        }
        in.close();

        inputSignal=new short[(byteArray.length)/2];

        for(int i=0;i<(byteArray.length/2);i++){
            short MSB=(short)byteArray[(2*i)+1];
            short LSB=(short)byteArray[(2*i)];
            inputSignal[i]=(short)((MSB<<8)|(255&LSB));
        }
    }

    double[][] getMFCC(){
        return MFCCk;
    }


}
