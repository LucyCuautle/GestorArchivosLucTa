/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsistematornel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class ConexionBD {
      
    public static Connection GetConnection(String planta)
    {
        Connection conexion=null;
      
        try
        {
            /*Class.forName("com.mysql.jdbc.Driver");
            String servidor = "jdbc:mysql://localhost/direcciontornel";//direcciontornel
            String usuarioDB="root";
            String passwordDB="root";
            conexion= DriverManager.getConnection(servidor,usuarioDB,passwordDB);*/
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://;database=direcciontornel"+planta+";integratedSecurity=true;";
            conexion =  DriverManager.getConnection(connectionUrl);
            
        }
        catch(ClassNotFoundException ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error1 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error2 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error3 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        finally
        {
            return conexion;
        }
    }
}