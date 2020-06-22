/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsemantico;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analizador {
    
    private static Codigo codigo;
    private static int tIDE, tOA, tPR, tCE, tCF, tOR, tOB;
    private static final String LINEA = System.getProperty("line.separator"); //Variable que genera los saltos de linea detectando el sistema del usuario.
    
    /**
     * @param texto 
     * @return  
     */
    public static Codigo busquedaErrores(String texto) {
        
        // Objeto para la construccion de strings.
        StringBuilder stb = new StringBuilder();
        
        codigo = new Codigo(texto);
        tIDE = 0; tOA = 0; tPR = 0; tCE = 0; tCF = 0; tOR = 0; tOB = 0;
        
        // Antes de iniciar, se deben eliminar los tabs del codigo para evitar errores adicionales.
        codigo.codigo = codigo.codigo.replace("\t", "");
        
        // Se divide el codigo por lineas.
        String[] lineaCodigo = codigo.codigo.split("\\n");
        
        Boolean condicion = false; //Variable que indica si estamos manejando condicional switch.
        
        // La verificacion linea por linea empieza en esta parte
        for (int i = 0; i < lineaCodigo.length; i++) {
            
            if (codigo.numSwitch == 0) {    //Si no existe ninguna condicion Switch inicializada.
                condicion = false;  //Se desactiva la bandera.
            }
            
            // Variable que almacena el numero de linea.
            int numLinea = i + 1;
            
            // Ahora la verificacion por linea se checa por palabras divididas por espacios.
            String[] palabra = lineaCodigo[i].split("\\s");;
            
            // Analisis sin espacios
            if (lineaCodigo[i].contains("<=") || lineaCodigo[i].contains(">=")) {   //Para condicionales con "<=" y ">=" se aplica esta separacion.
                palabra = lineaCodigo[i].split("((?<=((int|float|double|char|if|else)|(([<>][=])|([!=][=]))|([;)(}{:,])))|(?=((int|float|double|char|if|else)|(([<>][=])|([!=][=]))|([;)(}{:,]))))");
            }
            else if (lineaCodigo[i].contains("<") || lineaCodigo[i].contains(">") || lineaCodigo[i].contains("==")) { //Para condicionales con "<", ">" y "==" se aplica esta separacion.
                palabra = lineaCodigo[i].split("((?<=((int|float|double|char|if|else)|(([<>])|([!=][=]))|([;)(}{:,])))|(?=((int|float|double|char|if|else)|(([<>])|([!=][=]))|([;)(}{:,]))))");
            }
            else if (lineaCodigo[i].contains("+") || lineaCodigo[i].contains("-") || lineaCodigo[i].contains("*") || lineaCodigo[i].contains("/")) {    //Para operaciones aritmeticas se aplica esta separacion.
                palabra = lineaCodigo[i].split("((?<=((int|float|double|char|if|else)|([;)(}{=:,])|([+-/*])))|(?=((int|float|double|char|if|else)|([;)(}{=:,])|([+-/*]))))");
            }
            else {  //Para cualquier otro tipo de operaciones, se usa esta separacion.
                palabra = lineaCodigo[i].split("((?<=((int|float|double|char|if|else)|([;)(}{=:,])))|(?=((int|float|double|char|if|else)|([;)(}{=:,]))))");
            }
            
            palabra = Archivo.repararSplit(palabra); //Se verifica el split para reparar posibles errores como el ".".
            
            // Variable encargada de almacenar la variable base.
            String var = "";
            
            // Variable encargada de guardar el tipo de variable de la linea.
            String tipo = "null";
            
            // Variable encargada de guardar el valor de la variable.
            String valor = "null";
            
            // Bandera para saber si el tipo base ha sido asignado anteriormente.
            Boolean tipoBase = false;
            
            // Bandera para saber si la variable base ha sido asignada anteriormente.
            Boolean varBase = false;
            
            // Bandera para saber que parte del codigo define el valor de la variable.
            Boolean asignacion = false;
            
            // Bandera para saber si un error fue encontrado.
            Boolean error = false;
            
            for (int j = 0; j < palabra.length; j++) {
                
                // Si la palabra contiene espacios se eliminan.
                palabra[j] = palabra[j].replace(" ", "");
                
                // Si la palabra esta vacia, no tiene caso analizarla.
                if (palabra[j].isEmpty()) {
                    palabra = Archivo.borrarArreglo(j, palabra);
                    j--;
                    continue;
                }
                
                // Bandera para saber si el token es nuevo.
                Boolean nuevo = true;
                
                if (j == 0) {   //El tipo de variable solo es almacena si se trata de la primera palabra de la linea.
                    Pattern patron = Pattern.compile("int$|float$|double$|char$");    //Determinamos que queremos buscar un tipo.
                    Matcher coincidencia = patron.matcher(palabra[j]);  //Se verifica si es un tipo.
                    if (coincidencia.matches()) {   //Si se trata de un tipo.
                        tipo = palabra[j];  //Se guarda el tipo.
                        tipoBase = true;    //Se marca que el tipo base ha sido asignado.
                        if (tPR > 0) {  //Si existe mas de un token registrado.
                            int t = codigo.valorToken.indexOf(palabra[j]);  //Se busca su posicion.
                            if (t != -1) {  //Si el token existe.
                                codigo.token.add(codigo.token.get(t));  //Se agrega el token de nuevo.
                                codigo.valorToken.add(codigo.valorToken.get(t));    //Junto a su valor correspondiente.
                            }
                            else {  //De no existir el token.
                                tPR++;  //Se aumenta el contador de tokens PR.
                                codigo.token.add("PR" +tPR);    //Se almacena el nuevo token.
                                codigo.valorToken.add(palabra[j]);  //Con su respectivo valor.
                            }
                        }
                        else {  //Si se trata del primer token de tipo PR.
                            tPR++;  //Se aumenta el contador de tokens PR.
                            codigo.token.add("PR" +tPR);    //Se almacena el nuevo token.
                            codigo.valorToken.add(palabra[j]);  //Con su respectivo valor.
                        }
                        continue;   //Se verifica la siguiente palabra.
                    }
                }
                
                // Se verifica si se trata de las palabras reservadas if o else.
                Pattern patron = Pattern.compile("if$|else$");
                Matcher coincidencia = patron.matcher(palabra[j]);
                
                // Si coinicide, se verifica la siguiente palabra.
                if (coincidencia.matches()) {
                    
                    // Se agrega el token.
                        if (tPR > 0) {
                            int t = codigo.valorToken.indexOf(palabra[j]);  //Se busca su posicion.
                            if (t != -1) {  //Si el token existe.
                                codigo.token.add(codigo.token.get(t));  //Se agrega el token de nuevo.
                                codigo.valorToken.add(codigo.valorToken.get(t));    //Junto a su valor correspondiente.
                            }
                            else {  //De no existir el token.
                                tPR++;  //Se aumenta el contador de tokens PR.
                                codigo.token.add("PR" +tPR);    //Se almacena el nuevo token.
                                codigo.valorToken.add(palabra[j]);  //Con su respectivo valor.
                            }
                        }
                        // Si no hay ningun token, se registra el primero
                        else {
                            tPR++;
                            codigo.token.add("PR" +tPR);
                            codigo.valor.add(palabra[j]);
                        }
                        
                        
                    continue;
                }
                
                // Se verifica si es el operador de asignacion.
                patron = Pattern.compile("^[=]$");
                coincidencia = patron.matcher(palabra[j]);
                
                // Se activa la bandera que almacena el valor de la variable.
                if (coincidencia.matches()) {
                    asignacion = true;
                    
                    // Se inicia StringBuilder para empezar almacenar el valor de la variable.
                    stb = new StringBuilder();
                    
                    // Se registra el token de asignacion.
                    codigo.token.add("OAS");
                    codigo.valorToken.add("=");
                    
                    continue;
                }
                
                patron = Pattern.compile("^[,]$");  //Determinamos que queremos buscar una coma.
                coincidencia = patron.matcher(palabra[j]);  //Se verifica si es una coma.
                if (coincidencia.matches()) {   //Si la palabra concuerda.
                    codigo.token.add("COM");   //Se registra el token de coma.
                    codigo.valorToken.add(",");    //Se guarda el valor del token.
                    if (asignacion) {   //Si hay una asignacion.
                        asignar(var, stb.toString(), error);    //Se asigna el valor a la variable.
                        asignacion = false; //Se desactiva la bandera de asignacion.
                        error = false;  //Se desactiva la bandera de error para la siguiente comprobacion.
                        varBase = false;    //Al realizar la asignacion, se conserva el tipo pero se desecha la variable.
                        var = "";   //Se limpia la variable base.
                    }
                    continue; //Se checa el siguiente token.
                }
                
                // Se verifica si es el delimitador.
                patron = Pattern.compile("^[;]$");
                coincidencia = patron.matcher(palabra[j]);
                
                if (coincidencia.matches()) {
                    
                    // Se registra el token de delimitador.
                    codigo.token.add("DEL");
                    codigo.valorToken.add(";");
                    
                    
                    continue;
                }
                
                // Se verifica si es un parentesis.
                patron = Pattern.compile("[(]$");
                coincidencia = patron.matcher(palabra[j]);
                
                if (coincidencia.matches()) {
                    
                    // Al tratarse de una asignacion, el parentesis se almacena.
                    if (asignacion) {
                        stb.append(palabra[j]).append(" ");
                    }
                    
                    // Se registra el token de parentesis.
                    codigo.token.add("PAR1");
                    codigo.valorToken.add("(");
                    
                    
                    continue;
                }
                
                // Se verifica si es un parentesis.
                patron = Pattern.compile("[)]$");
                coincidencia = patron.matcher(palabra[j]);
                
                if (coincidencia.matches()) {
                    
                    // Al tratarse de una asignacion, el parentesis se almacena.
                    if (asignacion) {
                        stb.append(palabra[j]).append(" ");
                    }
                    
                    // Se registra el token de parentesis.
                    codigo.token.add("PAR2");
                    codigo.valorToken.add(")");
                    
                    
                    continue;
                }
                
                // Se verifica si es un corchete.
                patron = Pattern.compile("[{]$");
                coincidencia = patron.matcher(palabra[j]);
                
                if (coincidencia.matches()) {
                    
                    // Se registra el token de delimitador.
                    codigo.token.add("COR1");
                    codigo.valorToken.add("{");
                    
                    if (condicion) { //Si existe al menos una condicion Switch.
                        codigo.numSwitch++; //Se aumenta el contador de Switch iniciados.
                    }
                    
                    continue;
                }
                
                // Se verifica si es un corchete.
                patron = Pattern.compile("[}]$");
                coincidencia = patron.matcher(palabra[j]);
                
                if (coincidencia.matches()) {
                    
                    // Se registra el token de delimitador.
                    codigo.token.add("COR2");
                    codigo.valorToken.add("}");
                    
                    if (condicion) { //Si existe al menos una condicion Switch.
                        codigo.numSwitch--; //Se elimina una condicion.
                        codigo.varSwitch.pop(); //Se elimina la variable de la condicion.
                        codigo.tipoSwitch.pop();    //Tambien el tipo de esa condicion.
                    }
                    
                    continue;
                }
                
                // Se verifica si se trata de una constante entera.
                patron = Pattern.compile("^[-]?(\\d)+$");
                coincidencia = patron.matcher(palabra[j]);
                
                // Se activa la bandera que almacena la constante entera.
                if (coincidencia.matches()) {
                    
                    // Al tratarse de una asignacion, la constante se almacena.
                    if (asignacion) {
                        stb.append(palabra[j]).append(" ");
                    }
                    
                    // Se agrega el token.
                    if (tCE > 0) {
                        int t = codigo.valorToken.indexOf(palabra[j]);  //Se busca su posicion.
                        if (t != -1) {  //Si el token existe.
                            codigo.token.add(codigo.token.get(t));  //Se agrega el token de nuevo.
                            codigo.valorToken.add(codigo.valorToken.get(t));    //Junto a su valor correspondiente.
                        }
                        else {  //De no existir el token.
                            tCE++;  //Se aumenta el contador de tokens CE.
                            codigo.token.add("CE" +tCE);    //Se almacena el nuevo token.
                            codigo.valorToken.add(palabra[j]);  //Con su respectivo valor.
                        }
                    }
                    // Si no hay ningun token, se registra el primero
                    else {
                        tCE++;
                        codigo.token.add("CE" +tCE);
                        codigo.valorToken.add(palabra[j]);
                    }
                    
                    continue;
                }
                
                // Se verifica si se trata de una constante flotante.
                patron = Pattern.compile("^[-]?(\\d)+(.(\\d)+)$");
                coincidencia = patron.matcher(palabra[j]);
                
                // Se activa la bandera que almacena la constante flotante.
                if (coincidencia.matches()) {
                    
                    if (condicion) {
                        var = codigo.varSwitch.lastElement();
                        tipo = codigo.tipoSwitch.lastElement();
                    }
                    
                    // Se verifica que si existe incompatibilidad de tipos.
                    if (tipo.equalsIgnoreCase("int")) {
                        codigo.errores.add("ERROR: En la linea " +numLinea+ " hay incompatibilidad de tipos: " +var+ " es un int y " +palabra[j]+ " es un float.");
                        
                        error = true;
                    }
                    
                    // En caso contrario, almacenar la constante flotante.
                    else if (asignacion) {
                        stb.append(palabra[j]).append(" ");
                    }
                    
                    // Se agrega el token.
                    if (tCF > 0) {
                        int t = codigo.valorToken.indexOf(palabra[j]);  //Se busca su posicion.
                        if (t != -1) {  //Si el token existe.
                            codigo.token.add(codigo.token.get(t));  //Se agrega el token de nuevo.
                            codigo.valorToken.add(codigo.valorToken.get(t));    //Junto a su valor correspondiente.
                        }
                        else {  //De no existir el token.
                            tCF++;  //Se aumenta el contador de tokens CF.
                            codigo.token.add("CF" +tCF);    //Se almacena el nuevo token.
                            codigo.valorToken.add(palabra[j]);  //Con su respectivo valor.
                        }
                    }
                    // Si no hay ningun token, se registra el primero
                    else {
                        tCF++;
                        codigo.token.add("CF" +tCF);
                        codigo.valorToken.add(palabra[j]);
                    }
                    
                    if (j < palabra.length-1) { //Si no es la ultima palabra.
                        if (palabra[j+1].replace(" ", "").equalsIgnoreCase(":")) {  //Se quitan los espacios y se verifica si la siguiente palabra son dos puntos.
                            tipo = "null";  //De ser asi se regresa a null el tipo base.
                            var = "";   //Y la variable base se elimina.
                        }
                    }
                    
                    continue;
                }
                
                // Se verifica si se trata de un operador aritmetico.
                patron = Pattern.compile("^[+-/*]$");
                coincidencia = patron.matcher(palabra[j]);
                
                // Se activa la bandera que almacena al operador aritmetico.
                if (coincidencia.matches()) {
                    
                    // Al tratarse de una asignacion, el operador aritmetico se almacena.
                    if (asignacion) {
                        stb.append(palabra[j]).append(" ");
                    }
                    
                    // Si se intenta dividir con una variable base int.
                    if (tipo.equalsIgnoreCase("int") && palabra[j].contains("/")) {
                        codigo.errores.add("ERROR: En la linea " +numLinea+ " puede haber perdida de informacion al dividir un int.");

                        error = true;
                    }
                    
                    // Se agrega el token.
                    if (tOA > 0) {
                        int t = codigo.valorToken.indexOf(palabra[j]);  //Se busca su posicion.
                        if (t != -1) {  //Si el token existe.
                            codigo.token.add(codigo.token.get(t));  //Se agrega el token de nuevo.
                            codigo.valorToken.add(codigo.valorToken.get(t));    //Junto a su valor correspondiente.
                        }
                        else {  //De no existir el token.
                            tOA++;  //Se aumenta el contador de tokens OA.
                            codigo.token.add("OA" +tOA);    //Se almacena el nuevo token.
                            codigo.valorToken.add(palabra[j]);  //Con su respectivo valor.
                        }
                    }
                    // Si no hay ningun token, se registra el primero
                    else {
                        tOA++;
                        codigo.token.add("OA" +tOA);
                        codigo.valorToken.add(palabra[j]);
                    }
                    
                    
                    continue;
                }
                
                // Se verifica si se trata de un operador relacional.
                patron = Pattern.compile("([<>][=]?)$|([!=][=])$");
                coincidencia = patron.matcher(palabra[j]);
                
                // Se activa la bandera que almacena al operador relacional.
                if (coincidencia.matches()) {
                    
                    // Al tratarse de una asignacion, el operador relacional se almacena.
                    if (asignacion) {
                        stb.append(palabra[j]).append(" ");
                    }
                    
                    // Se agrega el token.
                    if (tOR > 0) {
                        int t = codigo.valorToken.indexOf(palabra[j]);  //Se busca su posicion.
                        if (t != -1) {  //Si el token existe.
                            codigo.token.add(codigo.token.get(t));  //Se agrega el token de nuevo.
                            codigo.valorToken.add(codigo.valorToken.get(t));    //Junto a su valor correspondiente.
                        }
                        else {  //De no existir el token.
                            tOR++;  //Se aumenta el contador de tokens OR.
                            codigo.token.add("OR" +tOR);    //Se almacena el nuevo token.
                            codigo.valorToken.add(palabra[j]);  //Con su respectivo valor.
                        }
                    }
                    // Si no hay ningun token, se registra el primero
                    else {
                        tOR++;
                        codigo.token.add("OR" +tOR);
                        codigo.valorToken.add(palabra[j]);
                    }
                    
                    continue;
                }
                
                // Se verifica si se trata de un operador booleano.
                patron = Pattern.compile("([&][&])$|([|][|])$");
                coincidencia = patron.matcher(palabra[j]);
                
                // Se activa la bandera que almacena al operador booleano
                if (coincidencia.matches()) {
                    
                    // Al tratarse de una asignacion, el operador booleano se almacena.
                    if (asignacion) {
                        stb.append(palabra[j]).append(" ");
                    }
                    
                    // Se agrega el token.
                        if (tOB > 0) {
                            // Primero debemos verificar que el token no exista.
                            // t guarda la posicion del token a analizar.
                            int t = 0;
                            // tokensReg guarda el total de tokens registrados hasta el momento.
                            int tokensReg = codigo.token.size();
                            while (t < tokensReg) {
                                
                                // Si el token existe.
                                if (codigo.valorToken.get(t).equals(palabra[j])) {
                                    // Se guarda el token existente.
                                    codigo.token.add(codigo.token.get(t));
                                    codigo.valorToken.add(codigo.valorToken.get(t));
                                    nuevo = false;
                                    break;
                                }
                                
                                t++;
                                
                            }
                            
                            if (nuevo) {
                                // Se guarda el nuevo token.
                                tOB++;
                                codigo.token.add("OB" +tOB);
                                codigo.valorToken.add(palabra[j]);
                            }
                        }
                        // Si no hay ningun token, se registra el primero
                        else {
                            tOB++;
                            codigo.token.add("OB" +tOB);
                            codigo.valorToken.add(palabra[j]);
                        }
                    
                    
                    continue;
                }
                
                // Se verifica si es una variable.
                patron = Pattern.compile("^[A-Za-z_$][\\w$]*$");
                coincidencia = patron.matcher(palabra[j]);
                
                // Si coincide se asigna el tipo base si no ha sido declarado antes.
                if (coincidencia.matches()) {
                    
                    // Variable donde se almacena el tipo de la variable que se analiza.
                    String tipoPalabra = "null";
                    
                    // Se verifica si esta es la variable base de la linea, de ser asi, se almacena.
                    if (tipoBase && !varBase) {
                        var = palabra[j];
                        tipoPalabra = tipo;
                        varBase = true;
                    }
                    
                    // Bandera indicadora de si se pasa a la siguiente variable.
                    Boolean salto = false;
                    
                    // Variable donde se almacena el valor de la variable que se analiza.
                    String valorPalabra = valor;
                    
                    int k = codigo.variable.indexOf(palabra[j]);    //Se busca si la variable ha sido registrada antes.
                    if (k != -1) {  //Si ha sido registrada antes.
                            
                        // Se obtiene y almacena el tipo de la variable.
                        tipoPalabra = codigo.tipo.get(k);
                            
                        // Se analiza si la variable ha sido declarada previamente.
                        if (!tipo.equalsIgnoreCase("null") && !asignacion) {
                            codigo.errores.add("ERROR: En la linea " +numLinea+ " la variable " + palabra[j] + " ha sido declarada previamente.");

                            error = true;
                        }
                            
                        // Se obtiene y almacena el valor de la variable.
                        valorPalabra = codigo.valor.get(k);
                            
                        // Se indica que la palabra se ha encontrado.
                        salto = true;
                    }
                    
                    // Se verifica de nuevo si esta es la variable de la base de linea pero sin declaracion.
                    if (j == 0) {
                        var = palabra[j];
                        tipo = tipoPalabra;
                        varBase = true;
                    }
                    else if (palabra[j-1].equalsIgnoreCase(":")) {
                        var = palabra[j];
                        tipo = tipoPalabra;
                        varBase = true;
                    }
                    
                    if (j >= 2) { //Si estamos trabajando con la palabra en 2da posicion o mayor.
                        if (palabra[j-2].replace(" ", "").equalsIgnoreCase("switch")) { //Se le quita espacios para comprobar si la palabra es un "switch".
                            condicion = true; //Se marca que estaremos trabajando con una condicion switch.
                            codigo.varSwitch.push(palabra[j]);
                            codigo.tipoSwitch.push(tipoPalabra);
                        }
                    }
                    
                    if (condicion) {    //Si se trata de una condicion Switch.
                        tipo = codigo.tipoSwitch.lastElement(); //Se almacena el tipo del Switch.
                        tipoBase = true;    //Se marca que el tipo base se ha asignado.
                    }
                    
                    // Se analiza si la variable no ha sido declarada.
                    if (tipoPalabra.equalsIgnoreCase("null")) {
                        codigo.errores.add("ERROR: En la linea " +numLinea+ " la variable " + palabra[j] + " no esta declarada.");
                        
                        error = true;
                    }
                    
                    // Se analiza si se esta intentando asignar una variable incompatible o no inicializada.
                    if (asignacion || condicion) {
                        
                        // Se analiza si el tipo base no es compatible con el tipo que se analiza.
                        if (tipo.equalsIgnoreCase("int") && (tipoPalabra.equalsIgnoreCase("double") || tipoPalabra.equalsIgnoreCase("float"))) {
                            codigo.errores.add("ERROR: En la linea " +numLinea+ " hay incompatibilidad de tipos: " +var+ " es un int y " +palabra[j]+ " es " +tipoPalabra+ ".");

                            error = true;
                        }
                        
                        // Se analiza si la variable no se encuentra inicializada.
                        if (valorPalabra.equalsIgnoreCase("null")) {
                            codigo.errores.add("ERROR: En la linea " +numLinea+ " la variable " + palabra[j] + " no esta inicializada.");

                            error = true;
                        }
                        
                        // Si ningun error fue encontrado, se almacena en stb para despues asignar el valor a la variable.
                        if (!error) {
                            stb.append(palabra[j]).append(" ");
                        }
                    }
                    
                    // Se agrega el token.
                    if (tIDE > 0) {
                        // Primero debemos verificar que el token no exista.
                        // t guarda la posicion del token a analizar.
                        int t = 0;
                        // tokensReg guarda el total de tokens registrados hasta el momento.
                        int tokensReg = codigo.token.size();
                        while (t < tokensReg) {
                            
                            // Si el token existe.
                            if (codigo.valorToken.get(t).equals(palabra[j])) {
                                // Se guarda el token existente.
                                codigo.token.add(codigo.token.get(t));
                                codigo.valorToken.add(codigo.valorToken.get(t));
                                nuevo = false;
                            
                                break;
                            }
                                
                            t++;
                                
                        }
                            
                        if (nuevo) {
                            // Se guarda el nuevo token.
                            tIDE++;
                            codigo.token.add("IDE" +tIDE);
                            codigo.valorToken.add(palabra[j]);
                        }
                    }
                    // Si no hay ningun token, se registra el primero
                    else {
                        tIDE++;
                        codigo.token.add("IDE" +tIDE);
                        codigo.valorToken.add(palabra[j]);
                    }
                    
                    // Se verifica la siguiente palabra si no es necesario agregar.
                    if (salto) {
                        continue;
                    }
                    
                    // La variable se registra
                    codigo.variable.add(palabra[j]);
                    codigo.tipo.add(tipoPalabra);
                    codigo.valor.add(valorPalabra);
                    
                    
                    
                }
                
            }
            
            // Si la linea tuvo una asignacion, esta se agrega en esta fase.
            if (asignacion) {
                asignar(var, stb.toString(), error); //Se realiza la asignacion.
            }
        }
        
        // Debemos generar el archivo de Tokens
        // Primero debemos guardar en un string todos los tokens encontrados
        StringBuilder stbTokens = new StringBuilder();
        
        // Recorremos todos los tokens
        for (String tokenStr: codigo.token) {
        	if (tokenStr.contains("DEL"))
        		stbTokens.append(String.format("%s%s", tokenStr, LINEA));
        	else if (tokenStr.contains("COR1"))
        		stbTokens.append(String.format("%s%s", tokenStr, LINEA));
        	else
        		stbTokens.append(String.format("%s ", tokenStr));
        }
        
        // Guardamos el archivo de Tokens
        Archivo.guardar("Tokens.txt", stbTokens.toString());
        
        // Se genera el ensamblador
        //generarEnsamblador();
        
        return codigo;
    }
    
    public static void asignar(String var, String valor, Boolean error) {
        if (!error) { //Si no hay error en la instruccion.
            int posVar = codigo.variable.indexOf(var); //Se encuentra la posicion de la variable a asignar.
            codigo.valor.set(posVar, valor); //Se le asigna el valor a la variable.
        }
    }
    
}
