package main.services;

import main.models.Enfermedad;
import main.models.Persona;
import main.models.PersonaEnferma; 

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;


public class EstadisticasService {

    private PacienteService pacienteService;
    private EnfermedadService enfermedadService;

    public EstadisticasService(PacienteService pacienteService, EnfermedadService enfermedadService) {
        this.pacienteService = pacienteService;
        this.enfermedadService = enfermedadService;
    }


    public int getTotalPacientes() {
        if (pacienteService == null || pacienteService.getAll() == null) {
            return 0;
        }
        return pacienteService.getAll().size();
    }


    public int getTotalEnfermedades() {
        if (enfermedadService == null || enfermedadService.getAll() == null) {
            return 0;
        }
        return enfermedadService.getAll().size();
    }

    public Map<String, Integer> getPacientesPorSexo() {
        Map<String, Integer> porSexo = new HashMap<>();
        porSexo.put("Masculino", 0);
        porSexo.put("Femenino", 0);
        porSexo.put("Otro", 0); 
        if (pacienteService != null && pacienteService.getAll() != null) {
            for (Persona p : pacienteService.getAll()) {
                String sexo = p.getSexo(); // Assuming getSexo() returns "M", "F", or other
                if ("M".equalsIgnoreCase(sexo) || "Masculino".equalsIgnoreCase(sexo)) {
                    porSexo.put("Masculino", porSexo.get("Masculino") + 1);
                } else if ("F".equalsIgnoreCase(sexo) || "Femenino".equalsIgnoreCase(sexo)) {
                    porSexo.put("Femenino", porSexo.get("Femenino") + 1);
                } else {
                    porSexo.put("Otro", porSexo.get("Otro") + 1);
                }
            }
        }
        return porSexo;
    }

    public Map<String, Integer> getEnfermedadesPorTipo() {
        Map<String, Integer> porTipo = new HashMap<>();
        if (enfermedadService != null && enfermedadService.getAll() != null) {
            for (Enfermedad e : enfermedadService.getAll()) {
                String tipo = e.getViaTransmision(); // Assuming this is the field for type
                porTipo.put(tipo, porTipo.getOrDefault(tipo, 0) + 1);
            }
        }
        return porTipo;
    }

    public Map<String, Integer> getPacientesPorOrigen() {
        Map<String, Integer> porOrigen = new HashMap<>();
        porOrigen.put("Nacional", 0);
        porOrigen.put("Extranjero", 0);

        if (pacienteService != null && pacienteService.getAll() != null) {
            for (Persona p : pacienteService.getAll()) {
                if (p instanceof PersonaEnferma) {
                    PersonaEnferma pe = (PersonaEnferma) p;
                    if (pe.isContagioExterior()) {
                        porOrigen.put("Extranjero", porOrigen.get("Extranjero") + 1);
                    } else {
                        porOrigen.put("Nacional", porOrigen.get("Nacional") + 1);
                    }
                }
            }
        }
        return porOrigen;
    }

    public Map<String, Map<String, Integer>> getCasosPorEdadYSexo() {
        Map<String, Map<String, Integer>> casos = new HashMap<>();

        String[] rangosEdad = {"0-18", "19-30", "31-45", "46-60", "61+"};
        for (String rango : rangosEdad) {
            Map<String, Integer> sexoMap = new HashMap<>();
            sexoMap.put("Masculino", 0);
            sexoMap.put("Femenino", 0);
            sexoMap.put("Otro", 0);
            casos.put(rango, sexoMap);
        }

        if (pacienteService != null && pacienteService.getAll() != null) {
            for (Persona p : pacienteService.getAll()) {
                int edad = p.getEdad();
                String rangoActual = "";
                if (edad <= 18) rangoActual = "0-18";
                else if (edad <= 30) rangoActual = "19-30";
                else if (edad <= 45) rangoActual = "31-45";
                else if (edad <= 60) rangoActual = "46-60";
                else rangoActual = "61+";

                Map<String, Integer> sexoMap = casos.get(rangoActual);
                String sexo = p.getSexo();
                if ("M".equalsIgnoreCase(sexo) || "Masculino".equalsIgnoreCase(sexo)) {
                    sexoMap.put("Masculino", sexoMap.get("Masculino") + 1);
                } else if ("F".equalsIgnoreCase(sexo) || "Femenino".equalsIgnoreCase(sexo)) {
                    sexoMap.put("Femenino", sexoMap.get("Femenino") + 1);
                } else {
                    sexoMap.put("Otro", sexoMap.get("Otro") + 1);
                }
            }
        }
        return casos;
    }

    public Map<String, Map<String, Integer>> getEstadisticasPorEnfermedad() {
        Map<String, Map<String, Integer>> estadisticas = new HashMap<>();
        if (enfermedadService != null && enfermedadService.getAll() != null) {
            for (Enfermedad e : enfermedadService.getAll()) {
                Map<String, Integer> counts = new HashMap<>();
                counts.put("Curados", e.getCurados());
                counts.put("Fallecidos", e.getMuertos());
                counts.put("Enfermos", e.getEnfermosActivos());
                estadisticas.put(e.getNombreComun(), counts);
            }
        }
        return estadisticas;
    }


    public List<Persona> getContactosDeEnfermosExterior() {
        List<Persona> contactos = new ArrayList<>();
        if (pacienteService != null && pacienteService.getAll() != null) {
            for (Persona p : pacienteService.getAll()) {
                if (p instanceof PersonaEnferma) {
                    PersonaEnferma pe = (PersonaEnferma) p;
                    // TODO
                    // I need to define how to identify a "contacto en vigilancia".
                    // For example, if they were marked as contagioExterior and are currently enfermo.
                    // Or if there's a specific field like `pe.isContactoEnVigilancia()`.
                    if (pe.isContagioExterior() && pe.isEnfermo()) {
                        contactos.add(pe);
                    }
                }
            }
        }
        return contactos;
    }

    public Map<String, Integer> getEnfermosPorPaisYMes(String pais, int mes, int anio) {
        Map<String, Integer> enfermosCount = new HashMap<>();
        if (pacienteService != null && pacienteService.getAll() != null) {
            Calendar cal = Calendar.getInstance();
            for (Persona p : pacienteService.getAll()) {
                if (p instanceof PersonaEnferma) {
                    PersonaEnferma pe = (PersonaEnferma) p;
                    if (pe.getPais() != null && pe.getPais().equalsIgnoreCase(pais) && pe.getFechaContagio() != null) {
                        cal.setTime(pe.getFechaContagio());
                        int pacienteMes = cal.get(Calendar.MONTH) + 1;
                        int pacienteAnio = cal.get(Calendar.YEAR);

                        if (pacienteMes == mes && pacienteAnio == anio && pe.getEnfermedad() != null) {
                            String nombreEnfermedad = pe.getEnfermedad().getNombreComun();
                            enfermosCount.put(nombreEnfermedad, enfermosCount.getOrDefault(nombreEnfermedad, 0) + 1);
                        }
                    }
                }
            }
        }
        return enfermosCount;
    }
}