package ia;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Programa para clasificar tweets de un corpus en distintas clases, 
 * dados sus ficheros de probabilidades.
 * Los ficheros de probabilidades deben terminar por X.txt, donde X es la
 * inicial de la clase.
 * La salida se genera en un fichero clasificacion.txt, en el que aparecen
 * las iniciales de las clases en las que se ha clasificado cada tweet.
 * 
 * java corpus<opt>.txt [aprendizajeX1.txt aprendizajeX2.txt ... aprendizajeXN.txt]
 * 
 * @author Javier Esteban P�rez Rivas
 * @author Sara Revilla B�ez
 *
 */
public class Clasificacion {

	public static void main(String[] args) {
		BufferedReader reader = null;
        PrintWriter writer = null;
        
        char[] clases = new char[args.length - 1]; // D, I, A...
        // Se guarda el identificador de la clase con su probabilidad
        Hashtable<Character, Double> class_table = new Hashtable<>(); 
        
        try {
            // Vector de tablas Hash que contienen las probabilidades de cada palabra dentro de cada clase
            ArrayList<Hashtable<String, Double>> prob_table = new ArrayList<Hashtable<String, Double>>();  
            
            /**
             * Lectura de los ficheros de probabilidades
             */
            for (int i = 1; i < args.length; ++i) {
                clases[i - 1] = args[i].charAt(args[i].length() - 5);
                prob_table.add(new Hashtable<String, Double>());
                
                // Ficheros de aprendizaje
                reader = new BufferedReader(new FileReader(args[i]));
                
                // N�mero de documentos y de palabras
                String line = reader.readLine();
                String tokens[] = line.split("\\s+");
                double n_documents = Double.parseDouble(tokens[tokens.length - 1]);
                class_table.put(clases[i - 1], n_documents);
                reader.readLine(); // La segunda l�nea no nos interesa
                
                while (reader.ready()) {
                    line = reader.readLine();
                    tokens = line.split("\\s+");
                    String palabra = tokens[1];
                    Double prob = Double.parseDouble(tokens[5]);
                    
                    prob_table.get(i - 1).put(palabra, prob);
                }
                reader.close();
            }
            
            // Logaritmo de la probabilidad de cada clase
            int corpus_size = 0;
            for(int i = 0; i < clases.length; i++)
                corpus_size += class_table.get(clases[i]);
            for(int i = 0; i < clases.length; i++) {
                class_table.put(clases[i], Math.log(class_table.get(clases[i]) / corpus_size));
            }
            
            reader = new BufferedReader(new FileReader(args[0]));   // Corpus
            writer = new PrintWriter(new FileWriter("clasificacion.txt"));
            
            /**
			 * Expresiones Regulares para el pre-procesado
			 */
            Pattern url_pattern = Pattern.compile("https?://.*");
            Pattern mention_pattern = Pattern.compile("(?<!\\w)@\\w+");
			Pattern punctuation_pattern = Pattern.compile("[\\p{Punct}&&[^'<>]]+");
			Pattern quatationMarks_pattern = Pattern.compile("(?<!\\w)'(?=\\w)|(?<=\\w)'(?!\\w)|(?<!\\w)'(?!\\w)");
            
            while (reader.ready()) {
            	/**
				 * Fase de preprocesado
				 */
                String line = reader.readLine();
                Matcher url_Matcher = url_pattern.matcher(line);
				line = url_Matcher.replaceAll("<URL>");
		        Matcher mention_Matcher = mention_pattern.matcher(line);
		        line = mention_Matcher.replaceAll("<MTN>");
				Matcher punctuation_Matcher = punctuation_pattern.matcher(line);
				line = punctuation_Matcher.replaceAll(" ");
				Matcher quatationMarks_Matcher = quatationMarks_pattern.matcher(line);
				line = quatationMarks_Matcher.replaceAll(" ");
				
				/**
				 * Separaci�n de la cadena
				 */
                String tokens[] = line.split("\\s+");
                
                String clase = null;
                double max_prob = Double.NEGATIVE_INFINITY;
                
                /**
                 * Calculo de la probabilidad del tweet. Se recorren todas las clases y se guarda la de mayor probabilidad
                 */
                for (int i = 0; i < clases.length; ++i) {
                    double prob = 0;
                    // Calculo la probabilidad para la clase i
                    for (String token : tokens) {
                        token = token.toLowerCase();
                        if (prob_table.get(i).get(token) == null)
                            token = "<UNK>";
                        prob += prob_table.get(i).get(token);
                    }
                    prob += class_table.get(clases[i]);
                    
                    // Si la probabilidad es mejor, actualizo la clase
                    if (prob > max_prob) {
                        clase = Character.toString(clases[i]);
                        max_prob = prob;
                    }
                }
                
                writer.println(clase);
                
            }
               
        } catch (FileNotFoundException e) {
			System.out.println("Error al abrir el fichero");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error al leer/escribir en el fichero");
			e.printStackTrace();
		} finally {
		    try {
		    	if(reader != null)
		    		reader.close();
		    	if(writer != null)
		    		writer.close();
		    } catch (IOException e) {}
		}
         
    }
	    
}