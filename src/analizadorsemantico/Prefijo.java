package analizadorsemantico;

import java.util.*; 

public class Prefijo {
    
    // Metodo para saber si se trata de un operador
    private static boolean esOperador(char c) 
    { 
        return (!(c >= 'a' && c <= 'z') && 
        !(c >= '0' && c <= '9') && 
        !(c >= 'A' && c <= 'Z')); 
    } 

    // Metodo para obtener la prioridad de la operacion para seguir la jerarquia 
    private static int obtenerPrioridad(char C) { 
        if (C == '-' || C == '+') 
            return 1; 
        else if (C == '*' || C == '/') 
            return 2; 
        else if (C == '^') 
            return 3; 
        return 0; 
    } 

    // Metodo que convierte una operacion de Infija a Prefija 
    public static String convertirInfijoaPrefijo(String infijo) { 
        
        // Creamos dos pilas, uno para operadores
        Stack<Character> operadores = new Stack<Character>(); 

        // Y otra para operandos
        Stack<String> operandos = new Stack<String>(); 

        for (int i = 0; i < infijo.length(); i++) 
        { 

            // Si este caracter se trata de un parentesis
            // inicial, este debe ser ingresado en
            // la pila de operadores
            if (infijo.charAt(i) == '(') 
            { 
                operadores.push(infijo.charAt(i)); 
            } 

            // If current character is a 
            // closing bracket, then pop from 
            // both stacks and push result 
            // in operandos stack until 
            // matching opening bracket is 
            // not found. 
            else if (infijo.charAt(i) == ')') 
            { 
                while (!operadores.empty() && 
                        operadores.peek() != '(') { 

                    // operand 1 
                    String op1 = operandos.peek(); 
                    operandos.pop(); 

                    // operand 2 
                    String op2 = operandos.peek(); 
                    operandos.pop(); 

                    // operator 
                    char op = operadores.peek(); 
                    operadores.pop(); 

                    // Add operandos and operator 
                    // in form operator + 
                    // operand1 + operand2. 
                    String tmp = op + op2 + op1; 
                    operandos.push(tmp); 
                } 

                // Pop opening bracket 
                // from stack. 
                operadores.pop(); 
            } 

            // If current character is an 
            // operand then push it into 
            // operandos stack. 
            else if (!esOperador(infijo.charAt(i))) 
            { 
                operandos.push(infijo.charAt(i) + ""); 
            } 

            // If current character is an 
            // operator, then push it into 
            // operadores stack after popping 
            // high priority operadores from 
            // operadores stack and pushing 
            // result in operandos stack. 
            else { 
                while (!operadores.empty() && 
                    obtenerPrioridad(infijo.charAt(i)) <= 
                    obtenerPrioridad(operadores.peek())) { 

                    String op1 = operandos.peek(); 
                    operandos.pop(); 

                    String op2 = operandos.peek(); 
                    operandos.pop(); 

                    char op = operadores.peek(); 
                    operadores.pop(); 

                    String tmp = op + op2 + op1; 
                    operandos.push(tmp); 
                } 

                operadores.push(infijo.charAt(i)); 
            } 
        } 

        // Pop operadores from operadores 
        // stack until it is empty and 
        // operation in add result of 
        // each pop operandos stack. 
        while (!operadores.empty()) 
        { 
                String op1 = operandos.peek(); 
                operandos.pop(); 

                String op2 = operandos.peek(); 
                operandos.pop(); 

                char op = operadores.peek(); 
                operadores.pop(); 

                String tmp = op + op2 + op1; 
                operandos.push(tmp); 
        } 

        // Final prefix expression is 
        // present in operandos stack. 
        return operandos.peek(); 
    } 
}
