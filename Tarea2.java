//Se importan algunas librerías para mayor facilidad
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

class Tarea2 {
    
    public static void main(String[] args) {
        
        String nombreArchivo = "input.txt";   //Nombre del archivo de entrada
        List<CasoPrueba> casos = crearListaCaso(nombreArchivo); //Se crea una lista con los casos de prueba del archivo
        
        for (int i = 0; i < casos.size(); i++) {
            CasoPrueba caso = casos.get(i);
            for (int j = 0; j < caso.cadenas.length; j++) {
                String cadena = caso.cadenas[j];
                boolean resultado = CKY(caso.gramatica, cadena);
                if (resultado == true) {
                    System.out.println("yes");
                } else {
                    System.out.println("no");
                }
            }
        }
    }
    
    public static List<CasoPrueba> crearListaCaso(String nombreArchivo) {
        List<CasoPrueba> casos = new ArrayList<>();  //Se crea una lista dentro del metodo para almacenar los casos
        
        try {
            
            File archivo = new File(nombreArchivo);
            Scanner scan = new Scanner(archivo);
            int numCasos = Integer.parseInt(scan.nextLine().trim());  //Primero se lee el número de casos de Prueba
            
            for (int i = 0; i < numCasos; i++) {
                
                String[] km = scan.nextLine().split(" ");
                int k = Integer.parseInt(km[0]);  //Se lee el número de simbolos no terminales
                int m = Integer.parseInt(km[1]);  //Se lee el número de cadenas a analizar
                Map<String, List<String[]>> gramatica = new HashMap<>();
                
                for (int j = 0; j < k; j++) {
                    
                    String[] produccion = scan.nextLine().split(" ");  //Se leen los no terminales con sus debidas producciones
                    String noTerminal = produccion[0];  //El primer elemento es el simbolo no terminal, el resto son producciones
                    List<String[]> producciones = new ArrayList<>();
                    
                    for (int p = 1; p < produccion.length; p++) {  // Se leen las producciones
                        
                        if (produccion[p].length() == 1) { 
                            producciones.add(new String[]{produccion[p]});
                        } else {
                            producciones.add(produccion[p].split(""));
                        }
                    }
                    gramatica.put(noTerminal, producciones);   //Se agregan las producciones leídas a la gramatica en curso
                }
                String[] cadenas = new String[m];
                
                for (int j = 0; j < m; j++) {
                    cadenas[j] = scan.nextLine();
                }
                
                casos.add(new CasoPrueba(gramatica, cadenas));  //Se guarda la gramatica en la lista de casos
            }
        } catch (Exception e) {
            System.out.println("No se ha encontrado el archivo: " + nombreArchivo);   //Error por si no se encuentra el archivo a leer
        }
        return casos;   //Se devuelve la lista con la estructura de las gramaticas de los casos de prueba
    }
    
    public static boolean CKY(Map<String, List<String[]>> gramatica, String entrada) {   //Se crea la función para implementar el algoritmo Cocke-Kasami-Younger
        
        int n = entrada.length();   
        Set<String>[][] tabla = new HashSet[n][n];  //Se crea la tabla bidimensional para almacenar los no terminales
        
        for (int i = 0; i < n; i++) { 
            for (int j = 0; j < n; j++) {
                tabla[i][j] = new HashSet<>();
            }
        }
        
        for (int i = 0; i < n; i++) {  //Ciclo para ir llenando la tabla
            char simbolo = entrada.charAt(i);
            for (String noTerminal : gramatica.keySet()) {
                List<String[]> producciones = gramatica.get(noTerminal);
                for (int p = 0; p < producciones.size(); p++) {
                    String[] produccion = producciones.get(p);
                    if (produccion.length == 1 && produccion[0].equals(Character.toString(simbolo))) {
                        tabla[i][i].add(noTerminal);
                    }
                }
            }
        }
        
        for (int longitud = 2; longitud <= n; longitud++) {  //Ciclos para verificar si la cadena logra ser derivada por la gramatica
            for (int i = 0; i <= n - longitud; i++) {
                int j = i + longitud - 1;
                for (int k = i; k < j; k++) {
                    for (String noTerminal : gramatica.keySet()) {
                        List<String[]> producciones = gramatica.get(noTerminal);
                        for (int p = 0; p < producciones.size(); p++) {
                            String[] produccion = producciones.get(p);
                            if (produccion.length == 2) {
                                String B = produccion[0];
                                String C = produccion[1];
                                if (tabla[i][k].contains(B) && tabla[k + 1][j].contains(C)) {
                                    tabla[i][j].add(noTerminal);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return tabla[0][n - 1].contains("S");  //Devuelve un booleano que verifica si el simbolo inicial se encuentra en la última casilla de la primera columna
    }
}

// Hecho por Diego Andrés Gonzalez Graciano el día 20/09/2024