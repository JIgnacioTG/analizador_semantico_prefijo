/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsemantico;


public class CPU {
    
    String AH, AL, BH, BL, CH, CL, DH, DL;
    
    public CPU () {
        AH = "";
        AL = "";
        BH = "";
        BL = "";
        CH = "";
        CL = "";
        DH = "";
        DL = "";
    }
    
    public String registrarVar(String variable) { //Método para el guardado de variables en los registros automatico.
        String registro = "";   //Variable que almacena el registro donde se guardo la variable.
        if (AH.isEmpty()) { //Primero se averigüa si AH esta vacia.
            AH = variable;  //De ser asi, se guarda la variable en el registro AH.
            registro = "AH";    //Se guarda el registro.
        }
        else if (AL.isEmpty()) {    //Si no se averigüa si AL esta vacia.
            AL = variable;  //De ser asi, se guarda la variable en el registro AL.
            registro = "AL";    //Se guarda el registro.
        }
        else if (BH.isEmpty()) {    //Si no se averigüa si BH esta vacia.
            BH = variable;  //De ser asi, se guarda la variable en el registro BH.
            registro = "BH";    //Se guarda el registro.
        }
        else if (BL.isEmpty()) {    //Si no se averigüa si BL esta vacia.
            BL = variable;  //De ser asi, se guarda la variable en el registro BL.
            registro = "BL";    //Se guarda el registro.
        }
        else if (CH.isEmpty()) {    //Si no se averigüa si CH esta vacia.
            CH = variable;  //De ser asi, se guarda la variable en el registro CH.
            registro = "CH";    //Se guarda el registro.
        }
        else if (CL.isEmpty()) {    //Si no se averigüa si CL esta vacia.
            CL = variable;  //De ser asi, se guarda la variable en el registro CL.
            registro = "CL";    //Se guarda el registro.
        }
        else if (DH.isEmpty()) {    //Si no se averigüa si DH esta vacia.
            DH = variable;  //De ser asi, se guarda la variable en el registro DH.
            registro = "DH";    //Se guarda el registro.
        }
        else {  // Si no, al ser DL el ultimo registro, se guarda en este registro el valor.
            DL = variable;  //La variable se encuentra almacenada en DL.
            registro = "DL";    //Se guarda el registro.
        }
        return registro;
    }
    
    public String registrarVar(String variable, String registro) {    //Método para el guardado de variables manual.
        String registroAnterior = "";   //Esta variable almacenara donde se guardo el valor anterior del registro.
        switch (registro) { //Se analiza a cual registro se guardara.
            case "AH":  if (AH.isEmpty()) { //Si el registro se encuentra vacio.
                            AH = variable;  //Se guarda en el registro la variable deseada.
                        }
                        else {  //De no ser asi.
                            registroAnterior = registrarVar(AH);   //Lo que almacena el registro AH se cambia de manera automatica.
                            AH = variable;  //Y AH ahora almacenara la variable deseada.
                        }
                        break;  //Fin de la operacion.
            case "AL":  if (AL.isEmpty()) { //Si el registro se encuentra vacio.
                            AL = variable;  //Se guarda en el registro la variable deseada.
                        }
                        else {  //De no ser asi.
                            registroAnterior = registrarVar(AL);   //Lo que almacena el registro AL se cambia de manera automatica.
                            AL = variable;  //Y AL ahora almacenara la variable deseada.
                        }
                        break;  //Fin de la operacion.
            case "BH":  if (BH.isEmpty()) { //Si el registro se encuentra vacio.
                            BH = variable;  //Se guarda en el registro la variable deseada.
                        }
                        else {  //De no ser asi.
                            registroAnterior = registrarVar(BH);   //Lo que almacena el registro BH se cambia de manera automatica.
                            BH = variable;  //Y BH ahora almacenara la variable deseada.
                        }
                        break;  //Fin de la operacion.
            case "BL":  if (BL.isEmpty()) { //Si el registro se encuentra vacio.
                            BL = variable;  //Se guarda en el registro la variable deseada.
                        }
                        else {  //De no ser asi.
                            registroAnterior = registrarVar(BL);   //Lo que almacena el registro BL se cambia de manera automatica.
                            BL = variable;  //Y AL ahora almacenara la variable deseada.
                        }
                        break;  //Fin de la operacion.
            case "CH":  if (CH.isEmpty()) { //Si el registro se encuentra vacio.
                            CH = variable;  //Se guarda en el registro la variable deseada.
                        }
                        else {  //De no ser asi.
                            registroAnterior = registrarVar(CH);   //Lo que almacena el registro CH se cambia de manera automatica.
                            CH = variable;  //Y CH ahora almacenara la variable deseada.
                        }
                        break;  //Fin de la operacion.
            case "CL":  if (CL.isEmpty()) { //Si el registro se encuentra vacio.
                            CL = variable;  //Se guarda en el registro la variable deseada.
                        }
                        else {  //De no ser asi.
                            registroAnterior = registrarVar(CL);   //Lo que almacena el registro CL se cambia de manera automatica.
                            CL = variable;  //Y CL ahora almacenara la variable deseada.
                        }
                        break;  //Fin de la operacion.
            case "DH":  if (DH.isEmpty()) { //Si el registro se encuentra vacio.
                            DH = variable;  //Se guarda en el registro la variable deseada.
                        }
                        else {  //De no ser asi.
                            registroAnterior = registrarVar(DH);   //Lo que almacena el registro DH se cambia de manera automatica.
                            DH = variable;  //Y DH ahora almacenara la variable deseada.
                        }
                        break;  //Fin de la operacion.
            default:    if (DL.isEmpty()) { //Si el registro se encuentra vacio.
                            DL = variable;  //Se guarda en el registro la variable deseada.
                        }
                        else {  //De no ser asi.
                            registroAnterior = registrarVar(DL);   //Lo que almacena el registro DL se cambia de manera automatica.
                            DL = variable;  //Y DL ahora almacenara la variable deseada.
                        }
        }
        return registroAnterior;
    }
    
