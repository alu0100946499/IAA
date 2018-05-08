package ia;
/*
 * Programa para generar el vocabulario con el que se trabaja.
 * Dentro del vocabulario se sustituyen las url's por el token <URL>, los signos de puntuación
 * como comas, puntos, etc... excepto el uso del genitivo sajón en palabras inglesas,
 * así como contracciones y palabra compuestas.
 * @author Javier Esteban Pérez Rivas
 * @author Sara Revilla Báez
 *
 */

//package ia;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vocabulario {
	
	
	public static void main(String[] args) {
		TreeSet<String> set = new TreeSet<String>();
		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(args[0]));
			writer = new PrintWriter(new FileWriter("vocabulario.txt"));
			
			Pattern url_pattern = Pattern.compile("https?://.*");
			Pattern punctuation_pattern = Pattern.compile("[\\p{Punct}&&[^'<>]]+");
			Pattern quatationMarks_pattern = Pattern.compile("(?<!\\w)'(?=\\w)|(?<=\\w)'(?!\\w)|(?<!\\w)'(?!\\w)");
			
			while(reader.ready()) {
				String cadena = reader.readLine();
				Matcher url_Matcher = url_pattern.matcher(cadena);
				cadena = url_Matcher.replaceAll("<URL>");
				Matcher punctuation_Matcher = punctuation_pattern.matcher(cadena);
				cadena = punctuation_Matcher.replaceAll(" ");
				Matcher quatationMarks_Matcher = quatationMarks_pattern.matcher(cadena);
				cadena = quatationMarks_Matcher.replaceAll(" ");

				String[] tokens = cadena.split("\\s+");
				
				for(int i = 0; i <  tokens.length; i++) {
				  String dummy = tokens[i];
				  if(dummy.equals("<URL>")) {
					  set.add(dummy);
				  }
				  else
					  set.add(dummy.toLowerCase());  

				}
			}
			
			writer.println(set.size());
			String[] array = set.toArray(new String[set.size()]);
			for(int i = 1; i < array.length; i++)
				writer.println(array[i]);
			writer.println("<UNK>");
			
			reader.close();
			writer.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Error al abrir el fichero");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error al leer/escribir en el fichero");
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {}
		}

	}

}
