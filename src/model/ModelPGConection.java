/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OWNER
 */
public class ModelPGConection {

    String cadenaConexion = "jdbc:postgresql://localhost:5432/base_PV";
    String pgUsuario = "postgres";
    String pgContra = "123";
    Connection conex;

    public ModelPGConection() {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ModelPGConection.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            conex = DriverManager.getConnection(cadenaConexion, pgUsuario, pgContra);
        } catch (SQLException ex) {
            Logger.getLogger(ModelPGConection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ResultSet consulta(String sql) {
        
        try {
            Statement at = conex.createStatement();
            return at.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(ModelPGConection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean accion(String sql) {
        //INSERT-UPDATE-DELETE

        boolean correcto;

        try {
            Statement at = conex.createStatement();
            at.execute(sql);
            at.close();//Cierro la conexion
            correcto = true;
            
        } catch (Exception e) {
            Logger.getLogger(ModelPGConection.class.getName()).log(Level.SEVERE, null, e);
            correcto = false;
        }
        return correcto;
    }

    public Connection getConex() {
        return conex;
    }

    public void setConex(Connection conex) {
        this.conex = conex;
    }
    
    
}
