
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsistematornel;

import entitys.Archivo;
import java.awt.Dimension;
import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase que contiene utilidades
 *
 * @author usuario
 *
 */
public class Utils {

    /**
     * Puerto por defecto
     */
    public static final int PORT = 2011;

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

    /**
     * Convierte un objeto de tipo {@link File} a un arreglo de bytes
     *
     * @param file Archivo a Convertir
     * @return byte[]
     * @throws IOException
     * @throws FileTransportException
     */
    public static byte[] getBytesFromFile(File file) throws IOException,
            FileTransportException {
        InputStream is;
        int length;
        byte[] bytes;
        int offset = 0;
        int numRead = 0;

        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new FileTransportException("No se encuentra el archivo");
        }

        length = (int) file.length();

        if (length > Integer.MAX_VALUE) {
            throw new FileTransportException("El archivo es demasiado grande");
        }

        bytes = new byte[length];

        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new FileTransportException("El archivo " + file.getName()
                    + " no fue convertido.");
        }

        is.close();
        return bytes;
    }

    /**
     * Convierte un arreglo de bytes en un objeto de tipo {@link File} y lo
     * escribe en disco
     *
     * @param byteArray arreglo a escribir
     * @param pathFile ruta donde se escribe el archivo
     * @throws IOException
     */
    public static void writeBytesToFile(byte[] byteArray, String pathFile)
            throws IOException {
        ByteArrayInputStream bis;
        File file;
        OutputStream out;
        byte[] buf;
        int len;

        file = new File(pathFile);

        if (!file.exists()) {
            file.createNewFile();
        }

        bis = new ByteArrayInputStream(byteArray);
        out = new FileOutputStream(file);
        buf = new byte[1024];

        while ((len = bis.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        out.close();
        bis.close();

    }

    /**
     * Agrega un mensaje a la consola Java y a la consola de la aplicacion
     *
     * @param console JtextArea, enviar null si se va usar System.out.
     * @param consoleScroll JScrollPane null si se va usar System.out.
     * @param msg Mensaje
     */
    public static void addMessage(final JTextArea console,
            final JScrollPane consoleScroll, final String msg) {
        if (console != null && consoleScroll != null) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    console.append(msg + "\n");
                    Dimension consoleSize = console.getSize();
                    Point point = new Point(0, consoleSize.height);
                    consoleScroll.getViewport().setViewPosition(point);
                }
            });
        }
        System.out.println(msg);
    }

    public static String getFecha() {

        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(System.currentTimeMillis());
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        int anio = calendario.get(Calendar.YEAR);

        return dia + "/" + (mes + 1) + "/" + anio;
    }

    public static String getAnio() {
        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(System.currentTimeMillis());
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        int anio = calendario.get(Calendar.YEAR);
        return "" + anio;
    }

    public static String getMes() {
        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(System.currentTimeMillis());
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        int anio = calendario.get(Calendar.YEAR);

        return getMesText((mes + 1));
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

    public static String getHora() {

        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(System.currentTimeMillis());
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);
        int segundo = calendario.get(Calendar.SECOND);

        return hora + ":" + minuto + ":" + segundo;
    }

    public static String getFechaHora() {

        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(System.currentTimeMillis());
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        int anio = calendario.get(Calendar.YEAR);
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);
        int segundo = calendario.get(Calendar.SECOND);

        return dia + "/" + (mes + 1) + "/" + anio + " " + hora + ":" + minuto + ":" + segundo;
    }

    public static void changeLoogAndFeel() {

        try {
            UIManager.LookAndFeelInfo[] lista = UIManager.getInstalledLookAndFeels();
            for (UIManager.LookAndFeelInfo l : lista) {
                if (l.getName().contains("Mac")) {
                    UIManager.setLookAndFeel(l.getClassName());
                    System.out.println(l.getName());
                    System.out.println(l.getClassName());
                    break;
                } else if (l.getName().contains("Windows")) {
                    UIManager.setLookAndFeel(l.getClassName());
                    System.out.println(l.getName());
                    System.out.println(l.getClassName());
                    break;
                }

            }
        } catch (Exception ex) {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                } catch (Exception exx) {
                    System.out.println("exx: " + exx);
                }
            }
        }
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
        return Utils.replaceSeparator(rutaAbsoluta.substring(0, index));
    }

    public static boolean isMonth(String str) {
        for (String mes : MESES) {
            if (mes.equals(str)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isYear(String year) {
        try {
            int yearAux = Integer.parseInt(year);
            if (yearAux >= 1900 && yearAux <= Integer.parseInt(getAnio())) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void copyFile_Java7(String origen, String destino) throws IOException {
        Path FROM = Paths.get(origen);
        Path TO = Paths.get(destino);
        //sobreescribir el fichero de destino, si existe, y copiar
        // los atributos, incluyendo los permisos rwx
        CopyOption[] options = new CopyOption[]{
            StandardCopyOption.REPLACE_EXISTING,
            StandardCopyOption.COPY_ATTRIBUTES
        };
        Files.copy(FROM, TO, options);
    }

    public static void copyFolder(File src, File dest)
    	throws IOException{
    	
    	if(src.isDirectory()){
    		
    		//if directory not exists, create it
    		if(!dest.exists()){
    		   dest.mkdir();
    		   System.out.println("Directory copied from " 
                              + src + "  to " + dest);
    		}
    		
    		//list all the directory contents
    		String files[] = src.list();
    		
    		for (String file : files) {
    		   //construct the src and dest file structure
    		   File srcFile = new File(src, file);
    		   File destFile = new File(dest, file);
    		   //recursive copy
    		   copyFolder(srcFile,destFile);
    		}
    	   
    	}else{
    		//if file, then copy it
    		//Use bytes stream to support all file types
    		InputStream in = new FileInputStream(src);
    	        OutputStream out = new FileOutputStream(dest); 
    	                     
    	        byte[] buffer = new byte[1024];
    	    
    	        int length;
    	        //copy the file content in bytes 
    	        while ((length = in.read(buffer)) > 0){
    	    	   out.write(buffer, 0, length);
    	        }
 
    	        in.close();
    	        out.close();
    	        System.out.println("File copied from " + src + " to " + dest);
    	}
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
    
    public static void cambiarNombre(String ruta) {

        File file = new File(ruta);

        for (File nombreArchivo : file.listFiles()) {
            if (nombreArchivo.isDirectory()) {
                System.out.println(nombreArchivo.getAbsolutePath());
                cambiarNombreDeCarpeta(nombreArchivo);
            }

        }

    }

    private static void cambiarNombreDeCarpeta(File nombreCarpeta) {
        for (File archivo : nombreCarpeta.listFiles()) {
            if (archivo.isDirectory()) {
                cambiarNombreDeCarpeta(archivo);
            } else {
                System.out.println(archivo.getAbsolutePath());
                int posicion = archivo.getName().lastIndexOf(".");
                String extension = archivo.getName().substring(posicion);

                if(extension.equals(".ppt") || extension.equals(".pptx")){
                    File archivoTemp = new File(nombreCarpeta.getAbsolutePath()+File.separator+nombreCarpeta.getName()+".pptx");
                    archivo.renameTo(archivoTemp);
                  
                }
                
            }

        }
    }
}
