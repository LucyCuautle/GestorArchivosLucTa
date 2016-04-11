/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientesistematornel;

import entitys.Accion;
import entitys.IndicadoresCatalogo;
import entitys.Archivo;
import entitys.FileTransport;
import entitys.Indicador;
import entitys.IndicadorProcesoUsuario;
import entitys.Proceso;
import entitys.ProcesoIndicador;
import entitys.ProcesosCatalogo;
import entitys.Usuario;
import entitys.UsuarioLogin;
import entitys.UsuarioProceso;
import entitys.UsuarioRegistro;
import entitys.UsuariosCatalogo;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import sistematornel.ui.Menu;

/**
 * Clase cliente
 *
 * @author usuario
 *
 */
public class Client extends FileTransportBase {

    //hola mi amor
    public interface OnLoginListener {
       // wow ... eres un genio por eso te amo <3
        // sabes que eres un genio mi amor y que te amo con toda el alma, gracias por ser quien eres conmigooooo eres lo mejor en  mi vida te amo loquit mio te amooo te amoooo te amooooo <3
        public void onLogin(UsuarioRegistro usuarioRegistro);
    }

    public interface OnStatusTextListener {

        public void onStatusText(boolean respuesta);
    }

    public interface OnMatrizListener {

        public void onMatriz(ArrayList<Indicador> indicadores);
    }

    public interface OnArchivoListener {

        public void onArchivo(Archivo archivo);
    }

    public interface OnFileTransportListener {

        public void onFileTransportListener(FileTransport fileTransport);
    }

    public interface OnUsuariosListener {

        public void onUsuarios(ArrayList<UsuarioRegistro> usuarios);
    }

    public interface OnProcesosListener {

        public void onProcesos(ArrayList<Proceso> procesos);
    }

    public interface OnIndicadoresListener {

        public void onIndicadores(ArrayList<Indicador> indicadores);
    }

    private OnLoginListener onLoginListener;
    private OnStatusTextListener onStatusTextListener;
    private OnMatrizListener onMatrizListener;
    private OnArchivoListener onArchivoListener;
    private OnFileTransportListener onFileTransportListener;
    private OnUsuariosListener onUsuariosListener;
    private OnProcesosListener onProcesosListener;
    private OnIndicadoresListener onIndicadoresListener;

    private InetAddress host;
    /**
     * Archivo que se va enviar
     */
    private File file;
    private int tipoObjeto;
    private Usuario usuario;
    private Accion accion;

