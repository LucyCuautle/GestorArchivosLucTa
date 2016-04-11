/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistematornel.ui;

import entitys.Archivo;
import entitys.Imagenes;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Tadeo-developer
 */
public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

        private Imagenes imagenes;
        private ArrayList<Archivo> archivosArbol;

        public MyTreeCellRenderer(ArrayList<Archivo> archivosArbol) {
            imagenes = new Imagenes();
            this.archivosArbol = archivosArbol;
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            // decide what icons you want by examining the node
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                if (node.getUserObject() instanceof String) {
                    // your root node, since you just put a String as a user obj 

                    //System.out.println("nodo nombre: " + (String) node.getUserObject());
                    for (Archivo archivo : archivosArbol) {
                        //System.out.println("archivo nombre: " + archivo.getNombre());
                        if (archivo.getNombre().equals((String) node.getUserObject())) {
                            if (archivo.isDirectory()) {
                                setIcon(imagenes.buscarImagenPorExntesion("carpeta"));
                                setOpenIcon(UIManager.getIcon("Tree.openIcon"));
                                setClosedIcon(UIManager.getIcon("Tree.closedIcon"));

                            } else {

                                int posExtension = archivo.getNombre().lastIndexOf(".");

                                if (posExtension > -1) {

                                    String ext = archivo.getNombre().substring(posExtension);

                                    if (ext.equals(".pdf")) {
                                        setIcon(imagenes.buscarImagenPorExntesion("pdf"));
                                    } else if (ext.equals(".ppt") || ext.equals(".pptx")) {
                                        setIcon(imagenes.buscarImagenPorExntesion("ppt"));
                                    } else if (ext.equals(".doc") || ext.equals(".docx")) {
                                        setIcon(imagenes.buscarImagenPorExntesion("doc"));
                                    } else if (ext.equals(".xls") || ext.equals(".xlsx")) {
                                        setIcon(imagenes.buscarImagenPorExntesion("xls"));
                                    }

                                }

                            }
                        }
                    }

                }
                /*else if (node.getUserObject() instanceof Contact) {
                 // decide based on some property of your Contact obj
                 Contact contact = (Contact)  node.getUserObject();
                 if (contact.isSomeProperty()) {
                 setIcon(UIManager.getIcon("FileView.hardDriveIcon"));
                 } else {
                 setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
                 }
                 }*/

            }

            return this;
        }

    }
