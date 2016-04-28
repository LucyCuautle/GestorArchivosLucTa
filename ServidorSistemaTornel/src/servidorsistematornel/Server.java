/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsistematornel;

import entitys.Accion;
import entitys.Archivo;
import entitys.DetalleIndicador;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.SocketException;
import entitys.FileTransport;
import entitys.Indicador;
import entitys.IndicadorProcesoUsuario;
import entitys.IndicadoresCatalogo;
import entitys.Proceso;
import entitys.ProcesoIndicador;
import entitys.ProcesosCatalogo;
import entitys.Rol;
import entitys.Usuario;
import entitys.UsuarioLogin;
import entitys.UsuarioProceso;
import entitys.UsuarioRegistro;
import entitys.UsuariosCatalogo;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Clase server
 *
 * @author usuario
 *
 */
public class Server extends FileTransportBase {

    public interface OnSincronizedListener {

        public void onSincronized(Object obj);
    }

    private ServerSocket serverSocket;
    private boolean isStopped = false;
    private OnSincronizedListener onSincronizedListener;
    private String pathLink;
    private String planta;

    public Server() {
    }

    /**
     *
     * @param port Puerto en el que arrancar el servidor
     * @param path Ruta donde se guardaran los archivos
     */
    public Server(int port, String path) {
        this.port = port;
        this.path = path;
        this.pathLink = "link";
        //File f = new File(pathLink);
        //f.mkdirs();
    }

    public void setOnSincronizedListener(OnSincronizedListener onSincronizedListener) {
        this.onSincronizedListener = onSincronizedListener;
    }

    @Override
    public void run() {
        synchronized (this) {
            Thread.currentThread();
        }
        addMessage("Iniciando...");
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
            addMessage("Error al abrir el puerto " + this.port);
        }
        addMessage("Servidor abierto en el puerto "
                + this.serverSocket.getLocalPort());
        while (!isStopped()) {
            openConnection();
        }

    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void start() {
        this.isStopped = false;
    }

