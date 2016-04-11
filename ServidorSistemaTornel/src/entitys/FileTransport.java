/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitys;

import java.io.Serializable;

public class FileTransport extends Object implements Serializable {

    private static final long serialVersionUID = 8458553842346689548L;
    private String name;
    private String proceso;
    private String numeroTarjeta;
    private String fecha;
    private String hora;
    private byte[] filearray;
    private Archivo archivo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public byte[] getFilearray() {
        return filearray;
    }

    public void setFilearray(byte[] filearray) {
        this.filearray = filearray;
    }

    public Archivo getArchivo() {
        return archivo;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "[" + this.name + "]";
    }
}
