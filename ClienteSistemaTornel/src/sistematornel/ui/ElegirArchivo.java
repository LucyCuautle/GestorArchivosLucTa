/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistematornel.ui;

import clientesistematornel.Client;
import clientesistematornel.Util;
import entitys.Accion;
import entitys.Archivo;
import entitys.FileTransport;
import entitys.Proceso;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import static sistematornel.ui.Arbol.TOTAL_NIVELES;
import sistematornel.ui.Login;
import static sistematornel.ui.Login.IP;
import sistematornel.ui.Menu;
import utils.Singleton;
import utils.Utils;

/**
 *
 * @author Tadeo-developer
 */
public class ElegirArchivo extends javax.swing.JFrame {

    /**
     * Creates new form ElegirArchivo
     */
    private File rootFile;
    private ArrayList archivosArbol;
    private Archivo archivoRaiz;
    private DefaultMutableTreeNode rootTree;
    private String nombrePlantilla = "plantilla";

    Object[] options = {"Abrir",
        "Eliminar", "Nada"};

    JPopupMenu menu = new JPopupMenu("Popup");

    File fileSeleccionado;
    Archivo archivoSeleccionado;
    DefaultMutableTreeNode archivoArbolSeleccionado;

    public ElegirArchivo(Archivo archivo) {
        initComponents();

        setLocationRelativeTo(null);
        archivoRaiz = archivo;

        JMenuItem item = new JMenuItem("configurar");
        jMenu1.add(item);

        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        //menu pop up
        item = new JMenuItem("Abrir");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fileSeleccionado != null && archivoSeleccionado != null && archivoArbolSeleccionado != null) {
                    if (fileSeleccionado.isFile()) {

                        if (fileSeleccionado.getName().toLowerCase().contains(nombrePlantilla)) {
                            JOptionPane.showMessageDialog(null, "No puede abrir este archivo");
                        } else {

                            try {
                                Desktop.getDesktop().open(fileSeleccionado);
                            } catch (IOException ex) {

                            }
                        }

                    }
                }
            }
        });
        menu.add(item);

        item = new JMenuItem("Eliminar");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (fileSeleccionado != null && archivoSeleccionado != null && archivoArbolSeleccionado != null) {

                    if (fileSeleccionado.getName().toLowerCase().contains(nombrePlantilla)) {
                        JOptionPane.showMessageDialog(null, "No puede eliminar este archivo");
                    } else {

                        Accion accionDelete = new Accion();
                        accionDelete.setAccion(2);
                        accionDelete.setSubAccion(1);
                        accionDelete.setObject(archivoSeleccionado);
                        Client cDelete = new Client(IP, Util.PORT, 3, accionDelete);//192.168.1.72

                        cDelete.setOnStatusTextListener(new Client.OnStatusTextListener() {

                            @Override
                            public void onStatusText(boolean respuesta) {
                                if (respuesta) {
                                    fileSeleccionado.delete();
                                    DefaultTreeModel model = (DefaultTreeModel) (jTree1.getModel());
                                    model.removeNodeFromParent(archivoArbolSeleccionado);
                                    archivoSeleccionado = null;
                                    fileSeleccionado = null;
                                    archivoArbolSeleccionado = null;
                                }
                            }
                        });

                        Thread hilo = new Thread(cDelete);
                        hilo.start();
                    }
                }
            }
        });
        menu.add(item);

        for (Proceso proceso : Singleton.getInstance().getUsuarioRegistro().getProcesos()) {
            jComboBox1.addItem(proceso.getNombre());
        }

        initArbol(archivo);

    }

    private void initArbol(Archivo archivo) {

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if (Utils.deleteCascade(rootFile)) {
                    System.out.println("se borro");
                } else {
                    System.out.println("no se borro");
                }

                e.getWindow().dispose();
            }
        });

        addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                if (Utils.deleteCascade(rootFile)) {
                    System.out.println("se borro");
                } else {
                    System.out.println("no se borro");
                }
            }

            public void componentShown(ComponentEvent e) {
                System.out.println("show");
            }
        });

        archivosArbol = new ArrayList<>();
        archivosArbol.add(archivo);
        for (Archivo arch : archivo.getListaArchivos()) {
            archivosArbol.add(arch);
        }

        rootFile = new File(archivo.getNombre());
        rootFile.mkdirs();

        rootTree = new DefaultMutableTreeNode(rootFile);
        addFiles(rootTree, archivo, false);
        jTree1 = new JTree(rootTree);
        jTree1.setCellRenderer(new MyTreeCellRenderer(archivosArbol));
        jScrollPane2.add(jTree1);
        jScrollPane2.setViewportView(jTree1);

        jTree1.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                //doMouseClicked(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                doMouseClicked(e);
            }

        });

        jTree1.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {

                try {
                    DefaultMutableTreeNode sel = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();

                    File f = getPathOfNodo(sel);
                    System.out.println("parent: " + sel.getRoot());
                    String parent = Utils.replaceSeparator(File.separator + sel.getRoot());

                    Archivo archivo = Utils.parseFileAtArchivoWithListFiles(f, parent);
                    System.out.println("click archivo:: " + archivo.getNombre());
                    if (archivo.isDirectory()) {

                        if (archivo.getNombre().equals("Tornel")
                                || Utils.isYear(archivo.getNombre())
                                || Utils.isMonth(archivo.getNombre())
                                || isProcess(archivo.getNombre())
                                || isNumeroTarjeta(archivo.getNombre())) {
                            Accion accion = new Accion();
                            accion.setAccion(2);

                            accion.setObject(archivo);
                            Client c = new Client(IP, Util.PORT, 3, accion);//192.168.1.72

                            c.setOnArchivoListener((Archivo archivoNew) -> {
                                archivosArbol.add(archivoNew);

                                for (Archivo arch : archivoNew.getListaArchivos()) {
                                    archivosArbol.add(arch);

                                }

                                addFiles(sel, archivoNew, false);

                            });

                            Thread hilo = new Thread(c);
                            hilo.start();
                        }

                    } else {

                        File fileAux = new File(archivo.getRutaAbsoluta());
                        if (fileAux.length() <= 0) {

                            System.out.println("No existe un archivo con datos: " + archivo.getRutaAbsoluta());

                            Accion accion = new Accion();
                            accion.setAccion(4);
                            accion.setObject(archivo);
                            Client c = new Client(IP, Util.PORT, 3, accion);

                            c.setOnFileTransportListener((FileTransport fileTransport) -> {

                                File fileRecibido = new File(fileTransport.getArchivo().getRutaAbsoluta());
                                System.out.println("file recibido: " + fileRecibido.getAbsolutePath());

                            });

                            Thread hilo = new Thread(c);
                            hilo.start();
                        } else {
                            System.out.println("Ya existe un archivo con datos: " + fileAux.getAbsolutePath());

                        }

                    }

                } catch (NullPointerException npe) {
                    System.out.println("TreeSelectionListener: " + npe.toString());
                }

                /*DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

                 System.out.println(selectedNode);

                 System.out.println(e.getPath().getPath().toString());*/
            }
        }
        );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Elegir Archivo");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Regresar al Menu");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sistematornel/ui/logo-tornel.png"))); // NOI18N

        jScrollPane2.setViewportView(jTree1);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addComponent(jLabel1)
                        .addGap(0, 124, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        JFileChooser fc = new JFileChooser();
        //Mostrar la ventana para abrir archivo y recoger la respuesta
        //En el parámetro del showOpenDialog se indica la ventana
        //  al que estará asociado. Con el valor this se asocia a la
        //  ventana que la abre.
        int respuesta = fc.showOpenDialog(this);

        //Comprobar si se ha pulsado Aceptar
        if (respuesta == JFileChooser.APPROVE_OPTION) {
            //Crear un objeto File con el archivo elegido
            File archivoElegido = fc.getSelectedFile();
            //Mostrar el nombre del archvivo en un campo de texto

            Client c = new Client(Login.IP, Util.PORT, archivoElegido.getAbsolutePath(), (String) jComboBox1.getSelectedItem(),Singleton.getInstance().getUsuarioRegistro().getIdUsuario());

            c.setOnStatusTextListener(new Client.OnStatusTextListener() {

                @Override
                public void onStatusText(boolean respuesta) {
                    if (respuesta) {
                        initArbol(archivoRaiz);
                    }
                }
            });
            //abrir archivo
             /*try {
             Desktop.getDesktop().open(archivoElegido);
             } catch (IOException ex) {
             Logger.getLogger(ElegirArchivo.class.getName()).log(Level.SEVERE, null, ex);
             }*/

            Thread hilo = new Thread(c);
            hilo.start();

        }// TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    public void addFiles(DefaultMutableTreeNode header, Archivo archivo, int nivel, boolean withDirectory) {

        if (nivel <= TOTAL_NIVELES) {

            for (Archivo f : archivo.getListaArchivos()) {

                String raizRelativa = Utils.getRelativeRaiz(rootFile.getAbsolutePath(), archivo.getRaiz());

                String rutaCombinada = raizRelativa + f.getRutaRelativa();

                System.out.println("addFiles: " + raizRelativa + f.getRutaRelativa());

                System.out.println("archivo parent: " + archivo.getNombre());
                System.out.println("archivo hijo: " + f.getNombre());

                DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(f.getNombre());

                if (f.isDirectory()) {

                    if (f.getNombre().equals("Tornel")
                            || Utils.isYear(f.getNombre())
                            || Utils.isMonth(f.getNombre())
                            || isProcess(f.getNombre())
                            || isNumeroTarjeta(f.getNombre())) {
                        File fileDirectory = new File(rutaCombinada);
                        fileDirectory.mkdirs();
                        System.out.println("addFiles: creando carpeta: " + fileDirectory.getAbsoluteFile());
                        header.add(nodo);
                    }

                } else {
                    File fileAux = new File(rutaCombinada);
                    descargarArchivo(f, fileAux);
                    header.add(nodo);
                }

                if (withDirectory) {

                    if (f.isDirectory()) {

                        Accion accion = new Accion();
                        accion.setAccion(2);

                        accion.setObject(f);
                        Client c = new Client(IP, Util.PORT, 3, accion);//192.168.1.72

                        c.setOnArchivoListener((Archivo archivoNew) -> {
                            archivosArbol.add(archivoNew);
                            for (Archivo arch : archivoNew.getListaArchivos()) {
                                archivosArbol.add(arch);
                            }
                            addFiles(nodo, archivoNew, nivel + 1, false);
                        });

                        Thread hilo = new Thread(c);
                        hilo.start();

                    }
                }

            }
        }

    }

    private boolean isProcess(String procesoAux) {
        for (Proceso proceso : Singleton.getInstance().getUsuarioRegistro().getProcesos()) {
            if (proceso.getNombre().equals(procesoAux)) {
                return true;
            }
        }

        return false;
    }
    
    private boolean isNumeroTarjeta(String numeroTarjeta){
        if(Singleton.getInstance().getUsuarioRegistro().getIdUsuario().equals(numeroTarjeta))
            return true;
        else 
            return false;
    }

    public void descargarArchivo(Archivo archivo, File file) {

        Accion accion = new Accion();
        accion.setAccion(4);
        accion.setObject(archivo);
        Client c = new Client(IP, Util.PORT, 3, accion);

        c.setOnFileTransportListener((FileTransport fileTransport) -> {

            try {
                System.out.println("file recibido de la carpeta: " + file.getAbsolutePath());
                Util.writeBytesToFile(fileTransport.getFilearray(), file.getAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(Arbol.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        Thread hilo = new Thread(c);
        hilo.start();
    }

    public void addFiles(DefaultMutableTreeNode header, Archivo archivo, boolean withDirectory) {
        addFiles(header, archivo, 0, withDirectory);
    }

    public File getPathOfNodo(DefaultMutableTreeNode nodo) {

        String ruta = "";
        for (TreeNode tn : nodo.getPath()) {
            ruta += tn.toString() + File.separator;
        }

        return new File(ruta);
    }

    void doMouseClicked(MouseEvent me) {
        TreePath tp = jTree1.getPathForLocation(me.getX(), me.getY());

        if (tp != null) {
            if (me.getClickCount() == 2 && !me.isConsumed()) {
                System.out.println("doble click!!!!!!!!!");
                me.consume();

                archivoArbolSeleccionado = (DefaultMutableTreeNode) tp.getLastPathComponent();
                fileSeleccionado = getPathOfNodo(archivoArbolSeleccionado);
                archivoSeleccionado = Utils.parseFileAtArchivoWithListFiles(fileSeleccionado, Utils.replaceSeparator(File.separator + archivoArbolSeleccionado.getRoot()));

                if (fileSeleccionado.isFile()) {

                    if (fileSeleccionado.getName().toLowerCase().contains(nombrePlantilla)) {
                        JOptionPane.showMessageDialog(null, "No puede abrir este archivo");
                    } else {
                        try {
                            Desktop.getDesktop().open(fileSeleccionado);
                        } catch (IOException ex) {

                        }
                    }
                }
            } else if (SwingUtilities.isLeftMouseButton(me)) {
                System.out.println("boton izquierdo");
                /*archivoArbolSeleccionado = (DefaultMutableTreeNode) tp.getLastPathComponent();
                 fileSeleccionado = getPathOfNodo(archivoArbolSeleccionado);
                 archivoSeleccionado = Utils.parseFileAtArchivoWithListFiles(fileSeleccionado, Utils.replaceSeparator(File.separator + archivoArbolSeleccionado.getRoot()));

                 if (fileSeleccionado.isFile()) {
                 try {
                 Desktop.getDesktop().open(fileSeleccionado);
                 } catch (IOException ex) {

                 }
                 }*/
            } else if (SwingUtilities.isRightMouseButton(me)) {
                System.out.println("boton derecho");

                archivoArbolSeleccionado = (DefaultMutableTreeNode) tp.getLastPathComponent();
                fileSeleccionado = getPathOfNodo(archivoArbolSeleccionado);
                System.out.println("FileSeleccionado: " + fileSeleccionado);
                archivoSeleccionado = Utils.parseFileAtArchivoWithListFiles(fileSeleccionado, Utils.replaceSeparator(File.separator + archivoArbolSeleccionado.getRoot()));

                if (fileSeleccionado.isFile()) {
                    menu.show(me.getComponent(), me.getX(), me.getY());
                }

            } else if (SwingUtilities.isMiddleMouseButton(me)) {
                System.out.println("se asume boton izquierdo");
            }
        }

    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        Menu menu = new Menu();
        menu.setVisible(true);
        this.setVisible(false);

    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