    /**
     * Constructor para pruebas locales
     *
     * @param pathFile
     */
    public Client(String pathFile) {
        try {
            this.host = InetAddress.getLocalHost();
            this.port = Util.PORT;
            this.path = pathFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param ipAddress Ip del Servidor
     * @param port Puerto del Servidor
     */
    public Client(String ipAddress, int port, String path, String proceso, String numeroTarjeta) {

        try {
            this.host = InetAddress.getByName(ipAddress);
            this.port = port;
            this.path = path;
            this.proceso = proceso;
            this.numeroTarjeta = numeroTarjeta;
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            System.out.println("El host no existe");
            e.printStackTrace();

        }

    }

    public Client(String ipAddress, int port, int tipoObjecto, Usuario usuario) {

        try {
            this.host = InetAddress.getByName(ipAddress);
            this.port = port;
            this.tipoObjeto = tipoObjecto;
            this.usuario = usuario;

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            System.out.println("El host no existe");
            e.printStackTrace();

        }

    }

    public Client(String ipAddress, int port, int tipoObjecto, Accion accion) {

        try {
            this.host = InetAddress.getByName(ipAddress);
            this.port = port;
            this.tipoObjeto = tipoObjecto;
            this.accion = accion;

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            System.out.println("El host no existe");
            e.printStackTrace();

        }

    }

    public void setOnLoginListener(OnLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
    }

    public void setOnStatusTextListener(OnStatusTextListener onStatusTextListener) {
        this.onStatusTextListener = onStatusTextListener;
    }

    public void setOnMatrizListener(OnMatrizListener onMatrizListener) {
        this.onMatrizListener = onMatrizListener;
    }

    public void setOnArchivoListener(OnArchivoListener onArchivoListener) {
        this.onArchivoListener = onArchivoListener;
    }

    public void setOnFileTransportListener(OnFileTransportListener onFileTransportListener) {
        this.onFileTransportListener = onFileTransportListener;
    }

    public void setOnUsuariosListener(OnUsuariosListener onUsuariosListener) {
        this.onUsuariosListener = onUsuariosListener;
    }

    public void setOnProcesosListener(OnProcesosListener onProcesosListener) {
        this.onProcesosListener = onProcesosListener;
    }

    public void setOnIndicadoresListener(OnIndicadoresListener onIndicadoresListener) {
        this.onIndicadoresListener = onIndicadoresListener;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        synchronized (this) {
            Thread.currentThread();
        }
        try {
            init();
            if (socket != null) {
                if (tipoObjeto == 0) {
                    sendFile();
                } else if (tipoObjeto == 1) {
                    sendUsuarioLogin(usuario);
                } else if (tipoObjeto == 2) {
                    sendUsuarioRegistro(usuario);
                } else if (tipoObjeto == 3) {
                    sendAccion(accion);
                }
            }
        } catch (FileTransportException e) {
            // TODO Auto-generated catch block
            addMessage(e.getMessage());
        }
    }

    /**
     * Inicia la conexion con el servidor
     *
     * @throws FileTransportException
     */
    private void init() throws FileTransportException {
        try {
            addMessage("Conectando con el Servidor...");
            this.socket = new Socket(this.host, this.port);

        } catch (IOException e) {

            JOptionPane.showMessageDialog(null, "Error al conectar con el servidor");

            throw new FileTransportException("Ha ocurrido un error: "
                    + e.getMessage());
        }
    }

    /**
     * Envia un objeto de tipo {@link FileTransport}
     *
     * @throws FileTransportException
     */
    private void sendFile() throws FileTransportException {
        String name;
        byte[] byteFile;

        if (this.path != null) {
            file = new File(this.path);
        }

        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            name = file.getName();
            byteFile = Util.getBytesFromFile(file);

            this.ft = new FileTransport();
            this.ft.setName(name);
            this.ft.setFilearray(byteFile);
            this.ft.setProceso(proceso);
            this.ft.setNumeroTarjeta(numeroTarjeta);
            this.ft.setFecha(Util.getFecha());
            this.ft.setHora(Util.getHora());

            addMessage("Subiendo archivo...");

            this.oos.writeObject(this.ft);

            addMessage("El archivo " + name + " fue enviado al servidor");

            this.ois = new ObjectInputStream(socket.getInputStream());

            String respuesta = (String) ois.readObject();

            if (respuesta.equals("exito")) {
                if (onStatusTextListener != null) {
                    onStatusTextListener.onStatusText(true);
                }
            } else {
                if (onStatusTextListener != null) {
                    onStatusTextListener.onStatusText(false);
                }
            }

            addMessage("Respuesta Servidor: " + respuesta);

            this.socket.close();
        } catch (IOException e) {
            throw new FileTransportException("Ha ocurrido un error: "
                    + e.getMessage());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendUsuarioLogin(Usuario usuario) {

        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());

            addMessage("Subiendo archivo...");

            this.oos.writeObject(usuario);

            addMessage("mandando usuario " + usuario.getNumeroTarjeta() + " fue enviado al servidor");

            this.ois = new ObjectInputStream(socket.getInputStream());

            UsuarioRegistro usuarioRegistro = (UsuarioRegistro) ois.readObject();

            if (usuarioRegistro != null) {
                addMessage("Respuesta Servidor: " + usuarioRegistro.getNombre());
            }

            if (usuarioRegistro != null) {

                if (onLoginListener != null) {
                    onLoginListener.onLogin(usuarioRegistro);
                }

            } else {
                if (onLoginListener != null) {
                    onLoginListener.onLogin(null);
                }
            }

            addMessage("Respuesta Servidor: " + ((UsuarioRegistro) ois.readObject()).getNombre());

            this.socket.close();
        } catch (IOException e) {
            System.out.println("IOException " + e);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void sendUsuarioRegistro(Usuario usuario) {

        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());

            addMessage("Registrando Usuario...");

            this.oos.writeObject(usuario);

            addMessage("mandando usuario " + usuario.getNumeroTarjeta() + " fue enviado al servidor");

            this.ois = new ObjectInputStream(socket.getInputStream());

            String respuesta = (String) ois.readObject();

            addMessage("Respuesta Servidor: " + respuesta);

            if (respuesta.equals("exito")) {

                if (onStatusTextListener != null) {
                    onStatusTextListener.onStatusText(true);
                }

            } else {
                if (onStatusTextListener != null) {
                    onStatusTextListener.onStatusText(false);
                }
            }

            addMessage("Respuesta Servidor: " + (String) ois.readObject());

            this.socket.close();
        } catch (IOException e) {
            System.out.println("IOException " + e);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void sendAccion(Accion accion) {

        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());

            addMessage("Solicitando datos...");

            this.oos.writeObject(accion);

            addMessage("enviando accion " + accion.getAccion() + " al servidor");

            this.ois = new ObjectInputStream(socket.getInputStream());

            Object objectRespuesta = ois.readObject();

            if (objectRespuesta instanceof ArrayList) {
                ArrayList<Indicador> respuesta = (ArrayList<Indicador>) objectRespuesta;
                addMessage("Respuesta Servidor: " + respuesta);

                if (onMatrizListener != null) {
                    onMatrizListener.onMatriz(respuesta);
                    addMessage("Respuesta Servidor exitosa");
                }

            } else if (objectRespuesta instanceof Archivo) {
                Archivo archivo = (Archivo) objectRespuesta;
                addMessage("Respuesta Servidor: " + archivo.getRutaAbsoluta());
                if (onArchivoListener != null) {
                    onArchivoListener.onArchivo(archivo);
                    addMessage("Respuesta Servidor exitosa");
                }
            } else if (objectRespuesta instanceof String) {

                if (((String) objectRespuesta).equals("exito")) {
                    if (onStatusTextListener != null) {
                        onStatusTextListener.onStatusText(true);
                    }

                } else {
                    if (onStatusTextListener != null) {
                        onStatusTextListener.onStatusText(false);
                    }
                }

            } else if (objectRespuesta instanceof FileTransport) {

                FileTransport ft = (FileTransport) objectRespuesta;

                int index = ft.getArchivo().getRutaAbsoluta().indexOf(ft.getArchivo().getRaiz());
                String raiz = ft.getArchivo().getRutaAbsoluta().substring(0, index);

                Util.writeBytesToFile(ft.getFilearray(), raiz + "/" + ft.getArchivo().getRutaRelativa());

                if (onFileTransportListener != null) {
                    onFileTransportListener.onFileTransportListener(ft);
                }

            } else if (objectRespuesta instanceof ProcesosCatalogo) {

                ProcesosCatalogo procesos = (ProcesosCatalogo) objectRespuesta;

                if (procesos.isEstatus()) {
                    if (procesos.getProcesos() != null) {
                        if (onProcesosListener != null) {
                            onProcesosListener.onProcesos(procesos.getProcesos());
                        }
                    } else {
                        if (onStatusTextListener != null) {
                            onStatusTextListener.onStatusText(procesos.isEstatus());
                        }
                    }
                } else {
                    if (onStatusTextListener != null) {
                        onStatusTextListener.onStatusText(procesos.isEstatus());
                    }
                }

            } else if (objectRespuesta instanceof IndicadoresCatalogo) {

                IndicadoresCatalogo indicadores = (IndicadoresCatalogo) objectRespuesta;

                if (indicadores.isEstatus()) {
                    if (indicadores.getIndicadores() != null) {
                        if (onIndicadoresListener != null) {
                            onIndicadoresListener.onIndicadores(indicadores.getIndicadores());
                        }
                    } else {
                        if (onStatusTextListener != null) {
                            onStatusTextListener.onStatusText(indicadores.isEstatus());
                        }
                    }
                } else {
                    if (onStatusTextListener != null) {
                        onStatusTextListener.onStatusText(indicadores.isEstatus());
                    }
                }

            } else if (objectRespuesta instanceof UsuariosCatalogo) {

                UsuariosCatalogo usuariosCatalogo = (UsuariosCatalogo) objectRespuesta;

                if (usuariosCatalogo.isEstatus()) {
                    if (usuariosCatalogo.getUsuariosRegistro() != null) {
                        if (onUsuariosListener != null) {
                            onUsuariosListener.onUsuarios(usuariosCatalogo.getUsuariosRegistro());
                        }
                    } else {
                        if (onStatusTextListener != null) {
                            onStatusTextListener.onStatusText(usuariosCatalogo.isEstatus());
                        }
                    }
                } else {
                    if (onStatusTextListener != null) {
                        onStatusTextListener.onStatusText(usuariosCatalogo.isEstatus());
                    }
                }

            } else if (objectRespuesta instanceof ProcesoIndicador) {
                ProcesoIndicador procesoIndicador = (ProcesoIndicador) objectRespuesta;
                if (onStatusTextListener != null) {
                    onStatusTextListener.onStatusText(procesoIndicador.isEstatus());
                }
            } else if (objectRespuesta instanceof UsuarioProceso) {
                UsuarioProceso usuarioProceso = (UsuarioProceso) objectRespuesta;
                if (onStatusTextListener != null) {
                    onStatusTextListener.onStatusText(usuarioProceso.isEstatus());
                }
            } else if (objectRespuesta instanceof IndicadorProcesoUsuario) {
                IndicadorProcesoUsuario ipu = (IndicadorProcesoUsuario) objectRespuesta;
                if (onStatusTextListener != null) {
                    onStatusTextListener.onStatusText(ipu.isEstatus());
                }
            } else {
                addMessage("Respuesta Servidor objeto no identificada");
            }

            this.socket.close();
        } catch (IOException e) {
            System.out.println("IOException " + e);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
