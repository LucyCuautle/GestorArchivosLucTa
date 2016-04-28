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
import entitys.IndicadorProcesoUsuario;
import entitys.Proceso;
import entitys.ProcesoIndicador;
import entitys.UsuarioProceso;
import entitys.UsuarioRegistro;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import static sistematornel.ui.Login.IP;
import utils.Singleton;

/**
 *
 * @author Tadeo-developer
 */
public class RegistroUsuario extends javax.swing.JFrame {

    private Object[] columnas = new Object[]{
        "Numero de grafica",
        "Descripcion",
        "Eficiencia/Eficacia",
        "Target",};

    private DefaultTableModel modelo;
    private DefaultListModel modeloListUsuarios;
    private DefaultListModel modeloListProcesos;
    private ArrayList<UsuarioRegistro> usuarios;
    private ArrayList<Indicador> indicadores;
    private ArrayList<Proceso> procesos;
    private int usuarioSeleccionado;
    private int procesoSeleccionado;
    private int indicadorSeleccionado;

    public RegistroUsuario() {
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);

        usuarioSeleccionado = -1;
        procesoSeleccionado = -1;

        modeloListUsuarios = new DefaultListModel();
        modeloListProcesos = new DefaultListModel();

        jList1.setModel(modeloListUsuarios);
        jList2.setModel(modeloListProcesos);

        setModel();
        
