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
import entitys.Imagenes;
import java.awt.Component;
import java.awt.Desktop;
import static java.awt.Frame.MAXIMIZED_BOTH;
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
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import static sistematornel.ui.Login.IP;
import utils.Singleton;
import utils.Utils;

public class Arbol extends javax.swing.JFrame {
    // hola mi vida te hablo desde tornel te amoooo 
    /**
     * Creates new form Arbol2
     */
    public static int TOTAL_NIVELES = 1;
    //private JTree tree;
    private File rootFile;
    private DefaultMutableTreeNode rootTree;
    private JScrollPane scroll;

    private ArrayList<Archivo> archivosArbol;

    File fileSeleccionado;
    Archivo archivoSeleccionado;
    DefaultMutableTreeNode archivoArbolSeleccionado;

    JPopupMenu menuPopup = new JPopupMenu("Popup");
    
    private String nombrePlantilla = "plantilla";

    public Arbol(Archivo archivo) {

        initComponents();
        setLocationRelativeTo(null);
        //setExtendedState(MAXIMIZED_BOTH);
        archivosArbol = new ArrayList<>();
        archivosArbol.add(archivo);
        for (Archivo arch : archivo.getListaArchivos()) {
            archivosArbol.add(arch);
        }
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Herramientas");
        JMenuItem item = new JMenuItem("Configurar");

        menu.add(item);

        bar.add(menu);
        setJMenuBar(bar);

        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("configurando");

                /*Arbol a = new Arbol(getRoot().getAbsolutePath());
                 a.setVisible(true);
                 setVisible(false);*/
            }
        });

        //menu pop up
        JMenuItem itemAbrir = new JMenuItem("Abrir");
        itemAbrir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fileSeleccionado != null && archivoSeleccionado != null && archivoArbolSeleccionado != null) {
                    if (fileSeleccionado.isFile()) {

                        if (fileSeleccionado.getName().toLowerCase().contains(nombrePlantilla)) {
                            
                            VentanaElegirAnioMes ventana = new VentanaElegirAnioMes();
                            ventana.setVisible(true);
                            ventana.setOnClickAcceptListener(new VentanaElegirAnioMes.OnClickAcceptListener() {

                                @Override
                                public void onClickAccept(boolean flag) {
                                    if(flag){
                                        openFile(fileSeleccionado);
                                    }else{
                                        JOptionPane.showMessageDialog(null, "No se cargaron las platillas correctamente");
                                    }
                                    
                                }
                            });
                            
                        } else {
                            openFile(fileSeleccionado);
                        }

                    }
                }
            }
        });
        menuPopup.add(itemAbrir);

        JMenuItem itemEliminar = new JMenuItem("Eliminar");
        itemEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (fileSeleccionado != null && archivoSeleccionado != null && archivoArbolSeleccionado != null) {
                    Accion accionDelete = new Accion(Singleton.getInstance().getPlanta());;
                    accionDelete.setAccion(2);
                    accionDelete.setSubAccion(1);
                    accionDelete.setObject(archivoSeleccionado);
                    Client cDelete = new Client(IP, Util.PORT, 3, accionDelete);//192.168.1.72

                    cDelete.setOnStatusTextListener(new Client.OnStatusTextListener() {

                        @Override
                        public void onStatusText(boolean respuesta) {
                            if (respuesta) {
                                fileSeleccionado.delete();
                                DefaultTreeModel model = (DefaultTreeModel) (jTree2.getModel());
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
        });
        menuPopup.add(itemEliminar);

        initArbol(archivo);

    }

    public Arbol() {

    }

    public void initArbol(Archivo root) {
        rootFile = new File(root.getNombre());
        rootFile.mkdirs();

        rootTree = new DefaultMutableTreeNode(rootFile);

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

        addFiles(rootTree, root, false);

        //create the tree by passing in the root node
        JButton button = new JButton("Regresar al menu");
        button.setBounds(100, 0, 150, 20);

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Menu menu = new Menu();
                menu.setVisible(true);
                setVisible(false);
            }
        });

        //tree = new JTree(rootTree);
        //tree.setCellRenderer(new MyTreeCellRenderer());
        //scroll = new JScrollPane(tree);
        jTree2 = new JTree(rootTree);
        jTree2.setCellRenderer(new MyTreeCellRenderer(archivosArbol));
        jScrollPane2.add(jTree2);
        jScrollPane2.setViewportView(jTree2);

        jTree2.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                //doMouseClicked(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                doMouseClicked(e);
            }

        });
        //add(button);
        //add(scroll);
        DefaultTreeCellRenderer render = (DefaultTreeCellRenderer) jTree2.getCellRenderer();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Gestor de archivos Tornel");
        this.pack();
        this.setVisible(true);

        jTree2.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {

                try {
                    DefaultMutableTreeNode sel = (DefaultMutableTreeNode) jTree2.getLastSelectedPathComponent();

                    File f = getPathOfNodo(sel);
                    System.out.println("parent: " + sel.getRoot());
                    String parent = Utils.replaceSeparator(File.separator + sel.getRoot());

                    Archivo archivo = Utils.parseFileAtArchivoWithListFiles(f, parent);

                    if (archivo.isDirectory()) {

                        System.out.println("archivo: " + archivo.getRutaAbsoluta());
                        Accion accion = new Accion(Singleton.getInstance().getPlanta());;
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

                    } else {

                        File fileAux = new File(archivo.getRutaAbsoluta());
                        if (fileAux.length() <= 0) {

                            System.out.println("No existe un archivo con datos: " + archivo.getRutaAbsoluta());

                            Accion accion = new Accion(Singleton.getInstance().getPlanta());;
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
                            /*try {
                             Desktop.getDesktop().open(fileAux);
                             } catch (IOException ex) {

                             }*/
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

    public void descargarArchivo(Archivo archivo, File file) {

        Accion accion = new Accion(Singleton.getInstance().getPlanta());;
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

    public void addFiles(DefaultMutableTreeNode header, Archivo archivo, int nivel, boolean withDirectory) {

        if (nivel <= TOTAL_NIVELES) {

            for (Archivo f : archivo.getListaArchivos()) {

                String raizRelativa = Utils.getRelativeRaiz(rootFile.getAbsolutePath(), archivo.getRaiz());

                String rutaCombinada = raizRelativa + f.getRutaRelativa();

                System.out.println("addFiles: " + raizRelativa + f.getRutaRelativa());

                if (f.isDirectory()) {

                    File fileDirectory = new File(rutaCombinada);
                    fileDirectory.mkdirs();
                    System.out.println("addFiles: creando carpeta: " + fileDirectory.getAbsoluteFile());
                } else {
                    File fileAux = new File(rutaCombinada);
                    descargarArchivo(f, fileAux);

                }
                DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(f.getNombre());

                header.add(nodo);

                if (withDirectory) {

                    if (f.isDirectory()) {

                        Accion accion = new Accion(Singleton.getInstance().getPlanta());;
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
        TreePath tp = jTree2.getPathForLocation(me.getX(), me.getY());

        if (tp != null) {
            if (me.getClickCount() == 2 && !me.isConsumed()) {
                System.out.println("doble click!!!!!!!!!");
                me.consume();

                archivoArbolSeleccionado = (DefaultMutableTreeNode) tp.getLastPathComponent();
                fileSeleccionado = getPathOfNodo(archivoArbolSeleccionado);
                archivoSeleccionado = Utils.parseFileAtArchivoWithListFiles(fileSeleccionado, Utils.replaceSeparator(File.separator + archivoArbolSeleccionado.getRoot()));

                if (fileSeleccionado.isFile()) {
                    if (fileSeleccionado.getName().toLowerCase().contains(nombrePlantilla)) {
                            
                            VentanaElegirAnioMes ventana = new VentanaElegirAnioMes();
                            ventana.setVisible(true);
                            ventana.setOnClickAcceptListener(new VentanaElegirAnioMes.OnClickAcceptListener() {

                                @Override
                                public void onClickAccept(boolean flag) {
                                    if(flag){
                                        openFile(fileSeleccionado);
                                       
                                    }else{
                                        JOptionPane.showMessageDialog(null, "No se cargaron las platillas correctamente");
                                    }
                                    
                                }
                            });
                            
                        } else {
                            openFile(fileSeleccionado);
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
                archivoSeleccionado = Utils.parseFileAtArchivoWithListFiles(fileSeleccionado, Utils.replaceSeparator(File.separator + archivoArbolSeleccionado.getRoot()));

                if (fileSeleccionado.isFile()) {
                    menuPopup.show(me.getComponent(), me.getX(), me.getY());
                }

            } else if (SwingUtilities.isMiddleMouseButton(me)) {
                System.out.println("se asume boton izquierdo");
            }
        }

    }

    private void openFile(File fileSeleccionado) {
        try {
            Desktop.getDesktop().open(fileSeleccionado);
        } catch (IOException ex) {

        }
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree2 = new javax.swing.JTree();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton2.setText("Regresar al menu");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(jTree2);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sistematornel/ui/logo_jk_tornel.jpg"))); // NOI18N

        jButton1.setText("Buscar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(jTextField1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton1))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(6, 6, 6)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Menu menu = new Menu();
        menu.setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTree jTree2;
    // End of variables declaration//GEN-END:variables
}
