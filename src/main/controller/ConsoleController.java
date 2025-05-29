package main.controller;

import java.util.Date;
import java.util.Scanner;
import java.util.List;
import java.util.Map;

import main.services.EnfermedadService;
import main.services.PacienteService;
import main.services.EstadisticasService;
import main.models.Persona;
import main.models.Enfermedad;
import main.models.PersonaEnferma;

public class ConsoleController {
    private Scanner scanner;
    private boolean running;
    
    // Add service references
    private EnfermedadService enfermedadService;
    private PacienteService pacienteService;
    private EstadisticasService estadisticasService;

    public ConsoleController(EstadisticasService estadisticasService, EnfermedadService enfermedadService, PacienteService pacienteService) {
        this.estadisticasService = estadisticasService;
        this.enfermedadService = enfermedadService;
        this.pacienteService = pacienteService;
        scanner = new Scanner(System.in);
        running = true;
    }

    public void start() {
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            processMainMenuChoice(choice);
        }
        scanner.close();
        System.out.println("Program terminated.");
    }

    public void displayMainMenu() {
    System.out.println("\n===== MAIN MENU =====");
    System.out.println("1. Manage Pacientes");
    System.out.println("2. Manage Enfermedades");
    System.out.println("3. Estadisticas");
    System.out.println("4. Cargar Datos de Prueba"); // Nueva opción
    System.out.println("0. Exit");
    System.out.println("=====================");
}

