/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsemantico;

/**
 *
 * @author Misael
 */
public class Ensamblador {
    
    private static final String LINEA = System.getProperty("line.separator"); //Variable que genera los saltos de línea detectando el sistema del usuario.
    
    public static Codigo generarObjeto(Codigo codigo) {    //Método que realiza la generación del codigo objeto (Ensamblador).
        String registroCon = "";   //Auxiliar que sirve para almacenar el registro donde se encuentra la variable condicional (Switch).
        String registro = "";   //Auxiliar que sirve para almacenar el registro que se maneja en el momento.
        int numCon = 1; //Variable que almacena el número de condicion.
        int numIns = 1; //Variable que almacena el número de instrucción.
        StringBuilder stb = new StringBuilder();    //Variable que contendrá el código ensamblador
        for (int i = 0; i < codigo.tripleta.numeroIns.size(); i++) {    //Se analiza la tripleta.
            String variable = "";   //Variable auxiliar para el guardado de la variable a trabajar.
            try {
                if (i == codigo.tripleta.posFinalSwitch.peek()) {
                    stb.append(LINEA)   //Se agrega un salto de línea.
                            .append("SalidaN").append(codigo.tripleta.posFinalSwitch.peek()).append(":").append(LINEA);
                    codigo.tripleta.posFinalSwitch.poll();
                }
            }
            catch (NullPointerException ex) {
            }
            if (i < codigo.tripleta.numeroIns.size()-1) {
                if (codigo.tripleta.operador.get(i+1).equalsIgnoreCase("==")) {   //Si la siguiente instrucción es una comparación.
                    stb.append(LINEA)   //Se agrega un salto de línea
                            .append("Condicion").append(numCon).append(":").append(LINEA);   //Se agrega una etiqueta de condición.
                }
            }
            if (i == codigo.tripleta.numeroIns.size()-1) {
                
            }
            if (codigo.tripleta.operador.get(i).equalsIgnoreCase("==")) {   //Si estamos dentro de una comparación.
                numCon++;   //Se aumenta el contador de condición.
                variable = codigo.tripleta.objeto.get(i);   //Se guarda la variable a trabajar.
                registroCon = codigo.cpu.devolverRegistro(variable);  //Se busca en que registro se tiene guardado el Dato Objeto.
                variable = codigo.tripleta.fuente.get(i);   //Se guarda la variable a trabajar.
                registro = codigo.cpu.devolverRegistro(variable);  //Se busca en que registro se tiene guardado el Dato Fuente.
                stb.append("\tCMP ").append(registroCon).append(", ").append(registro).append(LINEA)    //Se agrega la línea de comparación.
                        .append("\tJE Instruccion").append(numIns).append(LINEA)    //Se realiza el salto a la siguiente instrucción.
                        .append("\tJMP Condicion").append(numCon).append(LINEA)    //Se realiza el salto a la siguiente comparación.
                        .append(LINEA)  //Se agrega un salto de línea.
                        .append("Instruccion").append(numIns).append(":").append(LINEA);    //Se agrega la etiqueta para la instrucción.
                numIns++;   //Se aumenta el número de instrucción.
                i+=2;   //Se saltan las siguientes dos instrucciones (TRUE y FALSE).
                codigo.cpu.borrarRegistro(registro);    //El segundo registro de comparación no se vuelve a utilizar, así que se libera el registro.
            }
            else if (codigo.tripleta.objeto.get(i).equalsIgnoreCase("JUMP")) {  //Si el dato objeto contiene un JUMP.
                    stb.append("\tJMP SalidaN").append(codigo.tripleta.posFinalSwitch.peek()).append(LINEA); //Se escribe la instrucción de salto.
            }
            else if (!codigo.tripleta.objeto.get(i).contains("T") && !codigo.tripleta.fuente.get(i).contains("T")) {    //En cambio, si la línea no contiene temporales, se trata de una asignación.
                stb.append("\tMOV ").append(codigo.tripleta.objeto.get(i)).append(", ").append(codigo.tripleta.fuente.get(i)).append(LINEA);    //Se agrega la línea de asignación. 
            }
            else if (!codigo.tripleta.objeto.get(i).contains("T")) {    //Pero si solo el dato objeto no tiene temporal, entonces estamos ante una asignación final.
                variable = codigo.tripleta.fuente.get(i);   //Ahora se obtiene la variable temporal que guarda el resultado.
                registro = codigo.cpu.devolverRegistro(variable);   //Se obtiene el registro donde se encuentra almacenada.
                stb.append("\tMOV ").append(codigo.tripleta.objeto.get(i)).append(", ").append(registro).append(LINEA);   //Se registra el movimiento.
                codigo.cpu.borrarRegistro(registro);
            }
            else {  //En caso contrario, estamos ante una operación aritmetica.
                if (codigo.tripleta.operador.get(i).equalsIgnoreCase("=")) {
                    variable = codigo.tripleta.objeto.get(i);   //Se guarda la variable.
                    registroCon = codigo.cpu.devolverRegistro(variable);   //Se obtiene el registro donde se encuentra almacenada.
                    if (registroCon.isEmpty()) {   //Pero si no se encuentra registrada.
                        registroCon = codigo.cpu.registrarVar(variable);   //Se guarda la variable.
                        variable = codigo.tripleta.fuente.get(i);
                        registro = codigo.cpu.devolverRegistro(variable);
                        if (registro.isEmpty()) {
                            stb.append("\tMOV ").append(registroCon).append(", ").append(codigo.tripleta.fuente.get(i)).append(LINEA);   //Se mueve al registro el segundo número.
                        }
                        else {
                            stb.append("\tMOV ").append(registroCon).append(", ").append(registro).append(LINEA);   //Se mueve al registro el segundo número.
                        }
                    }
                    continue;
                }
                variable = codigo.tripleta.objeto.get(i);   //Se guarda la primera temporal.
                registroCon = codigo.cpu.devolverRegistro(variable);   //Se obtiene el registro donde se encuentra almacenada.
                variable = codigo.tripleta.fuente.get(i);   //Se guarda la segunda temporal.
                registro = codigo.cpu.devolverRegistro(variable);   //Se obtiene el registro donde se encuentra almacenada.
                if (codigo.tripleta.operador.get(i).equalsIgnoreCase("/")) {
                    if (!codigo.cpu.AH.isEmpty()) { //Se limpia la parte alta de AX si no se encuentra vacía.
                        stb.append("\tMOV AH, 0").append(LINEA);    //Se escribe la limpieza.
                        codigo.cpu.registrarVar("", "AH");    //Se limpia el registro moviendo lo que contiene a otro registro.
                    }
                    stb.append("\tMOV AL, ").append(registroCon).append(LINEA);    //Se pasa a CL el divisor.
                    codigo.cpu.registrarVar(codigo.tripleta.objeto.get(i), "AL");    //Se registra el divisor en CL.
                    stb.append("\tMOV CL, ").append(registro).append(LINEA);    //Se pasa a CL el divisor.
                    codigo.cpu.registrarVar(codigo.tripleta.fuente.get(i), "CL");    //Se registra el divisor en CL.
                    stb.append("\tDIV CL").append(LINEA);   //Se realiza la división y se indica los registros con información.
                    codigo.cpu.registrarVar("Residuo", "AH");   //En AH se almacena el residuo.
                    variable = codigo.tripleta.objeto.get(i);   //Se almacena la variable temporal con el resultado.
                    codigo.cpu.registrarVar(variable, "AL");    //Se indica que el resultado se encuentra en AL.
                    codigo.cpu.borrarRegistro("CL");    //Se indica que es irrelevante lo que CL almacenaba.
                    codigo.cpu.borrarRegistro("AH");    //De igual manera con AH.
                }
                else if (codigo.tripleta.operador.get(i).equalsIgnoreCase("*")) { //Si se trata de una multiplicación.
                    stb.append("\tMUL ").append(registroCon).append(", ").append(registro).append(LINEA);    //Se escribe la operación.
                    variable = codigo.tripleta.objeto.get(i);   //Se almacena la variable temporal con el resultado.
                    codigo.cpu.registrarVar(variable, registroCon);    //Se indica que el resultado se encuentra en AL.
                    codigo.cpu.borrarRegistro(registro);    //Se indica que el segundo registro puede reutilizarse.
                }
                else if (codigo.tripleta.operador.get(i).equalsIgnoreCase("-")) {   //Si se trata de una resta.
                    stb.append("\tSUB ").append(registroCon).append(", ").append(registro).append(LINEA);    //Se escribe la operación.
                    variable = codigo.tripleta.objeto.get(i);   //Se almacena la variable temporal con el resultado.
                    codigo.cpu.registrarVar(variable, registroCon);    //Se indica que el resultado se encuentra en AL.
                    codigo.cpu.borrarRegistro(registro);    //Se indica que el segundo registro puede reutilizarse.
                }
                else if (codigo.tripleta.operador.get(i).equalsIgnoreCase("+")) {   //Si se trata de una suma.
                    stb.append("\tADD ").append(registroCon).append(", ").append(registro).append(LINEA);    //Se escribe la operación.
                    variable = codigo.tripleta.objeto.get(i);   //Se almacena la variable temporal con el resultado.
                    codigo.cpu.registrarVar(variable, registroCon);    //Se indica que el resultado se encuentra en AL.
                    codigo.cpu.borrarRegistro(registro);    //Se indica que el segundo registro puede reutilizarse.
                }
            }
            if (i + 1 == codigo.tripleta.numeroIns.size()) {    //Si estamos en la última línea.
                try {
                    if (i + 2 == codigo.tripleta.posFinalSwitch.peek()) {   //Se revisa si el switch termina en la última línea.
                        stb.append(LINEA)   //Se agrega un salto de línea.
                                .append("SalidaN").append(codigo.tripleta.posFinalSwitch.peek()).append(":").append(LINEA);  //Se agrega la etiqueta de salida.
                        codigo.tripleta.posFinalSwitch.poll();  //Se borra la ultima posición del switch.
                    }
                }
                catch (NullPointerException ex) {
                }
                stb.append("\tFIN");    //Se escribe el final.
            }
        }
        Archivo.guardar("Codigo Objeto.txt", stb.toString());   //Se guarda el código objeto.
        return codigo;  //Se regresa el código completo.
    }
    
}
