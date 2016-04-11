/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitys;

import java.io.Serializable;

/**
 *
 * @author Tadeo-developer
 */
public class UsuarioProceso extends Catalogo implements Serializable{
    private UsuarioRegistro usuarioRegistro;
    private Proceso proceso;

    public UsuarioRegistro getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(UsuarioRegistro usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }
    
}
