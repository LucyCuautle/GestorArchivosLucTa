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
public class ProcesosCatalogo extends Catalogo implements Serializable{
    private ArrayList<Proceso> procesos;
   
    public ArrayList<Proceso> getProcesos() {
        return procesos;
    }

    public void setProcesos(ArrayList<Proceso> procesos) {
        this.procesos = procesos;
    }

 
}