private void processMainMenuChoice(int choice) {
    switch (choice) {
        case 1: managePacientes(); break;
        case 2: manageEnfermedades(); break;
        case 3: viewEstadisticas(); break;
        case 4: loadTestData(); break; // Llamada al método de carga
        case 0: running = false; break;
        default: System.out.println("Invalid choice. Please try again.");
    }
}

    private void managePacientes() {
        boolean subMenuRunning = true;
        while (subMenuRunning) {
            System.out.println("\n===== PACIENTES =====");
            System.out.println("1. Create new paciente");
            System.out.println("2. View pacientes");
            System.out.println("3. Update paciente");
            System.out.println("4. Delete paciente");
            System.out.println("0. Back to main menu");
            
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1: createPaciente(); break;
                case 2: viewPacientes(); break;
                case 3: updatePaciente(); break;
                case 4: deletePaciente(); break;
                case 0: subMenuRunning = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void createPaciente() {
        System.out.println("\n===== CREATE PACIENTE =====");
        String nombre = getStringInput("Enter nombre: ");
        String apellido = getStringInput("Enter apellido: ");
        String documento = getStringInput("Enter documento: ");
        
        try {
            Persona nuevaPaciente = new Persona(nombre, apellido, documento);
            pacienteService.registrarPaciente(nuevaPaciente);
            System.out.println("Paciente created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    } 

    private void viewPacientes() {
        System.out.println("\n===== VIEW PACIENTES =====");
        List<Persona> pacientes = pacienteService.getAll();
        
        if (pacientes.isEmpty()) {
            System.out.println("No pacientes found.");
            return;
        }
        
        for (Persona p : pacientes) {
            System.out.println("Codigo: " + p.obtenerCodigo());
            System.out.println("Nombre: " + p.getNombre());
            System.out.println("Apellido: " + p.getApellidos());
            System.out.println("-------------------------");
        }
    }

    private void updatePaciente() {
        System.out.println("\n===== UPDATE PACIENTE =====");
        String codigo = getStringInput("Enter codigo del paciente: ");
        
        try {
            Persona paciente = null;
            for (Persona p : pacienteService.getAll()) {
                if (p.obtenerCodigo().equals(codigo)) {
                    paciente = p;
                    break;
                }
            }
            
            if (paciente == null) {
                System.out.println("Paciente not found!");
                return;
            }
            
            System.out.println("Current details:");
            System.out.println("Nombre: " + paciente.getNombre());
            System.out.println("Apellido: " + paciente.getApellidos());
            System.out.println("Documento: " + paciente.getCarnetIdentidad());
            
            String nombre = getStringInput("Enter new nombre (or press Enter to keep current): ");
            String apellido = getStringInput("Enter new apellido (or press Enter to keep current): ");
            String documento = getStringInput("Enter new documento (or press Enter to keep current): ");
            
            if (!nombre.isEmpty()) paciente.setNombre(nombre);
            if (!apellido.isEmpty()) paciente.setApellidos(apellido);
            if (!documento.isEmpty()) paciente.setCarnetIdentidad(documento);
            
            pacienteService.updatePaciente(paciente);
            System.out.println("Paciente updated successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deletePaciente() {
        System.out.println("\n===== DELETE PACIENTE =====");
        String codigo = getStringInput("Enter codigo del paciente: ");
        
        try {
            Persona paciente = null;
            for (Persona p : pacienteService.getAll()) {
                if (p.obtenerCodigo().equals(codigo)) {
                    paciente = p;
                    break;
                }
            }
            
            if (paciente == null) {
                System.out.println("Paciente not found!");
                return;
            }
            
            System.out.println("Details of paciente to delete:");
            System.out.println("Nombre: " + paciente.getNombre());
            System.out.println("Apellido: " + paciente.getApellidos());
            
            String confirm = getStringInput("Are you sure you want to delete this paciente? (y/n): ");
            if (confirm.equalsIgnoreCase("y")) {
                pacienteService.deletePaciente(codigo);
                System.out.println("Paciente deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void manageEnfermedades() {
        boolean subMenuRunning = true;
        while (subMenuRunning) {
            System.out.println("\n===== ENFERMEDADES =====");
            System.out.println("1. Create new enfermedad");
            System.out.println("2. View enfermedades");
            System.out.println("3. Update enfermedad");
            System.out.println("4. Delete enfermedad");
            System.out.println("0. Back to main menu");
            
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1: createEnfermedad(); break;
                case 2: viewEnfermedades(); break;
                case 3: updateEnfermedad(); break;
                case 4: deleteEnfermedad(); break;
                case 0: subMenuRunning = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void createEnfermedad() {
        System.out.println("\n===== CREATE ENFERMEDAD =====");
        String nombre = getStringInput("Enter nombre: ");
        String tipo = getStringInput("Enter tipo: ");
        String nombreCientifico = getStringInput("Enter nombre cientifico: ");
        
        try {
            Enfermedad nuevaEnfermedad = new Enfermedad(nombre, tipo, nombreCientifico);
            enfermedadService.registrarEnfermedad(nuevaEnfermedad);
            System.out.println("Enfermedad created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewEnfermedades() {
        System.out.println("\n===== VIEW ENFERMEDADES =====");
        List<Enfermedad> enfermedades = enfermedadService.getAll();
        
        if (enfermedades.isEmpty()) {
            System.out.println("No enfermedades found.");
            return;
        }
        
        for (Enfermedad e : enfermedades) {
            System.out.println("Codigo: " + e.obtenerCodigo());
            System.out.println("Nombre: " + e.getNombreComun());
            System.out.println("Tipo: " + e.getViaTransmision());
            System.out.println("-------------------------");
        }
    }

    private void updateEnfermedad() {
        System.out.println("\n===== UPDATE ENFERMEDAD =====");
        String codigo = getStringInput("Enter codigo de enfermedad: ");
        
        try {
            Enfermedad enfermedad = null;
            for (Enfermedad e : enfermedadService.getAll()) {
                if (e.obtenerCodigo().equals(codigo)) {
                    enfermedad = e;
                    break;
                }
            }
            
            if (enfermedad == null) {
                System.out.println("Enfermedad not found!");
                return;
            }
            
            System.out.println("Current details:");
            System.out.println("Nombre: " + enfermedad.getNombreComun());
            System.out.println("Tipo: " + enfermedad.getViaTransmision());
            
            String nombre = getStringInput("Enter new nombre (or press Enter to keep current): ");
            String tipo = getStringInput("Enter new tipo (or press Enter to keep current): ");
            
            if (!nombre.isEmpty()) enfermedad.setNombreComun(nombre);
            if (!tipo.isEmpty()) enfermedad.setViaTransmision(tipo);
            
            enfermedadService.actualizarEnfermedad(enfermedad);
            System.out.println("Enfermedad updated successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteEnfermedad() {
        System.out.println("\n===== DELETE ENFERMEDAD =====");
        String codigo = getStringInput("Enter codigo de enfermedad: ");
        
        try {
            Enfermedad enfermedad = null;
            for (Enfermedad e : enfermedadService.getAll()) {
                if (e.obtenerCodigo().equals(codigo)) {
                    enfermedad = e;
                    break;
                }
            }
            
            if (enfermedad == null) {
                System.out.println("Enfermedad not found!");
                return;
            }
            
            System.out.println("Details of enfermedad to delete:");
            System.out.println("Nombre: " + enfermedad.getNombreComun());
            System.out.println("Tipo: " + enfermedad.getViaTransmision());
            
            String confirm = getStringInput("Are you sure you want to delete this enfermedad? (y/n): ");
            if (confirm.equalsIgnoreCase("y")) {
                enfermedadService.eliminarEnfermedad(codigo);
                System.out.println("Enfermedad deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Helper methods for input handling
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número valido.");
            }
        }
    }

    private void viewEstadisticas() {
        boolean estadisticasMenuRunning = true;
        
        while (estadisticasMenuRunning) {
            System.out.println("\n===== MENU DE ESTADÍSTICAS =====");
            System.out.println("1. Estadisticas Generales");
            System.out.println("2. Pacientes por Sexo");
            System.out.println("3. Enfermedades por Tipo de Transmision");
            System.out.println("4. Contagios Nacionales vs. Extranjero");
            System.out.println("5. Casos por Rango de Edad y Sexo");
            System.out.println("6. Estadisticas por Enfermedad (curados/fallecidos/enfermos)");
            System.out.println("7. Contactos de Enfermos en el Exterior");
            System.out.println("8. Enfermos por Pais y Mes");
            System.out.println("9. Todas las Estadisticas");
            System.out.println("0. Volver al Menu Principal");
            System.out.println("=================================");
            
            int choice = getIntInput("Seleccione una opcion: ");
            
            switch (choice) {
                case 1:
                    showGeneralStats();
                    break;
                case 2:
                    showPatientsBySex();
                    break;
                case 3:
                    showDiseasesByType();
                    break;
                case 4:
                    showNationalVsForeignCases();
                    break;
                case 5:
                    showCasesByAgeAndSex();
                    break;
                case 6:
                    showStatsByDisease();
                    break;
                case 7:
                    showContactsOfForeignCases();
                    break;
                case 8:
                    showCasesByCountryAndMonth();
                    break;
                case 9:
                    showAllStats();
                    break;
                case 0:
                    estadisticasMenuRunning = false;
                    break;
                default:
                    System.out.println("Opcion no valida. Por favor intente de nuevo.");
            }
        }
    }


    private void showNationalVsForeignCases() {
        System.out.println("\n--- Relacion entre Contagios Nacionales y Extranjeros ---");
        Map<String, Integer> stats = estadisticasService.getPacientesPorOrigen();
        
        int totalNacional = stats.getOrDefault("Nacional", 0);
        int totalExtranjero = stats.getOrDefault("Extranjero", 0);
        int total = totalNacional + totalExtranjero;
        
        if (total == 0) {
            System.out.println("No hay datos para mostrar.");
            return;
        }
        
        System.out.println("Nacional: " + totalNacional + " casos (" + 
                           String.format("%.1f%%", (totalNacional * 100.0 / total)) + ")");
        System.out.println("Extranjero: " + totalExtranjero + " casos (" + 
                           String.format("%.1f%%", (totalExtranjero * 100.0 / total)) + ")");
        
        // Gráfico simple
        System.out.println("\nDistribucion Grafica:");
        displayBarChart(stats, "Origen de Contagios");
        
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    private void showCasesByAgeAndSex() {
        System.out.println("\n--- Casos por Rango de Edad y Sexo ---");
        Map<String, Map<String, Integer>> stats = estadisticasService.getCasosPorEdadYSexo();
        
        if (stats.isEmpty()) {
            System.out.println("No hay datos para mostrar.");
            return;
        }
        
        String[] rangos = {"0-18", "19-30", "31-45", "46-60", "60+"};
        
        System.out.println("Rango Edad    Masculino    Femenino    Total");
        System.out.println("------------------------------------------------");
        
        int totalMasculino = 0, totalFemenino = 0;
        
        for (String rango : rangos) {
            Map<String, Integer> porSexo = stats.get(rango);
            if (porSexo != null) {
                int masculino = porSexo.getOrDefault("Masculino", 0);
                int femenino = porSexo.getOrDefault("Femenino", 0);
                int total = masculino + femenino;
                
                totalMasculino += masculino;
                totalFemenino += femenino;
                
                System.out.printf("%-13s %-12d %-10d %d\n", rango, masculino, femenino, total);
            }
        }
        
        System.out.println("------------------------------------------------");
        System.out.printf("%-13s %-12d %-10d %d\n", "Total", totalMasculino, totalFemenino, (totalMasculino + totalFemenino));
        
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    private void showStatsByDisease() {
        System.out.println("\n--- Estadisticas por Enfermedad ---");
        Map<String, Map<String, Integer>> stats = estadisticasService.getEstadisticasPorEnfermedad();
        
        if (stats.isEmpty()) {
            System.out.println("No hay datos para mostrar.");
            return;
        }
        
        System.out.println("Enfermedad            Curados    Fallecidos    Enfermos    Total");
        System.out.println("-------------------------------------------------------------------");
        
        for (Map.Entry<String, Map<String, Integer>> entry : stats.entrySet()) {
            String enfermedad = entry.getKey();
            Map<String, Integer> valores = entry.getValue();
            
            int curados = valores.getOrDefault("Curados", 0);
            int fallecidos = valores.getOrDefault("Fallecidos", 0);
            int enfermos = valores.getOrDefault("Enfermos", 0);
            int total = curados + fallecidos + enfermos;
            
            System.out.printf("%-22s %-10d %-12d %-10d %d\n", 
                            enfermedad.length() > 20 ? enfermedad.substring(0, 20) + ".." : enfermedad, 
                            curados, fallecidos, enfermos, total);
        }
        
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    private void showContactsOfForeignCases() {
        System.out.println("\n--- Personas en Vigilancia (Contactos de Enfermos en el Exterior) ---");
        List<Persona> contactos = estadisticasService.getContactosDeEnfermosExterior();
    
        if (contactos.isEmpty()) {
        System.out.println("No hay personas en vigilancia que sean contactos de enfermos en el exterior.");
        return;
        }
    
        System.out.println("Nombre                 Apellido              Edad    Sexo    Documento");
        System.out.println("------------------------------------------------------------------------");
    
        for (Persona p : contactos) {
        // El error probablemente está aquí al intentar acceder a propiedades
        // que podrían ser null, como nombre, apellidos, sexo o carnetIdentidad
        
        // Verificar primero que no sean null
        String nombre = p.getNombre() != null ? p.getNombre() : "N/A";
        String apellidos = p.getApellidos() != null ? p.getApellidos() : "N/A";
        String sexo = p.getSexo() != null ? p.getSexo() : "N/A";
        String documento = p.getCarnetIdentidad() != null ? p.getCarnetIdentidad() : "N/A";
        
        System.out.printf("%-22s %-22s %-7d %-7s %s\n", 
                      nombre.length() > 20 ? nombre.substring(0, 20) + ".." : nombre,
                      apellidos.length() > 20 ? apellidos.substring(0, 20) + ".." : apellidos,
                      p.getEdad(), sexo, documento);
        }
    
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    private void showCasesByCountryAndMonth() {
        System.out.println("\n--- Enfermos por Pais y Mes ---");
        
        String pais = getStringInput("Ingrese el pais: ");
        int mes = getIntInput("Ingrese el mes (1-12): ");
        int anio = getIntInput("Ingrese el año (ej. 2023): ");
        
        if (mes < 1 || mes > 12) {
            System.out.println("Mes invalido. Debe estar entre 1 y 12.");
            return;
        }
            System.out.println("Buscando enfermos en " + pais + " durante " + 
                      getMesNombre(mes) + " de " + anio);
                      
        Map<String, Integer> stats = estadisticasService.getEnfermosPorPaisYMes(pais, mes, anio);
        
        if (stats.isEmpty()) {
            System.out.println("No se encontraron enfermos en " + pais + " durante " + getMesNombre(mes) + " de " + anio);
            return;
        }
        
        int total = 0;
        System.out.println("\nEnfermos en " + pais + " durante " + getMesNombre(mes) + " de " + anio + ":");
        System.out.println("Enfermedad                     Casos");
        System.out.println("----------------------------------");
        
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            total += entry.getValue();
            System.out.printf("%-30s %d\n", 
                           entry.getKey().length() > 28 ? entry.getKey().substring(0, 28) + ".." : entry.getKey(), 
                           entry.getValue());
        }
        
        System.out.println("----------------------------------");
        System.out.printf("%-30s %d\n", "Total", total);
        
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    private String getMesNombre(int mes) {
        String[] meses = {"enero", "febrero", "marzo", "abril", "mayo", "junio", 
                          "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};
        return meses[mes - 1];
    }

    private void showAllStats() {
        showGeneralStats();
        showPatientsBySex();
        showDiseasesByType();
        showNationalVsForeignCases();
        showCasesByAgeAndSex();
        showStatsByDisease();
        showContactsOfForeignCases();
        
        System.out.println("\nNota: Las estadisticas por pais y mes requieren informacion especifica.");
        System.out.println("Utilice la opcion 8 del menú para consultar esos datos.");
        
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    private void displayBarChart(Map<String, Integer> data, String title) {
        int maxValue = 0;
        for (Integer value : data.values()) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        
        int chartWidth = 40;
        
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            System.out.printf("%-15s: ", entry.getKey());
            
            int barLength = maxValue > 0 ? chartWidth * entry.getValue() / maxValue : 0;
            for (int i = 0; i < barLength; i++) {
                System.out.print("█");
            }
            System.out.println(" " + entry.getValue());
        }
    }
    private void showGeneralStats() {
        System.out.println("\n--- Estadisticas Generales ---");
        System.out.println("Total de Pacientes: " + estadisticasService.getTotalPacientes());
        System.out.println("Total de Enfermedades: " + estadisticasService.getTotalEnfermedades());
        
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    private void showPatientsBySex() {
        System.out.println("\n--- Pacientes por Sexo ---");
        Map<String, Integer> pacientesPorSexo = estadisticasService.getPacientesPorSexo();
        if (pacientesPorSexo.isEmpty()) {
            System.out.println("No hay datos para mostrar.");
        } else {
            for (Map.Entry<String, Integer> entry : pacientesPorSexo.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
        
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    private void showDiseasesByType() {
        System.out.println("\n--- Enfermedades por Tipo de Transmision ---");
        Map<String, Integer> enfermedadesPorTipo = estadisticasService.getEnfermedadesPorTipo();
        if (enfermedadesPorTipo.isEmpty()) {
            System.out.println("No hay datos para mostrar.");
        } else {
            for (Map.Entry<String, Integer> entry : enfermedadesPorTipo.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
        
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

private void loadTestData() {
    System.out.println("Cargando datos de prueba...");
    
    // Crear enfermedades
    Enfermedad covid = new Enfermedad("COVID-19", "Respiratoria", "SARS-CoV-2");
    covid.setCurados(120);
    covid.setMuertos(15);
    covid.setEnfermosActivos(45);
    enfermedadService.registrarEnfermedad(covid);
    
    Enfermedad dengue = new Enfermedad("Dengue", "Vectorial", "DENV");
    dengue.setCurados(78);
    dengue.setMuertos(3);
    dengue.setEnfermosActivos(22);
    enfermedadService.registrarEnfermedad(dengue);
    
    Enfermedad influenza = new Enfermedad("Influenza", "Respiratoria", "H1N1");
    influenza.setCurados(230);
    influenza.setMuertos(8);
    influenza.setEnfermosActivos(65);
    enfermedadService.registrarEnfermedad(influenza);
    
    // Crear pacientes
    // Paciente 1 - Contagio nacional, masculino joven
    PersonaEnferma p1 = new PersonaEnferma("Carlos", "Rodríguez", "12345678A");
    p1.setEdad(25);
    p1.setSexo("M");
    p1.setContagioExterior(false);
    p1.setEnfermo(true);
    p1.setPais("Cuba");
    p1.setFechaContagio(new Date(122, 4, 15)); // 15-mayo-2022
    p1.setEnfermedad(covid);
    pacienteService.registrarPaciente(p1);
    
    // Paciente 2 - Contagio extranjero, femenino adulto
    PersonaEnferma p2 = new PersonaEnferma("María", "González", "23456789B");
    p2.setEdad(42);
    p2.setSexo("F");
    p2.setContagioExterior(true);
    p2.setEnfermo(false);
    p2.setPais("España");
    p2.setFechaContagio(new Date(122, 5, 20)); // 20-junio-2022
    p2.setEnfermedad(dengue);
    pacienteService.registrarPaciente(p2);
    
    // Paciente 3 - Contagio nacional, masculino mayor
    PersonaEnferma p3 = new PersonaEnferma("Antonio", "Pérez", "34567890C");
    p3.setEdad(67);
    p3.setSexo("M");
    p3.setContagioExterior(false);
    p3.setEnfermo(true);
    p3.setPais("Cuba");
    p3.setFechaContagio(new Date(122, 3, 5)); // 5-abril-2022
    p3.setEnfermedad(covid);
    pacienteService.registrarPaciente(p3);
    
    // Paciente 4 - Contagio extranjero, femenino joven
    PersonaEnferma p4 = new PersonaEnferma("Laura", "Sánchez", "45678901D");
    p4.setEdad(19);
    p4.setSexo("F");
    p4.setContagioExterior(true);
    p4.setEnfermo(false);
    p4.setPais("México");
    p4.setFechaContagio(new Date(122, 7, 10)); // 10-agosto-2022
    p4.setEnfermedad(influenza);
    pacienteService.registrarPaciente(p4);
    
    // Paciente 5 - Contacto en vigilancia
    PersonaEnferma p5 = new PersonaEnferma("Miguel", "Fernández", "56789012E");
    p5.setEdad(33);
    p5.setSexo("M");
    p5.setEnVigilancia(true);
    p5.setContactoExterior(true);
    p5.setPais("Cuba");
    pacienteService.registrarPaciente(p5);
    
    // Paciente 6 - Contagio extranjero, masculino adulto
    PersonaEnferma p6 = new PersonaEnferma("Elena", "Torres", "67890123F");
    p6.setEdad(55);
    p6.setSexo("F");
    p6.setContagioExterior(true);
    p6.setEnfermo(true);
    p6.setPais("Estados Unidos");
    p6.setFechaContagio(new Date(122, 8, 25)); // 25-septiembre-2022
    p6.setEnfermedad(covid);
    pacienteService.registrarPaciente(p6);
    
    // Paciente 7 - Contagio nacional, femenino mayor
    PersonaEnferma p7 = new PersonaEnferma("Roberto", "Díaz", "78901234G");
    p7.setEdad(72);
    p7.setSexo("M");
    p7.setContagioExterior(false);
    p7.setEnfermo(false);
    p7.setPais("Cuba");
    p7.setFechaContagio(new Date(122, 2, 12)); // 12-marzo-2022
    p7.setEnfermedad(dengue);
    pacienteService.registrarPaciente(p7);
    
    // Paciente 8 - Contacto en vigilancia
    PersonaEnferma p8 = new PersonaEnferma("Sofía", "Martínez", "89012345H");
    p8.setEdad(28);
    p8.setSexo("F");
    p8.setEnVigilancia(true);
    p8.setContactoExterior(true);
    p8.setPais("Cuba");
    pacienteService.registrarPaciente(p8);
    
    // Paciente 9 - Extranjero de un país concreto para prueba de estadística específica
    PersonaEnferma p9 = new PersonaEnferma("Juan", "López", "90123456I");
    p9.setEdad(47);
    p9.setSexo("M");
    p9.setContagioExterior(true);
    p9.setEnfermo(true);
    p9.setPais("Mexico");
    p9.setFechaContagio(new Date(122, 5, 5)); // 5-junio-2022
    p9.setEnfermedad(influenza);
    pacienteService.registrarPaciente(p9);
    
    // Paciente 10 - Extranjero del mismo país pero diferente mes
    PersonaEnferma p10 = new PersonaEnferma("Patricia", "Ruiz", "01234567J");
    p10.setEdad(36);
    p10.setSexo("F");
    p10.setContagioExterior(true);
    p10.setEnfermo(true);
    p10.setPais("México");
    p10.setFechaContagio(new Date(122, 6, 15)); // 15-julio-2022
    p10.setEnfermedad(covid);
    pacienteService.registrarPaciente(p10);
    
    System.out.println("Datos de prueba cargados exitosamente:");
    System.out.println("- 3 enfermedades registradas");
    System.out.println("- 10 pacientes registrados");
}



}