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
public class IndicadoresCatalogo extends Catalogo implements Serializable{
    private ArrayList<Indicador> indicadores;

    public ArrayList<Indicador> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(ArrayList<Indicador> indicadores) {
        this.indicadores = indicadores;
    }
    
    
}
