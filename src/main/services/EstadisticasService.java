package main.services;

import main.models.Enfermedad;
import main.models.Persona;
import main.models.PersonaEnferma; // Assuming PersonaEnferma extends Persona and has specific fields

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

    /**
     * Returns the total number of registered patients.
     */
    public int getTotalPacientes() {
        if (pacienteService == null || pacienteService.getAll() == null) {
            return 0;
        }
        return pacienteService.getAll().size();
    }

    /**
     * Returns the total number of registered diseases.
     */
    public int getTotalEnfermedades() {
        if (enfermedadService == null || enfermedadService.getAll() == null) {
            return 0;
        }
        return enfermedadService.getAll().size();
    }

    /**
     * Returns a map of patients counted by sex (e.g., "Masculino": count, "Femenino": count).
     */
    public Map<String, Integer> getPacientesPorSexo() {
        Map<String, Integer> porSexo = new HashMap<>();
        porSexo.put("Masculino", 0);
        porSexo.put("Femenino", 0);
        porSexo.put("Otro", 0); // Or however you handle other/unspecified

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

    /**
     * Returns a map of diseases counted by their type of transmission.
     * (e.g., "Respiratoria": count, "Vectorial": count)
     */
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

    /**
     * Returns a map of patients counted by their origin of contagion.
     * (e.g., "Nacional": count, "Extranjero": count)
     * This assumes PersonaEnferma has a boolean isContagioExterior()
     */
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

    /**
     * Returns a map where keys are age ranges (e.g., "0-18", "19-30")
     * and values are another map with sex ("Masculino", "Femenino") to count.
     * Example: {"0-18": {"Masculino": 5, "Femenino": 3}, ...}
     */
    public Map<String, Map<String, Integer>> getCasosPorEdadYSexo() {
        Map<String, Map<String, Integer>> casos = new HashMap<>();
        // Define age ranges
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

    /**
     * Returns a map where keys are disease names (or codes)
     * and values are another map with status ("Curados", "Fallecidos", "Enfermos") to count.
     * This uses the curados, muertos, enfermosActivos fields from Enfermedad model.
     * Example: {"COVID-19": {"Curados": 10, "Fallecidos": 2, "Enfermos": 5}, ...}
     */
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

    /**
     * Returns a list of Persona objects who are contacts of patients infected abroad
     * and are currently under surveillance.
     * This is a simplified interpretation. You might need a more specific "isContacto" or "isVigilancia" field.
     * For this example, it will list patients who had contagioExterior = true and are currently marked as enfermo = true.
     * A more accurate implementation would depend on how "contacto en vigilancia" is defined in your system.
     */
    public List<Persona> getContactosDeEnfermosExterior() {
        List<Persona> contactos = new ArrayList<>();
        if (pacienteService != null && pacienteService.getAll() != null) {
            for (Persona p : pacienteService.getAll()) {
                if (p instanceof PersonaEnferma) {
                    PersonaEnferma pe = (PersonaEnferma) p;
                    // This is a placeholder logic.
                    // You need to define how to identify a "contacto en vigilancia".
                    // For example, if they were marked as contagioExterior and are currently enfermo.
                    // Or if there's a specific field like `pe.isContactoEnVigilancia()`.
                    if (pe.isContagioExterior() && pe.isEnfermo()) { // Example: if they got sick from abroad
                        contactos.add(pe);
                    }
                }
            }
        }
        return contactos;
    }

    /**
     * Returns a map of diseases and their counts for a specific country, month, and year.
     * The key could be the disease name, and the value the number of cases.
     * This requires PersonaEnferma to have a fechaContagio (Date) and pais.
     * Example: {"COVID-19": 5, "Dengue": 2} for "Cuba", month 5, year 2023.
     */
    public Map<String, Integer> getEnfermosPorPaisYMes(String pais, int mes, int anio) {
        Map<String, Integer> enfermosCount = new HashMap<>();
        if (pacienteService != null && pacienteService.getAll() != null) {
            Calendar cal = Calendar.getInstance();
            for (Persona p : pacienteService.getAll()) {
                if (p instanceof PersonaEnferma) {
                    PersonaEnferma pe = (PersonaEnferma) p;
                    if (pe.getPais() != null && pe.getPais().equalsIgnoreCase(pais) && pe.getFechaContagio() != null) {
                        cal.setTime(pe.getFechaContagio());
                        int pacienteMes = cal.get(Calendar.MONTH) + 1; // Calendar.MONTH is 0-indexed
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