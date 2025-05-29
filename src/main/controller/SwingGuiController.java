package main.controller;

import main.gui.*;
import main.models.*;
import main.services.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        // Paciente Panel Listeners
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
                actualizarPaciente();
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
                crearEnfermedad();
            }
        });
         ep.getActualizarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarEnfermedad();
            }
        });
        ep.getEliminarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarEnfermedad();
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

        // Estadisticas Panel Listeners
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
        PacientePanel pp = mainFrame.getPacientePanel();
        try {
            String nombre = pp.getNombreField().getText();
            String apellido = pp.getApellidoField().getText();
            String documento = pp.getDocumentoField().getText();
            int edad = Integer.parseInt(pp.getEdadField().getText());
            String sexo = (String) pp.getSexoComboBox().getSelectedItem();
            String pais = pp.getPaisField().getText();
            boolean contagioExterior = pp.getContagioExteriorCheckBox().isSelected();
            boolean enfermo = pp.getEnfermoCheckBox().isSelected();
            
            PersonaEnferma paciente = new PersonaEnferma(nombre, apellido, documento);
            paciente.setEdad(edad);
            paciente.setSexo(sexo);
            paciente.setPais(pais);
            paciente.setContagioExterior(contagioExterior);
            paciente.setEnfermo(enfermo);

            String selectedEnfermedadItem = (String) pp.getEnfermedadComboBox().getSelectedItem();
            if (selectedEnfermedadItem != null && !selectedEnfermedadItem.equals("Ninguna")) {
                String enfermedadCodigo = selectedEnfermedadItem.split(" - ")[0];
                Enfermedad enfermedadAsignada = enfermedadService.getByCodigo(enfermedadCodigo);
                if (enfermedadAsignada != null) {
                    paciente.setEnfermedad(enfermedadAsignada);
                }
            }

            pacienteService.registrarPaciente(paciente);
            JOptionPane.showMessageDialog(mainFrame, "Paciente creado exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            refreshPacientesTable();
            limpiarCamposPaciente();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Error en formato de numero (Edad).", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarPaciente() {
        PacientePanel pp = mainFrame.getPacientePanel();
        String codigo = pp.getCodigoField().getText();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Ingrese el codigo del paciente a actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Persona pacienteExistente = pacienteService.getByCodigo(codigo);
        if (pacienteExistente == null || !(pacienteExistente instanceof PersonaEnferma)) {
            JOptionPane.showMessageDialog(mainFrame, "Paciente no encontrado o tipo incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        PersonaEnferma paciente = (PersonaEnferma) pacienteExistente;
        try {
            paciente.setNombre(pp.getNombreField().getText());
            paciente.setApellidos(pp.getApellidoField().getText());
            paciente.setCarnetIdentidad(pp.getDocumentoField().getText());
            paciente.setEdad(Integer.parseInt(pp.getEdadField().getText()));
            paciente.setSexo((String) pp.getSexoComboBox().getSelectedItem());
            paciente.setPais(pp.getPaisField().getText());
            paciente.setContagioExterior(pp.getContagioExteriorCheckBox().isSelected());
            paciente.setEnfermo(pp.getEnfermoCheckBox().isSelected());

            String selectedEnfermedadItem = (String) pp.getEnfermedadComboBox().getSelectedItem();
            if (selectedEnfermedadItem != null && !selectedEnfermedadItem.equals("Ninguna")) {
                String enfermedadCodigo = selectedEnfermedadItem.split(" - ")[0];
                Enfermedad enfermedadAsignada = enfermedadService.getByCodigo(enfermedadCodigo);
                paciente.setEnfermedad(enfermedadAsignada);
            } else {
                paciente.setEnfermedad(null);
            }

            pacienteService.updatePaciente(paciente);
            JOptionPane.showMessageDialog(mainFrame, "Paciente actualizado exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            refreshPacientesTable();
            limpiarCamposPaciente();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Error en formato de número (Edad).", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        pp.getPacienteTable().clearSelection();
    }

    private void refreshPacientesTable() {
        PacientePanel pp = mainFrame.getPacientePanel();
        DefaultTableModel model = pp.getTableModel();
        model.setRowCount(0); // Clear existing data
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
                        "N/A", "N/A", "N/A", "N/A" 
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
            
            Enfermedad enfermedad = new Enfermedad(nombreComun, viaTransmision, nombreCientifico);

            if (!ep.getCuradosField().getText().isEmpty()) enfermedad.setCurados(Integer.parseInt(ep.getCuradosField().getText()));
            if (!ep.getMuertosField().getText().isEmpty()) enfermedad.setMuertos(Integer.parseInt(ep.getMuertosField().getText()));
            if (!ep.getEnfermosActivosField().getText().isEmpty()) enfermedad.setEnfermosActivos(Integer.parseInt(ep.getEnfermosActivosField().getText()));

            enfermedadService.registrarEnfermedad(enfermedad);
            JOptionPane.showMessageDialog(mainFrame, "Enfermedad creada exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            refreshEnfermedadesTable();
            populateEnfermedadComboBox(); /
            limpiarCamposEnfermedad();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Error en formato de número (Curados, Muertos, Activos).", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarEnfermedad() {
        EnfermedadPanel ep = mainFrame.getEnfermedadPanel();
        String codigo = ep.getCodigoField().getText();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Ingrese el codigo de la enfermedad a actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Enfermedad enfermedad = enfermedadService.getByCodigo(codigo);
        if (enfermedad == null) {
            JOptionPane.showMessageDialog(mainFrame, "Enfermedad no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            enfermedad.setNombreComun(ep.getNombreComunField().getText());
            enfermedad.setViaTransmision(ep.getViaTransmisionField().getText());
            enfermedad.setNombreCientifico(ep.getNombreCientificoField().getText());
            if (!ep.getCuradosField().getText().isEmpty()) enfermedad.setCurados(Integer.parseInt(ep.getCuradosField().getText()));
            if (!ep.getMuertosField().getText().isEmpty()) enfermedad.setMuertos(Integer.parseInt(ep.getMuertosField().getText()));
            if (!ep.getEnfermosActivosField().getText().isEmpty()) enfermedad.setEnfermosActivos(Integer.parseInt(ep.getEnfermosActivosField().getText()));
            
            enfermedadService.actualizarEnfermedad(enfermedad);
            JOptionPane.showMessageDialog(mainFrame, "Enfermedad actualizada exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            refreshEnfermedadesTable();
            populateEnfermedadComboBox();
            limpiarCamposEnfermedad();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Error en formato de numero.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarEnfermedad() {
        EnfermedadPanel ep = mainFrame.getEnfermedadPanel();
        String codigo = ep.getCodigoField().getText();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Seleccione o ingrese el codigo de la enfermedad a eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(mainFrame, "¿Esta seguro de que desea eliminar esta enfermedad?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                enfermedadService.eliminarEnfermedad(codigo);
                JOptionPane.showMessageDialog(mainFrame, "Enfermedad eliminada exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                refreshEnfermedadesTable();
                populateEnfermedadComboBox();
                limpiarCamposEnfermedad();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
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
        model.setRowCount(0); // Clear existing data
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
    
    private void mostrarEstadisticaSeleccionada() {
        EstadisticasPanel esp = mainFrame.getEstadisticasPanel();
        String seleccion = (String) esp.getEstadisticaComboBox().getSelectedItem();
        JTextArea area = esp.getResultadoArea();
        area.setText(""); 

        switch (seleccion) {
            case "Estadisticas Generales":
                area.append("--- Estadisticas Generales ---\n");
                area.append("Total de Pacientes: " + estadisticasService.getTotalPacientes() + "\n");
                area.append("Total de Enfermedades: " + estadisticasService.getTotalEnfermedades() + "\n");
                break;
            case "Pacientes por Sexo":
                area.append("--- Pacientes por Sexo ---\n");
                Map<String, Integer> porSexo = estadisticasService.getPacientesPorSexo();
                for (Map.Entry<String, Integer> entry : porSexo.entrySet()) {
                    area.append(entry.getKey() + ": " + entry.getValue() + "\n");
                }
                break;
            case "Enfermedades por Tipo de Transmision":
                area.append("--- Enfermedades por Tipo de Transmision ---\n");
                Map<String, Integer> porTipo = estadisticasService.getEnfermedadesPorTipo();
                 for (Map.Entry<String, Integer> entry : porTipo.entrySet()) {
                    area.append(entry.getKey() + ": " + entry.getValue() + "\n");
                }
                break;
            case "Contagios Nacionales vs. Extranjero":
                area.append("--- Relacion entre Contagios Nacionales y Extranjeros ---\n");
                Map<String, Integer> porOrigen = estadisticasService.getPacientesPorOrigen();
                int totalNacional = porOrigen.getOrDefault("Nacional", 0);
                int totalExtranjero = porOrigen.getOrDefault("Extranjero", 0);
                int total = totalNacional + totalExtranjero;
                if (total > 0) {
                    area.append(String.format("Nacional: %d (%.1f%%)\n", totalNacional, (totalNacional * 100.0 / total)));
                    area.append(String.format("Extranjero: %d (%.1f%%)\n", totalExtranjero, (totalExtranjero * 100.0 / total)));
                } else {
                    area.append("No hay datos.\n");
                }
                break;
            case "Casos por Rango de Edad y Sexo":
                area.append("--- Casos por Rango de Edad y Sexo ---\n");
                Map<String, Map<String, Integer>> casosEdadSexo = estadisticasService.getCasosPorEdadYSexo();
                area.append(String.format("%-13s %-12s %-10s %s\n", "Rango Edad", "Masculino", "Femenino", "Total"));
                area.append("------------------------------------------------\n");
                for (Map.Entry<String, Map<String, Integer>> entry : casosEdadSexo.entrySet()) {
                    String rango = entry.getKey();
                    int masc = entry.getValue().getOrDefault("Masculino", 0);
                    int fem = entry.getValue().getOrDefault("Femenino", 0);
                    area.append(String.format("%-13s %-12d %-10d %d\n", rango, masc, fem, (masc + fem)));
                }
                break;
            case "Estadisticas por Enfermedad (curados/fallecidos/enfermos)":
                area.append("--- Estadisticas por Enfermedad ---\n");
                Map<String, Map<String, Integer>> estEnf = estadisticasService.getEstadisticasPorEnfermedad();
                area.append(String.format("%-22s %-10s %-12s %-10s %s\n", "Enfermedad", "Curados", "Fallecidos", "Enfermos", "Total"));
                area.append("-------------------------------------------------------------------\n");
                for (Map.Entry<String, Map<String, Integer>> entry : estEnf.entrySet()) {
                    String enf = entry.getKey();
                    int cur = entry.getValue().getOrDefault("Curados", 0);
                    int fall = entry.getValue().getOrDefault("Fallecidos", 0);
                    int act = entry.getValue().getOrDefault("Enfermos", 0);
                    area.append(String.format("%-22s %-10d %-12d %-10d %d\n", enf, cur, fall, act, (cur+fall+act)));
                }
                break;
            case "Contactos de Enfermos en el Exterior":
                area.append("--- Personas en Vigilancia (Contactos de Enfermos en el Exterior) ---\n");
                List<Persona> contactos = estadisticasService.getContactosDeEnfermosExterior();
                if (contactos.isEmpty()) {
                    area.append("No hay personas en vigilancia.\n");
                } else {
                    area.append(String.format("%-22s %-22s %-7s %-7s %s\n", "Nombre", "Apellido", "Edad", "Sexo", "Documento"));
                    area.append("------------------------------------------------------------------------\n");
                    for (Persona p : contactos) {
                        area.append(String.format("%-22s %-22s %-7d %-7s %s\n",
                                p.getNombre(), p.getApellidos(), p.getEdad(), p.getSexo(), p.getCarnetIdentidad()));
                    }
                }
                break;
            case "Enfermos por Pais y Mes (requiere input)":
                String pais = JOptionPane.showInputDialog(mainFrame, "Ingrese el país:");
                if (pais == null || pais.trim().isEmpty()) return;
                String mesStr = JOptionPane.showInputDialog(mainFrame, "Ingrese el mes (1-12):");
                if (mesStr == null || mesStr.trim().isEmpty()) return;
                String anioStr = JOptionPane.showInputDialog(mainFrame, "Ingrese el año (ej. 2023):");
                if (anioStr == null || anioStr.trim().isEmpty()) return;
                try {
                    int mes = Integer.parseInt(mesStr);
                    int anio = Integer.parseInt(anioStr);
                    area.append("--- Enfermos en " + pais + " durante " + mes + "/" + anio + " ---\n");
                    Map<String, Integer> enfPaisMes = estadisticasService.getEnfermosPorPaisYMes(pais, mes, anio);
                    if (enfPaisMes.isEmpty()) {
                        area.append("No se encontraron datos.\n");
                    } else {
                        for (Map.Entry<String, Integer> entry : enfPaisMes.entrySet()) {
                            area.append(entry.getKey() + ": " + entry.getValue() + " casos\n");
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Mes o año invalido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "Todas las Estadisticas":
                area.append("Mostrando todas las estadisticas (resumido)...\n");
                break;
            default:
                area.append("Seleccion no implementada aun.\n");
        }
    }

    private void cargarDatosDePrueba() {
        CargaDatosPanel cdp = mainFrame.getCargaDatosPanel();
        JTextArea log = cdp.getLogArea();
        log.setText("Cargando datos de prueba...\n");

        Enfermedad covid = new Enfermedad("COVID-19", "Respiratoria", "SARS-CoV-2");
        covid.setCurados(120); covid.setMuertos(15); covid.setEnfermosActivos(45);
        enfermedadService.registrarEnfermedad(covid);
        log.append("- Enfermedad COVID-19 registrada.\n");

        Enfermedad dengue = new Enfermedad("Dengue", "Vectorial", "DENV");
        dengue.setCurados(78); dengue.setMuertos(3); dengue.setEnfermosActivos(22);
        enfermedadService.registrarEnfermedad(dengue);
        log.append("- Enfermedad Dengue registrada.\n");
        
        Enfermedad influenza = new Enfermedad("Influenza", "Respiratoria", "H1N1");
        influenza.setCurados(230); influenza.setMuertos(8); influenza.setEnfermosActivos(65);
        enfermedadService.registrarEnfermedad(influenza);
        log.append("- Enfermedad Influenza registrada.\n");

        PersonaEnferma p1 = new PersonaEnferma("Carlos", "Rodriguez", "12345678A");
        p1.setEdad(25); p1.setSexo("M"); p1.setContagioExterior(false); p1.setEnfermo(true);
        p1.setPais("Cuba"); p1.setFechaContagio(new Date(122, 4, 15)); p1.setEnfermedad(covid);
        pacienteService.registrarPaciente(p1);
        log.append("- Paciente Carlos Rodriguez registrado.\n");

        PersonaEnferma p2 = new PersonaEnferma("Maria", "Gonzalez", "23456789B");
        p2.setEdad(42); p2.setSexo("F"); p2.setContagioExterior(true); p2.setEnfermo(false); 
        p2.setPais("Australia"); p2.setFechaContagio(new Date(122, 5, 20)); p2.setEnfermedad(dengue);
        pacienteService.registrarPaciente(p2);
        log.append("- Paciente Maria Gonzalez registrada.\n");
        

        log.append("Datos de prueba cargados exitosamente.\n");
        refreshPacientesTable();
        refreshEnfermedadesTable();
        populateEnfermedadComboBox();
        JOptionPane.showMessageDialog(mainFrame, "Datos de prueba cargados.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }
}