package ia;
/*
 * Programa para estimar las probabilidades del vocabulario en cada corpus.
 * Para cada palabra de un fichero de vocabulario dado, calcula su frecuencia
 * absoluta en un corpus dado (cuyo nombre de fichero DEBE terminar con la
 * extensión .txt) y también su probabilidad mediante suavizado laplaciano
 * con tratamiento de palabras desconocidas.
 * La salida se genera en un fichero aprendizajeX.txt, en el que, además de las
 * probabilidades, también aparece el número de documentos y el de palabras.
 * Se excluyen totalmente los enlaces.
 * 
 * java <corpusX>.txt <vocabulario.txt>
 * 
 * @author Javier Esteban Pérez Rivas
 * @author Sara Revilla Báez
 *
 */
 
//package ia;

import java.io.*;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.Set;

public class Aprendizaje {
    private static Hashtable<String, Integer> table = new Hashtable<>();
    private static int n_documents = 0;        /** Número de documentos del corpus */
    private static int n_words = 0;            /** Número de palabras del corpus */
    
    public static void main(String[] args) {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(args[1]));
            writer = new PrintWriter(new FileWriter("aprendizaje" + args[0].charAt(args[0].length() - 5) + ".txt"));
            
            int vocab_sz = Integer.parseInt(reader.readLine());
            for (int i = 0; i < vocab_sz; i++) {
                table.put(reader.readLine(), 0);
            }
            reader.close();
            
            reader = new BufferedReader(new FileReader(args[0]));
            Pattern url_pattern = Pattern.compile("https?://.*");
			Pattern punctuation_pattern = Pattern.compile("[\\p{Punct}&&[^'<>]]+");
			Pattern quatationMarks_pattern = Pattern.compile("(?<!\\w)'(?=\\w)|(?<=\\w)'(?!\\w)|(?<!\\w)'(?!\\w)");
            
            while (reader.ready()) {
				String cadena = reader.readLine();
				Matcher url_Matcher = url_pattern.matcher(cadena);
				cadena = url_Matcher.replaceAll("<URL>");
				Matcher punctuation_Matcher = punctuation_pattern.matcher(cadena);
				cadena = punctuation_Matcher.replaceAll(" ");
				Matcher quatationMarks_Matcher = quatationMarks_pattern.matcher(cadena);
				cadena = quatationMarks_Matcher.replaceAll(" ");
				
				String[] tokens = cadena.split("\\s+");
                n_documents++;
				
				for (int i = 0; i < tokens.length; i++) {
    			    String dummy = tokens[i];
    			   
                    if (table.get(dummy) != null) {
                        n_words++;
                        table.put(dummy, table.get(dummy) + 1);
                    }
				}	
			}
         
			
			writer.println("Número de documentos del corpus: " + n_documents);
			writer.println("Número de palabras del corpus: " + n_words);
			Set<String> keys_set = table.keySet();
			String[] keys = keys_set.toArray(new String[keys_set.size()]);
			Arrays.sort(keys);
		    for (String current_key : keys) {
		        double prob = Math.log((double)(table.get(current_key) + 1) / (double)(n_words + vocab_sz));
		        writer.printf("Palabra: " + "%-25s" + " Frec: " + "%-4s" + " Log_Prob: " + "%-10s" + "\n", current_key, table.get(current_key), prob); 
		    }
            
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