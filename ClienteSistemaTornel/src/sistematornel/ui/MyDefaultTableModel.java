/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistematornel.ui;

import entitys.Indicador;
import entitys.UsuarioRegistro;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import utils.Singleton;

/**
 *
 * @author Tadeo-developer
 */
public class MyDefaultTableModel extends DefaultTableModel{

    ArrayList<Indicador> indicadores;

    public MyDefaultTableModel(ArrayList<Indicador> indicadores) {
        this.indicadores = indicadores;
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
       
        Indicador filaIndicador = indicadores.get(row);
        UsuarioRegistro usuarioRegistro = Singleton.getInstance().getUsuarioRegistro();
        if(column == 5 && column <=3)
            return false;
        else if(column == 4){
            if(usuarioRegistro.getRol().getIdRol()==2){
                return true;
            }else{
                return false;
            }
        }else if(column>=6 && column<=17){
            if(usuarioRegistro.getRol().getIdRol()==2){
                return true;
            }else if(filaIndicador.getDuenioProceso().equalsIgnoreCase(usuarioRegistro.getNombre()+" "+usuarioRegistro.getApellidoPaterno())){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
       
        
    }
    
    /*@Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Indicador indicador = indicadores.get(rowIndex);
        Object returnValue = null;
        
        if(columnIndex==0){
            returnValue = indicador.getNombreProceso();
        }else if(columnIndex==1){
            returnValue = indicador.getDuenioProceso();
        }else if(columnIndex==2){
            returnValue = indicador.getNumeroGrafica();
        }else if(columnIndex==3){
            returnValue = indicador.getDescripcionIndicador();
        }else if(columnIndex==4){
            returnValue = indicador.getEficienciaEficacia();
        }else if(columnIndex==5){
            returnValue = indicador.getTarget();
        }else if(columnIndex>=6 && columnIndex <= 17){
            returnValue = indicador.getDetalleIndicadores().get(columnIndex-6).getPorcentaje();
        }else if(columnIndex == 18){
            returnValue = promedio(rowIndex);
        }
        return returnValue;
    }*/
    
    private Object promedio(int rowSelected) {
        int cols = getColumnCount() - 1;
        //int rows = modelo.getRowCount();
        float valor = 0;
        int cantidadValoresValidos = 0;
        //for (int i = 0; i < rows; i++) {
        for (int j = 6; j < cols; j++) {
            Object val = getValueAt(rowSelected, j);
            if (val != null) {
                Float valF = Float.parseFloat((String) val);
                    if(valF>0){
                        valor += valF;
                        cantidadValoresValidos++;
                    }
               
            }

        }

        //}
        float promedio = valor / cantidadValoresValidos;
        
        //setValueAt(promedio, rowSelected, getColumnCount() - 1);
        
        return promedio;

    }
    
    
    /*@Override
    public Class<?> getColumnClass(int columnIndex) {
         if (indicadores.isEmpty()) {
            return Object.class;
        }
        return getValueAt(0, columnIndex).getClass();
    }*/
    
}
