/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sistematornel.ui;

import clientesistematornel.Client;
import clientesistematornel.Util;
import entitys.Accion;
import entitys.Usuario;
import entitys.UsuarioLogin;
import entitys.UsuarioRegistro;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import utils.Colores;
import utils.Singleton;
import utils.Utils;

public class Login extends javax.swing.JFrame implements MouseListener {

    public static String IP = "localhost";//172.18.1.2

    public Login() {

        initComponents();
setLocationRelativeTo(null);
        //getContentPane().setBackground(Colores.AZUL);
        /*jButton1.setBackground(Colores.CREMA);
         jButton1.setOpaque(true);
         jButton1.setBorderPainted(true);

         jButton1.addMouseListener(new MouseListener() {

         @Override
         public void mouseClicked(MouseEvent e) {
         System.out.println("mouse clicked");
         }

         @Override
         public void mousePressed(MouseEvent e) {
         System.out.println("mouse pressed");
         jButton1.setBackground(Colores.AZUL);
         jButton1.setOpaque(true);
         jButton1.setBorderPainted(true);
         }

         @Override
         public void mouseReleased(MouseEvent e) {
         System.out.println("mouse released");
         jButton1.setBackground(Colores.CREMA);
         jButton1.setOpaque(true);
         jButton1.setBorderPainted(true);
         }

         @Override
         public void mouseEntered(MouseEvent e) {
         System.out.println("mouse entered");
         }

         @Override
         public void mouseExited(MouseEvent e) {
         System.out.println("mouse exited");
         }
         });*/
        //jButton1.setBackground(Colores.AZUL);
        //jButton1.setForeground(Colores.CAFE);
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Herramientas");
        JMenuItem item = new JMenuItem("Asignar IP");

        menu.add(item);
        bar.add(menu);
        setJMenuBar(bar);

        jLabel4.setText("IP: " + IP);

        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                IP = JOptionPane.showInputDialog(null, "Introduce una ip");
                jLabel4.setText("IP: " + IP);

            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setText("No. Tarjeta");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setText("Contraseña");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel4.setText("IP");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sistematornel/ui/logo-tornel.png"))); // NOI18N

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 102, 255));
        jButton2.setText("Iniciar Sesion");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel2))
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String userText = jTextField1.getText();
        String contrasenia = jTextField1.getText();

        if (Utils.validarNumeros(userText)) {
            final UsuarioLogin usuario = new UsuarioLogin();
            usuario.setNumeroTarjeta(jTextField1.getText());
            usuario.setContrasenia(jPasswordField1.getText());
            Client cliente = new Client(IP, Util.PORT, 1, usuario);//192.168.1.72

            cliente.setOnLoginListener(new Client.OnLoginListener() {

                @Override
                public void onLogin(UsuarioRegistro usuarioRegistro) {
                    
                    if (usuarioRegistro != null) {
                    System.out.println("usuarioRegistro nombre: "+usuarioRegistro.getNombre());
                    System.out.println("usuarioRegistro cont: "+usuarioRegistro.getContrasenia());
                        System.out.println("usuarioRegistro existe cont: "+usuarioRegistro.getContrasenia());
                        if (usuarioRegistro.getContrasenia() != null) {

                            System.out.println("iniciando sesion: "+usuarioRegistro.getContrasenia());
                            if (usuarioRegistro.getContrasenia().equals("111111")) {
                                System.out.println("contraseña es 111111");
                                String contrasenia = JOptionPane.showInputDialog("Ingrese nueva contraseña");
                                String contraseniaRepetida = JOptionPane.showInputDialog("Ingrese nuevamente la contraseña");

                                if (contrasenia.equals(contraseniaRepetida)) {
                                    usuarioRegistro.setContrasenia(contrasenia);

                                    Accion accion = new Accion();
                                    accion.setAccion(7);
                                    accion.setObject(usuarioRegistro);

                                    Client status = new Client(IP, Util.PORT, 3, accion);//192.168.1.72

                                    status.setOnStatusTextListener(new Client.OnStatusTextListener() {

                                        @Override
                                        public void onStatusText(boolean respuesta) {
                                            if (respuesta) {
                                                JOptionPane.showMessageDialog(null, "Se actualizo la contraseña correctamente");
                                                JOptionPane.showMessageDialog(null, "Favor de inicar sesion con la nueva contraseña");
                                                jTextField1.setText("");
                                                jPasswordField1.setText("");
                                            } else {
                                                JOptionPane.showMessageDialog(null, "Error actualizar la contraseña");
                                                jTextField1.setText("");
                                                jPasswordField1.setText("");
                                            }
                                        }
                                    });

                                    Thread hilo = new Thread(status);
                                    hilo.start();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden intentelo de nuevo");
                                }

                            } else {
                                JOptionPane.showMessageDialog(null, "Bienvenido " + usuarioRegistro.getNombre());

                                Singleton.getInstance().setUsuarioRegistro(usuarioRegistro);

                                Menu menu = new Menu();
                                menu.setVisible(true);
                                setVisible(false);
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, "Error al cambiar contraseña");
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectas ");
                    }
                }
            });

            Thread hilo = new Thread(cliente);
            hilo.start();
        } else {
            JOptionPane.showMessageDialog(null, "El numero de tarjeta deben ser unicamente numeros");
        }

        //buscarUsuario(jTextField1.getText(), jPasswordField1.getText());
    }//GEN-LAST:event_jButton2ActionPerformed

    /*public void buscarUsuario(String usuario, String contrasenia) {
        
     Connection conexion = ConexionBD.GetConnection();
     Statement st;
     ResultSet rs;
     String sql = "select * from Usuario where id_usuario = " + usuario + " and contrasenia = " + "'" + contrasenia + "'";
     try {
     st = conexion.createStatement();
     rs = st.executeQuery(sql);
            
     while (rs.next()) {
                
     System.out.println(rs.getInt("id_usuario"));
     System.out.println(rs.getString("nombre"));
                
     Menu m = new Menu();
     m.setVisible(true);
     this.setVisible(false);
     }
            
     } catch (SQLException e) {
     System.out.println("error select " + e);
     }
        
     }*/
    /**
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        Util.changeLoogAndFeel();


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (e.getComponent().getName().equals(jButton2.getName())) {

            System.out.println("pressed button 1");
        }

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
