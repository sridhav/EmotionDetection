/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sound;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioFileFormat.Type;

public class Capture extends JFrame {
    /**
	 * @uml.property  name="accuracy" multiplicity="(0 -1)" dimension="2"
	 */
    String accuracy[][];
  /**
 * @uml.property  name="running"
 */
protected boolean running;
  /**
 * @uml.property  name="out"
 */
ByteArrayOutputStream out;
  /**
 * @uml.property  name="capFileName"
 */
String capFileName=null;

  public Capture(String cap) {
    super("Capture Sound Demo");
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    Container content = getContentPane();
    capFileName=cap;
    final JButton capture = new JButton("Capture");
    final JButton stop = new JButton("Stop");
    final JButton play = new JButton("Play");

    capture.setEnabled(true);
    stop.setEnabled(false);
    play.setEnabled(false);

    ActionListener captureListener =
        new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        capture.setEnabled(false);
        stop.setEnabled(true);
        play.setEnabled(false);
                try {
                    captureAudio();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Capture.class.getName()).log(Level.SEVERE, null, ex);
                }
      }
    };
    capture.addActionListener(captureListener);
    content.add(capture, BorderLayout.NORTH);

    ActionListener stopListener =
        new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        capture.setEnabled(true);
        stop.setEnabled(false);
        play.setEnabled(true);
        running = false;
      }
    };
    stop.addActionListener(stopListener);
    content.add(stop, BorderLayout.CENTER);

    ActionListener playListener =
        new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        playAudio();
      }
    };
    play.addActionListener(playListener);
    content.add(play, BorderLayout.SOUTH);
  }

  private void captureAudio() throws FileNotFoundException {
    try {
      final AudioFormat format = getFormat();
      DataLine.Info info = new DataLine.Info(
        TargetDataLine.class, format);
      final TargetDataLine line = (TargetDataLine)
        AudioSystem.getLine(info);
      line.open(format);
      line.start();
      Runnable runner = new Runnable() {
        int bufferSize = (int)format.getSampleRate()
          * format.getFrameSize();
        byte buffer[] = new byte[bufferSize];
        public void run() {
          out = new ByteArrayOutputStream();
          running = true;
          int count=0;
          try {
            while (running) {
               count =
                line.read(buffer, 0, buffer.length);
              if (count > 0) {
                out.write(buffer, 0, count);
              }
            }
            ByteArrayInputStream in=new ByteArrayInputStream(out.toByteArray());
            AudioSystem.write(new AudioInputStream(in, format, out.toByteArray().length), Type.WAVE, new File("./wav/"+capFileName));
            out.close();
            String outputFile=capFileName.substring(0,capFileName.length()-4);
                    System.out.println("converting"+capFileName);
                            MainInterface x=new MainInterface();

                            for(int i=1;i<=7;i++){
                          /*      MFCC temp=new MFCC(new File("./wav/"+capFileName));
                                temp.writeToFileMFCC(new File(outputFile+"MFCC"),""+i+" ");
                                temp.writeToFileMEDC(new File(outputFile+"MEDC"),""+i+" ");



                         }*/
                        
                         x.runMFCC(new File("./wav/"+capFileName),outputFile,i);
                         x.svmCommand(outputFile, i);

              }
             accuracy=x.getAccuracy();
          //   revalidate();
                     String str="\t\tAnger\tBoredom\tDisgust\tAnxiety\tHappiness\tSadness\tNeutral\n";

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
          }         catch (FileNotFoundException ex) {
                        Logger.getLogger(Capture.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (WavFileException ex) {
                        Logger.getLogger(Capture.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException e) {
            System.err.println("I/O problems: " + e);
            System.exit(-1);
          }
        }
      };
      Thread captureThread = new Thread(runner);
      captureThread.start();
    } catch (LineUnavailableException e) {
      System.err.println("Line unavailable: " + e);
      System.exit(-2);
    }
  }

  private void playAudio() {
    try {
      byte audio[] = out.toByteArray();
      InputStream input =
        new ByteArrayInputStream(audio);
      final AudioFormat format = getFormat();
      final AudioInputStream ais =
        new AudioInputStream(input, format,
        audio.length / format.getFrameSize());
      DataLine.Info info = new DataLine.Info(
        SourceDataLine.class, format);
      final SourceDataLine line = (SourceDataLine)
        AudioSystem.getLine(info);
      line.open(format);
      line.start();

      Runnable runner = new Runnable() {
        int bufferSize = (int) format.getSampleRate()
          * format.getFrameSize();
        byte buffer[] = new byte[bufferSize];

        public void run() {
          try {
            int count;
            while ((count = ais.read(
                buffer, 0, buffer.length)) != -1) {
              if (count > 0) {
                line.write(buffer, 0, count);
              }
            }
            line.drain();
            line.close();
          } catch (IOException e) {
            System.err.println("I/O problems: " + e);
            System.exit(-3);
          }
        }
      };
      Thread playThread = new Thread(runner);
      playThread.start();
    } catch (LineUnavailableException e) {
      System.err.println("Line unavailable: " + e);
      System.exit(-4);
    }
  }

  private AudioFormat getFormat() {
    float sampleRate = 16000;
    int sampleSizeInBits = 8;
    int channels = 1;
    boolean signed = true;
    boolean bigEndian = true;
    return new AudioFormat(sampleRate,
      sampleSizeInBits, channels, signed, bigEndian);
  }

}