    /**
     * Este método aguarda a que hallá un conexión por parte del cliente lee la
     * entrada y escribe el archivo en disco y envia una respuesta al cliente
     */
    private void openConnection() {

        try {

            addMessage("Esperando Conexion...");

            this.socket = this.serverSocket.accept();

            addMessage("Conexion desde " + this.socket.getInetAddress());

            this.ois = new ObjectInputStream(this.socket.getInputStream());
            addMessage("Recibiendo archivo...");
            Object obj = ois.readObject();
            this.oos = new ObjectOutputStream(this.socket.getOutputStream());
            if (obj instanceof FileTransport) {
                ft = (FileTransport) obj;
                planta = ft.getPlanta();

                ArrayList<UsuarioRegistro> usuariosDelProceso = buscarUsuariosProceso(ft.getProceso());
                
                if(usuariosDelProceso.size()>1){
                    for(UsuarioRegistro user : usuariosDelProceso){
                        File directorio = new File(Utils.replaceSeparator(this.path + File.separator + planta+File.separator+Utils.getAnio() + File.separator + Utils.getMes() + File.separator + ft.getProceso()+File.separator+user.getIdUsuario()));
                        directorio.mkdirs();
                    }
                    
                    Utils.writeBytesToFile(ft.getFilearray(), Utils.replaceSeparator(this.path + File.separator +  planta+File.separator+Utils.getAnio() + File.separator + Utils.getMes() + File.separator + ft.getProceso() + File.separator + ft.getNumeroTarjeta()+ File.separator +ft.getName()));
                   
                }else{
                    File directorio = new File(Utils.replaceSeparator(this.path + File.separator +  planta+File.separator+Utils.getAnio() + File.separator + Utils.getMes() + File.separator + ft.getProceso()));
                    directorio.mkdirs();
                    Utils.writeBytesToFile(ft.getFilearray(), Utils.replaceSeparator(this.path + File.separator +  planta+File.separator+Utils.getAnio() + File.separator + Utils.getMes() + File.separator + ft.getProceso() + File.separator + ft.getName()));
                }
                
               
                

                addMessage("Copiado en " + this.path + System.getProperty("file.separator") + ft.getProceso() + System.getProperty("file.separator") + ft.getName());
                addMessage("Se subio el archivo con la fecha " + ft.getFecha());
                addMessage("Se subio el archivo con la hora " + ft.getHora());

                if (onSincronizedListener != null) {
                    onSincronizedListener.onSincronized(ft);
                }

                addMessage("Enviando Respuesta al cliente...");
                this.oos.writeObject("exito");

            } else if (obj instanceof UsuarioLogin) {

                UsuarioLogin user = (UsuarioLogin) obj;
                planta = user.getPlanta();
                addMessage("Usuario: " + user.getNumeroTarjeta());
                addMessage("Constrasenia: " + user.getContrasenia());

                UsuarioRegistro usuarioRegistro = buscarUsuario(user.getNumeroTarjeta(), user.getContrasenia());

                addMessage("Enviando Respuesta al cliente...");

                if (usuarioRegistro != null) {

                    if (onSincronizedListener != null) {
                        onSincronizedListener.onSincronized(usuarioRegistro);
                    }
                    addMessage("El usuario " + usuarioRegistro.getNombre() + " inicio sesion");
                    addMessage("contraseña " + usuarioRegistro.getContrasenia());
                    this.oos.writeObject(usuarioRegistro);
                } else {
                    this.oos.writeObject(null);
                }

            } else if (obj instanceof UsuarioRegistro) {
                UsuarioRegistro user = (UsuarioRegistro) obj;
                planta = user.getPlanta();
                addMessage("Usuario: " + user.getNumeroTarjeta());
                addMessage("Constrasenia: " + user.getContrasenia());

                boolean respuesta = insertarUsuario(user);

                addMessage("Enviando Respuesta al cliente...");

                if (respuesta) {

                    if (onSincronizedListener != null) {
                        onSincronizedListener.onSincronized(user);
                    }
                    addMessage("El usuario " + user.getNumeroTarjeta() + " se registro");
                    this.oos.writeObject("exito");
                } else {
                    this.oos.writeObject("fallo");
                }
            } else if (obj instanceof Accion) {
                Accion accion = (Accion) obj;
                
                System.out.println("planta: "+accion.getPlanta());
                //accion para obtener la matriz
                if (accion.getAccion() == 1) {

                    addMessage("Enviando Respuesta al cliente...");

                    this.oos.writeObject(obtenerMatrizCompleta());

                } else if (accion.getAccion() == 2) {//accion para obtener la ruta principal del gestor

                    Archivo archivo = (Archivo) accion.getObject();

                    if (accion.getSubAccion() == 0) {
                        if (archivo.getNombre().equals("root")) {
                            addMessage("Enviando Ruta de la Raiz al cliente...");

                            File f = new File(getPath());

                            Archivo arch = Utils.parseFileAtArchivoWithListFiles(f, Utils.replaceSeparator(File.separator + f.getName()));

                            addMessage("root archivo: " + arch.getRutaAbsoluta());
                            this.oos.writeObject(arch);
                        } else {
                            addMessage("Enviando Ruta especifica al cliente...");

                            String raizRelativa = Utils.getRelativeRaiz(getPath(), archivo.getRaiz());
                            String rutaCombinada = raizRelativa + archivo.getRutaRelativa();
                            File f = new File(rutaCombinada);
                            addMessage("archivo: " + rutaCombinada);
                            Archivo arch = Utils.parseFileAtArchivoWithListFiles(f, Utils.replaceSeparator(archivo.getRaiz()));

                            this.oos.writeObject(arch);
                        }
                    } else if (accion.getSubAccion() == 1) {
                        String raizRelativa = Utils.getRelativeRaiz(getPath(), archivo.getRaiz());
                        String rutaCombinada = raizRelativa + archivo.getRutaRelativa();
                        File f = new File(rutaCombinada);
                        if (f.delete()) {
                            this.oos.writeObject("exito");
                        } else {
                            this.oos.writeObject("fallo");
                        }

                    }

                } else if (accion.getAccion() == 3) {//accion para actualizar el detalle indicador (los meses)

                    DetalleIndicador detalleIndicador = (DetalleIndicador) accion.getObject();

                    if (actualizarDetalleIndicador(detalleIndicador)) {
                        this.oos.writeObject("exito");
                    } else {
                        this.oos.writeObject("fallo");
                    }

                } else if (accion.getAccion() == 4) {// accion para obtener una ruta especifica del gestor

                    Archivo archivo = (Archivo) accion.getObject();
                    try {

                        String raizRelativa = Utils.getRelativeRaiz(getPath(), archivo.getRaiz());
                        String rutaCombinada = raizRelativa + archivo.getRutaRelativa();

                        sendFile(rutaCombinada, archivo);
                    } catch (FileTransportException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (accion.getAccion() == 5) {

                    ProcesosCatalogo procesosEnviar = new ProcesosCatalogo();

                    if (accion.getSubAccion() == 0) {
                        if (accion.getObject() == null) {
                            procesosEnviar.setProcesos(buscarProcesos());
                            procesosEnviar.setEstatus(true);
                        } else {
                            procesosEnviar.setProcesos(buscarProcesos((UsuarioRegistro) accion.getObject()));
                            procesosEnviar.setEstatus(true);
                        }
                    } else if (accion.getSubAccion() == 1) {
                        procesosEnviar.setEstatus(insertarProceso((Proceso) accion.getObject()));
                    } else if (accion.getSubAccion() == 2) {
                        procesosEnviar.setEstatus(actualizarProceso((Proceso) accion.getObject()));
                    } else if (accion.getSubAccion() == 3) {
                        Object objAccion = accion.getObject();
                        if (objAccion instanceof Proceso) {
                            procesosEnviar.setEstatus(borrarProceso((Proceso) objAccion));
                        } else if (objAccion instanceof UsuarioProceso) {
                            procesosEnviar.setEstatus(borrarIndicadorProcesoUsuario((UsuarioProceso) objAccion));
                            procesosEnviar.setEstatus(borrarUsuarioProceso((UsuarioProceso) objAccion));
                        }
                    }

                    this.oos.writeObject(procesosEnviar);

                } else if (accion.getAccion() == 6) {
                    IndicadoresCatalogo indicadores = new IndicadoresCatalogo();

                    if (accion.getSubAccion() == 0) {
                        if (accion.getObject() == null) {
                            indicadores.setIndicadores(buscarIndicadoresCatalogo());
                            indicadores.setEstatus(true);
                        } else {
                            Object objAccion = accion.getObject();
                            if (objAccion instanceof Proceso) {
                                indicadores.setIndicadores(buscarIndicadores((Proceso) accion.getObject()));
                                indicadores.setEstatus(true);
                            } else if (objAccion instanceof UsuarioProceso) {
                                indicadores.setIndicadores(buscarIndicadores((UsuarioProceso) accion.getObject()));
                                indicadores.setEstatus(true);
                            }

                        }
                    } else if (accion.getSubAccion() == 1) {
                        indicadores.setEstatus(insertarIndicador((Indicador) accion.getObject()));
                    } else if (accion.getSubAccion() == 2) {
                        indicadores.setEstatus(actualizarIndicador((Indicador) accion.getObject()));
                    } else if (accion.getSubAccion() == 3) {
                        Object objAccion = accion.getObject();
                        if (objAccion instanceof Indicador) {
                            indicadores.setEstatus(borrarIndicador((Indicador) objAccion));
                        } else if (objAccion instanceof ProcesoIndicador) {
                            indicadores.setEstatus(borrarProcesoIndicador((ProcesoIndicador) objAccion));
                        } else if (objAccion instanceof IndicadorProcesoUsuario) {
                            indicadores.setEstatus(borrarIndicadorProcesoUsuario((IndicadorProcesoUsuario) objAccion));
                        }
                    }

                    this.oos.writeObject(indicadores);
                } else if (accion.getAccion() == 7) {
                    UsuarioRegistro usuarioRegistro = (UsuarioRegistro) accion.getObject();

                    if (actualizarContrasenia(usuarioRegistro)) {
                        this.oos.writeObject("exito");
                    } else {
                        this.oos.writeObject("fallo");
                    }
                } else if (accion.getAccion() == 8) {
                    UsuariosCatalogo usuariosCatalogo = new UsuariosCatalogo();

                    if (accion.getSubAccion() == 0) {
                        usuariosCatalogo.setUsuariosRegistro(buscarUsuarios());
                        usuariosCatalogo.setEstatus(true);
                    } else if (accion.getSubAccion() == 1) {
                        usuariosCatalogo.setEstatus(insertarUsuario((UsuarioRegistro) accion.getObject()));
                    } else if (accion.getSubAccion() == 2) {
                        usuariosCatalogo.setEstatus(actualizarUsuario((UsuarioRegistro) accion.getObject()));
                    } else if (accion.getSubAccion() == 3) {
                        usuariosCatalogo.setEstatus(borrarUsuario((UsuarioRegistro) accion.getObject()));
                    }
                    this.oos.writeObject(usuariosCatalogo);

                } else if (accion.getAccion() == 9) { //insertar proceso-indicador
                    ProcesoIndicador procesoIndicador = (ProcesoIndicador) accion.getObject();
                    procesoIndicador.setEstatus(insertarProcesoIndicador(procesoIndicador.getProceso(), procesoIndicador.getIndicador()));
                    this.oos.writeObject(procesoIndicador);
                } else if (accion.getAccion() == 10) { //insertar usuario-proceso
                    UsuarioProceso usuarioProceso = (UsuarioProceso) accion.getObject();
                    usuarioProceso.setEstatus(insertarUsuarioProceso(usuarioProceso.getUsuarioRegistro(), usuarioProceso.getProceso()));
                    this.oos.writeObject(usuarioProceso);
                } else if (accion.getAccion() == 11) { //insertar indicador-proceso-usuario
                    IndicadorProcesoUsuario ipu = (IndicadorProcesoUsuario) accion.getObject();
                    ipu.setEstatus(insertarIndicadorProcesoUsuario(ipu.getIndicador(), ipu.getProceso(), ipu.getUsuarioRegistro()));
                    this.oos.writeObject(ipu);
                } else if (accion.getAccion() == 12) {
                    String anioMes = (String) accion.getObject();
                    String[] split = anioMes.split(",");
                    File origen = new File(getPath() +File.separator+planta+File.separator + split[0] + File.separator + split[1]);   
                    File destino = new File(getPath()+File.separator+planta+File.separator+pathLink);
                    destino.mkdirs();
                    try {
                        Utils.copyFolder(origen, destino);
                        Utils.cambiarNombre(getPath()+File.separator+planta+File.separator+pathLink);
                        this.oos.writeObject("exito");
                    } catch (IOException e) {
                        this.oos.writeObject("fallo");
                    }

                } else {
                    this.oos.writeObject("no accion");
                }

            }

            this.ois.close();
            this.socket.close();

        } catch (SocketException e) {
            if (isStopped()) {
                addMessage("El servidor esta parado");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            addMessage("IOEXception: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            addMessage("ClassNotFoundException: " + e);
        } finally {
            try {
                if (this.ois != null) {
                    this.ois.close();
                }
                if (this.socket != null) {
                    this.socket.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block

                addMessage("Finally : " + e);
            }

        }
    }

    public UsuarioRegistro buscarUsuario(String usuario, String contrasenia) {

        addMessage("inicia conexion a la bd");
        Connection conexion = ConexionBD.GetConnection(planta);
        addMessage("se realizo la conexion");
        addMessage("usuario: " + usuario);
        addMessage("contrasenia: " + contrasenia);
        Statement st;
        ResultSet rs;
        String sql = "select * from usuario where usuario.id_usuario = " + usuario + " and contrasenia = " + "'" + contrasenia + "';";

        try {

            addMessage("inicia consulta");
            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            UsuarioRegistro usuarioRegistro = new UsuarioRegistro();
            addMessage("se realizo la consulta");
            while (rs.next()) {
                addMessage("existe usuario");
                usuarioRegistro.setIdUsuario(rs.getString("id_usuario"));
                usuarioRegistro.setNombre(rs.getString("nombre"));
                usuarioRegistro.setContrasenia(rs.getString("contrasenia"));
                usuarioRegistro.setApellidoPaterno(rs.getString("apellido_p"));
                usuarioRegistro.setApellidoMaterno(rs.getString("apellido_m"));
                usuarioRegistro.setEmail(rs.getString("e_mail"));

                usuarioRegistro.setProcesos(buscarProcesosDeUsuario(Integer.parseInt(rs.getString("id_usuario"))));

                usuarioRegistro.setRol(buscarRol(Integer.parseInt(usuarioRegistro.getIdUsuario())));

                return usuarioRegistro;
            }

        } catch (SQLException e) {
            addMessage("ocurrio un problema: " + e);
            JOptionPane.showMessageDialog(null, "Error al buscar usuario: " + e.toString());
        }

        return null;
    }

    public ArrayList<UsuarioRegistro> buscarUsuarios() {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;
        ResultSet rs;
        String sql = "select * from usuario";

        ArrayList<UsuarioRegistro> usuariosRegistro = new ArrayList<>();
        try {

            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            UsuarioRegistro usuarioRegistro = null;

            while (rs.next()) {
                usuarioRegistro = new UsuarioRegistro();
                usuarioRegistro.setIdUsuario(rs.getString("id_usuario"));
                usuarioRegistro.setNombre(rs.getString("nombre"));
                usuarioRegistro.setContrasenia(rs.getString("contrasenia"));
                usuarioRegistro.setApellidoPaterno(rs.getString("apellido_p"));
                usuarioRegistro.setApellidoMaterno(rs.getString("apellido_m"));
                usuarioRegistro.setEmail(rs.getString("e_mail"));

                //usuarioRegistro.setProcesos(buscarProcesosDeUsuario(Integer.parseInt(rs.getString("id_usuario"))));
                //usuarioRegistro.setRol(buscarRol(Integer.parseInt(usuarioRegistro.getIdUsuario())));
                usuariosRegistro.add(usuarioRegistro);

            }

        } catch (SQLException e) {
            addMessage("ocurrio un problema: " + e);
            JOptionPane.showMessageDialog(null, "Error al buscar usuario: " + e.toString());
        }

        return usuariosRegistro;
    }

    public ArrayList<Proceso> buscarProcesosDeUsuario(int idUsuario) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;
        ResultSet rs;

        String sql = "select * from proceso,DetalleUP\n"
                + "where proceso.id_proceso = DetalleUP.id_proceso\n"
                + "and DetalleUP.id_usuario = " + idUsuario + ";";

        ArrayList<Proceso> procesos = new ArrayList<Proceso>();
        try {
            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {

                Proceso proceso = new Proceso();
                proceso.setId(rs.getInt("id_proceso"));
                proceso.setNombre(rs.getString("nombre_proceso"));

                procesos.add(proceso);
            }

        } catch (SQLException e) {
            System.out.println("error select " + e);
        }

        return procesos;
    }

    public boolean insertarUsuario(UsuarioRegistro usuarioRegistro) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;

        try {
            st = conexion.createStatement();
            String sql = "insert into Usuario (id_usuario,nombre,apellido_p,apellido_m,e_mail) values(" + usuarioRegistro.getNumeroTarjeta() + ",'" + usuarioRegistro.getNombre() + "','" + usuarioRegistro.getApellidoPaterno() + "','" + usuarioRegistro.getApellidoMaterno() + "','" + usuarioRegistro.getEmail() + "')";
            st.executeUpdate(sql);

            conexion.close();
            st.close();
            addMessage("Registro usuario exitoso");
            
            insertarUsuarioRol(usuarioRegistro);
            //JOptionPane.showMessageDialog(null, "Registro exitoso");

            return true;

        } catch (SQLException e) {
            System.out.println("error insertar: " + e);
            return false;

        }

    }

    public boolean insertarProceso(Proceso proceso) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;

        try {
            st = conexion.createStatement();
            String sql = "insert into proceso (nombre_proceso) values('" + proceso.getNombre() + "')";
            st.executeUpdate(sql);

            conexion.close();
            st.close();
            addMessage("Registro proceso exitoso");
            //JOptionPane.showMessageDialog(null, "Registro exitoso");

            return true;

        } catch (SQLException e) {
            System.out.println("error insertar: " + e);
            return false;

        }

    }

    public boolean insertarIndicador(Indicador indicador) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;

        try {
            st = conexion.createStatement();
            String sql = "insert into indicador_cat (numero_grafica,indicador_descripcion,eficiencia_eficacia,target) "
                    + "values('" + indicador.getNumeroGrafica() + "','" + indicador.getDescripcionIndicador() + "','" + indicador.getEficienciaEficacia() + "','" + indicador.getTarget() + "')";
            st.executeUpdate(sql);

            conexion.close();
            st.close();
            addMessage("Registro indicador exitoso");
            addMessage("Agregando meses en 0 al indicador");
            //JOptionPane.showMessageDialog(null, "Registro exitoso");

            Indicador in = buscarUltimoIndicador();
            return procedimientoDetalleIndicador(in);

        } catch (SQLException e) {
            System.out.println("error insertar: " + e);
            return false;

        }

    }

    public boolean actualizarDetalleIndicador(DetalleIndicador detalleIndicador) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;

        try {
            st = conexion.createStatement();
            String sql = "update DetalleIndicador set porcentaje = '" + detalleIndicador.getPorcentaje() + "' where id_detallemi = " + detalleIndicador.getIdDetalleIndicador() + ";";
            int estatus = st.executeUpdate(sql);

            if (estatus > 0) {

                conexion.close();
                st.close();

                System.out.println("update detalle correctamente");

                return true;
            } else {
                conexion.close();
                st.close();
                System.out.println("update detalle no actualizado");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("error update: " + e);
            return false;

        }

    }

    public boolean actualizarContrasenia(UsuarioRegistro usuarioRegistro) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;

        try {
            st = conexion.createStatement();
            String sql = "update Usuario set contrasenia = '" + usuarioRegistro.getContrasenia() + "' where id_usuario = " + usuarioRegistro.getIdUsuario() + ";";
            int estatus = st.executeUpdate(sql);

            if (estatus > 0) {

                conexion.close();
                st.close();

                addMessage("Se cambio la contraseña de " + usuarioRegistro.getNombre());

                return true;
            } else {
                conexion.close();
                st.close();
                addMessage("No se cambio la contraseña de " + usuarioRegistro.getNombre());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("error update: " + e);
            addMessage("Error actualizar contraseña " + usuarioRegistro.getNombre());
            return false;

        }

    }

    public boolean actualizarUsuario(UsuarioRegistro usuarioRegistro) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;

        try {
            st = conexion.createStatement();
            String sql = "update Usuario set "
                    + "nombre = '" + usuarioRegistro.getNombre() + "'"
                    + ",apellido_p = '" + usuarioRegistro.getApellidoPaterno() + "'"
                    + ",apellido_m = '" + usuarioRegistro.getApellidoMaterno() + "'"
                    + ",e_mail = '" + usuarioRegistro.getEmail() + "'"
                    + " where id_usuario = " + usuarioRegistro.getIdUsuario() + ";";
            int estatus = st.executeUpdate(sql);

            if (estatus > 0) {

                conexion.close();
                st.close();

                addMessage("Se actualizo el usuario " + usuarioRegistro.getNombre());

                return true;
            } else {
                conexion.close();
                st.close();
                addMessage("No se actualizo el usuario " + usuarioRegistro.getNombre());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("error update: " + e);
            addMessage("Error actualizar el usuario " + usuarioRegistro.getNombre());
            return false;

        }

    }

    public boolean actualizarProceso(Proceso proceso) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;

        try {
            st = conexion.createStatement();
            String sql = "update Proceso set "
                    + "nombre_proceso = '" + proceso.getNombre() + "'"
                    + " where id_proceso = " + proceso.getId() + ";";
            int estatus = st.executeUpdate(sql);

            if (estatus > 0) {

                conexion.close();
                st.close();

                addMessage("Se actualizo el proceso " + proceso.getNombre());

                return true;
            } else {
                conexion.close();
                st.close();
                addMessage("No se actualizo el proceso " + proceso.getNombre());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("error update: " + e);
            addMessage("Error actualizar el proceso " + proceso.getNombre());
            return false;

        }

    }

    public boolean actualizarIndicador(Indicador indicador) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;

        try {
            st = conexion.createStatement();
            String sql = "update indicador_cat set "
                    + "numero_grafica = '" + indicador.getNumeroGrafica() + "'"
                    + ",indicador_descripcion = '" + indicador.getDescripcionIndicador() + "'"
                    + ",eficiencia_eficacia = '" + indicador.getEficienciaEficacia() + "'"
                    + ",target = '" + indicador.getTarget() + "'"
                    + " where id_indicador = " + indicador.getIdIndicador() + ";";
            int estatus = st.executeUpdate(sql);

            if (estatus > 0) {

                conexion.close();
                st.close();

                addMessage("Se actualizo el indicador " + indicador.getDescripcionIndicador());

                return true;
            } else {
                conexion.close();
                st.close();
                addMessage("No se actualizo el indicador " + indicador.getDescripcionIndicador());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("error update: " + e);
            addMessage("Error actualizar el indicador " + indicador.getDescripcionIndicador());
            return false;

        }

    }

    public boolean insertarProcesoIndicador(Proceso proceso, Indicador indicador) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;

        try {
            st = conexion.createStatement();
            String sql = "insert into proceso_indicador (id_proceso,id_indicador) "
                    + "values(" + proceso.getId() + "," + indicador.getIdIndicador() + ")";
            st.executeUpdate(sql);

            conexion.close();
            st.close();
            addMessage("Registro proceso-indicador exitoso");
            //JOptionPane.showMessageDialog(null, "Registro exitoso");

            return true;

        } catch (SQLException e) {
            System.out.println("error insertar: " + e);
            return false;

        }

    }

