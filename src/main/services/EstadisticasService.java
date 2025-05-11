package main.services;

import main.models.Enfermedad;
import main.models.Persona;
import main.models.PersonaEnferma;

import java.util.*;

public class EstadisticasService {
    private PacienteService pacienteService;
    private EnfermedadService enfermedadService;

    public EstadisticasService(PacienteService pacienteService, EnfermedadService enfermedadService) {
        this.pacienteService = pacienteService;
        this.enfermedadService = enfermedadService;
    }

    // Estad�sticas existentes
    public int getTotalPacientes() {
        return pacienteService.getAll().size();
    }
    
    public int getTotalEnfermedades() {
        return enfermedadService.getAll().size();
    }
    
    public Map<String, Integer> getPacientesPorSexo() {
        Map<String, Integer> stats = new HashMap<>();
        for (Persona p : pacienteService.getAll()) {
            String sexo = p.getSexo() != null ? p.getSexo() : "No especificado";
            stats.put(sexo, stats.getOrDefault(sexo, 0) + 1);
        }
        return stats;
    }
    
    public Map<String, Integer> getEnfermedadesPorTipo() {
        Map<String, Integer> stats = new HashMap<>();
        for (Enfermedad e : enfermedadService.getAll()) {
            String tipo = e.getViaTransmision() != null ? e.getViaTransmision() : "No especificado";
            stats.put(tipo, stats.getOrDefault(tipo, 0) + 1);
        }
        return stats;
    }
    
    // Nuevas estad�sticas
    
    // 1. Relaci�n entre contagiados en territorio nacional y en el extranjero
    public Map<String, Integer> getPacientesPorOrigen() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("Nacional", 0);
        stats.put("Extranjero", 0);
    
        for (Persona p : pacienteService.getAll()) {
            if (p instanceof PersonaEnferma) {
                PersonaEnferma pe = (PersonaEnferma) p;
                if (pe.isContagioExterior()) {
                    stats.put("Extranjero", stats.get("Extranjero") + 1);
                } else {
                    stats.put("Nacional", stats.get("Nacional") + 1);
                }
        }
    }
    return stats;
}
    
    // 2. Relaci�n entre casos por rango de edad y sexo
    public Map<String, Map<String, Integer>> getCasosPorEdadYSexo() {
        Map<String, Map<String, Integer>> stats = new HashMap<>();
        
        // Definir rangos de edad
        String[] rangosEdad = {"0-18", "19-30", "31-45", "46-60", "60+"};
        for (String rango : rangosEdad) {
            Map<String, Integer> porSexo = new HashMap<>();
            porSexo.put("Masculino", 0);
            porSexo.put("Femenino", 0);
            stats.put(rango, porSexo);
        }
        
        for (Persona p : pacienteService.getAll()) {
            int edad = p.getEdad();
            String rango;
            
            if (edad <= 18) rango = "0-18";
            else if (edad <= 30) rango = "19-30";
            else if (edad <= 45) rango = "31-45";
            else if (edad <= 60) rango = "46-60";
            else rango = "60+";
            
            String sexo = p.getSexo().equals("M") ? "Masculino" : "Femenino";
            Map<String, Integer> porSexo = stats.get(rango);
            porSexo.put(sexo, porSexo.get(sexo) + 1);
        }
        
        return stats;
    }
    
    // 3. Relaci�n entre curados, fallecidos, enfermos por enfermedad
    public Map<String, Map<String, Integer>> getEstadisticasPorEnfermedad() {
        Map<String, Map<String, Integer>> stats = new HashMap<>();
        
        for (Enfermedad e : enfermedadService.getAll()) {
            Map<String, Integer> estadisticas = new HashMap<>();
            estadisticas.put("Curados", e.getCurados());
            estadisticas.put("Fallecidos", e.getMuertos());
            estadisticas.put("Enfermos", e.getEnfermosActivos());
            
            stats.put(e.getNombreComun(), estadisticas);
        }
        
        return stats;
    }
    
    // 4. Lista de personas en vigilancia que son contactos de enfermos en el exterior
    public List<Persona> getContactosDeEnfermosExterior() {
    List<Persona> contactos = new ArrayList<>();
    
    if (pacienteService == null || pacienteService.getAll() == null) {
        return contactos; // Retorna lista vacía en lugar de null
    }
    
    for (Persona p : pacienteService.getAll()) {
        if (p instanceof PersonaEnferma) {
            PersonaEnferma pe = (PersonaEnferma) p;
            if (pe.isEnVigilancia() && pe.isContactoExterior()) {
                contactos.add(p);
            }
        }
    }
    
    return contactos;
    }

public Map<String, Integer> getEnfermosPorPaisYMes(String pais, int mes, int anio) {
    Map<String, Integer> stats = new HashMap<>();
    
    // Añadir logs para depuración
    System.out.println("Buscando enfermos en país: " + pais + ", mes: " + mes + ", año: " + anio);
    int encontrados = 0;
    
    for (Persona p : pacienteService.getAll()) {
        if (p instanceof PersonaEnferma) {
            PersonaEnferma pe = (PersonaEnferma) p;
            
            // Verificar que los datos necesarios no sean null
            if (pe.getPais() == null || pe.getFechaContagio() == null || pe.getEnfermedad() == null) {
                continue;
            }
            
            // Ajustar comparación de fechas (restar 1900 al año para comparación con Date)
            int yearToCompare = anio - 1900;
            int monthToCompare = mes - 1; // Los meses en Date van de 0-11
            
            
            if (pe.isEnfermo() && 
                pe.getPais().equalsIgnoreCase(pais) && 
                pe.getFechaContagio().getMonth() == monthToCompare && 
                pe.getFechaContagio().getYear() == yearToCompare) {
                
                encontrados++;
                String nombreEnfermedad = pe.getEnfermedad().getNombreComun() != null ? 
                    pe.getEnfermedad().getNombreComun() : "No especificada";
                
                stats.put(nombreEnfermedad, stats.getOrDefault(nombreEnfermedad, 0) + 1);
                System.out.println("¡Coincidencia encontrada! " + nombreEnfermedad);
            }
        }
    }
    
    System.out.println("Total de enfermos encontrados: " + encontrados);
    return stats;
}
}