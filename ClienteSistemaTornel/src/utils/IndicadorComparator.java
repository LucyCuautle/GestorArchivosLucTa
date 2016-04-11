/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import entitys.Indicador;
import java.util.Comparator;

/**
 *
 * @author Tadeo-developer
 */
public class IndicadorComparator implements Comparator<Indicador>{


    @Override
    public int compare(Indicador o1, Indicador o2) {
       
        String[] splitGrafica = o1.getNumeroGrafica().replace(".",",").split(",");
        String[] splitGraficaOtra = o2.getNumeroGrafica().replace(".",",").split(",");
        
        System.out.println("splitGrafica: "+o1.getNumeroGrafica());
        System.out.println("splitGraficaOtra: "+o2.getNumeroGrafica());
        
        if(splitGrafica.length>1){
            System.out.println("splitGraficaSplit: "+splitGrafica[1]);
            if(splitGraficaOtra.length>1){
                System.out.println("splitGraficaOtraSplit: "+splitGraficaOtra[1]);
                if(Integer.parseInt(splitGrafica[1])>Integer.parseInt(splitGraficaOtra[1])){
                    System.out.println("grafica mayorSplit");
                    return 1;
                }else if(Integer.parseInt(splitGrafica[1])<Integer.parseInt(splitGraficaOtra[1])){
                    System.out.println("grafica menor");
                    return -1;
                }else{
                    System.out.println("grafica iguales");
                    return 0;
                }
            }else{
                return 1;
            }
        }else{
            if(splitGraficaOtra.length>1){
                 System.out.println("splitGraficaOtra: "+splitGraficaOtra[1]);
                return -1;
            }else{
                return 0;
            }
        }
    }
    
}
