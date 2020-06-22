/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsemantico;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author LENOVO
 */
public class Tripleta {
    
    // la tripleta contiene la informacion necesaria para el codigo intermedio.
    // tambien almacena las posiciones donde los ciclos fueron llamados.
    
    ArrayList<Integer> numeroIns;
    ArrayList<String> objeto;
    ArrayList<String> fuente;
    ArrayList<String> operador;
    Queue<Integer> posFinalSwitch;
    
    public Tripleta() {
        numeroIns = new ArrayList<>();
        objeto = new ArrayList<>();
        fuente = new ArrayList<>();
        operador = new ArrayList<>();
        posFinalSwitch = new LinkedList<>();
    }
    
    public Tripleta(ArrayList<Integer> numeroIns, ArrayList<String> objeto, ArrayList<String>  fuente, 
            ArrayList<String> operador, Queue<Integer> posFinalSwitch) {
        this.numeroIns = numeroIns;
        this.objeto = objeto;
        this.fuente = fuente;
        this.operador = operador;
        this.posFinalSwitch = posFinalSwitch;
    }
    
}
