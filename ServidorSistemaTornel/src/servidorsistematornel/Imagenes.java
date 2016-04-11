/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsistematornel;

import entitys.Imagen;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author Tadeo-developer
 */
public class Imagenes {
    
    public static ArrayList<Imagen> imagenes;
    private ImageIcon icon_pdf;
    
    
    public Imagenes(){
    
        imagenes = new ArrayList<>();
        imagenes.add(new Imagen("pdf",crearImagen("pdf")));
        imagenes.add(new Imagen("doc",crearImagen("doc")));
        imagenes.add(new Imagen("xls",crearImagen("xls")));
        imagenes.add(new Imagen("ppt",crearImagen("ppt")));
    
    }
    
    private ImageIcon crearImagen(String extension){
    
        ImageIcon icon = new ImageIcon(getClass().getResource(File.separator+"imagenes"+File.separator+"icon_"+extension+".png"));
        icon = escalarImagen(icon);
        
        return icon;
        
    }
    
    private ImageIcon escalarImagen(ImageIcon image){
        return new ImageIcon(image.getImage().getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
       
    }
    
    public ImageIcon buscarImagenPorExntesion(String extension){
    
        for(Imagen img : imagenes){
            if(img.getNombre().equals(extension))
                return img.getImage();
        }
        
        return null;
    }
    
}
