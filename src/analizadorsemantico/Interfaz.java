package analizadorsemantico;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

public class Interfaz extends javax.swing.JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Codigo codigo;
    private static final String LINEA = System.getProperty("line.separator"); //Variable que genera los saltos de linea detectando el sistema del usuario.
    
    DefaultTableModel modelo = new DefaultTableModel(); //Se define un nuevo modelo de tabla.
    public Interfaz() {
        initComponents();
        setLocationRelativeTo(null);    //Centrar la interfaz a la mitad de la pantalla.
        String titulos[]={"Lexema", "Token", "Tipo", "Valor"};   //Se crean los titlos a la tabla.
        modelo.setColumnIdentifiers(titulos);   //Se agregan los titulos al modelo.
        Simbolos.setModel(modelo);  //Se envia el modelo de tabla al JTable.
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Entrada = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Salida = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Simbolos = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        Enviar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(javax.swing.UIManager.getDefaults().getColor("window"));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Analizador Semántico");

        Entrada.setColumns(20);
        Entrada.setFont(new java.awt.Font("Liberation Mono", 0, 12)); // NOI18N
        Entrada.setLineWrap(true);
        Entrada.setRows(5);
        Entrada.setTabSize(4);
        Entrada.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(Entrada);

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setText("Introduzca las líneas de código:");

        Salida.setColumns(20);
        Salida.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        Salida.setRows(5);
        jScrollPane2.setViewportView(Salida);

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setText("Errores");

        Simbolos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        Simbolos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(Simbolos);

        jLabel4.setBackground(javax.swing.UIManager.getDefaults().getColor("window"));
        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("Tabla de símbolos");

        Enviar.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        Enviar.setText("Compilar");
        Enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnviarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(230, 230, 230)
                .addComponent(Enviar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(0, 172, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(Enviar)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
 
    private void EnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EnviarActionPerformed
        codigo = Analizador.busquedaErrores(Entrada.getText()); //Se buscan posibles errores en el codigo y se realizan las asignaciones.
        codigo = Optimizador.optimizarCodigo(codigo);   //Se optimiza el codigo
        codigo = Intermedio.generarIntermedio(codigo);  //Se genera el codigo intermedio.
        codigo = Ensamblador.generarObjeto(codigo); //Se genera el codigo objeto.
        rellenarTablas();
    }//GEN-LAST:event_EnviarActionPerformed

    public void rellenarTablas() {
        for(int i=0; i<Simbolos.getRowCount();i++) //For para eliminar filas y limpiar la tabla.
        {
            modelo.removeRow(i);
            i-=1;
        }
        for(int e=0; e<codigo.valorToken.size(); e++)  //Se verifica el tamanio de mi vector de datos
        {
            // Checamos si el token no ha sido registrado
            Boolean registrado = false;
            // Recorremos la tabla de simbolos
            for (int row = 0; row < modelo.getRowCount(); row++) {
                // Verificamos si el token ha sido registrado
                if (codigo.valorToken.get(e).equalsIgnoreCase(modelo.getValueAt(row, 0).toString())) {
                    // De ser asi, marcamos la bandera
                    registrado = true;
                    break;
                }
            }
            // Si se encuentra registrado
            if (registrado)
                // Saltamos el registro de este Token
                continue;
            
            // Revisamos si el el token tiene tipo y/o valor
            int posicion = codigo.variable.indexOf(codigo.valorToken.get(e));
            
            // Si el token tiene tipo o valor
            if (posicion != -1) {
                // Se agregan las 4 columnas
                modelo.addRow(new Object[]{codigo.valorToken.get(e),codigo.token.get(e),codigo.tipo.get(posicion),codigo.valor.get(posicion)});    //Se imprimen todos los valores en el lexema   
            }
            // De otra manera
            else {
                // Se ingresa en la tabla con tipo y valor vacíos
                modelo.addRow(new Object[]{codigo.valorToken.get(e),codigo.token.get(e), null, null});
            }
        }
        StringBuilder stb = new StringBuilder();
        for (String error : codigo.errores) {
            stb.append(error).append(LINEA);
        }
        Salida.setText(stb.toString());
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {

        }
        java.awt.EventQueue.invokeLater(() -> {
            new Interfaz().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea Entrada;
    private javax.swing.JButton Enviar;
    private javax.swing.JTextArea Salida;
    private javax.swing.JTable Simbolos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}