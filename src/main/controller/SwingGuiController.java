package main.controller;

import main.gui.*;
import main.helpers.ValidationUtils;
import main.models.*;
import main.services.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat; 

public class SwingGuiController {
    private MainFrame mainFrame;
    private PacienteService pacienteService;
    private EnfermedadService enfermedadService;
    private EstadisticasService estadisticasService;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");


    public SwingGuiController(MainFrame mainFrame, PacienteService pacienteService,
                              EnfermedadService enfermedadService, EstadisticasService estadisticasService) {
        this.mainFrame = mainFrame;
        this.pacienteService = pacienteService;
        this.enfermedadService = enfermedadService;
        this.estadisticasService = estadisticasService;

        attachListeners();
        loadInitialData();
    }

    private void attachListeners() {
        PacientePanel pp = mainFrame.getPacientePanel();
        pp.getCrearButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearPaciente();
            }
        });
        pp.getActualizarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					actualizarPaciente();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
        });
        pp.getEliminarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarPaciente();
            }
        });
        pp.getLimpiarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCamposPaciente();
            }
        });
        pp.getPacienteTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    cargarPacienteSeleccionado();
                }
            }
        });


        EnfermedadPanel ep = mainFrame.getEnfermedadPanel();
        ep.getCrearButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					crearEnfermedad();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
         ep.getActualizarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarEnfermedad();
            }
        });

        ep.getLimpiarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCamposEnfermedad();
            }
        });
        ep.getEnfermedadTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    cargarEnfermedadSeleccionada();
                }
            }
        });

        EstadisticasPanel esp = mainFrame.getEstadisticasPanel();
        esp.getMostrarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarEstadisticaSeleccionada();
            }
        });
        
        CargaDatosPanel cdp = mainFrame.getCargaDatosPanel();
        cdp.getCargarDatosButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarDatosDePrueba();
            }
        });
    }

    private void loadInitialData() {
        refreshPacientesTable();
        refreshEnfermedadesTable();
        populateEnfermedadComboBox();
    }
    
    private void populateEnfermedadComboBox() {
        PacientePanel pp = mainFrame.getPacientePanel();
        pp.getEnfermedadComboBox().removeAllItems();
        pp.getEnfermedadComboBox().addItem("Ninguna"); 
        List<Enfermedad> enfermedades = enfermedadService.getAll();
        for (Enfermedad enf : enfermedades) {
            pp.getEnfermedadComboBox().addItem(enf.obtenerCodigo() + " - " + enf.getNombreComun());
        }
    }

   private void crearPaciente() {
    try {
        PacientePanel pp = mainFrame.getPacientePanel();
        
        String nombre = pp.getNombreField().getText().trim();
        String apellido = pp.getApellidoField().getText().trim();
        String documento = pp.getDocumentoField().getText().trim();
        String edadStr = pp.getEdadField().getText().trim();
        String pais = pp.getPaisField().getText().trim();
        String fechaContagioStr = pp.getFechaContagioField().getText().trim();
        Date fechaContagio = validarFecha(fechaContagioStr);

        
        
        ValidationUtils.validarNombre(nombre);
        ValidationUtils.validarApellido(apellido);
        ValidationUtils.validarDocumento(documento);
        int edad = ValidationUtils.validarEdad(edadStr);
        ValidationUtils.validarCampoObligatorio(pais, "Pais");

        PersonaEnferma nuevoPaciente = new PersonaEnferma(nombre, apellido, documento);
        nuevoPaciente.setEdad(edad);
        nuevoPaciente.setSexo((String) pp.getSexoComboBox().getSelectedItem());
        nuevoPaciente.setPais(pais);
        nuevoPaciente.setEnfermo(pp.getEnfermoCheckBox().isSelected());
        nuevoPaciente.setContagioExterior(pp.getContagioExteriorCheckBox().isSelected());
        nuevoPaciente.setFechaContagio(fechaContagio);
        
        if (nuevoPaciente.isEnfermo()) {
            String contactosStr = pp.getContactosArea().getText().trim();
            List<PersonaContacto> contactos = parseContactos(contactosStr);
            nuevoPaciente.setContactos(contactos);
        }

        if (nuevoPaciente.isContagioExterior()) {
            String paisesVisitadosStr = pp.getPaisesVisitadosArea().getText().trim();
            List<VisitaPais> paisesVisitados = parsePaisesVisitados(paisesVisitadosStr);
            nuevoPaciente.setPaisesVisitados(paisesVisitados);
        }

        String enfermedadSeleccionada = (String) pp.getEnfermedadComboBox().getSelectedItem();
        if (enfermedadSeleccionada != null && !enfermedadSeleccionada.isEmpty() && 
            !enfermedadSeleccionada.equals("Ninguna") && !enfermedadSeleccionada.equals("Seleccione...")) {
            String enfermedadCodigo = enfermedadSeleccionada.split(" - ")[0];
            Enfermedad enfermedad = enfermedadService.getByCodigo(enfermedadCodigo);
            if (enfermedad != null) {
                nuevoPaciente.setEnfermedad(enfermedad);
            }
        }
        
        pacienteService.registrarPaciente(nuevoPaciente);
        
        refreshPacientesTable();
        populateEnfermedadComboBox();
        limpiarCamposPaciente();
        
        ValidationUtils.mostrarExito("Paciente registrado exitosamente.", mainFrame);
        
    } catch (Exception e) {
        ValidationUtils.mostrarError(e.getMessage(), mainFrame);
   }
}
    
    private void actualizarPaciente() throws Exception {
        PacientePanel pp = mainFrame.getPacientePanel();
        String codigo = pp.getCodigoField().getText();
        String mensaje = "";
        boolean actualizado = false;
        
        if (codigo.isEmpty()) {
            mensaje = "Ingrese el codigo del paciente a actualizar.";
        } else {
            Persona pacienteExistente = pacienteService.getByCodigo(codigo);
            if (pacienteExistente == null || !(pacienteExistente instanceof PersonaEnferma)) {
                mensaje = "Paciente no encontrado o tipo incorrecto.";
            } else {
                PersonaEnferma paciente = (PersonaEnferma) pacienteExistente;
                try {
                    String nombre = pp.getNombreField().getText().trim();
                    String apellido = pp.getApellidoField().getText().trim();
                    String documento = pp.getDocumentoField().getText().trim();
                    String edadStr = pp.getEdadField().getText().trim();
                    String pais = pp.getPaisField().getText().trim();
                    String fechaContagioStr = pp.getFechaContagioField().getText().trim();
                    Date fechaContagio = validarFecha(fechaContagioStr);

                    ValidationUtils.validarNombre(nombre);
                    ValidationUtils.validarApellido(apellido);
                    ValidationUtils.validarDocumento(documento);
                    int edad = ValidationUtils.validarEdad(edadStr);
                    ValidationUtils.validarCampoObligatorio(pais, "Pais");

                    paciente.setNombre(nombre);
                    paciente.setApellidos(apellido);
                    paciente.setCarnetIdentidad(documento);
                    paciente.setEdad(edad);
                    paciente.setPais(pais);
                    paciente.setContagioExterior(pp.getContagioExteriorCheckBox().isSelected());
                    paciente.setEnfermo(pp.getEnfermoCheckBox().isSelected());
                    paciente.setFechaContagio(fechaContagio);

                    if (paciente.isContagioExterior()) {
                        String paisesVisitadosStr = pp.getPaisesVisitadosArea().getText().trim();
                        List<VisitaPais> paisesVisitados = parsePaisesVisitados(paisesVisitadosStr);
                        paciente.setPaisesVisitados(paisesVisitados);
                    } else {
                        paciente.limpiarPaisesVisitados();
                    }

                    String selectedEnfermedadItem = (String) pp.getEnfermedadComboBox().getSelectedItem();
                    if (selectedEnfermedadItem != null && !selectedEnfermedadItem.equals("Ninguna")) {
                        String enfermedadCodigo = selectedEnfermedadItem.split(" - ")[0];
                        Enfermedad enfermedadAsignada = enfermedadService.getByCodigo(enfermedadCodigo);
                        paciente.setEnfermedad(enfermedadAsignada);
                    } else {
                        paciente.setEnfermedad(null);
                    }

                    pacienteService.updatePaciente(paciente);
                    mensaje = "Paciente actualizado exitosamente.";
                    actualizado = true;
                    
                } catch (NumberFormatException ex) {
                    mensaje = "Error en formato de número (Edad).";
                } catch (IllegalArgumentException ex) {
                    mensaje = "Error: " + ex.getMessage();
                } catch (Exception e) {
                    mensaje = "Error: " + e.getMessage();
                }
            }
        }
        
        if (actualizado) {
            JOptionPane.showMessageDialog(mainFrame, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            refreshPacientesTable();
            limpiarCamposPaciente();
        } else {
            JOptionPane.showMessageDialog(mainFrame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPaciente() {
        PacientePanel pp = mainFrame.getPacientePanel();
        String codigo = pp.getCodigoField().getText();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Seleccione o ingrese el codigo del paciente a eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(mainFrame, "¿Esta seguro de que desea eliminar este paciente?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                pacienteService.deletePaciente(codigo);
                JOptionPane.showMessageDialog(mainFrame, "Paciente eliminado exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                refreshPacientesTable();
                limpiarCamposPaciente();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cargarPacienteSeleccionado() {
        PacientePanel pp = mainFrame.getPacientePanel();
        int selectedRow = pp.getPacienteTable().getSelectedRow();
        if (selectedRow >= 0) {
            String codigo = (String) pp.getTableModel().getValueAt(selectedRow, 0);
            Persona persona = pacienteService.getByCodigo(codigo);
            if (persona instanceof PersonaEnferma) {
                PersonaEnferma paciente = (PersonaEnferma) persona;
                pp.getCodigoField().setText(paciente.obtenerCodigo());
                pp.getNombreField().setText(paciente.getNombre());
                pp.getApellidoField().setText(paciente.getApellidos());
                pp.getDocumentoField().setText(paciente.getCarnetIdentidad());
                pp.getEdadField().setText(String.valueOf(paciente.getEdad()));
                pp.getSexoComboBox().setSelectedItem(paciente.getSexo());
                pp.getPaisField().setText(paciente.getPais());
                pp.getContagioExteriorCheckBox().setSelected(paciente.isContagioExterior());
                pp.getEnfermoCheckBox().setSelected(paciente.isEnfermo());

             if (paciente.getFechaContagio() != null) {
                    pp.getFechaContagioField().setText(dateFormat.format(paciente.getFechaContagio()));
                } else {
                    pp.getFechaContagioField().setText("");
                }

                boolean esEnfermo = paciente.isEnfermo();
                pp.getContactosLabel().setVisible(esEnfermo);
        pp.getContactosScrollPane().setVisible(esEnfermo);

        if (esEnfermo && paciente.getContactos() != null) {
            pp.getContactosArea().setText(formatContactos(paciente.getContactos()));
        } else {
            pp.getContactosArea().setText("");
        }


                boolean esContagioExterior = paciente.isContagioExterior();
                pp.getPaisesVisitadosLabel().setVisible(esContagioExterior);
                pp.getPaisesVisitadosScrollPane().setVisible(esContagioExterior);

                if (esContagioExterior && paciente.getPaisesVisitados() != null) {
                    pp.getPaisesVisitadosArea().setText(formatPaisesVisitados(paciente.getPaisesVisitados()));
                } else {
                    pp.getPaisesVisitadosArea().setText("");
                }

                if (paciente.getEnfermedad() != null) {
                    String enfermedadItem = paciente.getEnfermedad().obtenerCodigo() + " - " + paciente.getEnfermedad().getNombreComun();
                    pp.getEnfermedadComboBox().setSelectedItem(enfermedadItem);
                } else {
                    pp.getEnfermedadComboBox().setSelectedItem("Ninguna");
                }
            }
        }
    }


    private void limpiarCamposPaciente() {
        PacientePanel pp = mainFrame.getPacientePanel();
        pp.getCodigoField().setText("");
        pp.getNombreField().setText("");
        pp.getApellidoField().setText("");
        pp.getDocumentoField().setText("");
        pp.getEdadField().setText("");
        pp.getSexoComboBox().setSelectedIndex(0);
        pp.getPaisField().setText("");
        pp.getContagioExteriorCheckBox().setSelected(false);
        pp.getEnfermoCheckBox().setSelected(false);
        pp.getEnfermedadComboBox().setSelectedItem("Ninguna");
        pp.getPaisesVisitadosArea().setText("");
        pp.getPaisesVisitadosLabel().setVisible(false);
        pp.getPaisesVisitadosScrollPane().setVisible(false);
        pp.getPacienteTable().clearSelection();
    }

    private void refreshPacientesTable() {
        PacientePanel pp = mainFrame.getPacientePanel();
        DefaultTableModel model = pp.getTableModel();
        model.setRowCount(0);

        List<Persona> pacientes = pacienteService.getAll();
        for (Persona p : pacientes) {

            if (p instanceof PersonaEnferma) {
                PersonaEnferma pe = (PersonaEnferma) p;
                model.addRow(new Object[]{
                        pe.obtenerCodigo(),
                        pe.getNombre(),
                        pe.getApellidos(),
                        pe.getCarnetIdentidad(),
                        pe.getEdad(),
                        pe.getSexo(),
                        pe.getPais(),
                        pe.getFechaContagio() != null ? dateFormat.format(pe.getFechaContagio()) : "N/A",
                        pe.isEnfermo() ? "Si" : "No",
                        pe.isContagioExterior() ? "Si" : "No",
                        pe.getEnfermedad() != null ? pe.getEnfermedad().getNombreComun() : "N/A"
                });
            } else { 
                 model.addRow(new Object[]{
                        p.obtenerCodigo(),
                        p.getNombre(),
                        p.getApellidos(),
                        p.getCarnetIdentidad(),
                        p.getEdad(),
                        p.getSexo(),
                        "N/A", "N/A", "N/A", "N/A", "N/A" 
                });
            }
        }
    }

    // --- Enfermedad Methods ---
    private void crearEnfermedad() {
        EnfermedadPanel ep = mainFrame.getEnfermedadPanel();
        try {
            String nombreComun = ep.getNombreComunField().getText();
            String viaTransmision = ep.getViaTransmisionField().getText();
            String nombreCientifico = ep.getNombreCientificoField().getText();
            String muertosStr = ep.getMuertosField().getText();
            String curadosStr = ep.getCuradosField().getText();
            String enfermosActivosStr = ep.getEnfermosActivosField().getText();

            
            ValidationUtils.validarNombre(nombreComun);
            ValidationUtils.validarNombre(nombreCientifico);
            int cantMuertos = ValidationUtils.validarNumeroEntero(muertosStr, "Muertos");
            int cantCurados = ValidationUtils.validarNumeroEntero(curadosStr, "Curados");
            int cantEnfermos = ValidationUtils.validarNumeroEntero(enfermosActivosStr, "Enfermos Activos");
            

            Enfermedad enfermedad = new Enfermedad(nombreComun, nombreCientifico, viaTransmision, cantCurados, cantMuertos, cantEnfermos);


            enfermedadService.registrarEnfermedad(enfermedad);
            JOptionPane.showMessageDialog(mainFrame, "Enfermedad creada exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            refreshEnfermedadesTable();
            populateEnfermedadComboBox(); 
            limpiarCamposEnfermedad();
        } catch (Exception e) {
            ValidationUtils.mostrarError(e.getMessage(), mainFrame);
        }
    }
    
    private void actualizarEnfermedad() {
        EnfermedadPanel ep = mainFrame.getEnfermedadPanel();
        String codigo = ep.getCodigoField().getText();
        String mensaje = "";
        boolean actualizada = false;
        
        if (codigo.isEmpty()) {
            mensaje = "Ingrese el codigo de la enfermedad a actualizar.";
        } else {
            Enfermedad enfermedad = enfermedadService.getByCodigo(codigo);
            if (enfermedad == null) {
                mensaje = "Enfermedad no encontrada.";
            } else {
                try {
                    ValidationUtils.validarNombre(ep.getNombreComunField().getText());
                    ValidationUtils.validarNombre(ep.getNombreCientificoField().getText());

                    int cantMuertos = ValidationUtils.validarNumeroEntero(ep.getMuertosField().getText(), "Muertos");
                    int cantCurados = ValidationUtils.validarNumeroEntero(ep.getCuradosField().getText(), "Curados");
                    int cantEnfermos = ValidationUtils.validarNumeroEntero(ep.getEnfermosActivosField().getText(), "Enfermos Activos");

                    enfermedad.setNombreComun(ep.getNombreComunField().getText());
                    enfermedad.setViaTransmision(ep.getViaTransmisionField().getText());
                    enfermedad.setNombreCientifico(ep.getNombreCientificoField().getText());
                    enfermedad.setCurados(cantCurados);
                    enfermedad.setMuertos(cantMuertos);
                    enfermedad.setEnfermosActivos(cantEnfermos);
                    
                    enfermedadService.actualizarEnfermedad(enfermedad);
                    mensaje = "Enfermedad actualizada exitosamente.";
                    actualizada = true;
                    
                } catch (NumberFormatException ex) {
                    mensaje = "Error en formato de numero.";
                } catch (IllegalArgumentException ex) {
                    mensaje = "Error: " + ex.getMessage();
                } catch (Exception e) {
                    mensaje = "Error: " + e.getMessage();
                }
            }
        }
        
        if (actualizada) {
            JOptionPane.showMessageDialog(mainFrame, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            refreshEnfermedadesTable();
            populateEnfermedadComboBox();
            limpiarCamposEnfermedad();
        } else {
            JOptionPane.showMessageDialog(mainFrame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void cargarEnfermedadSeleccionada() {
        EnfermedadPanel ep = mainFrame.getEnfermedadPanel();
        int selectedRow = ep.getEnfermedadTable().getSelectedRow();
        if (selectedRow >= 0) {
            String codigo = (String) ep.getTableModel().getValueAt(selectedRow, 0);
            Enfermedad enfermedad = enfermedadService.getByCodigo(codigo);
            if (enfermedad != null) {
                ep.getCodigoField().setText(enfermedad.obtenerCodigo());
                ep.getNombreComunField().setText(enfermedad.getNombreComun());
                ep.getViaTransmisionField().setText(enfermedad.getViaTransmision());
                ep.getNombreCientificoField().setText(enfermedad.getNombreCientifico());
                ep.getCuradosField().setText(String.valueOf(enfermedad.getCurados()));
                ep.getMuertosField().setText(String.valueOf(enfermedad.getMuertos()));
                ep.getEnfermosActivosField().setText(String.valueOf(enfermedad.getEnfermosActivos()));
            }
        }
    }

    private void limpiarCamposEnfermedad() {
        EnfermedadPanel ep = mainFrame.getEnfermedadPanel();
        ep.getCodigoField().setText("");
        ep.getNombreComunField().setText("");
        ep.getViaTransmisionField().setText("");
        ep.getNombreCientificoField().setText("");
        ep.getCuradosField().setText("");
        ep.getMuertosField().setText("");
        ep.getEnfermosActivosField().setText("");
        ep.getEnfermedadTable().clearSelection();
    }

    private void refreshEnfermedadesTable() {
        EnfermedadPanel ep = mainFrame.getEnfermedadPanel();
        DefaultTableModel model = ep.getTableModel();
        model.setRowCount(0); 
        List<Enfermedad> enfermedades = enfermedadService.getAll();
        for (Enfermedad enf : enfermedades) {
            model.addRow(new Object[]{
                    enf.obtenerCodigo(),
                    enf.getNombreComun(),
                    enf.getViaTransmision(),
                    enf.getNombreCientifico(),
                    enf.getCurados(),
                    enf.getMuertos(),
                    enf.getEnfermosActivos()
            });
        }
    }
    

    private void cargarDatosDePrueba() {
        try {


            Enfermedad covid = new Enfermedad("COVID-19", "Respiratoria", "SARS-CoV-2");
            enfermedadService.registrarEnfermedad(covid);
            Enfermedad gripe = new Enfermedad("Gripe Comun", "Respiratoria", "Influenza");
            enfermedadService.registrarEnfermedad(gripe);
            Enfermedad malaria = new Enfermedad("Malaria", "Vector", "Plasmodium");
            enfermedadService.registrarEnfermedad(malaria);

            // --- Cargar pacientes de prueba ---
            // Paciente 1: Cubano, contagio local (no en el exterior)
            PersonaEnferma p1 = new PersonaEnferma("Carlos", "Diaz", "85010112345");
            p1.setEdad(38);
            p1.setSexo("M");
            p1.setPais("Cuba");
            p1.setEnfermo(true);
            p1.setContagioExterior(false);
            p1.setEnfermedad(gripe);
            p1.setFechaContagio(dateFormat.parse("15-05-2025"));
            pacienteService.registrarPaciente(p1);

            // Paciente 2: Cubana, contagiada en el exterior
            PersonaEnferma p2 = new PersonaEnferma("Ana", "Garcia", "90031554321");
            p2.setEdad(33);
            p2.setSexo("F");
            p2.setPais("Cuba");
            p2.setEnfermo(true);
            p2.setContagioExterior(true); 
            p2.setFechaContagio(dateFormat.parse("01-06-2025"));
            List<VisitaPais> visitasAna = new ArrayList<VisitaPais>();
            visitasAna.add(new VisitaPais("Espana", 15));
            visitasAna.add(new VisitaPais("Francia", 5));
            p2.setPaisesVisitados(visitasAna);
            p2.setEnfermedad(covid);
            pacienteService.registrarPaciente(p2);
            
            // Paciente 3: Extranjero, contagiado (siempre es en el exterior)
            PersonaEnferma p3 = new PersonaEnferma("John", "Smith", "99999999999");
            p3.setEdad(50);
            p3.setSexo("M");
            p3.setPais("USA");
            p3.setEnfermo(true);
            p3.setContagioExterior(true); 
            p3.setFechaContagio(dateFormat.parse("20-04-2025"));
            List<VisitaPais> visitasJohn = new ArrayList<VisitaPais>();
            visitasJohn.add(new VisitaPais("Mexico", 10));
            p3.setPaisesVisitados(visitasJohn);
            p3.setEnfermedad(malaria);
            pacienteService.registrarPaciente(p3);
            
            // Paciente 4: Cubana, sana
            PersonaEnferma p4 = new PersonaEnferma("Laura", "Fernandez", "78122067890");
            p4.setEdad(45);
            p4.setSexo("F");
            p4.setPais("Cuba");
            p4.setEnfermo(false);
            p4.setContagioExterior(false);
            pacienteService.registrarPaciente(p4);

            loadInitialData();
            JOptionPane.showMessageDialog(mainFrame, "Datos de prueba cargados exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, "Error al cargar datos de prueba: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarEstadisticaSeleccionada() {
        EstadisticasPanel panel = mainFrame.getEstadisticasPanel();
        String seleccion = (String) panel.getEstadisticaComboBox().getSelectedItem();
        JTextArea area = panel.getResultadoArea();
        area.setText(""); 
        String mensaje = "";

        if (seleccion == null) {
            mensaje = "Por favor, seleccione una estadistica.";
        } else {
            try {
                if (seleccion.equals("Estadisticas Generales")) {
                    mostrarEstadisticasGenerales(area);
                } else if (seleccion.equals("Pacientes por Sexo")) {
                    mostrarPacientesPorSexo(area);
                } else if (seleccion.equals("Contagios Nacionales vs. Extranjero")) {
                    mostrarContagiosPorOrigen(area);
                } else if (seleccion.equals("Enfermedades por Tipo de Transmision")) {
                    mostrarEnfermedadesPorTipo(area);
                } else if (seleccion.equals("Casos por Rango de Edad y Sexo")) {
                    mostrarCasosPorEdadYSexo(area);
                } else if (seleccion.equals("Estadisticas por Enfermedad")) {
                    mostrarEstadisticasPorEnfermedad(area);
                } else if (seleccion.equals("Contactos de Enfermos en el Exterior")) {
                    mostrarContactosExterior(area);
                } else if (seleccion.equals("Todas las Estadisticas")) {
                    mostrarTodasLasEstadisticas(area);
                } else if (seleccion.equals("Enfermos por Pais y Mes (requiere input)")) {
                    mostrarEnfermosPorPaisYMes(area);
                } else {
                    mensaje = "Estadistica '" + seleccion + "' no implementada.";
                }
            } catch (Exception e) {
                mensaje = "Error al generar la estadistica: " + e.getMessage();
            }
        }
        
        if (!mensaje.isEmpty()) {
            area.setText(mensaje);
        }
    }

    

    private void mostrarEstadisticasGenerales(JTextArea area) {
        area.append("--- Estadisticas Generales ---\n");
        area.append("Total de Pacientes Registrados: " + estadisticasService.getTotalPacientes() + "\n");
        area.append("Total de Enfermedades Registradas: " + estadisticasService.getTotalEnfermedades() + "\n");
        area.append("Pacientes Actualmente Enfermos: " + estadisticasService.getPacientesEnfermos() + "\n");
    }

    private void mostrarPacientesPorSexo(JTextArea area) {
        area.append("--- Pacientes por Sexo ---\n");
        Map<String, Integer> porSexo = estadisticasService.getPacientesPorSexo();
        for (Map.Entry<String, Integer> entry : porSexo.entrySet()) {
            area.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
    }

    private void mostrarContagiosPorOrigen(JTextArea area) {
        area.append("--- Contagios por Origen ---\n");
        Map<String, Integer> porOrigen = estadisticasService.getPacientesPorOrigen();
        for (Map.Entry<String, Integer> entry : porOrigen.entrySet()) {
            area.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
    }


    private void mostrarEnfermedadesPorTipo(JTextArea area) {
        area.append("--- Enfermedades por Tipo de Transmision ---\n");
        Map<String, Integer> porTipo = estadisticasService.getEnfermedadesPorTipo();
        for (Map.Entry<String, Integer> entry : porTipo.entrySet()) {
            area.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
    }

    private void mostrarCasosPorEdadYSexo(JTextArea area) {
        area.append("--- Casos por Rango de Edad y Sexo ---\n");
        Map<String, Map<String, Integer>> porEdadYSexo = estadisticasService.getCasosPorEdadYSexo();
        for (Map.Entry<String, Map<String, Integer>> entry : porEdadYSexo.entrySet()) {
            area.append("Rango de Edad " + entry.getKey() + ":\n");
            for (Map.Entry<String, Integer> sexoEntry : entry.getValue().entrySet()) {
                area.append("  " + sexoEntry.getKey() + ": " + sexoEntry.getValue() + "\n");
            }
        }
    }

    private void mostrarEstadisticasPorEnfermedad(JTextArea area) {
        area.append("--- Estadisticas por Enfermedad ---\n");
        Map<String, Map<String, Integer>> estPorEnf = estadisticasService.getEstadisticasPorEnfermedad();
        for (Map.Entry<String, Map<String, Integer>> entry : estPorEnf.entrySet()) {
            area.append("Enfermedad: " + entry.getKey() + "\n");
            for (Map.Entry<String, Integer> detalleEntry : entry.getValue().entrySet()) {
                area.append("  " + detalleEntry.getKey() + ": " + detalleEntry.getValue() + "\n");
            }
        }
    }

    private void mostrarContactosExterior(JTextArea area) {
        area.append("--- Contactos de Enfermos en el Exterior (Vigilancia) ---\n");
        List<Persona> contactos = estadisticasService.getContactosDeEnfermosExterior();
        if (contactos.isEmpty()) {
            area.append("No se encontraron contactos en vigilancia para esta categoria.\n");
        } else {
            area.append(String.format("%-20s %-20s %-5s %-15s\n", "Nombre", "Apellidos", "Edad", "Documento"));
            area.append("------------------------------------------------------------------\n");
            for (Persona p : contactos) {
                area.append(String.format("%-20s %-20s %-5d %-15s\n",
                        p.getNombre(), p.getApellidos(), p.getEdad(), p.getCarnetIdentidad()));
            }
        }
    }
        private void mostrarEnfermosPorPaisYMes(JTextArea area) {
        String pais = JOptionPane.showInputDialog(mainFrame, "Ingrese el nombre del pais:", "Filtro por Pais", JOptionPane.QUESTION_MESSAGE);
        String mensaje = "";
        
        if (pais == null || pais.trim().isEmpty()) {
            mensaje = "Operacion cancelada. No se ingreso un pais.";
        } else {
            pais = pais.trim();
            String mesAnioStr = JOptionPane.showInputDialog(mainFrame, "Ingrese el mes y anio (MM-yyyy):", "Filtro por Fecha", JOptionPane.QUESTION_MESSAGE);
            
            if (mesAnioStr == null || mesAnioStr.trim().isEmpty()) {
                mensaje = "Operacion cancelada. No se ingreso una fecha.";
            } else {
                try {
                    String[] parts = mesAnioStr.trim().split("-");
                    if (parts.length != 2) {
                        mensaje = "Error en el formato de fecha. Por favor, use MM-yyyy.";
                    } else {
                        int mes = Integer.parseInt(parts[0]);
                        int anio = Integer.parseInt(parts[1]);

                        if (mes < 1 || mes > 12 || anio < 1900 || anio > 2100) {
                            mensaje = "Error: Mes o anio fuera de rango valido.";
                        } else {
                            Map<String, Integer> enfermosPorEnfermedad = estadisticasService.getEnfermosPorPaisYMes(pais, mes, anio);
                            StringBuilder sb = new StringBuilder();
                            sb.append("--- Casos por Enfermedad en '").append(pais).append("' durante ").append(mesAnioStr).append(" ---\n");
                            
                            if (enfermosPorEnfermedad.isEmpty()) {
                                sb.append("No se encontraron casos para los criterios seleccionados.\n");
                            } else {
                                sb.append(String.format("%-30s %-10s\n", "Enfermedad", "Casos"));
                                sb.append("----------------------------------------\n");
                                
                                int totalCasos = 0;
                                for (Map.Entry<String, Integer> entry : enfermosPorEnfermedad.entrySet()) {
                                    sb.append(String.format("%-30s %-10d\n", entry.getKey(), entry.getValue()));
                                    totalCasos += entry.getValue();
                                }
                                sb.append("\nTotal de casos: ").append(totalCasos).append("\n");
                            }
                            mensaje = sb.toString();
                        }
                    }
                } catch (NumberFormatException e) {
                    mensaje = "Error en el formato de fecha. Por favor, use MM-yyyy.";
                }
            }
        }
        area.setText(mensaje);
    }


    private void mostrarTodasLasEstadisticas(JTextArea area) {
        area.append("=== REPORTE COMPLETO DE ESTADISTICAS ===\n\n");
        mostrarEstadisticasGenerales(area);
        area.append("\n");
        mostrarPacientesPorSexo(area);
        area.append("\n");
        mostrarContagiosPorOrigen(area);
        area.append("\n");
        mostrarEnfermedadesPorTipo(area);
        area.append("\n");
        mostrarCasosPorEdadYSexo(area);
        area.append("\n");
        mostrarEstadisticasPorEnfermedad(area);
        area.append("\n");
        mostrarContactosExterior(area);
    }


private List<VisitaPais> parsePaisesVisitados(String text) throws IllegalArgumentException {
        List<VisitaPais> visitas = new ArrayList<VisitaPais>();
        
        if (text != null && !text.trim().isEmpty()) {
            String[] paisesEntries = text.split(";");
            for (String entry : paisesEntries) {
                if (!entry.trim().isEmpty()) {
                    String[] parts = entry.split(",");
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("Formato invalido para paises visitados. Use 'pais,dias;...'. Error en: " + entry);
                    }
                    String pais = parts[0].trim();
                    String diasStr = parts[1].trim();
                    if (pais.isEmpty()) {
                        throw new IllegalArgumentException("El nombre del pais no puede estar vacio.");
                    }
                    try {
                        int dias = Integer.parseInt(diasStr);
                        if (dias <= 0) {
                            throw new IllegalArgumentException("Los dias de estancia deben ser un numero positivo.");
                        }
                        visitas.add(new VisitaPais(pais, dias));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Formato de dias invalido para el pais: " + pais);
                    }
                }
            }
        }
        return visitas;
    }

    private Date validarFecha(String fechaStr) throws Exception {
        return ValidationUtils.validarYParsearFecha(fechaStr, "contagio");
    }

    private String formatPaisesVisitados(List<VisitaPais> visitas) {
        String resultado = "";
        
        if (visitas != null && !visitas.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < visitas.size(); i++) {
                VisitaPais visita = visitas.get(i);
                sb.append(visita.getPais()).append(",").append(visita.getDiasEstancia());
                if (i < visitas.size() - 1) {
                    sb.append(";");
                }
            }
            resultado = sb.toString();
        }
        return resultado;
    }

        private List<PersonaContacto> parseContactos(String text) throws IllegalArgumentException {
        List<PersonaContacto> contactos = new ArrayList<PersonaContacto>();
        
        if (text != null && !text.trim().isEmpty()) {
            String[] contactosEntries = text.split(";");
            for (String entry : contactosEntries) {
                if (!entry.trim().isEmpty()) {
                    String[] parts = entry.split(",");
                    if (parts.length != 6) {
                        throw new IllegalArgumentException("Formato invalido para contactos. Use 'nombre,apellido,ci,edad,sexo,fecha;...'. Error en: " + entry);
                    }
                    
                    String nombre = parts[0].trim();
                    String apellido = parts[1].trim();
                    String ci = parts[2].trim();
                    String edadStr = parts[3].trim();
                    String sexo = parts[4].trim();
                    String fechaStr = parts[5].trim();
                    
                    try {
                        int edad = Integer.parseInt(edadStr);
                        Date fechaContacto = ValidationUtils.validarYParsearFecha(fechaStr, "contacto");
                        PersonaContacto contacto = new PersonaContacto(nombre, apellido, ci);
                        contacto.setEdad(edad);
                        contacto.setSexo(sexo);
                        contacto.setFechaEntrevista(fechaContacto);
                        contactos.add(contacto);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Error en la edad del contacto: " + entry);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Error en la fecha del contacto: " + entry + ". " + e.getMessage());
                    }
                }
            }
        }
        return contactos;
    }

    private String formatContactos(List<PersonaContacto> contactos) {
        String resultado = "";
        
        if (contactos != null && !contactos.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < contactos.size(); i++) {
                PersonaContacto contacto = contactos.get(i);
                sb.append(contacto.getNombre()).append(",")
                  .append(contacto.getApellidos()).append(",")
                  .append(contacto.getCarnetIdentidad()).append(",")
                  .append(contacto.getEdad()).append(",")
                  .append(contacto.getSexo()).append(",")
                  .append(dateFormat.format(contacto.getFechaEntrevista()));
                if (i < contactos.size() - 1) {
                    sb.append(";");
                }
            }
            resultado = sb.toString();
        }
        return resultado;
    }
}