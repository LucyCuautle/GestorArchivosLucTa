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
public class IndicadorProcesoUsuario extends UsuarioProceso implements Serializable{
    private Indicador indicador;
    
    public void setIndicador(Indicador indicador){
        this.indicador = indicador;
    }
    
    public Indicador getIndicador(){
        return indicador;
    }
    
}
