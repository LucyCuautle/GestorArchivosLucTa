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
public class DetalleIndicador extends Object implements Serializable{
    private int idDetalleIndicador;
    private int idIndicador;
    private String porcentaje;
    private String dia;
    private String mes;
    private String anio;

    public int getIdDetalleIndicador() {
        return idDetalleIndicador;
    }

    public void setIdDetalleIndicador(int idDetalleIndicador) {
        this.idDetalleIndicador = idDetalleIndicador;
    }
    
    public int getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(int idIndicador) {
        this.idIndicador = idIndicador;
    }
    
    

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }
    
    
    
}
