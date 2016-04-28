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
public abstract class Usuario extends Object implements Serializable{
    private String planta;
    private String numeroTarjeta;
    private String contrasenia;

    public String getPlanta(){
        return planta;
    }
    
    public void setPlanta(String planta){
        this.planta = planta;
    }
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
    
    
}
