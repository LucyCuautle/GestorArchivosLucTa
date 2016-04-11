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
public class Accion extends Object implements Serializable{
    
    private int accion;
    private int subAccion;
    private Object object;

    public int getAccion() {
        return accion;
    }

    public void setAccion(int accion) {
        this.accion = accion;
    }

    public int getSubAccion() {
        return subAccion;
    }

    public void setSubAccion(int subAccion) {
        this.subAccion = subAccion;
    }
    
    

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
    
    
    
    
    
}
