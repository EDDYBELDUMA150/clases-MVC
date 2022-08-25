/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controler;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.ws.Holder;
import model.ModelPGConection;
import model.ModelPersona;
import model.Persona;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import view.ViewPersona;

/**
 *
 * @author OWNER
 */
public class ControlerPersona {

    private ModelPersona modelo;
    private ViewPersona vista;
    private JFileChooser jfc;

    public ControlerPersona(ModelPersona modelo, ViewPersona vista) {
        this.modelo = modelo;
        this.vista = vista;
        vista.setVisible(true);
    }

    public void iniciaControl() {
        vista.getBtCargar().addActionListener(l -> cargaDatos());
        vista.getBtcrear().addActionListener(l -> abrirdialog(1));
        vista.getBteditar().addActionListener(l -> abrirdialog(2));
        vista.getBtAceptar().addActionListener(l -> crearEditarPersona());
        vista.getBtCancelar().addActionListener(l -> vista.getJdialog().dispose());
        vista.getBteliminar().addActionListener(l -> eliminarPersona());
        vista.getBtImprimir().addActionListener(l->imprimirReporte());
        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                busqueda(vista.getTxtBusqueda().getText());
            }
        };
        vista.getTxtBusqueda().addKeyListener(kl);
        vista.getBtExaminar().addActionListener(l -> examinarFoto());
    }

    private void cargaDatos() {
        vista.getTable().setDefaultRenderer(Object.class, new ImagenEnTabla());
        vista.getTable().setRowHeight(50);

        DefaultTableModel estucturaTabla;
        estucturaTabla = (DefaultTableModel) vista.getTable().getModel();
        estucturaTabla.setNumRows(0);

        List<Persona> listap = modelo.getPersona();

        Holder<Integer> i = new Holder<>(0);
        listap.stream().forEach(pe -> {
            estucturaTabla.addRow(new Object[9]);
            vista.getTable().setValueAt(pe.getIdpersona(), i.value, 0);
            vista.getTable().setValueAt(pe.getNombres(), i.value, 1);
            vista.getTable().setValueAt(pe.getApellidos(), i.value, 2);
            vista.getTable().setValueAt(pe.getFechanacimiento(), i.value, 3);
            vista.getTable().setValueAt(pe.getTelefono(), i.value, 4);
            vista.getTable().setValueAt(pe.getSexo(), i.value, 5);
            vista.getTable().setValueAt(pe.getSueldo(), i.value, 6);
            vista.getTable().setValueAt(pe.getCupo(), i.value, 7);

            Image foto = pe.getFoto();
            if (foto != null) {
                foto = foto.getScaledInstance(50, 75, Image.SCALE_SMOOTH);
                ImageIcon icono = new ImageIcon(foto);

                DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
                dtcr.setIcon(icono);
                vista.getTable().setValueAt(new JLabel(icono), i.value, 8);
            } else {
                vista.getTable().setValueAt(null, i.value, 8);
            }
            i.value++;
        });

    }

    private void abrirdialog(int opcion) {
        String titulo;
        if (opcion == 1) {
            titulo = "Crear persona";
            vista.getJdialog().setName("C");
            vista.getTxtID().setEnabled(true);
            vista.getJdialog().setVisible(true);
            vista.getJdialog().setSize(791, 600);
            vista.getJdialog().setLocationRelativeTo(vista);
            vista.getJdialog().setTitle(titulo);
            vista.getJlTema().setText(titulo);
        } else {
            titulo = "Editar persona";
            vista.getJdialog().setName("E");
            vista.getTxtID().setEnabled(false);
            try {
                int fila = vista.getTable().getSelectedRow();

                if (fila >= 0) {
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                    vista.getTxtID().setText(vista.getTable().getValueAt(fila, 0).toString());
                    vista.getTxtNombre().setText(vista.getTable().getValueAt(fila, 1).toString());
                    vista.getTxtApellido().setText(vista.getTable().getValueAt(fila, 2).toString());
                    vista.getDate().setDate(formato.parse(vista.getTable().getValueAt(fila, 3).toString()));
                    vista.getTxtTelefono().setText(vista.getTable().getValueAt(fila, 4).toString());
                    if (vista.getTable().getValueAt(fila, 5).toString().contentEquals("Masculino")) {
                        vista.getBtMasculino().setSelected(true);
                    } else {
                        vista.getBtFemenino().setSelected(true);
                    }
                    vista.getTxtSueldo().setText(vista.getTable().getValueAt(fila, 6).toString());
                    vista.getTxtCupo().setText(vista.getTable().getValueAt(fila, 7).toString());
                    

                    vista.getJdialog().setVisible(true);
                    vista.getJdialog().setSize(791, 600);
                    vista.getJdialog().setLocationRelativeTo(vista);
                    vista.getJdialog().setTitle(titulo);
                    vista.getJlTema().setText(titulo);
                } else {
                    JOptionPane.showMessageDialog(null, "Fila no seleccionada");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.toString());
            }
        }
    }

    public void crearEditarPersona() {
        if (vista.getJdialog().getName().contentEquals("C")) {
            //INSERT
            String id = vista.getTxtID().getText();
            String nombre = vista.getTxtNombre().getText();
            String apellido = vista.getTxtApellido().getText();
            Date fecha_nacimiento = vista.getDate().getDate();
            long d = fecha_nacimiento.getTime();
            java.sql.Date fch = new java.sql.Date(d);
            String telefono = vista.getTxtTelefono().getText();
            double sueldo = Double.parseDouble(vista.getTxtSueldo().getText());
            int cupo = Integer.parseInt(vista.getTxtCupo().getText());

            ModelPersona persona = new ModelPersona();
            persona.setIdpersona(id);
            persona.setNombres(nombre);
            persona.setApellidos(apellido);
            persona.setFechanacimiento(fch);
            persona.setTelefono(telefono);
            if (vista.getBtMasculino().isSelected()) {
                persona.setSexo("Masculino");
            } else {
                persona.setSexo("Femenino");
            }
            persona.setSueldo(sueldo);
            persona.setCupo(cupo);

            try {
                FileInputStream img = new FileInputStream(jfc.getSelectedFile());
                int largo = (int) jfc.getSelectedFile().length();
                persona.setImageFile(img);
                persona.setLength(largo);

            } catch (IOException ex) {
                Logger.getLogger(ControlerPersona.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (persona.setPersonaFoto()) {
                JOptionPane.showMessageDialog(vista, "Persona creada!!");
                vaciarCampos();
                cargaDatos();
            } else {
                JOptionPane.showMessageDialog(vista, "Fallo en crear la persona");
            }
        } else {
            //UPTADE
            String id = vista.getTxtID().getText();
            String nombre = vista.getTxtNombre().getText();
            String apellido = vista.getTxtApellido().getText();
            Date fecha_nacimiento = vista.getDate().getDate();
            String telefono = vista.getTxtTelefono().getText();
            double sueldo = Double.parseDouble(vista.getTxtSueldo().getText());
            int cupo = Integer.parseInt(vista.getTxtCupo().getText());

            ModelPersona persona = new ModelPersona();
            persona.setIdpersona(id);
            persona.setNombres(nombre);
            persona.setApellidos(apellido);
            persona.setFechanacimiento(fecha_nacimiento);
            persona.setTelefono(telefono);
            
            if (vista.getBtMasculino().isSelected()) {
                persona.setSexo("Masculino");
            } else {
                persona.setSexo("Femenino");
            }
            persona.setSueldo(sueldo);
            persona.setCupo(cupo);
            
            if (persona.uptadePersona()) {
                JOptionPane.showMessageDialog(vista, "Persona modificada!!");
                cargaDatos();
            } else {
                JOptionPane.showMessageDialog(vista, "Fallo en la modificacion de la persona");
            }
        }
    }

    public void eliminarPersona() {
        try {
            int fila = vista.getTable().getSelectedRow();

            if (fila >= 0) {    
                String id = vista.getTable().getValueAt(fila, 0).toString();

                ModelPersona persona = new ModelPersona();
                persona.setIdpersona(id);

                if (vista.getTable().getRowSelectionAllowed()) {

                    int a = JOptionPane.showConfirmDialog(null, "Deseas eliminar a esta persona");
                    if (a == 0) {
                        persona.deletePersona();
                        JOptionPane.showMessageDialog(vista, "Se eliminado a esta persona");
                        cargaDatos();
                    } else {
                        JOptionPane.showMessageDialog(vista, "Operacion Cancelada");
                    }
                }

            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }

    public void busqueda(String buscador) {

        String id = vista.getTxtBusqueda().getText();
        String nombre = vista.getTxtBusqueda().getText();
        String apellido = vista.getTxtBusqueda().getText();

        ModelPersona persona = new ModelPersona();

        persona.setIdpersona(id);
        persona.setNombres(nombre);
        persona.setApellidos(apellido);
        persona.buscarPersona();

        List<Persona> listap = modelo.getPersona();

        Stream<Persona> person = listap.stream().filter(p -> {
            return p.getIdpersona().equalsIgnoreCase(id) || p.getNombres().equalsIgnoreCase(nombre) || p.getApellidos().equalsIgnoreCase(apellido);
        });

        listap = (ArrayList<Persona>) person.collect(Collectors.toList());
        llenar(listap);

    }

    private void llenar(List<Persona> lista) {
        DefaultTableModel estucturaTabla;
        estucturaTabla = (DefaultTableModel) vista.getTable().getModel();
        estucturaTabla.setRowCount(0);

        lista.stream().forEach(p1 -> {
            String[] persona1 = {p1.getIdpersona(), p1.getNombres(), p1.getApellidos(), String.valueOf(p1.getFechanacimiento()), p1.getTelefono(), p1.getSexo(), String.valueOf(p1.getSueldo()), String.valueOf(p1.getCupo())};
            estucturaTabla.addRow(persona1);
        });
    }

    public void vaciarCampos() {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        vista.getTxtID().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtApellido().setText("");
        vista.getDate().setCalendar(null);
        vista.getTxtTelefono().setText("");
        vista.getButtonGroup2().clearSelection();
        vista.getTxtSueldo().setText("");
        vista.getTxtCupo().setText("");
        vista.getJlFoto().setIcon(null);
    }

    public boolean validarDatos() {
        boolean valido = true;
        if (!vista.getTxtID().getText().matches("[0-9]{10}")) {
            JOptionPane.showMessageDialog(null, "ID INCORECTO");
            valido = false;
        }
        if (!vista.getTxtNombre().getText().matches("[A-Za-z]{1,}")) {
            JOptionPane.showMessageDialog(null, "VALOR INCORECTA");
            valido = false;
        }
        if (!vista.getTxtApellido().getText().matches("[A-Za-z]{1,}")) {
            JOptionPane.showMessageDialog(null, "VALOR INCORECTA");
            valido = false;
        }
        if (!vista.getDate().getDate().equals(null)) {
            JOptionPane.showMessageDialog(null, "VALOR INCORECTA");
            valido = false;
        }
        if (!vista.getTxtTelefono().getText().matches("[0-9]{10}")) {
            JOptionPane.showMessageDialog(null, "VALOR INCORECTA");
            valido = false;
        }
        if ((vista.getBtFemenino().isSelected() == false) || (vista.getBtMasculino().isSelected() == false)) {
            JOptionPane.showMessageDialog(null, "SELECCIONE UNA MARCA");
            valido = false;
        }

        return valido;
    }

    public void examinarFoto() {
        jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int estado = jfc.showOpenDialog(vista);
        if (estado == JFileChooser.APPROVE_OPTION) {
            try {
                Image image = ImageIO.read(jfc.getSelectedFile()).getScaledInstance(
                        vista.getJlFoto().getWidth(), vista.getJlFoto().getHeight(), Image.SCALE_DEFAULT);
                Icon icono = new ImageIcon(image);
                vista.getJlFoto().setIcon(icono);
                vista.getJlFoto().updateUI();

            } catch (IOException ex) {
                Logger.getLogger(ControlerPersona.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void imprimirReporte(){
        try {
            ModelPGConection con=new ModelPGConection();
            
            JasperReport jr=(JasperReport)JRLoader.loadObject(getClass().getResource("/view/reportes/Reporte_Personas.jasper"));
            JasperPrint jp= JasperFillManager.fillReport(jr,null,con.getConex());
            JasperViewer jv = new JasperViewer(jp,false);
            jv.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(ControlerPersona.class.getName()).log(Level.SEVERE, null, ex);
        }                    
    }
    }
