/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistematornel.ui;

import entitys.Indicador;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import utils.Colores;


public class RenderTabla extends DefaultTableCellRenderer {

    ArrayList<Indicador> indicadores;

    public RenderTabla(ArrayList<Indicador> indicadores) {
        this.indicadores = indicadores;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); //To change body of generated methods, choose Tools | Templates.

        
        cell.setForeground(Color.WHITE);
        Indicador filaIndicador = indicadores.get(row);

        Float target = null;
        if (column != 5 && column > 5 && column < 18) {
            try {
                target = Float.parseFloat(filaIndicador.getTarget());

                if (value instanceof String) {

                    Float val = Float.parseFloat((String) value);

                    if (filaIndicador.getDescripcionIndicador().indexOf("PPM") >= 0) {
                        if (val.floatValue() >= target.floatValue()) {
                            cell.setBackground(Color.RED);
                        } else if (val.floatValue() < target.floatValue()) {
                            cell.setBackground(Colores.AZUL);
                        }
                    } else {

                        if (val.floatValue() == target.floatValue()) {
                            cell.setBackground(Colores.AZUL);
                        } else if (val.floatValue() > target.floatValue()) {
                            cell.setBackground(Colores.NARANJA);
                        } else {
                            cell.setBackground(Color.RED);
                        }
                    }

                } else {
                    cell.setBackground(Colores.NARANJA);
                }

            } catch (NumberFormatException e) {
                cell.setBackground(Colores.NARANJA);
            }

        } else if (column == 4) {
            if (filaIndicador.getEficienciaEficacia().equalsIgnoreCase("E")) {
                cell.setBackground(Colores.CAFE);
            } else {
                cell.setBackground(Colores.GUINDA);
            }
        } else if (column == 5) {
            cell.setBackground(Colores.GUINDA_CLARO);
        } else if (column == 18) {
            cell.setBackground(Colores.GUINDA_CLARO);
        } else {
            cell.setBackground(Colores.AZUL);
        }

        /*if(column>6){
         if(value instanceof Integer){
         cell.setBackground(Color.BLUE);
         }else{
         cell.setBackground(Color.RED);
         }
         }*/
        return cell;
    }

}
