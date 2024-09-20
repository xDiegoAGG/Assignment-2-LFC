//Se importan librerías para mayor facilidad
import java.util.List;
import java.util.Map;

//Se emplea una clase para manejar cada caso de Prueba como un objeto
class CasoPrueba {
    Map<String, List<String[]>> gramatica;
    String[] cadenas;
    
    CasoPrueba(Map<String, List<String[]>> gramatica, String[] cadenas) {  //Se define el constructor de la clase
        this.gramatica = gramatica;
        this.cadenas = cadenas;
    }
}