    public String devolverRegistro (String variable) {
        String registro = "";    //Se crea la variable que contendra el registro.
        if (variable.equalsIgnoreCase(AH)) {    //Se verifica si la variable se encuentra en AH.
            registro = "AH";    //De ser asi, en registro se indica que AH contiene la variable.
        }
        else if (variable.equalsIgnoreCase(AL)) {    //Se verifica si la variable se encuentra en AL.
            registro = "AL";    //De ser asi, en registro se indica que AL contiene la variable.
        }
        else if (variable.equalsIgnoreCase(BH)) {    //Se verifica si la variable se encuentra en BH.
            registro = "BH";    //De ser asi, en registro se indica que BH contiene la variable.
        }
        else if (variable.equalsIgnoreCase(BL)) {    //Se verifica si la variable se encuentra en BL.
            registro = "BL";    //De ser asi, en registro se indica que BL contiene la variable.
        }
        else if (variable.equalsIgnoreCase(CH)) {    //Se verifica si la variable se encuentra en CH.
            registro = "CH";    //De ser asi, en registro se indica que CH contiene la variable.
        }
        else if (variable.equalsIgnoreCase(CL)) {    //Se verifica si la variable se encuentra en CL.
            registro = "CL";    //De ser asi, en registro se indica que CL contiene la variable.
        }
        else if (variable.equalsIgnoreCase(DH)) {    //Se verifica si la variable se encuentra en DH.
            registro = "DH";    //De ser asi, en registro se indica que DH contiene la variable.
        }
        else if (variable.equalsIgnoreCase(DL)) {    //Se verifica si la variable se encuentra en DL.
            registro = "DL";    //De ser asi, en registro se indica que DL contiene la variable.
        }
        return registro;    //Se devuelve el registro, en caso de no ser encontrado, se devuelve vacio.
    }
    
    public void borrarRegistro (String registro) {
        switch (registro) { //Se analiza cual registro se borrara.
            case "AH":  AH = "";    //Se limpia el registro AH.
                        break;  //Fin de la operacion.
            case "AL":  AL = "";    //Se limpia el registro AL.
                        break;  //Fin de la operacion.
            case "BH":  BH = "";    //Se limpia el registro BH.
                        break;  //Fin de la operacion.
            case "BL":  BL = "";    //Se limpia el registro BL.
                        break;  //Fin de la operacion.
            case "CH":  CH = "";    //Se limpia el registro CH.
                        break;  //Fin de la operacion.
            case "CL":  CL = "";    //Se limpia el registro CL.
                        break;  //Fin de la operacion.
            case "DH":  DH = "";    //Se limpia el registro DH.
                        break;  //Fin de la operacion.
            case "DL":  DL = "";    //Se limpia el registro DL.
        }
    }
    
}
