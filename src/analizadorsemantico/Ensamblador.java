/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsemantico;


public class Ensamblador {
    
    private static final String LINEA = System.getProperty("line.separator"); //Variable que genera los saltos de linea detectando el sistema del usuario.
    
    public static Codigo generarObjeto(Codigo codigo) {    //Metodo que realiza la generacion del codigo objeto (Ensamblador).
        String registroCon = "";   //Auxiliar que sirve para almacenar el registro donde se encuentra la variable condicional (Switch).
        String registro = "";   //Auxiliar que sirve para almacenar el registro que se maneja en el momento.
        int numCon = 1; //Variable que almacena el numero de condicion.
        StringBuilder stb = new StringBuilder();    //Variable que contendra el codigo ensamblador
        for (int i = 0; i < codigo.tripleta.numeroIns.size(); i++) {    //Se analiza la tripleta.
            String variable = "";   //Variable auxiliar para el guardado de la variable a trabajar.
            if (i < codigo.tripleta.numeroIns.size()-1) {
                if (codigo.tripleta.operador.get(i+1).equalsIgnoreCase("==")) {   //Si la siguiente instruccion es una comparacion.
                    stb.append(LINEA)   //Se agrega un salto de linea
                            .append("Condicion").append(numCon).append(":").append(LINEA);   //Se agrega una etiqueta de condicion.
                }
            }
            if (i == codigo.tripleta.numeroIns.size()-1) {
                
            }
            else if (codigo.tripleta.objeto.get(i).equalsIgnoreCase("JUMP")) {  //Si el dato objeto contiene un JUMP.
                    stb.append("\tJMP SalidaN").append(LINEA); //Se escribe la instruccion de salto.
            }
            else if (!codigo.tripleta.objeto.get(i).contains("T") && !codigo.tripleta.fuente.get(i).contains("T")) {    //En cambio, si la linea no contiene temporales, se trata de una asignacion.
                stb.append("\tMOV ").append(codigo.tripleta.objeto.get(i)).append(", ").append(codigo.tripleta.fuente.get(i)).append(LINEA);    //Se agrega la linea de asignacion. 
            }
            else if (!codigo.tripleta.objeto.get(i).contains("T")) {    //Pero si solo el dato objeto no tiene temporal, entonces estamos ante una asignacion final.
                variable = codigo.tripleta.fuente.get(i);   //Ahora se obtiene la variable temporal que guarda el resultado.
                registro = codigo.cpu.devolverRegistro(variable);   //Se obtiene el registro donde se encuentra almacenada.
                stb.append("\tMOV ").append(codigo.tripleta.objeto.get(i)).append(", ").append(registro).append(LINEA);   //Se registra el movimiento.
                codigo.cpu.borrarRegistro(registro);
            }
            else {  //En caso contrario, estamos ante una operacion aritmetica.
                if (codigo.tripleta.operador.get(i).equalsIgnoreCase("=")) {
                    variable = codigo.tripleta.objeto.get(i);   //Se guarda la variable.
                    registroCon = codigo.cpu.devolverRegistro(variable);   //Se obtiene el registro donde se encuentra almacenada.
                    if (registroCon.isEmpty()) {   //Pero si no se encuentra registrada.
                        registroCon = codigo.cpu.registrarVar(variable);   //Se guarda la variable.
                        variable = codigo.tripleta.fuente.get(i);
                        stb.append("\tMOV ").append(registroCon).append(", ").append(variable).append(LINEA);   //Se mueve al registro el segundo numero.
                    }
                    continue;
                }
                variable = codigo.tripleta.objeto.get(i);   //Se guarda la primera temporal.
                registroCon = codigo.cpu.devolverRegistro(variable);   //Se obtiene el registro donde se encuentra almacenada.
                variable = codigo.tripleta.fuente.get(i);   //Se guarda la segunda temporal.
                if (codigo.tripleta.operador.get(i).equalsIgnoreCase("/")) {
                    if (!codigo.cpu.AH.isEmpty()) { //Se limpia la parte alta de AX si no se encuentra vacia.
                        stb.append("\tMOV AH, 0").append(LINEA);    //Se escribe la limpieza.
                        codigo.cpu.registrarVar("", "AH");    //Se limpia el registro moviendo lo que contiene a otro registro.
                    }
                    stb.append("\tMOV AL, ").append(registroCon).append(LINEA);    //Se pasa a CL el divisor.
                    codigo.cpu.registrarVar(codigo.tripleta.objeto.get(i), "AL");    //Se registra el divisor en CL.
                    stb.append("\tMOV CL, ").append(variable).append(LINEA);    //Se pasa a CL el divisor.
                    codigo.cpu.registrarVar(codigo.tripleta.fuente.get(i), "CL");    //Se registra el divisor en CL.
                    stb.append("\tDIV CL").append(LINEA);   //Se realiza la division y se indica los registros con informacion.
                    codigo.cpu.registrarVar("Residuo", "AH");   //En AH se almacena el residuo.
                    variable = codigo.tripleta.objeto.get(i);   //Se almacena la variable temporal con el resultado.
                    codigo.cpu.registrarVar(variable, "AL");    //Se indica que el resultado se encuentra en AL.
                    codigo.cpu.borrarRegistro("CL");    //Se indica que es irrelevante lo que CL almacenaba.
                    codigo.cpu.borrarRegistro("AH");    //De igual manera con AH.
                }
                else if (codigo.tripleta.operador.get(i).equalsIgnoreCase("*")) { //Si se trata de una multiplicacion.
                    stb.append("\tMUL ").append(registroCon).append(", ").append(variable).append(LINEA);    //Se escribe la operacion.
                    variable = codigo.tripleta.objeto.get(i);   //Se almacena la variable temporal con el resultado.
                    codigo.cpu.registrarVar(variable, registroCon);    //Se indica que el resultado se encuentra en AL.
                }
                else if (codigo.tripleta.operador.get(i).equalsIgnoreCase("-")) {   //Si se trata de una resta.
                    stb.append("\tSUB ").append(registroCon).append(", ").append(variable).append(LINEA);    //Se escribe la operacion.
                    variable = codigo.tripleta.objeto.get(i);   //Se almacena la variable temporal con el resultado.
                    codigo.cpu.registrarVar(variable, registroCon);    //Se indica que el resultado se encuentra en AL.
                }
                else if (codigo.tripleta.operador.get(i).equalsIgnoreCase("+")) {   //Si se trata de una suma.
                    stb.append("\tADD ").append(registroCon).append(", ").append(variable).append(LINEA);    //Se escribe la operacion.
                    variable = codigo.tripleta.objeto.get(i);   //Se almacena la variable temporal con el resultado.
                    codigo.cpu.registrarVar(variable, registroCon);    //Se indica que el resultado se encuentra en AL.
                }
            }
            if (i + 1 == codigo.tripleta.numeroIns.size()) {    //Si estamos en la ultima linea.
                stb.append("\tFIN");    //Se escribe el final.
            }
        }
        Archivo.guardar("Codigo Objeto.txt", stb.toString());   //Se guarda el codigo objeto.
        return codigo;  //Se regresa el codigo completo.
    }
    
}
