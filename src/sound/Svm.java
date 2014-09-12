/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sound;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Pavani
 */
public class Svm {

    /**
	 * @uml.property  name="exec"
	 */
    Process exec;
    /**
	 * @uml.property  name="out"
	 */
    String out="";
    /**
	 * @uml.property  name="in"
	 */
    BufferedReader in;
    public String train(String file) throws IOException{
        exec=Runtime.getRuntime().exec("./svm/svm-train -s 3 -p 0.1 -t 0 ./svmTraining/"+file);
        in=new BufferedReader(new InputStreamReader(exec.getInputStream()));
        String temp;
        while((temp=in.readLine())!=null)
            out=out+temp;
        return out;
    }

    public String predict(File file,String trainer,String out) throws IOException{
        exec=Runtime.getRuntime().exec("./svm/svm-predict ./test/"+file.getName()+" ./svmTraining/"+trainer+ " ./out/"+out);
        in=new BufferedReader(new InputStreamReader(exec.getInputStream()));
        String temp;
        while((temp=in.readLine())!=null)
            out=out+temp;
        return out;
    }

}
