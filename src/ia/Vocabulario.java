package ia;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Programa para generar el vocabulario con el que se trabaja.
 * Dentro del vocabulario se eliminan las url's, los signos de puntuación
 * como comas, puntos, etc... excepto el uso del genitivo sajón en palabras inglesas,
 * así como contracciones y palabra compuestas.
 * @author Javier Esteban Pérez Rivas
 * @author Sara Revilla Báez
 *
 */
public class Vocabulario{
	
	
	public static void main(String[] args) {
		TreeSet<String> set = new TreeSet<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
			PrintWriter writer = new PrintWriter(new FileWriter("vocabulario"));
			
			Pattern word_pattern = Pattern.compile("\\w+'\\w+|(?<=\\p{Punct})\\w?+(?=\\p{Punct})|(?<=\\p{Punct})\\w?+|\\w?+(?=\\p{Punct})");
 			
			while(reader.ready()) {
				String cadena = reader.readLine();
				String[] tokens = cadena.split("\\s+");
				
				for(int i = 0; i <  tokens.length; i++) {
				  String dummy = tokens[i];
				  Matcher matcher = word_pattern.matcher(tokens[i]);
				  if (matcher.find()) {
	          dummy = matcher.group();
				  }

				  
				  if (!dummy.startsWith("http")) {
	          set.add(dummy.toLowerCase());
				  }
				}	
			}
			
			writer.println(set.size());
			String[] array = set.toArray(new String[set.size()]);
			for(int i = 0; i < array.length; i++)
				writer.println(array[i]);
			
			reader.close();
			writer.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Error al abrir el fichero");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error al leer/escribir en el fichero");
			e.printStackTrace();
		}

	}

}
