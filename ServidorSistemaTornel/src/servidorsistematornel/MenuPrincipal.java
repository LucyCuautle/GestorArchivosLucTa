/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsistematornel;

import entitys.FileTransport;
import entitys.UsuarioLogin;
import entitys.UsuarioRegistro;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;

import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar; 
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author raultadeo
 */
public class MenuPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form MenuPrincipal
     */
    private Thread hilo;
    private SystemTray systemTray;
    private TrayIcon trayIcono;
    private Server server;

    public MenuPrincipal() {
        initComponents();

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Herramientas");
        JMenuItem item = new JMenuItem("Cambiar Ruta");

        bar.add(menu);
        menu.add(item);
        setJMenuBar(bar);

        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                server.setPath(getRoot().getAbsolutePath());
                jLabel1.setText(server.getPath());
            }
        });

        trayIcono = new TrayIcon(getImagen().getImage(), "ServidorSistemaTornel", crearMenu());
        trayIcono.setImageAutoSize(true);

        systemTray = SystemTray.getSystemTray();

        trayIcono.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                systemTray.remove(trayIcono);
                setVisible(true);
            }
        });

        
        
        server = new Server(Utils.PORT, getRoot().getAbsolutePath());
        
        //server.obtenerMatrizCompleta();

        jLabel1.setText(server.getPath());

        server.setOnSincronizedListener(new Server.OnSincronizedListener() {

            @Override
            public void onSincronized(Object obj) {
                if (obj instanceof FileTransport) {
                    FileTransport fileTransport = (FileTransport)obj;
                    trayIcono.displayMessage("Atencion", "" + "Area: " + fileTransport.getProceso() + " Archivo: " + fileTransport.getName() + " sincronizado", TrayIcon.MessageType.INFO);

                }else if(obj instanceof UsuarioRegistro){
                    UsuarioRegistro usuarioRegistro = (UsuarioRegistro) obj;
                    trayIcono.displayMessage("Atencion","El usuario "+usuarioRegistro.getNombre()+" inicio sesion", TrayIcon.MessageType.INFO);

                }

            }
        });

        server.setjScrollPane(jScrollPane1);
        server.setjTextArea(jTextArea1);

        hilo = new Thread(server);
        hilo.start();

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    systemTray.add(trayIcono);
                } catch (AWTException ex) {
                    Logger.getLogger(MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
                trayIcono.displayMessage("Atencion", "El Servidor quedara en segundo plano", TrayIcon.MessageType.INFO);

                setVisible(false);
            }
        });
    }

    public ImageIcon getImagen() {
        ImageIcon imagenIcon = new ImageIcon(getClass().getResource("/imagenes/icono.png"));
        
        return imagenIcon; 
    }

    public PopupMenu crearMenu() {
        PopupMenu menu = new PopupMenu();
        MenuItem abrir = new MenuItem("Abrir");
        MenuItem salir = new MenuItem("Salir");
        abrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                systemTray.remove(trayIcono);
                setVisible(true);

                //hilo.stop();
                //System.exit(0);
            }
        });

        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                hilo.stop();
                System.exit(0);
            }
        });
        menu.add(abrir);
        menu.add(salir);
        return menu;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton2.setText("Parar Servidor");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText("Ruta");

        jLabel2.setText("v1.0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(126, 126, 126)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)))
                        .addGap(0, 149, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        hilo.stop();
        System.exit(0);
    }//GEN-LAST:event_jButton2ActionPerformed

    public File getRoot() {
        JFileChooser fc = new JFileChooser();

        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int respuesta = fc.showOpenDialog(this);

        //Comprobar si se ha pulsado Aceptar
        if (respuesta == JFileChooser.APPROVE_OPTION) {
            //Crear un objeto File con el archivo elegido
            File archivoElegido = fc.getSelectedFile();

            return archivoElegido;
        }

        return null;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        Utils.changeLoogAndFeel();
        //</editor-fold>
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
