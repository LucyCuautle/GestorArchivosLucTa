/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitys;

import javax.swing.ImageIcon;

/**
 *
 * @author Tadeo-developer
 */
public class Imagen {
    
    private String nombre;
    private ImageIcon image;
    
    public Imagen(String nombre, ImageIcon image){
        this.nombre = nombre;
        this.image = image;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }
    
    
    
}
