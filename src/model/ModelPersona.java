/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 *
 * @author OWNER
 */
public class ModelPersona extends Persona {

    ModelPGConection mpgc = new ModelPGConection();

    public ModelPersona() {

    }

    public ModelPersona(String idpersona, String nombres, String apellidos, Date fechanacimiento, String telefono, String sexo, double sueldo, int cupo) {
        super(idpersona, nombres, apellidos, fechanacimiento, telefono, sexo, sueldo, cupo);
    }

    public List<Persona> getPersona() {
        List<Persona> listaPersonas = new ArrayList<Persona>();

        String sql = "select * from persona";
        ResultSet rs = mpgc.consulta(sql);
        byte[] bytea;

        try {
            while (rs.next()) {
                Persona person = new Persona();
                person.setIdpersona(rs.getString("idpersona"));
                person.setNombres(rs.getString("nombres"));
                person.setApellidos(rs.getString("apellidos"));
                person.setFechanacimiento(rs.getDate("fechanacimiento"));
                person.setTelefono(rs.getString("telefono"));
                person.setSexo(rs.getString("sexo"));
                person.setSueldo(rs.getDouble("Sueldo"));
                person.setCupo(rs.getInt("cupo"));

                bytea = rs.getBytes("foto");
                try {
                    if (bytea != null) {
                        person.setFoto(getImagen(bytea));
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ModelPersona.class.getName()).log(Level.SEVERE, null, ex);
                }

                listaPersonas.add(person);
            }
        } catch (SQLException e) {
            Logger.getLogger(ModelPGConection.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ModelPersona.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaPersonas;
    }

    public boolean setPersona() {
        String sql = "INSERT INTO persona (idpersona, nombres, apellidos, fechanacimiento, telefono, sexo, sueldo, cupo) "
                + "VALUES('" + getIdpersona() + "', '" + getNombres() + "', '" + getApellidos() + "', '" + getFechanacimiento() + "',"
                + " '" + getTelefono() + "', '" + getSexo() + "', '" + getSueldo() + "', '" + getCupo() + "')";
        return mpgc.accion(sql);
    }

    public boolean setPersonaFoto() {
        String sql;
        sql = "INSERT INTO persona (idpersona, nombres, apellidos, fechanacimiento, telefono, sexo, sueldo, cupo, foto)";
        sql += "VALUES (?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps = mpgc.conex.prepareStatement(sql);
            ps.setString(1, getIdpersona());
            ps.setString(2, getNombres());
            ps.setString(3, getApellidos());
            ps.setDate(4, (java.sql.Date) getFechanacimiento());
            ps.setString(5, getTelefono());
            ps.setString(6, getSexo());
            ps.setDouble(7, getSueldo());
            ps.setInt(8, getCupo());
            ps.setBinaryStream(9, getImageFile(), getLength());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModelPersona.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean uptadePersona() {
        String sql = "UPDATE persona SET nombres='" + getNombres() + "', apellidos='" + getApellidos() + "', "
                + "fechanacimiento='" + getFechanacimiento() + "', telefono='" + getTelefono() + "', sexo='" + getSexo() + "', "
                + "sueldo='" + getSueldo() + "', cupo='" + getCupo() + "', foto='"+ getFoto()+"' WHERE idpersona='" + getIdpersona() + "'";
        return mpgc.accion(sql);
    }

    public boolean deletePersona() {
        String sql = "DELETE FROM persona where idpersona='" + getIdpersona() + "'";
        return mpgc.accion(sql);
    }

    public boolean buscarPersona() {
        String sql = "select idpersona, nombres, apellidos, fechanacimiento, telefono, sexo, sueldo, cupo from persona "
                + "where idpersona like '" + getIdpersona() + "%' OR lower(nombres) like'" + getNombres() + "%' OR lower(apellidos) like'" + getApellidos() + "%'";
        return mpgc.accion(sql);
    }

    private Image getImagen(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        Iterator it = ImageIO.getImageReadersByFormatName("jpeg");
        ImageReader imagereader = (ImageReader) it.next();
        Object source = bais;

        ImageInputStream iis = ImageIO.createImageInputStream(source);
        imagereader.setInput(iis, true);
        ImageReadParam param = imagereader.getDefaultReadParam();
        param.setSourceSubsampling(1, 1, 0, 0);

        return imagereader.read(0, param);
    }
}
