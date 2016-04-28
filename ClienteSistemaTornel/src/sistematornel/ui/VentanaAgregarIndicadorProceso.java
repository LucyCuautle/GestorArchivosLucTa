/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistematornel.ui;

import clientesistematornel.Client;
import clientesistematornel.Util;
import entitys.Accion;
import entitys.Indicador;
import entitys.Proceso;
import entitys.ProcesoIndicador;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import static sistematornel.ui.Login.IP;
import utils.Singleton;

/**
 *
 * @author Tadeo-developer
 */
public class VentanaAgregarIndicadorProceso extends javax.swing.JFrame {

    private Object[] columnas = new Object[]{
        "Numero de grafica",
        "Descripcion",
        "Eficiencia/Eficacia",
        "Target",};

    private DefaultTableModel modelo;
    private ArrayList<Indicador> indicadores;
    private int indicadorSeleccionado;
    private Proceso proceso;
    public VentanaAgregarIndicadorProceso(Proceso proceso) {
        initComponents();
        
        this.proceso = proceso;
        indicadorSeleccionado = -1;
        jTable1.setRowHeight(25);
        //jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        modelo = new DefaultTableModel();

        jTable1.setModel(modelo);

        for (int i = 0; i < columnas.length; i++) {
            modelo.addColumn(columnas[i]);
        }

        
        Accion accion = new Accion(Singleton.getInstance().getPlanta());;
        accion.setAccion(6);
        
        Client c = new Client(IP, Util.PORT, 3, accion);//192.168.1.72

        c.setOnIndicadoresListener(new Client.OnIndicadoresListener() {

            @Override
            public void onIndicadores(ArrayList<Indicador> indicadoresNew) {
                indicadores = indicadoresNew;
                for (Indicador indicador : indicadores) {

                    Vector fila = new Vector();
                    fila.add(indicador.getNumeroGrafica());
                    fila.add(indicador.getDescripcionIndicador());
                    fila.add(indicador.getEficienciaEficacia());
                    fila.add(indicador.getTarget());

                    modelo.addRow(fila);

                }

            }
        });
        Thread hilo = new Thread(c);
        hilo.start();
        
         jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                 indicadorSeleccionado = jTable1.getSelectedRow();
            }
        });
    }
    
    private void agregarProcesoIndicador(Indicador indicador){
        ProcesoIndicador procesoIndicador = new ProcesoIndicador();
        procesoIndicador.setProceso(proceso);
        procesoIndicador.setIndicador(indicador);
        Accion accion = new Accion(Singleton.getInstance().getPlanta());;
        accion.setAccion(9);
        accion.setObject(procesoIndicador);
        Client c = new Client(IP, Util.PORT, 3, accion);//192.168.1.72

        c.setOnStatusTextListener(new Client.OnStatusTextListener() {

            @Override
            public void onStatusText(boolean respuesta) {
                if(respuesta){
                    RegistroProceso registroProceso = new RegistroProceso();
                    registroProceso.setVisible(true);
                    setVisible(false);
                    dispose();
                }else {
                    JOptionPane.showMessageDialog(null, "No se logro agregar el indicador");
                }
            }
        });
        Thread hilo = new Thread(c);
        hilo.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton1.setText("Agregar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (indicadorSeleccionado >= 0) {
             agregarProcesoIndicador(indicadores.get(indicadorSeleccionado));
        }else{
            JOptionPane.showMessageDialog(null,"Seleccione un indicador para agregar ");
        }
       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        RegistroProceso registroProceso = new RegistroProceso();
                    registroProceso.setVisible(true);
                    setVisible(false);
                    dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
