/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsemantico;

import java.util.ArrayList;
import java.util.Stack;


public class Intermedio {
    
    private static final String LINEA = System.getProperty("line.separator"); //Variable que genera los saltos de linea detectando el sistema del usuario.
    
    public static Codigo generarIntermedio (Codigo codigo) {
        
        // Indica el numero de instruccion.
        int numIns = 1;
        
        // Numero de temporal
        int numIf = 1;  //Numero de condicion para la variable temporal.
        int numElse = 1;
        
        // Variables que almacenaran la tripleta
        ArrayList<Integer> numeroIns = new ArrayList<>();
        ArrayList<String> objeto = new ArrayList<>();
        ArrayList<String> fuente = new ArrayList<>();
        ArrayList<String> operador = new ArrayList<>();
        Stack<String> variableIf = new Stack<>();
        Stack<String> variableElse = new Stack<>();
        Stack<String> ultimoOperador = new Stack<>();
        
        // se verifican los tokens uno por uno.
        for (int i = 0; i < codigo.token.size(); i++) {
            
            // cuando es una palabra reservada
            if(codigo.token.get(i).contains("PR")) {
                
                // si es un "if".
                if (codigo.valorToken.get(i).equalsIgnoreCase("if")) {
                    
                    
                    // Se guarda en la tripleta la variable a comprobar
                    numeroIns.add(numIns);
                    objeto.add("T1");
                    fuente.add(codigo.valorToken.get(i+2));
                    operador.add("=");
                    numIns++;
                    
                    // Avanzamos a la siguiente variable
                    i += 3;
                    
                    // Se guarda en la tripleta el valor de la comprobacion
                    numeroIns.add(numIns);
                    objeto.add("T1");
                    fuente.add(codigo.valorToken.get(i+1));
                    operador.add(codigo.valorToken.get(i));
                    numIns++;
                    i++;
                    
//                    // Si es verdadero, debe continuar con la siguiente instruccion.
//                    int sigIns = numIns + 2;
//                    numeroIns.add(numIns);
//                    objeto.add("TRUE");
//                    fuente.add(String.format("%d", sigIns));
//                    operador.add("TR" +numIf);
//                    numIns++;
//                    
//                    // Si es falso, se debe saltar a fuera de la condicion
//                    numeroIns.add(numIns);
//                    objeto.add("FALSE");
//                    fuente.add("Reemplazar"+variableIf.size());
//                    operador.add("TR" +numIf);
                    
                    // Si es verdadero, debe continuar con la siguiente instruccion.
                    int sigIns = numIns + 2;
                    numeroIns.add(numIns);
                    objeto.add("TR" +numIf);
                    fuente.add("TRTRUE");
                    operador.add(sigIns+"");
                    numIns++;
                    
                    // Si es falso, se debe enviar hasta despues del fin de la instruccion if
                    // Debemos ver si el siguiente token es un COR1 "{" para saber si tendra varias instrucciones
                    int indexIf = i;
                    Boolean corEncontrado = false;
                    // Se debe encontrar el corchete, un delimitador o COR2 puede terminar esta busqueda, incluso el fin del documento
                    while (indexIf < codigo.token.size()) {
                    	if (codigo.token.get(indexIf).equalsIgnoreCase("COR1")) {
                    		corEncontrado = true;
                    		break;
                    	}
                    	indexIf++;
                    }
                    if (corEncontrado) {
                    	variableIf.push("T1");  //Se guarda la variable temporal del if.
                    	
                    	numeroIns.add(numIns);
                        objeto.add("TR" +numIf);
                        fuente.add("TRFALSE");
                        operador.add("Reemplazar"+variableIf.size());
                        
                        // Se indica que el último operador ingresado es el if
                        ultimoOperador.push("if");
                	}
                	// En caso contrario, debemos hacer la salida dos lineas despues
                	else {
                        // Se declara un salto
                		numeroIns.add(numIns);
                        objeto.add("TR" +numIf);
                        fuente.add("TRFALSE");
                        operador.add(String.format("%d", numIns+2));
                        
                	}
                    numIns++;
                    
                    continue;
                }
                
                else if (codigo.valorToken.get(i).equalsIgnoreCase("else")) {
                	
                	// Debemos ver si el siguiente token es un COR1 "{" para saber si tendra varias instrucciones
                	if (codigo.token.get(i+1).equalsIgnoreCase("COR1")) {
                		variableElse.push(String.format("Else%d", numElse));  //Se guarda la variable temporal del else.
                    	
                        // Se declara un salto
                        numeroIns.add(numIns);
                        objeto.add("JUMP");
                        fuente.add(String.format("Else%d", numElse));
                        operador.add(String.format("%d", numIns));
                        numElse++;
                        
                        // Se indica que el último operador ingresado es el else
                        ultimoOperador.push("else");
                	}
                	// En caso contrario, debemos hacer el JUMP dos lineas despues
                	else {
                        // Se declara un salto
                        numeroIns.add(numIns);
                        objeto.add("JUMP");
                        fuente.add("");
                        operador.add(String.format("%d", numIns+2));
                	}
                	numIns++;
                    
                    continue;
                    
                }
                
                else {
                    continue;
                }
                
            }
            
            if (codigo.token.get(i).equalsIgnoreCase("COR2")) { //Si estamos ante el final de una instruccion.
            	int sigIns = numIns; //Variable auxiliar para saber el numero de la siguiente instruccion.
            	// Revisamos si tenemos que revisar condicionales
            	if (ultimoOperador.size() > 0) {
            		// Obtenemos el ultimo operador aplicado
            		String operadorEvaluar = ultimoOperador.pop();
            		// Entonces definimos si es un if o else
            		if (operadorEvaluar.equalsIgnoreCase("if")) {
            			// La siguiente instrucción es una más
            			sigIns++;
            			if (variableIf.size() > 0) {    //Si hay al menos una condicional if activa.
                            while (operador.indexOf("Reemplazar"+variableIf.size()) != -1) {    //Se va realizar de manera ciclica el reemplazo de numero de instruccion mientras exista.
                                operador.set(operador.indexOf("Reemplazar"+variableIf.size()), sigIns+""); //Se reemplaza y se indica donde empieza la siguiente instruccion.
                            }
                            // Se elimina la ultima condicional
                            variableIf.pop();
                        }
            		}
            		else if (operadorEvaluar.equalsIgnoreCase("else")) {
            			if (variableElse.size() > 0) {
                        	while (fuente.indexOf(String.format("Else%d", variableElse.size())) != -1) {    //Se va realizar de manera ciclica el reemplazo de numero de instruccion mientras exista.
                                operador.set(fuente.indexOf(String.format("Else%d", variableElse.size())), sigIns+""); //Se reemplaza y se indica donde empieza la siguiente instruccion.
                                // Ahora que hemos utilizado la fuente, debemos vaciarla
                                fuente.set(fuente.indexOf(String.format("Else%d", variableElse.size())), "");
                            }
                            // Se elimina la ultima condicional
                            variableElse.pop();
                        }
            		}
            	}
            }
            
            // Si el token es un operador de asignacion.
            if(codigo.token.get(i).equalsIgnoreCase("OAS")) {
                
                // Se considera si hay un delimitador despues de su siguiente token
                if (codigo.token.get(i+2).equalsIgnoreCase("DEL") || codigo.token.get(i+2).equalsIgnoreCase("COM")) {
                    
                    // De ser asi estamos ante una asignacion simple y la tripleta.
                    numeroIns.add(numIns);
                    objeto.add(codigo.valorToken.get(i-1));
                    fuente.add(codigo.valorToken.get(i+1));
                    operador.add("=");
                    numIns++;
                }
                
                // En este caso, tenemos una probable operacion matematica.
                else {
                    
                    // Variable a la que se asignara el resultado final.
                    String variableAsig = codigo.valorToken.get(i-1);
                    
                    // se almacenan los tokens de la operacion.
                    ArrayList<String> tokensOperacion = new ArrayList<>();
                    
                    // Saltamos el guardado del operador de asignacion.
                    i++;
                    
                    while (!codigo.token.get(i).equalsIgnoreCase("DEL")) {
                        tokensOperacion.add(codigo.valorToken.get(i));
                        i++;
                    }
                    
                    // Se va recorriendo la lista hasta que no queden variables por comparar
                    while (tokensOperacion.size() > 1) {
                        
                        // Si hay un parentesis en la operacion.
                        if (tokensOperacion.indexOf(")") != -1) {
                            
                            // Se almacenan las posiciones.
                            int posFin = tokensOperacion.indexOf(")");
                            int posIni = posFin - 2;
                            
                            // Se revisa que antes dos tokens anteriores no sean "(".
                            if (!tokensOperacion.get(posIni).equalsIgnoreCase("(")) {
                                
                                // Se busca el inicio de este parentesis
                                for (int n = posFin; n >= 0; n--) {
                                    
                                    // Si se encuentra el parentesis.
                                    if (tokensOperacion.get(n).equalsIgnoreCase("(")) {
                                        // Se guarda la posicion.
                                        posIni = n;
                                        
                                        // Se termina el ciclo.
                                        break;
                                    }
                                }
                                
                                // Se guarda la operacion que se encuentra dentro del parentesis.
                                ArrayList<String> tokensParentesis = new ArrayList<>();
                                
                                // Se recorre el arreglo hasta que encontremos el final del parentesis.
                                int posReemplazo = posIni + 1;
                                while(!tokensOperacion.get(posReemplazo+1).equalsIgnoreCase(")")) {
                                    // Se guarda la operacion
                                    tokensParentesis.add(tokensOperacion.remove(posReemplazo));
                                    // Si es el ultimo
                                    if (tokensOperacion.get(posReemplazo+1).equalsIgnoreCase(")")) {
                                        tokensParentesis.add(tokensOperacion.get(posReemplazo));
                                    }
                                }
                                
                                // Se analiza lo que se encuentra adentro del parentesis.
                                while (tokensParentesis.size() > 1) {
                                    
                                    // Si hay una multiplicacion en la operacion.
                                    if (tokensParentesis.lastIndexOf("*") != -1) {

                                    	// Variable que almacenara la fuente temporal
                                		String fuenteTemp = "";
                                    	// Debemos revisar si el anterior objeto no es T1
                                    	if (!objeto.get(objeto.size()-1).equalsIgnoreCase("T1")) {
                                    		// Se almacena la antepenultima variable en el triplo y se elimina de la lista.
                                            numeroIns.add(numIns);
                                            objeto.add("T1");
                                            fuente.add(tokensParentesis.remove(tokensParentesis.lastIndexOf("*")-1));
                                            operador.add("=");
                                            numIns++;

                                            // En el triplo se almacena la operacion realizada.
                                            numeroIns.add(numIns);
                                            objeto.add("T1");
                                            fuenteTemp = tokensParentesis.remove(tokensParentesis.lastIndexOf("*")+1);
                                            fuente.add(fuenteTemp);
                                            operador.add(tokensParentesis.remove(tokensParentesis.lastIndexOf("*")));
                                            numIns++;
                                    	}
                                    	else {
                                    		fuenteTemp = tokensParentesis.remove(tokensParentesis.lastIndexOf("*")-1);
                                    		// Se almacena la antepenultima variable en el triplo y se elimina de la lista.
                                            numeroIns.add(numIns);
                                            objeto.add("T1");
                                            fuente.add(fuenteTemp);
                                            // Nos saltamos una operacion
                                            fuenteTemp = tokensParentesis.remove(tokensParentesis.lastIndexOf("*")+1);
                                            // Asignamos el operador
                                            operador.add(tokensParentesis.remove(tokensParentesis.lastIndexOf("*")));
                                            numIns++;
                                    	}
                                    	// Si hay más de un elemento
                                    	if (tokensParentesis.size() > 1)
            	                        	// Se agrega a la lista el triplo realizado.
            	                            tokensParentesis.add(fuenteTemp);
                                    	else
                                    		// Si lo que continua es la operacion, nos preparamos para asignar el triplo
                                    		tokensParentesis.add("T1");

                                        continue;
                                    }

                                    // Si hay una division en la operacion.
                                    else if (tokensParentesis.lastIndexOf("/") != -1) {

                                    	// Variable que almacenara la fuente temporal
                                		String fuenteTemp = "";
                                    	// Debemos revisar si el anterior objeto no es T1
                                    	if (!objeto.get(objeto.size()-1).equalsIgnoreCase("T1")) {
                                    		// Se almacena la antepenultima variable en el triplo y se elimina de la lista.
                                            numeroIns.add(numIns);
                                            objeto.add("T1");
                                            fuente.add(tokensParentesis.remove(tokensParentesis.lastIndexOf("/")-1));
                                            operador.add("=");
                                            numIns++;

                                            // En el triplo se almacena la operacion realizada.
                                            numeroIns.add(numIns);
                                            objeto.add("T1");
                                            fuenteTemp = tokensParentesis.remove(tokensParentesis.lastIndexOf("/")+1);
                                            fuente.add(fuenteTemp);
                                            operador.add(tokensParentesis.remove(tokensParentesis.lastIndexOf("/")));
                                            numIns++;
                                    	}
                                    	else {
                                    		fuenteTemp = tokensParentesis.remove(tokensParentesis.lastIndexOf("/")-1);
                                    		// Se almacena la antepenultima variable en el triplo y se elimina de la lista.
                                            numeroIns.add(numIns);
                                            objeto.add("T1");
                                            fuente.add(fuenteTemp);
                                            // Nos saltamos una operacion
                                            fuenteTemp = tokensParentesis.remove(tokensParentesis.lastIndexOf("/")+1);
                                            // Asignamos el operador
                                            operador.add(tokensParentesis.remove(tokensParentesis.lastIndexOf("/")));
                                            numIns++;
                                    	}
                                    	// Si hay más de un elemento
                                    	if (tokensParentesis.size() > 1)
            	                        	// Se agrega a la lista el triplo realizado.
            	                            tokensParentesis.add(fuenteTemp);
                                    	else
                                    		// Si lo que continua es la operacion, nos preparamos para asignar el triplo
                                    		tokensParentesis.add("T1");

                                        continue;
                                    }

                                    // Si solo quedan sumas y restas.
                                    else {
                                    	
                                    	// Variable que almacenara la fuente temporal
                                		String fuenteTemp = "";
                                    	// Debemos revisar si el anterior objeto no es T1
                                    	if (!objeto.get(objeto.size()-1).equalsIgnoreCase("T1")) {
                                    		// Se almacena la antepenultima variable en el triplo y se elimina de la lista.
                                            numeroIns.add(numIns);
                                            objeto.add("T1");
                                            fuente.add(tokensParentesis.remove(tokensParentesis.size()-3));
                                            operador.add("=");
                                            numIns++;

                                            // En el triplo se almacena la operacion realizada.
                                            numeroIns.add(numIns);
                                            objeto.add("T1");
                                            fuenteTemp = tokensParentesis.remove(tokensParentesis.size()-1);
                                            fuente.add(fuenteTemp);
                                            operador.add(tokensParentesis.remove(tokensParentesis.size()-1));
                                            numIns++;
                                    	}
                                    	else {
                                    		fuenteTemp = tokensParentesis.remove(tokensParentesis.size()-3);
                                    		// Se almacena la antepenultima variable en el triplo y se elimina de la lista.
                                            numeroIns.add(numIns);
                                            objeto.add("T1");
                                            fuente.add(fuenteTemp);
                                            // Nos saltamos una operacion
                                            fuenteTemp = tokensParentesis.remove(tokensParentesis.size()-1);
                                            // Asignamos el operador
                                            operador.add(tokensParentesis.remove(tokensParentesis.size()-1));
                                            numIns++;
                                    	}
                                    	// Si hay más de un elemento
                                    	if (tokensParentesis.size() > 1)
            	                        	// Se agrega a la lista el triplo realizado.
            	                            tokensParentesis.add(fuenteTemp);
                                    	else
                                    		// Si lo que continua es la operacion, nos preparamos para asignar el triplo
                                    		tokensParentesis.add("T1");
                                    }
                                    
                                }
                                
                                // Ahora la variable temporal restante se pasa al tokensOperacion.
                                tokensOperacion.set(posReemplazo, tokensParentesis.get(0));
                                
                                // El ciclo analiza la siguiente variable.
                                continue;
                            }
                            
                            // En este caso, la operacion no tiene mas operaciones alrededor
                            else {
                                
                                // Primero debemos comprobar que no estemos en el final del lado derecho.
                                if (posFin < tokensOperacion.size()-1) {
                                    
                                    // Se verifica que haya una suma, resta, division o multiplicacion o parentesis final del lado derecho
                                    if (tokensOperacion.get(posFin+1).equalsIgnoreCase("+") || tokensOperacion.get(posFin+1).equalsIgnoreCase("-") || tokensOperacion.get(posFin+1).equalsIgnoreCase("/") || tokensOperacion.get(posFin+1).equalsIgnoreCase("*") || tokensOperacion.get(posFin+1).equalsIgnoreCase(")")) {
                                        
                                        // De ser asi, se elimina el parentesis
                                        tokensOperacion.remove(posFin);
                                    }
                                    
                                    // En cualquier otro caso, se cambia el parentesis por una multiplicacion
                                    else {
                                        tokensOperacion.set(posFin, "*");
                                    }
                                        
                                }
                                
                                // Al ser el final, solamente se elimina el parentesis.
                                else {
                                    tokensOperacion.remove(posFin);
                                }
                                
                                // Ahora debemos comprobar que no estemos al principio del lado izquierdo.
                                if (posIni > 0) {
                                    // Se verifica que haya una suma, resta, division, multiplicacion o parentesis del lado izquierdo.
                                    if (tokensOperacion.get(posIni-1).equalsIgnoreCase("+") || tokensOperacion.get(posIni-1).equalsIgnoreCase("-") || tokensOperacion.get(posIni-1).equalsIgnoreCase("/") || tokensOperacion.get(posIni-1).equalsIgnoreCase("*") || tokensOperacion.get(posIni-1).equalsIgnoreCase(")") || tokensOperacion.get(posIni-1).equalsIgnoreCase("(")) {
                                        
                                        // De ser asi, se elimina el parentesis
                                        tokensOperacion.remove(posIni);
                                    }
                                    
                                    // En cualquier otro caso, se cambia el parentesis por una multiplicacion
                                    else {
                                        tokensOperacion.set(posIni, "*");
                                    }
                                    
                                }
                                
                                // Al ser el principio, solamente se elimina el parentesis.
                                else {
                                    tokensOperacion.remove(posIni);
                                }
                                
                            }
                            
                            continue;
                        }
                        
                        // Si hay una multiplicacion en la operacion.
                        else if (tokensOperacion.lastIndexOf("*") != -1) {
                        	
                        	// Variable que almacenara la fuente temporal
                    		String fuenteTemp = "";
                        	// Debemos revisar si el anterior objeto no es T1
                        	if (!objeto.get(objeto.size()-1).equalsIgnoreCase("T1")) {
                        		// Se almacena la antepenultima variable en el triplo y se elimina de la lista.
                                numeroIns.add(numIns);
                                objeto.add("T1");
                                fuente.add(tokensOperacion.remove(tokensOperacion.lastIndexOf("*")-1));
                                operador.add("=");
                                numIns++;

                                // En el triplo se almacena la operacion realizada.
                                numeroIns.add(numIns);
                                objeto.add("T1");
                                fuenteTemp = tokensOperacion.remove(tokensOperacion.lastIndexOf("*")+1);
                                fuente.add(fuenteTemp);
                                operador.add(tokensOperacion.remove(tokensOperacion.lastIndexOf("*")));
                                numIns++;
                        	}
                        	else {
                        		fuenteTemp = tokensOperacion.remove(tokensOperacion.lastIndexOf("*")-1);
                        		// Se almacena la antepenultima variable en el triplo y se elimina de la lista.
                                numeroIns.add(numIns);
                                objeto.add("T1");
                                fuente.add(fuenteTemp);
                                // Nos saltamos una operacion
                                fuenteTemp = tokensOperacion.remove(tokensOperacion.lastIndexOf("*")+1);
                                // Asignamos el operador
                                operador.add(tokensOperacion.remove(tokensOperacion.lastIndexOf("*")));
                                numIns++;
                        	}
                        	// Si hay más de un elemento
                        	if (tokensOperacion.size() > 1)
	                        	// Se agrega a la lista el triplo realizado.
	                            tokensOperacion.add(fuenteTemp);
                        	else
                        		// Si lo que continua es la operacion, nos preparamos para asignar el triplo
                        		tokensOperacion.add("T1");
                            
                            continue;
                        }
                        
                        // Si hay una division en la operacion.
                        else if (tokensOperacion.lastIndexOf("/") != -1) {
                        	
                        	// Variable que almacenara la fuente temporal
                    		String fuenteTemp = "";
                        	// Debemos revisar si el anterior objeto no es T1
                        	if (!objeto.get(objeto.size()-1).equalsIgnoreCase("T1")) {
                        		// Se almacena la antepenultima variable en el triplo y se elimina de la lista.
                                numeroIns.add(numIns);
                                objeto.add("T1");
                                fuente.add(tokensOperacion.remove(tokensOperacion.lastIndexOf("/")-1));
                                operador.add("=");
                                numIns++;

                                // En el triplo se almacena la operacion realizada.
                                numeroIns.add(numIns);
                                objeto.add("T1");
                                fuenteTemp = tokensOperacion.remove(tokensOperacion.lastIndexOf("/")+1);
                                fuente.add(fuenteTemp);
                                operador.add(tokensOperacion.remove(tokensOperacion.lastIndexOf("/")));
                                numIns++;
                        	}
                        	else {
                        		fuenteTemp = tokensOperacion.remove(tokensOperacion.lastIndexOf("/")-1);
                        		// Se almacena la antepenultima variable en el triplo y se elimina de la lista.
                                numeroIns.add(numIns);
                                objeto.add("T1");
                                fuente.add(fuenteTemp);
                                // Nos saltamos una operacion
                                fuenteTemp = tokensOperacion.remove(tokensOperacion.lastIndexOf("/")+1);
                                // Asignamos el operador
                                operador.add(tokensOperacion.remove(tokensOperacion.lastIndexOf("/")));
                                numIns++;
                        	}
                        	// Si hay más de un elemento
                        	if (tokensOperacion.size() > 1)
	                        	// Se agrega a la lista el triplo realizado.
	                            tokensOperacion.add(fuenteTemp);
                        	else
                        		// Si lo que continua es la operacion, nos preparamos para asignar el triplo
                        		tokensOperacion.add("T1");
                            
                            continue;
                        }
                        
                        // Si solo quedan sumas y restas.
                        else {
                        	// Variable que almacenara la fuente temporal
                    		String fuenteTemp = "";
                        	// Debemos revisar si el anterior objeto no es T1
                        	if (!objeto.get(objeto.size()-1).equalsIgnoreCase("T1")) {
                        		// Se almacena la antepenultima variable en el triplo y se elimina de la lista.
                                numeroIns.add(numIns);
                                objeto.add("T1");
                                fuente.add(tokensOperacion.remove(tokensOperacion.size()-3));
                                operador.add("=");
                                numIns++;

                                // En el triplo se almacena la operacion realizada.
                                numeroIns.add(numIns);
                                objeto.add("T1");
                                fuenteTemp = tokensOperacion.remove(tokensOperacion.size()-1);
                                fuente.add(fuenteTemp);
                                operador.add(tokensOperacion.remove(tokensOperacion.size()-1));
                                numIns++;
                        	}
                        	else {
                        		fuenteTemp = tokensOperacion.remove(tokensOperacion.size()-3);
                        		// Se almacena la antepenultima variable en el triplo y se elimina de la lista.
                                numeroIns.add(numIns);
                                objeto.add("T1");
                                fuente.add(fuenteTemp);
                                // Nos saltamos una operacion
                                fuenteTemp = tokensOperacion.remove(tokensOperacion.size()-1);
                                // Asignamos el operador
                                operador.add(tokensOperacion.remove(tokensOperacion.size()-1));
                                numIns++;
                        	}
                        	// Si hay más de un elemento
                        	if (tokensOperacion.size() > 1)
	                        	// Se agrega a la lista el triplo realizado.
	                            tokensOperacion.add(fuenteTemp);
                        	else
                        		// Si lo que continua es la operacion, nos preparamos para asignar el triplo
                        		tokensOperacion.add("T1");
                        }
                        
                    }
                    
                    // El resultado final es almacenado en la variable
                    numeroIns.add(numIns);
                    objeto.add(variableAsig);
                    fuente.add(tokensOperacion.get(0));
                    operador.add("=");
                    numIns++;
                    
                }
            }
        }
        
        // Ahora se genera el texto de la tripleta.
        StringBuilder texto = new StringBuilder("\tDato Objeto\tDato Fuente\tOperador");
        
        texto.append(LINEA);
        for (int i = 0; i < numeroIns.size(); i++) {
            texto.append(numeroIns.get(i)).append("\t")
                    .append(objeto.get(i)).append("\t\t")
                    .append(fuente.get(i)).append("\t\t")
                    .append(operador.get(i)).append("\t").append(LINEA);
        }
        texto.append(numeroIns.size()+1).append("\tFIN");
        
        // se guarda la tripleta
        codigo.tripleta = new Tripleta(numeroIns, objeto, fuente, operador);
        
        // Se guarda el archivo.
        Archivo.guardar("Codigo Intermedio.txt", texto.toString());
        
        return codigo;
    }
    
}
