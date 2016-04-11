/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import entitys.UsuarioRegistro;

/**
 *
 * @author Tadeo-developer
 */
public class Singleton {
    
    private static Singleton singleton = null;
    
    private UsuarioRegistro usuarioRegistro;
    
    public static Singleton getInstance(){
        
        if(singleton==null){
            singleton = new Singleton();
        }
        
        return singleton;
        
    }

    public UsuarioRegistro getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(UsuarioRegistro usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }
    
    
    
}
