/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsemantico;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author LENOVO
 */
public class Archivo {
    
    public static void guardar(String nombre, String extension, String texto) {
        try (FileWriter fw = new FileWriter(nombre+"."+extension)) {
            fw.write(texto);
        }
        catch (IOException ex) {
            System.out.println("No se pudo guardar el archivo.");
        }
    }
    
    public static void guardar(String archivo, String texto) {
        try (FileWriter fw = new FileWriter(archivo)) {
            fw.write(texto);
        }
        catch (IOException ex) {
            System.out.println("No se pudo guardar el archivo.");
        }
    }
    
    public static String arregloTexto (String[] arreglo) { //Método para convertir un arreglo a texto.
        StringBuilder stb = new StringBuilder();    //Se inicializa la construcción del texto.
        for (String texto : arreglo) {  //Se recorre el arreglo.
            stb.append(texto);  //Al texto se le va agregando cada elemento del arreglo.
        }
        return stb.toString();  //Se devuelve el texto generado.
    }
    
    // metodo auxiliar para la eliminacion de elementos de un arreglo
    public static String[] borrarArreglo (int posicion, String[] arreglo) {
        
        // se inicializa un nuevo arreglo con un tamaño menor al existente
        String[] nuevoArreglo = new String[arreglo.length - 1];
        
        if (posicion > 0){
            System.arraycopy(arreglo, 0, nuevoArreglo, 0, posicion);
        }
        
        if (nuevoArreglo.length > posicion){
            System.arraycopy(arreglo, posicion + 1, nuevoArreglo, posicion, nuevoArreglo.length - posicion);
        }
        
        return nuevoArreglo;
    }
    
    public static String[] repararSplit (String[] arreglo) {
        
        ArrayList<String> nuevoArreglo = new ArrayList<>();
        
        for (int i = 0; i < arreglo.length; i++) {
            if (i < arreglo.length-1) {
                if (arreglo[i+1].equalsIgnoreCase(".")) {
                    nuevoArreglo.add(arreglo[i] + arreglo[i+1] + arreglo[i+2]);
                    i+=2;
                    continue;
                }
            }
            nuevoArreglo.add(arreglo[i]);
        }
        
        return Arrays.copyOf(nuevoArreglo.toArray(), nuevoArreglo.size(), String[].class);
    }
    
}