        jList1.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (usuarios.size() > jList1.getSelectedIndex() && jList1.getSelectedIndex() >= 0) {
                    getProcesos(usuarios.get(jList1.getSelectedIndex()));
                    usuarioSeleccionado = jList1.getSelectedIndex();
                    setModel();
                }

            }
        });

        jList2.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (procesos.size() > jList2.getSelectedIndex() && jList2.getSelectedIndex() >= 0) {
                    getIndicadores(usuarios.get(usuarioSeleccionado), procesos.get(jList2.getSelectedIndex()));
                    procesoSeleccionado = jList2.getSelectedIndex();
                   
                }
            }
        });

        //jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
       

        Accion accionProceso = new Accion(Singleton.getInstance().getPlanta());;
        accionProceso.setAccion(8);

        Client clienteUsuario = new Client(IP, Util.PORT, 3, accionProceso);//192.168.1.72

        clienteUsuario.setOnUsuariosListener(new Client.OnUsuariosListener() {

            @Override
            public void onUsuarios(ArrayList<UsuarioRegistro> usuariosNew) {
                usuarios = usuariosNew;
                for (UsuarioRegistro usuario : usuarios) {
                    modeloListUsuarios.addElement(usuario.getNombre() + " " + usuario.getApellidoPaterno() + " " + usuario.getApellidoMaterno());
                }

                jList1.setSelectedIndex(0);
            }
        });

        Thread hilo = new Thread(clienteUsuario);
        hilo.start();
    }

    private void setModel() {
        modelo = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        jTable1.setModel(modelo);

        jTable1.setRowHeight(25);

        for (int i = 0; i < columnas.length; i++) {
            modelo.addColumn(columnas[i]);
        }

        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                indicadorSeleccionado = jTable1.getSelectedRow();
            }
        });
    }

    private void getProcesos(UsuarioRegistro usuarioRegistro) {
        Accion accion = new Accion(Singleton.getInstance().getPlanta());;
        accion.setAccion(5);
        accion.setObject(usuarioRegistro);
        Client cliente = new Client(IP, Util.PORT, 3, accion);//192.168.1.72

        cliente.setOnProcesosListener(new Client.OnProcesosListener() {

            @Override
            public void onProcesos(ArrayList<Proceso> procesosNew) {

                modeloListProcesos.clear();
                procesos = procesosNew;
                for (Proceso proceso : procesos) {
                    modeloListProcesos.addElement(proceso.getNombre());
                }

                jList2.setSelectedIndex(0);

            }
        });
        Thread hilo = new Thread(cliente);
        hilo.start();
    }

    private void getIndicadores(UsuarioRegistro usuario, Proceso proceso) {
        UsuarioProceso usuarioProceso = new UsuarioProceso();
        //usuarioProceso.setPlanta(Singleton.getInstance().getPlanta());
        usuarioProceso.setUsuarioRegistro(usuario);
        usuarioProceso.setProceso(proceso);
        Accion accionIndicador = new Accion(Singleton.getInstance().getPlanta());;
        accionIndicador.setAccion(6);
        accionIndicador.setObject(usuarioProceso);
        Client clienteIndicador = new Client(IP, Util.PORT, 3, accionIndicador);//192.168.1.72

        clienteIndicador.setOnIndicadoresListener(new Client.OnIndicadoresListener() {

            @Override
            public void onIndicadores(ArrayList<Indicador> indicadoresNew) {

                
                setModel();
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
        Thread hiloIndicador = new Thread(clienteIndicador);
        hiloIndicador.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Usuarios");

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton1.setText("Eliminar");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton2.setText("Editar");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setText("Procesos");

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList2);

        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton3.setText("Nuevo");
        jButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton4.setText("Eliminar");
        jButton4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel3.setText("Indicadores");

        jButton5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton5.setText("Agregar");
        jButton5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

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
        jScrollPane4.setViewportView(jTable1);

        jButton9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton9.setText("Eliminar");
        jButton9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton10.setText("Agregar");
        jButton10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton11.setText("Regresar");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 192, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1)
                                .addGap(12, 12, 12))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton5)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4)
                                .addGap(10, 10, 10))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton9))
                            .addComponent(jButton11, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9)
                    .addComponent(jButton10))
                .addGap(18, 18, 18)
                .addComponent(jButton11)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        MenuRegistrar menu = new MenuRegistrar();
        menu.setVisible(true);
        this.setVisible(false);
        dispose();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (usuarioSeleccionado >= 0) {
            VentanaAgregarProcesoUsuario vap = new VentanaAgregarProcesoUsuario(usuarios.get(usuarioSeleccionado));
            vap.setVisible(true);
            setVisible(false);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un usuario");
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        if (usuarioSeleccionado >= 0) {
            if (procesoSeleccionado >= 0) {
                VentanaAgregarIndicadorProcesoUsuario vaip
                        = new VentanaAgregarIndicadorProcesoUsuario(usuarios.get(usuarioSeleccionado), procesos.get(procesoSeleccionado));
                vaip.setVisible(true);
                setVisible(false);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un proceso");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un usuario");
        }

    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if (usuarioSeleccionado >= 0) {
            if (procesoSeleccionado >= 0) {
                if (indicadorSeleccionado >= 0) {
                    IndicadorProcesoUsuario ipu = new IndicadorProcesoUsuario();
                    ipu.setUsuarioRegistro(usuarios.get(usuarioSeleccionado));
                    ipu.setProceso(procesos.get(procesoSeleccionado));
                    ipu.setIndicador(indicadores.get(indicadorSeleccionado));
                    
                    VentanaEliminarIndicadorProcesoUsuario veipu = new VentanaEliminarIndicadorProcesoUsuario(ipu);
                    veipu.setVisible(true);
                    setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un indicador para eliminar");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un proceso");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un usuario");
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        if (usuarioSeleccionado >= 0) {
            if (procesoSeleccionado >= 0) {
                VentanaEliminarProceso vep = new VentanaEliminarProceso(procesos.get(procesoSeleccionado));
                vep.setVisible(true);
                setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un proceso para eliminar");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un usuario");
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (usuarioSeleccionado >= 0) {
            VentanaEliminarUsuario veu = new VentanaEliminarUsuario(usuarios.get(usuarioSeleccionado));
            veu.setVisible(true);
            setVisible(false);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un usuario");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Registrar registrar = new Registrar();
        registrar.setVisible(true);
        setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Registrar registrar = new Registrar(usuarios.get(usuarioSeleccionado));
        registrar.setVisible(true);
        setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
