/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientesistematornel;

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
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Clase que contiene utilidades
 *
 * @author usuario
 *
 */
public class Util {

    /**
     * Puerto por defecto
     */
    public static final int PORT = 2011;

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
                    System.out.println("exx: "+exx);
                }
            }
        }
    }
}
