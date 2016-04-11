/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import entitys.Archivo;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Tadeo-developer
 */
public class Utils {
    
    public static String[] MESES = {
        "Enero",
        "Febrero",
        "Marzo",
        "Abril",
        "Mayo",
        "Junio",
        "Julio",
        "Agosto",
        "Septiembre",
        "Octubre",
        "Noviembre",
        "Diciembre"
    };
    
     public static String getAnio(){
        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(System.currentTimeMillis());
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        int anio = calendario.get(Calendar.YEAR);
        return ""+anio;
    }

    public static Archivo parseFileAtArchivoWithListFiles(File f, String raiz) {

        Archivo archivo = new Archivo();
        archivo.setDirectory(f.isDirectory());
        archivo.setNombre(f.getName());
        archivo.setRutaAbsoluta(f.getAbsolutePath());
        archivo.setPadre(f.getParentFile().getName());

        String root = f.getAbsolutePath();
        int index = root.indexOf(raiz);
        String rutaRelativa = root.substring(index);
        archivo.setRutaRelativa(rutaRelativa);
        archivo.setRaiz(raiz);

        if (f.isDirectory()) {
            ArrayList<Archivo> archivos = new ArrayList<Archivo>();
            for (File file : f.listFiles()) {
                archivos.add(parseFileAtArchivo(file, raiz));
            }
            archivo.setListaArchivos(archivos);
        }
        return archivo;

    }

    public static Archivo parseFileAtArchivo(File f, String raiz) {

        Archivo archivo = new Archivo();
        archivo.setDirectory(f.isDirectory());
        archivo.setNombre(f.getName());
        archivo.setRutaAbsoluta(f.getAbsolutePath());
        archivo.setPadre(f.getParentFile().getName());
        String root = f.getAbsolutePath();
        int index = root.indexOf(raiz);
        String rutaRelativa = root.substring(index);
        archivo.setRutaRelativa(rutaRelativa);
        archivo.setRaiz(raiz);

        return archivo;

    }

    public static String replaceSeparator(String text) {
        String so = System.getProperty("os.name");
        if (so.contains("Mac")) {
            return text.replace("\\", "/");
        } else if (so.contains("Window")) {
            return text.replace("/", "\\");
        }

        return File.separator;
    }

    public static String getRelativeRaiz(String rutaAbsoluta, String raiz) {
        int index = rutaAbsoluta.indexOf(Utils.replaceSeparator(raiz));
        return rutaAbsoluta.substring(0, index);
    }

    public static boolean deleteCascade(File file) {

        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                if (!deleteCascade(f)) {
                    //return false;
                }

            }

            if (!f.delete()) {
                //return false;
            }

        }

        if (!file.delete()) {
            return false;
        }

        return true;

    }

    public static boolean validarNumeros(String texto) {
        Pattern pat = Pattern.compile("[0-9\\.]{1,}");
        
        Matcher mat = pat.matcher(texto);
        if (mat.matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean isMonth(String str){
         for(String mes : MESES)
             if(mes.equals(str))
                 return true;
         
         return false;
     }
    
     public static boolean isYear(String year){
         try{
             int yearAux = Integer.parseInt(year);
             if(yearAux>=1900 && yearAux<= Integer.parseInt(getAnio()))
                 return true;
             else
                 return false;
         }catch(NumberFormatException e){
             return false;
         }
     }
     
     public static String getMesText(int mes) {
        switch (mes) {
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 12:
                return "Diciembre";
            default:
                return "no mes";

        }
    }

}
