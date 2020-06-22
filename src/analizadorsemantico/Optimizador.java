/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsemantico;

import java.util.ArrayList;

/**
 *
 * @author Misael
 */
public class Optimizador {
    
    private static final String LINEA = System.getProperty("line.separator"); //Variable que genera los saltos de línea detectando el sistema del usuario.
    
    // Metodo encargado de la optimización del código.
    public static Codigo optimizarCodigo(Codigo codigo) {
        
        // Objeto para la construcción de strings.
        StringBuilder stb = new StringBuilder();
        
        // Listas que almacenaran los tokens optimizados.
        ArrayList<String> tokensOpt = new ArrayList<>();
        ArrayList<String> valorTokensOpt = new ArrayList<>();
        
        // Se recorren los tokens
        int tamanioTokens = codigo.token.size();
        
        for (int i = 0; i < tamanioTokens; i++) {
            String actualToken = codigo.token.get(i);
            String actualValor = codigo.valorToken.get(i);
            String preToken = "";
            String preValor = "";
            String posToken = "";
            Boolean ultimo = true;
            
            // Se guarda el token anterior necesario para algunas comprobaciones.
            if (i > 0) {
                preToken = codigo.token.get(i-1);
                preValor = codigo.valorToken.get(i-1);
            }
            
            // Se guarda el token posterior necesario para algunas comprobaciones.
            if (i < tamanioTokens-1) {
                posToken = codigo.token.get(i+1);
                ultimo = false;
            }
            
            // Si hay un delimitador.
            if (actualToken.equalsIgnoreCase("DEL")) {
                
                // Si no es el último token.
                if (!ultimo) {
                    
                    // Verificar si el siguiente token es un corchete.
                    if (posToken.equalsIgnoreCase("COR2")) {
                        
                        // Se agrega el delimitador tal cual.
                        stb.append(actualValor);
                        tokensOpt.add(actualToken);
                        valorTokensOpt.add(actualValor);
                    }
                    
                    // Si no es un corchete
                    else {
                        
                        // Se agrega el delimitador junto a un salto de línea.
                        stb.append(actualValor).append(LINEA);
                        tokensOpt.add(actualToken);
                        valorTokensOpt.add(actualValor);
                    }
                }
                
                // Si es el último token.
                else {
                    
                    // Se agrega tal cual el delimitador.
                    stb.append(actualValor);
                    tokensOpt.add(actualToken);
                    valorTokensOpt.add(actualValor);
                }
            }
            
            // Si hay una palabra reservada
            else if (actualToken.contains("PR")) {
                
                // Si el anterior token es un parentesis.
                if (preToken.equalsIgnoreCase("PAR2")) {
                    
                    // Se realiza un salto de línea
                    stb.append(LINEA);
                }
                
                if (actualValor.equalsIgnoreCase("switch") || actualValor.equalsIgnoreCase("default") || actualValor.equalsIgnoreCase("break")) {
                    stb.append(actualValor);
                    tokensOpt.add(actualToken);
                    valorTokensOpt.add(actualValor);
                }
                else {
                    // Se escribe la palabra del token y un espacio.
                    stb.append(actualValor).append(" ");
                    tokensOpt.add(actualToken);
                    valorTokensOpt.add(actualValor);
                }
            }
            
            // Si hay un corchete inicial
            else if (actualToken.equalsIgnoreCase("COR1")) {
                
                // Se escribe un salto de línea y se escribe el corchete inicial.
                stb.append(LINEA);
                stb.append(actualValor);
                tokensOpt.add(actualToken);
                valorTokensOpt.add(actualValor);
            }
            
            // Si hay un corchete final
            else if (actualToken.equalsIgnoreCase("COR2")) {
                
                // Se escribe el corchete final y un salto de línea.
                stb.append(actualValor);
                tokensOpt.add(actualToken);
                valorTokensOpt.add(actualValor);
                stb.append(LINEA);
            }
            
            // Instrucción 3: Si hay una asignación.
            else if (actualToken.equalsIgnoreCase("OAS")) {
                
                // Se debe agregar el operador.
                stb.append(actualValor);
                tokensOpt.add(actualToken);
                valorTokensOpt.add(actualValor);
                
                // Bandera indicadora de que se ha encontrado el reemplazo
                Boolean reemplazo = false;
                
                // Se almacenará el codigo que contiene el IDE anterior en esta variable.
                String preCodigo = "";
                
                // Se ignora la optimizacion si se trata de una asignacion simple.
                if (codigo.token.get(i+2).equalsIgnoreCase("DEL")) {
                    continue;
                }
                
                // Se analizan todas las variables registradas.
                for (int j = 0; j < codigo.variable.size(); j++) {
                    
                    // Si la variable contiene de valor 1, se ignora.
                    if (codigo.valor.get(j).equalsIgnoreCase("1 ") || codigo.valor.get(j).equalsIgnoreCase("null")) {
                        continue;
                    }
                    
                    // Si se encuentra la variable con el token anterior
                    if (preValor.equalsIgnoreCase(codigo.variable.get(j))) {
                        
                        // Se escribe el codigo que contiene.
                        preCodigo = codigo.valor.get(j);
                        
                        // Se rompe el ciclo.
                        break;
                    }
                    
                }
                
                // Ahora se debe analizar en todos los IDE su valor para la sustitución.
                for (int j = 0; j < codigo.variable.size(); j++) {
                    
                    // Se obtiene el valor de las variables y se compara si contienen alguna parte del codigo.
                    // De ser así (y que no sea la misma variable).
                    if (preCodigo.contains(codigo.valor.get(j)) && !preValor.equalsIgnoreCase(codigo.variable.get(j)) && !codigo.valor.get(j).equalsIgnoreCase("1 ") && !codigo.valor.get(j).equalsIgnoreCase("null")) {
                        
                        // Se guarda el caracter anterior
                        int charAnt = preCodigo.indexOf(codigo.valor.get(j)) - 1;
                        
                        // Pero si la posición es -1, se evita una posible excepcion.
                        if (charAnt == -1) {
                            charAnt = 0;
                        }
                        
                        // Si antes de la parte a reemplazar no hay un espacio, se ignora.
                        if (preCodigo.charAt(charAnt) != ' ' && charAnt != 0) {
                            continue;
                        }
                        
                        // Se sustituye la parte del código por la variable.
                        preCodigo = preCodigo.replace(codigo.valor.get(j), codigo.variable.get(j)+ " ");
                        
                        // Bandera para indicar que si se realizo una sustitucion.
                        reemplazo = true;
                    }
                }
                
                if (reemplazo) {
                    
                    // Se guardan los tokens correspondientes al codigo.
                    String[] preCodigoTokens = preCodigo.split("\\s");
                    
                    // Se verifica lo capturado.
                    for (int j = 0; j < preCodigoTokens.length ; j++) {
                        
                        // Si la palabra del código actual es 1 y a continuación hay una multiplicación, el 1 se elimina.
                        if (preCodigoTokens[j].equalsIgnoreCase("1") && (preCodigoTokens[j+1].equalsIgnoreCase("(") || preCodigoTokens[j+1].equalsIgnoreCase("*"))) {
                            preCodigoTokens[j] = "";
                                
                            // Si había un asterisco, se elimina de igual forma.
                            if (preCodigoTokens[j+1].equalsIgnoreCase("*")) {
                                preCodigoTokens[j] = "";
                                j++;
                            }
                                
                            // Se termina el ciclo
                            continue;
                        }
                        
                        // Se lee el arreglo de tokens
                        for (int k = 0 ; k < codigo.token.size() ; k ++) {
                            
                            // Si el codigo concuerda con el valor del token.
                            if (preCodigoTokens[j].equalsIgnoreCase(codigo.valorToken.get(k))) {
                                
                                // Se almacena en la nueva lista de tokens.
                                tokensOpt.add(codigo.token.get(k));
                                valorTokensOpt.add(codigo.valorToken.get(k));
                                
                                // Se termina el ciclo
                                break;
                            }
                        }
                    }
                    
                    // Se eliminan los espacios.
                    preCodigo = Archivo.arregloTexto(preCodigoTokens);
                    stb.append(preCodigo);
                        
                    // Se salta todo el análisis de la línea (ya esta optimizado).
                    while (!codigo.token.get(i+1).equalsIgnoreCase("DEL")) {
                        i++;
                    }
                    
                }
                
                
            }
            
            // Con cualquier otro token, se escribe tal cual.
            else {
                stb.append(actualValor);
                tokensOpt.add(actualToken);
                valorTokensOpt.add(actualValor);
            }
            
            
        }
        
        // Se almacenan las listas con los nuevos tokens
        codigo.token = tokensOpt;
        codigo.valorToken = valorTokensOpt;
        
        // Se guarda el código optimizado
        Archivo.guardar("Optimizado.txt", stb.toString());
        
        return codigo;
    }
    
}
