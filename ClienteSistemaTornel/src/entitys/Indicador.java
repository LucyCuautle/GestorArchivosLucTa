/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitys;

import java.io.Serializable;
import java.util.ArrayList;

public class Indicador extends Object implements Serializable{
    
    private int idIndicador;
    private String nombreProceso;
    private String duenioProceso;
    private String numeroGrafica;
    private String descripcionIndicador;
    private String eficienciaEficacia;
    private String target;
    private int idRol;
    private ArrayList<DetalleIndicador> detalleIndicadores;

    public int getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(int idIndicador) {
        this.idIndicador = idIndicador;
    }

    
    public String getNombreProceso() {
        return nombreProceso;
    }

    public void setNombreProceso(String nombreProceso) {
        this.nombreProceso = nombreProceso;
    }

    public String getDuenioProceso() {
        return duenioProceso;
    }

    public void setDuenioProceso(String duenioProceso) {
        this.duenioProceso = duenioProceso;
    }

    public String getNumeroGrafica() {
        return numeroGrafica;
    }

    public void setNumeroGrafica(String numeroGrafica) {
        this.numeroGrafica = numeroGrafica;
    }

    public String getDescripcionIndicador() {
        return descripcionIndicador;
    }

    public void setDescripcionIndicador(String descripcionIndicador) {
        this.descripcionIndicador = descripcionIndicador;
    }

    public String getEficienciaEficacia() {
        return eficienciaEficacia;
    }

    public void setEficienciaEficacia(String eficienciaEficacia) {
        this.eficienciaEficacia = eficienciaEficacia;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
    
    public ArrayList<DetalleIndicador> getDetalleIndicadores() {
        return detalleIndicadores;
    }

    public void setDetalleIndicadores(ArrayList<DetalleIndicador> detalleIndicadores) {
        this.detalleIndicadores = detalleIndicadores;
    }
    
    
    
}