    public boolean insertarUsuarioProceso(UsuarioRegistro usuarioRegistro, Proceso proceso) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;

        try {
            st = conexion.createStatement();
            String sql = "insert into DetalleUP (id_usuario,id_proceso) "
                    + "values(" + usuarioRegistro.getIdUsuario() + "," + proceso.getId() + ")";
            st.executeUpdate(sql);

            conexion.close();
            st.close();
            addMessage("Registro proceso-indicador exitoso");
            //JOptionPane.showMessageDialog(null, "Registro exitoso");

            return true;

        } catch (SQLException e) {
            System.out.println("error insertar: " + e);
            return false;

        }

    }
    
    public boolean insertarUsuarioRol(UsuarioRegistro usuarioRegistro) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;

        try {
            st = conexion.createStatement();
            String sql = "insert into DetalleUR (id_usuario,id_rol) "
                    + "values(" + usuarioRegistro.getNumeroTarjeta() + "," + 1 + ")";
            st.executeUpdate(sql);

            conexion.close();
            st.close();
            addMessage("Registro usuario-rol exitoso");
            //JOptionPane.showMessageDialog(null, "Registro exitoso");

            return true;

        } catch (SQLException e) {
            System.out.println("error insertar: " + e);
            return false;

        }

    }


    public boolean insertarIndicadorProcesoUsuario(Indicador indicador, Proceso proceso, UsuarioRegistro usuarioRegistro) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;

        try {
            st = conexion.createStatement();
            String sql = "insert into indicador_proceso_usuario (id_indicador,id_proceso,id_usuario) "
                    + "values(" + indicador.getIdIndicador() + "," + proceso.getId() + "," + usuarioRegistro.getIdUsuario() + ")";
            st.executeUpdate(sql);

            conexion.close();
            st.close();
            addMessage("Registro indicador-proceso-usuario exitoso");
            //JOptionPane.showMessageDialog(null, "Registro exitoso");

            return true;

        } catch (SQLException e) {
            System.out.println("error insertar: " + e);
            return false;

        }

    }

    public ArrayList<Indicador> buscarIndicadores() {

        ArrayList<Indicador> indicadores = new ArrayList<>();
        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;
        ResultSet rs;
        String sql = "select indicador_cat.id_indicador,nombre_proceso,nombre,apellido_p,numero_grafica,indicador_descripcion,eficiencia_eficacia,target,rol.id_rol from proceso,indicador_cat,usuario,indicador_proceso_usuario,rol,detalleur\n"
                + "where proceso.id_proceso = indicador_proceso_usuario.id_proceso\n"
                + "and indicador_cat.id_indicador = indicador_proceso_usuario.id_indicador\n"
                + "and usuario.id_usuario = indicador_proceso_usuario.id_usuario\n"
                + "and usuario.id_usuario = detalleur.id_usuario\n" 
                + "and rol.id_rol = detalleur.id_rol order by numero_grafica;";

        try {
            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {

                Indicador indicador = new Indicador();
                indicador.setIdIndicador(rs.getInt("id_indicador"));
                indicador.setNombreProceso(rs.getString("nombre_proceso"));
                indicador.setDuenioProceso(rs.getString("nombre") + " " + rs.getString("apellido_p"));
                indicador.setNumeroGrafica(rs.getString("numero_grafica"));
                indicador.setDescripcionIndicador(rs.getString("indicador_descripcion"));
                indicador.setEficienciaEficacia(rs.getString("eficiencia_eficacia"));
                indicador.setTarget(rs.getString("target"));
                indicador.setIdRol(rs.getInt("id_rol"));
                

                indicadores.add(indicador);

            }

            conexion.close();
            st.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("error select " + e);
        }

        return indicadores;
    }

    public ArrayList<DetalleIndicador> agregarPorcentajes(int idIndicador) {

        System.out.println("idIndicador: " + idIndicador);
        ArrayList<DetalleIndicador> detalleIndicadores = new ArrayList<>();
        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;
        ResultSet rs;
        String sql = "select DetalleIndicador.id_detallemi,indicador_cat.id_indicador,dia,mes,anio,porcentaje from indicador_cat,DetalleIndicador\n"
                + "where indicador_cat.id_indicador = DetalleIndicador.id_indicador\n"
                + "and indicador_cat.id_indicador = " + idIndicador;

        try {
            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                DetalleIndicador detalleIndicador = new DetalleIndicador();
                detalleIndicador.setIdDetalleIndicador(rs.getInt("id_detallemi"));
                detalleIndicador.setIdIndicador(rs.getInt("id_indicador"));
                detalleIndicador.setDia(rs.getString("dia"));
                detalleIndicador.setMes(rs.getString("mes"));
                detalleIndicador.setAnio(rs.getString("anio"));
                detalleIndicador.setPorcentaje(rs.getString("porcentaje"));

                detalleIndicadores.add(detalleIndicador);

            }

            conexion.close();
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("error select " + e);
        }

        return detalleIndicadores;
    }

    public ArrayList<Indicador> obtenerMatrizCompleta() {

        ArrayList<Indicador> indicadores = buscarIndicadores();

        for (Indicador indicador : indicadores) {

            indicador.setDetalleIndicadores(agregarPorcentajes(indicador.getIdIndicador()));
        }

        return indicadores;

    }

    private void sendFile(String path, Archivo archivo) throws FileTransportException {
        String name;
        byte[] byteFile;
        File file = null;
        if (path != null) {
            file = new File(path);
        }

        try {

            name = file.getName();
            byteFile = Utils.getBytesFromFile(file);

            FileTransport ft = new FileTransport();
            ft.setName(name);
            ft.setFilearray(byteFile);
            ft.setProceso("");
            ft.setFecha(Utils.getFecha());
            ft.setHora(Utils.getHora());
            ft.setArchivo(archivo);

            addMessage("Enviando archivo al cliente...");

            this.oos.writeObject(ft);

            addMessage("El archivo " + name + " fue enviado al cliente");

            this.socket.close();
        } catch (IOException e) {
            throw new FileTransportException("Ha ocurrido un error: "
                    + e.getMessage());
        }
    }

    public ArrayList<Proceso> buscarProcesos() {

        ArrayList<Proceso> procesos = new ArrayList<>();
        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;
        ResultSet rs;
        String sql = "select * from proceso";
        try {
            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Proceso proceso = new Proceso();
                proceso.setId(rs.getInt("id_proceso"));
                proceso.setNombre(rs.getString("nombre_proceso"));

                procesos.add(proceso);

            }

            conexion.close();
            st.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("error select " + e);
        }

        return procesos;

    }

    public ArrayList<Proceso> buscarProcesos(UsuarioRegistro usuarioRegistro) {

        ArrayList<Proceso> procesos = new ArrayList<>();
        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;
        ResultSet rs;
        String sql = "select proceso.id_proceso,nombre_proceso from usuario,proceso,DetalleUP\n"
                + "where usuario.id_usuario = DetalleUP.id_usuario\n"
                + "and proceso.id_proceso = DetalleUP.id_proceso\n"
                + "and usuario.id_usuario = " + usuarioRegistro.getIdUsuario() + ";";
        try {
            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Proceso proceso = new Proceso();
                proceso.setId(rs.getInt("id_proceso"));
                proceso.setNombre(rs.getString("nombre_proceso"));

                procesos.add(proceso);

            }

            conexion.close();
            st.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("error select " + e);
        }

        return procesos;

    }
    
    public ArrayList<UsuarioRegistro> buscarUsuariosProceso(String nombreProceso){
    
        ArrayList<UsuarioRegistro> usuarios = new ArrayList<>();
        Connection conexion = ConexionBD.GetConnection(planta);
        
        Statement st;
        ResultSet rs;
        String sql = "select * from usuario,proceso,DetalleUP\n" +
"where proceso.id_proceso = DetalleUP.id_proceso\n" +
"and Usuario.id_usuario = DetalleUP.id_usuario\n" +
"and proceso.nombre_proceso = '"+nombreProceso+"';";

        try {

            addMessage("inicia consulta");
            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            
            addMessage("se realizo la consulta");
            while (rs.next()) {
                addMessage("existe usuario");
                UsuarioRegistro usuarioRegistro = new UsuarioRegistro();
                usuarioRegistro.setIdUsuario(rs.getString("id_usuario"));
                usuarioRegistro.setNombre(rs.getString("nombre"));
                usuarioRegistro.setContrasenia(rs.getString("contrasenia"));
                usuarioRegistro.setApellidoPaterno(rs.getString("apellido_p"));
                usuarioRegistro.setApellidoMaterno(rs.getString("apellido_m"));
                usuarioRegistro.setEmail(rs.getString("e_mail"));
                usuarios.add(usuarioRegistro);
            }

        } catch (SQLException e) {
            addMessage("ocurrio un problema: " + e);
            JOptionPane.showMessageDialog(null, "Error al buscar usuario: " + e.toString());
        }

        return usuarios;
    
        
    }

    public ArrayList<Indicador> buscarIndicadores(Proceso proceso) {

        ArrayList<Indicador> indicadores = new ArrayList<>();
        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;
        ResultSet rs;
        String sql = "select nombre_proceso,indicador_cat.id_indicador,numero_grafica,indicador_descripcion,eficiencia_eficacia,target from proceso,indicador_cat,proceso_indicador\n"
                + "where proceso.id_proceso = proceso_indicador.id_proceso\n"
                + "and indicador_cat.id_indicador = proceso_indicador.id_indicador\n"
                + "and proceso.id_proceso = " + proceso.getId() + ";";

        try {
            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {

                Indicador indicador = new Indicador();
                indicador.setIdIndicador(rs.getInt("id_indicador"));
                indicador.setNombreProceso(rs.getString("nombre_proceso"));
                indicador.setNumeroGrafica(rs.getString("numero_grafica"));
                indicador.setDescripcionIndicador(rs.getString("indicador_descripcion"));
                indicador.setEficienciaEficacia(rs.getString("eficiencia_eficacia"));
                indicador.setTarget(rs.getString("target"));

                indicadores.add(indicador);

            }

            conexion.close();
            st.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("error select " + e);
        }

        return indicadores;
    }

    public ArrayList<Indicador> buscarIndicadores(UsuarioProceso usuarioProceso) {

        ArrayList<Indicador> indicadores = new ArrayList<>();
        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;
        ResultSet rs;
        String sql = "select indicador_cat.id_indicador,nombre_proceso,numero_grafica,indicador_descripcion,eficiencia_eficacia,target from proceso,indicador_cat,usuario,indicador_proceso_usuario\n"
                + "                    where proceso.id_proceso = indicador_proceso_usuario.id_proceso\n"
                + "                    and indicador_cat.id_indicador = indicador_proceso_usuario.id_indicador\n"
                + "                    and usuario.id_usuario = indicador_proceso_usuario.id_usuario \n"
                + "                    and usuario.id_usuario = " + usuarioProceso.getUsuarioRegistro().getIdUsuario() + "\n"
                + "                    and proceso.id_proceso = " + usuarioProceso.getProceso().getId() + "\n"
                + "                    order by numero_grafica;";

        try {
            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {

                Indicador indicador = new Indicador();
                indicador.setIdIndicador(rs.getInt("id_indicador"));
                indicador.setNombreProceso(rs.getString("nombre_proceso"));
                indicador.setNumeroGrafica(rs.getString("numero_grafica"));
                indicador.setDescripcionIndicador(rs.getString("indicador_descripcion"));
                indicador.setEficienciaEficacia(rs.getString("eficiencia_eficacia"));
                indicador.setTarget(rs.getString("target"));

                indicadores.add(indicador);

            }

            conexion.close();
            st.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("error select " + e);
        }

        return indicadores;
    }

    public ArrayList<Indicador> buscarIndicadoresCatalogo() {

        ArrayList<Indicador> indicadores = new ArrayList<>();
        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;
        ResultSet rs;
        String sql = "select * from indicador_cat order by numero_grafica";

        try {
            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {

                Indicador indicador = new Indicador();
                indicador.setIdIndicador(rs.getInt("id_indicador"));
                indicador.setNumeroGrafica(rs.getString("numero_grafica"));
                indicador.setDescripcionIndicador(rs.getString("indicador_descripcion"));
                indicador.setEficienciaEficacia(rs.getString("eficiencia_eficacia"));
                indicador.setTarget(rs.getString("target"));

                indicadores.add(indicador);

            }

            conexion.close();
            st.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("error select " + e);
        }

        return indicadores;
    }

    public Rol buscarRol(int idUsuario) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;
        ResultSet rs;
        String sql = "select Rol.id_rol,rol from Usuario,Rol,DetalleUR\n"
                + "where Usuario.id_usuario = DetalleUR.id_usuario\n"
                + "and Rol.id_rol = DetalleUR.id_rol\n"
                + "and Usuario.id_usuario = " + idUsuario;

        Rol rol = new Rol();
        try {
            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {

                System.out.println("Rol.id_rol: " + rs.getInt("id_rol"));
                System.out.println("rol: " + rs.getString("rol"));
                rol.setIdRol(rs.getInt("id_rol"));
                rol.setRol(rs.getString("rol"));

            }

            conexion.close();
            st.close();
            rs.close();

            return rol;

        } catch (SQLException e) {
            System.out.println("error select " + e);
        }

        return rol;
    }

    public Indicador buscarUltimoIndicador() {

        Indicador indicador = new Indicador();
        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;
        ResultSet rs;
        //ultimo registro mysql
        //String sql = "select * from indicador_cat order by id_indicador desc limit 1;";

        //ultimo registro sql server
        String sql = "SELECT * FROM indicador_cat WHERE id_indicador = (SELECT MAX(id_indicador) FROM indicador_cat);";
        try {
            st = conexion.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {

                indicador.setIdIndicador(rs.getInt("id_indicador"));
                indicador.setNumeroGrafica(rs.getString("numero_grafica"));
                indicador.setDescripcionIndicador(rs.getString("indicador_descripcion"));
                indicador.setEficienciaEficacia(rs.getString("eficiencia_eficacia"));
                indicador.setTarget(rs.getString("target"));

                addMessage("ultimo indicador encontrado id: " + indicador.getIdIndicador());

            }

            conexion.close();
            st.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("error select " + e);
        }

        return indicador;
    }

    public boolean procedimientoDetalleIndicador(Indicador indicador) {

        Connection conexion = ConexionBD.GetConnection(planta);

        try {
            conexion.setAutoCommit(false);

            Statement st = conexion.createStatement();
            st.executeUpdate("insert into DetalleIndicador (id_indicador,dia,mes,anio)"
                    + " values (" + indicador.getIdIndicador() + ",'15','Enero','2015');");
            st.executeUpdate("insert into DetalleIndicador (id_indicador,dia,mes,anio)"
                    + " values (" + indicador.getIdIndicador() + ",'15','Febrero','2015');");
            st.executeUpdate("insert into DetalleIndicador (id_indicador,dia,mes,anio)"
                    + " values (" + indicador.getIdIndicador() + ",'15','Marzo','2015');");
            st.executeUpdate("insert into DetalleIndicador (id_indicador,dia,mes,anio)"
                    + " values (" + indicador.getIdIndicador() + ",'15','Abril','2015');");
            st.executeUpdate("insert into DetalleIndicador (id_indicador,dia,mes,anio)"
                    + " values (" + indicador.getIdIndicador() + ",'15','Mayo','2015');");
            st.executeUpdate("insert into DetalleIndicador (id_indicador,dia,mes,anio)"
                    + " values (" + indicador.getIdIndicador() + ",'15','Junio','2015');");
            st.executeUpdate("insert into DetalleIndicador (id_indicador,dia,mes,anio)"
                    + " values (" + indicador.getIdIndicador() + ",'15','Julio','2015');");
            st.executeUpdate("insert into DetalleIndicador (id_indicador,dia,mes,anio)"
                    + " values (" + indicador.getIdIndicador() + ",'15','Agosto','2015');");
            st.executeUpdate("insert into DetalleIndicador (id_indicador,dia,mes,anio)"
                    + " values (" + indicador.getIdIndicador() + ",'15','Septiembre','2015');");
            st.executeUpdate("insert into DetalleIndicador (id_indicador,dia,mes,anio)"
                    + " values (" + indicador.getIdIndicador() + ",'15','Octubre','2015');");
            st.executeUpdate("insert into DetalleIndicador (id_indicador,dia,mes,anio)"
                    + " values (" + indicador.getIdIndicador() + ",'15','Noviembre','2015');");
            st.executeUpdate("insert into DetalleIndicador (id_indicador,dia,mes,anio)"
                    + " values (" + indicador.getIdIndicador() + ",'15','Diciembre','2015');");
            conexion.commit();
            st.close();
            conexion.close();
            return true;

        } catch (SQLException e) {
            addMessage("error select " + e);
            try {
                addMessage("fallo la transaccion insertar meses realizando rollback");
                conexion.rollback();
                conexion.close();

                return false;
            } catch (SQLException ex) {
                addMessage("fallo rollbacl");
                return false;
            }

        }
    }

    public boolean borrarIndicador(Indicador indicador) {

        Connection conexion = ConexionBD.GetConnection(planta);

        try {
            conexion.setAutoCommit(false);
            Statement st = conexion.createStatement();
            st.executeUpdate("delete from DetalleIndicador where id_indicador = " + indicador.getIdIndicador() + ";");
            st.executeUpdate("delete from proceso_indicador where id_indicador = " + indicador.getIdIndicador() + ";");
            st.executeUpdate("delete from indicador_proceso_usuario where id_indicador = " + indicador.getIdIndicador() + ";");
            st.executeUpdate("delete from indicador_cat where id_indicador = " + indicador.getIdIndicador() + ";");

            conexion.commit();
            st.close();
            conexion.close();

            addMessage("Se borro el indicador: " + indicador.getDescripcionIndicador());
            return true;

        } catch (SQLException e) {
            addMessage("error select " + e);
            try {
                addMessage("fallo la transaccion borrarIndicador realizando rollback");
                conexion.rollback();
                return false;
            } catch (SQLException ex) {
                addMessage("fallo rollback");
                return false;
            }

        }
    }

    public boolean borrarProceso(Proceso proceso) {

        Connection conexion = ConexionBD.GetConnection(planta);

        try {
            conexion.setAutoCommit(false);
            Statement st = conexion.createStatement();
            st.executeUpdate("delete from DetalleUP where id_proceso = " + proceso.getId() + ";");
            st.executeUpdate("delete from proceso_indicador where id_proceso = " + proceso.getId() + ";");
            st.executeUpdate("delete from indicador_proceso_usuario where id_proceso = " + proceso.getId() + ";");
            st.executeUpdate("delete from proceso where id_proceso = " + proceso.getId() + ";");

            conexion.commit();
            st.close();
            conexion.close();
            addMessage("Se borro el proceso: " + proceso.getNombre());

            return true;

        } catch (SQLException e) {
            addMessage("error select " + e);
            try {
                addMessage("fallo la transaccion borrarProceso realizando rollback");
                conexion.rollback();
                return false;
            } catch (SQLException ex) {
                addMessage("fallo rollback");
                return false;
            }

        }
    }

    public boolean borrarUsuario(UsuarioRegistro usuario) {

        Connection conexion = ConexionBD.GetConnection(planta);

        try {
            conexion.setAutoCommit(false);
            Statement st = conexion.createStatement();
            st.executeUpdate("delete from DetalleUR where id_usuario = " + usuario.getIdUsuario() + ";");
            st.executeUpdate("delete from DetalleUP where id_usuario = " + usuario.getIdUsuario() + ";");
            st.executeUpdate("delete from indicador_proceso_usuario where id_usuario = " + usuario.getIdUsuario() + ";");
            st.executeUpdate("delete from usuario where id_usuario = " + usuario.getIdUsuario() + ";");

            conexion.commit();
            st.close();
            conexion.close();

            addMessage("Se borro el usuario: " + usuario.getNombre());
            return true;

        } catch (SQLException e) {
            addMessage("error select " + e);
            try {
                addMessage("fallo la transaccion borrarUsuario realizando rollback");
                conexion.rollback();
                return false;
            } catch (SQLException ex) {
                addMessage("fallo rollback");
                return false;
            }

        }
    }

    public boolean borrarProcesoIndicador(ProcesoIndicador procesoIndicador) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;

        try {
            st = conexion.createStatement();
            String sql = "delete from proceso_indicador where id_proceso = " + procesoIndicador.getProceso().getId()
                    + " and id_indicador = " + procesoIndicador.getIndicador().getIdIndicador()
                    + ";";
            int estatus = st.executeUpdate(sql);
            if (estatus > 0) {

                conexion.close();
                st.close();

                addMessage("Se borro el indicador " + procesoIndicador.getIndicador().getDescripcionIndicador() + " del proceso:" + procesoIndicador.getProceso().getNombre());

                return true;
            } else {
                conexion.close();
                st.close();
                addMessage("No se borro el indicador " + procesoIndicador.getIndicador().getDescripcionIndicador() + " del proceso:" + procesoIndicador.getProceso().getNombre());
                return false;
            }

        } catch (SQLException e) {
            addMessage("Error al borrar el indicador " + procesoIndicador.getIndicador().getDescripcionIndicador() + " del proceso:" + procesoIndicador.getProceso().getNombre());
            return false;

        }

    }

    public boolean borrarIndicadorProcesoUsuario(IndicadorProcesoUsuario ipu) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;

        try {
            st = conexion.createStatement();
            String sql = "delete from indicador_proceso_usuario where id_proceso = " + ipu.getProceso().getId()
                    + " and id_usuario = " + ipu.getUsuarioRegistro().getIdUsuario()
                    + " and id_indicador = " + ipu.getIndicador().getIdIndicador()
                    + ";";
            int estatus = st.executeUpdate(sql);
            if (estatus > 0) {

                conexion.close();
                st.close();

                addMessage("Se borro el indicador " + ipu.getIndicador().getDescripcionIndicador()
                        + " del proceso:" + ipu.getProceso().getNombre()
                        + " y del usuario:" + ipu.getUsuarioRegistro().getNombre());

                return true;
            } else {
                conexion.close();
                st.close();
                addMessage("No se borro el indicador " + ipu.getIndicador().getDescripcionIndicador()
                        + " del proceso:" + ipu.getProceso().getNombre()
                        + " y del usuario:" + ipu.getUsuarioRegistro().getNombre());
                return false;
            }

        } catch (SQLException e) {
            addMessage("Error al borrar el indicador " + ipu.getIndicador().getDescripcionIndicador()
                    + " del proceso:" + ipu.getProceso().getNombre()
                    + " y del usuario:" + ipu.getUsuarioRegistro().getNombre());
            return false;

        }

    }

    public boolean borrarIndicadorProcesoUsuario(UsuarioProceso usuarioProceso) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;

        try {
            st = conexion.createStatement();
            String sql = "delete from indicador_proceso_usuario where id_proceso = " + usuarioProceso.getProceso().getId()
                    + " and id_usuario = " + usuarioProceso.getUsuarioRegistro().getIdUsuario()
                    + ";";
            int estatus = st.executeUpdate(sql);
            if (estatus > 0) {

                conexion.close();
                st.close();

                addMessage("Se borro el proceso:" + usuarioProceso.getProceso().getNombre()
                        + " del usuario:" + usuarioProceso.getUsuarioRegistro().getNombre());

                return true;
            } else {
                conexion.close();
                st.close();
                addMessage("No borro el proceso:" + usuarioProceso.getProceso().getNombre()
                        + " del usuario:" + usuarioProceso.getUsuarioRegistro().getNombre());
                return false;
            }

        } catch (SQLException e) {
            addMessage("Error al borrar el proceso:" + usuarioProceso.getProceso().getNombre()
                    + " del usuario:" + usuarioProceso.getUsuarioRegistro().getNombre());
            return false;

        }

    }

    public boolean borrarUsuarioProceso(UsuarioProceso usuarioProceso) {

        Connection conexion = ConexionBD.GetConnection(planta);
        Statement st;

        try {
            st = conexion.createStatement();
            String sql = "delete from DetalleUP where id_proceso = " + usuarioProceso.getProceso().getId()
                    + " and id_usuario = " + usuarioProceso.getUsuarioRegistro().getIdUsuario()
                    + ";";
            int estatus = st.executeUpdate(sql);
            if (estatus > 0) {

                conexion.close();
                st.close();

                addMessage("Se borro el proceso up:" + usuarioProceso.getProceso().getNombre()
                        + " del usuario:" + usuarioProceso.getUsuarioRegistro().getNombre());

                return true;
            } else {
                conexion.close();
                st.close();
                addMessage("No borro el proceso up:" + usuarioProceso.getProceso().getNombre()
                        + " del usuario:" + usuarioProceso.getUsuarioRegistro().getNombre());
                return false;
            }

        } catch (SQLException e) {
            addMessage("Error al borrar el proceso up:" + usuarioProceso.getProceso().getNombre()
                    + " del usuario:" + usuarioProceso.getUsuarioRegistro().getNombre());
            return false;

        }

    }
}
