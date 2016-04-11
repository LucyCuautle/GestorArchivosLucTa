/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitys;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Tadeo-developer
 */
public class UsuariosCatalogo extends Catalogo implements Serializable{
    private ArrayList<UsuarioRegistro> usuariosRegistro;

    public ArrayList<UsuarioRegistro> getUsuariosRegistro() {
        return usuariosRegistro;
    }

    public void setUsuariosRegistro(ArrayList<UsuarioRegistro> usuariosRegistro) {
        this.usuariosRegistro = usuariosRegistro;
    }

   
    
    
}
