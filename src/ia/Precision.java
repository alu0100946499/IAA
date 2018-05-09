package ia;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Precision {

	public static void main(String[] args) {
		BufferedReader readerReal = null;
		BufferedReader readerClas = null;
	    float correctCount = 0;
	    float totalCount = 0;
		
	    try {
			readerReal = new BufferedReader(new FileReader(args[0]));
			readerClas = new BufferedReader(new FileReader(args[1]));
			
			String cadenaReal = null;
			String cadenaClas = null;
			
			while(readerReal.ready()) {
				cadenaReal = readerReal.readLine();
				cadenaClas = readerClas.readLine();
				
				if(cadenaReal.equals(cadenaClas))
					correctCount++;
				totalCount++;
			}
			System.out.println(correctCount + " - " + totalCount);
			System.out.println("El porcentaje de acierto es del " + (correctCount/totalCount * 100) + "%");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    try {
    		    readerReal.close();
    		    readerClas.close();
		    } catch (IOException e) {}
		}
        

	}

}
