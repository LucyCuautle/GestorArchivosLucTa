/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsistematornel;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import entitys.FileTransport;


/**
 * Clase base para el cliente y el servidor.
 * @author usuario
 * 
 */
public abstract class FileTransportBase implements Runnable{
 /**
  * Flujo de salida
  */
 protected ObjectOutputStream oos;
 /**
  * Flujo de entrada
  */
 protected ObjectInputStream ois;
 /**
  * Objeto que viaja entre el cliente y el servidor
  */
 protected FileTransport ft;
 protected int port;
 protected Socket socket;
 /**
  * Ruta del archivo en el cliente, rutal de la carpeta en el servidor
  */
 protected String path;
 protected JTextArea jTextArea;
 protected JScrollPane jScrollPane;
 
 public ObjectOutputStream getOos() {
  return oos;
 }
 public void setOos(ObjectOutputStream oos) {
  this.oos = oos;
 }
 public ObjectInputStream getOis() {
  return ois;
 }
 public void setOis(ObjectInputStream ois) {
  this.ois = ois;
 }
 public FileTransport getFt() {
  return ft;
 }
 public void setFt(FileTransport ft) {
  this.ft = ft;
 }
 public int getPort() {
  return port;
 }
 public void setPort(int port) {
  this.port = port;
 }
 public Socket getSocket() {
  return socket;
 }
 public void setSocket(Socket socket) {
  this.socket = socket;
 }
 public String getPath() {
  return path;
 }
 public void setPath(String path) {
  this.path = path;
 }
 public JTextArea getjTextArea() {
  return jTextArea;
 }
 public void setjTextArea(JTextArea jTextArea) {
  this.jTextArea = jTextArea;
 }
 public JScrollPane getjScrollPane() {
  return jScrollPane;
 }
 public void setjScrollPane(JScrollPane jScrollPane) {
  this.jScrollPane = jScrollPane;
 }
 
 /**
  * Agrega mensajes a la consola de la aplicación
  * @param msg
  */
 public void addMessage(String msg) {
  Utils.addMessage(jTextArea, jScrollPane, msg);
 }
}
